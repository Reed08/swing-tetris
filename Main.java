import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Main {
        private static ArrayList<JPanel> gameSquares = new ArrayList<JPanel>();
        private static ArrayList<JPanel> nextSquares = new ArrayList<JPanel>();

        private static Color currentColor;
        private static int currentShape;
        private static int currentRot;
        private static Color nextColor;
        private static int nextShape;
        private static boolean first = true;
        private static JLabel pointsLabel;
        private static JLabel cleansLabel;
        private static Timer gameTimer;
        private static JFrame frame;
        private static boolean stopped = false;
        private static final Object gameLock = new Object();

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
                frame = new JFrame("Tetris");
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
                pointsLabel = new JLabel("Points: 0", SwingConstants.CENTER);
                pointsLabel.setFont(new Font("Arial", Font.PLAIN, 32));
                pointsLabel.setForeground(Color.WHITE);
                infoPanel.add(pointsLabel);
                cleansLabel = new JLabel("Cleans: 0", SwingConstants.CENTER);
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
                frame.setFocusable(true);
                frame.requestFocusInWindow();

                currentColor = getColor();
                currentShape = (int) (Math.random() * SHAPES.length);
                synchronized (gameLock) {
                        drawShape(gameSquares, 4, 0, 10, currentColor, currentShape, currentRot);
                }
                nextColor = getColor();
                nextShape = (int) (Math.random() * SHAPES.length);
                synchronized (gameLock) {
                        drawShape(nextSquares, 0, 0, 4, nextColor, nextShape, 0);
                }
                first = true;

                frame.addKeyListener(new KeyAdapter() {
                        public void keyPressed(KeyEvent e) {
                                if (stopped)
                                        return;
                                synchronized (gameLock) {
                                        int shapeY = getMinY(gameSquares, currentColor, 10);
                                        int shapeX = getMinX(gameSquares, currentColor, 10);
                                        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                                                removeShape(gameSquares, currentColor);
                                                boolean possible = drawShape(gameSquares, shapeX - 1, shapeY, 10,
                                                                currentColor,
                                                                currentShape, currentRot);
                                                if (!possible) {
                                                        removeShape(gameSquares, currentColor);
                                                        drawShape(gameSquares, shapeX, shapeY, 10, currentColor,
                                                                        currentShape,
                                                                        currentRot);
                                                }
                                        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                                                removeShape(gameSquares, currentColor);
                                                boolean possible = drawShape(gameSquares, shapeX + 1, shapeY, 10,
                                                                currentColor,
                                                                currentShape, currentRot);
                                                if (!possible) {
                                                        removeShape(gameSquares, currentColor);
                                                        drawShape(gameSquares, shapeX, shapeY, 10, currentColor,
                                                                        currentShape,
                                                                        currentRot);
                                                }
                                        } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                                                currentRot++;
                                                if (currentRot >= SHAPES[currentShape].length) {
                                                        currentRot = 0;
                                                }
                                                removeShape(gameSquares, currentColor);
                                                boolean possible = drawShape(gameSquares, shapeX, shapeY, 10,
                                                                currentColor,
                                                                currentShape, currentRot);
                                                if (!possible) {
                                                        currentRot--;
                                                        if (currentRot < 0) {
                                                                currentRot = SHAPES[currentShape].length - 1;
                                                        }
                                                        removeShape(gameSquares, currentColor);
                                                        drawShape(gameSquares, shapeX, shapeY, 10, currentColor,
                                                                        currentShape,
                                                                        currentRot);
                                                }
                                        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                                                gameTimer.setDelay(100);
                                        } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                                                int dropCount = 0;
                                                int maxDrops = 20;

                                                while (dropCount < maxDrops) {
                                                        shapeY = getMinY(gameSquares, currentColor, 10);
                                                        shapeX = getMinX(gameSquares, currentColor, 10);

                                                        if (shapeX == 10 || shapeY == 20) {
                                                                gameTimer.stop();
                                                                stopped = true;
                                                                break;
                                                        }

                                                        removeShape(gameSquares, currentColor);
                                                        boolean possible = drawShape(gameSquares, shapeX, shapeY + 1,
                                                                        10,
                                                                        currentColor, currentShape, currentRot);

                                                        dropCount++;

                                                        if (!possible && !first) {
                                                                removeShape(gameSquares, currentColor);
                                                                drawShape(gameSquares, shapeX, shapeY, 10, currentColor,
                                                                                currentShape, currentRot);
                                                                currentRot = 0;
                                                                drawShape(gameSquares, 4, 0, 10, nextColor, nextShape,
                                                                                currentRot);
                                                                first = true;
                                                                currentColor = nextColor;
                                                                currentShape = nextShape;
                                                                removeShape(nextSquares, nextColor);
                                                                nextColor = getColor();
                                                                nextShape = (int) (Math.random() * SHAPES.length);
                                                                drawShape(nextSquares, 0, 0, 4, nextColor, nextShape,
                                                                                0);
                                                                break;
                                                        } else if (!possible && first) {
                                                                removeShape(gameSquares, currentColor);
                                                                removeShape(nextSquares, nextColor);
                                                                gameTimer.stop();
                                                                stopped = true;
                                                                break;
                                                        } else {
                                                                first = false;
                                                        }
                                                }
                                        }
                                }
                        }

                        public void keyReleased(KeyEvent e) {
                                if (stopped)
                                        return;
                                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                                        gameTimer.setDelay(750);
                                }
                        }
                });

                gameTimer = new Timer(750, _ -> {
                        synchronized (gameLock) {
                                int shapeY = getMinY(gameSquares, currentColor, 10);
                                int shapeX = getMinX(gameSquares, currentColor, 10);
                                removeShape(gameSquares, currentColor);
                                boolean possible = drawShape(gameSquares, shapeX, shapeY + 1, 10, currentColor,
                                                currentShape,
                                                currentRot);
                                if (!possible && !first) {
                                        removeShape(gameSquares, currentColor);
                                        drawShape(gameSquares, shapeX, shapeY, 10, currentColor, currentShape,
                                                        currentRot);
                                        currentRot = 0;
                                        drawShape(gameSquares, 4, 0, 10, nextColor, nextShape, currentRot);
                                        first = true;
                                        currentColor = nextColor;
                                        currentShape = nextShape;
                                        removeShape(nextSquares, nextColor);
                                        nextColor = getColor();
                                        nextShape = (int) (Math.random() * SHAPES.length);
                                        drawShape(nextSquares, 0, 0, 4, nextColor, nextShape, 0);
                                } else if (!possible && first) {
                                        removeShape(gameSquares, currentColor);
                                        removeShape(nextSquares, nextColor);
                                        gameTimer.stop();
                                        stopped = true;
                                } else {
                                        first = false;
                                }
                        }
                });
                gameTimer.start();
        }

        private static void createSquareLayout(JPanel p, ArrayList<JPanel> squares, int rows, int cols) {
                p.removeAll();
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
                for (int i = 0; i < p.length; i++) {
                        for (int j = 0; j < p[i].length; j++) {
                                if (p[i][j] && i > largestHeight) {
                                        largestHeight = i;
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
                if (endIdx >= squares.size() || startX + getWidth(pattern) >= width || startX < 0) {
                        return false;
                }
                for (int i = startIdx; i <= endIdx; i++) {
                        if ((i - startIdx) / width < pattern.length && (i - startIdx) % width < pattern[0].length
                                        && pattern[(i - startIdx) / width][(i - startIdx) % width]) {
                                if (!squares.get(i).getBackground().equals(Color.WHITE)) {
                                        return false;
                                } else {
                                        squares.get(i).setBackground(c);
                                }
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

        private static void removeShape(ArrayList<JPanel> squares, Color color) {
                for (int i = 0; i < squares.size(); i++) {
                        if (squares.get(i).getBackground().equals(color))
                                squares.get(i).setBackground(Color.WHITE);
                }
        }
}