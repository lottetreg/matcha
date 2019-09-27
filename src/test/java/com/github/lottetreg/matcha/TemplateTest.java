package com.github.lottetreg.matcha;

import org.junit.Test;

import java.util.List;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;

public class TemplateTest {

  @Test
  public void itRendersItself() {
    Template template = new Template("/templates/example.twig.html");
    List<Post> posts = List.of(new Post(Map.of("slug", "how-to-do-something")));
    Map<String, Object> context = Map.of("posts", posts);

    byte[] renderedTemplate = template.render(context);

    assertEquals("\n<h3>how-to-do-something</h3>\n\n", new String(renderedTemplate));
  }
}
