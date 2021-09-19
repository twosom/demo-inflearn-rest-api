package com.icloud.demoinflearnrestapi.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
public class EventControllerTests {


    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("정상적으로 이벤트를 생성하는 테스트")
    void createEvent() throws Exception {
        EventDto event = EventDto.builder()
                .name("Spring")
                .description("REST API Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.now())
                .closeEnrollmentDateTime(LocalDateTime.now().plusWeeks(1))
                .beginEventDateTime(LocalDateTime.now())
                .endEventDateTime(LocalDateTime.now().plusMonths(1))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역 D2 스타텁 팩토리")
                .build();

        mockMvc.perform(
                        post("/api/events/")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaTypes.HAL_JSON)
                                .content(objectMapper.writeValueAsString(event))
                )
                .andExpect(
                        status().isCreated()
                )
                .andDo(
                        print()
                )
                .andExpect(
                        jsonPath("id").exists()
                )
                .andExpect(
                        header().exists(HttpHeaders.LOCATION)
                )
                .andExpect(
                        header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE)
                )
                .andExpect(
                        jsonPath("free").value(false)
                )
                .andExpect(
                        jsonPath("offline").value(true)
                )
                .andExpect(
                        jsonPath("eventStatus").value(EventStatus.DRAFT.name())
                )
        ;

    }

    @Test
    @DisplayName("입력받을 수 없는 값을 사용한 경우에 에러가 발생하는 테스트")
    void createEvent_Bad_Request() throws Exception {
        Event event = Event.builder()
                .id(100)
                .name("Spring")
                .description("REST API Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.now())
                .closeEnrollmentDateTime(LocalDateTime.now().plusMonths(1))
                .beginEventDateTime(LocalDateTime.now())
                .endEventDateTime(LocalDateTime.now().plusWeeks(1))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역 D2 스타텁 팩토리")
                .free(true)
                .offline(false)
                .eventStatus(EventStatus.PUBLISHED)
                .build();

        mockMvc.perform(
                        post("/api/events/")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaTypes.HAL_JSON)
                                .content(objectMapper.writeValueAsString(event))
                )
                .andDo(
                        print()
                )
                .andExpect(
                        status().isBadRequest()
                )
        ;

    }
    

    @Test
    @DisplayName("입력 값이 비어있는 경우에 에러가 발생하는 테스트")
    void createEvent_Bad_Request_Empty_Input() throws Exception {
        EventDto eventDto = EventDto.builder()
                .build();

        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventDto))
                )
                .andDo(
                        print()
                )
                .andExpect(
                        status().isBadRequest()
                )
        ;

    }

    @Test
    @DisplayName("입력 값이 잘못된 경우에 에러가 발생하는 테스트")
    void createEvent_Bad_Request_Wrong_Input() throws Exception {
        EventDto eventDto = EventDto.builder()
                .name("Spring")
                .description("REST API Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.now().plusMonths(2))
                .closeEnrollmentDateTime(LocalDateTime.now().plusMonths(1))
                .beginEventDateTime(LocalDateTime.now().plusWeeks(2))
                .endEventDateTime(LocalDateTime.now().plusWeeks(1))
                .basePrice(10000)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역 D2 스타텁 팩토리")
                .build();

        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventDto))
                )
                .andDo(
                        print()
                )
                .andExpect(
                        status().isBadRequest()
                )
                .andExpect(
                        jsonPath("$[0].objectName").exists()
                )
                .andExpect(
                        jsonPath("$[0].defaultMessage").exists()
                )
                .andExpect(
                        jsonPath("$[0].code").exists()
                )
        ;

    }


}
