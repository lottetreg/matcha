package com.github.lottetreg.matcha;

import com.github.lottetreg.cup.Request;
import com.github.lottetreg.cup.Response;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class BaseController implements Controllable {
  private Request request;
  private HashMap<String, Object> params = new HashMap<>();
  private HashMap<String, String> headers = new HashMap<>();
  private HashMap<String, Object> data = new HashMap<>();

  public Request getRequest() {
    return this.request;
  }

  public Map<String, Object> getParams() {
    return this.params;
  }

  public Object getParam(String param) {
    return getParams().get(param);
  }

  public void addHeader(String key, String value) {
    this.headers.put(key, value);
  }

  public void addData(String key, Object value) {
    this.data.put(key, value);
  }

  public Controllable addParams(Map<String, String> newParams) {
    newParams.forEach((key, value) -> this.params.put(key, value));

    return this;
  }

  public Controllable setRequest(Request request) {
    this.request = request;
    addParams(UrlEncodedQuery.parse(this.request.getBody()));

    return this;
  }

  public Response call(String actionName) {
    try {
      Method action = getClass().getMethod(actionName);
      Object result = action.invoke(this);
      int statusCode = 200;
      byte[] body = new byte[]{};

      if (result instanceof Template) {
        Template template = (Template) result;
        Path path = Path.of(template.getPath());
        addHeader("Content-Type", FileHelpers.getContentType(path));
        body = template.render(this.data);

      } if (result instanceof Redirect) {
        Redirect redirect = (Redirect) result;
        String path = redirect.getPath();
        String uri = "http://" + getRequest().getHeader("Host") + path;
        addHeader("Location", uri);
        statusCode = 302;
      }

      return new Response(statusCode, this.headers, body);

    } catch (NoSuchMethodException e) {
      throw new MissingControllerAction(actionName, e);

    } catch (InvocationTargetException | IllegalAccessException e) {
      throw new FailedToInvokeControllerAction(actionName, e);
    }
  }
}
