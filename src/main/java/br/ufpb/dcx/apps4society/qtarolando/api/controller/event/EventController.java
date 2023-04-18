package br.ufpb.dcx.apps4society.qtarolando.api.controller.event;

import br.ufpb.dcx.apps4society.qtarolando.api.dto.EventDTO;
import br.ufpb.dcx.apps4society.qtarolando.api.model.Event;
import br.ufpb.dcx.apps4society.qtarolando.api.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/events")
public class EventController implements EventInterface {

    @Autowired
    private EventService eventService;

    @Override
    @GetMapping("/{id}")
    public Event getEventById(@RequestHeader("Authorization") String token,
                              @PathVariable("id") Integer id) {
        return eventService.getEventById(token, id);
    }

    @Override
    @GetMapping("/filter")
    public Page<Event> getEventsByFilter(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "categoryId", required = false) Long categoryId,
            @RequestParam(value = "modality", required = false) String modality,
            @RequestParam(value = "dateType", required = false) String dateType,
            @RequestParam(value = "initialDate", required = false) String initialDate,
            @RequestParam(value = "finalDate", required = false) String finalDate,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "pageSize", defaultValue = "24") Integer pageSize) {
        return eventService.getEventsByFilter(title, categoryId, modality, dateType, initialDate, finalDate,
                page,
                pageSize);
    }

    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    //É possivel criar um mesmo evento multiplas vezes?
    public void createEvent(@RequestHeader("Authorization") String token,
                            @Valid @RequestBody EventDTO eventDTO) {
        eventService.createEvent(token, eventDTO);
    }

    @Override
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateEvent(@RequestHeader("Authorization") String token,
                            @Valid @PathVariable("id") Integer id,
                            @RequestBody EventDTO newEventDTO) {
        eventService.updateEvent(token, id, newEventDTO);
    }

    @Override
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEvent(@PathVariable("id") Integer id) {
        eventService.deleteEvent(id);
    }

}