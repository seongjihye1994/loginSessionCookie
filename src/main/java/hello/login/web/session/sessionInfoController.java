package hello.login.web.session;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;

@Slf4j
@RestController
public class sessionInfoController {

    @GetMapping("/session-info")
    public String sessionInfo(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return "세션이 없습니다.";
        }

        // 세션 데이터 출력
        session.getAttributeNames().asIterator()
                .forEachRemaining(name -> log.info("session name={}, value={}", name, session.getAttribute(name)));

        log.info("sessionId={}", session.getId());
        log.info("getMaxInactiveInterval={}", session.getMaxInactiveInterval());
        log.info("getCreationTime={}", new Date(session.getCreationTime()));
        log.info("getLastAccessedTime={}", new Date(session.getLastAccessedTime())); // 세션에 사용자가 마지막으로 접근한 시간
        log.info("isNew={}", session.isNew()); // 기존에 있는 세션인지, 새로 발급받은 세션인지 여부
        /**
         * session name=loginMember, value=Member(id=1, loginId=test, name=테스터, password=test!)
         * sessionId=BA2C82064D4D651C4C744B6ED19965E6
         * getMaxInactiveInterval=1800
         * getCreationTime=Tue Mar 29 15:51:20 KST 2022
         * getLastAccessedTime=Tue Mar 29 15:51:21 KST 2022
         * isNew=false
         */

        return "세션 출력";
    }

}
