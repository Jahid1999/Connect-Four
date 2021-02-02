package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.ColorPicker;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
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
    int ai =5;
    int sX , eX, sY, eY;

    private boolean redMove = true;
    private boolean isGameEnded = false;
    private Disc[][] grid = new Disc[columns][rows];
    public int[][] board = new int [rows][columns];

    private Pane discRoot = new Pane();
    Pane left = new Pane();
    Pane right = new Pane();

    static Color humanColor = Color.DARKSLATEGREY;
    static Color aiColor = Color.DARKCYAN;
    ColorPicker cp;
    Button chooseColor;
    Button start;


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
        lighting.setSurfaceScale(1.5);

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
                    left.getChildren().remove(start);
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
                    sX=j;
                    sY=i;
                    eX=j+3;
                    eY=i+3;
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
                    sX=j+3;
                    sY=i-3;
                    eX=j;
                    eY=i;
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
                    sX=j;
                    sY=i;
                    eX=j+3;
                    eY=i;
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
                    sX=j;
                    sY=i;
                    eX=j;
                    eY=i+3;
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

    /*public boolean isValidCol (int col)
    {
        for (int i=0; i<rows; i++)
        {
            if(board[i][col]==0)
            {
                return true;
            }
        }

        return false;
    }*/

    public void drawLine(int player)
    {
        sX= sX*(tile_size + 5)+(3*tile_size)/4;
        eX= eX*(tile_size + 5)+(3*tile_size)/4;
        sY= (rows-sY)*(tile_size + 5)-tile_size/4;
        eY= (rows-eY)*(tile_size + 5)-tile_size/4;

        Line line = new Line(sX,sY,eX,eY);
        line.setFill(Color.WHITE);
        line.setStroke(Color.WHITE);
        line.setStyle("-fx-stroke-width: 3");
        right.getChildren().addAll(line);

        Circle cir = new Circle(sX, sY,10);

        if (player == human)
        {
            cir.setFill(humanColor.invert());
        }
        else
        {
            cir.setFill(aiColor.invert());
        }

        PathTransition pathTransition = new PathTransition();
        pathTransition.setDuration(Duration.millis(1000));
        pathTransition.setNode(cir);
        pathTransition.setPath(line);
        pathTransition.setCycleCount(PathTransition.INDEFINITE);
        pathTransition.setAutoReverse(true);
        pathTransition.play();

        right.getChildren().add(cir);
    }

    public void checkWin ()
    {
        if(winning(board, human))
        {
            gameOver(human);
            drawLine(human);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Congratulations! You have won!");
            alert.setTitle("Connect Four");
            Circle turn = new Circle(tile_size / 4,humanColor);

            Light.Distant light = new Light.Distant();
            light.setAzimuth(45.0);
            light.setElevation(30.0);

            Lighting lighting = new Lighting();
            lighting.setLight(light);
            lighting.setSurfaceScale(5.0);
            turn.setEffect(lighting);

            alert.setGraphic(turn);
            ((Stage)alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image("/images/connect4.png"));

            Optional<ButtonType> result = alert.showAndWait();
            if(!result.isPresent()||result.get() == ButtonType.OK||result.get() == ButtonType.CANCEL)
            {
                System.exit(0);
            }
        }
        if(winning(board, ai))
        {
            gameOver(ai);
            drawLine(ai);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("OOPS! You have lost!");
            alert.setTitle("Connect Four");

            Circle turn = new Circle(tile_size / 4,aiColor);

            Light.Distant light = new Light.Distant();
            light.setAzimuth(45.0);
            light.setElevation(30.0);

            Lighting lighting = new Lighting();
            lighting.setLight(light);
            lighting.setSurfaceScale(5.0);
            turn.setEffect(lighting);

            alert.setGraphic(turn);
            ((Stage)alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image("/images/connect4.png"));

            Optional<ButtonType> result = alert.showAndWait();
            if(!result.isPresent()||result.get() == ButtonType.OK||result.get() == ButtonType.CANCEL)
            {
                System.exit(0);
            }
        }
    }

    TranslateTransition a1;
    TranslateTransition a2;

    public int calculateRow (int [][] board, int col)
    {
        for(int i=0; i<rows; i++)
        {
            if(board[i][col]==0)
            {
                return i;
            }
        }

        return -1;
    }

    private void placeDisc(Disc disc, int column) {

        int row = calUIRow(column);

        int rowA = calculateRow(board, column);

        if (row == -1 || rowA == -1)
        {
            return;
        }

        if(redMove)
        {
            grid[column][row] = disc;
            board[rowA][column] = human;
        }

        Light.Distant light = new Light.Distant();
        light.setAzimuth(45.0);
        light.setElevation(30.0);

        Lighting lighting = new Lighting();
        lighting.setLight(light);
        lighting.setSurfaceScale(5.0);
        disc.setEffect(lighting);

        discRoot.getChildren().add(disc);
        disc.setTranslateX(column * (tile_size + 5) + tile_size / 4);

        TranslateTransition animation = new TranslateTransition(Duration.seconds(1), disc);
        animation.setToY((row * (tile_size + 5) + tile_size / 4)+1.5*tile_size);

        if (redMove)
        {
            a1 = animation;
            a2 = new TranslateTransition(Duration.seconds(0));
        }
        else
        {
            a2 =animation;
        }

        SequentialTransition st = new SequentialTransition(disc, a1, new PauseTransition(Duration.seconds(1)), a2);

        st.play();

        checkWin();
        checkDraw();

        if(!isGameEnded)
        {
            Circle turn = new Circle(tile_size, rows*tile_size / 2,tile_size/2, aiColor);

            turn.setEffect(lighting);

            FadeTransition t = new FadeTransition(Duration.seconds(4), turn);
            t.setFromValue(10);
            t.setToValue(0);
            t.play();
            left.getChildren().add(turn);

            Button turnText = new Button("AI's Turn");
            turnText.setFont(new Font(22));
            turnText.setStyle("-fx-background-color: #777d80");
            turnText.setMinWidth(2*tile_size);
            turnText.setLayoutY((rows+1)*tile_size / 2 + 10);
            FadeTransition fade = new FadeTransition(Duration.seconds(4), turnText);
            fade.setFromValue(10);
            fade.setToValue(0);
            fade.play();
            left.getChildren().add(turnText);
        }
        redMove = !redMove;
        if(!redMove)
        {
            Minimax minimax = new Minimax(board);
            int col = minimax.getMove();

            int rowAI = calculateRow(board, col);
            int rowUI = calUIRow(col);
            placeDisc(new Disc(redMove), col);

            grid[col][rowUI] = disc;
            board[rowAI][col] = ai;

            checkWin();
            checkDraw();
        }
    }

    private void checkDraw()
    {
        for (int i=0; i<rows; i++)
        {
            for (int j=0; j<columns; j++)
            {
                if (board[i][j]==0)
                {
                    return;
                }
            }
        }

        showDrawAlert();
    }

    private void showDrawAlert()
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("It's a draw! GGWP!");
        alert.setTitle("Connect Four");
        Image im = new Image("/images/connect4.png", false);
        Circle dp = new Circle(30);
        dp.setFill(new ImagePattern(im));
        alert.setGraphic(dp);
        ((Stage)alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image("/images/connect4.png"));

        Optional<ButtonType> result = alert.showAndWait();
        if(!result.isPresent()||result.get() == ButtonType.OK||result.get() == ButtonType.CANCEL)
        {
            System.exit(0);
        }
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
        winner.setLayoutY(((rows-2)*tile_size / 2)-10);

        Light.Distant light = new Light.Distant();
        light.setAzimuth(45.0);
        light.setElevation(30.0);

        Lighting lighting = new Lighting();
        lighting.setLight(light);
        lighting.setSurfaceScale(5.0);

        Circle turn = null;
        Button turnText = null;
        if (player==human)
        {
            turn = new Circle(tile_size / 2, humanColor);
            turn.setEffect(lighting);
            turnText = new Button("You");
        }
        else if (player==ai)
        {
            turn = new Circle(tile_size / 2, aiColor);
            turn.setEffect(lighting);
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

    public void firstAIMove (Disc disc)
    {
        grid[3][5] = disc;
        board[0][3] = ai;

        Light.Distant light = new Light.Distant();
        light.setAzimuth(90.0);
        light.setElevation(35.0);

        Lighting lighting = new Lighting();
        lighting.setLight(light);
        lighting.setSurfaceScale(5.0);
        disc.setEffect(lighting);

        discRoot.getChildren().add(disc);
        disc.setTranslateX(3 * (tile_size + 5) + tile_size / 4);

        TranslateTransition animation = new TranslateTransition(Duration.seconds(1), disc);
        animation.setToY((5 * (tile_size + 5) + tile_size / 4)+1.5*tile_size);

        animation.play();
    }

    public void startGame (Stage primaryStage)
    {
        GridPane root = new GridPane();

        left.setPrefSize(2*tile_size, columns* tile_size);
        Circle turn = new Circle(tile_size / 2, redMove ? humanColor : aiColor);

        Light.Distant light = new Light.Distant();
        light.setAzimuth(45.0);
        light.setElevation(35.0);

        Lighting lighting = new Lighting();
        lighting.setLight(light);
        lighting.setSurfaceScale(5.0);

        turn.setEffect(lighting);
        turn.setCenterX(tile_size);
        turn.setCenterY(rows*tile_size / 2);

        VBox vbox = new VBox();

        Button about = new Button("About Us");
        about.setFont(new Font(22));
        about.setStyle("-fx-background-color: #777d80");
        about.setMinWidth(2*tile_size);
        about.setOnAction(e-> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("3 Musketeers");
            alert.setTitle("About Us");
            /*String content = String.format("\t\t\t\t\tTeam: 3 Musketeers \nTeam Members: \n\t1.Yasin Sazid(BSSE 1006)" +
                    "\n\t2.Atkia Akila Karim(BSSE 1015)\n\t3.Abdullah-Al-Jahid(BSSE 1030)");*/
            String content = String.format("Developers: \n\t\t\tYasin Sazid (BSSE 1006)" +
                    "\n\n\t\t\tAtkia Akila Karim (BSSE 1015)\n\n\t\t\tAbdullah-Al-Jahid (BSSE 1030)");
            alert.setContentText(content);
            alert.getDialogPane().setMinWidth(500);
            Image im = new Image("/images/3m3.jpg", false);
            Circle dp = new Circle(60);
            dp.setFill(new ImagePattern(im));
            alert.setGraphic(dp);
            ((Stage)alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image("/images/connect4.png"));
            alert.show();
        });
        Button help = new Button("Help");
        help.setFont(new Font(22));
        help.setStyle("-fx-background-color: #636c89");
        help.setMinWidth(2*tile_size);
        help.setOnAction(e-> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("About Connect Four");
            alert.setTitle("Help");
            String content = String.format("Description:\nConnect Four (also known as Four Up, Plot Four, Find Four, Four in a Row," +
                    "Drop Four, and Gravitrips in the Soviet Union) is a two-player connection board game, in which the players" +
                    "choose a color and then take turns dropping colored discs into a seven-column, six-row " +
                    "vertically suspended grid. The pieces fall straight down, occupying the lowest available " +
                    "space within the column. The objective of the game is to be the first to form a horizontal, " +
                    "vertical, or diagonal line of four of one's own discs." +
                    "\n\nObjective:\n" +
                    "The aim for both players is to make a straight line of four own pieces; the line can be vertical, horizontal or diagonal.\n" +
                    "\nHow The Game Starts:\n" +
                    "1. Human First: You can make the first move by clicking your desired column within the board.\n" +
                    "2. AI First: AI will make the first move if you click the START button.\n" +
                    "\nHow The Game Goes On:\n" +
                    "Each player takes alternating turns. During each player’s turn, drop a colored piece of that player’s color into the slots of the game board." +
                    "The players take turns dropping pieces into the slots. These pieces fall to the bottom of the board and the game continues until one player has " +
                    "four in a row and wins or until the board fills up, which results in a tie." +
                    "\n\nStrategy:\n" +
                    "1. Beginner: Remember to check all possible connecting lines, including horizontal, vertical and diagonal lines for possible threats.\n" +
                    "2. Advanced: A key strategy to win is by making two simultaneous threats for the opponent. Usually this involves connecting three discs to " +
                    "prevent the opponent from having an advantage in that column. Also, remember that discs placed in the middle are more valuable then those place " +
                    "on the sides because players have more chances of creating four in a row with them.");

            alert.setContentText(content);
            alert.getDialogPane().setMinWidth(650);
            alert.getDialogPane().setMinHeight(700);
            Image im = new Image("/images/connect4.png", false);
            Circle dp = new Circle(50);
            dp.setFill(new ImagePattern(im));
            alert.setGraphic(dp);
            ((Stage)alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image("/images/connect4.png"));
            alert.show();
        });

        vbox.getChildren().add(0,about);
        vbox.getChildren().add(1,help);

        Button turnText = new Button("Your Turn");
        turnText.setFont(new Font(22));
        turnText.setStyle("-fx-background-color: #777d80");
        turnText.setMinWidth(2*tile_size);
        turnText.setLayoutY((rows+1)*tile_size / 2 + 10);

        start = new Button("START");
        start.setFont(new Font(22));
        start.setStyle("-fx-background-color: #0f8080");
        start.setMinWidth(2*tile_size);
        start.setLayoutY((rows+1)*tile_size / 2 + 10);

        EventHandler<ActionEvent> st = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                left.getChildren().remove(cp);
                left.getChildren().remove(chooseColor);
                FadeTransition fade = new FadeTransition(Duration.seconds(2), start);
                fade.setFromValue(10);
                fade.setToValue(0);
                fade.play();

                fade.setOnFinished(ex -> {
                    left.getChildren().remove(start);
                });

                firstAIMove(new Disc(false));
            }
        };

        start.setOnAction(st);

        chooseColor = new Button("Choose Color");
        chooseColor.setFont(new Font(22));
        chooseColor.setStyle("-fx-background-color: #777d80");
        //chooseColor.setStyle("-fx-text-fill:White");
        chooseColor.setMinWidth(2*tile_size);
        chooseColor.setLayoutY((rows+1)*tile_size / 2 + 276);

        cp = new ColorPicker(Color.DARKSLATEGREY);
        cp.setMinWidth(2*tile_size);
        cp.setLayoutY((rows+1)*tile_size / 2 + 325);

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

        cp.setOnAction(event);

        left.getChildren().add(vbox);
        left.getChildren().add(turn);
        left.getChildren().add(cp);
        left.getChildren().add(chooseColor);
        left.getChildren().add(turnText);
        left.getChildren().add(start);

        left.setStyle("-fx-background-color: #b4bcc1");


        right.getChildren().add(discRoot);

        Shape gridShape = makeGrid();
        right.getChildren().add(gridShape);
        right.getChildren().addAll(makeColumns());

        root.add(left,0,1);
        root.add(right,1,1);


        primaryStage.setTitle("Connect Four");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.getIcons().add(new Image("/images/connect4.png"));
        primaryStage.show();
    }

    public void initialAlert ()
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("How To Start The Game");
        alert.setTitle("Connect Four");
        String content = String.format("1. Human First: You can make the first move by clicking your desired column within the board.\n\n" +
                "2. AI First: AI will make the first move if you click the START button.\n");
        alert.setContentText(content);
        alert.getDialogPane().setMinWidth(550);
        alert.getDialogPane().setMinHeight(250);
        Image im = new Image("/images/connect4.png", false);
        Circle dp = new Circle(30);
        dp.setFill(new ImagePattern(im));
        alert.setGraphic(dp);
        ((Stage)alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image("/images/connect4.png"));
        alert.show();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        startGame(primaryStage);

        initialAlert();
    }

    public static void main(String[] args) {

        launch(args);
    }
}