package hello.login.domain.member;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.*;

@Slf4j
@Repository
public class MemberRepository {

    private static Map<Long, Member> store = new HashMap<>(); // static 사용
    private static long sequence = 0L; // static 사용

    public Member save(Member member) {
        member.setId(++sequence);
        log.info("save member={}", member);
        store.put(member.getId(), member);
        return member;
    }

    public Member findById(Long id) {
        return store.get(id); // Map에 key 던져서 value 받기
    }

    /**
     *
     * Optional<T>는 null이 올 수 있는 값을 감싸는 Wrapper 클래스로,
     * 참조하더라도 NPE가 발생하지 않도록 도와준다.
     *
     *     public Member findByLoginId(String loginId) {
     *         List<Member> all = findAll();
     *         for (Member m : all) {
     *             if (m.getLoginId().equals(loginId)) {
     *                 return m;
     *             }
     *         }
     *         return null;
     *     }
     *  와 동일한 로직
     */
    public Optional<Member> findByLoginId(String loginId) {
        /*List<Member> all = findAll();
        for (Member m : all) {
            if (m.getLoginId().equals(loginId)) {
                return Optional.of(m);
            }
        }
        return Optional.empty();*/

        /**
         * 람다 사용 (list를 stream()으로 변환 가능) 해서 코드를 줄일 수 있다.
         * findAll()한 값에 stream() 으로 루프를 돌려
         * filter를 건다.
         * filter는 sql의 where 절 처럼 조건으로 거를 수 있다.
         * .findFirst()는 루프를 돌리면서 조건에 먼저 부합하는 가장 처음 나오는
         * 값을 리턴한다.
         */
        return findAll().stream().filter(m -> m.getLoginId().equals(loginId)).findFirst();
    }

    public void clearStore() {
        store.clear();
    }

    public List<Member> findAll() {
        return new ArrayList<>(store.values()); // Map에 values()로 key에 매칭되는 value들을 list로 받음
    }

}
