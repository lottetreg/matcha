# Matcha

A Java web framework

## Setup

In the `dependencies` of your build.gradle file add:

```implementation 'com.github.lottetreg:matcha:development-SNAPSHOT'```

## Usage

From within the `main()` function of your app, create a new `Matcha` instance and pass it a list of `Routable`s (the list can be empty). Call `serve` and pass it the port number for the server to run on.

For example:

```java
// in MyBlogApp.java
package com.github.lottetreg.myBlogApp;

import com.github.lottetreg.matcha.Matcha;

import java.io.IOException;
import java.util.Collections;

public class MyBlogApp {
  public static void main(String[] args) {
    try {
      new Matcha(Collections.emptyList()).serve(3000);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }  
```

## Add a Model

Have your model class extend `BaseModel`. In order to make use of mass assignment, your model must have public fields. It must also have a constructor that calls `super`. For example:

```java
// in Post.java
package com.github.lottetreg.myBlogApp;

import com.github.lottetreg.matcha.Model;

import java.util.Map;

public class Post extends BaseModel {
  public String slug;
  public String title;
  public String body;

  public Post(Map<String, String> data) {
    super(data);
  }
}

```

Add a CSV file for your model that is named after the pluralized form of the class name. For example, the `Post` class must have a corresponding posts.csv:

```
slug,title,body
how-to-do-something,How to Do Something,Have you ever wanted to know how to do something?
```

## Adding an Index Page

Place a template in your `resources` directory. Matcha uses [Jtwig](http://jtwig.org/) for its templating.

```html
<!-- In resources/templates/posts/index.twig.html -->
{% for post in posts %}
<h3>{{ post.title }}</h3>
<p>{{ post.body }}</p>
<br>
{% endfor %}
``` 

Create a `PostsController` class and have it extend `BaseController`. Add an action for the index page (you can call it whatever you want). Inside the action, you can find all of the persisted posts with the `BaseModel.all()` method. By adding these posts to the controller's data object with `addData(String, Object)`, the template will be able to access them. Your action should return a new `Template` with the path to the index view:

```java
// in PostsController.java
package com.github.lottetreg.myBlogApp;

import com.github.lottetreg.matcha.BaseController;
import com.github.lottetreg.matcha.Model;
import com.github.lottetreg.matcha.Template;

public class PostsController extends BaseController {

  public Template index() {
    addData("posts", BaseModel.all(Post.class));

    return new Template("/templates/posts/index.twig.html");
  }
}
```

Now, add your route and pass it into your `Matcha` instance:

```java
// in in MyBlogApp.java
package com.github.lottetreg.myBlogApp;

import com.github.lottetreg.matcha.Matcha;
import com.github.lottetreg.matcha.Routable;
import com.github.lottetreg.matcha.Route;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class MyBlogApp {
  public static void main(String[] args) {
    try {
      new Matcha(getRoutes()).serve(3000);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static List<Routable> getRoutes() {
    return Arrays.asList(
        new Route("GET", "/posts", PostsController.class, "index")
    );
  }
}

```

## Adding a Show Page

Add another action to your `PostsController`. We've called this one `show`, and in it we find the first `Post` with the slug specified in the params:

```java
// in PostsController.java
package com.github.lottetreg.myBlogApp;

import com.github.lottetreg.matcha.BaseController;
import com.github.lottetreg.matcha.Model;
import com.github.lottetreg.matcha.Template;

public class PostsController extends BaseController {

  //...

  public Template show() {
    Post post = BaseModel.findBy(Post.class, "slug", getParam("slug"));
    addData("post", post);

    return new Template("/templates/posts/show.twig.html");
  }
}
```

Make sure you have your show template in your `resources` directory.

Finally, add your new route:

```java
// in in MyBlogApp.java
package com.github.lottetreg.myBlogApp;

import com.github.lottetreg.matcha.Matcha;
import com.github.lottetreg.matcha.Routable;
import com.github.lottetreg.matcha.Route;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class MyBlogApp {
  public static void main(String[] args) {
    try {
      new Matcha(getRoutes()).serve(3000);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static List<Routable> getRoutes() {
    return Arrays.asList(
        new Route("GET", "/posts", PostsController.class, "index"),
        new Route("GET", "/posts/:slug", PostsController.class, "show")
    );
  }
}
```









