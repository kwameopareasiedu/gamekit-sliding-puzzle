package puzzle;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Puzzle {
  public static final Puzzle LEVEL_1;

  static {
    try {
      LEVEL_1 = new Puzzle("level1");
    } catch (IOException | URISyntaxException e) {
      throw new RuntimeException(e);
    }
  }

  public final int size;
  public final BufferedImage referenceImage;
  public final List<BufferedImage> pieceImages;
  public final int[] initialGrid;

  private Puzzle(String directory) throws IOException, URISyntaxException {
    URL configFileUrl = getClass().getResource(String.format("/%s/config.txt", directory));
    if (configFileUrl == null) throw new FileNotFoundException(String.format("%s not found", directory));

    List<String> configLines = Files.readAllLines(Paths.get(configFileUrl.toURI()));

    pieceImages = new ArrayList<>();
    size = Integer.parseInt(configLines.get(0));
    initialGrid = Arrays.stream(configLines.get(1).split(",")).mapToInt(Integer::parseInt).toArray();

    List<String> pieceNames = configLines.subList(2, configLines.size());

    for (var pieceName : pieceNames) {
      String puzzlePiecePath = String.format("/%s/%s", directory, pieceName);
      InputStream pieceImageStream = getClass().getResourceAsStream(puzzlePiecePath);
      if (pieceImageStream == null) throw new FileNotFoundException(String.format("%s not found", puzzlePiecePath));

      pieceImages.add(ImageIO.read(pieceImageStream));
      pieceImageStream.close();
    }

    String referenceImagePath = String.format("/%s/ref.jpg", directory);
    InputStream referenceImageStream = getClass().getResourceAsStream(referenceImagePath);
    if (referenceImageStream == null)
      throw new FileNotFoundException(String.format("%s not found", referenceImagePath));

    referenceImage = ImageIO.read(referenceImageStream);
    referenceImageStream.close();
  }
}
