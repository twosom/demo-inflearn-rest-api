package com.icloud.demoinflearnrestapi.events;

import org.junit.jupiter.api.Test;

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
}