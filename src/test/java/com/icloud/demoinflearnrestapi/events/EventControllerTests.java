package com.icloud.demoinflearnrestapi.events;

import com.icloud.demoinflearnrestapi.common.BaseControllerTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.stream.IntStream;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class EventControllerTests extends BaseControllerTest {


    @Autowired
    ModelMapper modelMapper;


    @DisplayName("정상적으로 이벤트를 생성하는 테스트")
    @Test
    public void createEvent() throws Exception {
        EventDto event = EventDto.builder()
                .name("Spring")
                .description("REST API Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2018, 11, 23, 14, 21))
                .closeEnrollmentDateTime(LocalDateTime.of(2018, 11, 24, 14, 21))
                .beginEventDateTime(LocalDateTime.of(2018, 11, 25, 14, 21))
                .endEventDateTime(LocalDateTime.of(2018, 11, 26, 14, 21))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역 D2 스타텁 팩토리")
                .build();

        mockMvc.perform(post("/api/events/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8.name())
                        .accept(MediaTypes.HAL_JSON)
                        .content(objectMapper.writeValueAsString(event))
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("free").value(false))
                .andExpect(jsonPath("offline").value(true))
                .andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT.name()))
                .andDo(document("create-event",
                        //TODO 링크 문서화
                        links(
                                linkWithRel("self").description("자기 자신을 가리킵니다."),
                                linkWithRel("query-events").description("검색 쿼리를 나타냅니다."),
                                linkWithRel("update-event").description("업데이트 이벤트를 나타냅니다."),
                                linkWithRel("profile").description("해당 API 를 설명하는 문서 링크입니다.")
                        ),
                        //TODO 요청 헤더 문서화
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("요청 헤더의 ACCEPT 필드입니다."),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("요청 헤더의 CONTENT_TYPE 필드입니다.")
                        ),
                        //TODO 요청 필드 문서화
                        requestFields(
                                fieldWithPath("name").description("이벤트의 이름입니다."),
                                fieldWithPath("description").description("이벤트의 상세 설명입니다."),
                                fieldWithPath("beginEnrollmentDateTime").description("이벤트의 모집 시작 일시입니다."),
                                fieldWithPath("closeEnrollmentDateTime").description("이벤트의 모집 중지 일시입니다."),
                                fieldWithPath("beginEventDateTime").description("이벤트가 시작되는 일시입니다."),
                                fieldWithPath("endEventDateTime").description("이벤트가 종료되는 일시입니다."),
                                fieldWithPath("location").description("이벤트 위치입니다."),
                                fieldWithPath("basePrice").description("이벤트 기본 가격입니다."),
                                fieldWithPath("maxPrice").description("이벤트 최대 가격입니다."),
                                fieldWithPath("limitOfEnrollment").description("이벤트 모집제한 인원수입니다.")
                        ),
                        //TODO 응답 헤더 문서화
                        responseHeaders(
                                headerWithName(HttpHeaders.LOCATION).description("응답 헤더의 LOCATION 필드입니다."),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("응답 헤더의 CONTENT_TYPE 필드입니다.")
                        ),
                        //TODO 응답 필드 문서화
                        responseFields(
                                fieldWithPath("id").description("이벤트의 생성 ID 입니다."),
                                fieldWithPath("name").description("이벤트의 이름입니다."),
                                fieldWithPath("description").description("이벤트의 상세 설명입니다."),
                                fieldWithPath("beginEnrollmentDateTime").description("이벤트의 모집 시작 일시입니다."),
                                fieldWithPath("closeEnrollmentDateTime").description("이벤트의 모집 중지 일시입니다."),
                                fieldWithPath("beginEventDateTime").description("이벤트가 시작되는 일시입니다."),
                                fieldWithPath("endEventDateTime").description("이벤트가 종료되는 일시입니다."),
                                fieldWithPath("location").description("이벤트 위치입니다."),
                                fieldWithPath("basePrice").description("이벤트 기본 가격입니다."),
                                fieldWithPath("maxPrice").description("이벤트 최대 가격입니다."),
                                fieldWithPath("limitOfEnrollment").description("이벤트 모집제한 인원수입니다."),
                                fieldWithPath("offline").description("이벤트의 온/오프라인 여부입니다."),
                                fieldWithPath("free").description("이벤트의 유/무료 여부입니다."),
                                fieldWithPath("eventStatus").description("이벤트의 상태값입니다."),
                                //TODO _links 밑에 있는 데이터들은 무시
                                fieldWithPath("_links.*.*").ignored()
                        )

                ))
        ;
    }


    @Test
    public void createEvent_BadRequest() throws Exception {
        Event event = Event.builder()
                .id(100)
                .name("Spring")
                .description("REST API Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2018, 11, 23, 14, 21))
                .closeEnrollmentDateTime(LocalDateTime.of(2018, 11, 24, 14, 21))
                .beginEventDateTime(LocalDateTime.of(2018, 11, 25, 14, 21))
                .endEventDateTime(LocalDateTime.of(2018, 11, 26, 14, 21))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역 D2 스타텁 팩토리")
                .free(true)
                .offline(false)
                .eventStatus(EventStatus.PUBLISHED)
                .build();

        mockMvc.perform(post("/api/events/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8.name())
                        .accept(MediaTypes.HAL_JSON)
                        .content(objectMapper.writeValueAsString(event))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;
    }

    @Test
    void createEvent_Bad_Request_Empty_Input() throws Exception {
        EventDto eventDto = EventDto.builder().build();

        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8.name())
                        .content(objectMapper.writeValueAsString(eventDto))
                )
                .andExpect(status().isBadRequest());
    }


    @DisplayName("입력 값이 잘못된 경우에 에러가 발생하는 테스트")
    @Test
    void createEvent_Bad_Request_Wrong_Input() throws Exception {
        EventDto eventDto = EventDto.builder()
                .name("Spring")
                .description("REST API Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2018, 11, 26, 14, 21))
                .closeEnrollmentDateTime(LocalDateTime.of(2018, 11, 25, 14, 21))
                .beginEventDateTime(LocalDateTime.of(2018, 11, 24, 14, 21))
                .endEventDateTime(LocalDateTime.of(2018, 11, 23, 14, 21))
                .basePrice(10000)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역 D2 스타텁 팩토리")
                .build();

        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8.name())
                        .content(objectMapper.writeValueAsString(eventDto))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors[0].objectName").exists())
                .andExpect(jsonPath("errors[0].defaultMessage").exists())
                .andExpect(jsonPath("errors[0].code").exists())
                .andExpect(jsonPath("_links.index").exists())
        ;
    }

    @DisplayName("30개의 이벤트를 10개씩 두번째 페이지 조회하기")
    @Test
    void queryEvents() throws Exception {
        //TODO 30개를 만들고, 10개 사이즈로 두번째 페이지 조회하면 이전, 다음 페이지로 가는 링크가 있어야 한다.
        // 이벤트 이름순으로 정렬하기
        // page 관련 링크


        // GIVEN
        IntStream.range(0, 30)
                .forEach(this::generateEvent);
        // WHEN
        this.mockMvc.perform(get("/api/events")
                        .param("page", "1")
                        .param("size", "10")
                        .param("sort", "name,DESC")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("page").exists())
                .andExpect(jsonPath("_embedded.eventList[0]._links.self").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andDo(document(
                        "query-events",
                        links(
                                linkWithRel("first").description("이벤트 조회 페이지의 맨 처음으로 갑니다."),
                                linkWithRel("prev").description("현재 이벤트 조회 페이지의 바로 이전 페이지로 갑니다."),
                                linkWithRel("next").description("현재 이벤트 조회 페이지의 바로 다음 페이지로 갑니다."),
                                linkWithRel("self").description("현재 페이지를 나타냅니다."),
                                linkWithRel("last").description("이벤트 조회 페이지의 맨 마지막으로 갑니다."),
                                linkWithRel("profile").description("이벤트 조회 페이지의 API 문서로 갑니다.")
                        ),
                        requestParameters(
                                parameterWithName("page").description("이벤트를 조회할 페이지를 지정합니다."),
                                parameterWithName("size").description("이벤트를 조회할 갯수를 지정합니다."),
                                parameterWithName("sort").description("이벤트를 조회할 때 정렬할 조건을 지정합니다.")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("응답하는 콘텐츠 타입입니다.")
                        ),
                        responseFields(
                                fieldWithPath("_embedded.eventList[].id").description("이벤트의 생성 ID 입니다."),
                                fieldWithPath("_embedded.eventList[].name").description("이벤트의 이름입니다."),
                                fieldWithPath("_embedded.eventList[].description").description("이벤트의 상세 설명입니다."),
                                fieldWithPath("_embedded.eventList[].beginEnrollmentDateTime").description("이벤트의 모집 시작 일시입니다."),
                                fieldWithPath("_embedded.eventList[].closeEnrollmentDateTime").description("이벤트의 모집 중지 일시입니다."),
                                fieldWithPath("_embedded.eventList[].beginEventDateTime").description("이벤트가 시작되는 일시입니다."),
                                fieldWithPath("_embedded.eventList[].endEventDateTime").description("이벤트가 종료되는 일시입니다."),
                                fieldWithPath("_embedded.eventList[].location").description("이벤트 위치입니다."),
                                fieldWithPath("_embedded.eventList[].basePrice").description("이벤트 기본 가격입니다."),
                                fieldWithPath("_embedded.eventList[].maxPrice").description("이벤트 최대 가격입니다."),
                                fieldWithPath("_embedded.eventList[].limitOfEnrollment").description("이벤트 모집제한 인원수입니다."),
                                fieldWithPath("_embedded.eventList[].offline").description("이벤트의 온/오프라인 여부입니다."),
                                fieldWithPath("_embedded.eventList[].free").description("이벤트의 유/무료 여부입니다."),
                                fieldWithPath("_embedded.eventList[].eventStatus").description("이벤트의 상태값입니다."),
                                fieldWithPath("_embedded.eventList[]._links.self.href").description("해당 이벤트로 가는 링크입니다."),
                                fieldWithPath("_links.*.*").ignored(),
                                fieldWithPath("page.size").description("현재 조회된 이벤트 목록의 개수입니다."),
                                fieldWithPath("page.totalElements").description("총 이벤트의 개수입니다."),
                                fieldWithPath("page.totalPages").description("모든 페이지 수 입니다."),
                                fieldWithPath("page.number").description("현재 페이지입니다.")
                        )
                ))
        ;
    }

    @DisplayName("기존의 이벤트를 하나 조회하기")
    @Test
    void getEvent() throws Exception {
        // GIVEN
        var event = this.generateEvent(100);

        // WHEN & THEN
        this.mockMvc.perform(get("/api/events/{id}", event.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("name").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andDo(document(
                        "get-an-event",
                        links(
                                linkWithRel("self").description("자기 자신의 링크를 나타냅니다."),
                                linkWithRel("profile").description("이벤트 단건 조회 API 문서의 링크를 나타냅니다.")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("응답 타입을 나타냅니다.")
                        ),
                        responseFields(
                                fieldWithPath("id").description("이벤트의 생성 ID 입니다."),
                                fieldWithPath("name").description("이벤트의 이름입니다."),
                                fieldWithPath("description").description("이벤트의 상세 설명입니다."),
                                fieldWithPath("beginEnrollmentDateTime").description("이벤트의 모집 시작 일시입니다."),
                                fieldWithPath("closeEnrollmentDateTime").description("이벤트의 모집 중지 일시입니다."),
                                fieldWithPath("beginEventDateTime").description("이벤트가 시작되는 일시입니다."),
                                fieldWithPath("endEventDateTime").description("이벤트가 종료되는 일시입니다."),
                                fieldWithPath("location").description("이벤트 위치입니다."),
                                fieldWithPath("basePrice").description("이벤트 기본 가격입니다."),
                                fieldWithPath("maxPrice").description("이벤트 최대 가격입니다."),
                                fieldWithPath("limitOfEnrollment").description("이벤트 모집제한 인원수입니다."),
                                fieldWithPath("offline").description("이벤트의 온/오프라인 여부입니다."),
                                fieldWithPath("free").description("이벤트의 유/무료 여부입니다."),
                                fieldWithPath("eventStatus").description("이벤트의 상태값입니다."),
                                //TODO _links 밑에 있는 데이터들은 무시
                                fieldWithPath("_links.*.*").ignored()
                        )

                ))

        ;
    }

    @DisplayName("없는 이벤트를 조회했을 때 404 응답받기")
    @Test
    void getEvent404() throws Exception {
        // WHEN & THEN
        this.mockMvc.perform(get("/api/events/11883"))
                .andDo(print())
                .andExpect(status().isNotFound())
        ;

    }

    @DisplayName("이벤트를 정상적으로 수정하기")
    @Test
    void updateEvent() throws Exception {
        // GIVEN
        var event = this.generateEvent(200);

        var eventDto = this.modelMapper.map(event, EventDto.class);
        var eventName = "Updated Event";
        eventDto.setName(eventName);

        // WHEN & THEN
        this.mockMvc.perform(put("/api/events/{id}", event.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventDto))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value(eventName))
                .andExpect(jsonPath("_links.self").exists())
                .andDo(document(
                        "update-event",
                        links(
                                linkWithRel("self").description("자기 자신을 가리키는 링크"),
                                linkWithRel("profile").description("이벤트 수정 API 문서를 가리키는 링크")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("요청 타입을 나타내는 HTTP 헤더"),
                                headerWithName(HttpHeaders.CONTENT_LENGTH).description("요청 본문의 길이를 나타내는 HTTP 헤더")
                        ),
                        requestFields(
                                fieldWithPath("name").description("이벤트의 이름입니다."),
                                fieldWithPath("description").description("이벤트의 상세 설명입니다."),
                                fieldWithPath("beginEnrollmentDateTime").description("이벤트의 모집 시작 일시입니다."),
                                fieldWithPath("closeEnrollmentDateTime").description("이벤트의 모집 중지 일시입니다."),
                                fieldWithPath("beginEventDateTime").description("이벤트가 시작되는 일시입니다."),
                                fieldWithPath("endEventDateTime").description("이벤트가 종료되는 일시입니다."),
                                fieldWithPath("location").description("이벤트 위치입니다."),
                                fieldWithPath("basePrice").description("이벤트 기본 가격입니다."),
                                fieldWithPath("maxPrice").description("이벤트 최대 가격입니다."),
                                fieldWithPath("limitOfEnrollment").description("이벤트 모집제한 인원수입니다.")
                        )
                        ,
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("응답 타입을 나타내는 HTTP 헤더")
                        ),
                        responseFields(
                                fieldWithPath("id").description("수정된 이벤트의 생성 ID 입니다."),
                                fieldWithPath("name").description("수정된 이벤트의 이름입니다."),
                                fieldWithPath("description").description("수정된 이벤트의 상세 설명입니다."),
                                fieldWithPath("beginEnrollmentDateTime").description("수정된 이벤트의 모집 시작 일시입니다."),
                                fieldWithPath("closeEnrollmentDateTime").description("수정된 이벤트의 모집 중지 일시입니다."),
                                fieldWithPath("beginEventDateTime").description("수정된 이벤트가 시작되는 일시입니다."),
                                fieldWithPath("endEventDateTime").description("수정된 이벤트가 종료되는 일시입니다."),
                                fieldWithPath("location").description("수정된 이벤트 위치입니다."),
                                fieldWithPath("basePrice").description("수정된 이벤트 기본 가격입니다."),
                                fieldWithPath("maxPrice").description("수정된 이벤트 최대 가격입니다."),
                                fieldWithPath("limitOfEnrollment").description("수정된 이벤트 모집제한 인원수입니다."),
                                fieldWithPath("offline").description("수정된 이벤트의 온/오프라인 여부입니다."),
                                fieldWithPath("free").description("수정된 이벤트의 유/무료 여부입니다."),
                                fieldWithPath("eventStatus").description("수정된 이벤트의 상태값입니다."),
                                //TODO _links 밑에 있는 데이터들은 무시
                                fieldWithPath("_links.*.*").ignored()
                        )
                ))
        ;
    }


    @DisplayName("입력값이 비어있는 경우에 이벤트 수정 실패")
    @Test
    void updateEvent400_empty() throws Exception {
        Event event = this.generateEvent(200);
        var eventDto = new EventDto();

        // WHEN & THEN
        this.mockMvc.perform(put("/api/events/{id}", event.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventDto))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;

    }

    @DisplayName("입력값이 잘못된 경우에 이벤트 수정 실패")
    @Test
    void updateEvent400_wrong() throws Exception {
        Event event = this.generateEvent(200);
        var eventDto = this.modelMapper.map(event, EventDto.class);
        eventDto.setBasePrice(20000);
        eventDto.setMaxPrice(1000);

        // WHEN & THEN
        this.mockMvc.perform(put("/api/events/{id}", event.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventDto))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;

    }


    @DisplayName("존재하지 않는 이벤트 수정 실패")
    @Test
    void updateEvent404() throws Exception {
        // GIVEN
        Event event = this.generateEvent(200);
        var eventDto = this.modelMapper.map(event, EventDto.class);

        // WHEN & THEN
        this.mockMvc.perform(put("/api/events/12312412")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(eventDto))
                )
                .andDo(print())
                .andExpect(status().isNotFound())
        ;

    }


    private Event generateEvent(int index) {
        Event event = Event.builder()
                .name("event " + index)
                .description("test event")
                .beginEnrollmentDateTime(LocalDateTime.of(2018, 11, 23, 14, 21))
                .closeEnrollmentDateTime(LocalDateTime.of(2018, 11, 24, 14, 21))
                .beginEventDateTime(LocalDateTime.of(2018, 11, 25, 14, 21))
                .endEventDateTime(LocalDateTime.of(2018, 11, 26, 14, 21))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("test location")
                .free(false)
                .offline(true)
                .eventStatus(EventStatus.DRAFT)
                .build();

        return this.eventRepository.save(event);
    }
}
