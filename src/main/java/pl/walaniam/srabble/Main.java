package pl.walaniam.srabble;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@ComponentScan
public class Main {
    
    public static void main(String[] args) {
        ApplicationContext appCtx = new AnnotationConfigApplicationContext(Main.class);
        log.info("Loaded {}", appCtx);
//        for (String beanName : appCtx.getBeanDefinitionNames()) {
//            System.out.println(beanName);
//        }
    }
}
