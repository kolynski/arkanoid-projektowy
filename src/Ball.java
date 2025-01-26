import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Image;
import java.awt.Color;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Ball {
    private double x, y;
    private int size;
    private double dx, dy;
    private Image ballImage;

    public Ball(int x, int y, int size, double dx, double dy) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.dx = dx;
        this.dy = dy;
        loadImage();
    }

    private void loadImage() {
        try {
            ballImage = ImageIO.read(new File("assets/images/pilka.png"));
        } catch (IOException e) {
            System.out.println("Error loading ball image: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void move() {
        x += dx;
        y += dy;
    }

    public void draw(Graphics g) {
        if (ballImage != null) {
            g.drawImage(ballImage, (int)x, (int)y, size, size, null);
        } else {
            g.setColor(Color.WHITE);
            g.fillOval((int)x, (int)y, size, size);
        }
    }
    public void setDx(double dx) {
        this.dx = dx;
    }

    public void setDy(double dy) {
        this.dy = dy;
    }

    public boolean intersectsBlock(Rectangle blockBounds) {
        Rectangle ballBounds = getBounds();
        if (ballBounds.intersects(blockBounds)) {
            double ballCenterX = x + size / 2;
            double ballCenterY = y + size / 2;
            
            double intersectX = Math.min(Math.abs(ballCenterX - blockBounds.x), 
                                       Math.abs(ballCenterX - (blockBounds.x + blockBounds.width)));
            double intersectY = Math.min(Math.abs(ballCenterY - blockBounds.y), 
                                       Math.abs(ballCenterY - (blockBounds.y + blockBounds.height)));

            if (intersectX < intersectY) {
                bounceX();
            } else {
                bounceY();
            }
            return true;
        }
        return false;
    }

    public void bounceX() { dx = -dx; }
    public void bounceY() { dy = -dy; }

    public Rectangle getBounds() {
        return new Rectangle((int)x, (int)y, size, size);
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public void setX(double x) { this.x = x; }
    public void setY(double y) { this.y = y; }
}