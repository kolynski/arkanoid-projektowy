import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.imageio.ImageIO; // Dodaj ten import
import java.io.File;         // Dodaj ten import

public class MenuPanel extends JPanel implements KeyListener {
    private JFrame parentFrame;
    private Image backgroundImage;
    private enum MenuState { MENU, HIGH_SCORES }
    private MenuState currentState;
    private ScoreManager scoreManager;

    public MenuPanel(JFrame frame) {
        this.parentFrame = frame;
        this.currentState = MenuState.MENU;
        this.scoreManager = new ScoreManager();
        
        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);
        
        loadBackground();
    }

    private void loadBackground() {
        try {
            backgroundImage = ImageIO.read(new File("assets/backgrounds/tlo1.jpg"));
        } catch (Exception e) {
            System.out.println("Error loading menu background: " + e.getMessage());
        }
    }

    private void drawStringWithShadow(Graphics g, String text, int x, int y) {
        g.setColor(Color.BLACK);
        g.drawString(text, x - 1, y - 1);
        g.drawString(text, x - 1, y + 1);
        g.drawString(text, x + 1, y - 1);
        g.drawString(text, x + 1, y + 1);
        
        g.setColor(Color.WHITE);
        g.drawString(text, x, y);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
        
        if (currentState == MenuState.MENU) {
            drawMenu(g);
        } else {
            drawHighScores(g);
        }
    }

    private void drawMenu(Graphics g) {
        g.setFont(new Font("Arial", Font.BOLD, 40));
        FontMetrics fm = g.getFontMetrics();
        
        String[] options = {
            "Press 'S' to Start Game",
            "Press 'H' for High Scores",
            "Press 'W' to Exit"
        };
        
        int centerY = getHeight() / 2 - 60;
        
        for (int i = 0; i < options.length; i++) {
            drawStringWithShadow(g, options[i], 
                (getWidth() - fm.stringWidth(options[i])) / 2, 
                centerY + i * 60);
        }
    }

    private void drawHighScores(Graphics g) {
        g.setFont(new Font("Arial", Font.BOLD, 32));
        FontMetrics fmTitle = g.getFontMetrics();
        String title = "HIGH SCORES";
        int titleX = (getWidth() - fmTitle.stringWidth(title)) / 2;
        drawStringWithShadow(g, title, titleX, 100);

        List<Score> scores = scoreManager.getHighScores();
        g.setFont(new Font("Arial", Font.BOLD, 20));
        FontMetrics fm = g.getFontMetrics();
        int y = 150;

        if (scores.isEmpty()) {
            String noScores = "No high scores yet!";
            int x = (getWidth() - fm.stringWidth(noScores)) / 2;
            drawStringWithShadow(g, noScores, x, y);
        } else {
            for (int i = 0; i < Math.min(10, scores.size()); i++) {
                Score score = scores.get(i);
                String display = String.format("%d. %s  %06d", 
                    i + 1, score.getName(), score.getScore());
                int x = (getWidth() - fm.stringWidth(display)) / 2;
                drawStringWithShadow(g, display, x, y);
                y += 30;
            }
        }

        g.setFont(new Font("Arial", Font.BOLD, 16));
        String returnText = "Press ESCAPE to return to Menu";
        int returnX = (getWidth() - g.getFontMetrics().stringWidth(returnText)) / 2;
        drawStringWithShadow(g, returnText, returnX, 500);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (currentState == MenuState.HIGH_SCORES) {
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                currentState = MenuState.MENU;
                SoundManager.playMenuMove();
                repaint();
                return;
            }
        } else {
            switch (Character.toLowerCase(e.getKeyChar())) {
                case 's':
                    startGame();
                    SoundManager.playMenuSelect();
                    break;
                case 'h':
                    currentState = MenuState.HIGH_SCORES;
                    SoundManager.playMenuMove();
                    repaint();
                    break;
                case 'w':
                    System.exit(0);
                    break;
            }
        }
    }

    private void startGame() {
        parentFrame.getContentPane().removeAll();
        GamePanel gamePanel = new GamePanel(Difficulty.EASY);
        parentFrame.add(gamePanel);
        parentFrame.pack();
        parentFrame.revalidate();
        gamePanel.startGame();
        gamePanel.requestFocusInWindow();
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}
}