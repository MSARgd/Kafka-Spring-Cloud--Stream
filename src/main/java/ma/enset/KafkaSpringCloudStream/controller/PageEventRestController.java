package ma.enset.KafkaSpringCloudStream.controller;

import lombok.AllArgsConstructor;
import ma.enset.KafkaSpringCloudStream.entity.PageEvent;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import java.util.Date;
import java.util.Random;
@RestController
@AllArgsConstructor
public class PageEventRestController {
    private StreamBridge streamBridge;
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
}