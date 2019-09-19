package com.github.lottetreg.matcha;

import com.github.lottetreg.cup.Cup;
import com.github.lottetreg.cup.Responsive;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.stream.Stream;

public class HTTPServer {
  public static void main(String[] args) {
    try {
      new Cup().serve(5000, getAllRoutes(getRoutes()));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static List<Responsive> getRoutes() {
    return new ArrayList<>(Arrays.asList(
        new Route("GET", "/simple_get", ExampleController.class, "empty"),
        new Route("POST", "/echo_body", ExampleController.class, "echo"),
        new Route("GET", "/method_options", ExampleController.class, "empty"),
        new Route("GET", "/method_options2", ExampleController.class, "empty"),
        new Route("PUT", "/method_options2", ExampleController.class, "empty"),
        new Route("POST", "/method_options2", ExampleController.class, "empty"),
        new Route("GET", "/pickles", ExampleController.class, "pickles"),
        new Route("GET", "/pickles_with_header", ExampleController.class, "picklesWithHeader"),
        new Redirect("GET", "/redirect", "/simple_get"),
        new Route("HEAD", "/get_with_body", ExampleController.class, ""),
        new Route("GET", "/time", ExampleController.class, "time")
    ));
  }

  private static List<Responsive> getAllRoutes(List<Responsive> routes) {
    routes.addAll(defaultRoutes());
    routes.addAll(resourcesForCurrentDirectory());

    return routes;
  }

  private static List<Responsive> defaultRoutes() {
    return Arrays.asList(
        new Resource("GET", "/", "/index.html")
    );
  }

  private static List<Responsive> resourcesForCurrentDirectory() {
    List<Responsive> resources = new ArrayList();

    BiPredicate<Path, BasicFileAttributes> isRegularFile =
        (path, attrs) -> attrs.isRegularFile();

    Path currentDir = Paths.get(".");
    try (Stream<Path> stream =
             Files.find(currentDir, Integer.MAX_VALUE, isRegularFile)) {

      stream.forEach(path -> {
        String resourcePath = "/" + Path.of(".").relativize(path).toString();
        Resource resource = new Resource("GET", resourcePath, resourcePath);
        resources.add(resource);
      });

    } catch (IOException e) {
      e.printStackTrace();
    }

    return resources;
  }
}
