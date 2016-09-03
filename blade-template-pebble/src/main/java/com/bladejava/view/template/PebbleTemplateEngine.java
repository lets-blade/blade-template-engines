package com.bladejava.view.template;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;
import java.util.Set;

import com.blade.context.ApplicationWebContext;
import com.blade.mvc.http.Request;
import com.blade.mvc.http.wrapper.Session;
import com.blade.mvc.view.ModelAndView;
import com.blade.mvc.view.template.TemplateEngine;
import com.blade.mvc.view.template.TemplateException;
import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.error.PebbleException;
import com.mitchellbosecke.pebble.loader.ClasspathLoader;
import com.mitchellbosecke.pebble.loader.Loader;
import com.mitchellbosecke.pebble.template.PebbleTemplate;

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
	public PebbleTemplateEngine(Loader loader) {
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
	public void render(ModelAndView modelAndView, Writer writer) throws TemplateException {
		
		Map<String, Object> modelMap = modelAndView.getModel();
		
		Request request = ApplicationWebContext.request();
		Session session = request.session();

		Set<String> attrs = request.attributes();
		if (null != attrs && attrs.size() > 0) {
			for (String attr : attrs) {
				modelMap.put(attr, request.attribute(attr));
			}
		}

		Set<String> session_attrs = session.attributes();
		if (null != session_attrs && session_attrs.size() > 0) {
			for (String attr : session_attrs) {
				modelMap.put(attr, session.attribute(attr));
			}
		}
		
		try {
			PebbleTemplate template = engine.getTemplate(modelAndView.getView());
			template.evaluate(writer, modelMap);
		} catch (PebbleException e) {
			throw new TemplateException(e);
		} catch (IOException e) {
			throw new TemplateException(e);
		}
		
	}

}