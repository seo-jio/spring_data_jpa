package study.datajpa;

import org.hibernate.annotations.BatchSize;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;
import java.util.UUID;

@EnableJpaAuditing
@SpringBootApplication
public class DataJpaApplication {

	public static void main(String[] args) {
		SpringApplication.run(DataJpaApplication.class, args);
	}

	@Bean
	public AuditorAware<String> auditorProvider(){
		//인터페이스를 구현할 때 구현해야 할 메소드가 하나면 밑에와 같이 람다식으로 구현할 수 있다.
		return () -> Optional.of(UUID.randomUUID().toString());
	}
}
