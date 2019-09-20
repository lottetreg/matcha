package com.github.lottetreg.matcha;

import com.github.lottetreg.cup.Request;
import com.github.lottetreg.cup.Response;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class RedirectTest {
  @Test
  public void itHasAPath() {
    Redirect redirect = new Redirect("", "/", "");
    assertEquals("/", redirect.getPath());
  }

  @Test
  public void itHasAMethod() {
    Redirect redirect = new Redirect("GET", "", "");
    assertEquals("GET", redirect.getMethod());
  }

  @Test
  public void itHasARedirectPath() {
    Redirect redirect = new Redirect("", "", "/redirect");
    assertEquals("/redirect", redirect.getRedirectPath());
  }

  @Test
  public void itReturns301ResponseWithTheRedirectPathInTheHeaders() throws IOException {
    Request request = new Request("GET", "/", new HashMap<>(Map.of("Host", "www.example.com")), "");

    Redirect redirect = new Redirect("", "", "/some_other_path");

    Response response = redirect.getResponse(request);

    assertEquals(301, response.getStatusCode());
    assertEquals("", new String(response.getBody()));
    assertEquals("http://www.example.com/some_other_path", response.getHeaders().get("Location"));
  }
}
