package com.github.lottetreg.matcha;

import com.github.lottetreg.cup.Request;
import com.github.lottetreg.cup.Response;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class ResourceRouteTest {
  private Request emptyRequest() {
    return new Request("GET", "/", new HashMap<>(), "");
  }

  @Test
  public void itHasAPath() {
    ResourceRoute resourceRoute = new ResourceRoute("", "/", "");
    assertEquals("/", resourceRoute.getPath());
  }

  @Test
  public void itHasAMethod() {
    ResourceRoute resourceRoute = new ResourceRoute("GET", "", "");
    assertEquals("GET", resourceRoute.getMethod());
  }

  @Test
  public void itHasAResourcePath() {
    ResourceRoute resourceRoute = new ResourceRoute("", "", "/");
    assertEquals("/", resourceRoute.getResourcePath());
  }

  @Test
  public void itReturnsA200ResponseWithTheResource() {
    ResourceRoute resourceRoute = new ResourceRoute("", "", "/src/test/java/com/github/lottetreg/matcha/support/index.html");

    Response response = resourceRoute.getResponse(emptyRequest());

    assertEquals(200, response.getStatusCode());
    assertEquals("<h1>Hello, World!</h1>\n", new String(response.getBody()));
    assertEquals("text/html", response.getHeaders().get("Content-Type"));
  }

  @Test
  public void itReturnsA404IfTheResourceIsMissing() {
    ResourceRoute resourceRoute = new ResourceRoute("", "", "/missing.html");

    Response response = resourceRoute.getResponse(emptyRequest());

    assertEquals(404, response.getStatusCode());
    assertEquals("", new String(response.getBody()));
    assertEquals(new HashMap<>(), response.getHeaders());
  }
}
