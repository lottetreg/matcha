package com.github.lottetreg.matcha;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

class UrlEncodedQuery {
  static String decode(String query) {
    try {
      return URLDecoder.decode(query, StandardCharsets.UTF_8.toString());
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
  }

  static Map<String, String> parse(String query) {
    String querySeparator = "&";
    String paramSeparator = "=";
    HashMap<String, String> queryParams = new HashMap<>();

    Arrays.stream(query.split(querySeparator))
        .forEach(param -> {
          String[] splitParam = param.split(paramSeparator);
          if (splitParam.length == 2) {
            queryParams.put(splitParam[0], decode(splitParam[1]));
          }
        });

    return queryParams;
  }
}
