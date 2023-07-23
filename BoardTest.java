import junit.framework.TestCase;


public class BoardTest extends TestCase {
    Board b;
    Piece pyr1, pyr2, pyr3, pyr4, s, sRotated, square, L, L1, L2, L3, stick, stickRotated;

    // This shows how to build things in setUp() to re-use
    // across tests.

    // In this case, setUp() makes shapes,
    // and also a 3X6 board, with pyr placed at the bottom,
    // ready to be used by tests.

    protected void setUp() throws Exception {
        b = new Board(3, 6);

        pyr1 = new Piece(Piece.PYRAMID_STR);
        pyr2 = pyr1.computeNextRotation();
        pyr3 = pyr2.computeNextRotation();
        pyr4 = pyr3.computeNextRotation();

        s = new Piece(Piece.S1_STR);
        sRotated = s.computeNextRotation();

        square = new Piece(Piece.SQUARE_STR);

        L = new Piece(Piece.L1_STR);
        L1 = L.computeNextRotation();
        L2 = L1.computeNextRotation();
        L3 = L2.computeNextRotation();

        stick = new Piece(Piece.STICK_STR);
        stickRotated = stick.computeNextRotation();

        b.place(pyr1, 0, 0);
    }

    // Check the basic width/height/max after the one placement
    public void testSample1() {
        assertEquals(1, b.getColumnHeight(0));
        assertEquals(2, b.getColumnHeight(1));
        assertEquals(2, b.getMaxHeight());
        assertEquals(3, b.getRowWidth(0));
        assertEquals(1, b.getRowWidth(1));
        assertEquals(0, b.getRowWidth(2));
    }

    // Place sRotated into the board, then check some measures
    public void testSample2() {
        b.commit();
        int result = b.place(sRotated, 1, 1);
        assertEquals(Board.PLACE_OK, result);
        assertEquals(1, b.getColumnHeight(0));
        assertEquals(4, b.getColumnHeight(1));
        assertEquals(3, b.getColumnHeight(2));
        assertEquals(4, b.getMaxHeight());
    }


    public void testSample3() {
        // Check board width and height
        assertEquals(3, b.getWidth());
        assertEquals(6, b.getHeight());

        // Check get grid
        assertTrue(b.getGrid(0, 0));
        assertFalse(b.getGrid(0, 1));

        // Check drop height
        assertEquals(1, b.dropHeight(stick, 0));
        assertEquals(2, b.dropHeight(square, 0));
    }


    // Check place and states of board after pieces placement
    public void testSample4() {
        b.commit();
        int result = b.place(L, 1, 4);
        assertEquals(result, Board.PLACE_OUT_BOUNDS);

        b.commit();
        result = b.place(square, 1, 1);
        assertEquals(result, Board.PLACE_BAD);

        b.commit();
        result = b.place(sRotated, 1, 1);
        assertEquals(result, Board.PLACE_OK);
        assertEquals(1, b.getColumnHeight(0));
        assertEquals(4, b.getColumnHeight(1));
        assertEquals(3, b.getColumnHeight(2));
        assertEquals(4, b.getMaxHeight());

        b.commit();
        result = b.place(stick, 0, 1);
        assertEquals(result, Board.PLACE_ROW_FILLED);
        assertEquals(5, b.getColumnHeight(0));
        assertEquals(4, b.getColumnHeight(1));
        assertEquals(3, b.getColumnHeight(2));
        assertEquals(5, b.getMaxHeight());
    }


    // Check clear rows after pieces placement
    public void testSample5() {
        b.commit();
        int result = b.place(sRotated, 1, 1);
        assertEquals(result, Board.PLACE_OK);
        assertEquals(1, b.getColumnHeight(0));
        assertEquals(4, b.getColumnHeight(1));
        assertEquals(3, b.getColumnHeight(2));
        assertEquals(4, b.getMaxHeight());

        b.commit();
        result = b.place(stick, 0, 1);
        assertEquals(result, Board.PLACE_ROW_FILLED);
        assertEquals(5, b.getColumnHeight(0));
        assertEquals(4, b.getColumnHeight(1));
        assertEquals(3, b.getColumnHeight(2));
        assertEquals(5, b.getMaxHeight());

        b.commit();
        result = b.place(square, 1, 4);
        assertEquals(result, Board.PLACE_ROW_FILLED);
        assertEquals(5, b.getColumnHeight(0));
        assertEquals(6, b.getColumnHeight(1));
        assertEquals(6, b.getColumnHeight(2));
        assertEquals(6, b.getMaxHeight());

        int numberOfClearedRows = b.clearRows();
        assertEquals(4, numberOfClearedRows);
        assertEquals(1, b.getColumnHeight(0));
        assertEquals(2, b.getColumnHeight(1));
        assertEquals(2, b.getColumnHeight(2));
        assertEquals(2, b.getMaxHeight());
    }


    // Check undo after place and clear rows
    public void testSample6() {
        b.commit();
        int result = b.place(L3, 0, 1);
        assertEquals(result, Board.PLACE_ROW_FILLED);
        assertEquals(3, b.getColumnHeight(0));
        assertEquals(3, b.getColumnHeight(1));
        assertEquals(3, b.getColumnHeight(2));
        assertEquals(3, b.getMaxHeight());

        b.commit();
        int numberOfClearedRows = b.clearRows();
        assertEquals(2, numberOfClearedRows);
        assertEquals(1, b.getColumnHeight(0));
        assertEquals(1, b.getColumnHeight(1));
        assertEquals(0, b.getColumnHeight(2));
        assertEquals(1, b.getMaxHeight());

        b.undo();

        assertEquals(3, b.getColumnHeight(0));
        assertEquals(3, b.getColumnHeight(1));
        assertEquals(3, b.getColumnHeight(2));
        assertEquals(3, b.getMaxHeight());
    }
}
