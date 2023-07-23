// Board.java

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * CS108 Tetris Board.
 * Represents a Tetris board -- essentially a 2-d grid
 * of booleans. Supports tetris pieces and row clearing.
 * Has an "undo" feature that allows clients to add and remove pieces efficiently.
 * Does not do any drawing or have any idea of pixels. Instead,
 * just represents the abstract 2-d board.
 */
public class Board {
    private int width;
    private int height;
    private boolean[][] grid;
    private boolean DEBUG = false;
    boolean committed;
    private int maxHeight;
    private int[] widths;
    private int[] heights;
    private boolean[][] xGrid;
    private int[] xWidths;
    private int[] xHeights;


    // Here a few trivial methods are provided:

    /**
     * Creates an empty board of the given width and height
     * measured in blocks.
     */
    public Board(int width, int height) {
        this.width = width;
        this.height = height;
        this.grid = new boolean[width][height];
        this.committed = true;

        this.maxHeight = 0;
        this.widths = new int[height];
        this.heights = new int[width];

        this.xGrid = new boolean[width][height];
        this.xWidths = new int[height];
        this.xHeights = new int[width];
    }


    /**
     * Returns the width of the board in blocks.
     */
    public int getWidth() {
        return this.width;
    }


    /**
     * Returns the height of the board in blocks.
     */
    public int getHeight() {
        return this.height;
    }


    /**
     * Returns the max column height present in the board.
     * For an empty board this is 0.
     */
    public int getMaxHeight() {
        return this.maxHeight;
    }


    /**
     * Checks the board for internal consistency -- used
     * for debugging.
     */
    public void sanityCheck() {
        if (DEBUG) {
            if (incorrectWidths()) {
                throw new RuntimeException("Incorrect widths!");
            }

            if (incorrectHeights()) {
                throw new RuntimeException("Incorrect heights!");
            }

            if (incorrectMaxHeights()) {
                throw new RuntimeException("Incorrect max height!");
            }
        }
    }

    private boolean incorrectMaxHeights() {
        int currMaxHeight = getCurrentMaxHeight();

        return getMaxHeight() != currMaxHeight;
    }

    private int getCurrentMaxHeight() {
        int resultMaxHeight = 0;

        for (int i = 0; i < width; i++) {
            int currMaxHeight = getCurrentColumnHeight(i);

            resultMaxHeight = currMaxHeight > resultMaxHeight ? currMaxHeight : resultMaxHeight;
        }

        return resultMaxHeight;
    }

    private boolean incorrectHeights() {
        for (int i = 0; i < width; i++) {
            int currHeight = getCurrentColumnHeight(i);

            if (getColumnHeight(i) != currHeight) {
                return true;
            }
        }

        return false;
    }

    private int getCurrentColumnHeight(int r) {
        int resultHeight = 0;

        for (int i = 0; i < height; i++) {
            if (grid[r][i]) {
                resultHeight = i + 1;
            }
        }

        return resultHeight;
    }

    private boolean incorrectWidths() {
        for (int i = 0; i < height; i++) {
            int currWidth = getCurrentRowWidth(i);

            if (getRowWidth(i) != currWidth) {
                return true;
            }
        }

        return false;
    }

    private int getCurrentRowWidth(int h) {
        int resultWidth = 0;

        for (int i = 0; i < width; i++) {
            if (grid[i][h]) {
                resultWidth++;
            }
        }

        return resultWidth;
    }

    /**
     * Given a piece and an x, returns the y
     * value where the piece would come to rest
     * if it were dropped straight down at that x.
     *
     * <p>
     * Implementation: use the skirt and the col heights
     * to compute this fast -- O(skirt length).
     */
    public int dropHeight(Piece piece, int x) {
        int y = 0;
        int[] pieceSkirt = piece.getSkirt();

        for (int i = 0; i < pieceSkirt.length; i++) {
            int currY = getColumnHeight(x + i) - pieceSkirt[i];

            if (currY > y) {
                y = currY;
            }
        }

        return y;
    }


    /**
     * Returns the height of the given column --
     * i.e. the y value of the highest block + 1.
     * The height is 0 if the column contains no blocks.
     */
    public int getColumnHeight(int x) {
        return this.heights[x];
    }


    /**
     * Returns the number of filled blocks in
     * the given row.
     */
    public int getRowWidth(int y) {
        return this.widths[y];
    }


    /**
     * Returns true if the given block is filled in the board.
     * Blocks outside of the valid width/height area
     * always return true.
     */
    public boolean getGrid(int x, int y) {
        return !inBounds(x, y) || this.grid[x][y];
    }

    private boolean inBounds(int x, int y) {
        return x >= 0 && x < this.width && y >= 0 && y < this.height;
    }


    public static final int PLACE_OK = 0;
    public static final int PLACE_ROW_FILLED = 1;
    public static final int PLACE_OUT_BOUNDS = 2;
    public static final int PLACE_BAD = 3;

