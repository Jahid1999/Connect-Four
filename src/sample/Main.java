package sample;

import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main extends Application {

    private static final int tile_size = 100;
    private static final int columns = 7;
    private static final int rows = 6;

    private boolean redMove = true;
    private boolean isGameEnded = false;
    private Disc[][] grid = new Disc[columns][rows];

    private Pane discRoot = new Pane();
    Pane left = new Pane();


    private Shape makeGrid() {
        Shape board = new Rectangle((columns + 1) * tile_size, (rows + 1) * tile_size);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                Circle circle = new Circle(tile_size / 2);
                circle.setCenterX(tile_size / 2);
                circle.setCenterY(tile_size / 2);
                circle.setTranslateX(j * (tile_size + 5) + tile_size / 4);
                circle.setTranslateY(i * (tile_size + 5) + tile_size / 4);

                board = Shape.subtract(board, circle);
            }
        }

        Light.Distant light = new Light.Distant();
        light.setAzimuth(45.0);
        light.setElevation(25.0);

        Lighting lighting = new Lighting();
        lighting.setLight(light);
        lighting.setSurfaceScale(5.0);

        board.setFill(Color.LIGHTBLUE);
        board.setEffect(lighting);

        return board;
    }

    private List<Rectangle> makeColumns() {
        List<Rectangle> list = new ArrayList<>();

        for (int i = 0; i < columns; i++) {
            Rectangle rect = new Rectangle(tile_size, (rows + 1) * tile_size);
            rect.setTranslateX(i * (tile_size + 5) + tile_size / 4);
            rect.setFill(Color.TRANSPARENT);

            rect.setOnMouseEntered(e -> rect.setFill(Color.rgb(200, 200, 50, 0.3)));
            rect.setOnMouseExited(e -> rect.setFill(Color.TRANSPARENT));

            final int column = i;
            rect.setOnMouseClicked(e -> placeDisc(new Disc(redMove), column));

            list.add(rect);
        }

        return list;
    }

    private void placeDisc(Disc disc, int column) {
        int row = rows - 1;
        do {
            if (!getDisc(column, row).isPresent())
                break;

            row--;
        } while (row >= 0);

        if (row < 0)
            return;

        grid[column][row] = disc;
        discRoot.getChildren().add(disc);
        disc.setTranslateX(column * (tile_size + 5) + tile_size / 4);

        final int currentRow = row;

        TranslateTransition animation = new TranslateTransition(Duration.seconds(0.3), disc);
        animation.setToY(row * (tile_size + 5) + tile_size / 4);
        animation.setOnFinished(e -> {
            if (gameEnded(column, currentRow)) {
                gameOver();
            }

            redMove = !redMove;
        });
        animation.play();
        if(!isGameEnded)
        {
            Circle turn = new Circle(tile_size / 2, redMove ? Color.YELLOW : Color.RED);
            turn.setCenterX(tile_size);
            turn.setCenterY(rows*tile_size / 2);

            Button turnText = new Button(redMove? "AI's Turn" : "Your Turn");
            turnText.setFont(new Font(22));
            turnText.setStyle("-fx-background-color: #ffffff");
            turnText.setMinWidth(2*tile_size);
            turnText.setLayoutY((rows+1)*tile_size / 2 + 10);

            left.getChildren().add(turn);
            left.getChildren().add(turnText);
        }
    }

    private boolean gameEnded(int column, int row) {
        List<Point2D> vertical = IntStream.rangeClosed(row - 3, row + 3)
                .mapToObj(r -> new Point2D(column, r))
                .collect(Collectors.toList());

        List<Point2D> horizontal = IntStream.rangeClosed(column - 3, column + 3)
                .mapToObj(c -> new Point2D(c, row))
                .collect(Collectors.toList());

        Point2D topLeft = new Point2D(column - 3, row - 3);
        List<Point2D> diagonal1 = IntStream.rangeClosed(0, 6)
                .mapToObj(i -> topLeft.add(i, i))
                .collect(Collectors.toList());

        Point2D botLeft = new Point2D(column - 3, row + 3);
        List<Point2D> diagonal2 = IntStream.rangeClosed(0, 6)
                .mapToObj(i -> botLeft.add(i, -i))
                .collect(Collectors.toList());

        return checkRange(vertical) || checkRange(horizontal)
                || checkRange(diagonal1) || checkRange(diagonal2);
    }

    private boolean checkRange(List<Point2D> points) {
        int chain = 0;

        for (Point2D p : points) {
            int column = (int) p.getX();
            int row = (int) p.getY();

            Disc disc = getDisc(column, row).orElse(new Disc(!redMove));
            if (disc.red == redMove) {
                chain++;
                if (chain == 4) {
                    return true;
                }
            } else {
                chain = 0;
            }
        }

        return false;
    }

    private void gameOver() {
        isGameEnded = true;
        Pane ended = new Pane();
        ended.setPrefSize(2*tile_size, 4* tile_size);
        ended.setLayoutY((rows-2)*tile_size / 2);

        Button winner = new Button("<Winner>");
        winner.setFont(new Font(22));
        winner.setStyle("-fx-background-color: #b09d21");
        winner.setMinWidth(2*tile_size);
        winner.setLayoutY((rows-2)*tile_size / 2);

        Circle turn = new Circle(tile_size / 2, redMove ? Color.RED : Color.YELLOW);
        turn.setCenterX(tile_size);
        turn.setCenterY(rows*tile_size / 2);

        Button turnText = new Button(redMove? "You" : "AI");
        turnText.setFont(new Font(22));
        turnText.setStyle("-fx-background-color: #ffffff");
        turnText.setMinWidth(2*tile_size);
        turnText.setLayoutY((rows+1)*tile_size / 2 + 10);

        left.getChildren().add(winner);
        left.getChildren().add(turn);
        left.getChildren().add(turnText);
    }

    private Optional<Disc> getDisc(int column, int row) {
        if (column < 0 || column >= columns
                || row < 0 || row >= rows)
            return Optional.empty();

        return Optional.ofNullable(grid[column][row]);
    }

    private static class Disc extends Circle {
        private final boolean red;
        public Disc(boolean red) {
            super(tile_size / 2, red ? Color.RED : Color.YELLOW);
            this.red = red;

            setCenterX(tile_size / 2);
            setCenterY(tile_size / 2);
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        GridPane root = new GridPane();

        left.setPrefSize(2*tile_size, columns* tile_size);
        Circle turn = new Circle(tile_size / 2, redMove ? Color.RED : Color.YELLOW);
        turn.setCenterX(tile_size);
        turn.setCenterY(rows*tile_size / 2);

        VBox vbox = new VBox();
        vbox.setFillWidth(true);

        Button btn = new Button("Connect Four");
        btn.setFont(new Font(22));
        btn.setStyle("-fx-background-color: #ab7cbf");
        btn.setMinWidth(2*tile_size);
        vbox.getChildren().add(btn);

        Button turnText = new Button("Your Turn");
        turnText.setFont(new Font(22));
        turnText.setStyle("-fx-background-color: #ffffff");
        turnText.setMinWidth(2*tile_size);
        turnText.setLayoutY((rows+1)*tile_size / 2 + 10);


        left.getChildren().add(vbox);
        left.getChildren().add(turn);
        left.getChildren().add(turnText);

        Pane right = new Pane();
        right.getChildren().add(discRoot);

        Shape gridShape = makeGrid();
        right.getChildren().add(gridShape);
        right.getChildren().addAll(makeColumns());

        root.add(left,0,1);
        root.add(right,1,1);


        primaryStage.setTitle("Connect Four");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
