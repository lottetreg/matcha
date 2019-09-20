package com.github.lottetreg.matcha;

import java.nio.file.Path;
import java.util.Map;

public class Template {
  private Path path;

  public Template(String path) {
    this.path = Path.of(path);
  }

  byte[] render(Map<String, String> context) {
    String fileContent = new String(FileHelpers.readFile(this.path));
    String renderedStringTemplate = TemplateRenderer.render(context, fileContent);

    return renderedStringTemplate.getBytes();
  }

  Path getPath() {
    return this.path;
  }
}
