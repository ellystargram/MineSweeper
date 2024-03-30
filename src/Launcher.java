import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import MinesEngine.Mine;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import XWindow.XWindow;

public class Launcher extends XWindow {
    JComboBox<String> difficulty = new JComboBox<String>();
    JTextField width = new JTextField();
    JTextField height = new JTextField();
    JTextField mines = new JTextField();
    JButton start = new JButton("Start");
    JLabel difficultyLabel = new JLabel("Difficulty");
    JLabel widthLabel = new JLabel("Width");
    JLabel heightLabel = new JLabel("Height");
    JLabel minesLabel = new JLabel("Mines");
    String[] difficultyList = new String[] { "Easy", "Medium", "Hard", "Custom" };
    int widthValue = 0;
    int heightValue = 0;
    int minesValue = 0;
    int[] widthValueDifficulty = new int[] { 9, 16, 30 };
    int[] heightValueDifficulty = new int[] { 9, 16, 16 };
    int[] minesValueDifficulty = new int[] { 10, 40, 99 };
    KeyAdapter widthKeyAdapter = new KeyAdapter() {
        public void keyPressed(KeyEvent e) {
            super.keyPressed(e);
            if (KeyEvent.getKeyText(e.getKeyCode()).matches("\\d")) {
                difficulty.setSelectedIndex(3);
            } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                System.exit(1);
            }
        };

        public void keyReleased(KeyEvent e) {
            width.setText(width.getText().replaceAll("[^\\d]", ""));
        };
    };
    KeyAdapter heightKeyAdapter = new KeyAdapter() {
        public void keyPressed(KeyEvent e) {
            super.keyPressed(e);
            if (KeyEvent.getKeyText(e.getKeyCode()).matches("\\d")) {
                difficulty.setSelectedIndex(3);
            } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                System.exit(1);
            }
        };

        public void keyReleased(KeyEvent e) {
            height.setText(height.getText().replaceAll("[^\\d]", ""));
        };
    };
    KeyAdapter minesKeyAdapter = new KeyAdapter() {
        public void keyPressed(KeyEvent e) {
            super.keyPressed(e);
            if (KeyEvent.getKeyText(e.getKeyCode()).matches("\\d")) {
                difficulty.setSelectedIndex(3);
            } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                System.exit(1);
            }
        };

        public void keyReleased(KeyEvent e) {
            mines.setText(mines.getText().replaceAll("[^\\d]", ""));
        };
    };
    ActionListener difficultyActionListener = e -> {
        int index = difficulty.getSelectedIndex();
        if (index != 3) {
            width.setText(String.valueOf(widthValueDifficulty[index]));
            height.setText(String.valueOf(heightValueDifficulty[index]));
            mines.setText(String.valueOf(minesValueDifficulty[index]));
        }
    };
    ActionListener startActionListener = e -> {
        widthValue = Integer.parseInt(width.getText());
        heightValue = Integer.parseInt(height.getText());
        minesValue = Integer.parseInt(mines.getText());
        new Mine(widthValue, heightValue, minesValue);
        // dispose();
    };

    public static void main(String[] args) throws Exception {
        new Launcher();
    }

    public Launcher() {
        super(250, 300, "MineLauncher", true);
        setMinimizeEnable(true);
        setMaximizeEnable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel panel = new JPanel(null);
        add(panel);
        panel.setBackground(new Color(64, 64, 64));

        panel.add(difficultyLabel);
        panel.add(widthLabel);
        panel.add(heightLabel);
        panel.add(minesLabel);

        difficultyLabel.setForeground(Color.WHITE);
        widthLabel.setForeground(Color.WHITE);
        heightLabel.setForeground(Color.WHITE);
        minesLabel.setForeground(Color.WHITE);

        difficultyLabel.setBounds(20, 10, 100, 30);
        widthLabel.setBounds(20, 50, 100, 30);
        heightLabel.setBounds(20, 90, 100, 30);
        minesLabel.setBounds(20, 130, 100, 30);

        panel.add(difficulty);
        panel.add(width);
        panel.add(height);
        panel.add(mines);
        panel.add(start);

        difficulty.setBounds(130, 10, 100, 30);
        width.setBounds(130, 50, 100, 30);
        height.setBounds(130, 90, 100, 30);
        mines.setBounds(130, 130, 100, 30);
        start.setBounds(20, 170, 210, 60);

        for (int i = 0; i < difficultyList.length; i++) {
            difficulty.addItem(difficultyList[i]);
        }
        difficulty.setSelectedIndex(0);
        width.setText(String.valueOf(widthValueDifficulty[0]));
        height.setText(String.valueOf(heightValueDifficulty[0]));
        mines.setText(String.valueOf(minesValueDifficulty[0]));

        width.addKeyListener(widthKeyAdapter);
        height.addKeyListener(heightKeyAdapter);
        mines.addKeyListener(minesKeyAdapter);
        difficulty.addActionListener(difficultyActionListener);
        start.addActionListener(startActionListener);

        setVisible(true);
    }
}
