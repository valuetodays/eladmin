//
//package me.zhengjie.modules.security.config;
//
//import java.util.Map;
//import java.util.Set;
//
//import lombok.RequiredArgsConstructor;
//import me.zhengjie.modules.security.security.JwtAccessDeniedHandler;
//import me.zhengjie.modules.security.security.JwtAuthenticationEntryPoint;
//import me.zhengjie.modules.security.security.TokenConfigurer;
//import me.zhengjie.modules.security.security.TokenProvider;
//import me.zhengjie.modules.security.service.OnlineUserService;
//import me.zhengjie.utils.AnonTagUtils;
//import me.zhengjie.utils.enums.RequestMethodEnum;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.core.GrantedAuthorityDefaults;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.web.filter.CorsFilter;
//
///**
// * @author Zheng Jie
// */
//@Configuration
//@RequiredArgsConstructor
//@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
//public class SpringSecurityConfig {
//
//    @Inject
//    TokenProvider tokenProvider;
//    @Inject
//    CorsFilter corsFilter;
//    @Inject
//    JwtAuthenticationEntryPoint authenticationErrorHandler;
//    @Inject
//    JwtAccessDeniedHandler jwtAccessDeniedHandler;
//    @Inject
//    ApplicationContext applicationContext;
//    @Inject
//    SecurityProperties properties;
//    @Inject
//    OnlineUserService onlineUserService;
//
//    @Bean
//    GrantedAuthorityDefaults grantedAuthorityDefaults() {
//        // 去除 ROLE_ 前缀
//        return new GrantedAuthorityDefaults("");
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        // 密码加密方式
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    protected SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
//        // 获取匿名标记
//        Map<String, Set<String>> anonymousUrls = AnonTagUtils.getAnonymousUrl(applicationContext);
//        return httpSecurity
//                // 禁用 CSRF
//                .csrf().disable()
//                .addFilter(corsFilter)
//                // 授权异常
//                .exceptionHandling()
//                .authenticationEntryPoint(authenticationErrorHandler)
//                .accessDeniedHandler(jwtAccessDeniedHandler)
//                // 防止iframe 造成跨域
//                .and()
//                .headers()
//                .frameOptions()
//                .disable()
//                // 不创建会话
//                .and()
//                .sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
//                .authorizeRequests()
//                // 静态资源等等
//                .antMatchers(
//                        HttpMethod.GET,
//                        "/*.html",
//                        "/**/*.html",
//                        "/**/*.css",
//                        "/**/*.js",
//                        "/webSocket/**"
//                ).permitAll()
//                // swagger 文档
//                .antMatchers("/swagger-ui.html").permitAll()
//                .antMatchers("/swagger-resources/**").permitAll()
//                .antMatchers("/webjars/**").permitAll()
//                .antMatchers("/*/api-docs").permitAll()
//                // 文件
//                .antMatchers("/avatar/**").permitAll()
//                .antMatchers("/file/**").permitAll()
//                // 阿里巴巴 druid
//                .antMatchers("/druid/**").permitAll()
//                // 放行OPTIONS请求
//                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
//                // 自定义匿名访问所有url放行：允许匿名和带Token访问，细腻化到每个 Request 类型
//                // GET
//                .antMatchers(HttpMethod.GET, anonymousUrls.get(RequestMethodEnum.GET.getType()).toArray(new String[0])).permitAll()
//                // POST
//                .antMatchers(HttpMethod.POST, anonymousUrls.get(RequestMethodEnum.POST.getType()).toArray(new String[0])).permitAll()
//                // PUT
//                .antMatchers(HttpMethod.PUT, anonymousUrls.get(RequestMethodEnum.PUT.getType()).toArray(new String[0])).permitAll()
//                // PATCH
//                .antMatchers(HttpMethod.PATCH, anonymousUrls.get(RequestMethodEnum.PATCH.getType()).toArray(new String[0])).permitAll()
//                // DELETE
//                .antMatchers(HttpMethod.DELETE, anonymousUrls.get(RequestMethodEnum.DELETE.getType()).toArray(new String[0])).permitAll()
//                // 所有类型的接口都放行
//                .antMatchers(anonymousUrls.get(RequestMethodEnum.ALL.getType()).toArray(new String[0])).permitAll()
//                // 所有请求都需要认证
//                .anyRequest().authenticated()
//                .and().apply(securityConfigurerAdapter())
//                .and().build();
//    }
//
//    private TokenConfigurer securityConfigurerAdapter() {
//        return new TokenConfigurer(tokenProvider, properties, onlineUserService);
//    }
//}
