package com.icloud.demoinflearnrestapi.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest
public class EventControllerTests {


    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    WebApplicationContext ctx;

    @BeforeEach
    void beforeEach() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx)
                .addFilter(new CharacterEncodingFilter(StandardCharsets.UTF_8.name(), true))
                .alwaysDo(print())
                .build();
    }

    @MockBean
    EventRepository eventRepository;


    @Test
    void createEvent() throws Exception {
        Event event = Event.builder()
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
                .build();

        event.setId(10);
        when(eventRepository.save(event)).thenReturn(event);

        mockMvc.perform(
                        post("/api/events/")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaTypes.HAL_JSON)
                                .content(objectMapper.writeValueAsString(event))
                )
                .andExpect(
                        status().isCreated()
                )
                .andDo(print())
                .andExpect(jsonPath("id").exists())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, "application/hal+json;charset=UTF-8"));
    }

}
