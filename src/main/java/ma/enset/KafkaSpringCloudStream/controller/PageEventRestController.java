package ma.enset.KafkaSpringCloudStream.controller;
import lombok.AllArgsConstructor;
import ma.enset.KafkaSpringCloudStream.entity.PageEvent;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Windowed;
import org.apache.kafka.streams.state.*;
import org.springframework.cloud.stream.binder.kafka.streams.InteractiveQueryService;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
@RestController
@AllArgsConstructor
public class PageEventRestController {
    private StreamBridge streamBridge;

    private InteractiveQueryService interactiveQueryService;
    @GetMapping("/publish/{topic}/{name}")
    private PageEvent publish(@PathVariable("topic") String topic,@PathVariable("name") String name){
        PageEvent pageEvent = PageEvent.builder()
                .name(name)
                .user(Math.random()>0.5 ? "User1" : "User2")
                .date(new Date((long) (Math.random()*System.currentTimeMillis())))
                .duration(new Random().nextInt(9001))
                .build();
        streamBridge.send(topic,pageEvent);
        return pageEvent;
    }
    @GetMapping(path = "/analytics",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Map<String,Long>> analytics(){
        return Flux.interval(Duration.ofSeconds(1))
                .map(sequence -> {
                    Map<String,Long> stringLongMap = new HashMap<>();
                    ReadOnlyWindowStore<String, Long> queryableStore = interactiveQueryService.getQueryableStore("page-count", QueryableStoreTypes.windowStore());
                    Instant now = Instant.now();
                    Instant instant = now.minusMillis(5000);
                    KeyValueIterator<Windowed<String>,Long> keyValueIterator = queryableStore.fetchAll(instant,now);
                    while (keyValueIterator.hasNext()){
                        KeyValue<Windowed<String>,Long>  next = keyValueIterator.next();
                        stringLongMap.put(next.key.key(), next.value);
                    }
                    return stringLongMap;
                }).share();
    }
}