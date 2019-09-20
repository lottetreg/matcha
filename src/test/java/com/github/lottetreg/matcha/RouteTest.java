package com.github.lottetreg.matcha;

import com.github.lottetreg.cup.Request;
import com.github.lottetreg.cup.Response;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;

public class RouteTest {
  @Rule
  public ExpectedException exceptionRule = ExpectedException.none();

  private Request emptyRequest() {
    return new Request("GET", "/", new HashMap<>(), "");
  }

  private Route newRouteForController(Class controller) {
    return new Route("", "", controller, "");
  }

  @Ignore
  public static class Controller extends BaseController {
    public Response call(String actionName) {
      return new Response(200);
    }
  }

  @Test
  public void itReturnsAResponseFromCallingTheController() {
    Route route = newRouteForController(RouteTest.Controller.class);

    Response response = route.getResponse(emptyRequest());

    assertEquals(200, response.getStatusCode());
    assertEquals("", new String(response.getBody()));
    assertEquals(new HashMap<>(), response.getHeaders());
  }

  @Ignore
  public static class BrokenConstructor implements Controllable {
    Request request;

    public BrokenConstructor() {
      throw new RuntimeException();
    }

    public Controllable setRequest(Request request) {
      this.request = request;
      return this;
    }

    public Response call(String actionName) {
      return new Response(200);
    }
  }

  @Test
  public void itThrowsAnExceptionIfTheControllerConstructorThrowsAnException() {
    exceptionRule.expect(Route.FailedToInstantiateController.class);
    exceptionRule.expectCause(instanceOf(InvocationTargetException.class));
    exceptionRule.expectMessage("BrokenConstructor");

    Route route = newRouteForController(RouteTest.BrokenConstructor.class);

    route.getResponse(emptyRequest());
  }
}
