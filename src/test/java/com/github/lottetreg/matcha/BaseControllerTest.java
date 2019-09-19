package com.github.lottetreg.matcha;

import com.github.lottetreg.cup.Request;
import com.github.lottetreg.cup.Response;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;

public class BaseControllerTest {
  public class TestController extends BaseController {
    public void empty() {
    }

    public String hello() {
      return "Hello!";
    }

    public Path index() {
      return Path.of("/src/test/java/com/github/lottetreg/matcha/support/index.html");
    }

    public Path missingFile() {
      return Path.of("/missing.html");
    }

    public void error() {
      throw new RuntimeException("Something went wrong");
    }

    public Template embeddedData() {
      addData("name", "Pickles");
      return new Template("/src/test/java/com/github/lottetreg/matcha/support/embedded_data.html");
    }

    public Template missingData() {
      return new Template("/src/test/java/com/github/lottetreg/matcha/support/embedded_data.html");
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
  public void callReturnsAResponseFromAnActionThatReturnsAString() {
    Controllable controller = new TestController().setRequest(emptyRequest());

    Response response = controller.call("hello");

    assertEquals(200, response.getStatusCode());
    assertEquals("Hello!", new String(response.getBody()));
    assertEquals(new HashMap<>(Map.of("Content-Type", "text/plain")), response.getHeaders());
  }

  @Test
  public void callReturnsAResponseFromAnActionThatReturnsAPath() {
    Controllable controller = new TestController().setRequest(emptyRequest());

    Response response = controller.call("index");

    assertEquals(200, response.getStatusCode());
    assertEquals("<h1>Hello, World!</h1>\n", new String(response.getBody()));
    assertEquals(new HashMap<>(Map.of("Content-Type", "text/html")), response.getHeaders());
  }

  @Test
  public void callReturnsAResponseFromAnActionThatReturnsATemplate() {
    Controllable controller = new TestController().setRequest(emptyRequest());

    Response response = controller.call("embeddedData");

    assertEquals(200, response.getStatusCode());
    assertEquals("<h1>Hello, Pickles!</h1>\n", new String(response.getBody()));
    assertEquals(new HashMap<>(Map.of("Content-Type", "text/html")), response.getHeaders());
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

  @Test
  public void callThrowsAnExceptionIfTemplateDataIsMissing() {
    Controllable controller = new TestController().setRequest(emptyRequest());

    exceptionRule.expect(Controllable.MissingTemplateData.class);
    exceptionRule.expectCause(instanceOf(TemplateRenderer.MissingContextKey.class));
    exceptionRule.expectMessage("name");

    controller.call("missingData");
  }
}
