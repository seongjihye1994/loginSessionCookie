package hello.login.web.session;

import hello.login.domain.member.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;

public class SessionManagerTest {

    SessionManager sessionManager = new SessionManager();

    @Test
    void sessionTest() {

        /** 세션 생성 후 클라이언트에 응답 */
        MockHttpServletResponse response = new MockHttpServletResponse();
        // HttpServletResponse 는 스프링이 아닌 서블릿 컨테이너가 제공하는 객체
        // 그래서 스프링 컨테이너에서 HttpServletResponse 를 테스트하기 위해서 스프링이 MockHttpServletResponse 를 제공한다.
        Member member = new Member();
        sessionManager.createSession(member, response);

        /** 클라이언트가 서버에 요청 시 쿠키를 헤더에 넣어 요청 */
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(response.getCookies()); // mySessionId=12312-32435321...

        /** 클라이언트가 요청시 전송한 세션 조회 */
        Object result = sessionManager.getSession(request);
        assertThat(result).isEqualTo(member);

        /** 세션 만료 */
        sessionManager.expire(request);
        Object expired = sessionManager.getSession(request);
        assertThat(expired).isNull();

    }
}
