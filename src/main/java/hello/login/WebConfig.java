package hello.login;

import hello.login.web.argumentresolver.LoginMemberArgumentResolver;
import hello.login.web.filter.LogFilter;
import hello.login.web.filter.LoginCheckFilter;
import hello.login.web.interceptor.LogInterceptor;
import hello.login.web.interceptor.LoginCheckInterceptor;
import lombok.extern.java.Log;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.Filter;
import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // ArgumentResolver 를 활용한 커스텀 어노테이션 @Login 등록
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberArgumentResolver());
    }

    // 스프링 인터셉터 빈 등록
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LogInterceptor()) // LogInterceptor 빈으로 등록
                .order(1)   // 인터셉터 호출 순서
                .addPathPatterns("/**")     // 인터셉터 적용할 url
                .excludePathPatterns("/css/**", "/*.ico", "/error");   // 인터셉터 적용하지 않을 url

        registry.addInterceptor(new LoginCheckInterceptor())
                .order(2)
                .addPathPatterns("/**")
                .excludePathPatterns("/", "/members/add", "/login", "/logout",
                        "/css/**", "/*.ico", "/error");
    }

    // -----------------------------------------------------------------------------

    // 서블릿 Filter 빈 등록
    @Bean
    public FilterRegistrationBean logFilter() {
        FilterRegistrationBean<Filter> filterFilterRegistrationBean = new FilterRegistrationBean<>();

        // 등록할 필터를 지정한다.
        filterFilterRegistrationBean.setFilter(new LogFilter());

        // 필터는 체인으로 동작한다. 따라서 순서가 필요하다. 낮을 수록 먼저 동작
        filterFilterRegistrationBean.setOrder(1);

        // 필터를 적용할 URL 패턴을 지정한다. 한번에 여러 패턴을 지정할 수 있다.
        filterFilterRegistrationBean.addUrlPatterns("/*");

        return filterFilterRegistrationBean;
    }

    // @Bean
    public FilterRegistrationBean loginCheckFilter() {
        FilterRegistrationBean<Filter> filterFilterRegistrationBean = new FilterRegistrationBean<>();

        // 등록할 필터를 지정한다.
        filterFilterRegistrationBean.setFilter(new LoginCheckFilter());

        // 필터는 체인으로 동작한다. 따라서 순서가 필요하다. 낮을 수록 먼저 동작
        filterFilterRegistrationBean.setOrder(2);

        // 필터를 적용할 URL 패턴을 지정한다. 한번에 여러 패턴을 지정할 수 있다.
        filterFilterRegistrationBean.addUrlPatterns("/*");

        return filterFilterRegistrationBean;
    }
}
