package com.example.controller;

import com.example.enums.SourceEnum;
import com.example.model.EnumModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * 枚举接口
 *
 * @author vains
 */
@RestController
@RequestMapping("/enum")
@Tag(name = "枚举入参接口", description = "提供以枚举作为入参的接口，展示SpringDoc自定义配置效果")
public class EnumController {

    @GetMapping("/test01/{source}")
    @Operation(summary = "url参数枚举", description = "将枚举当做url参数")
    public Mono<SourceEnum> test01(@PathVariable SourceEnum source) {
        return Mono.just(source);
    }

    @GetMapping("/test02")
    @Operation(summary = "查询参数枚举", description = "将枚举当做查询参数")
    public Mono<SourceEnum> test02(SourceEnum source) {
        return Mono.just(source);
    }

    @PostMapping(value = "/test03")
    @Operation(summary = "参数类包含枚举", description = "将枚举当做参数类的属性")
    public Mono<EnumModel> test03(@RequestBody EnumModel model) {
        return Mono.just(model);
    }

}