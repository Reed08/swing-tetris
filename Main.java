import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Main {
    // #region Colors
    public static final Color[] COLORS = {
            new Color(230, 34, 34),
            new Color(230, 58, 34),
            new Color(230, 81, 34),
            new Color(230, 105, 34),
            new Color(230, 128, 34),
            new Color(230, 151, 34),
            new Color(230, 175, 34),
            new Color(230, 198, 34),
            new Color(230, 222, 34),
            new Color(214, 230, 34),
            new Color(191, 230, 34),
            new Color(167, 230, 34),
            new Color(144, 230, 34),
            new Color(120, 230, 34),
            new Color(97, 230, 34),
            new Color(73, 230, 34),
            new Color(50, 230, 34),
            new Color(34, 230, 47),
            new Color(34, 230, 70),
            new Color(34, 230, 94),
            new Color(34, 230, 117),
            new Color(34, 230, 141),
            new Color(34, 230, 164),
            new Color(34, 230, 188),
            new Color(34, 230, 211),
            new Color(34, 218, 230),
            new Color(34, 195, 230),
            new Color(34, 171, 230),
            new Color(34, 148, 230),
            new Color(34, 124, 230),
            new Color(34, 101, 230),
            new Color(34, 77, 230),
            new Color(34, 54, 230),
            new Color(47, 34, 230),
            new Color(70, 34, 230),
            new Color(94, 34, 230),
            new Color(117, 34, 230),
            new Color(141, 34, 230),
            new Color(164, 34, 230),
            new Color(188, 34, 230),
            new Color(211, 34, 230),
            new Color(230, 34, 218),
            new Color(230, 34, 195),
            new Color(230, 34, 171),
            new Color(230, 34, 148),
            new Color(230, 34, 124),
            new Color(230, 34, 101),
            new Color(230, 34, 77),
            new Color(230, 34, 54),
            new Color(230, 34, 58)
    };
    // #endregion

    // #region Shapes
    public static final boolean[][][][] shapes = {
            // I tetronimoe
            {
                    // Rotation 1
                    {
                            { true, true, true, true },
                            { false, false, false, false },
                            { false, false, false, false },
                            { false, false, false, false }
                    },
                    // Rotation 2
                    {
                            { true, false, false, false },
                            { true, false, false, false },
                            { true, false, false, false },
                            { true, false, false, false }
                    }
            },
            // J tetronimoe
            {
                    // Rotation 1
                    {
                            { true, false, false, false },
                            { true, true, true, false },
                            { false, false, false, false },
                            { false, false, false, false }
                    },
                    // Rotation 2
                    {
                            { false, true, false, false },
                            { false, true, false, false },
                            { true, true, false, false },
                            { false, false, false, false }
                    },
                    // Rotation 3
                    {
                            { true, true, true, false },
                            { false, false, true },
                            { false, false, false, false },
                            { false, false, false, false }
                    },
                    // Rotation 4
                    {
                            { true, true, false, false },
                            { true, false, false, false },
                            { true, false, false, false },
                            { false, false, false, false }
                    }
            },
            // L tetronimoe
            {
                    // Rotation 1
                    {
                            { false, false, true, false },
                            { true, true, true, false },
                            { false, false, false, false },
                            { false, false, false, false }
                    },
                    // Rotation 2
                    {
                            { true, true, false, false },
                            { false, true, false, false },
                            { false, true, false, false },
                            { false, false, false, false }
                    },
                    // Rotation 3
                    {
                            { true, true, true, false },
                            { true, false, false, false },
                            { false, false, false, false },
                            { false, false, false, false }
                    },
                    // Rotation 4
                    {
                            { true, false, false, false },
                            { true, false, false, false },
                            { true, true, false, false },
                            { false, false, false, false }
                    }
            },
            // O tetronimoe
            {
                    // Only one rotarion -- shape doesn't change
                    {
                            { true, true, false, false },
                            { true, true, false, false },
                            { false, false, false, false },
                            { false, false, false, false },
                    }
            },
            // S tetronimoe
            {
                    // Rotation 1
                    {
                            { false, true, true, false },
                            { true, true, false, false },
                            { false, false, false, false },
                            { false, false, false, false }
                    },
                    // Rotation 2 -- only needs 2 rotations, repeats after that
                    {
                            { true, false, false, false },
                            { true, true, false, false },
                            { false, true, false, false },
                            { false, false, false, false }
                    }
            },
            // T tetronimoe
            {
                    // Rotation 1
                    {
                            { false, true, false, false },
                            { true, true, true, false },
                            { false, false, false, false },
                            { false, false, false, false }
                    },
                    // Rotation 2
                    {
                            { false, true, false, false },
                            { true, true, false, false },
                            { false, true, false, false },
                            { false, false, false, false }
                    },
                    // Rotation 3
                    {
                            { true, true, true, false },
                            { false, true, false, false },
                            { false, false, false, false },
                            { false, false, false, false }
                    },
                    // Rotation 4
                    {
                            { true, false, false, false },
                            { true, true, false, false },
                            { true, false, false, false },
                            { false, false, false, false }
                    }
            },
            // Z tetronimoe
            {
                    // Rotation 1
                    {
                            { true, true, false, false },
                            { false, true, true, false },
                            { false, false, false, false },
                            { false, false, false, false }
                    },
                    // Rotation 2 -- only needs 2 rotations, repeats after that
                    {
                            { false, true, false, false },
                            { true, true, false, false },
                            { true, false, false, false },
                            { false, false, false, false }
                    }
            }
    };
    // #endregion

    public static void main(String[] args) {
        ArrayList<JPanel> gameSquares = new ArrayList<JPanel>();
        ArrayList<JPanel> nextSquares = new ArrayList<JPanel>();
        JFrame frame = new JFrame("Tetris");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 2));
        JPanel gamePanel = new JPanel();
        gamePanel.setBackground(Color.RED);
        createSquareLayout(gamePanel, gameSquares, 20, 10);
        panel.add(gamePanel);
        JPanel infoPanel = new JPanel();
        infoPanel.setBackground(Color.BLUE);
        infoPanel.setLayout(new GridLayout(3, 1));
        JLabel pointsLabel = new JLabel("Points: 0", SwingConstants.CENTER);
        pointsLabel.setFont(new Font("Arial", Font.PLAIN, 32));
        pointsLabel.setForeground(Color.WHITE);
        infoPanel.add(pointsLabel);
        JLabel cleansLabel = new JLabel("Cleans: 0", SwingConstants.CENTER);
        cleansLabel.setFont(new Font("Arial", Font.PLAIN, 32));
        cleansLabel.setForeground(Color.WHITE);
        infoPanel.add(cleansLabel);
        JPanel nextPanel = new JPanel();
        nextPanel.setBackground(Color.BLUE);
        nextPanel.setLayout(new GridLayout(2, 1));
        JLabel nextLabel = new JLabel("Next:", SwingConstants.CENTER);
        nextLabel.setFont(new Font("Arial", Font.PLAIN, 32));
        nextLabel.setForeground(Color.WHITE);
        nextPanel.add(nextLabel);
        JPanel nextSquaresPanel = new JPanel();
        createSquareLayout(nextSquaresPanel, nextSquares, 2, 4);
        nextPanel.add(nextSquaresPanel);
        infoPanel.add(nextPanel);
        panel.add(infoPanel);
        frame.add(panel);
        frame.setSize(1000, 1000);
        frame.setVisible(true);
    }

    private static void createSquareLayout(JPanel p, ArrayList<JPanel> squares, int rows, int cols) {
        p.setLayout(new GridLayout(rows, cols));
        for (int i = 0; i < rows * cols; i++) {
            JPanel newPanel = new JPanel();
            newPanel.setBackground(Color.WHITE);
            newPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            p.add(newPanel);
            squares.add(newPanel);
        }
    }

    private static JPanel getSquare(ArrayList<JPanel> squares, int x, int y) {
        JPanel square = squares.get(y * 10 + x);
        return square;
    }

    public static void drawShape(ArrayList<JPanel> squares, int startX, int startY, Color c, int shape, int rot) {

    }
}