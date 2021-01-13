package sample;

import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Main extends Application {

    private static final int tile_size = 100;
    private static final int rows = 6;
    private static final int columns = 7;

    private boolean redMove = true;
    private Disc[][] grid = new Disc[columns][rows];

    private Pane discRoot = new Pane();

    private Parent createContent() {
        Pane root = new Pane();
        root.getChildren().add(discRoot);

        Shape gridShape = makeGrid();
        root.getChildren().add(gridShape);
        root.getChildren().addAll(makeColumnsHighlighted());

        return  root;
    }

    private Shape makeGrid() {
        int width = (columns+1) * tile_size;
        int height = (rows+1) * tile_size;
        Shape shape = new Rectangle(width, height);

        for(int i=0 ; i<rows; i++) {
            for(int j=0 ; j<columns; j++) {
                Circle circle = new Circle(tile_size/2);
                circle.setCenterX(tile_size/2);
                circle.setCenterY(tile_size/2);
                circle.setTranslateX(j* (tile_size + 5) + (tile_size/4));
                circle.setTranslateY(i* (tile_size + 5) + (tile_size/4));

                shape = Shape.subtract(shape, circle);
            }
        }

        Light.Distant light = new Light.Distant();
        light.setAzimuth(45.0);
        light.setElevation(25.0);

        Lighting lighting = new Lighting();
        lighting.setLight(light);
        lighting.setSurfaceScale(5.0);

        shape.setFill(Color.LIGHTBLUE);
        shape.setEffect(lighting);

        return shape;
    }

    private List<Rectangle> makeColumnsHighlighted(){
        List<Rectangle> rects = new ArrayList<>();

        for (int i = 0; i < columns; i++) {
            Rectangle rectangle = new Rectangle(tile_size, (rows+1) * tile_size);
            rectangle.setTranslateX(i*(tile_size + 5) + (tile_size/4));
            rectangle.setFill(Color.TRANSPARENT);

            rectangle.setOnMouseEntered(e-> rectangle.setFill(Color.rgb(200, 200, 50, .3)));
            rectangle.setOnMouseExited(e-> rectangle.setFill(Color.TRANSPARENT));

            final int column = i;
            rectangle.setOnMouseClicked(e-> placeDisc(new Disc(redMove), column));

            rects.add(rectangle);
        }

        return rects;
    }

    private void placeDisc(Disc disc, int col) {
        int row = rows-1;
        do {
            if(getDisc(col, row).isPresent())
                break;
            row --;
        }while( row>=0);

        if(row<0)
            return;
        grid[col][row] = disc;
        discRoot.getChildren().add(disc);
        disc.setTranslateX(col * (tile_size + 5) + (tile_size/4));

        final int currentRow = row;
        TranslateTransition animation = new TranslateTransition(Duration.seconds(0.5), disc);
        animation.setToY(row * (tile_size + 5) + (tile_size/4));
//        animation.setOnFinished(e-> {
//            if(gameEnded(col, currentRow))
//                gameOver();
//            redMove = ! redMove;
//        });
        animation.play();

    }

//    private boolean gameEnded( int col, int row) {
//
//    }

    private void gameOver() {
        System.out.println("Winner is " + (redMove? "Red": "Yellow"));
    }

    private Optional<Disc> getDisc(int col, int row) {

        if(col < 0 || col > columns || row < 0 || row > rows)
            return Optional.empty();
        return Optional.ofNullable(grid[col][row]);
    }

    private static class Disc extends Circle {
        private final boolean red;
        public Disc(boolean red) {
            super(tile_size/2, red ? Color.RED:Color.YELLOW);
            this.red = red;
            setCenterX(tile_size/2);
            setCenterY(tile_size/2);

        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setScene(new Scene(createContent()));
        primaryStage.setTitle("Connect Four");
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
