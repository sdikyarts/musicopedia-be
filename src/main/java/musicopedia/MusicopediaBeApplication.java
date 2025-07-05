package musicopedia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class MusicopediaBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(MusicopediaBeApplication.class, args);
    }

}
