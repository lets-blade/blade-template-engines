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
package com.hellokaton.blade.template;

import com.hellokaton.blade.exception.TemplateException;
import com.hellokaton.blade.mvc.WebContext;
import com.hellokaton.blade.mvc.http.Request;
import com.hellokaton.blade.mvc.http.Session;
import com.hellokaton.blade.mvc.ui.ModelAndView;
import com.hellokaton.blade.mvc.ui.template.TemplateEngine;
import jetbrick.template.*;
import jetbrick.template.resolver.GlobalResolver;
import lombok.Getter;
import lombok.Setter;

import java.io.Writer;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;

/**
 * JetbrickTemplateEngine
 *
 * @author <a href="mailto:hellokaton@gmail.com" target="_blank">hellokaton</a>
 * @since 0.2.1
 */
@Getter
@Setter
public class JetbrickTemplateEngine implements TemplateEngine {

    private JetEngine jetEngine;
    private Properties config = new Properties();

    public JetbrickTemplateEngine() {
        this(new JetbrickOptions());
    }

    public JetbrickTemplateEngine(JetbrickOptions options) {
        config.put(JetConfig.TEMPLATE_SUFFIX, options.getTemplateSuffix());
        Class<?> bootClass = WebContext.blade().bootClass();
        if (null != bootClass) {
            config.put(JetConfig.AUTOSCAN_PACKAGES, bootClass.getPackage().getName());
        }

        String classpathLoader = "jetbrick.template.loader.ClasspathResourceLoader";
        config.put(JetConfig.TEMPLATE_LOADERS, "$classpathLoader");
        config.put("$classpathLoader", classpathLoader);
        config.put("$classpathLoader.root", "/" + options.getTemplateRootDir() + "/");
        config.put("$classpathLoader.reloadable", options.isReloadable());

        if (null != options.getExtConfig() && !options.getExtConfig().isEmpty()) {
            config.putAll(options.getExtConfig());
        }
        jetEngine = JetEngine.create(config);
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
    public void render(ModelAndView modelAndView, Writer writer) {
        if (null == jetEngine) {
            this.jetEngine = JetEngine.create(config);
        }
        Map<String, Object> modelMap = modelAndView.getModel();

        Request request = WebContext.request();
        Session session = request.session();

        modelMap.putAll(request.attributes());

        if (null != session) {
            modelMap.putAll(session.attributes());
        }

        String suffix = config.getProperty(JetConfig.TEMPLATE_SUFFIX, ".html");

        JetContext context = new JetContext(modelMap.size());
        context.putAll(modelMap);

        String templateName = modelAndView.getView().endsWith(suffix) ? modelAndView.getView() : modelAndView.getView() + suffix;
        try {
            JetTemplate template = jetEngine.getTemplate(templateName);
            template.render(context, writer);
        } catch (Exception e) {
            throw new TemplateException(e);
        }
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

    public JetbrickTemplateEngine addConfig(String key, String value) {
        config.put(key, value);
        return this;
    }

    public JetbrickTemplateEngine registerMethods(Class<?> methodType) {
        GlobalResolver globalResolver = getGlobalResolver();
        globalResolver.registerMethods(methodType);
        return this;
    }

    public JetbrickTemplateEngine autoScan(String... packageNames) {
        GlobalResolver globalResolver = getGlobalResolver();
        globalResolver.scan(Arrays.asList(packageNames), true);
        return this;
    }

    public JetbrickTemplateEngine registerFunctions(Class<?> functionType) {
        GlobalResolver globalResolver = getGlobalResolver();
        globalResolver.registerFunctions(functionType);
        return this;
    }

    public JetbrickTemplateEngine registerTags(Class<?> tagType) {
        GlobalResolver globalResolver = getGlobalResolver();
        globalResolver.registerTags(tagType);
        return this;
    }

}