package puzzle;

import dev.gamekit.core.Application;
import dev.gamekit.utils.Config;
import dev.gamekit.utils.Resolution;

public class SlidingPuzzle {
  public static void main(String[] args) {
    Application game = new Application(
      new Config("Sliding Puzzle", Resolution.HD, true)
    ) { };
    game.loadScene(new MainMenu());
    game.run();
  }
}
