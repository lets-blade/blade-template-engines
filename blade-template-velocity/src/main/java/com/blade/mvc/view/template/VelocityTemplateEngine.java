package com.blade.mvc.view.template;

import java.io.Writer;
import java.util.Map;
import java.util.Set;

import com.blade.Blade;
import com.blade.context.DynamicClassReader;
import com.blade.mvc.http.Request;
import com.blade.mvc.http.wrapper.Session;
import com.blade.mvc.view.ModelAndView;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import com.blade.context.ApplicationWebContext;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.apache.velocity.runtime.resource.loader.JarResourceLoader;

public class VelocityTemplateEngine implements TemplateEngine {

	private VelocityEngine ve;

	private String suffix = "/templates/";

    /**
     * Constructor
     */
    public VelocityTemplateEngine() {
		ve = new VelocityEngine();
		if(DynamicClassReader.isJarContext()){
			ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "jar");
			ve.setProperty("jar.resource.loader.class", JarResourceLoader.class.getName());
			ve.setProperty("jar.resource.loader.path", Blade.$().webRoot());
		} else{
			ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
			ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
		}
		ve.setProperty(RuntimeConstants.INPUT_ENCODING, "UTF-8");
		ve.setProperty(RuntimeConstants.OUTPUT_ENCODING, "UTF-8");
		ve.init();
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
        this.ve = velocityEngine;
    }
	
	public VelocityEngine getVelocityEngine() {
		return this.ve;
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
			Template template = ve.getTemplate(suffix + modelAndView.getView());
			VelocityContext context = new VelocityContext(modelMap);
			template.merge(context, writer);
		} catch (Exception e){
			throw new TemplateException(e);
		}
	}

}