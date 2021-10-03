package com.icloud.demoinflearnrestapi.index;

import com.icloud.demoinflearnrestapi.events.EventController;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class IndexController {


    @GetMapping("/api/")
    public RepresentationModel index() {
        return new RepresentationModel<>(
                linkTo(methodOn(EventController.class).getClass()).withRel("events")
        );
    }

}
