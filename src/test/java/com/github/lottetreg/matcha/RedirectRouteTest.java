package com.github.lottetreg.matcha;

import com.github.lottetreg.cup.Request;
import com.github.lottetreg.cup.Response;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class RedirectRouteTest {
  @Test
  public void itHasAPath() {
    RedirectRoute redirectRoute = new RedirectRoute("", "/", "");
    assertEquals("/", redirectRoute.getPath());
  }

  @Test
  public void itHasAMethod() {
    RedirectRoute redirectRoute = new RedirectRoute("GET", "", "");
    assertEquals("GET", redirectRoute.getMethod());
  }

  @Test
  public void itHasARedirectPath() {
    RedirectRoute redirectRoute = new RedirectRoute("", "", "/redirectRoute");
    assertEquals("/redirectRoute", redirectRoute.getRedirectPath());
  }

  @Test
  public void itReturns301ResponseWithTheRedirectPathInTheHeaders() throws IOException {
    Request request = new Request("GET", "/", new HashMap<>(Map.of("Host", "www.example.com")), "");

    RedirectRoute redirectRoute = new RedirectRoute("", "", "/some_other_path");

    Response response = redirectRoute.getResponse(request);

    assertEquals(301, response.getStatusCode());
    assertEquals("", new String(response.getBody()));
    assertEquals("http://www.example.com/some_other_path", response.getHeaders().get("Location"));
  }
}
