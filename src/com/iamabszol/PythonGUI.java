package com.iamabszol;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

//import py4j.GatewayServer;

public class PythonGUI {

    private int numberOfPlayers = 0;
    private int boardWidth = 0;
    private int boardHeight = 0;

    private Board[] myBoards = null;
    private final int cellBuffer = 0;
    private final int boardBuffer = 0;
    private int expander = 20;
    private int drawCols = 10;

    private JFrame frame = null;
    private JPanel panel = null;

    private int[][] text_boards = null;
    private int[] test_genes = null;

    // removed constructor as Py4J requires initialize through app

    public void setup(int numberOfPlayers, int boardWidth, int boardHeight, int expander, int drawCols) {
        this.numberOfPlayers = numberOfPlayers;
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;

        // optional
        if(expander >= 5) {
        	this.expander = expander;
        }
        if(drawCols > 0) {
        	this.drawCols = drawCols;
        }
        constructGUI();
    }

    private void constructGUI() {

        int widthPlayers = 0;
        int heightPlayers = 0;
        if(numberOfPlayers % drawCols == 0) {
            widthPlayers = drawCols;
            heightPlayers = (numberOfPlayers / drawCols) - 1;
        } else {
            if(numberOfPlayers < drawCols) {
                widthPlayers = numberOfPlayers;
                heightPlayers = 0;
            } else {
                widthPlayers = drawCols;
                heightPlayers = numberOfPlayers / drawCols;
            }
        }

        frame = new JFrame("Python GUI - Tetris Players");
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel = new JPanel();
        panel.setLayout(null);
        panel.setSize(new Dimension(boardWidth * expander + ((widthPlayers - 1) * boardWidth * expander), boardHeight * expander + (heightPlayers * boardHeight * expander)));

        myBoards = new Board[numberOfPlayers];

        int row = 0;
        for(int i = 0; i < myBoards.length; i++) {
            myBoards[i] = new Board(boardWidth * expander, boardHeight * expander, cellBuffer, boardBuffer);
            panel.add(myBoards[i]);
            if(i % drawCols == 0 && i != 0) {
                row++;
            }
            myBoards[i].setBounds((i % drawCols) * boardWidth * expander, row * boardHeight * expander, boardWidth * expander, boardHeight * expander);
        }

        frame.add(panel);
        frame.setSize(new Dimension(boardWidth * expander + ((widthPlayers - 1) * boardWidth * expander), boardHeight * expander + (heightPlayers * boardHeight * expander)));
        frame.setResizable(false);
        frame.setVisible(true);

    }

    private void apply_test() {

        ArrayList<int[][]> master = new ArrayList<>();
        for(int i = 0; i < myBoards.length; i++) {
            text_boards = new int[boardHeight][boardWidth];
            for(int r = 0; r < text_boards.length; r++) {
                for(int c = 0; c < text_boards[r].length; c++) {
                    text_boards[r][c] = 1;
                }
            }

            test_genes = new int[4];
            for(int g = 0; g < test_genes.length; g++) {
                test_genes[g] = new Random().nextInt(10);
            }
            myBoards[i].draw(null, test_genes, 0);
        }
    }

    public Board getBoard(int index) {
        if(myBoards != null) {
            if(index < myBoards.length) {
                return myBoards[index];
            }
            System.err.println("Index out of bounds!");
            return null;
        }
        return null;
    }

    class Board extends JPanel {

    	String message = null;
    	
        int width = 0;
        int height = 0;

        int[][] board = null;
        int[] genes = null;
        int score = 0;

        public Board(int width, int height, int cellBuffer, int boardBuffer) {
            this.width = width;
            this.height = height;
            setLayout(null);
            setSize(width, height);
        }
        
        // convert byte array to integer matrix, 2d and 1d support
        // https://stackoverflow.com/questions/36453353/using-py4j-to-send-matrices-to-from-python-to-java-as-int-arrays
        public int[][] convertToIntegerMatrix2D(byte[] data) {
        	java.nio.ByteBuffer buf = java.nio.ByteBuffer.wrap(data);
        	int n = buf.getInt(), m = buf.getInt();
        	int[][] matrix = new int[n][m];
        	for (int i = 0; i < n; ++i)
                for (int j = 0; j < m; ++j)
                   matrix[i][j] = buf.getInt();
        	return matrix;
        }
        
