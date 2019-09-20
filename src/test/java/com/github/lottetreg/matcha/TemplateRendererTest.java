package com.github.lottetreg.matcha;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Map;

import static org.junit.Assert.assertEquals;

public class TemplateRendererTest {
  @Rule
  public ExpectedException exceptionRule = ExpectedException.none();

  @Test
  public void itRendersATemplateWithOneInterpolation() {
    String stringTemplate = "<h1><%= name %></h1>";
    Map<String, String> context = Map.of("name", "Pickles");

    assertEquals("<h1>Pickles</h1>", TemplateRenderer.render(context, stringTemplate));
  }

  @Test
  public void itRendersATemplateWithTwoInterpolations() {
    String stringTemplate = "<h1><%= name %></h1><h2><%= birthDate %></h2>";
    Map<String, String> context = Map.of(
        "name", "Pickles",
        "birthDate", "10/07/2014");

    assertEquals("<h1>Pickles</h1><h2>10/07/2014</h2>", TemplateRenderer.render(context, stringTemplate));
  }

  @Test
  public void itRendersATemplateWithNoInterpolations() {
    String stringTemplate = "<h1>Pickles</h1>";
    Map<String, String> context = Map.of();

    assertEquals("<h1>Pickles</h1>", TemplateRenderer.render(context, stringTemplate));
  }

  @Test
  public void itRendersATemplateWithNoMatchingContextVariable() {
    String stringTemplate = "<h1><%= name %></h1>";
    Map<String, String> context = Map.of();

    exceptionRule.expect(TemplateRenderer.MissingContextKey.class);
    exceptionRule.expectMessage("name");

    TemplateRenderer.render(context, stringTemplate);
  }
}
