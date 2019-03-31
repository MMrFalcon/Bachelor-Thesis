package Falcon

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@EnableJpaRepositories("Falcon.Repository")
@EntityScan("Falcon.Persist")
@SpringBootApplication
class SimpleForumExampleApplication {

	static void main(String[] args) {
		SpringApplication.run SimpleForumExampleApplication, args
	}
}
