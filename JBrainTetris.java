import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class JBrainTetris extends JTetris {

    JCheckBox brainMode;
    JSlider adversary;
    JLabel ok;
    private final DefaultBrain brain;
    private Brain.Move nextMove;
    private int pieceCounter;


    /**
     * Creates a new JTetris where each tetris square
     * is drawn with the given number of pixels.
     *
     * @param pixels
     */
    JBrainTetris(int pixels) {
        super(pixels);

        this.brain = new DefaultBrain();
    }


    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        JBrainTetris brainTetris = new JBrainTetris(16);
        JFrame frame = JTetris.createFrame(brainTetris);
        frame.setVisible(true);
    }


    @Override
    public JComponent createControlPanel() {
        JPanel panel = (JPanel) super.createControlPanel();
        panel.add(new JLabel("Brain:"));
        brainMode = new JCheckBox("Brain Active");
        panel.add(brainMode);

        JPanel little = new JPanel();
        little.add(new JLabel("Adversary:"));
        adversary = new JSlider(0, 100, 0);
        adversary.setPreferredSize(new Dimension(100, 15));
        little.add(adversary);

        panel.add(little);

        ok = new JLabel("Ok");
        panel.add(ok);

        return panel;
    }


    @Override
    public void tick(int verb) {
        if (verb == DOWN) {
            if (brainMode.isSelected()) {

                if (pieceCounter != count) {
                    board.undo();
                    nextMove = brain.bestMove(board, currentPiece, HEIGHT, nextMove);
                    pieceCounter = count;
                }

                if (nextMove != null) {
                    checkForRotation();

                    if (currentX < nextMove.x) {
                        super.tick(RIGHT);
                    } else if (currentX > nextMove.x) {
                        super.tick(LEFT);
                    }
                }
            }
        }

        super.tick(verb);
    }


    private void checkForRotation() {
        if (!currentPiece.equals(nextMove.piece)) {
            currentPiece = currentPiece.fastRotation();
        }
    }


    @Override
    public Piece pickNextPiece() {
        Random rand = new Random();
        int value = rand.nextInt(100);

        if (value >= adversary.getValue()) {
            ok.setText("Ok");
            return super.pickNextPiece();
        }

        ok.setText("*Ok*");

        return choosePieceManually();
    }

    private Piece choosePieceManually() {
        Piece resultPiece = null;
        Brain.Move currMove = null;
        double score = 0;

        for (Piece piece : pieces) {
            currMove = brain.bestMove(board, piece, HEIGHT, currMove);

            if (currMove != null) {
                double currScore = currMove.score;

                if (currScore > score) {
                    score = currScore;
                    resultPiece = piece;
                }
            } else {
                return super.pickNextPiece();
            }
        }

        return resultPiece;
    }
}
