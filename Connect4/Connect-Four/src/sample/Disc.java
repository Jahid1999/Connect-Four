package sample;

import javafx.animation.TranslateTransition;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.util.Optional;

public class Disc extends Circle {
    private static final int tile_size = 100;
    private static final int columns = 7;
    private static final int rows = 6;

    private boolean redMove = true;
    private Disc[][] grid = new Disc[columns][rows];

    private Pane discRoot = new Pane();
    public final boolean red;
    public Disc(boolean red) {
        super(tile_size / 2, red ? Color.RED : Color.YELLOW);
        this.red = red;

        setCenterX(tile_size / 2);
        setCenterY(tile_size / 2);
    }
    public Optional<Disc> getDisc(int column, int row) {
        if (column < 0 || column >= columns
                || row < 0 || row >= rows)
            return Optional.empty();

        return Optional.ofNullable(grid[column][row]);
    }
    public void placeDisc(Disc disc, int column) {
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

        TranslateTransition animation = new TranslateTransition(Duration.seconds(0.5), disc);
        animation.setToY(row * (tile_size + 5) + tile_size / 4);
//        animation.setOnFinished(e -> {
//            if (gameEnded(column, currentRow)) {
//                gameOver();
//            }
//
//            redMove = !redMove;
//        });
        animation.play();
    }
}
