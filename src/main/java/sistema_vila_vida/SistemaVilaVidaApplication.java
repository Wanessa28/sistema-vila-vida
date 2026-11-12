
package sistema_vila_vida;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SistemaVilaVidaApplication {

    public static void main(String[] args) {
        SpringApplication.run(SistemaVilaVidaApplication.class, args);
    }
}
