import java.io.*;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class ScoreManager {
    private static final String SCORE_FILE = "highscores.txt";
    private List<Score> highScores;
    private int currentPosition = 0;
    private char[] currentName = {'A', 'A', 'A'};
    private boolean isEnteringName = false;
    private int pendingScore = 0;

    public ScoreManager() {
        highScores = new ArrayList<>();
        loadHighScores();
    }

    private void loadHighScores() {
        highScores.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(SCORE_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    highScores.add(new Score(parts[0], Integer.parseInt(parts[1])));
                }
            }
        } catch (IOException e) {
            System.out.println("No high scores file found. Will be created.");
        }
        Collections.sort(highScores);
    }

    private void saveHighScores() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(SCORE_FILE))) {
            for (Score score : highScores) {
                writer.write(score.getName() + ":" + score.getScore() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startNameEntry(int score) {
        pendingScore = score;
        isEnteringName = true;
        currentPosition = 0;
        currentName = new char[]{'A', 'A', 'A'};
    }

    public void handleInput(KeyEvent e) {
        if (!isEnteringName) return;

        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                currentName[currentPosition] = (char) (((currentName[currentPosition] - 'A' + 1) % 26) + 'A');
                SoundManager.playMenuMove();
                break;
            case KeyEvent.VK_DOWN:
                currentName[currentPosition] = (char) (((currentName[currentPosition] - 'A' + 25) % 26) + 'A');
                SoundManager.playMenuMove();
                break;
            case KeyEvent.VK_RIGHT:
                if (currentPosition < 2) {
                    currentPosition++;
                    SoundManager.playMenuMove();
                }
                break;
            case KeyEvent.VK_LEFT:
                if (currentPosition > 0) {
                    currentPosition--;
                    SoundManager.playMenuMove();
                }
                break;
            case KeyEvent.VK_ENTER:
                finishNameEntry();
                SoundManager.playMenuSelect();
                break;
        }
    }

    private void finishNameEntry() {
        String name = new String(currentName);
        highScores.add(new Score(name, pendingScore));
        Collections.sort(highScores);
        if (highScores.size() > 10) {
            highScores.remove(highScores.size() - 1);
        }
        saveHighScores();
        isEnteringName = false;
    }

    public boolean isEnteringName() { return isEnteringName; }
    public List<Score> getHighScores() { return highScores; }
    public char[] getCurrentName() { return currentName; }
    public int getCurrentPosition() { return currentPosition; }
}