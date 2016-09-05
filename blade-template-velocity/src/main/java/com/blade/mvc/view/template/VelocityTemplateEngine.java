package com.blade.mvc.view.template;

import java.io.Writer;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import com.blade.context.WebApplicationContext;
import com.blade.mvc.http.Request;
import com.blade.mvc.http.wrapper.Session;
import com.blade.mvc.view.ModelAndView;

public class VelocityTemplateEngine implements TemplateEngine {

	private VelocityEngine ve;
	private Properties config;
	private String templatePath = "/templates/";
	private String suffix = ".vm";
	
    /**
     * Constructor
     */
    public VelocityTemplateEngine() {
		this.config = new Properties();
		this.config.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
		this.config.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
		this.config.setProperty(RuntimeConstants.INPUT_ENCODING, "UTF-8");
		this.config.setProperty(RuntimeConstants.OUTPUT_ENCODING, "UTF-8");

		ve = new VelocityEngine();
		ve.init(config);
    }

	public VelocityTemplateEngine(String templatePath, String suffix){
		this.templatePath = templatePath;
		this.suffix = suffix;
	}

	public VelocityTemplateEngine(Properties config) {
		this.config = config;
		ve = new VelocityEngine();
		ve.init(config);
	}

    public VelocityTemplateEngine(VelocityEngine velocityEngine) {
        if (velocityEngine == null) {
            throw new IllegalArgumentException("velocityEngine must not be null");
        }
        this.ve = velocityEngine;
    }

	@Override
	public void render(ModelAndView modelAndView, Writer writer) throws TemplateException {
		
		Map<String, Object> modelMap = modelAndView.getModel();
		
		Request request = WebApplicationContext.request();
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
			String templateName = modelAndView.getView().endsWith(suffix) ? templatePath + modelAndView.getView() : templatePath + modelAndView.getView() + suffix;
			Template template = ve.getTemplate(templateName);
			VelocityContext context = new VelocityContext(modelMap);
			template.merge(context, writer);
		} catch (Exception e){
			throw new TemplateException(e);
		}
	}

	public VelocityEngine getVelocityEngine() {
		return this.ve;
	}

	public Properties getConfig() {
		return config;
	}

	public String getTemplatePath() {
		return templatePath;
	}

	public void setTemplatePath(String templatePath) {
		this.templatePath = templatePath;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}
}