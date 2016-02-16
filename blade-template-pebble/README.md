# blade-template-pebble

How to use the Pebble template route for Blade example:

```java
Blade blade = me();
blade.viewEngin(new PebbleTemplateEngine());

blade.get("/show", new RouteHandler() {
	public void handle(Request request, Response response) {
		request.attribute("name", "blade-1.6");
		response.render("views/show.pebble");
	}
});
```
