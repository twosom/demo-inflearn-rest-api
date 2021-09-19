package com.icloud.demoinflearnrestapi.events;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    void testFree() throws Exception {
        // GIVEN
        Event event = Event.builder()
                .basePrice(0)
                .maxPrice(0)
                .build();

        // WHEN
        event.update();

        // THEN
        assertTrue(event.isFree());


        // GIVEN
        event = Event.builder()
                .basePrice(100)
                .maxPrice(0)
                .build();

        // WHEN
        event.update();

        // THEN
        assertFalse(event.isFree());


        // GIVEN
        event = Event.builder()
                .basePrice(0)
                .maxPrice(100)
                .build();

        // WHEN
        event.update();

        // THEN
        assertFalse(event.isFree());
    }

    @Test
    void testOffline() throws Exception {
        // GIVEN
        Event event = Event.builder()
                .location("강남역 네이버 D2 스타텁 팩토리")
                .build();

        // WHEN
        event.update();

        // THEN
        assertTrue(event.isOffline());


        // GIVEN
        event = Event.builder()
                .build();

        // WHEN
        event.update();

        // THEN
        assertFalse(event.isOffline());
    }
}