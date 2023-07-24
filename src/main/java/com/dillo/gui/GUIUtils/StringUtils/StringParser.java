package com.dillo.gui.GUIUtils.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringParser {

  public static StringParserClass parseStringFlawed(String input) {
    String emojiPattern = "[\\p{So}]";
    Pattern regex = Pattern.compile(emojiPattern);
    Matcher matcherEmoji = regex.matcher(input);
    String updatedText = matcherEmoji.replaceAll("");

    Pattern pattern = Pattern.compile("PRISTINE! You found  Flawed (\\w+) Gemstone x(\\d+)!");

    Matcher matcher = pattern.matcher(updatedText);

    if (matcher.find()) {
      String numberString = matcher.group(2);
      int number = Integer.parseInt(numberString);
      String gemName = matcher.group(1);

      return new StringParserClass(gemName, number);
    }

    return null;
  }

  public static StringParserClass parseStringRough(String input) {
    Pattern pattern = Pattern.compile("&r&d&lPRISTINE! &r&fYou found &r&aFlawed (\\w+) Gemstone &r&8x\\d+&r&f!&r");
    Matcher matcher = pattern.matcher(input);

    if (matcher.find()) {
      String numberString = matcher.group(1);
      int number = Integer.parseInt(numberString);
      String gemName = matcher.group(2);

      return new StringParserClass(gemName, number);
    }

    return null;
  }
}
