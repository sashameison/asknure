package asknure.narozhnyi.core.util;

import java.util.List;
import java.util.Random;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ColorGenerator {
  public static final List<String> COLORS =
      List.of("#3fc4be", "#ffbc2a", "#2462d3", "7dc02a", "#767175", "#af62d1", "#dc3a20", "#59dd93");

  public static String generateColor() {
    var random = new Random();
    var randomPosition = random.nextInt(COLORS.size());
    return COLORS.get(randomPosition);
  }
}
