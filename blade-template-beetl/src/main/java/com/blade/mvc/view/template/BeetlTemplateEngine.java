package com.blade.mvc.view.template;

import com.blade.context.ApplicationWebContext;
import com.blade.mvc.http.Request;
import com.blade.mvc.http.Response;
import com.blade.mvc.http.wrapper.Session;
import com.blade.mvc.view.ModelAndView;
import org.beetl.core.Configuration;
import org.beetl.ext.servlet.ServletGroupTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;
import java.util.Set;

public class BeetlTemplateEngine implements TemplateEngine {

	private Configuration cfg = null;

	public BeetlTemplateEngine(){
		try {
			this.cfg = Configuration.defaultConfiguration();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public BeetlTemplateEngine(Configuration cfg){
		this.cfg = cfg;
	}

	@Override
	public void render(ModelAndView modelAndView, Writer writer) throws TemplateException {
		
		Map<String, Object> modelMap = modelAndView.getModel();

		Request request = ApplicationWebContext.request();
		Response response = ApplicationWebContext.response();
		Session session = request.session();

		HttpServletRequest httpServletRequest = request.raw();
		HttpServletResponse httpServletResponse = response.raw();

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
			response.contentType("text/html;charset=UTF-8");
			ServletGroupTemplate.instance().render(modelAndView.getView(), httpServletRequest, httpServletResponse);
		} catch (Exception e){
			throw new TemplateException(e);
		}

		
	}

}