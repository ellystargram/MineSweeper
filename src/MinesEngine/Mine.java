package MinesEngine;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import XWindow.XWindow;

public class Mine extends XWindow {
    JLabel[][] boardPixel;
    MineState[][] boardMineStates;
    FlagState[][] boardFlagStates;
    PixelState[][] boardPixelStates;

    ImageIcon[] mineCountIcon = new ImageIcon[9];
    ImageIcon FlagIcon;
    ImageIcon QuestionIcon;
    ImageIcon NoneIcon;
    ImageIcon FlagMismatchedIcon;
    ImageIcon MineIcon;

    JPanel board = new JPanel(null);
    int pixelSize = 20;
    JLabel leftMinesLabel;
    int leftMines = 0;
    int totalMines = 0;
    boolean gameStart = false;
    boolean enableTouchReaction = true;
    MouseAdapter mouseAdapter = new MouseAdapter() {
        public void mousePressed(MouseEvent e) {
            super.mousePressed(e);
            if (enableTouchReaction == false) {
                return;
            }
            if (e.getButton() == MouseEvent.BUTTON1) {
                if (e.getSource() instanceof JLabel) {
                    JLabel label = (JLabel) e.getSource();
                    for (int i = 0; i < boardPixel.length; i++) {
                        for (int j = 0; j < boardPixel[i].length; j++) {
                            if (label == boardPixel[i][j]) {
                                System.out.println("Left Clicked: " + i + ", " + j);
                                if (boardPixelStates[i][j] == PixelState.COVERED
                                        && boardFlagStates[i][j] != FlagState.FLAG) {
                                    if (gameStart == false) {
                                        gameStart = true;
                                        System.out.println("Game Started");
                                        plantMines(leftMines, i, j);
                                        System.out.println(leftMines);
                                    }

                                    uncover(i, j);

                                    if (countLeftTile() == totalMines) {
                                        JOptionPane.showMessageDialog(null, "You Win!");
                                        dispose();
                                    }
                                } else if (boardPixelStates[i][j] == PixelState.UNCOVERED) {
                                    if (boardMineStates[i][j] == MineState.NUMBER) {
                                        int xStart;
                                        int xEnd;
                                        int yStart;
                                        int yEnd;

                                        if (i == 0) {
                                            xStart = 0;
                                        } else {
                                            xStart = i - 1;
                                        }

                                        if (i == boardMineStates.length - 1) {
                                            xEnd = boardMineStates.length - 1;
                                        } else {
                                            xEnd = i + 1;
                                        }

                                        if (j == 0) {
                                            yStart = 0;
                                        } else {
                                            yStart = j - 1;
                                        }

                                        if (j == boardMineStates[i].length - 1) {
                                            yEnd = boardMineStates[i].length - 1;
                                        } else {
                                            yEnd = j + 1;
                                        }

                                        int flagCount = 0;
                                        for (int k = xStart; k <= xEnd; k++) {
                                            for (int l = yStart; l <= yEnd; l++) {
                                                if (boardFlagStates[k][l] == FlagState.FLAG) {
                                                    flagCount++;
                                                }
                                            }
                                        }
                                        if (flagCount == howManyMinesAroundHere(i, j)) {
                                            boolean mismatched = false;
                                            for (int k = xStart; k <= xEnd; k++) {
                                                for (int l = yStart; l <= yEnd; l++) {
                                                    if (k == i && l == j) {
                                                        continue;
                                                    }
                                                    if (boardPixelStates[k][l] == PixelState.COVERED
                                                            && ((boardFlagStates[k][l] == FlagState.FLAG
                                                                    && boardMineStates[k][l] != MineState.MINE)
                                                                    || (boardFlagStates[k][l] != FlagState.FLAG
                                                                            && boardMineStates[k][l] == MineState.MINE))) {
                                                        mismatched = true;
                                                        break;
                                                    }
                                                }
                                                if (mismatched) {
                                                    break;
                                                }
                                            }
                                            if (mismatched) {
                                                JOptionPane.showMessageDialog(null, "You Lose!");
                                                enableTouchReaction = false;
                                                revealMines();
                                                changeTitle("You Lose!");
                                                return;
                                            } else {
                                                for (int x = xStart; x <= xEnd; x++) {
                                                    for (int y = yStart; y <= yEnd; y++) {
                                                        if (x == i && y == j) {
                                                            continue;
                                                        }
                                                        if (boardPixelStates[x][y] == PixelState.COVERED
                                                                && boardFlagStates[x][y] != FlagState.FLAG)
                                                            uncover(x, y);
                                                    }
                                                }
                                            }
                                            if (countLeftTile() == totalMines) {
                                                JOptionPane.showMessageDialog(null, "You Win!");
                                                dispose();
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } else if (e.getButton() == java.awt.event.MouseEvent.BUTTON3) {
                if (e.getSource() instanceof JLabel) {
                    JLabel label = (JLabel) e.getSource();
                    for (int i = 0; i < boardPixel.length; i++) {
                        for (int j = 0; j < boardPixel[i].length; j++) {
                            if (label == boardPixel[i][j]) {
                                System.out.println("Right Clicked: " + i + ", " + j);
                                if (boardPixelStates[i][j] == PixelState.COVERED) {
                                    if (boardFlagStates[i][j] == FlagState.NONE) {
                                        boardFlagStates[i][j] = FlagState.FLAG;
                                        boardPixel[i][j].setIcon(FlagIcon);
                                        leftMines--;
                                        leftMinesLabel.setText("Mines Left: " + leftMines);
                                    } else if (boardFlagStates[i][j] == FlagState.FLAG) {
                                        boardFlagStates[i][j] = FlagState.QUESTION;
                                        boardPixel[i][j].setIcon(QuestionIcon);
                                        leftMines++;
                                        leftMinesLabel.setText("Mines Left: " + leftMines);
                                    } else if (boardFlagStates[i][j] == FlagState.QUESTION) {
                                        boardFlagStates[i][j] = FlagState.NONE;
                                        boardPixel[i][j].setIcon(NoneIcon);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        };
    };

    public Mine(int width, int height, int mines) {
        super(1280, 720, "Minesweeper", true);
        totalMines = mines;
        setMinimizeEnable(true);
        setMaximizeEnable(false);
        pixelSize = Math.min(1000 / width, 500 / height);
        setSize(pixelSize * width + 100, pixelSize * height + 200);

        for (int i = 0; i < 9; i++) {
            mineCountIcon[i] = new ImageIcon(new ImageIcon("src/MinesEngine/" + i + ".png").getImage()
                    .getScaledInstance(pixelSize, pixelSize, Image.SCALE_DEFAULT));
        }
        FlagIcon = new ImageIcon(
                new ImageIcon("src/MinesEngine/coveredFlag.png").getImage().getScaledInstance(pixelSize,
                        pixelSize, Image.SCALE_DEFAULT));
        QuestionIcon = new ImageIcon(new ImageIcon("src/MinesEngine/coveredQuestion.png").getImage()
                .getScaledInstance(pixelSize, pixelSize, Image.SCALE_DEFAULT));
        NoneIcon = new ImageIcon(new ImageIcon("src/MinesEngine/coveredDefault.png").getImage()
                .getScaledInstance(pixelSize, pixelSize, Image.SCALE_DEFAULT));
        FlagMismatchedIcon = new ImageIcon(new ImageIcon("src/MinesEngine/coveredFlagMismatched.png").getImage()
                .getScaledInstance(pixelSize, pixelSize, Image.SCALE_SMOOTH));
        MineIcon = new ImageIcon(new ImageIcon("src/MinesEngine/coveredMine.png").getImage()
                .getScaledInstance(pixelSize, pixelSize, Image.SCALE_SMOOTH));

        leftMines = mines;
        leftMinesLabel = new JLabel("Mines Left: " + leftMines);

        boardPixel = new JLabel[width][height];
        boardMineStates = new MineState[width][height];
        boardFlagStates = new FlagState[width][height];
        boardPixelStates = new PixelState[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                boardPixel[i][j] = new JLabel();
                boardMineStates[i][j] = MineState.EMPTY;
                boardFlagStates[i][j] = FlagState.NONE;
                boardPixelStates[i][j] = PixelState.COVERED;
                boardPixel[i][j].setBounds(i * pixelSize + 50, j * pixelSize + 50, pixelSize, pixelSize);
                boardPixel[i][j].setOpaque(true);
                boardPixel[i][j].setBackground(new Color(86, 86, 86));
                boardPixel[i][j].setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.BLACK));
                board.add(boardPixel[i][j]);
                boardPixel[i][j].addMouseListener(mouseAdapter);
                boardPixel[i][j].setIcon(NoneIcon);
            }
        }
        board.add(leftMinesLabel);

        add(board);
        board.setBackground(new Color(64, 64, 64));

        leftMinesLabel.setFont(new Font("", Font.PLAIN, 20));
        leftMinesLabel.setForeground(Color.WHITE);
        leftMinesLabel.setBounds(((pixelSize * width + 100) - 200) / 2, (pixelSize * height + 200) - 60, 200, 20);
        setVisible(true);
    }

    private void plantMines(int mines, int firstClickedX, int firstClickedY) {
        if (mines < 0) {
            throw new IllegalArgumentException("mines is negative");
        }

        final int boardWidth = boardPixel.length;
        final int boardHeight = boardPixel[0].length;

        if (firstClickedX < 0 || firstClickedX >= boardWidth) {
            throw new IllegalArgumentException("firstClickedX is out of bounds");
        }
        if (firstClickedY < 0 || firstClickedY >= boardHeight) {
            throw new IllegalArgumentException("firstClickedY is out of bounds");
        }

        final int boardSize = boardWidth * boardHeight;

        ArrayList<Integer> remainBlockIndexes = new ArrayList<>(boardSize);

        for (int x = 0; x < boardWidth; x++) {
            for (int y = 0; y < boardHeight; y++) {
                if ((x >= firstClickedX - 1 && x <= firstClickedX + 1)
                        && (y >= firstClickedY - 1 && y <= firstClickedY + 1)) {
                    continue;
                }
                remainBlockIndexes.add(y * boardWidth + x);
            }
        }

        if (mines > remainBlockIndexes.size()) {
            throw new IllegalArgumentException("mines is greater than possible");
        }

        if (mines == remainBlockIndexes.size()) {
            var iterator = remainBlockIndexes.iterator();

            while (iterator.hasNext()) {
                final int blockIndex = iterator.next();
                final int blockX = blockIndex % boardWidth;
                final int blockY = blockIndex / boardWidth;

                boardMineStates[blockX][blockY] = MineState.MINE;
            }
        } else {
            Random random = new Random();
            for (int i = 0; i < mines; i++) {
                final int selectedIndex = random.nextInt(remainBlockIndexes.size());
                final int selectedBlockIndex = remainBlockIndexes.get(selectedIndex);
                remainBlockIndexes.remove(selectedIndex);

                final int blockX = selectedBlockIndex % boardWidth;
                final int blockY = selectedBlockIndex / boardWidth;
                boardMineStates[blockX][blockY] = MineState.MINE;
            }
        }

    }

    private int howManyMinesAroundHere(int x, int y) {
        int mines = 0;
        int xStart;
        int xEnd;
        int yStart;
        int yEnd;

        if (x == 0) {
            xStart = 0;
        } else {
            xStart = x - 1;
        }

        if (x == boardMineStates.length - 1) {
            xEnd = boardMineStates.length - 1;
        } else {
            xEnd = x + 1;
        }

        if (y == 0) {
            yStart = 0;
        } else {
            yStart = y - 1;
        }

        if (y == boardMineStates[x].length - 1) {
            yEnd = boardMineStates[x].length - 1;
        } else {
            yEnd = y + 1;
        }

        for (int i = xStart; i <= xEnd; i++) {
            for (int j = yStart; j <= yEnd; j++) {
                if (boardMineStates[i][j] == MineState.MINE) {
                    mines++;
                }
            }
        }
        return mines;
    }

    private void uncover(int x, int y) {
        if (boardPixelStates[x][y] == PixelState.COVERED && boardFlagStates[x][y] == FlagState.FLAG) {
            return;
        }
        int xStart;
        int xEnd;
        int yStart;
        int yEnd;

        if (boardMineStates[x][y] == MineState.MINE) {
            JOptionPane.showMessageDialog(null, "You Lose!");
            enableTouchReaction = false;
            revealMines();
            changeTitle("You Lose!");
            return;
        }

        boardPixelStates[x][y] = PixelState.UNCOVERED;
        boardPixel[x][y].setBackground(new Color(34, 34, 34));

        boardPixel[x][y].setIcon(mineCountIcon[howManyMinesAroundHere(x, y)]);
        if (howManyMinesAroundHere(x, y) != 0) {
            boardMineStates[x][y] = MineState.NUMBER;
            return;
        }

        if (x == 0) {
            xStart = 0;
        } else {
            xStart = x - 1;
        }

        if (x == boardMineStates.length - 1) {
            xEnd = boardMineStates.length - 1;
        } else {
            xEnd = x + 1;
        }

        if (y == 0) {
            yStart = 0;
        } else {
            yStart = y - 1;
        }

        if (y == boardMineStates[x].length - 1) {
            yEnd = boardMineStates[x].length - 1;
        } else {
            yEnd = y + 1;
        }

        for (int i = xStart; i <= xEnd; i++) {
            for (int j = yStart; j <= yEnd; j++) {
                if (boardPixelStates[i][j] == PixelState.COVERED) {
                    uncover(i, j);
                }
            }
        }

    }

    private int countLeftTile() {
        int leftTile = 0;
        for (int i = 0; i < boardPixelStates.length; i++) {
            for (int j = 0; j < boardPixelStates[i].length; j++) {
                if (boardPixelStates[i][j] == PixelState.COVERED) {
                    leftTile++;
                }
            }
        }
        return leftTile;
    }

    private void revealMines()
    {
        final int boardWidth = boardPixel.length;
        final int boardHeight = boardPixel[0].length;
        
        for (int x = 0; x < boardWidth; x++) {
            for (int y = 0; y < boardHeight; y++) {
                if (boardPixelStates[x][y] == PixelState.COVERED) {
                    if (boardMineStates[x][y] == MineState.MINE) {
                        if (boardFlagStates[x][y] != FlagState.FLAG) {
                            boardPixel[x][y].setIcon(MineIcon);
                        }
                    } else {
                        if (boardFlagStates[x][y] == FlagState.FLAG) {
                            boardPixel[x][y].setIcon(FlagMismatchedIcon);
                        }
                    }
                }
            }
        }
    }

    enum MineState {
        MINE,
        EMPTY,
        NUMBER
    }

    enum FlagState {
        FLAG,
        QUESTION,
        NONE
    }

    enum PixelState {
        COVERED,
        UNCOVERED
    }
}
