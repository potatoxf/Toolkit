package pxf.toolkit.extension.captcha.components;

import com.google.code.kaptcha.util.Configurable;
import com.google.common.collect.Lists;
import java.awt.Color;
import java.util.List;
import java.util.Random;

/**
 * 验证码颜色
 *
 * @author potatoxf
 * @date 2021/5/1
 */
public abstract class CaptchaTool extends Configurable {

  public Color getColor() {
    List<Color> colors = Lists.newArrayList();
    colors.add(new Color(0, 135, 255));
    colors.add(new Color(51, 153, 51));
    colors.add(new Color(255, 102, 102));
    colors.add(new Color(255, 153, 0));
    colors.add(new Color(153, 102, 0));
    colors.add(new Color(153, 102, 153));
    colors.add(new Color(51, 153, 153));
    colors.add(new Color(102, 102, 255));
    colors.add(new Color(0, 102, 204));
    colors.add(new Color(204, 51, 51));
    colors.add(new Color(128, 153, 65));
    Random random = new Random();
    int colorIndex = random.nextInt(10);
    return colors.get(colorIndex);
  }
}