package org.hackystat.projectbrowser.page.telemetry.datapanel;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Color generator to generate colors with maximum distinguish.
 * @author Shaoxuan Zhang
 *
 */
public class RandomColorGenerator {

  /**
   * Generate n random colors, guarantee them have maximum difference from each other.
   * @param n the number of colors to generate.
   * @return a list of Color instances.
   */
  public static List<Color> generateRandomColor(int n) {
    float diff = (float)1 / n;
    Random rand = new Random();
    float h = (float)rand.nextDouble();
    List<Color> colors = new ArrayList<Color>();
    for (int i = 0; i < n; ++i) {
      colors.add(Color.getHSBColor(
          h, (float)(rand.nextDouble() / 2 + 0.5), (float)(rand.nextDouble() / 2 + 0.5)));
      h += diff;
    }
    return colors;
  }
  
  /**
   * Generate n random colors as Strings in RRGGBB format, 
   * guarantee them have maximum difference from each other.
   * @param n the number of colors to generate.
   * @return a list of Strings that represent the colors.
   */
  public static List<String> generateRandomColorInHex(int n) {
    List<Color> colors = generateRandomColor(n);
    List<String> hexColors = new ArrayList<String>();
    for (Color color : colors) {
      hexColors.add(Integer.toHexString(color.getRGB()).substring(2));
    }
    return hexColors;
  }
}
