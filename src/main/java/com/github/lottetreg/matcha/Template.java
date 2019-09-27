package com.github.lottetreg.matcha;

import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import java.util.Map;

public class Template {
  private String path;

  public Template(String path) {
    this.path = path;
  }

  byte[] render(Map<String, Object> context) {
    JtwigTemplate template = JtwigTemplate.classpathTemplate(this.path);
    JtwigModel model = JtwigModel.newModel();

    context.forEach(model::with);

    return template.render(model).getBytes();
  }

  String getPath() {
    return this.path;
  }
}
