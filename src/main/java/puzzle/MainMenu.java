package puzzle;

import dev.gamekit.core.Application;
import dev.gamekit.core.IO;
import dev.gamekit.core.Renderer;
import dev.gamekit.core.Scene;
import dev.gamekit.ui.Spacing;
import dev.gamekit.ui.enums.Alignment;
import dev.gamekit.ui.enums.CrossAxisAlignment;
import dev.gamekit.ui.enums.TextAlignment;
import dev.gamekit.ui.widgets.Button;
import dev.gamekit.ui.widgets.*;
import dev.gamekit.utils.Resolution;

import java.awt.*;
import java.awt.image.BufferedImage;

public class MainMenu extends Scene {
  private final BufferedImage btnImage = IO.loadImageResource("ui/btn.png");
  private final Widget[] levelButtons = new Widget[Level.LEVELS.length];
  private View view = View.MAIN;

  public MainMenu() {
    super("Main Menu");

    for (int i = 0; i < Level.LEVELS.length; i++) {
      Level level = Level.LEVELS[i];

      levelButtons[i] = Button.create(
        FixedSize.create(
          160, 85,
          NinePatch.create(
            btnImage,
            Center.create(
              Text.create(level.name.toUpperCase())
                .withFontSize(20)
                .withFontStyle(Font.BOLD)
                .withAlignment(TextAlignment.CENTER)
            )
          ).withSpacing(10, 10, 32, 10)
        )
      ).withMouseClickListener(e ->  {
        Scene levelScene = new LevelScene(level);
        Application.getInstance().loadScene(levelScene);
      });
    }
  }

  @Override
  protected void onRender() {
    Renderer.setBackground(Color.BLACK);
    Renderer.clear();
  }

  @Override
  public Widget onCreateUI() {
    return switch (view) {
      case MAIN -> createMainUI();
      case LEVEL_SELECT -> createLevelSelectUI();
    };
  }

  private Widget createMainUI() {
    return Stack.create(
      Center.create(
        Column.create(
            Text.create("Sliding Puzzle")
              .withFontSize(72)
              .withFontStyle(Font.BOLD)
              .withAlignment(TextAlignment.CENTER)
              .withShadow(true)
              .withShadowColor(Color.DARK_GRAY)
              .withShadowOffset(8, 12),
            Button.create(
              FixedSize.create(
                200, 106,
                NinePatch.create(
                  btnImage,
                  Center.create(
                    Text.create("PLAY")
                      .withFontSize(32)
                      .withFontStyle(Font.BOLD)
                      .withAlignment(TextAlignment.CENTER)
                  )
                ).withSpacing(10, 10, 32, 10)
              )
            ).withMouseClickListener(
              e -> updateUI(() -> view = View.LEVEL_SELECT)
            )
          ).withGapSize(32)
          .withCrossAxisAlignment(CrossAxisAlignment.CENTER)
      ),
      FixedSize.create(
        Resolution.HD.width(), Resolution.HD.height(),
        Align.create(
          Alignment.BOTTOM_CENTER,
          Padding.create(
            new Spacing(48),
            Column.create(
                Text.create("Developed by Kwame Opare Asiedu")
                  .withFontStyle(Font.BOLD)
                  .withAlignment(TextAlignment.CENTER),
                Text.create("GameKit v0.3.0-SNAPSHOT-3")
                  .withAlignment(TextAlignment.CENTER)
              ).withCrossAxisAlignment(CrossAxisAlignment.CENTER)
              .withGapSize(8)
          )
        )
      )
    );
  }

  private Widget createLevelSelectUI() {
    return Stack.create(
      Center.create(
        Column.create(
            Text.create("Select Level")
              .withFontSize(48)
              .withFontStyle(Font.BOLD)
              .withAlignment(TextAlignment.CENTER)
              .withShadow(true)
              .withShadowColor(Color.DARK_GRAY)
              .withShadowOffset(6, 8),
            Column.create(levelButtons)
              .withCrossAxisAlignment(CrossAxisAlignment.CENTER)
              .withGapSize(12)
          )
          .withCrossAxisAlignment(CrossAxisAlignment.CENTER)
          .withGapSize(12)
      ),
      FixedSize.create(
        Resolution.HD.width(), Resolution.HD.height(),
        Align.create(
          Alignment.TOP_LEFT,
          Padding.create(
            new Spacing(24),
            Button.create(
              FixedSize.create(
                100, 55,
                NinePatch.create(
                  btnImage,
                  Center.create(
                    Text.create("Back")
                      .withFontSize(16)
                      .withFontStyle(Font.BOLD)
                      .withAlignment(TextAlignment.CENTER)
                  )
                ).withSpacing(10, 10, 32, 10)
              )
            ).withMouseClickListener(
              e -> updateUI(() -> view = View.MAIN)
            )
          )
        )
      )
    );
  }

  enum View {
    MAIN, LEVEL_SELECT
  }
}
