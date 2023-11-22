package com.example.config.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.OAuthScope;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

/**
 * Spring Doc OpenApi 注解配置
 *
 * @author vains
 */
@Configuration
@OpenAPIDefinition(
        info = @Info(
                // 标题
                title = "${custom.info.title}",
                // 版本
                version = "${custom.info.version}",
                // 描述
                description = "${custom.info.description}",
                // 首页
                termsOfService = "${custom.info.termsOfService}",
                // license
                license = @License(
                        name = "${custom.license.name}",
                        // license 地址
                        url = "http://127.0.0.1:8080/example/test01"
                )
        ),
        // 这里的名字是引用下边 @SecurityScheme 注解中指定的名字，指定后发起请求时会在请求头中按照OAuth2的规范添加token
        security = @SecurityRequirement(name = "${custom.security.name}")
)
@SecuritySchemes({@SecurityScheme(
        // 指定 SecurityScheme 的名称(OpenAPIDefinition注解中的security属性中会引用该名称)
        name = "${custom.security.name}",
        // 指定认证类型为oauth2
        type = SecuritySchemeType.OAUTH2,
        // 设置认证流程
        flows = @OAuthFlows(
                // 设置授权码模式
                authorizationCode = @OAuthFlow(
                        // 获取token地址
                        tokenUrl = "${custom.security.token-url}",
                        // 授权申请地址
                        authorizationUrl = "${custom.security.authorization-url}",
                        // oauth2的申请的scope(需要在OAuth2客户端中存在)
                        scopes = {
                                @OAuthScope(name = "openid", description = "OpenId登录"),
                                @OAuthScope(name = "profile", description = "获取用户信息"),
                                @OAuthScope(name = "message.read", description = "读"),
                                @OAuthScope(name = "message.write", description = "写")
                        }
                )
        )
)})
@ConditionalOnProperty(name = "custom.config-type", havingValue = "anno")
public class SpringDocAnnoConfig {
}