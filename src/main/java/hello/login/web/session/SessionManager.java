package hello.login.web.session;

import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 세션 관리
 */
@Component
public class SessionManager {

    // 생성한 세션 값과 매치되는 key
    public static final String SESSION_COOKIE_NAME = "mySessionId";

    // 서버의 세션 저장소
    private Map<String, Object> sessionStore = new ConcurrentHashMap<>();

    /**
     * 세션 생성
     * 클라이언트            vs          서버
     * mySessionId / UUID     UUID / Member
     * -> UUID (토큰값) 으로 연결됨
     */
    public void createSession(Object value, HttpServletResponse response) {

        // 1. 세션 id를 생성하고, 값을 세션에 저장
        String sessionId = UUID.randomUUID().toString(); // UUID: java가 제공하는 랜덤 값 추출 객체
        sessionStore.put(sessionId, value); // sessionId, Member

        // 2. 쿠키 생성
        Cookie mySessionCookie = new Cookie(SESSION_COOKIE_NAME, sessionId); // ("mySessionId", sessionId)

        // 3. sessionId로 응답 쿠키를 생성해서 클라이언트에 전달
        response.addCookie(mySessionCookie);
    }

    /**
     * 세션 조회
     */
    public Object getSession(HttpServletRequest request) {

        Cookie sessionCookie = findCookie(request, SESSION_COOKIE_NAME); // 클라이언트가 요청한 쿠키 하나 리턴

        if (sessionCookie == null) {
            return null;
        }

        // 3. 찾은 쿠키값이 존재한다면 서버의 세션 저장소에서 sessionId로 찾기
        return sessionStore.get(sessionCookie.getValue()); // member 객체 리턴
    }

    /**
     * 세션 만료
     */
    public void expire(HttpServletRequest request) {

        // 1. 클라이언트가 요청한 쿠키 가져오기
        Cookie sessionCookie = findCookie(request, SESSION_COOKIE_NAME);
        if (sessionCookie != null) {
            // 2. 찾은 쿠키값이 존재한다면 서버의 세션 저장소에서 sessionId로 찾아서 쿠키 지우기
            sessionStore.remove(sessionCookie.getValue());
        }
    }

    public Cookie findCookie(HttpServletRequest request, String cookieName) {

        // 1. 클라이언트 요청 헤더에 있는 쿠키 꺼내기
        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            return null;
        }

        // 2. 클라이언트가 요청한 쿠키 배열을 루프로 돌려
        // 쿠키의 이름과 서버에서 저장한 쿠키 이름이 일치하는지 확인
        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(cookieName))
                .findAny()
                .orElse(null);
    }

}
