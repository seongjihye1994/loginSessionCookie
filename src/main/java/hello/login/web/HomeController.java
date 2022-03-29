package hello.login.web;

import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import hello.login.web.session.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberRepository memberRepository;
    private final SessionManager sessionManager;

    //    @GetMapping("/")
    public String home() {
        return "home";
    }

    /**
     * 쿠키를 확인해 로그인 한 사용자와 하지않은 사용자에게 다른 홈화면 보여주기
     *
     * 스프링이 제공하는 @CookieValue 를 사용해서 클라이언트의 요청 헤더에 있는 쿠키를 꺼내올 수 있다.
     * (흔히 알고있는 HttpServletRequest 처럼 다른 방법도 많다.)
     * 로그인을 하지 않은 사용자도 '/' url을 요청할 수 있도록 required = false 처리
     *
     * @param memberId -> 쿠키값 (쿠키값은 원래 String 이지만, 스프링이 자동으로 타입 컨버터를 해준다.)
     * @param model
     * @return
     */
    // @GetMapping("/")
    public String homeLogin(@CookieValue(name = "memberId", required = false) Long memberId, Model model) {

        // http 요청 헤더에 쿠키가 없다면 로그인 안 한 사용자 이므로 home 으로 보내기
        if (memberId == null) {
            return "home";
        }

        // http 요청 헤더에 쿠키가 있는 경우
        Member loginMember = memberRepository.findById(memberId);

        // 조회 결과가 없는 경우 home 으로
        if (loginMember == null) {
            return "home";
        }

        // 조회 결과가 있는 경우 loginHome 으로
        model.addAttribute("member", loginMember);
        return "loginHome";
    }

    // @GetMapping("/")
    public String homeLoginV2(HttpServletRequest request, Model model) {

        // 세션 관리자에 저장된 회원 정보 조회
        Member member = (Member) sessionManager.getSession(request);

        // 조회 결과가 없는 경우 home 으로
        if (member == null) {
            return "home";
        }

        // 조회 결과가 있는 경우 loginHome 으로
        model.addAttribute("member", member);
        return "loginHome";
    }

    // @GetMapping("/")
    public String homeLoginV3(HttpServletRequest request, Model model) {

        // 메인 화면으로 접속 시 getSession(true)로 해버리면 로그인 하지 않은 사용자도
        // 세션을 생성해 가지게 되므로 getSession(false)로 설정한다.
        HttpSession session = request.getSession(false);
        if (session == null) {
            return "home";
        }

        Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);

        // 세션에 회원 데이터가 없으면 home 으로
        if (loginMember == null) {
            return "home";
        }

        // 세션이 유지되면 로그인으로 이동
        model.addAttribute("member", loginMember);
        return "loginHome";
    }

    @GetMapping("/")
    public String homeLoginV3Spring(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember, Model model) {

        // 세션에 회원 데이터가 없으면 home 으로
        if (loginMember == null) {
            return "home";
        }

        // 세션이 유지되면 로그인으로 이동
        model.addAttribute("member", loginMember);
        return "loginHome";
    }
}
