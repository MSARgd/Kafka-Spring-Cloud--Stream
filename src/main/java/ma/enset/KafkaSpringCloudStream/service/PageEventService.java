package ma.enset.KafkaSpringCloudStream.service;
import ma.enset.KafkaSpringCloudStream.entity.PageEvent;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Date;
import java.util.Random;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;
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

        return () -> new PageEvent(null, Math.random()>0.5 ? "Page1": "Page2",
                Math.random()>0.5 ? "User1" : "User2",
                new Date((long) (Math.random()*System.currentTimeMillis())),
                new Random().nextInt(9001)
        );
    }
    @Bean
    public Function<PageEvent,PageEvent> pageEventFunction(){
        return (input) -> {
          input.setDate(new Date());
          return input;
        };
    }
    /**
     * Kafka Stream
     */
    @Bean
    public Function<KStream<String,PageEvent>,KStream<String,Long>> kStreamFunction(){
        return (input)->{
            return input
                    .filter((s, pageEvent) -> pageEvent.getDuration()>100)
                    .map((s, pageEvent) -> new KeyValue<>(pageEvent.getName(),0L))
                    .groupBy((s, aLong) -> s, Grouped.with(Serdes.String(),Serdes.Long()))
                    .windowedBy(TimeWindows.of(Duration.ofSeconds(5)))
                    .count(Materialized.as("page-count"))
                    .toStream()
                    .map((k, v) -> new KeyValue<>(
                            "[ "+k.window().startTime().getEpochSecond()+" , "+k.window().endTime().getEpochSecond()+"]  =>"+
                            k.key(),v));

        };
    }
}

/** we need to add the topic to recive mes  sages from it
 default chanel take the name of the method "pageEventConsumer-in-0"
 for changing it we add this propertie application.proprties
 spring.cloud.stream.bindings.pageEventConsumer-in-0=testTopic
 **/