        public int[] convertToIntegerMatrix1D(byte[] data) {
        	java.nio.ByteBuffer buf = java.nio.ByteBuffer.wrap(data);
        	int n = buf.getInt();
        	int[] matrix = new int[n];
        	for (int i = 0; i < n; ++i)
                matrix[i] = buf.getInt();
        	return matrix;
        }

        // python literally has max() to do this =[
        private int getMaxRow() {
            int[] results = {0,0};
            for(int r = 0; r < board.length; r++) {
                int sum = 0;
                for(int c = 0; c < board[r].length; c++) {
                    sum += (int) board[r][c];
                }
                if(sum > results[0]) {
                    results[1] = r;
                }
            }
            return results[1];
        }
        
        public void drawText(String message) {
        	this.message = message;
        	board = null;
        	genes = null;
        	score = 0;
        	repaint();
        }

        public void draw(int[][] board, int[] genes, int score) {
            this.board = board;
            this.genes = genes;
            this.score = score;
            repaint();
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Color[] colors = new Color[]{Color.red, Color.green, Color.blue, Color.black, Color.yellow, Color.pink, Color.cyan, Color.orange};
            int[] sizes = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20};
            g.setColor(Color.white);
            g.fillRect(0, 0, width, height);
            g.setColor(colors[new Random().nextInt(colors.length)]);
            g.drawRect(0, 0, width - 1, height - 1);
            if(board != null && genes != null) {
                // calculate appropriate font size (allows for dynamic changing)
                int calcRow = getMaxRow();
                int bestSize = 0;
                for (int s = 0; s < sizes.length; s++) {
                    g.setFont(new Font("Arial", Font.BOLD, sizes[s]));
                    String line = "";
                    for (int i = 0; i < board[calcRow].length; i++) {
                        line = line + "\t" + board[calcRow][i];
                    }
                    if (g.getFontMetrics().stringWidth(line) < width && (g.getFontMetrics().getHeight() * (boardHeight + 3)) < height) {
                        bestSize = sizes[s];
                    } else
                        break;
                }
                g.setFont(new Font("Arial", Font.BOLD, bestSize));
                g.setColor(Color.black);
                int row = g.getFontMetrics().getHeight();
                int col = 0;
                for (int r = 0; r < board.length; r++) {
                    String line = "";
                    for (int c = 0; c < board[r].length; c++) {
                        //g.drawString("" + board[r][c], col, row);
                        //col += g.getFontMetrics().stringWidth("" + board[r][c]) + cellBuffer;
                        line = line + " " + board[r][c];
                    }
                    g.drawString(line, col, row);
                    row += g.getFontMetrics().getHeight();
                }
                // draw genes if there's enough room
                if(row < (height - g.getFontMetrics().getHeight())) {
                    String line = "[";
                    for(int c = 0; c < genes.length; c++) {
                        line = line + genes[c] + ",";
                    }
                    line = line.substring(0, line.length() - 1) + "]";
                    g.drawString("Genes: " + line, 0, row);
                    row += g.getFontMetrics().getHeight();
                    g.drawString("Score: " + score, 0, row);
                }
            } else {
                int bestSize = 0;
                String msg = "Completed or yet to begin";
                if(message != null) msg = message;
                for (int s = 0; s < sizes.length; s++) {
                    g.setFont(new Font("Arial", Font.BOLD, sizes[s]));
                    if (g.getFontMetrics().stringWidth(msg) < width) {
                        bestSize = sizes[s];
                    } else
                        break;
                }
                g.setFont(new Font("Arial", Font.BOLD, bestSize));
                g.setColor(Color.black);
                g.drawString(msg, (width / 2) - (g.getFontMetrics().stringWidth(msg) / 2), (height / 2) - (g.getFontMetrics().getHeight() / 2));
            }

        }

    }

    /*
    public static void main(String[] args) {
        // kept name app for directive for any further modification
        PythonGUI app = new PythonGUI();
        GatewayServer server = new GatewayServer(app);
        server.start();
    }
     */
}
