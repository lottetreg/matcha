package com.github.lottetreg.matcha;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;

public class UrlEncodedQueryTest {
  @Test
  public void decodeQueryDecodesAQueryString() {
    String query = "slug=how-to-drive-a-car&title=How+to+Drive+a+Car&body=Have+you+ever+tried+to+drive+a+car%3F";

    String decodedQuery = UrlEncodedQuery.decode(query);

    assertEquals("slug=how-to-drive-a-car&title=How to Drive a Car&body=Have you ever tried to drive a car?", decodedQuery);
  }

  @Test
  public void decodeQueryDecodesAQueryStringWithOneParam() {
    String query = "body=Have+you+ever+tried+to+drive+a+car%3F";

    String decodedQuery = UrlEncodedQuery.decode(query);

    assertEquals("body=Have you ever tried to drive a car?", decodedQuery);
  }

  @Test
  public void decodeQueryReturnsAnEmptyQueryString() {
    String query = "";

    String decodedQuery = UrlEncodedQuery.decode(query);

    assertEquals("", decodedQuery);
  }

  @Test
  public void decodeQueryReturnsAQueryStringThatIsNotEncoded() {
    String query = "this isn't encoded";

    String decodedQuery = UrlEncodedQuery.decode(query);

    assertEquals("this isn't encoded", decodedQuery);
  }

  @Test
  public void parseReturnsAMapOfTheQueryParams() {
    String query = "slug=how-to-drive-a-car&title=How+to+Drive+a+Car&body=Have+you+ever+tried+to+drive+a+car%3F";

    Map<String, String> parsedQuery = UrlEncodedQuery.parse(query);

    assertEquals("how-to-drive-a-car", parsedQuery.get("slug"));
    assertEquals("How to Drive a Car", parsedQuery.get("title"));
    assertEquals("Have you ever tried to drive a car?", parsedQuery.get("body"));
  }
}
