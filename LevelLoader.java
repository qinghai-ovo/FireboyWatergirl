import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LevelLoader {
    private int[][] mapData; // 存储地图数据
    private int width, height; // 关卡宽度 & 高度

    public LevelLoader(String levelFile) {
        loadLevel(levelFile);
    }

    private void loadLevel(String levelFile) {
        List<int[]> rows = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(levelFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(" ");
                int[] row = new int[tokens.length];
                for (int i = 0; i < tokens.length; i++) {
                    row[i] = Integer.parseInt(tokens[i]);
                }
                rows.add(row);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 转换为 2D 数组
        height = rows.size();
        width = height > 0 ? rows.get(0).length : 0;
        mapData = rows.toArray(new int[height][width]);
    }

    public int[][] getMapData() {
        return mapData;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}