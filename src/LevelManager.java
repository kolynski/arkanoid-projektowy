import java.io.*;
import java.util.*;

public class LevelManager {
    private static final int BLOCK_WIDTH = 60;
    private static final int BLOCK_HEIGHT = 30;
    private static final int BLOCK_SPACING = 3;
    private static final int EDGE_MARGIN = 30;

    public static List<Block> loadLevel(int level) {
        String fileName = "levels/level" + level + ".txt";
        List<Block> blocks = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            int row = 0;
            while ((line = reader.readLine()) != null) {
                for (int col = 0; col < line.length(); col++) {
                    if (line.charAt(col) == '1') {
                        int x = EDGE_MARGIN + col * (BLOCK_WIDTH + BLOCK_SPACING);
                        int y = EDGE_MARGIN + row * (BLOCK_HEIGHT + BLOCK_SPACING);
                        blocks.add(new Block(x, y, BLOCK_WIDTH, BLOCK_HEIGHT, level));
                    }
                }
                row++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return blocks;
    }
}