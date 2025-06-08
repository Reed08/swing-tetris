import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Main {
        private static ArrayList<JPanel> gameSquares = new ArrayList<JPanel>();
        private static ArrayList<JPanel> nextSquares = new ArrayList<JPanel>();

        // #region Colors
        private static final Color[] COLORS = {
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
        private static final boolean[][][][] SHAPES = {
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
                Color currentColor = getColor();
                int currentShape = (int) (Math.random() * SHAPES.length);
                drawShape(gameSquares, 4, 0, 10, currentColor, currentShape, 0);
                Color nextColor = getColor();
                int nextShape = (int) (Math.random() * SHAPES.length);
                drawShape(nextSquares, 0, 0, 4, nextColor, nextShape, 0);
                while (true) {
                        if (checkStopped(gameSquares, currentColor, 10)) {
                                drawShape(gameSquares, 4, 0, 10, nextColor, nextShape, 0);
                                currentColor = nextColor;
                                currentShape = nextShape;
                                nextColor = getColor();
                                nextShape = (int) (Math.random() * SHAPES.length);
                                createSquareLayout(nextSquaresPanel, nextSquares, 2, 4);
                                drawShape(nextSquares, 0, 0, 4, nextColor, nextShape, 0);
                        } else {
                                int shapeY = getMinY(gameSquares, currentColor, 10) + 1;
                                int shapeX = getMinX(gameSquares, currentColor, 10);
                                removeShape(gameSquares, currentColor);
                                drawShape(gameSquares, shapeX, shapeY, 10, currentColor, currentShape, 0);
                        }
                }
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

        private static int getWidth(boolean[][] p) {
                int largestWidth = 0;
                for (int i = 0; i < p.length; i++) {
                        for (int j = 0; j < p[i].length; j++) {
                                if (p[i][j] && j > largestWidth) {
                                        largestWidth = j;
                                }
                        }
                }
                return largestWidth;
        }

        private static int getHeight(boolean[][] p) {
                int largestHeight = 0;
                for (int i = 0; i < p[0].length; i++) {
                        for (int j = 0; j < p.length; j++) {
                                if (p[j][i] && j > largestHeight) {
                                        largestHeight = j;
                                }
                        }
                }
                return largestHeight;
        }

        private static boolean drawShape(ArrayList<JPanel> squares, int startX, int startY, int width, Color c,
                        int shape,
                        int rot) {
                int startIdx = startY * width + startX;
                while (rot >= SHAPES[shape].length) {
                        rot -= SHAPES[shape].length;
                }
                boolean[][] pattern = SHAPES[shape][rot];
                int endIdx = startIdx + getHeight(pattern) * width + getWidth(pattern);
                if (endIdx >= squares.size()) {
                        return false;
                }
                for (int i = startIdx; i <= endIdx; i++) {
                        if (pattern[(i - startIdx) / 4][(i - startIdx) % 4]) {
                                squares.get(i).setBackground(c);
                        }
                }
                return true;
        }

        private static Color getColor() {
                Color color = COLORS[(int) (Math.random() * COLORS.length)];
                boolean used = true;
                while (used) {
                        color = COLORS[(int) (Math.random() * COLORS.length)];
                        used = false;
                        for (int i = 0; i < gameSquares.size(); i++) {
                                if (gameSquares.get(i).getBackground().equals(color)) {
                                        used = true;
                                        break;
                                }
                        }
                        for (int i = 0; i < nextSquares.size(); i++) {
                                if (nextSquares.get(i).getBackground().equals(color)) {
                                        used = true;
                                        break;
                                }
                        }
                }
                return color;
        }

        private static int getMinX(ArrayList<JPanel> squares, Color color, int width) {
                int minColorX = width;
                for (int i = 0; i < squares.size(); i++) {
                        if (squares.get(i).getBackground().equals(color)) {
                                if (i % width < minColorX) {
                                        minColorX = i % width;
                                }
                        }
                }
                return minColorX;
        }

        private static int getMaxX(ArrayList<JPanel> squares, Color color, int width) {
                int maxColorX = -1;
                for (int i = 0; i < squares.size(); i++) {
                        if (squares.get(i).getBackground().equals(color)) {
                                if (i % width > maxColorX) {
                                        maxColorX = i % width;
                                }
                        }
                }
                return maxColorX;
        }

        private static int getMinY(ArrayList<JPanel> squares, Color color, int width) {
                int minColorY = squares.size() / width;
                for (int i = 0; i < squares.size(); i++) {
                        if (squares.get(i).getBackground().equals(color)) {
                                if (i / width < minColorY) {
                                        minColorY = i / width;
                                }
                        }
                }
                return minColorY;
        }

        private static int getMaxY(ArrayList<JPanel> squares, Color color, int width) {
                int maxColorY = -1;
                for (int i = 0; i < squares.size(); i++) {
                        if (squares.get(i).getBackground().equals(color)) {
                                maxColorY = i / width;
                        }
                }
                return maxColorY;
        }

        private static boolean checkStopped(ArrayList<JPanel> squares, Color color, int width) {
                int minColorX = getMinX(squares, color, width);
                int maxColorX = getMaxX(squares, color, width);
                int colorY = getMaxY(squares, color, width);
                if (colorY == squares.size() / width - 1)
                        return true;
                for (int i = minColorX; i <= maxColorX; i++) {
                        if (!squares.get((colorY + 1) * width + i).getBackground().equals(Color.WHITE)) {
                                return true;
                        }
                }
                return false;
        }

        private static void removeShape(ArrayList<JPanel> squares, Color color) {
                for (int i = 0; i < squares.size(); i++) {
                        if (squares.get(i).getBackground().equals(color))
                                squares.get(i).setBackground(Color.WHITE);
                }
        }
}