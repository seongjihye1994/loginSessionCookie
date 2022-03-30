package hello.login.web.filter;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;

@Slf4j
public class LogFilter implements Filter {

    /**
     * 필터 초기화 메서드, 서블릿 컨테이너가 생성될 때 호출
     * @param filterConfig
     * @throws ServletException
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("log filter init");
    }

    /**
     * 고객의 요청이 올 때 마다 해당 메서드가 호출됨, 필터의 로직을 구현
     * @param request
     * @param response
     * @param chain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("log filter doFilter");

        /**
         * 서블릿이 설계될 때 HTTP 요청 이외의 다른 요청도 사용할 수 있도록
         * 설계되었기 때문에 HttpServletRequest 가 아닌 ServletRequest 가 파라미터로 전달된다.
         * 그래서 Http 요청에 대한 서블릿을 사용하기 위해서는 HttpServletRequest 로 다운캐스팅 해야한다.
         * -> 참고로 HttpServletRequest 의 부모가 ServletRequest 이다.
         */
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();// 모든 사용자의 URI 정보

        String uuid = UUID.randomUUID().toString(); // 요청 사용자를 구분하기 위한 값

        try {
            log.info("REQUEST [{}][{}]", uuid, requestURI);

            /** 다음 필터가 있으면 필터를 호출하고, 필터가 없으면 서블릿을 호출한다.
             * 만약 이 로직을 호출하지 않으면 다음 단계로 진행되지 않는다.
             * -> 다음 단계 : 디스패처 서블릿 -> 컨트롤러
             * */
            chain.doFilter(request, response);
        } catch (Exception e) {
            throw e;
        } finally {
            log.info("RESPONSE [{}][{}]", uuid, requestURI);
        }
    }

    /**
     * 필터 종료 메서드, 서블릿 컨테이너가 종료될 때 호출
     */
    @Override
    public void destroy() {
        log.info("log filter destroy");
    }
}
