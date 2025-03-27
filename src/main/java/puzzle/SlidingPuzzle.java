package puzzle;

import dev.gamekit.core.Application;
import dev.gamekit.core.Scene;
import dev.gamekit.core.Window;

public class SlidingPuzzle extends Scene {
  public SlidingPuzzle() {
    super("Play Scene");
  }

  public static void main(String[] args) {
    Window.setFullscreen(true);
    Window.setResolution(Window.Resolution.FULL);
    Application game = new Application("Sliding Puzzle") { };
    game.loadScene(new SlidingPuzzle());
    game.run();
  }
}
