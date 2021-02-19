package pl.walaniam.srabble;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Optional;

@Slf4j
@SpringBootApplication
public class Main {

    static {
        String os = System.getProperty("os.name");
        log.info("Running on: " + os);
        if (!Optional.ofNullable(os).map(String::toLowerCase).orElse("").contains("windows")) {
            System.setProperty("java.awt.headless", "true");
        }
    }
    
    public static void main(String[] args) {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(Main.class);
        builder.headless(false);
        ConfigurableApplicationContext context = builder.run(args);
        log.info("Loaded {}", context);
    }
}
