package com.icloud.demoinflearnrestapi.events;

import lombok.*;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Builder @AllArgsConstructor @NoArgsConstructor
@Getter @Setter @EqualsAndHashCode(of = "id")
public class Event {

    @Id @GeneratedValue
    private Integer id;
    private String name;
    private String description;
    private LocalDateTime beginEnrollmentDateTime;
    private LocalDateTime closeEnrollmentDateTime;
    private LocalDateTime beginEventDateTime;
    private LocalDateTime endEventDateTime;
    private String location;    // (optional) 이게 없다면 온라인 모임
    private int basePrice;  // (optional)
    private int maxPrice;  // (optional)
    private int limitOfEnrollment;
    private boolean offline;
    private boolean free;
    @Enumerated(EnumType.STRING)
    private EventStatus eventStatus = EventStatus.DRAFT;


    public void update() {
        // UPDATE FREE
        this.free = this.basePrice == 0 && this.maxPrice == 0;
        this.offline = StringUtils.hasText(this.location);
    }
}
