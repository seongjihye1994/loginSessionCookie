package hello.login.web.argumentresolver;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER) // 어노테이션을 작성할 곳, default 값은 모든 대상, 만약 @Target(ElementType.FIELD)로 지정해주면, 필드에만 어노테이션을 달 수 있음.
@Retention(RetentionPolicy.RUNTIME) // 어노테이션의 지속 시간 설정, RUNTIME: 런타임시에도 .class 파일에 존재
public @interface Login {
}
