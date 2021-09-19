package com.icloud.demoinflearnrestapi.events;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class EventEntityModel extends EntityModel<Event> {

    @JsonUnwrapped
    Event event;

    public EventEntityModel(Event event, Link ...links) {
        this.event = event;
        add(links);
        add(linkTo(EventController.class).slash(event.getId()).withSelfRel());
    }
}
