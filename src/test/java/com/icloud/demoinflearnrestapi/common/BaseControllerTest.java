package com.icloud.demoinflearnrestapi.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.icloud.demoinflearnrestapi.events.EventRepository;
import org.junit.jupiter.api.Disabled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class)
@Disabled
public class BaseControllerTest {


    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected EventRepository eventRepository;

    @Autowired
    protected MockMvc mockMvc;

}
