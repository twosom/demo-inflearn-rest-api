package com.icloud.demoinflearnrestapi.events;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class EventTest {

    @Test
    void builder() throws Exception {
        Event event = Event.builder()
                .name("Inflearn Spring REST API")
                .description("REST API development with Spring")
                .build();
        assertNotNull(event);
    }


    @Test
    void javaBean() throws Exception {
        // GIVEN
        String name = "Event";
        String description = "Spring";

        // WHEN
        Event event = new Event();
        event.setName(name);
        event.setDescription(description);

        // THEN
        assertEquals(event.getName(), name);
        assertEquals(event.getDescription(), description);
    }

    @DisplayName("event 의 free 값이 잘 설정 되는지 테스트")
    @ParameterizedTest(name = "basePrice : {0}, maxPrice : {1}, isFree : {2}")
    @MethodSource(value = "parametersForTestFree")
    void testFree(int basePrice, int maxPrice, boolean isFree) throws Exception {
        // GIVEN
        Event event = Event.builder()
                .basePrice(basePrice)
                .maxPrice(maxPrice)
                .build();

        // WHEN
        event.update();

        // THEN
        assertEquals(event.isFree(), isFree);
    }

    static Object[] parametersForTestFree() {
        return new Object[]{
                new Object[]{0, 0, true},
                new Object[]{100, 0, false},
                new Object[]{0, 100, false},
                new Object[]{100, 200, false}
        };
    }


    @DisplayName("Event 의 offline 값이 잘 설정 되는지 테스트")
    @ParameterizedTest(name = "location : {0}, isOffline : {1}")
    @MethodSource(value = "parametersForTestOffline")
    void testOffline(String location, boolean isOffline) throws Exception {
        // GIVEN
        Event event = Event.builder()
                .location(location)
                .build();

        // WHEN
        event.update();

        // THEN
        assertEquals(event.isOffline(), isOffline);
    }

    static Object[] parametersForTestOffline() {
        return new Object[]{
                new Object[]{"강남역 네이버 D2스타텁 팩토리", true},
                new Object[]{null, false},
                new Object[]{"              ", false}
        };
    }
}