import junit.framework.TestCase;

import java.util.*;

/*
  Unit test for Piece class -- starter shell.
 */
public class PieceTest extends TestCase {
    // You can create data to be used in the your
    // test cases like this. For each run of a test method,
    // a new PieceTest object is created and setUp() is called
    // automatically by JUnit.
    // For example, the code below sets up some
    // pyramid and s pieces in instance variables
    // that can be used in tests.
    private Piece stick, stickRotated;
    private Piece L1, L2, L3, L4;
    private Piece mirrorL1, mirrorL2, mirrorL3, mirrorL4;
    private Piece s, sRotated;
    private Piece mirrorS, mirrorSRotated;
    private Piece square, squareRotated;
    private Piece pyr1, pyr2, pyr3, pyr4;
    private Piece[] pieces;

    protected void setUp() throws Exception {
        super.setUp();

        stick = new Piece(Piece.STICK_STR);
        stickRotated = stick.computeNextRotation();

        L1 = new Piece(Piece.L1_STR);
        L2 = L1.computeNextRotation();
        L3 = L2.computeNextRotation();
        L4 = L3.computeNextRotation();

        mirrorL1 = new Piece(Piece.L2_STR);
        mirrorL2 = mirrorL1.computeNextRotation();
        mirrorL3 = mirrorL2.computeNextRotation();
        mirrorL4 = mirrorL3.computeNextRotation();

        s = new Piece(Piece.S1_STR);
        sRotated = s.computeNextRotation();

        mirrorS = new Piece(Piece.S2_STR);
        mirrorSRotated = mirrorS.computeNextRotation();

        square = new Piece(Piece.SQUARE_STR);
        squareRotated = square.computeNextRotation();

        pyr1 = new Piece(Piece.PYRAMID_STR);
        pyr2 = pyr1.computeNextRotation();
        pyr3 = pyr2.computeNextRotation();
        pyr4 = pyr3.computeNextRotation();

        pieces = Piece.getPieces();
    }


    public void testSampleSize() {
        // Check size of pyramid piece
        assertEquals(3, pyr1.getWidth());
        assertEquals(2, pyr1.getHeight());

        // Now try after rotation
        // Effectively we're testing size and rotation code here
        assertEquals(2, pyr2.getWidth());
        assertEquals(3, pyr2.getHeight());

        // Now try with some other piece, made a different way
        Piece l = new Piece(Piece.STICK_STR);
        assertEquals(1, l.getWidth());
        assertEquals(4, l.getHeight());

        // Check size for stick piece
        assertEquals(1, stick.getWidth());
        assertEquals(4, stick.getHeight());

        // Check size for rotated stick piece
        assertEquals(4, stickRotated.getWidth());
        assertEquals(1, stickRotated.getHeight());

        // Check size for L piece
        assertEquals(2, L1.getWidth());
        assertEquals(3, L1.getHeight());

        // Check size for rotated L piece
        assertEquals(3, L2.getWidth());
        assertEquals(2, L2.getHeight());

        // Check size for double rotated L piece
        assertEquals(2, L3.getWidth());
        assertEquals(3, L3.getHeight());

        // Check size for mirror L piece
        assertEquals(2, mirrorL1.getWidth());
        assertEquals(3, mirrorL1.getHeight());

        // Check size for rotated mirror L piece
        assertEquals(3, mirrorL2.getWidth());
        assertEquals(2, mirrorL2.getHeight());

        // Check size for double rotated mirror L piece
        assertEquals(2, mirrorL3.getWidth());
        assertEquals(3, mirrorL3.getHeight());

        // Check size for S piece
        assertEquals(3, s.getWidth());
        assertEquals(2, s.getHeight());

        // Check size for rotated S piece
        assertEquals(2, sRotated.getWidth());
        assertEquals(3, sRotated.getHeight());

        // Check size for mirror S piece
        assertEquals(3, mirrorS.getWidth());
        assertEquals(2, mirrorS.getHeight());

        // Check size for rotated mirror S piece
        assertEquals(2, mirrorSRotated.getWidth());
        assertEquals(3, mirrorSRotated.getHeight());

        // Check size for square
        assertEquals(2, square.getWidth());
        assertEquals(2, square.getHeight());

        // Check size for rotated square
        assertEquals(2, squareRotated.getWidth());
        assertEquals(2, squareRotated.getHeight());
    }


