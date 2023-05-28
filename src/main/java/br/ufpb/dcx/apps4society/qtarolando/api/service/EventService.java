package br.ufpb.dcx.apps4society.qtarolando.api.service;

import br.ufpb.dcx.apps4society.qtarolando.api.dto.EventDTO;
import br.ufpb.dcx.apps4society.qtarolando.api.model.Event;
import br.ufpb.dcx.apps4society.qtarolando.api.model.UserAccount;
import br.ufpb.dcx.apps4society.qtarolando.api.repository.EventCustomRepository;
import br.ufpb.dcx.apps4society.qtarolando.api.repository.EventRepository;
import br.ufpb.dcx.apps4society.qtarolando.api.security.UserPrincipal;
import br.ufpb.dcx.apps4society.qtarolando.api.service.exceptions.AuthorizationException;
import br.ufpb.dcx.apps4society.qtarolando.api.service.exceptions.ObjectNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Log4j2
public class EventService {

    @Autowired
    private EventCustomRepository eventCustomRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private JWTService jwtService;

    public Event getEventById(String token, Integer id) throws ObjectNotFoundException {
        log.info("entrou");
        Optional<String> userEmail = jwtService.recoverUser(token);
        log.info(userEmail);
        if (!userEmail.isPresent()) {
            throw new ObjectNotFoundException("Usuario não está logado");
        }

        return eventRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(
                        "Evento não encontrado!ee Id: " + id + ", Tipo: " + Event.class.getName()));
    }

    @Transactional
    public Page<Event> getEventsByFilter(String title, Long categoryId, String modality,
                                         String dateType,
                                         String initialDate,
                                         String finalDate, Integer page, Integer pageSize) {

        Pageable pageable = PageRequest.of(page, pageSize);

        return eventCustomRepository.find(title, categoryId, modality, dateType, initialDate, finalDate,
                pageable);
    }

    @Transactional
    public void createEvent(String token, EventDTO eventDTO) {
        log.info("entrou");
        UserAccount user = validateUser(token);
        log.info(user);

        Event newEvent = new Event(eventDTO);
        eventRepository.save(newEvent);

        UserAccount userAccount = userAccountService.find(user.getId());
        userAccount.getEvents().add(newEvent);
        userAccountService.updateUserEvents(userAccount);
    }

    @Transactional
    public void updateEvent(String token, Integer id, EventDTO newEventDTO) throws ObjectNotFoundException {
        UserAccount user = validateUser(token);

        Event event = getEventById(token, id);
        if (event == null) {
            throw new ObjectNotFoundException("Evento não encontrado! Id: " + id + ", Tipo: " + Event.class.getName());
        }
        if (!user.getEvents().contains(event)) {
            throw new AuthorizationException("Acesso negado 3");
        }

        BeanUtils.copyProperties(newEventDTO, event, "id");
        eventRepository.save(event);
    }

    @Transactional
    public void deleteEvent(Integer id) throws ObjectNotFoundException {
        UserPrincipal userSS = UserAccountService.getUserAuthenticated();
        if (userSS == null) {
            throw new AuthorizationException("Acesso negado 1");
        }
        Optional<Event> event = eventRepository.findById(id);
        if (!event.isPresent()) {
            throw new ObjectNotFoundException("Evento não encontrado! Id: " + id + ", Tipo: " + Event.class.getName());
        }

        UserAccount userAccount = userAccountService.findByEmail(userSS.getEmail());
        if (userAccount.getEvents().contains(event.get())) {
            eventRepository.delete(event.get());
        } else {
            throw new AuthorizationException("Acesso negado 2");
        }
    }

    private UserAccount validateUser(String token) throws ObjectNotFoundException {
        Optional<String> userEmail = jwtService.recoverUser(token);
        log.info(userEmail);

        if(!userEmail.isPresent()) {
            throw new ObjectNotFoundException("Usuário não encontrado!");

        }
        return userAccountService.findByEmail(userEmail.get());
    }
}