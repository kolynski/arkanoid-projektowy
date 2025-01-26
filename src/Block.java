import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class Block {
    private int x, y, width, height;
    private boolean visible;
    private boolean isSpecial;
    private int durability;
    private Color color;
    private static Random random = new Random();
    
    private static BufferedImage normalBlockImage;
    private static BufferedImage specialBlockImage;
    private static BufferedImage durableBlock2Image;
    private static BufferedImage durableBlock3Image;
    
    static {
        try {
            normalBlockImage = ImageIO.read(new File("assets/images/czerwony1.png"));
            specialBlockImage = ImageIO.read(new File("assets/images/niebieski1kula.png"));
            durableBlock2Image = ImageIO.read(new File("assets/images/czerwony2.png"));
            durableBlock3Image = ImageIO.read(new File("assets/images/czerwony3.png"));
        } catch (IOException e) {
            System.out.println("Error loading block images: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public Block(int x, int y, int width, int height, int currentLevel) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.visible = true;
        
        double specialProbability;
        double durableProbability;
        
        switch (currentLevel) {
            case 1: // EASY
                specialProbability = 0.2;
                durableProbability = 0.2;
                durability = random.nextDouble() < durableProbability ? 2 : 1;
                break;
            case 2: // MEDIUM
                specialProbability = 0.3;
                durableProbability = 0.4;
                durability = random.nextDouble() < durableProbability ? 2 : 1;
                break;
            case 3: // HARD
                specialProbability = 0.4;
                durableProbability = 0.4;
                durability = random.nextDouble() < durableProbability ? 3 : 1;
                break;
            default:
                specialProbability = 0.2;
                durability = 1;
        }
        
        if (random.nextDouble() < specialProbability && durability == 1) {
            this.isSpecial = true;
            this.color = Color.BLUE;
        } else {
            this.isSpecial = false;
            updateColorBasedOnDurability();
        }
    }

    private void updateColorBasedOnDurability() {
        switch (durability) {
            case 3:
                color = Color.BLACK;
                break;
            case 2:
                color = new Color(139, 0, 0); 
                break;
            case 1:
                color = Color.RED;
                break;
        }
    }

    public void draw(Graphics g) {
        if (visible) {
            if (isSpecial && specialBlockImage != null) {
                g.drawImage(specialBlockImage, x, y, width, height, null);
            } else if (durability == 3 && durableBlock3Image != null) {
                g.drawImage(durableBlock3Image, x, y, width, height, null);
            } else if (durability == 2 && durableBlock2Image != null) {
                g.drawImage(durableBlock2Image, x, y, width, height, null);
            } else if (normalBlockImage != null) {
                g.drawImage(normalBlockImage, x, y, width, height, null);
            } else {
                // Fallback to colored blocks
                g.setColor(color);
                g.fillRect(x, y, width, height);
                g.setColor(Color.WHITE);
                g.drawRect(x, y, width, height);
                
                if (isSpecial) {
                    g.setColor(Color.WHITE);
                    int circleSize = 8;
                    g.fillOval(x + width/2 - circleSize/2, 
                              y + height/2 - circleSize/2, 
                              circleSize, circleSize);
                }
            }
        }
    }

    public void hit() {
        if (durability > 1) {
            durability--;
            updateColorBasedOnDurability();
        } else {
            visible = false;
        }
    }

    public boolean isSpecial() { return isSpecial; }
    public int getX() { return x; }
    public int getY() { return y; }
    public Rectangle getBounds() { return new Rectangle(x, y, width, height); }
    public boolean isVisible() { return visible; }
    public void setVisible(boolean visible) { this.visible = visible; }
}