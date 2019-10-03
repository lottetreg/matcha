package com.github.lottetreg.matcha;

import com.github.lottetreg.cup.Request;
import com.github.lottetreg.cup.Response;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
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
  public void hasPathReturnsTrueIfThePathMatches() {
    BaseRoute route = new TestRoute("GET", "/posts");

    assertTrue(route.hasPath("/posts"));
  }

  @Test
  public void hasPathReturnsTrueIfThePathMatchesWithOneUrlParam() {
    BaseRoute route = new TestRoute("GET", "/posts/:slug");

    assertTrue(route.hasPath("/posts/some-slug"));
  }

  @Test
  public void hasPathReturnsTrueIfThePathMatchesWithMultipleUrlParams() {
    BaseRoute route = new TestRoute("GET", "/posts/:slug/comments/:id");

    assertTrue(route.hasPath("/posts/some-slug/comments/1"));
  }

  @Test
  public void hasPathReturnsFalseIfThePathDoesNotMatch() {
    BaseRoute route = new TestRoute("GET", "/posts");

    assertFalse(route.hasPath("/"));
  }

  @Test
  public void hasPathReturnsFalseIfThePathDoesNotMatchWithAUrlParam() {
    BaseRoute route = new TestRoute("GET", "/posts/:slug");

    assertFalse(route.hasPath("/posts"));
  }

  @Test
  public void hasPathReturnsFalseIfItDoesNotMatchAPathWithAUrlParam() {
    BaseRoute route = new TestRoute("GET", "/posts");

    assertFalse(route.hasPath("/posts/some-slug"));
  }

  @Test
  public void getParamsReturnsOneUrlParam() {
    BaseRoute route = new TestRoute("GET", "/posts/:slug");

    Map<String, String> params = route.getParams("/posts/some-slug");

    assertEquals("some-slug", params.get("slug"));
  }

  @Test
  public void getParamsReturnsMultipleUrlParams() {
    BaseRoute route = new TestRoute("GET", "/posts/:slug/comments/:id");

    Map<String, String> params = route.getParams("/posts/some-slug/comments/1");

    assertEquals("some-slug", params.get("slug"));
    assertEquals("1", params.get("id"));
  }
}
