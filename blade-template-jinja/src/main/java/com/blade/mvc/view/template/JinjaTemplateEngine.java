package com.blade.mvc.view.template;

import com.blade.mvc.view.ModelAndView;
import com.hubspot.jinjava.Jinjava;

import java.io.Writer;

/**
 * Created by biezhi on 2017/2/21.
 */
public class JinjaTemplateEngine implements TemplateEngine {

    private Jinjava jinjava = new Jinjava();

    @Override
    public void render(ModelAndView modelAndView, Writer writer) throws TemplateException {

    }
}
