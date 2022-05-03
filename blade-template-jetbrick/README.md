# blade-template-jetbrick

How to use the Jetbrick-Template route for Blade example:

```java
Blade blade = Blade.of();
blade.templateEngine(new JetbrickTemplateEngine());

blade.get("/show", ctx -> {
    ctx.attribute("name", "blade-1.6");
    ctx.render("views/show.jetx");
});
```

Jetbrick-Template refer: [https://github.com/subchen/jetbrick-template-2x](https://github.com/subchen/jetbrick-template-2x)
