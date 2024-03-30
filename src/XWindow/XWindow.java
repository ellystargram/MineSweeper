package XWindow;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import java.awt.Point;
import java.awt.PopupMenu;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowFocusListener;

public class XWindow extends JFrame {
    private int xSize = 500;
    private int ySize = 500;
    private Point location = null;
    private String title = "Untitled";
    private JLabel titleLabel = new JLabel(title);
    private boolean enableDarkMode = true;
    private boolean enableMaximize = false;
    private boolean enableMinimize = false;
    private JPanel contentScreen = new JPanel(new BorderLayout());
    private ImageIcon closeAlive = new ImageIcon(new ImageIcon("./src/XWindow/closeAlive.png").getImage()
            .getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH));
    private ImageIcon closeDead = new ImageIcon(new ImageIcon("./src/XWindow/closeDead.png").getImage()
            .getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH));
    private ImageIcon miniAlive = new ImageIcon(new ImageIcon("./src/XWindow/miniAlive.png").getImage()
            .getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH));
    private ImageIcon miniDead = new ImageIcon(new ImageIcon("./src/XWindow/miniDead.png").getImage()
            .getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH));
    private ImageIcon maxAlive = new ImageIcon(new ImageIcon("./src/XWindow/maxAlive.png").getImage()
            .getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH));
    private ImageIcon maxDead = new ImageIcon(
            new ImageIcon("./src/XWindow/maxDead.png").getImage().getScaledInstance(20,
                    20, java.awt.Image.SCALE_SMOOTH));
    private ImageIcon disabled = new ImageIcon(new ImageIcon("./src/XWindow/disabled.png").getImage()
            .getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH));
    private JLabel closeButton = new JLabel(closeDead);
    private JLabel miniButton = new JLabel(miniDead);
    private JLabel maxButton = new JLabel(maxDead);
    private MouseMotionAdapter mouseMoveControl = new MouseMotionAdapter() {
        public void mouseMoved(MouseEvent evt) {
            super.mouseMoved(evt);
            boolean judge = false;
            if (evt.getSource().equals(closeButton)) {
                judge = true;
            } else if (evt.getSource().equals(miniButton) && enableMinimize) {
                judge = true;
            } else if (evt.getSource().equals(maxButton) && enableMaximize) {
                judge = true;
            }

            if (isFocused() == false) {
                closeButton.setIcon(disabled);
                miniButton.setIcon(disabled);
                maxButton.setIcon(disabled);
            } else if (judge) {
                closeButton.setIcon(closeAlive);
                if(enableMinimize){
                    miniButton.setIcon(miniAlive);
                }
                else{
                    miniButton.setIcon(disabled);
                }
                if(enableMaximize){
                    maxButton.setIcon(maxAlive);
                }
                else{
                    maxButton.setIcon(disabled);
                }
            } else {
                closeButton.setIcon(closeDead);
                if(enableMinimize){
                    miniButton.setIcon(miniDead);
                }
                else{
                    miniButton.setIcon(disabled);
                }
                if(enableMaximize){
                    maxButton.setIcon(maxDead);
                }
                else{
                    maxButton.setIcon(disabled);
                }
            }
        }
    };
    private WindowFocusListener windowFocusWindow = new WindowFocusListener() {
        public void windowGainedFocus(java.awt.event.WindowEvent evt) {
            boolean judge = false;
            if (evt.getSource().equals(closeButton)) {
                judge = true;
            } else if (evt.getSource().equals(miniButton) && enableMinimize) {
                judge = true;
            } else if (evt.getSource().equals(maxButton) && enableMaximize) {
                judge = true;
            }

            if (judge) {
                closeButton.setIcon(closeAlive);
                if(enableMinimize){
                    miniButton.setIcon(miniAlive);
                }
                else{
                    miniButton.setIcon(disabled);
                }
                if(enableMaximize){
                    maxButton.setIcon(maxAlive);
                }
                else{
                    maxButton.setIcon(disabled);
                }
            } else {
                closeButton.setIcon(closeDead);
                if(enableMinimize){
                    miniButton.setIcon(miniDead);
                }
                else{
                    miniButton.setIcon(disabled);
                }
                if(enableMaximize){
                    maxButton.setIcon(maxDead);
                }
                else{
                    maxButton.setIcon(disabled);
                }
            }
        }

        public void windowLostFocus(java.awt.event.WindowEvent evt) {
            closeButton.setIcon(disabled);
            miniButton.setIcon(disabled);
            maxButton.setIcon(disabled);
        }
    };

    public XWindow() {
        defaultSettings();
        this.getContentPane().setBackground(new Color(64, 64, 64));
        installTitleBar();
        this.setVisible(true);
    }

    public XWindow(int xSize, int ySize, String title) {
        System.out.println("constructor");
        this.xSize = xSize;
        this.ySize = ySize;
        this.title = title;

        defaultSettings();

        installTitleBar();
        this.setVisible(true);

        System.out.println(this.contentScreen != null);
    }

    public XWindow(int xSize, int ySize, String title, boolean enableDarkMode) {
        this.xSize = xSize;
        this.ySize = ySize;
        this.title = title;
        this.enableDarkMode = enableDarkMode;

        defaultSettings();

        installTitleBar();
        this.setVisible(true);
    }

    private void defaultSettings() {
        this.addWindowFocusListener(windowFocusWindow);
        this.addMouseMotionListener(mouseMoveControl);
        this.setUndecorated(true);
        this.setSize(xSize, ySize);
        this.setLocationRelativeTo(null);
        this.setTitle(title);
        titleLabel.setOpaque(true);
        titleLabel.setText(title);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        super.add(contentScreen);
        this.setDefaultCloseOperation(2);
        this.setResizable(enableMaximize);
    }

    private void installTitleBar() {
        setVisible(true);
        titleLabel.setPreferredSize(new Dimension(getWidth(), 30));
        titleLabel.setLayout(null);
        setDarkMode(enableDarkMode);

        closeButton.setPreferredSize(new Dimension(20, 20));
        closeButton.setBounds(5, 5, 20, 20);
        closeButton.addMouseMotionListener(mouseMoveControl);
        closeButton.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent evt) {
                super.mouseReleased(evt);
                if (evt.getButton() == MouseEvent.BUTTON1) {
                    dispose();
                }
            }
        });
        titleLabel.add(closeButton);

        miniButton.setPreferredSize(new Dimension(20, 20));
        miniButton.setBounds(30, 5, 20, 20);
        miniButton.addMouseMotionListener(mouseMoveControl);
        miniButton.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent evt) {
                super.mouseReleased(evt);
                if (evt.getButton() == MouseEvent.BUTTON1 && enableMinimize) {
                    setState(ICONIFIED);
                }
            }
        });
        titleLabel.add(miniButton);

        maxButton.setPreferredSize(new Dimension(20, 20));
        maxButton.setBounds(55, 5, 20, 20);
        maxButton.addMouseMotionListener(mouseMoveControl);
        maxButton.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent evt) {
                super.mouseReleased(evt);
                if (evt.getButton() == MouseEvent.BUTTON1 && enableMaximize) {
                    if (getExtendedState() == NORMAL) {
                        location = getLocationOnScreen();
                        setExtendedState(MAXIMIZED_BOTH);

                    } else if (getExtendedState() == MAXIMIZED_BOTH) {
                        setExtendedState(NORMAL);
                        setSize(xSize, ySize);
                        setLocation(location);
                    }

                    updateTitle();
                }

            }
        });
        titleLabel.add(maxButton);

        super.add(titleLabel, "North");

        MouseMotionAdapter mouseMoveTitleBar = new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent evt) {
                super.mouseDragged(evt);

                Point dragPoint = evt.getLocationOnScreen();
                int x = dragPoint.x - location.x;
                int y = dragPoint.y - location.y;
                setLocation(x, y);
            }
        };
        MouseAdapter mouseButtonTitleBar = new MouseAdapter() {
            public void mousePressed(MouseEvent evt) {
                super.mousePressed(evt);
                if (evt.getSource().equals(titleLabel) && evt.getButton() == 1) {
                    location = evt.getPoint();
                }
            }
        };
        titleLabel.addMouseMotionListener(mouseMoveControl);
        titleLabel.addMouseMotionListener(mouseMoveTitleBar);
        titleLabel.addMouseListener(mouseButtonTitleBar);

        changeTitle(title);

    }

    public void changeTitle(String title) {
        this.title = title;

        String tempTitle = title;
        int titleLength = title.length();
        int titleSize = titleLabel.getFont().getSize();
        int titleBarAvailableSize = getSize().width - 100;

        if (titleBarAvailableSize < 0) {
            titleBarAvailableSize = 1;
        }


        if (titleLength * titleSize > titleBarAvailableSize) {

            tempTitle = title.substring(0, titleBarAvailableSize/titleSize) + "...";
        }
        titleLabel.setText(tempTitle);
        titleLabel.setForeground(Color.white);
    }

    public void updateTitle() {
        String tempTitle = title;
        int titleLength = title.length();
        int titleSize = titleLabel.getFont().getSize();
        int titleBarAvailableSize = getSize().width - 100;

        if (titleBarAvailableSize < 0) {
            titleBarAvailableSize = 1;
        }

        if (titleLength * titleSize > titleBarAvailableSize) {
            tempTitle = title.substring(0, titleBarAvailableSize/titleSize) + "...";
        }
        titleLabel.setText(tempTitle);
    }

    public void setDarkMode(boolean darkmode) {
        this.enableDarkMode = darkmode;
        if (enableDarkMode) {
            contentScreen.setBackground(new Color(64, 64, 64));
            titleLabel.setBackground(Color.black);
        } else {
            contentScreen.setBackground(new Color(222, 222, 222));
            titleLabel.setBackground(Color.white);
        }
    }

    public boolean isDarkMode() {
        return this.enableDarkMode;
    }

    public Component add(Component comp) {
        if (this.contentScreen == null)
            return super.add(comp);
        else
            return contentScreen.add(comp);
    }

    public void add(PopupMenu popup) {
        if (contentScreen == null)
            super.add(popup);
        else
            contentScreen.add(popup);
    }

    public void add(Component comp, Object constraints) {
        if (this.contentScreen == null)
            super.add(comp, constraints);
        else
            contentScreen.add(comp, constraints);
    }

    public Component add(Component comp, int index) {
        if (this.contentScreen == null)
            return super.add(comp, index);
        else
            return contentScreen.add(comp, index);
    }

    public Component add(String name, Component comp) {
        if (this.contentScreen == null)
            return super.add(name, comp);
        else
            return contentScreen.add(name, comp);
    }

    public void add(Component comp, Object constraints, int index) {
        if (this.contentScreen == null)
            super.add(comp, constraints, index);
        else
            contentScreen.add(comp, constraints, index);
    }
    public void setMaximizeEnable(boolean enableMaximize) {
        this.enableMaximize = enableMaximize;
        this.setResizable(enableMaximize);
        if (enableMaximize) {
            maxButton.setIcon(maxDead);
        } else {
            maxButton.setIcon(disabled);
        }
    }
    public void setMinimizeEnable(boolean enableMinimize) {
        this.enableMinimize = enableMinimize;
        if (enableMinimize) {
            miniButton.setIcon(miniDead);
        } else {
            miniButton.setIcon(disabled);
        }
    }
}