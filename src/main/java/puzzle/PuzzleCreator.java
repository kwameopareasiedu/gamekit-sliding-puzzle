package puzzle;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class takes images in puzzles resource directory
 * and slices them up into the different puzzle pieces
 */
public class PuzzleCreator {
  public static void main(String[] args) throws IOException, URISyntaxException {
    createPuzzlePieces("level1", 4, 512);
  }

  private static void createPuzzlePieces(String directoryPath, int gridSize, int pieceSize) throws IOException, URISyntaxException {
    if (gridSize <= 1) throw new IllegalArgumentException("Pieces must be more than 1");

    // Read in the reference image
    String refImagePath = String.format("/%s/ref.jpg", directoryPath);
    InputStream refImageStream = PuzzleCreator.class.getResourceAsStream(refImagePath);
    if (refImageStream == null) throw new FileNotFoundException(String.format("%s not found", refImagePath));
    BufferedImage refImage = ImageIO.read(refImageStream);

    // Adjust the image so it is a multiple of gridSize
    int adjustedImageWidth = (int) (Math.ceil(refImage.getWidth() / (double) gridSize) * gridSize);
    int adjustedImageHeight = (int) (Math.ceil(refImage.getHeight() / (double) gridSize) * gridSize);

    if (adjustedImageWidth != refImage.getWidth() || adjustedImageHeight != refImage.getHeight()) {
      BufferedImage adjustedImage = new BufferedImage(adjustedImageWidth, adjustedImageHeight, refImage.getType());
      Graphics2D adjustedImageGraphics = adjustedImage.createGraphics();

      adjustedImageGraphics.drawImage(
        refImage, 0, 0, adjustedImageWidth, adjustedImageHeight,
        0, 0, refImage.getWidth(), refImage.getHeight(), null
      );

      refImage = adjustedImage;
    }

    // Slice the image into puzzle pieces
    BufferedImage pieceImg = new BufferedImage(pieceSize, pieceSize, refImage.getType());
    Graphics2D pieceGraphics = pieceImg.createGraphics();

    int refImageSliceWidth = refImage.getWidth() / gridSize;
    int refImageSliceHeight = refImage.getHeight() / gridSize;

    String dirPath = "/" + directoryPath;
    URL dirUrl = PuzzleCreator.class.getResource(dirPath);
    if (dirUrl == null) throw new FileNotFoundException(String.format("%s not found", dirPath));

    File dirFile = new File(new URI(dirUrl.toString()));
    File configFile = new File(dirFile, "config.txt");
    StringBuilder config = new StringBuilder();
    List<String> indexList = new ArrayList<>();

    for (int y = 0; y < gridSize; y++) {
      for (int x = 0; x < gridSize; x++) {
        int index = y * gridSize + x;
        int x1 = x * refImageSliceWidth;
        int y1 = y * refImageSliceHeight;
        int x2 = x1 + refImageSliceWidth;
        int y2 = y1 + refImageSliceHeight;
        File sliceFile = new File(dirFile, String.format("piece-%d.jpg", index));

        pieceGraphics.drawImage(refImage, 0, 0, pieceSize, pieceSize, x1, y1, x2, y2, null);
        ImageIO.write(pieceImg, "jpg", sliceFile);
        config.append(sliceFile.getName()).append("\n");
        indexList.add(String.valueOf(index));

        System.out.println("Saved " + sliceFile.toURI());
      }
    }

    Collections.shuffle(indexList);
    config.insert(0, String.join(",", indexList) + "\n");
    config.insert(0, gridSize + "\n");

    Files.write(Paths.get(configFile.toURI()), config.toString().getBytes());
    refImageStream.close();
  }
}
