package hello.login;

import hello.login.web.filter.LogFilter;
import hello.login.web.filter.LoginCheckFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;

@Configuration
public class WebConfig {

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

    @Bean
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
