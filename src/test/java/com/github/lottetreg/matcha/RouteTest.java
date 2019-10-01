package com.github.lottetreg.matcha;

import com.github.lottetreg.cup.Request;
import com.github.lottetreg.cup.Response;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;

public class RouteTest {
  @Rule
  public ExpectedException exceptionRule = ExpectedException.none();

  @Ignore
  public static class MockController extends BaseController {
    public Response call(String actionName) {
      return new Response(200);
    }
  }

  @Test
  public void itReturnsAResponseFromCallingTheController() {
    Route route = new Route("", "", RouteTest.MockController.class, "");
    Request request = new Request("", "", new HashMap<>(), "");

    Response response = route.getResponse(request);

    assertEquals(200, response.getStatusCode());
    assertEquals("", new String(response.getBody()));
    assertEquals(new HashMap<>(), response.getHeaders());
  }

  @Test
  public void itSetsTheParamsOnTheController() {
    Route route = new Route("", "/posts/:slug", RouteTest.MockController.class, "");
    Request request = new Request("", "/posts/how-to-do-something", new HashMap<>(), "");

    route.getResponse(request);

    assertEquals("how-to-do-something", route.getController().getParams().get("slug"));
  }

  @Ignore
  public static class BrokenConstructor implements Controllable {
    public BrokenConstructor() {
      throw new RuntimeException();
    }

    public Controllable setRequest(Request request) {
      return this;
    }

    public Controllable addParams(Map<String, String> params) {
      return this;
    }

    public Map<String, String> getParams() {
      return Map.of();
    }

    public String getParam(String paramName) { return ""; }

    public Response call(String actionName) {
      return new Response(200);
    }
  }

  @Test
  public void itThrowsAnExceptionIfTheControllerConstructorThrowsAnException() {
    exceptionRule.expect(Route.FailedToInstantiateController.class);
    exceptionRule.expectCause(instanceOf(InvocationTargetException.class));
    exceptionRule.expectMessage("BrokenConstructor");

    Route route = new Route("", "", BrokenConstructor.class, "");
    Request request = new Request("", "", new HashMap<>(), "");
    route.getResponse(request);
  }
}