    // Test the skirt returned by a few pieces
    public void testSampleSkirt() {
        // Note must use assertTrue(Arrays.equals(... as plain .equals does not work
        // right for arrays.

        // Check skirt for pyramid piece
        assertTrue(Arrays.equals(new int[]{0, 0, 0}, pyr1.getSkirt()));
        assertTrue(Arrays.equals(new int[]{1, 0}, pyr2.getSkirt()));
        assertTrue(Arrays.equals(new int[]{1, 0, 1}, pyr3.getSkirt()));
        assertTrue(Arrays.equals(new int[]{0, 1}, pyr4.getSkirt()));

        // Check skirt for S piece
        assertTrue(Arrays.equals(new int[]{0, 0, 1}, s.getSkirt()));
        assertTrue(Arrays.equals(new int[]{1, 0}, sRotated.getSkirt()));

        // Check skirt for mirror S piece
        assertTrue(Arrays.equals(new int[]{1, 0, 0}, mirrorS.getSkirt()));
        assertTrue(Arrays.equals(new int[]{0, 1}, mirrorSRotated.getSkirt()));

        // Check skirt for stick piece
        assertTrue(Arrays.equals(new int[]{0}, stick.getSkirt()));
        assertTrue(Arrays.equals(new int[]{0, 0, 0, 0}, stickRotated.getSkirt()));

        // Check skirt for L piece
        assertTrue(Arrays.equals(new int[]{0, 0}, L1.getSkirt()));
        assertTrue(Arrays.equals(new int[]{0, 0, 0}, L2.getSkirt()));
        assertTrue(Arrays.equals(new int[]{2, 0}, L3.getSkirt()));
        assertTrue(Arrays.equals(new int[]{0, 1, 1}, L4.getSkirt()));

        // Check skirt for mirror L piece
        assertTrue(Arrays.equals(new int[]{0, 0}, mirrorL1.getSkirt()));
        assertTrue(Arrays.equals(new int[]{1, 1, 0}, mirrorL2.getSkirt()));
        assertTrue(Arrays.equals(new int[]{0, 2}, mirrorL3.getSkirt()));
        assertTrue(Arrays.equals(new int[]{0, 0, 0}, mirrorL4.getSkirt()));

        // Check skirt for square piece
        assertTrue(Arrays.equals(new int[]{0, 0}, square.getSkirt()));
        assertTrue(Arrays.equals(new int[]{0, 0}, squareRotated.getSkirt()));
    }


    public void testSampleEquals() {
        // Check equals for stick piece
        assertTrue(stick.equals(new Piece(Piece.STICK_STR)));
        assertTrue(!stick.equals(stickRotated));
        assertTrue(stickRotated.equals(stick.computeNextRotation()));
        assertTrue(!stick.equals(square));
        assertTrue(!stick.equals(new Piece("0 0	0 1	 0 2  0 3  3 3")));

        // Check equals for L piece
        assertTrue(L1.equals(new Piece(Piece.L1_STR)));
        assertTrue(!L1.equals(L2));
        assertTrue(!L2.equals(L3));
        assertTrue(!L3.equals(L4));
        assertTrue(L2.equals(L3.computeNextRotation().computeNextRotation().computeNextRotation()));
        assertTrue(L3.equals(L1.computeNextRotation().computeNextRotation()));
        assertTrue(L4.equals(L3.computeNextRotation()));
        assertTrue(!L1.equals(s));
        assertTrue(!L1.equals(new Piece("0 0	0 1	 0 2  1 0  0 0")));

        // Check equals for mirror L piece
        assertTrue(mirrorL1.equals(new Piece(Piece.L2_STR)));
        assertTrue(!mirrorL1.equals(mirrorL2));
        assertTrue(!mirrorL2.equals(mirrorL3));
        assertTrue(!mirrorL3.equals(mirrorL4));
        assertTrue(mirrorL2.equals(mirrorL3.computeNextRotation().computeNextRotation().computeNextRotation()));
        assertTrue(mirrorL3.equals(mirrorL1.computeNextRotation().computeNextRotation()));
        assertTrue(mirrorL4.equals(mirrorL3.computeNextRotation()));
        assertTrue(!mirrorL1.equals(mirrorS));
        assertTrue(!mirrorL1.equals(new Piece("0 0	1 0 1 1	 1 2  2 2")));

        // Check equals for S piece
        assertTrue(s.equals(new Piece(Piece.S1_STR)));
        assertTrue(!s.equals(sRotated));
        assertTrue(sRotated.equals(s.computeNextRotation()));
        assertTrue(!s.equals(L1));
        assertTrue(!s.equals(new Piece("0 0	1 0	 1 1  2 1  1 1")));

        // Check equals for mirror S piece
        assertTrue(mirrorS.equals(new Piece(Piece.S2_STR)));
        assertTrue(!mirrorS.equals(mirrorSRotated));
        assertTrue(mirrorSRotated.equals(mirrorS.computeNextRotation()));
        assertTrue(!mirrorS.equals(mirrorL1));
        assertTrue(!mirrorS.equals(new Piece("0 1	1 1  1 0  2 0  0 0")));

        // Check equals for square piece
        assertTrue(square.equals(new Piece(Piece.SQUARE_STR)));
        assertTrue(square.equals(squareRotated));
        assertTrue(squareRotated.equals(square.computeNextRotation()));
        assertTrue(!square.equals(stick));
        assertTrue(!square.equals(new Piece("0 0  0 1  1 0  1 1  1 1")));

        //Check equals for pyramid piece
        assertTrue(pyr1.equals(new Piece(Piece.PYRAMID_STR)));
        assertTrue(!pyr1.equals(pyr2));
        assertTrue(!pyr2.equals(pyr3));
        assertTrue(!pyr3.equals(pyr4));
        assertTrue(pyr2.equals(pyr3.computeNextRotation().computeNextRotation().computeNextRotation()));
        assertTrue(pyr3.equals(pyr1.computeNextRotation().computeNextRotation()));
        assertTrue(pyr4.equals(pyr3.computeNextRotation()));
        assertTrue(!pyr1.equals(L1));
        assertTrue(!pyr1.equals(new Piece("0 0  1 0  1 1  2 0  0 0")));
    }

