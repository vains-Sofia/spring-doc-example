package com.example.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 来源枚举
 *
 * @author vains
 */
@Getter
@AllArgsConstructor
public enum SourceEnum implements BasicEnum<Integer, SourceEnum> {

    /**
     * 1-web网站
     */
    WEB(1, "web网站"),

    /**
     * 2-APP应用
     */
    APP(2, "APP应用");

    /**
     * 来源代码
     */
    private final Integer value;

    /**
     * 来源名称
     */
    private final String source;

}