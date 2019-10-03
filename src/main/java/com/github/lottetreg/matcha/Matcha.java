package com.github.lottetreg.matcha;

import com.github.lottetreg.cup.*;

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

public class Matcha implements Callable {
  private Router router;

  public Matcha(List<Routable> routes) throws IOException {
    this.router = new Router(getAllRoutes(routes));
  }

  public void serve(int portNumber) throws IOException {
    new Cup(this).serve(portNumber);
  }

  public Response call(Request request) {
    return this.router.route(request);
  }

  private static List<Routable> getAllRoutes(List<Routable> routes) throws IOException {
    List<Routable> mutableRoutesList = new ArrayList<>(routes);
    mutableRoutesList.addAll(defaultRoutes());
    mutableRoutesList.addAll(resourcesForCurrentDirectory());

    return mutableRoutesList;
  }

  private static List<Routable> defaultRoutes() {
    return Arrays.asList(
        new ResourceRoute("GET", "/", "/index.html")
    );
  }

  private static List<Routable> resourcesForCurrentDirectory() throws IOException {
    List<Routable> resources = new ArrayList<>();

    BiPredicate<Path, BasicFileAttributes> isRegularFile =
        (path, attrs) -> attrs.isRegularFile();

    Path currentDir = Paths.get(".");

    try (Stream<Path> stream =
             Files.find(currentDir, Integer.MAX_VALUE, isRegularFile)) {

      stream.forEach(path -> {
        String resourcePath = "/" + Path.of(".").relativize(path).toString();
        ResourceRoute resourceRoute = new ResourceRoute("GET", resourcePath, resourcePath);
        resources.add(resourceRoute);
      });
    }

    return resources;
  }
}
