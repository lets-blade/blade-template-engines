package com.hellokaton.blade.template;

import com.hellokaton.blade.exception.TemplateException;
import com.hellokaton.blade.mvc.WebContext;
import com.hellokaton.blade.mvc.http.Request;
import com.hellokaton.blade.mvc.http.Session;
import com.hellokaton.blade.mvc.ui.ModelAndView;
import com.hellokaton.blade.mvc.ui.template.TemplateEngine;
import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.loader.Loader;
import com.mitchellbosecke.pebble.template.PebbleTemplate;

import java.io.Writer;
import java.util.Map;

public class PebbleTemplateEngine implements TemplateEngine {

    /**
     * The Pebble Engine instance.
     */
    private final PebbleEngine engine;

    /**
     * Construct a new template engine using pebble with a default engine.
     */
    public PebbleTemplateEngine() {
        engine = new PebbleEngine.Builder().build();
    }

    /**
     * Construct a new template engine using pebble with an engine using a special loader.
     */
    public <T> PebbleTemplateEngine(Loader<T> loader) {
        this.engine = new PebbleEngine.Builder().loader(loader).build();
    }

    /**
     * Construct a new template engine using pebble with a specified engine.
     *
     * @param engine The pebble template engine.
     */
    public PebbleTemplateEngine(PebbleEngine engine) {
        this.engine = engine;
    }

    public PebbleEngine getPebbleEngine() {
        return engine;
    }

    @Override
    public void render(ModelAndView modelAndView, Writer writer) {

        Map<String, Object> modelMap = modelAndView.getModel();

        Request request = WebContext.request();
        Session session = request.session();

        modelMap.putAll(request.attributes());

        if (null != session) {
            modelMap.putAll(session.attributes());
        }

        try {
            PebbleTemplate template = engine.getTemplate(modelAndView.getView());
            template.evaluate(writer, modelMap);
        } catch (Exception e) {
            throw new TemplateException(e);
        }
    }

}