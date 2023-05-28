package br.ufpb.dcx.apps4society.qtarolando.api.service;

import br.ufpb.dcx.apps4society.qtarolando.api.dto.EventDTO;
import br.ufpb.dcx.apps4society.qtarolando.api.dto.UserAccountNewDTO;
import br.ufpb.dcx.apps4society.qtarolando.api.model.Event;
import br.ufpb.dcx.apps4society.qtarolando.api.model.UserAccount;
import br.ufpb.dcx.apps4society.qtarolando.api.repository.EventCustomRepository;
import br.ufpb.dcx.apps4society.qtarolando.api.repository.EventRepository;
import br.ufpb.dcx.apps4society.qtarolando.api.service.exceptions.AuthorizationException;
import br.ufpb.dcx.apps4society.qtarolando.api.service.exceptions.ObjectNotFoundException;
import br.ufpb.dcx.apps4society.qtarolando.api.util.EventCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class EventServiceTest {

    @InjectMocks
    private EventService eventService;

    @Mock
    private EventRepository eventRepositoryMock;

    @Mock
    private EventCustomRepository eventCustomRepository;

//    @Mock
//    private UserAccountService userAccountService;

    @BeforeEach
    void setUp(){
        List<Event> events = new ArrayList<>() ;
        events.add(EventCreator.defaultEvent());
        events.get(0).setId(1);
        PageImpl<Event> eventPage = new PageImpl<>(events);

//        UserAccountNewDTO user = new UserAccountNewDTO("w02@gmail.com", "wellington", "12345678");
//        UserAccount userForTest = new UserAccount(user);


        BDDMockito.when(eventCustomRepository.find(ArgumentMatchers.anyString(), ArgumentMatchers.anyLong(),
                        ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString(),
                        ArgumentMatchers.anyString(), ArgumentMatchers.any(PageRequest.class)))
                .thenReturn(eventPage);

        BDDMockito.when(eventRepositoryMock.findById(ArgumentMatchers.anyInt()))
                .thenReturn(Optional.ofNullable(events.get(0)));

        BDDMockito.doNothing().when(eventRepositoryMock).delete(ArgumentMatchers.any(Event.class));

        BDDMockito.when(eventRepositoryMock.save(ArgumentMatchers.any(Event.class)))
                .thenReturn(events.get(0));

//        BDDMockito.when(userAccountService.insert(ArgumentMatchers.any(UserAccountNewDTO.class)))
//                .thenReturn(userForTest);


    }



//    @Test
//    void getEventById_ReturnsEvent(){
//        int expectedId = 1;
//
//        Event eventFound = eventService.getEventById(expectedId);
//
//        Assertions.assertThat(eventFound).isNotNull();
//
//        Assertions.assertThat(eventFound.getId())
//                .isNotNull()
//                .isEqualTo(expectedId);
//
//    }

//    @Test
//    void getEventById_ThrowsObjectNotFoundException(){
//        BDDMockito.when(eventRepositoryMock.findById(ArgumentMatchers.anyInt()))
//                .thenThrow(ObjectNotFoundException.class);
//
//        int expectedId = 2;
//
//        Assertions.assertThatThrownBy(() -> eventService.getEventById(expectedId))
//                .isInstanceOf(ObjectNotFoundException.class);
//    }
//
//    @Test
//    void getEventById_doesNotThrowAnyException(){
//        int expectedId = 1;
//
//        Assertions.assertThatCode(() -> eventService.getEventById(expectedId))
//                .doesNotThrowAnyException();
//
//    }

    @Test
    void getEventsByFilter_ReturnsEventByTitle(){
        String expectedTitle = EventCreator.defaultEvent().getTitle();

        Page<Event> events = eventService.getEventsByFilter(expectedTitle, 1L, "PRESENCIAL",
                "dateType", "initialDate", "finalDate",1,1);

        Assertions.assertThat(events).isNotNull();

        Assertions.assertThat(events.toList())
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(events.toList().get(0).getTitle())
                .isNotNull()
                .isEqualTo(expectedTitle);

    }

    @Test
    void getEventsByFilter_ReturnsEmptyListOfEventByTitle(){
        BDDMockito.when(eventCustomRepository.find(ArgumentMatchers.anyString(), ArgumentMatchers.anyLong(),
                        ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString(),
                        ArgumentMatchers.anyString(), ArgumentMatchers.any(PageRequest.class)))
                .thenReturn(Page.empty());

        String expectedTitle = EventCreator.defaultEvent().getTitle();

        Page<Event> events = eventService.getEventsByFilter(expectedTitle, 1L, "PRESENCIAL",
                "dateType", "initialDate", "finalDate",1,1);

        Assertions.assertThat(events.toList())
                .isNotNull()
                .hasSize(0)
                .isEmpty();

    }

    @Test
    void getEventsByFilter_ReturnsListOfEventsByCategoryId(){
        Long expectedCategoryId = EventCreator.defaultEvent().getCategories().get(0).getId();
        String expectedCategoryName = EventCreator.defaultEvent().getCategories().get(0).getName();

        Page<Event> events = eventService.getEventsByFilter("Praia", expectedCategoryId, "PRESENCIAL",
                "dateType", "initialDate", "finalDate",1,1);

        Assertions.assertThat(events).isNotNull();

        Assertions.assertThat(events.toList())
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(events.toList().get(0).getCategories().get(0).getId())
                .isEqualTo(expectedCategoryId)
                .isNotNull();

        Assertions.assertThat(events.toList().get(0).getCategories().get(0).getName())
                .isEqualTo(expectedCategoryName)
                .isNotNull();
    }

    @Test
    void getEventsByFilter_ReturnsEmptyListOfEventByCategoryId(){
        BDDMockito.when(eventCustomRepository.find(ArgumentMatchers.anyString(), ArgumentMatchers.anyLong(),
                        ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString(),
                        ArgumentMatchers.anyString(), ArgumentMatchers.any(PageRequest.class)))
                .thenReturn(Page.empty());

        Long expectedCategoryId = EventCreator.defaultEvent().getCategories().get(0).getId();

        Page<Event> events = eventService.getEventsByFilter("Praia", expectedCategoryId, "PRESENCIAL",
                "dateType", "initialDate", "finalDate",1,1);

        Assertions.assertThat(events.toList())
                .isNotNull()
                .hasSize(0)
                .isEmpty();

    }

    @Test
    void getEventsByFilter_ReturnsListOfEventsByEventModalityWith_Presencial_Modality(){
        String expectedModalityDescription = EventCreator.defaultEvent().getModality().getDescription();
        int expectedModalityId = EventCreator.defaultEvent().getModality().getCod();

        Page<Event> events = eventService.getEventsByFilter("Praia", 1L, expectedModalityDescription,
                "dateType", "initialDate", "finalDate",1,1);

        Assertions.assertThat(events.toList())
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(events.toList().get(0).getModality().getCod())
                .isEqualTo(expectedModalityId);

        Assertions.assertThat(events.toList().get(0).getModality().getDescription())
                .isEqualTo(expectedModalityDescription);
    }

    @Test
    void getEventsByFilter_ReturnsListOfEventsByEventModalityWith_Online_Modality(){
        Event eventWithOnlineModality = EventCreator.defaultEvent();
        eventWithOnlineModality.setModality("ONLINE");

        String expectedModalityDescription = eventWithOnlineModality.getModality().getDescription();
        int expectedModalityId = eventWithOnlineModality.getModality().getCod();

        List<Event> eventsList = new ArrayList<>() ;
        eventsList.add(EventCreator.defaultEvent());
        eventsList.get(0).setModality("ONLINE");

        PageImpl<Event> eventPage = new PageImpl<>(eventsList);

        BDDMockito.when(eventCustomRepository.find(ArgumentMatchers.anyString(), ArgumentMatchers.anyLong(),
                        ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString(),
                        ArgumentMatchers.anyString(), ArgumentMatchers.any(PageRequest.class)))
                .thenReturn(eventPage);

        Page<Event> events = eventService.getEventsByFilter("Praia", 1L, expectedModalityDescription,
                "dateType", "initialDate", "finalDate",1,1);

        Assertions.assertThat(events.toList())
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(events.toList().get(0).getModality().getCod())
                .isEqualTo(expectedModalityId);

        Assertions.assertThat(events.toList().get(0).getModality().getDescription())
                .isEqualTo(expectedModalityDescription);
    }

    @Test
    void getEventsByFilter_ReturnsListOfEventsByDateRange(){
        String initialDateExpected = "2022-09-20T19:00:00";
        String finalDateExpected = "2022-09-27T16:00:00";
        String expectedTitle = "Praia";

        Page<Event> events = eventService.getEventsByFilter("Praia", 1L, "PRESENCIAL",
                "dateType", initialDateExpected, finalDateExpected,1,1);

        Assertions.assertThat(events.toList())
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(events.toList().get(0).getTitle())
                .isNotNull()
                .isEqualTo(expectedTitle);


    }

//    @Test
//    void createEvent(){
//        EventDTO eventDTO = EventCreator.defaultEventDTO();
//        String expectedTitle = eventDTO.getTitle();
//
//        UserAccountNewDTO user = new UserAccountNewDTO("w02@gmail.com", "wellington", "12345678");
//
//        UserAccount expected = userAccountService.insert(user);
//
//        eventService.createEvent(eventDTO);
//
//        Page<Event> events = eventService.getEventsByFilter(expectedTitle, 1L, "PRESENCIAL",
//                "dateType", "initialDate", "finalDate",1,1);
//
//        Assertions.assertThat(events)
//                .isNotNull()
//                .isNotEmpty()
//                .hasSize(1);
//
//        Assertions.assertThat(events.toList().get(0).getTitle())
//                .isNotNull()
//                .isEqualTo(eventDTO.getTitle());
//    }

//    @Test
//    void createEvent_ThrowsNullPointerException_whenUserIsnull(){
//        BDDMockito.when(eventRepositoryMock.save(ArgumentMatchers.any(Event.class)))
//                .thenThrow(NullPointerException.class);
//
//        EventDTO eventDTO = EventCreator.defaultEventDTO();
//
//        Assertions.assertThatThrownBy(() -> eventService.createEvent(eventDTO))
//                .isInstanceOf(NullPointerException.class);
//
//
//    }
//
//    @Test
//    void updateEvent_ThrowsAuthorizationException_whenDontHaveAccessToUpdate() {
//        int expectedId = 1;
//        EventDTO newEvent = EventCreator.defaultEventDTO();
//
//
//        Assertions.assertThatThrownBy(() -> eventService.updateEvent(expectedId, newEvent))
//                .isInstanceOf(AuthorizationException.class)
//        ;
//
//    }

    @Test
    void deleteEvent_ThrowsAuthorizationException_whenDontHaveAccessToRemove() {

        Assertions.assertThatThrownBy(() -> eventService.deleteEvent(1))
                .isInstanceOf(AuthorizationException.class)
        ;

    }

}