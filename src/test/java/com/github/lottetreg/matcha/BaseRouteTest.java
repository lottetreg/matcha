package com.github.lottetreg.matcha;

import com.github.lottetreg.cup.Request;
import com.github.lottetreg.cup.Response;
import org.junit.Ignore;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

public class BaseRouteTest {
  @Ignore
  class TestRoute extends BaseRoute {
    TestRoute(String method, String path) {
      super(method, path);
    }

    public Response getResponse(Request request) {
      return new Response(200);
    }
  }

  @Test
  public void pathMatchesReturnsTrueIfThePathMatches() {
    BaseRoute route = new TestRoute("GET", "/posts");

    assertTrue(route.hasPath("/posts"));
  }

  @Test
  public void pathMatchesReturnsTrueIfThePathMatchesWithOneUrlParam() {
    BaseRoute route = new TestRoute("GET", "/posts/:slug");

    assertTrue(route.hasPath("/posts/some-slug"));
  }

  @Test
  public void pathMatchesReturnsTrueIfThePathMatchesWithMultipleUrlParams() {
    BaseRoute route = new TestRoute("GET", "/posts/:slug/comments/:id");

    assertTrue(route.hasPath("/posts/some-slug/comments/1"));
  }

  @Test
  public void pathMatchesReturnsFalseIfThePathDoesNotMatch() {
    BaseRoute route = new TestRoute("GET", "/posts");

    assertFalse(route.hasPath("/"));
  }

  @Test
  public void pathMatchesReturnsFalseIfThePathDoesNotMatchWithAUrlParam() {
    BaseRoute route = new TestRoute("GET", "/posts/:slug");

    assertFalse(route.hasPath("/posts"));
  }

  @Test
  public void pathMatchesReturnsFalseIfItDoesNotMatchAPathWithAUrlParam() {
    BaseRoute route = new TestRoute("GET", "/posts");

    assertFalse(route.hasPath("/posts/some-slug"));
  }
}
