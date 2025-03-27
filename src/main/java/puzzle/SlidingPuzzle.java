package puzzle;

import dev.gamekit.core.Window;
import dev.gamekit.core.*;
import dev.gamekit.ui.Alignment;
import dev.gamekit.ui.Spacing;
import dev.gamekit.ui.widgets.Align;
import dev.gamekit.ui.widgets.Padding;
import dev.gamekit.ui.widgets.Widget;

import java.awt.*;

import static dev.gamekit.utils.Math.clamp;

public class SlidingPuzzle extends Scene {
  private static final Color CLEAR_COLOR = new Color(0xE5A070);
  private static final int PIECE_SIZE = 256;

  private final Puzzle puzzle = Puzzle.LEVEL_1;
  private final int[] grid = new int[puzzle.size * puzzle.size];
  private final int gridOffset = (int) (0.5 * (puzzle.size * PIECE_SIZE - PIECE_SIZE));
  private int emptyX, emptyY;

  public SlidingPuzzle() {
    super("Play Scene");
    emptyX = emptyY = puzzle.size - 1;
    System.arraycopy(puzzle.initialGrid, 0, grid, 0, grid.length);
  }

  public static void main(String[] args) {
    Window.setFullscreen(true);
    Window.setResolution(Window.Resolution.FULL);
    Application game = new Application("Sliding Puzzle") { };
    game.loadScene(new SlidingPuzzle());
    game.run();
  }

  @Override
  protected void onUpdate() {
    if (Input.isKeyJustPressed(Input.KEY_UP)) {
      emptyY = clamp(emptyY - 1, 0, puzzle.size - 1);
    } else if (Input.isKeyJustPressed(Input.KEY_RIGHT)) {
      emptyX = clamp(emptyX + 1, 0, puzzle.size - 1);
    } else if (Input.isKeyJustPressed(Input.KEY_DOWN)) {
      emptyY = clamp(emptyY + 1, 0, puzzle.size - 1);
    } else if (Input.isKeyJustPressed(Input.KEY_LEFT)) {
      emptyX = clamp(emptyX - 1, 0, puzzle.size - 1);
    }
  }

  @Override
  protected void onRender() {
    Renderer.setBackground(CLEAR_COLOR);
    Renderer.clear();

    for (int y = 0; y < puzzle.size; y++) {
      for (int x = 0; x < puzzle.size; x++) {
        int x1 = x * PIECE_SIZE - gridOffset;
        int y1 = y * PIECE_SIZE - gridOffset;
        int edx = emptyY * puzzle.size + emptyX;
        int idx = y * puzzle.size + x;

        if (idx != edx) {
          Renderer.drawImage(
            puzzle.pieceImages.get(grid[idx]),
            x1, -y1, PIECE_SIZE, PIECE_SIZE
          );
        }
      }
    }
  }

  @Override
  public Widget onCreateUI() {
    return Align.create(
      Padding.create(
        IntrinsicImage.create(puzzle.referenceImage, 256),
        new Spacing(24)
      ),
      Alignment.TOP_LEFT
    );
  }
}
