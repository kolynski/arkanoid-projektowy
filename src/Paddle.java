import java.awt.Graphics;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Image;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Paddle {
    private int x, y, width, height;
    private int speed;
    private Image paddleImage;

    public Paddle(int x, int y, int width, int height, int speed) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.speed = speed;
        loadImage();
    }

    private void loadImage() {
        try {
            paddleImage = ImageIO.read(new File("assets/images/belka.png"));
        } catch (IOException e) {
            System.out.println("Error loading paddle image: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void draw(Graphics g) {
        if (paddleImage != null) {
            g.drawImage(paddleImage, x, y, width, height, null);
        } else {
            // Fallback rectangle if image fails to load
            g.setColor(Color.GREEN);
            g.fillRect(x, y, width, height);
        }
    }

    public void moveLeft() {
        x -= speed;
        if (x < 0) x = 0;
    }

    public void moveRight(int screenWidth) {
        x += speed;
        if (x + width > screenWidth) x = screenWidth - width;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    // Getters
    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
}