package com.github.lottetreg.matcha;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;

public class TemplateTest {
  @Test
  public void itRendersItself() {
    Template template = new Template("/src/test/java/com/github/lottetreg/matcha/support/embedded_data.html");
    Map<String, String> context = Map.of("name", "Pickles");

    assertEquals("<h1>Hello, Pickles!</h1>\n", new String(template.render(context)));
  }
}
