import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import java.io.File;

public class GamePanel extends JPanel implements KeyListener, ActionListener {
    private Timer timer;
    private List<Ball> balls;
    private Paddle paddle;
    private List<Block> blocks;
    private int score = 0;
    private Difficulty difficulty;
    private int currentLevel = 1;
    private boolean levelCompleted = false;
    private boolean gameFinished = false;
    private boolean gameOver = false;
    private boolean gameStarted = false;
    private ScoreManager scoreManager;
    private Image backgroundImage;

    public GamePanel(Difficulty difficulty) {
        this.difficulty = difficulty;
        this.scoreManager = new ScoreManager();
        loadBackground(currentLevel);
        initializeGame();
    }

    private void loadBackground(int level) {
        try {
            String bgPath = "assets/backgrounds/level" + level + ".jpg";
            File bgFile = new File(bgPath);
            if (!bgFile.exists()) {
                System.out.println("Background file not found: " + bgPath);
                return;
            }
            backgroundImage = ImageIO.read(bgFile);
        } catch (Exception e) {
            System.out.println("Error loading background for level " + level + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void initializeGame() {
        setPreferredSize(new Dimension(800, 600));
        paddle = new Paddle(360, 550, 80, 10, 20);
        balls = new ArrayList<>();
        balls.add(new Ball(paddle.getX() + paddle.getWidth()/2 - 10,
                          paddle.getY() - 20,
                          20,
                          difficulty.getSpeed(),
                          difficulty.getSpeed()));
        blocks = new ArrayList<>();
        createBlocks();

        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);

        timer = new Timer(10, this);
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
        
        for (Ball ball : balls) {
            ball.draw(g);
        }
        
        paddle.draw(g);
        
        for (Block block : blocks) {
            block.draw(g);
        }

        g.setFont(new Font("Arial", Font.BOLD, 14));
        FontMetrics fm = g.getFontMetrics();
        
        String scoreText = "Score: " + score;
        String levelText = "Level: " + currentLevel;
        String ballsText = "Balls: " + balls.size();
        
        int scoreX = (getWidth() - fm.stringWidth(scoreText)) / 2 - 100;
        int levelX = (getWidth() - fm.stringWidth(levelText)) / 2;
        int ballsX = (getWidth() - fm.stringWidth(ballsText)) / 2 + 100;
        
        drawStringWithShadow(g, scoreText, scoreX, 20);
        drawStringWithShadow(g, levelText, levelX, 20);
        drawStringWithShadow(g, ballsText, ballsX, 20);

        if (!gameStarted && !gameOver && !levelCompleted) {
            g.setFont(new Font("Arial", Font.BOLD, 20));
            fm = g.getFontMetrics();
            String spaceText = "Press SPACE to start";
            drawStringWithShadow(g, spaceText, 
                (getWidth() - fm.stringWidth(spaceText)) / 2, 300);
        }

        if (gameOver || gameFinished) {
            if (scoreManager.isEnteringName()) {
                drawNameEntry(g);
            } else {
                if (gameOver) {
                    drawGameOver(g);
                }
                if (gameFinished) {
                    drawGameFinished(g);
                }
            }
        }

        if (levelCompleted && !gameFinished) {
            drawLevelCompleted(g);
        }
    }

    private void drawGameOver(Graphics g) {
        g.setFont(new Font("Arial", Font.BOLD, 24));
        FontMetrics fm = g.getFontMetrics();
        String loseText = "PRZEGRAŁEŚ!";
        int x = (getWidth() - fm.stringWidth(loseText)) / 2;
        
        g.setColor(Color.BLACK);
        g.drawString(loseText, x + 1, 251);
        g.setColor(Color.RED);
        g.drawString(loseText, x, 250);

        g.setFont(new Font("Arial", Font.BOLD, 18));
        fm = g.getFontMetrics();
        String[] messages = {
            "Twój wynik to: " + score,
            "Naciśnij 'R' aby spróbować ponownie",
            "Naciśnij 'E' aby wyjść do menu"
        };

        int y = 280;
        for (String msg : messages) {
            drawStringWithShadow(g, msg, 
                (getWidth() - fm.stringWidth(msg)) / 2, y);
            y += 30;
        }
    }

    private void drawGameFinished(Graphics g) {
        g.setFont(new Font("Arial", Font.BOLD, 24));
        FontMetrics fm = g.getFontMetrics();
        String[] messages = {
            "Gratulacje! Ukończyłeś grę!",
            "Końcowy wynik: " + score,
            "Naciśnij 'E' aby wrócić do menu"
        };

        int y = 250;
        for (String msg : messages) {
            if (y == 250) {
                g.setColor(Color.BLACK);
                g.drawString(msg, (getWidth() - fm.stringWidth(msg)) / 2 + 1, y + 1);
                g.setColor(Color.GREEN);
            }
            drawStringWithShadow(g, msg, (getWidth() - fm.stringWidth(msg)) / 2, y);
            y += 40;
        }
    }

    private void drawLevelCompleted(Graphics g) {
        g.setFont(new Font("Arial", Font.BOLD, 24));
        FontMetrics fm = g.getFontMetrics();
        String[] messages = {
            "Poziom " + currentLevel + " ukończony!",
            "Naciśnij 'N' aby przejść dalej"
        };

        int y = 250;
        for (String msg : messages) {
            if (y == 250) {
                g.setColor(Color.BLACK);
                g.drawString(msg, (getWidth() - fm.stringWidth(msg)) / 2 + 1, y + 1);
                g.setColor(Color.GREEN);
            }
            drawStringWithShadow(g, msg, (getWidth() - fm.stringWidth(msg)) / 2, y);
            y += 40;
        }
    }

    private void drawNameEntry(Graphics g) {
        g.setFont(new Font("Arial", Font.BOLD, 24));
        FontMetrics fm = g.getFontMetrics();
        
        String namePrompt = "ENTER YOUR NAME:";
        int promptX = (getWidth() - fm.stringWidth(namePrompt)) / 2;
        drawStringWithShadow(g, namePrompt, promptX, 250);
        
        char[] name = scoreManager.getCurrentName();
        int currentPos = scoreManager.getCurrentPosition();
        String nameStr = new String(name);
        int totalWidth = fm.stringWidth(nameStr);
        int startX = (getWidth() - totalWidth) / 2;
        
        for (int i = 0; i < 3; i++) {
            String letter = String.valueOf(name[i]);
            int letterX = startX + i * fm.charWidth('A');
            
            if (i == currentPos) {
                g.setColor(new Color(255, 255, 0, 100));
                int charWidth = fm.charWidth(name[i]);
                g.fillRect(letterX - 2, 280, charWidth + 4, 25);
                
                g.setColor(Color.BLACK);
                g.drawString(letter, letterX - 1, 300 - 1);
                g.drawString(letter, letterX - 1, 300 + 1);
                g.drawString(letter, letterX + 1, 300 - 1);
                g.drawString(letter, letterX + 1, 300 + 1);
                g.setColor(Color.YELLOW);
                g.drawString(letter, letterX, 300);
            } else {
                drawStringWithShadow(g, letter, letterX, 300);
            }
        }
        
        g.setFont(new Font("Arial", Font.BOLD, 16));
        String instructions = "Use ↑↓ to change letters, ←→ to move, ENTER to confirm";
        drawStringWithShadow(g, instructions, 
            (getWidth() - g.getFontMetrics().stringWidth(instructions)) / 2, 350);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!levelCompleted && !gameFinished && !gameOver) {
            if (!gameStarted) {
                Ball mainBall = balls.get(0);
                mainBall.setX(paddle.getX() + paddle.getWidth()/2 - 10);
                mainBall.setY(paddle.getY() - 20);
                repaint();
                return;
            }

            List<Ball> ballsToAdd = new ArrayList<>();
            List<Ball> ballsToRemove = new ArrayList<>();
            boolean allBallsLost = true;
            boolean allBlocksDestroyed = true;

            for (Ball ball : balls) {
                ball.move();

                if (ball.getX() <= 0 || ball.getX() >= getWidth() - 20) {
                    ball.bounceX();
                    SoundManager.playWallHit();
                }
                if (ball.getY() <= 0) {
                    ball.bounceY();
                    SoundManager.playWallHit();
                }

                if (ball.getBounds().intersects(paddle.getBounds())) {
                    Rectangle paddleBounds = paddle.getBounds();
                    Rectangle ballBounds = ball.getBounds();
                    
                    if (ballBounds.getY() + ballBounds.getHeight() > paddleBounds.getY() + 5) {
                        ball.bounceX();
                        if (ball.getX() < paddleBounds.getX() + paddleBounds.getWidth() / 2) {
                            ball.setX(paddleBounds.getX() - ballBounds.getWidth() - 1);
                        } else {
                            ball.setX(paddleBounds.getX() + paddleBounds.getWidth() + 1);
                        }
                    } else {
                        ball.bounceY();
                        double ballCenter = ball.getX() + ballBounds.getWidth() / 2.0;
                        double paddleCenter = paddleBounds.getX() + paddleBounds.getWidth() / 2.0;
                        double hitPoint = (ballCenter - paddleCenter) / (paddleBounds.getWidth() / 2.0);
                        double angle = hitPoint * Math.PI / 3;
                        double speed = difficulty.getSpeed();
                        ball.setDx(speed * Math.sin(angle));
                        ball.setDy(-speed * Math.cos(angle));
                        ball.setY(paddleBounds.getY() - ballBounds.getHeight() - 1);
                    }
                    SoundManager.playPaddleHit();
                }

                if (ball.getY() > getHeight()) {
                    ballsToRemove.add(ball);
                } else {
                    allBallsLost = false;
                }

                for (Block block : blocks) {
                    if (block.isVisible()) {
                        allBlocksDestroyed = false;
                        if (ball.intersectsBlock(block.getBounds())) {
                            block.hit();
                            score += 10;
                            SoundManager.playBlockHit();

                            if (block.isSpecial() && !block.isVisible()) {
                                ballsToAdd.add(new Ball(
                                    block.getX() + 30,
                                    block.getY() + 10,
                                    20,
                                    difficulty.getSpeed(),
                                    difficulty.getSpeed()
                                ));
                            }
                        }
                    }
                }
            }

            balls.removeAll(ballsToRemove);
            balls.addAll(ballsToAdd);

            if (allBlocksDestroyed) {
                levelCompleted = true;
                timer.stop();
            }

            if (allBallsLost && gameStarted) {
                gameOver = true;
                timer.stop();
                scoreManager.startNameEntry(score);
                SoundManager.playGameOver();
            }
        }
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (gameOver || gameFinished) {
            if (scoreManager.isEnteringName()) {
                scoreManager.handleInput(e);
                repaint();
                return;
            }
            switch (Character.toLowerCase(e.getKeyChar())) {
                case 'r':
                    if (gameOver) restartGame();
                    break;
                case 'e':
                    returnToMenu();
                    break;
            }
            return;
        }

        if (!gameStarted && e.getKeyCode() == KeyEvent.VK_SPACE) {
            gameStarted = true;
            return;
        }

        if (!levelCompleted) {
            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                paddle.moveLeft();
                if (!gameStarted) {
                    balls.get(0).setX(paddle.getX() + paddle.getWidth()/2 - 10);
                }
            } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                paddle.moveRight(getWidth());
                if (!gameStarted) {
                    balls.get(0).setX(paddle.getX() + paddle.getWidth()/2 - 10);
                }
            }
        } else if (e.getKeyChar() == 'n' && !gameFinished) {
            nextLevel();
        }
    }

    private void createBlocks() {
        blocks = LevelManager.loadLevel(currentLevel);
    }

    public void startGame() {
        timer.start();
    }


    private void restartGame() {
        gameOver = false;
        gameStarted = false;
        score = 0;
        currentLevel = 1;
        difficulty = Difficulty.EASY;
        loadBackground(currentLevel);
        paddle = new Paddle(360, 550, 80, 10, 20);
        balls = new ArrayList<>();
        balls.add(new Ball(paddle.getX() + paddle.getWidth()/2 - 10,
                  paddle.getY() - 20,
                  20,
                  difficulty.getSpeed(),
                  difficulty.getSpeed()));;
        blocks.clear();
        createBlocks();
        timer.start();
    }

    private void returnToMenu() {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        frame.getContentPane().removeAll();
        MenuPanel menuPanel = new MenuPanel(frame);
        frame.add(menuPanel);
        frame.pack();
        frame.revalidate();
        menuPanel.requestFocusInWindow();
    }

    private void nextLevel() {
        currentLevel++;
        if (currentLevel <= 3) {
            levelCompleted = false;
            gameStarted = false;
            switch (currentLevel) {
                case 2:
                    difficulty = Difficulty.MEDIUM;
                    break;
                case 3:
                    difficulty = Difficulty.HARD;
                    break;
            }
            
            loadBackground(currentLevel);
            paddle = new Paddle(360, 550, 80, 10, 20);
            balls = new ArrayList<>();
            balls.add(new Ball(paddle.getX() + paddle.getWidth()/2 - 10,
                  paddle.getY() - 20,
                  20,
                  difficulty.getSpeed(),
                  difficulty.getSpeed()));
            blocks.clear();
            createBlocks();
            timer.start();
        } else {
            gameFinished = true;
            scoreManager.startNameEntry(score);
            timer.stop();
        }
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}
}