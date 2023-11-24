package com.example.config;

import com.alibaba.nacos.client.naming.event.InstancesChangeEvent;
import com.alibaba.nacos.common.notify.Event;
import com.alibaba.nacos.common.notify.NotifyCenter;
import com.alibaba.nacos.common.notify.listener.Subscriber;
import com.alibaba.nacos.common.utils.JacksonUtils;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.properties.AbstractSwaggerUiConfigProperties;
import org.springdoc.core.properties.SwaggerUiConfigParameters;
import org.springdoc.core.properties.SwaggerUiConfigProperties;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.springdoc.core.utils.Constants.DEFAULT_API_DOCS_URL;
import static org.springframework.cloud.loadbalancer.core.CachingServiceInstanceListSupplier.SERVICE_INSTANCE_CACHE_NAME;

@Slf4j
@Component
public class NacosInstancesChangeEventListener extends Subscriber<InstancesChangeEvent> {

    private final String LB_SCHEME = "lb";

    private final RouteDefinitionLocator locator;

    @Resource
    private CacheManager defaultLoadBalancerCacheManager;

    private final SwaggerUiConfigProperties swaggerUiConfigProperties;

    private final SwaggerUiConfigParameters swaggerUiConfigParameters;

    public NacosInstancesChangeEventListener(RouteDefinitionLocator locator,
                                             SwaggerUiConfigProperties swaggerUiConfigProperties,
                                             SwaggerUiConfigParameters swaggerUiConfigParameters) {
        this.locator = locator;
        this.swaggerUiConfigProperties = swaggerUiConfigProperties;
        this.swaggerUiConfigParameters = swaggerUiConfigParameters;
    }

    @Override
    public void onEvent(InstancesChangeEvent event) {
        log.info("Spring Gateway 接收实例刷新事件：{}, 开始刷新缓存", JacksonUtils.toJson(event));
        Cache cache = defaultLoadBalancerCacheManager.getCache(SERVICE_INSTANCE_CACHE_NAME);
        if (cache != null) {
            cache.evict(event.getServiceName());
        }
        // 刷新group
        refreshGroup();
        log.info("Spring Gateway 实例刷新完成");
    }

    /**
     * 刷新swagger的group
     */
    public void refreshGroup() {
        // 初始化配置文件
        Set<AbstractSwaggerUiConfigProperties.SwaggerUrl> propertiesUrls = swaggerUiConfigProperties.getUrls();
        if (ObjectUtils.isEmpty(propertiesUrls)) {
            propertiesUrls = new HashSet<>();
        } else {
            // 如果有值则清除，使用从gateway获取到的路由加载
            propertiesUrls.clear();
        }

        // 初始化配置参数
        Set<AbstractSwaggerUiConfigProperties.SwaggerUrl> swaggerUrlSet = swaggerUiConfigParameters.getUrls();
        if (ObjectUtils.isEmpty(swaggerUrlSet)) {
            swaggerUrlSet = new HashSet<>();
        } else {
            swaggerUrlSet.clear();
        }

        // 获取网关路由
        List<RouteDefinition> definitions = locator.getRouteDefinitions().collectList().block();
        if (ObjectUtils.isEmpty(definitions)) {
            return;
        }

        List<AbstractSwaggerUiConfigProperties.SwaggerUrl> swaggerUrlList = definitions.stream()
                // 只处理在注册中心注册过的(lb://service)
                .filter(definition -> definition.getUri().getScheme().equals(LB_SCHEME))
                .map(definition -> {
                    // 生成SwaggerUrl配置，以微服务在注册中心中的名字当做组名、请求路径(我这里使用的是自动扫描生成的，所以直接用了这个，其它自定义的)
                    String authority = definition.getUri().getAuthority();
                    return new AbstractSwaggerUiConfigProperties.SwaggerUrl(authority, authority + DEFAULT_API_DOCS_URL, authority);
                })
                .toList();

        // 修改实际配置
        swaggerUrlSet.addAll(swaggerUrlList);
        // 修改配置文件
        propertiesUrls.addAll(swaggerUrlList);

        // 重置两个配置
        swaggerUiConfigParameters.setUrls(swaggerUrlSet);
        swaggerUiConfigProperties.setUrls(propertiesUrls);
        // 这里同时修改两个配置是因为初始化时会有校验，
        // 单独修改 propertiesUrls 仅第一次生效，
        // 单独修改swaggerUrlSet时url会自动变为 /v3/api-docs/{group}，
        // 所以两个都修改才能按修改内容加载
    }

    @PostConstruct
    public void registerToNotifyCenter() {
        // 注册监听事件
        NotifyCenter.registerSubscriber(this);
    }

    @Override
    public Class<? extends Event> subscribeType() {
        return InstancesChangeEvent.class;
    }
}