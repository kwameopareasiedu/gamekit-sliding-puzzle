package puzzle;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * This class takes images in puzzles resource directory
 * and slices them up into the different puzzle pieces
 */
public class PuzzleCreator {
  public static void main(String[] args) throws IOException, URISyntaxException {
    URL imagesDirPath = PuzzleCreator.class.getResource(String.format("/%s", "images"));
    if (imagesDirPath == null) throw new NullPointerException("Images directory not found in resources");

    File imagesDir = new File(new URI(imagesDirPath.toString()));
    if (!imagesDir.isDirectory()) throw new IllegalArgumentException("Images directory is not a directory");

    File[] imageFiles = imagesDir.listFiles();
    if (imageFiles == null) throw new IllegalArgumentException("Image file list is empty");

    for (var imageFile : imageFiles) {
      String fileName = imageFile.getName();
      String[] splitParts = fileName.split("\\.")[0].split("-");
      String levelName = splitParts[0];
      int levelSize = Integer.parseInt(splitParts[1]);

      createLevelData(imageFile, levelName, levelSize);
    }
  }

  private static void createLevelData(File levelImageFile, String levelName, int levelSize) throws IOException, URISyntaxException {
    if (levelSize < 3) throw new IllegalArgumentException("Grid size must be 3 or greater");

    BufferedImage levelImage = ImageIO.read(levelImageFile);

    // Adjust the levelImage so it is perfectly divisible by levelSize
    int adjustedWidth = (int) (Math.ceil(levelImage.getWidth() / (double) levelSize) * levelSize);
    int adjustedHeight = (int) (Math.ceil(levelImage.getHeight() / (double) levelSize) * levelSize);

    if (adjustedWidth != levelImage.getWidth() || adjustedHeight != levelImage.getHeight()) {
      BufferedImage newImage = new BufferedImage(adjustedWidth, adjustedHeight, levelImage.getType());
      Graphics2D g = newImage.createGraphics();

      g.drawImage(
        levelImage, 0, 0, adjustedWidth, adjustedHeight,
        0, 0, levelImage.getWidth(), levelImage.getHeight(), null
      );

      levelImage = newImage;
      g.dispose();
    }

    // Slice the image into puzzle pieces
    int sliceCount = levelSize * levelSize;
    int sliceSize = levelImage.getWidth() / levelSize;
    BufferedImage[] sliceImages = new BufferedImage[sliceCount];

    for (int sliceIdx = 0; sliceIdx < sliceCount; sliceIdx++) {
      BufferedImage sliceBuffer = new BufferedImage(sliceSize, sliceSize, levelImage.getType());
      Graphics2D g = sliceBuffer.createGraphics();
      int x = sliceIdx / levelSize;
      int y = sliceIdx % levelSize;

      int sx1 = x * sliceSize, sy1 = y * sliceSize;
      int sx2 = (x + 1) * sliceSize, sy2 = (y + 1) * sliceSize;
      g.drawImage(levelImage, 0, 0, sliceSize, sliceSize, sx1, sy1, sx2, sy2, null);
      sliceImages[sliceIdx] = sliceBuffer;
      g.dispose();
    }

    URL resDirUrl = PuzzleCreator.class.getResource("/");
    if (resDirUrl == null) throw new IOException("Res dir not found");
    File levelOutDir = new File(new File(resDirUrl.toURI()), levelName + "-" + levelSize);
    if (!levelOutDir.exists() && !levelOutDir.mkdirs())
      throw new IOException("Could not create level directory");

    for (int i = 0, sliceImagesLength = sliceImages.length; i < sliceImagesLength; i++) {
      BufferedImage sliceImage = sliceImages[i];
      File sliceOutFile = new File(levelOutDir, String.format("%d.jpg", i));
      ImageIO.write(sliceImage, "jpg", sliceOutFile);
    }

    File levelImageOutFile = new File(levelOutDir, "level.jpg");
    ImageIO.write(levelImage, "jpg", levelImageOutFile);
  }
}
