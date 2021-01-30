package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ColorPicker;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.ImagePattern;
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
    int ai =5;//2;

    private boolean redMove = true;
    private boolean isGameEnded = false;
    private Disc[][] grid = new Disc[columns][rows];
    public int[][] board = new int [rows][columns];

    private Pane discRoot = new Pane();
    Pane left = new Pane();

    static Color humanColor = Color.RED;
    static Color aiColor = Color.YELLOW;
    ColorPicker cp;
    Button chooseColor;


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

            EventHandler<javafx.scene.input.MouseEvent> event1 = new EventHandler<javafx.scene.input.MouseEvent>() {
                public void handle(MouseEvent e)
                {
                    left.getChildren().remove(cp);
                    left.getChildren().remove(chooseColor);
                    placeDisc(new Disc(redMove), column);
                }
            };
            rect.setOnMouseClicked(event1);

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
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Congratulations! You have won!");
            alert.setTitle("Connect Four");
            Circle turn = new Circle(tile_size / 4,aiColor);
            alert.setGraphic(turn);

            Optional<ButtonType> result = alert.showAndWait();
            if(!result.isPresent()||result.get() == ButtonType.OK||result.get() == ButtonType.CANCEL)
            {
                System.exit(0);
            }
        }
        if(winning(board, ai))
        {
            gameOver(ai);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("OOPS! You have lost!");
            alert.setTitle("Connect Four");
//            Image im = new Image("/images/3m1.jpg", false);
//            Circle turn = new Circle(tile_size / 4);

            Circle turn = new Circle(tile_size / 4,humanColor);
            alert.setGraphic(turn);

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
            Circle turn = new Circle(tile_size, rows*tile_size / 2,tile_size/2, aiColor);
            FadeTransition t = new FadeTransition(Duration.seconds(5), turn);
            t.setFromValue(10);
            t.setToValue(0);
            t.play();
            left.getChildren().add(turn);

            Button turnText = new Button("AI's Turn");
            turnText.setFont(new Font(22));
            turnText.setStyle("-fx-background-color: #aeaeae");
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

//    private boolean checkRange(List<Point2D> points) {
//        int chain = 0;
//
//        for (Point2D p : points) {
//            int column = (int) p.getX();
//            int row = (int) p.getY();
//
//            Disc disc = getDisc(column, row).orElse(new Disc(!redMove));
//            if (disc.red == redMove) {
//                chain++;
//                if (chain == 4) {
//                    return true;
//                }
//            } else {
//                chain = 0;
//            }
//        }
//
//        return false;
//    }

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
        if (player==human)
        {
            turn = new Circle(tile_size / 2, humanColor);
            turnText = new Button("You");
        }
        else if (player==ai)
        {
            turn = new Circle(tile_size / 2, aiColor);
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

//        for(int i=0; i<6; i++)
//        {
//            for(int j=0; j<7; j++)
//            {
//                System.out.print(board[i][j]+"-------");
//            }
//            System.out.println();
//        }
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
            super(tile_size / 2, red ? humanColor : aiColor);
            this.red = red;

            setCenterX(tile_size / 2);
            setCenterY(-tile_size);
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        GridPane root = new GridPane();

        left.setPrefSize(2*tile_size, columns* tile_size);
        Circle turn = new Circle(tile_size / 2, redMove ? humanColor : aiColor);
        turn.setCenterX(tile_size);
        turn.setCenterY(rows*tile_size / 2);

        VBox vbox = new VBox();

        Button about = new Button("About Us");
        about.setFont(new Font(22));
        about.setStyle("-fx-background-color: #ffffff");
        about.setMinWidth(2*tile_size);
        about.setOnAction(e-> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("3 Musketeers");
            alert.setTitle("About Us");
            String content = String.format("\t\t\t\t\tTeam: 3 Musketeers \nTeam Members: \n\t1.Yasin Sazid(BSSE 1006)" +
                    "\n\t2.Atkia Akila Karim(BSSE 1015)\n\t3.Abdullah-Al-Jahid(BSSE 1030)");
            alert.setContentText(content);
            alert.getDialogPane().setMinWidth(500);
            Image im = new Image("/images/3m2.jpg", false);
            Circle dp = new Circle(60);
            dp.setFill(new ImagePattern(im));
            alert.setGraphic(dp);
            alert.show();
        });
        Button help = new Button("Help");
        help.setFont(new Font(22));
        help.setStyle("-fx-background-color: #a35103");
        help.setMinWidth(2*tile_size);
        help.setOnAction(e-> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("About Connect Four");
            alert.setTitle("Help!!");
            String content = String.format("Description: \n\nConnect Four (also known as Four Up, Plot Four, Find Four, Four in a Row," +
                    "Drop Four, and Gravitrips in the Soviet Union) is a two-player connection board game, in which the players" +
                    "\nchoose a color and then take turns dropping colored discs into a seven-column, six-row " +
                    "\nvertically suspended grid. The pieces fall straight down, occupying the lowest available " +
                    "\nspace within the column. The objective of the game is to be the first to form a horizontal, " +
                    "\nvertical, or diagonal line of four of one's own discs." +
                    "\n\nObjective:\n\n" +
                    "The aim for both players is to make a straight line of four own pieces; the line can be vertical, horizontal or diagonal.\n" +
                    "\nHow The Game Goes On:\n\n" +
                    "Each player takes alternating turns. During each player’s turn, drop a colored piece of that player’s color into the slots of the game board." +
                    "The players take turns dropping pieces into the slots. These pieces fall to the bottom of the board and the game continues until one player has " +
                    "four in a row and wins or until the board fills up, which results in a tie." +
                    "\n\nStrategy:\n\n" +
                    "1.Beginners: Remember to check all possible connecting lines, including horizontal, vertical and diagonal lines for possible threats.\n" +
                    "2.Advanced: A key strategy to win is by making two simultaneous threats for the opponent. Usually this involves connecting three discs to " +
                    "prevent the opponent from having an advantage in that column. Also, remember that discs placed in the middle are more valuable then those place " +
                    "on the sides because players have more chances of creating four in a row with them.");
            alert.setContentText(content);
            alert.getDialogPane().setMinWidth(650);
            alert.getDialogPane().setMinHeight(700);
            Image im = new Image("/images/3m2.jpg", false);
            Circle dp = new Circle(60);
            dp.setFill(new ImagePattern(im));
            alert.setGraphic(dp);
            alert.show();
        });

        vbox.getChildren().add(0,about);
        vbox.getChildren().add(1,help);

        Button turnText = new Button("Your Turn");
        turnText.setFont(new Font(22));
        turnText.setStyle("-fx-background-color: #aeaeae");
        turnText.setMinWidth(2*tile_size);
        turnText.setLayoutY((rows+1)*tile_size / 2 + 10);

        chooseColor = new Button("Choose Color");
        chooseColor.setFont(new Font(22));
        chooseColor.setStyle("-fx-background-color: #aeaeae");
        chooseColor.setMinWidth(2*tile_size);
        chooseColor.setLayoutY((rows+1)*tile_size / 2 + 250);

        cp = new ColorPicker(Color.RED);
        cp.setMinWidth(2*tile_size);
        cp.setLayoutY((rows+1)*tile_size / 2 + 300);

        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                // color
                humanColor = cp.getValue();
                aiColor = humanColor.invert();
                turn.setFill(humanColor);

                left.getChildren().remove(cp);
                left.getChildren().remove(chooseColor);
            }
        };

        // set listener
        cp.setOnAction(event);

        left.getChildren().add(vbox);
        left.getChildren().add(turn);
        left.getChildren().add(turnText);
        left.getChildren().add(cp);
        left.getChildren().add(chooseColor);

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