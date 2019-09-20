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

public class Matcha {
  public void serve(int portNumber, List<Responsive> routes) throws IOException {
    new Cup().serve(portNumber, getAllRoutes(routes));
  }

  private static List<Responsive> getAllRoutes(List<Responsive> routes) throws IOException {
    routes.addAll(defaultRoutes());
    routes.addAll(resourcesForCurrentDirectory());

    return routes;
  }

  private static List<Responsive> defaultRoutes() {
    return Arrays.asList(
        new Resource("GET", "/", "/index.html")
    );
  }

  private static List<Responsive> resourcesForCurrentDirectory() throws IOException { // pass in directory, add unit tests
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