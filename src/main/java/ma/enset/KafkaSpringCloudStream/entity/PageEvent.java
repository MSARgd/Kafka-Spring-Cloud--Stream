package ma.enset.KafkaSpringCloudStream.entity;
import lombok.*;

import java.util.Date;

//@Entity
@Data @NoArgsConstructor @AllArgsConstructor @Builder @ToString
public class PageEvent {
    private Long id;
    private String name;
    private String user;
    private Date date;
    private long duration;

}
