package com.github.lottetreg.matcha;

import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TemplateRenderer {
  static String render(Map<String, String> context, String stringTemplate) {
    Pattern pattern = Pattern.compile("<%=\\s+(\\w+)\\s+%>");
    Matcher matcher = pattern.matcher(stringTemplate);

    return matcher.replaceAll(matchResult -> {
      String match = matchResult.group(1);
      Optional<String> contextValue = Optional.ofNullable(context.get(match));

      return contextValue.orElseThrow(() ->
          new MissingContextKey(matchResult.group(1)));
    });
  }

  static public class MissingContextKey extends RuntimeException {
    MissingContextKey(String key) {
      super(key);
    }
  }
}
