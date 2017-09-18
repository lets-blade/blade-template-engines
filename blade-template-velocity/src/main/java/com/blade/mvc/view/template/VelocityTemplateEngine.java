package com.blade.mvc.view.template;

import com.blade.exception.TemplateException;
import com.blade.mvc.WebContext;
import com.blade.mvc.http.Request;
import com.blade.mvc.http.Session;
import com.blade.mvc.ui.ModelAndView;
import com.blade.mvc.ui.template.TemplateEngine;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import java.io.Writer;
import java.util.Map;
import java.util.Properties;

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

    public VelocityTemplateEngine(String templatePath, String suffix) {
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
    public void render(ModelAndView modelAndView, Writer writer) {

        Map<String, Object> modelMap = modelAndView.getModel();

        Request request = WebContext.request();
        Session session = request.session();

        modelMap.putAll(request.attributes());

        if (null != session) {
            modelMap.putAll(session.attributes());
        }

        try {
            String templateName = modelAndView.getView().endsWith(suffix) ? templatePath + modelAndView.getView() : templatePath + modelAndView.getView() + suffix;
            Template template = ve.getTemplate(templateName);
            VelocityContext context = new VelocityContext(modelMap);
            template.merge(context, writer);
        } catch (Exception e) {
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