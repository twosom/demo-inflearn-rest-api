package com.icloud.demoinflearnrestapi.events;

import com.icloud.demoinflearnrestapi.index.IndexController;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/api/events", produces = MediaTypes.HAL_JSON_VALUE)
public class EventController {

    private final EventRepository eventRepository;
    private final ModelMapper modelMapper;
    private final EventValidator eventValidator;

    @InitBinder("eventDto")
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(eventValidator);
    }

    @PostMapping
    public ResponseEntity<Object> createEvent(@RequestBody @Validated EventDto eventDto, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest()
                    .body(
                            EntityModel.of(errors)
                                    .add(linkTo(methodOn(IndexController.class).index()).withRel("index"))
                    );
        }
        Event event = modelMapper.map(eventDto, Event.class);
        event.update();
        Event createdEvent = eventRepository.save(event);
        WebMvcLinkBuilder selfLinkBuilder = linkTo(getClass()).slash(createdEvent.getId());
        URI createdUri = selfLinkBuilder.toUri();

        EntityModel<Event> eventEntityModel = EntityModel.of(event,
                selfLinkBuilder.withSelfRel(),
                selfLinkBuilder.withRel("query-events"),
                selfLinkBuilder.withRel("update-event"),
                Link.of("/docs/index.html#resources-events-create").withRel("profile")
        );

        return ResponseEntity.created(createdUri)
                .body(eventEntityModel);
    }

    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<Event>>> queryEvents(Pageable pageable, PagedResourcesAssembler<Event> assembler) {
        Page<Event> page = this.eventRepository.findAll(pageable);
        var entityModels = assembler.toModel(page, this::convertToLinkedModel);
        entityModels.add(
                Link.of("/doc/index.html#resources-events-list").withRel("profile")
        );
        return ResponseEntity.ok(entityModels);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Event>> getEvents(@PathVariable("id") Integer id) {
        var optionalEvent = this.eventRepository.findById(id);
        if (optionalEvent.isEmpty()) {
            return ResponseEntity.notFound()
                    .build();
        }

        var event = optionalEvent.get();
        EntityModel<Event> eventResource = EntityModel.of(event,
                linkTo(getClass()).slash(event.getId()).withSelfRel(),
                Link.of("/docs/index.html#resources-events-get").withRel("profile")
        );
        return ResponseEntity.ok(eventResource);

    }


    @PutMapping("/{id}")
    public ResponseEntity updateEvent(@PathVariable("id") Integer id,
                                      @RequestBody @Validated EventDto eventDto,
                                      Errors errors) {
        var optionalEvent = this.eventRepository.findById(id);
        if (optionalEvent.isEmpty()) {
            return ResponseEntity.notFound()
                    .build();
        }

        if (errors.hasErrors()) {
            return ResponseEntity.badRequest()
                    .body(
                            EntityModel.of(errors)
                                    .add(linkTo(methodOn(IndexController.class).index()).withRel("index"))
                    );
        }

        var existingEvent = optionalEvent.get();
        modelMapper.map(eventDto, existingEvent);
        var updatedEvent = this.eventRepository.save(existingEvent);

        var eventResource = EntityModel.of(updatedEvent,
                linkTo(getClass()).slash(updatedEvent.getId()).withSelfRel(),
                Link.of("/docs/index.html#resources-events-update").withRel("profile")
        );


        return ResponseEntity.ok(eventResource);
    }


    private EntityModel<Event> convertToLinkedModel(Event event) {
        return EntityModel.of(event,
                linkTo(getClass()).slash(event.getId()).withSelfRel()
        );
    }
}
