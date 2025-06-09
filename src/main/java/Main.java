// Developer: Lincoln Farkas

package src.main.java;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 * Main class for the Tetris game.
 * Handles game initialization, UI setup, game loop, and user input.
 */
public class Main {
        // Stores the panels representing the main game grid (20x10)
        private static ArrayList<JPanel> gameSquares = new ArrayList<JPanel>();
        // Stores the panels representing the "next" piece preview (2x4)
        private static ArrayList<JPanel> nextSquares = new ArrayList<JPanel>();

        // Player's current score and number of lines cleared
        private static int points = 0;
        private static int cleans = 0;

        // Current and next piece color and shape info
        private static Color currentColor;
        private static int currentShape;
        private static int currentRot;
        private static Color nextColor;
        private static int nextShape;
        private static boolean first = true; // True if the current piece is the first in a new round

        // UI labels for points and cleans
        private static JLabel pointsLabel;
        private static JLabel cleansLabel;
        private static Timer gameTimer; // Main game timer for piece falling
        private static JFrame frame;
        private static boolean stopped = false; // Game over state
        private static final Object gameLock = new Object(); // Synchronization lock for thread safety; had problems
                                                             // with race conditions between main loop and hard drop
                                                             // early on

        // Audio resources for background music and death sound
        private static AudioInputStream musicStream;
        private static AudioInputStream deathStream;
        private static Clip clip;

        // #region Colors
        /**
         * Array of possible colors for tetrominoes.
         * Using a wide palette for visual variety and to avoid color repetition.
         * 50 colors because max number of tetronimoes at once = 200 squares / 4 squares
         * per tetronimoe = 50 tetronimoes.
         */
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
        /**
         * 4D array representing all tetromino shapes and their rotations.
         * Each shape contains multiple rotations, each as a 4x4 boolean grid.
         * It took a while to make each rotation combo for each tetronimoe at first, but
         * it enabled quicker development than making an algorithm to programmatically
         * rotate the pieces.
         */
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

        /**
         * Loads an audio resource from the classpath and returns a buffered stream.
         * 
         * @param resourcePath Path to the audio resource.
         * @return Buffered AudioInputStream for playback.
         * @throws Exception if the resource is not found.
         */
        private static AudioInputStream getBufferedAudioInputStream(String resourcePath) throws Exception {
                InputStream resourceStream = Main.class.getResourceAsStream(resourcePath);
                if (resourceStream == null) {
                        throw new Exception("Resource not found: " + resourcePath);
                }
                // Learned that buffered input streams were required for packaged applications
                // when original implementation caused exception
                BufferedInputStream bufferedStream = new BufferedInputStream(resourceStream);
                return AudioSystem.getAudioInputStream(bufferedStream);
        }

