package puzzle;

import dev.gamekit.core.Application;
import dev.gamekit.core.Input;
import dev.gamekit.core.Renderer;
import dev.gamekit.core.Scene;
import dev.gamekit.ui.Spacing;
import dev.gamekit.ui.enums.Alignment;
import dev.gamekit.ui.enums.CrossAxisAlignment;
import dev.gamekit.ui.enums.TextAlignment;
import dev.gamekit.ui.widgets.*;
import dev.gamekit.ui.widgets.Image;
import dev.gamekit.utils.Config;
import dev.gamekit.utils.Resolution;

import java.awt.*;

import static dev.gamekit.utils.Math.clamp;

public class SlidingPuzzle extends Scene {
  private static final Color CLEAR_COLOR = new Color(0x764A1F);
  private static final int PIECE_SIZE = 128;

  private final Level level = Level.LEVEL_1;
  private final int[] grid = new int[level.size * level.size];
  private final int gridOffset = (int) (0.5 * (level.size * PIECE_SIZE - PIECE_SIZE));
  private int emptyIdx;

  public SlidingPuzzle() {
    super("Play Scene");

    for (int i = 0; i < level.grid.length; i++) {
      if (level.grid[i] == -1) {
        emptyIdx = i;
        break;
      }
    }

    System.arraycopy(level.grid, 0, grid, 0, grid.length);
  }

  public static void main(String[] args) {
    Application game = new Application(
      new Config("Sliding Puzzle", Resolution.HD, false)
    ) { };
    game.loadScene(new SlidingPuzzle());
    game.run();
  }

  @Override
  protected void onUpdate() {
    int previousEmptyIdx = emptyIdx;
    int y = emptyIdx / level.size;
    int x = emptyIdx % level.size;

    if (Input.isKeyJustPressed(Input.KEY_UP)) {
      y = clamp(y + 1, 0, level.size - 1);
    } else if (Input.isKeyJustPressed(Input.KEY_RIGHT)) {
      x = clamp(x - 1, 0, level.size - 1);
    } else if (Input.isKeyJustPressed(Input.KEY_DOWN)) {
      y = clamp(y - 1, 0, level.size - 1);
    } else if (Input.isKeyJustPressed(Input.KEY_LEFT)) {
      x = clamp(x + 1, 0, level.size - 1);
    }

    emptyIdx = y * level.size + x;

    if (previousEmptyIdx != emptyIdx) {
      int temp = grid[previousEmptyIdx];
      grid[previousEmptyIdx] = grid[emptyIdx];
      grid[emptyIdx] = temp;
    }
  }

  @Override
  protected void onRender() {
    Renderer.setBackground(level.bgColor);
    Renderer.clear();

    for (int y = 0; y < level.size; y++) {
      for (int x = 0; x < level.size; x++) {
        int x1 = x * PIECE_SIZE - gridOffset;
        int y1 = y * PIECE_SIZE - gridOffset;
        int idx = y * level.size + x;

        if (grid[idx] != -1) {
          Renderer.drawImage(
            level.sliceImages[grid[idx]],
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
        Column.create(
          Text.create("REFERENCE")
            .withAlignment(TextAlignment.CENTER)
            .withFontStyle(Font.BOLD)
            .withFontSize(24),
          FixedSize.create(
            192, 192,
            Image.create(level.image)
          ),
          Text.create("TIP: Use the arrow keys").withAlignment(TextAlignment.CENTER)
        ).withCrossAxisAlignment(CrossAxisAlignment.STRETCH).withGapSize(16),
        new Spacing(24, 48)
      ),
      Alignment.TOP_LEFT
    );
  }
}
