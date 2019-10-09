package com.github.lottetreg.matcha;

import com.github.lottetreg.cup.Request;
import com.github.lottetreg.cup.Response;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;

public class BaseControllerTest {
  @Ignore
  public static class TestController extends BaseController {
    public void empty() {
    }

    public void error() {
      throw new RuntimeException("Something went wrong");
    }

    public Template embeddedData() {
      List<Post> posts = List.of(new Post("how-to-do-something", "", ""));
      addData("posts", posts);

      return new Template("/templates/example.twig.html");
    }
  }

  private Request emptyRequest() {
    return new Request("GET", "/", new HashMap<>(), "");
  }

  @Rule
  public ExpectedException exceptionRule = ExpectedException.none();

  @Test
  public void callReturnsAResponseFromAnActionThatReturnsNothing() {
    Controllable controller = new TestController().setRequest(emptyRequest());

    Response response = controller.call("empty");

    assertEquals(200, response.getStatusCode());
    assertEquals("", new String(response.getBody()));
    assertEquals(new HashMap<>(), response.getHeaders());
  }

  @Test
  public void callReturnsAResponseFromAnActionThatReturnsATemplate() {
    Controllable controller = new TestController().setRequest(emptyRequest());

    Response response = controller.call("embeddedData");

    assertEquals(200, response.getStatusCode());
    assertEquals("\n<h3>how-to-do-something</h3>\n\n", new String(response.getBody()));
    assertEquals(new HashMap<>(Map.of("Content-Type", "text/html")), response.getHeaders());
  }

  @Test
  public void addParamsAddsToTheParams() {
    Controllable controller = new TestController()
        .addParams(Map.of("some-param", "some value"));

    assertEquals("some value", controller.getParam("some-param"));
  }

  @Test
  public void setRequestAddsTheParamsFromTheRequestBody() {
    String body = "slug=how-to-drive-a-car&title=How+to+Drive+a+Car&body=Have+you+ever+tried+to+drive+a+car%3F";
    Request request = new Request("","", new HashMap<>(), body);

    Controllable controller = new TestController()
        .setRequest(request);

    assertEquals("how-to-drive-a-car", controller.getParam("slug"));
    assertEquals("How to Drive a Car", controller.getParam("title"));
    assertEquals("Have you ever tried to drive a car?", controller.getParam("body"));
  }

  @Test
  public void callThrowsAnExceptionIfTheActionIsMissing() {
    Controllable controller = new TestController().setRequest(emptyRequest());

    exceptionRule.expect(Controllable.MissingControllerAction.class);
    exceptionRule.expectCause(instanceOf(NoSuchMethodException.class));
    exceptionRule.expectMessage("missingAction");

    controller.call("missingAction");
  }

  @Test
  public void callThrowsAnExceptionIfTheActionThrowsAnException() {
    Controllable controller = new TestController().setRequest(emptyRequest());

    exceptionRule.expect(Controllable.FailedToInvokeControllerAction.class);
    exceptionRule.expectCause(instanceOf(InvocationTargetException.class));
    exceptionRule.expectMessage("error");

    controller.call("error");
  }
}
