package com.bladejava.view.template;

import java.io.Writer;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import com.blade.mvc.http.Request;
import com.blade.mvc.http.wrapper.Session;
import com.blade.mvc.view.ModelAndView;
import com.blade.mvc.view.template.TemplateEngine;
import com.blade.mvc.view.template.TemplateException;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import com.blade.context.ApplicationWebContext;


public class VelocityTemplateEngine implements TemplateEngine {

	private final VelocityEngine velocityEngine;

    /**
     * Constructor
     */
    public VelocityTemplateEngine() {
        Properties properties = new Properties();
        properties.setProperty("resource.loader", "class");
        properties.setProperty("class.resource.loader.class",
                "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        velocityEngine = new org.apache.velocity.app.VelocityEngine(properties);
    }

    /**
     * Constructor
     *
     * @param velocityEngine The velocity engine, must not be null.
     */
    public VelocityTemplateEngine(VelocityEngine velocityEngine) {
        if (velocityEngine == null) {
            throw new IllegalArgumentException("velocityEngine must not be null");
        }
        this.velocityEngine = velocityEngine;
    }
	
	public VelocityEngine getVelocityEngine() {
		return velocityEngine;
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
		
		Template template = velocityEngine.getTemplate(modelAndView.getView());
		VelocityContext context = new VelocityContext(modelMap);
        template.merge(context, writer);
		
	}

}