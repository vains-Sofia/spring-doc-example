package com.example.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.Scopes;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring Doc OpenApi Java代码配置
 *
 * @author vains
 */
@Configuration
@ConditionalOnProperty(name = "custom.config-type", havingValue = "bean")
public class SpringDocBeanConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        // 基础信息
        Info info = new Info()
                .title("example-api-java")
                .version("0.0.1")
                .description("这是一个使用SpringDoc生成的在线文档.")
                .termsOfService("http://127.0.0.1:8080/example/test01")
                .license(new License().name("Apache 2.0").url("http://127.0.0.1:8080/example/test01"));

        // 组件
        Components components = new Components();
        // 安全认证组件
        SecurityScheme securityScheme = new SecurityScheme();

        // 创建一个oauth认证流程
        OAuthFlows oAuthFlows = new OAuthFlows();
        // 设置OAuth2流程中认证服务的基本信息
        OAuthFlow oAuthFlow = new OAuthFlow()
                // 授权申请地址
                .authorizationUrl("http://kwqqr48rgo.cdhttp.cn/oauth2/authorize")
                // 获取token地址
                .tokenUrl("http://kwqqr48rgo.cdhttp.cn/oauth2/token")
                .scopes(new Scopes()
                        .addString("openid", "OpenId登录")
                        .addString("profile", "获取用户信息")
                        .addString("message.read", "读")
                        .addString("message.write", "写")
        );
        // 使用授权码模式
        oAuthFlows.authorizationCode(oAuthFlow);

        // OAuth2流程
        securityScheme.flows(oAuthFlows)
                .type(SecurityScheme.Type.OAUTH2);

        // 安全认证名
        String securityName = "Authenticate";
        // 将认证配置加入组件中
        components.addSecuritySchemes(securityName, securityScheme);

        SecurityRequirement securityRequirement = new SecurityRequirement();
        // 将安全认证和swagger-ui关联起来
        securityRequirement.addList(securityName);
        return new OpenAPI()
                // 基础描述信息
                .info(info)
                // 添加OAuth2认证流程组件
                .components(components)
                // 添加请求时携带OAuth2规范的请求头(通过OAuth2流程获取token后发请求时会自动携带Authorization请求头)
                .addSecurityItem(securityRequirement);
    }

}