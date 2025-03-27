package puzzle;

import dev.gamekit.ui.Constraints;
import dev.gamekit.ui.widgets.Widget;

import java.awt.*;
import java.awt.image.BufferedImage;

import static dev.gamekit.utils.Math.clamp;

public class IntrinsicImage extends Widget {
  public static final Color TRANSPARENT_COLOR = new Color(0x0000000, true);

  private final BufferedImage srcImg;
  private final int size;

  protected IntrinsicImage(BufferedImage srcImage, int size) {
    this.srcImg = srcImage;
    this.size = size;
  }

  public static IntrinsicImage create(BufferedImage srcImage, int size) {
    return new IntrinsicImage(srcImage, size);
  }

  @Override
  protected boolean stateEquals(Widget widget) {
    if (widget instanceof IntrinsicImage imageWidget) {
      return srcImg == imageWidget.srcImg;
    } else return false;
  }

  @Override
  protected void performLayout(Constraints constraints) {
    intrinsicBounds.setSize(srcImg.getWidth(), srcImg.getHeight());

    int computedWidth = clamp(size, constraints.minWidth(), constraints.maxWidth());
    int computedHeight = clamp(size, constraints.minHeight(), constraints.maxHeight());
    computedBounds.setSize(computedWidth, computedHeight);
  }

  @Override
  protected void performRender(Graphics2D g) {
    g.setBackground(TRANSPARENT_COLOR);
    g.clearRect(0, 0, computedBounds.width, computedBounds.height);
    g.drawImage(srcImg, 0, 0, computedBounds.width, computedBounds.height, null);
  }
}
