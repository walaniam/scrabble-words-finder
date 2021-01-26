package pl.walaniam.srabble;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@Slf4j
@SpringBootApplication
public class Main {
    
    public static void main(String[] args) {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(Main.class);
        builder.headless(false);
        ConfigurableApplicationContext context = builder.run(args);
        log.info("Loaded");
//        for (String beanName : appCtx.getBeanDefinitionNames()) {
//            System.out.println(beanName);
//        }
    }
}
