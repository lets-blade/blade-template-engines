/**
 * Copyright (c) 2016, biezhi 王爵 (biezhi.me@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.blade.view.template;

import java.io.Writer;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;

import com.blade.context.ApplicationWebContext;
import com.blade.view.ModelAndView;
import com.blade.view.template.TemplateEngine;
import com.blade.web.http.Request;
import com.blade.web.http.wrapper.Session;

import jetbrick.template.JetContext;
import jetbrick.template.JetEngine;
import jetbrick.template.JetTemplate;
import jetbrick.template.TemplateException;
import jetbrick.template.web.JetWebEngine;

/**
 * JetbrickTemplateEngine
 * 
 * @author	<a href="mailto:biezhi.me@gmail.com" target="_blank">biezhi</a>
 * @since	1.0
 */
public class JetbrickTemplateEngine implements TemplateEngine {

	private JetEngine jetEngine;

	public JetbrickTemplateEngine() {
		jetEngine = JetEngine.create();
	}
	
	public JetbrickTemplateEngine(ServletContext servletContext) {
		jetEngine = JetWebEngine.create(servletContext);
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
	
	public JetEngine getJetEngine() {
		return jetEngine;
	}

	@Override
	public void render(ModelAndView modelAndView, Writer writer) throws TemplateException {
		JetTemplate template = jetEngine.getTemplate(modelAndView.getView());
		Map<String, Object> modelMap = modelAndView.getModel();
		JetContext context = new JetContext(modelMap.size());
		
		Request request = ApplicationWebContext.request();
		Session session = request.session();

		Set<String> attrs = request.attributes();
		if (null != attrs && attrs.size() > 0) {
			for (String attr : attrs) {
				context.put(attr, request.attribute(attr));
			}
		}

		Set<String> session_attrs = session.attributes();
		if (null != session_attrs && session_attrs.size() > 0) {
			for (String attr : session_attrs) {
				context.put(attr, session.attribute(attr));
			}
		}

		context.putAll(modelMap);
		template.render(context, writer);
	}

}