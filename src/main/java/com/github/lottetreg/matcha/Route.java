package com.github.lottetreg.matcha;

import com.github.lottetreg.cup.Request;
import com.github.lottetreg.cup.Response;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class Route extends BaseRoute {
  private Class controllerClass;
  private String actionName;

  public Route(String method, String path, Class controllerClass, String actionName) {
    super(method, path);
    this.controllerClass = controllerClass;
    this.actionName = actionName;
  }

  public Response getResponse(Request request) {
    String controllerName = getControllerName();
    String actionName = getActionName();

    try {
      Constructor<?> constructor = this.controllerClass.getConstructor();
      Controllable controller = ((Controllable) constructor.newInstance()).setRequest(request);

      return controller.call(actionName);

    } catch (NoSuchMethodException e) {
      throw new MissingControllerConstructor(controllerName, e);

    } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
      throw new FailedToInstantiateController(controllerName, e);
    }
  }

  private String getControllerName() {
    return this.controllerClass.getSimpleName();
  }

  private String getActionName() {
    return this.actionName;
  }

  static class MissingControllerConstructor extends RuntimeException {
    MissingControllerConstructor(String controller, Throwable cause) {
      super(controller, cause);
    }
  }

  static class FailedToInstantiateController extends RuntimeException {
    FailedToInstantiateController(String controller, Throwable cause) {
      super(controller, cause);
    }
  }
}
