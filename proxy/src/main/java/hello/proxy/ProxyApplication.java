package hello.proxy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import hello.proxy.config.AppV1Config;
import hello.proxy.config.AppV2Config;

@Import({AppV1Config.class, AppV2Config.class}) // 클래스를 스프링 빈으로 등록된다
@SpringBootApplication(scanBasePackages = "hello.proxy.app") //주의, app 하위만 컴포넌트 스캔 대상으로 지정해둠
public class ProxyApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProxyApplication.class, args);
	}

}
