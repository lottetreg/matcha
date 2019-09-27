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

  public Matcha(List<Responsive> routes) throws IOException {
    this.router = new Router(getAllRoutes(routes));
  }

  public void serve(int portNumber) throws IOException {
    new Cup(this).serve(portNumber);
  }

  public Response call(Request request) {
    return this.router.route(request);
  }


  // TODO: move these somewhere else
  private static List<Responsive> getAllRoutes(List<Responsive> routes) throws IOException {
    List<Responsive> mutableRoutesList = new ArrayList<>(routes);
    mutableRoutesList.addAll(defaultRoutes());
    mutableRoutesList.addAll(resourcesForCurrentDirectory());

    return mutableRoutesList;
  }

  private static List<Responsive> defaultRoutes() {
    return Arrays.asList(
        new Resource("GET", "/", "/index.html")
    );
  }

  private static List<Responsive> resourcesForCurrentDirectory() throws IOException {
    List<Responsive> resources = new ArrayList<>();

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
    }

    return resources;
  }
}
