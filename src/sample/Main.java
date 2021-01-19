package sample;

import miniMaxPackage.Minimax;
import javafx.animation.*;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
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

public class Main extends Application {

    private static final int tile_size = 100;
    private static final int columns = 7;
    private static final int rows = 6;
    int human =1;
    int ai=2;

    private boolean redMove = true;
    private boolean isGameEnded = false;
    private Disc[][] grid = new Disc[columns][rows];
    public int[][] board = new int [rows][columns];

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

    public boolean winning (int [][] board, int player)
    {
        for(int i=0; i<3; i++)
        {
            for(int j=0; j<4; j++)
            {
                if(board[i][j]==player&&board[i+1][j+1]==player&&board[i+2][j+2]==player&&board[i+3][j+3]==player)
                {
                    return true;
                }
            }
        }

        for(int i=3; i<6; i++)
        {
            for(int j=0; j<4; j++)
            {
                if(board[i][j]==player&&board[i-1][j+1]==player&&board[i-2][j+2]==player&&board[i-3][j+3]==player)
                {
                    return true;
                }
            }
        }

        for(int i=0; i<6; i++)
        {
            for(int j=0; j<4; j++)
            {
                if(board[i][j]==player&&board[i][j+1]==player&&board[i][j+2]==player&&board[i][j+3]==player)
                {
                    return true;
                }
            }
        }

        for(int i=0; i<3; i++)
        {
            for(int j=0; j<7; j++)
            {
                if(board[i][j]==player&&board[i+1][j]==player&&board[i+2][j]==player&&board[i+3][j]==player)
                {
                    return true;
                }
            }
        }

        return false;
    }

    public int calUIRow (int col)
    {
        int row = rows - 1;
        do {
            if (!getDisc(col, row).isPresent())
                break;

            row--;
        } while (row >= 0);

        if (row < 0)
            return -1;

        return row;
    }

    public boolean isValidCol (int col)
    {
        for (int i=0; i<rows; i++)
        {
            if(board[i][col]==0)
            {
                return true;
            }
        }

        return false;
    }

    public void checkWin ()
    {
        if(winning(board, human))
        {
            gameOver(human);
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Congratulations! You have won!");

            Optional<ButtonType> result = alert.showAndWait();
            if(!result.isPresent()||result.get() == ButtonType.OK||result.get() == ButtonType.CANCEL)
            {
                System.exit(0);
            }
        }
        if(winning(board, ai))
        {
            gameOver(ai);
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "OOPS! You have lost!");

            Optional<ButtonType> result = alert.showAndWait();
            if(!result.isPresent()||result.get() == ButtonType.OK||result.get() == ButtonType.CANCEL)
            {
                System.exit(0);
            }
        }
    }

    TranslateTransition a1;
    TranslateTransition a2;

    private void placeDisc(Disc disc, int column) {

        int row = calUIRow(column);

        if(redMove)
        {
            grid[column][row] = disc;
            board[5-row][column] = human;
            //System.out.println("-------------" + row);
        }

        discRoot.getChildren().add(disc);
        disc.setTranslateX(column * (tile_size + 5) + tile_size / 4);

        final int currentRow = row;
        TranslateTransition animation = new TranslateTransition(Duration.seconds(1), disc);
        animation.setToY((row * (tile_size + 5) + tile_size / 4)+1.5*tile_size);
        animation.setOnFinished(e -> {

        });

        if (redMove)
        {
            a1 = animation;
            a2 = new TranslateTransition(Duration.seconds(0));
        }
        else
        {
            a2 =animation;
        }

        //animation.play();

        SequentialTransition st = new SequentialTransition(disc, a1, new PauseTransition(Duration.seconds(1)), a2);

        st.play();

        checkWin();

        if(!isGameEnded)
        {
            Circle turn = new Circle(tile_size, rows*tile_size / 2,tile_size/2, Color.YELLOW);
            FadeTransition t = new FadeTransition(Duration.seconds(5), turn);
            t.setFromValue(10);
            t.setToValue(0);
            t.play();
            left.getChildren().add(turn);

            Button turnText = new Button("AI's Turn");
            turnText.setFont(new Font(22));
            turnText.setStyle("-fx-background-color: #ffffff");
            turnText.setMinWidth(2*tile_size);
            turnText.setLayoutY((rows+1)*tile_size / 2 + 10);
            FadeTransition fade = new FadeTransition(Duration.seconds(5), turnText);
            fade.setFromValue(10);
            fade.setToValue(0);
            fade.play();
            left.getChildren().add(turnText);

            //Circle turn = new Circle(tile_size / 2, redMove ? Color.RED : Color.YELLOW);
            //Button turnText = new Button(redMove? "AI's Turn" : "Your Turn");
        }
        redMove = !redMove;
        if(!redMove)
        {

            Minimax minimax = new Minimax(board);
            int col = minimax.getMove();

            int rowAI = minimax.calculateRow(board, col);
            int rowUI = calUIRow(col);
            /*System.out.println("row---" + rowAI);
            System.out.println("col---" + col);*/
            placeDisc(new Disc(redMove), col);

            //System.out.println("col----" + col);

            grid[col][rowUI] = disc;
            board[rowAI][col] = ai;

            checkWin();
        }
    }



    /*private boolean gameEnded(int column, int row) {
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
    }*/

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

    private void gameOver(int player) {
        isGameEnded = true;
        Pane ended = new Pane();
        ended.setPrefSize(2*tile_size, 4* tile_size);
        ended.setLayoutY((rows-2)*tile_size / 2);

        Button winner = new Button("<Winner>");
        winner.setFont(new Font(22));
        winner.setStyle("-fx-background-color: #b09d21");
        winner.setMinWidth(2*tile_size);
        winner.setLayoutY((rows-2)*tile_size / 2);

        Circle turn = null;
        Button turnText = null;
        if (player==1)
        {
            turn = new Circle(tile_size / 2, Color.RED);
            turnText = new Button("You");
        }
        else if (player==2)
        {
            turn = new Circle(tile_size / 2, Color.YELLOW);
            turnText = new Button("AI");
        }

        turn.setCenterX(tile_size);
        turn.setCenterY(rows*tile_size / 2);

        turnText.setFont(new Font(22));
        turnText.setStyle("-fx-background-color: #ffffff");
        turnText.setMinWidth(2*tile_size);
        turnText.setLayoutY((rows+1)*tile_size / 2 + 10);

        left.getChildren().add(winner);
        left.getChildren().add(turn);
        left.getChildren().add(turnText);

        for(int i=0; i<6; i++)
        {
            for(int j=0; j<7; j++)
            {
                System.out.print(board[i][j]+"-------");
            }
            System.out.println();
        }
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
            setCenterY(-tile_size);
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