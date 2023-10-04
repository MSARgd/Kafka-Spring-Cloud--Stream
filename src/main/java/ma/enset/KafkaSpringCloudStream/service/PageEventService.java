package ma.enset.KafkaSpringCloudStream.service;
import ma.enset.KafkaSpringCloudStream.entity.PageEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Random;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Service
public class PageEventService {
    @Bean
    public Consumer<PageEvent> pageEventConsumer(){

        return (input) -> {
            System.out.println("********************************");
            System.out.println(input.toString());
            System.out.println("********************************");
        };
    }
    @Bean
    public Supplier<PageEvent> pageEventSupplier(){

        return () -> new PageEvent(null,UUID.randomUUID().toString().substring(0,10).replaceAll("[^a-z]",""),
                Math.random()>0.5 ? "User1" : "User2",
                new Date((long) (Math.random()*System.currentTimeMillis())),
                new Random().nextInt(9001)
        );
    }
}

/** we need to add the topic to recive mes  sages from it
 default chanel take the name of the method "pageEventConsumer-in-0"
 for changing it we add this propertie application.proprties
 spring.cloud.stream.bindings.pageEventConsumer-in-0=testTopic
 **/