package com.example.controller;import io.swagger.v3.oas.annotations.Operation;import org.springframework.web.bind.annotation.GetMapping;import org.springframework.web.bind.annotation.RestController;import reactor.core.publisher.Mono;/** * 测试接口 * * @author vains */@RestControllerpublic class TestController {    @GetMapping("/test01")    @Operation(summary = "测试接口")    public Mono<String> test01() {        return Mono.just("test01");    }    @GetMapping("/test02")    @Operation(summary = "测试接口2")    public Mono<Integer> test01(Integer aInt) {        return Mono.just(aInt);    }}