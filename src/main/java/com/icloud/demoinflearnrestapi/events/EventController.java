package com.icloud.demoinflearnrestapi.events;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Controller
@RequestMapping(value = "/api/events", produces = MediaTypes.HAL_JSON_VALUE)
@RequiredArgsConstructor
public class EventController {

    private final EventRepository eventRepository;
    private final ModelMapper mapper;
    private final EventValidator eventValidator;

    @InitBinder("eventDto")
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(eventValidator);
    }

    @PostMapping
    public ResponseEntity<Object> createEvent(@RequestBody @Valid EventDto eventDto, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest()
                    .body(errors);
        }
        Event newEvent = eventRepository.save(mapper.map(eventDto, Event.class));
        URI createdUri = linkTo(this.getClass()).slash(newEvent.getId())
                .toUri();

        return ResponseEntity.created(createdUri)
                .body(newEvent);
    }
}
