# blade-template-velocity

How to use the Velocity Template route for Blade example:

```java
Blade blade = me();
blade.viewEngin(new VelocityTemplateEngine());

blade.get("/show", new RouteHandler() {
	public void handle(Request request, Response response) {
		request.attribute("name", "blade-1.6");
		response.render("views/show.vm");
	}
});
```