    public void testSampleFastRotation() {
        // Check fast rotation for stick piece
        assertTrue(stickRotated.equals(pieces[Piece.STICK].fastRotation()));
        assertTrue(stick.equals(pieces[Piece.STICK].fastRotation().fastRotation()));
        assertTrue(stickRotated.equals(pieces[Piece.STICK].fastRotation().fastRotation().fastRotation()));
        assertTrue(stick.equals(pieces[Piece.STICK].fastRotation().fastRotation().fastRotation().fastRotation()));

        // Check fast rotation for L piece
        assertTrue(L2.equals(pieces[Piece.L1].fastRotation()));
        assertTrue(L3.equals(pieces[Piece.L1].fastRotation().fastRotation()));
        assertTrue(L4.equals(pieces[Piece.L1].fastRotation().fastRotation().fastRotation()));
        assertTrue(L1.equals(pieces[Piece.L1].fastRotation().fastRotation().fastRotation().fastRotation()));
        assertTrue(L2.equals(pieces[Piece.L1].fastRotation().fastRotation().fastRotation().fastRotation().fastRotation()));

        // Check fast rotation for mirror L piece
        assertTrue(mirrorL2.equals(pieces[Piece.L2].fastRotation()));
        assertTrue(mirrorL3.equals(pieces[Piece.L2].fastRotation().fastRotation()));
        assertTrue(mirrorL4.equals(pieces[Piece.L2].fastRotation().fastRotation().fastRotation()));
        assertTrue(mirrorL1.equals(pieces[Piece.L2].fastRotation().fastRotation().fastRotation().fastRotation()));
        assertTrue(mirrorL2.equals(pieces[Piece.L2].fastRotation().fastRotation().fastRotation().fastRotation().fastRotation()));

        // Check fast rotation for S piece
        assertTrue(sRotated.equals(pieces[Piece.S1].fastRotation()));
        assertTrue(s.equals(pieces[Piece.S1].fastRotation().fastRotation()));
        assertTrue(sRotated.equals(pieces[Piece.S1].fastRotation().fastRotation().fastRotation()));
        assertTrue(s.equals(pieces[Piece.S1].fastRotation().fastRotation().fastRotation().fastRotation()));

        // Check fast rotation for mirror S piece
        assertTrue(mirrorSRotated.equals(pieces[Piece.S2].fastRotation()));
        assertTrue(mirrorS.equals(pieces[Piece.S2].fastRotation().fastRotation()));
        assertTrue(mirrorSRotated.equals(pieces[Piece.S2].fastRotation().fastRotation().fastRotation()));
        assertTrue(mirrorS.equals(pieces[Piece.S2].fastRotation().fastRotation().fastRotation().fastRotation()));

        // Check fast rotation for square piece
        assertTrue(squareRotated.equals(pieces[Piece.SQUARE].fastRotation()));
        assertTrue(square.equals(pieces[Piece.SQUARE].fastRotation().fastRotation()));
        assertTrue(squareRotated.equals(pieces[Piece.SQUARE].fastRotation().fastRotation().fastRotation()));
        assertTrue(square.equals(pieces[Piece.SQUARE].fastRotation().fastRotation().fastRotation().fastRotation()));

        // Check fast rotation for pyramid piece
        assertTrue(pyr2.equals(pieces[Piece.PYRAMID].fastRotation()));
        assertTrue(pyr3.equals(pieces[Piece.PYRAMID].fastRotation().fastRotation()));
        assertTrue(pyr4.equals(pieces[Piece.PYRAMID].fastRotation().fastRotation().fastRotation()));
        assertTrue(pyr1.equals(pieces[Piece.PYRAMID].fastRotation().fastRotation().fastRotation().fastRotation()));
        assertTrue(pyr2.equals(pieces[Piece.PYRAMID].fastRotation().fastRotation().fastRotation().fastRotation().fastRotation()));
    }
}