    /**
     * Attempts to add the body of a piece to the board.
     * Copies the piece blocks into the board grid.
     * Returns PLACE_OK for a regular placement, or PLACE_ROW_FILLED
     * for a regular placement that causes at least one row to be filled.
     *
     * <p>Error cases:
     * A placement may fail in two ways. First, if part of the piece may falls out
     * of bounds of the board, PLACE_OUT_BOUNDS is returned.
     * Or the placement may collide with existing blocks in the grid
     * in which case PLACE_BAD is returned.
     * In both error cases, the board may be left in an invalid
     * state. The client can use undo(), to recover the valid, pre-place state.
     */
    public int place(Piece piece, int x, int y) {
        if (!committed) throw new RuntimeException("place commit problem");

        committed = false;
        copyBackup();

        int result = PLACE_OK;

        TPoint[] pointsToFillByPiece = getPointsToFullByPiece(piece, x, y);

        if (outOfBoundPiece(pointsToFillByPiece)) {
            result = PLACE_OUT_BOUNDS;
        } else if (alreadyFilled(pointsToFillByPiece)) {
            result = PLACE_BAD;
        } else {
            for (final TPoint tp : pointsToFillByPiece) {
                grid[tp.x][tp.y] = true;
                if (++widths[tp.y] == width) {
                    result = PLACE_ROW_FILLED;
                }
            }

            updateHeights();
            updateMaxHeight();
            sanityCheck();
        }

        return result;
    }

    private void updateMaxHeight() {
        maxHeight = 0;

        for (int i = 0; i < width; i++) {
            int currMaxHeight = getColumnHeight(i);
            maxHeight = currMaxHeight > maxHeight ? currMaxHeight : maxHeight;
        }
    }

    private boolean alreadyFilled(TPoint[] pointsToFillByPiece) {
        for (final TPoint tp : pointsToFillByPiece) {
            if (grid[tp.x][tp.y]) {
                return true;
            }
        }

        return false;
    }

    private boolean outOfBoundPiece(TPoint[] pointsToFillByPiece) {
        for (final TPoint tp : pointsToFillByPiece) {
            if (!inBounds(tp.x, tp.y)) {
                return true;
            }
        }

        return false;
    }

    private TPoint[] getPointsToFullByPiece(Piece piece, int x, int y) {
        TPoint[] pieceBody = piece.getBody();

        TPoint[] resultPoints = new TPoint[pieceBody.length];

        for (int i = 0; i < pieceBody.length; i++) {
            TPoint pieceCurrP = pieceBody[i];

            int xToFill = x + pieceCurrP.x;
            int yToFill = y + pieceCurrP.y;

            TPoint pointToFill = new TPoint(xToFill, yToFill);
            resultPoints[i] = pointToFill;
        }

        return resultPoints;
    }


    /**
     * Deletes rows that are filled all the way across, moving
     * things above down. Returns the number of rows cleared.
     */
    public int clearRows() {
        if (committed) {
            copyBackup();
            committed = false;
        }

        int rowsCleared = 0;

        List<Integer> filledRowsIndexes = getFilledRowsIndexes();
        rowsCleared = filledRowsIndexes.size();

        clearFilledRows(filledRowsIndexes);

        makeRowsShiftDown(filledRowsIndexes);

        updateHeights();

        maxHeight -= rowsCleared;

        sanityCheck();

        return rowsCleared;
    }

    private void copyBackup() {
        for (int i = 0; i < width; i++) {
            System.arraycopy(grid[i], 0, xGrid[i], 0, height);
        }

        System.arraycopy(widths, 0, xWidths, 0, height);
        System.arraycopy(heights, 0, xHeights, 0, width);
    }

    private void updateHeights() {
        Arrays.fill(heights, 0);

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (grid[i][j]) {
                    heights[i] = j + 1;
                }
            }
        }
    }

    private void clearFilledRows(List<Integer> filledRowsIndexes) {
        for (final int row : filledRowsIndexes) {
            for (int i = 0; i < width; i++) {
                grid[i][row] = false;
                widths[row] = 0;
            }
        }
    }

    private void makeRowsShiftDown(List<Integer> filledRowsIndexes) {
        for (final int row : filledRowsIndexes) {
            for (int i = row; i < maxHeight - 1; i++) {
                copyRowFromTo(i + 1, i);
                widths[i] = widths[i + 1];
                clearFilledRows(List.of(i + 1));
            }
        }

        if (filledRowsIndexes.size() > 0) {
            clearFilledRows(List.of(maxHeight - 1));
        }
    }

    private void copyRowFromTo(int from, int to) {
        for (int i = 0; i < width; i++) {
            grid[i][to] = grid[i][from];
        }
    }

    private List<Integer> getFilledRowsIndexes() {
        List<Integer> resultIndexes = new ArrayList<>();

        for (int i = widths.length - 1; i >= 0; i--) {
            if (widths[i] == width) {
                resultIndexes.add(i);
            }
        }

        return resultIndexes;
    }


    /**
     * Reverts the board to its state before up to one place
     * and one clearRows();
     * If the conditions for undo() are not met, such as
     * calling undo() twice in a row, then the second undo() does nothing.
     * See the overview docs.
     */
    public void undo() {
        if (!committed) {
            swapBackup();
            updateMaxHeight();
            commit();
            sanityCheck();
        }
    }

    private void swapBackup() {
        boolean[][] tempGrid = xGrid;
        xGrid = grid;
        grid = tempGrid;

        int[] tempWidths = xWidths;
        xWidths = widths;
        widths = tempWidths;

        int[] tempHeights = xHeights;
        xHeights = heights;
        heights = tempHeights;
    }


    /**
     * Puts the board in the committed state.
     */
    public void commit() {
        committed = true;
    }


    /*
     Renders the board state as a big String, suitable for printing.
     This is the sort of print-obj-state utility that can help see complex
     state change over time.
     (provided debugging utility)
     */
    public String toString() {
        StringBuilder buff = new StringBuilder();
        for (int y = height - 1; y >= 0; y--) {
            buff.append('|');
            for (int x = 0; x < width; x++) {
                if (getGrid(x, y)) buff.append('+');
                else buff.append(' ');
            }
            buff.append("|\n");
        }
        for (int x = 0; x < width + 2; x++) buff.append('-');
        return (buff.toString());
    }
}


