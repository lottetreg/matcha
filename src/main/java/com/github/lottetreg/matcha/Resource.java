package com.github.lottetreg.matcha;

import com.github.lottetreg.cup.BaseRoute;
import com.github.lottetreg.cup.Request;
import com.github.lottetreg.cup.Response;

import java.nio.file.Path;
import java.util.Map;

public class Resource extends BaseRoute {
  private String resourcePath;

  public Resource(String method, String path, String resourcePath) {
    super(method, path);
    this.resourcePath = resourcePath;
  }

  public Response getResponse(Request request) {
    try {
      String contentType = FileHelpers.getContentType(Path.of(getResourcePath()));
      byte[] fileContents = FileHelpers.readFile(Path.of(getResourcePath()));

      return new Response(200, Map.of("Content-Type", contentType), fileContents);

    } catch (FileHelpers.MissingFile e) {
      return new Response(404);
    }
  }

  public String getResourcePath() {
    return this.resourcePath;
  }
}
