package ma.enset.KafkaSpringCloudStream.service;
import ma.enset.KafkaSpringCloudStream.entity.PageEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import java.util.function.Consumer;
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
}

/** we need to add the topic to recive mes  sages from it
 default chanel take the name of the method "pageEventConsumer-in-0"
 for changing it we add this propertie application.proprties
 spring.cloud.stream.bindings.pageEventConsumer-in-0=testTopic
 **/