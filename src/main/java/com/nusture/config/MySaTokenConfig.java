package com.nusture.config;

import cn.dev33.satoken.interceptor.SaRouteInterceptor;
import cn.dev33.satoken.jwt.StpLogicJwtForStateless;
import cn.dev33.satoken.jwt.StpLogicJwtForStyle;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpLogic;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MySaTokenConfig implements WebMvcConfigurer {
    //全局路由鉴权
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SaRouteInterceptor((req, res, handler) -> {
            SaRouter.match("/**").notMatch("/doLogin","/isLogin","/doRegister","/getUsername", "/**/*.js", "/**/*.css").check(r -> StpUtil.checkLogin());
        })).addPathPatterns("/**");
    }
    //整合 jwt (Stateless模式)
    @Bean
    public StpLogic getStpLogicJwt() {
        return new StpLogicJwtForStateless();
    }
}
