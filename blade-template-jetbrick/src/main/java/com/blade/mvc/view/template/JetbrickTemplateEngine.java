/**
 * Copyright (c) 2016, biezhi 王爵 (biezhi.me@gmail.com)
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.blade.mvc.view.template;

import com.blade.http.Request;
import com.blade.http.Response;
import com.blade.http.Session;
import com.blade.kit.BladeKit;
import com.blade.mvc.WebContext;
import com.blade.mvc.ui.ModelAndView;
import com.blade.mvc.ui.template.TemplateEngine;
import jetbrick.template.*;
import jetbrick.template.resolver.GlobalResolver;

import java.io.Writer;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * JetbrickTemplateEngine
 *
 * @author <a href="mailto:biezhi.me@gmail.com" target="_blank">biezhi</a>
 * @since 1.0
 */
public class JetbrickTemplateEngine implements TemplateEngine {

    private JetEngine jetEngine;
    private Properties config = new Properties();
    private String suffix = ".html";

    public JetbrickTemplateEngine() {
        config.put(JetConfig.TEMPLATE_SUFFIX, suffix);
//        if(null != basePackage && !"".equals(basePackage)){
//            config.put(JetConfig.AUTOSCAN_PACKAGES, basePackage);
//        }
        String $classpathLoader = "jetbrick.template.loader.ClasspathResourceLoader";
        config.put(JetConfig.TEMPLATE_LOADERS, "$classpathLoader");
        config.put("$classpathLoader", $classpathLoader);
        config.put("$classpathLoader.root", "/templates/");
        config.put("$classpathLoader.reloadable", "true");
    }

    public JetbrickTemplateEngine(Properties config) {
        this.config = config;
        jetEngine = JetEngine.create(config);
    }

    public JetbrickTemplateEngine(String conf) {
        jetEngine = JetEngine.create(conf);
    }

    public JetbrickTemplateEngine(JetEngine jetEngine) {
        if (null == jetEngine) {
            throw new IllegalArgumentException("jetEngine must not be null");
        }
        this.jetEngine = jetEngine;
    }

    @Override
    public void render(ModelAndView modelAndView, Writer writer) throws TemplateException {
        if (null == jetEngine) {
            this.jetEngine = JetEngine.create(config);
        }
        Map<String, Object> modelMap = modelAndView.getModel();

        Request request = WebContext.request();
        Response response = WebContext.response();
        Session session = request.session();
        Set<String> attrs = request.attributes();
        if (BladeKit.isNotEmpty(attrs)) {
            attrs.forEach(attr -> modelMap.put(attr, request.attribute(attr)));
        }

        Set<String> session_attrs = session.attributes();
        if (BladeKit.isNotEmpty(session_attrs)) {
            session_attrs.forEach(attr -> modelMap.put(attr, request.attribute(attr)));
        }

        JetContext context = new JetContext(modelMap.size());
        context.putAll(modelMap);

        String templateName = modelAndView.getView().endsWith(suffix) ? modelAndView.getView() : modelAndView.getView() + suffix;
        JetTemplate template = jetEngine.getTemplate(templateName);
        template.render(context, writer);
    }

    public JetEngine getJetEngine() {
        return jetEngine;
    }

    public void setJetEngine(JetEngine jetEngine) {
        this.jetEngine = jetEngine;
    }

    public JetGlobalContext getGlobalContext() {
        if (null == jetEngine) {
            this.jetEngine = JetEngine.create(config);
        }
        return jetEngine.getGlobalContext();
    }

    public GlobalResolver getGlobalResolver() {
        if (null == jetEngine) {
            this.jetEngine = JetEngine.create(config);
        }
        return jetEngine.getGlobalResolver();
    }

    public Properties getConfig() {
        return config;
    }

    public TemplateEngine addConfig(String key, String value) {
        config.put(key, value);
        return this;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }
}