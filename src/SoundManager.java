import javax.sound.sampled.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SoundManager {
    private static File menuMoveFile;
    private static File menuSelectFile;
    private static File gameOverFile;
    private static File wallHitFile;
    private static File paddleHitFile;
    private static File blockHitFile;
    
    private static List<Clip> activeClips = new ArrayList<>();

    static {
        try {
            menuMoveFile = new File("assets/sounds/select.wav");
            menuSelectFile = new File("assets/sounds/done.wav");
            gameOverFile = new File("assets/sounds/miu.wav");
            wallHitFile = new File("assets/sounds/mchit.wav");
            paddleHitFile = new File("assets/sounds/roblox.wav");
            blockHitFile = new File("assets/sounds/metal_pipe.wav");
        } catch (Exception e) {
            System.out.println("Error loading sound files: " + e.getMessage());
        }
    }

    private static void playSound(File soundFile) {
        try {
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            
            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    clip.close();
                    activeClips.remove(clip);
                }
            });
            
            activeClips.add(clip);
            clip.start();
        } catch (Exception e) {
            System.out.println("Error playing sound: " + e.getMessage());
        }
    }

    public static void playMenuMove() {
        if (menuMoveFile != null) playSound(menuMoveFile);
    }

    public static void playMenuSelect() {
        if (menuSelectFile != null) playSound(menuSelectFile);
    }

    public static void playGameOver() {
        if (gameOverFile != null) playSound(gameOverFile);
    }

    public static void playWallHit() {
        if (wallHitFile != null) playSound(wallHitFile);
    }

    public static void playPaddleHit() {
        if (paddleHitFile != null) playSound(paddleHitFile);
    }

    public static void playBlockHit() {
        if (blockHitFile != null) playSound(blockHitFile);
    }
}