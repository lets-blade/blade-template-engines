package com.hellokaton.blade.template;

import com.hellokaton.blade.exception.TemplateException;
import com.hellokaton.blade.mvc.WebContext;
import com.hellokaton.blade.mvc.http.Request;
import com.hellokaton.blade.mvc.http.Session;
import com.hellokaton.blade.mvc.ui.ModelAndView;
import com.hellokaton.blade.mvc.ui.template.TemplateEngine;
import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.IOException;
import java.io.Writer;
import java.util.Locale;
import java.util.Map;

/**
 * Created by hongyb on 2017/12/24.
 */
public class FreeMarkerTemplateEngine implements TemplateEngine {
    private static final String DEFAULT_ENCODING = "UTF-8";
    private  String suffix =".ftl";
    private Configuration configuration = null;
    public FreeMarkerTemplateEngine() {
        configuration = new Configuration();
        configuration.setEncoding(Locale.CHINA, DEFAULT_ENCODING);
        configuration.setClassForTemplateLoading(FreeMarkerTemplateEngine.class, "/templates/");
    }

    public FreeMarkerTemplateEngine(Configuration configuration) {
        this.configuration = configuration;
    }
    public Configuration getConfiguration(){
        return this.configuration;
    }

    @Override
    public void render(ModelAndView modelAndView, Writer writer) throws TemplateException {
        String view = modelAndView.getView();
        Map<String, Object> model = modelAndView.getModel();
        Request request = WebContext.request();
        Session session = request.session();
        model.putAll(request.attributes());
        if(null != session){
            model.putAll(session.attributes());
        }
        try {
            Template template = configuration.getTemplate(view+ suffix);
            template.process(model,writer);
        } catch (IOException | freemarker.template.TemplateException e) {
            throw new TemplateException(e);
        }
    }

    public void setSuffix(String suffix){
        this.suffix = suffix;
    }
}