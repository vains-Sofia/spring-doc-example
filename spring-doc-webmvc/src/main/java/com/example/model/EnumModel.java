package com.example.model;

import com.example.enums.SourceEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 枚举属性类
 *
 * @author vains
 */
@Data
@Schema(description = "包含枚举属性的类")
public class EnumModel {

    @Schema(title = "名字")
    private String name;

    @Schema(title = "来源")
    private SourceEnum source;

}