# blade-template-jetbrick

How to use the Jetbrick-Template route for Blade example:

```java
Blade blade = me();
blade.viewEngin(new JetbrickTemplateEngine());

blade.get("/show", new RouteHandler() {
	public void handle(Request request, Response response) {
		request.attribute("name", "blade-1.6");
		response.render("views/show.jetx");
	}
});
```

Jetbrick-Template refer: [https://github.com/subchen/jetbrick-template-2x](https://github.com/subchen/jetbrick-template-2x)
