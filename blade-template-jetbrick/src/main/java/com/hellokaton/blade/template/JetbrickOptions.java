package com.hellokaton.blade.template;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class JetbrickOptions {

    private String templateRootDir = "templates";
    private String templateSuffix = ".html";
    private boolean reloadable = true;
    private Map<String, Object> extConfig;

}