        /**
         * Main entry point for the Tetris game.
         * Sets up the UI, initializes game state, and starts the game loop.
         */
        public static void main(String[] args) throws Exception {
                // JFrame and main panel setup
                frame = new JFrame("Tetris");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                frame.setResizable(true);
                frame.setSize(500, 500);
                frame.addComponentListener(new ComponentAdapter() {
                        @Override
                        public void componentResized(ComponentEvent e) {
                                // Ensure that window remains in square aspect ratio
                                int size = frame.getWidth();
                                frame.setSize(size, size);
                        }
                });

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
                frame.setVisible(true);
                frame.setFocusable(true);
                frame.requestFocusInWindow();

                // Initialize the first and next tetrominoes
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

                // Load and start background music
                musicStream = getBufferedAudioInputStream("/music.wav");
                clip = AudioSystem.getClip();
                clip.open(musicStream);
                clip.loop(Clip.LOOP_CONTINUOUSLY);
                clip.start();

                // Add key listener for player controls (move, rotate, drop, etc.)
                frame.addKeyListener(new KeyAdapter() {
                        public void keyPressed(KeyEvent e) {
                                // Prevent input if game is stopped
                                if (stopped)
                                        return;
                                synchronized (gameLock) {
                                        // Get current piece position
                                        int shapeY = getMinY(gameSquares, currentColor, 10);
                                        int shapeX = getMinX(gameSquares, currentColor, 10);

                                        // Handle left/right movement
                                        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                                                removeShape(gameSquares, currentColor);
                                                boolean possible = drawShape(gameSquares, shapeX - 1, shapeY, 10,
                                                                currentColor,
                                                                currentShape, currentRot);
                                                // All if(!possible) statements revert position/rotation if new values
                                                // would cause collisions or bound issues
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
                                        }

                                        // Handle rotation
                                        else if (e.getKeyCode() == KeyEvent.VK_UP) {
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
                                        }

                                        // Handle soft drop (down arrow)
                                        else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                                                gameTimer.setDelay(100);
                                        }

                                        // Handle hard drop (space bar)
                                        else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
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
                                                                checkCleans(gameSquares, 10);
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
                                                                clip.stop();
                                                                clip.close();
                                                                try {
                                                                        deathStream = getBufferedAudioInputStream(
                                                                                        "/death.wav");
                                                                        clip.open(deathStream);
                                                                } catch (Exception ex) {
                                                                        ex.printStackTrace();
                                                                }
                                                                clip.loop(0);
                                                                clip.start();
                                                                stopped = true;
                                                                int result = JOptionPane.showConfirmDialog(frame,
                                                                                "Game Over! Would you like to play again?",
                                                                                "Game Over",
                                                                                JOptionPane.YES_NO_OPTION);
                                                                if (result == JOptionPane.YES_OPTION) {
                                                                        points = 0;
                                                                        cleans = 0;
                                                                        pointsLabel.setText("Points: 0");
                                                                        cleansLabel.setText("Cleans: 0");
                                                                        for (Color c : COLORS) {
                                                                                removeShape(gameSquares, c);
                                                                                removeShape(nextSquares, c);
                                                                        }
                                                                        currentColor = getColor();
                                                                        currentShape = (int) (Math.random()
                                                                                        * SHAPES.length);
                                                                        drawShape(gameSquares, 4, 0, 10, currentColor,
                                                                                        currentShape, currentRot);
                                                                        nextColor = getColor();
                                                                        nextShape = (int) (Math.random()
                                                                                        * SHAPES.length);
                                                                        drawShape(nextSquares, 0, 0, 4, nextColor,
                                                                                        nextShape, 0);
                                                                        first = true;
                                                                        stopped = false;
                                                                        clip.stop();
                                                                        clip.close();
                                                                        try {
                                                                                musicStream = getBufferedAudioInputStream(
                                                                                                "/music.wav");
                                                                                clip.open(musicStream);
                                                                        } catch (Exception ex) {
                                                                                ex.printStackTrace();
                                                                        }
                                                                        clip.loop(Clip.LOOP_CONTINUOUSLY);
                                                                        clip.start();
                                                                        gameTimer.setDelay(750);
                                                                        gameTimer.start();
                                                                } else {
                                                                        System.exit(0);
                                                                }
                                                                break;
                                                        } else {
                                                                first = false;
                                                                points += 2;
                                                                pointsLabel.setText("Points: " + points);
                                                        }
                                                }
                                        }
                                }
                        }

                        public void keyReleased(KeyEvent e) {
                                // Restore normal drop speed after soft drop
                                if (stopped)
                                        return;
                                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                                        gameTimer.setDelay(750);
                                }
                        }
                });

                // Main game timer: moves the piece down at intervals
                gameTimer = new Timer(750, _ -> {
                        synchronized (gameLock) {
                                // Award extra points for soft drop
                                if (gameTimer.getDelay() == 100) {
                                        points++;
                                        pointsLabel.setText("Points: " + points);
                                }
                                // Move piece down, check for collision, handle new piece or game over
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
                                        checkCleans(gameSquares, 10);
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
                                        clip.stop();
                                        clip.close();
                                        try {
                                                deathStream = getBufferedAudioInputStream("/death.wav");
                                                clip.open(deathStream);
                                        } catch (Exception ex) {
                                                ex.printStackTrace();
                                        }
                                        clip.loop(0);
                                        clip.start();
                                        stopped = true;
                                        int result = JOptionPane.showConfirmDialog(frame,
                                                        "Game Over! Would you like to play again?",
                                                        "Game Over",
                                                        JOptionPane.YES_NO_OPTION);
                                        if (result == JOptionPane.YES_OPTION) {
                                                points = 0;
                                                cleans = 0;
                                                pointsLabel.setText("Points: 0");
                                                cleansLabel.setText("Cleans: 0");
                                                for (Color c : COLORS) {
                                                        removeShape(gameSquares, c);
                                                        removeShape(nextSquares, c);
                                                }
                                                currentColor = getColor();
                                                currentShape = (int) (Math.random() * SHAPES.length);
                                                drawShape(gameSquares, 4, 0, 10, currentColor, currentShape,
                                                                currentRot);
                                                nextColor = getColor();
                                                nextShape = (int) (Math.random() * SHAPES.length);
                                                drawShape(nextSquares, 0, 0, 4, nextColor, nextShape, 0);
                                                first = true;
                                                stopped = false;
                                                clip.stop();
                                                clip.close();
                                                try {
                                                        musicStream = getBufferedAudioInputStream("/music.wav");
                                                        clip.open(musicStream);
                                                } catch (Exception ex) {
                                                        ex.printStackTrace();
                                                }
                                                clip.loop(Clip.LOOP_CONTINUOUSLY);
                                                clip.start();
                                                gameTimer.setDelay(750);
                                                gameTimer.start();
                                        } else {
                                                System.exit(0);
                                        }
                                } else {
                                        first = false;
                                }
                        }
                });
                gameTimer.start();
        }

        /**
         * Creates a grid of JPanels for the game or next piece preview.
         * 
         * @param p       The parent panel.
         * @param squares The list to store the created panels.
         * @param rows    Number of rows.
         * @param cols    Number of columns.
         */
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

        /**
         * Returns the width (number of columns) of a tetromino pattern.
         * Used for collision and boundary checking.
         */
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

        /**
         * Returns the height (number of rows) of a tetromino pattern.
         * Used for collision and boundary checking.
         */
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

        /**
         * Attempts to draw a tetromino shape at the specified position.
         * Returns false if the shape would collide or go out of bounds.
         * 
         * @param squares The grid to draw on.
         * @param startX  X position.
         * @param startY  Y position.
         * @param width   Grid width.
         * @param c       Color of the shape.
         * @param shape   Shape index.
         * @param rot     Rotation index.
         * @return true if drawing was successful, false otherwise.
         */
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

        /**
         * Randomly selects a color for a new tetromino, ensuring it is not currently
         * used.
         * This avoids visual confusion between active and upcoming pieces.
         */
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

        /**
         * Finds the minimum X coordinate of the current piece in the grid.
         * Used for movement and collision logic.
         */
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

        /**
         * Finds the minimum Y coordinate of the current piece in the grid.
         * Used for movement and collision logic.
         */
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

        /**
         * Removes all squares of the given color from the grid.
         * Used to erase the current piece before moving or rotating.
         */
        private static void removeShape(ArrayList<JPanel> squares, Color color) {
                for (int i = 0; i < squares.size(); i++) {
                        if (squares.get(i).getBackground().equals(color))
                                squares.get(i).setBackground(Color.WHITE);
                }
        }

        /**
         * Checks for completed lines, clears them, and updates score and cleans.
         * Implements Tetris scoring: more points for clearing multiple lines at once.
         * 
         * @param squares The game grid.
         * @param width   Grid width.
         */
        private static void checkCleans(ArrayList<JPanel> squares, int width) {
                int numRows = squares.size() / width;
                ArrayList<Integer> fullRows = new ArrayList<>();
                // 1) collect full rows
                for (int row = 0; row < numRows; row++) {
                        boolean full = true;
                        for (int col = 0; col < width; col++) {
                                if (squares.get(row * width + col).getBackground().equals(Color.WHITE)) {
                                        full = false;
                                        break;
                                }
                        }
                        if (full) {
                                fullRows.add(row);
                        }
                }
                if (fullRows.isEmpty()) {
                        return;
                }
                // 2) clear each full row
                for (int row : fullRows) {
                        for (int col = 0; col < width; col++) {
                                squares.get(row * width + col).setBackground(Color.WHITE);
                        }
                }
                // 3) shift down rows above each cleared line
                Collections.sort(fullRows);
                for (int rowIndex : fullRows) {
                        for (int r = rowIndex; r > 0; r--) {
                                for (int col = 0; col < width; col++) {
                                        Color above = squares.get((r - 1) * width + col).getBackground();
                                        squares.get(r * width + col).setBackground(above);
                                }
                        }
                        // clear the now‐vacant top row
                        for (int col = 0; col < width; col++) {
                                squares.get(col).setBackground(Color.WHITE);
                        }
                }
                // 4) update score & cleans
                int lines = fullRows.size();
                cleans += lines;
                switch (lines) {
                        case 1:
                                points += 40;
                                break;
                        case 2:
                                points += 100;
                                break;
                        case 3:
                                points += 300;
                                break;
                        default:
                                points += 1200;
                                break; // tetris
                }
                pointsLabel.setText("Points: " + points);
                cleansLabel.setText("Cleans: " + cleans);
        }
}