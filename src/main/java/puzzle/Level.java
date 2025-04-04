package puzzle;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Random;

public class Level {
  public static final Level LEVEL_1;
  public static final Level LEVEL_2;

  static {
    try {
      LEVEL_1 = new Level("level1-4");
      LEVEL_2 = new Level("level2-4");
    } catch (IOException | URISyntaxException e) {
      throw new RuntimeException(e);
    }
  }

  public final String name;
  public final int size;
  public final BufferedImage image;
  public final BufferedImage[] sliceImages;
  public final Color bgColor;
  public final int[] grid;

  private final Random rnd = new Random();

  private Level(String levelDirPath) throws IOException, URISyntaxException {
    URL resDirUrl = Level.class.getResource("/");
    if (resDirUrl == null) throw new IOException("Res dir not found");
    File resDir = new File(resDirUrl.toURI());
    File levelDir = new File(resDir, levelDirPath);

    name = levelDirPath.split("-")[0];
    size = Integer.parseInt(levelDirPath.split("-")[1]);
    image = ImageIO.read(new File(levelDir, "level.jpg"));
    sliceImages = new BufferedImage[size * size];
    grid = new int[size * size];
    bgColor = getAverageColor(image);

    for (int i = 0; i < sliceImages.length; i++) {
      sliceImages[i] = ImageIO.read(new File(levelDir, i + ".jpg"));
      grid[i] = i < sliceImages.length - 1 ? i : -1;
    }

    while (!isSlidePuzzleSolvable(grid, size)) {
      shuffle(grid);
    }
  }

  private void shuffle(int[] grid) {
    for (int i = grid.length - 1; i > 0; i--) {
      int randomIndex = rnd.nextInt(0, i + 1);

      int temp = grid[i];
      grid[i] = grid[randomIndex];
      grid[randomIndex] = temp;
    }
  }

  private int countInversions(int[] grid) {
    int emptyTileVal = -1;
    int inversionCount = 0;

    for (int i = 0; i < grid.length; i++) {
      if (grid[i] == emptyTileVal)
        continue;

      for (int j = i + 1; j < grid.length; j++)
        if (grid[j] != emptyTileVal && grid[i] > grid[j])
          inversionCount++;
    }

    return inversionCount;
  }

  private int getRowNumberFromBelow(int width, int emptyTileIdx) {
    int row = emptyTileIdx / width;
    return width - row;
  }

  private boolean isSlidePuzzleSolvable(int[] grid, int width) {
    int inversionCount = countInversions(grid);
    int emptyTileVal = -1;
    int emptyTileIdx = 0;

    for (int i = 0; i < grid.length; i++) {
      if (grid[i] == emptyTileVal)
        emptyTileIdx = i;
    }


    if (width % 2 != 0) {
      return inversionCount % 2 == 0;
    } else {
      int rowNumber = getRowNumberFromBelow(width, emptyTileIdx);
      return (rowNumber % 2 != 0) == (inversionCount % 2 == 0);
    }
  }

  private Color getAverageColor(BufferedImage image) {
    int w = image.getWidth(), h = image.getHeight();
    int redSum = 0, greenSum = 0, blueSum = 0;

    for (int x = 0; x < w; x++) {
      for (int y = 0; y < h; y++) {
        int color = image.getRGB(x, y);
        redSum += (color >> 16) & 0xFF;
        greenSum += (color >> 8) & 0xFF;
        blueSum += color & 0xFF;
      }
    }

    int num = w * h;
    int r = redSum / num, g = greenSum / num, b = blueSum / num;
    return new Color(r << 16 | g << 8 | b);
  }
}


