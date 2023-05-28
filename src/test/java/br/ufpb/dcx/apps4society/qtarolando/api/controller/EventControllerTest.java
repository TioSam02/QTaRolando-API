package br.ufpb.dcx.apps4society.qtarolando.api.controller;

import br.ufpb.dcx.apps4society.qtarolando.api.controller.event.EventController;
import br.ufpb.dcx.apps4society.qtarolando.api.dto.EventDTO;
import br.ufpb.dcx.apps4society.qtarolando.api.model.Event;
import br.ufpb.dcx.apps4society.qtarolando.api.service.EventService;
import br.ufpb.dcx.apps4society.qtarolando.api.util.EventCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(SpringExtension.class)
public class EventControllerTest {

    @InjectMocks
    private EventController eventController;

    @Mock
    private EventService eventServiceMock;

    @BeforeEach
    void setUp(){
        List<Event> events = new ArrayList<>() ;
        events.add(EventCreator.defaultEvent());
        PageImpl<Event> eventPage = new PageImpl<>(events);

        EventDTO eventDTO = EventCreator.defaultEventDTO();

//        BDDMockito.when(eventServiceMock.listAllUsingPage(ArgumentMatchers.any()))
//                .thenReturn(eventPage);
//
//        BDDMockito.when(eventServiceMock.getAllEvents())
//                .thenReturn(events);

//        BDDMockito.when(eventServiceMock.getEventsByFilter(ArgumentMatchers.anyString(), ArgumentMatchers.anyLong(),
//                        ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString(),
//                        ArgumentMatchers.anyString(), ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt()))
//                .thenReturn(eventPage);
//
//        BDDMockito.doNothing().when(eventServiceMock).createEvent(eventDTO);
//
//        BDDMockito.doNothing().when(eventServiceMock).updateEvent(1, eventDTO);
//
//        BDDMockito.doNothing().when(eventServiceMock).deleteEvent(ArgumentMatchers.anyInt());

    }
//
//    @Test
//    void listAll_ReturnsListOfEventInsidePage(){
//        String expectedSubtitle = EventCreator.defaultEvent().getSubtitle();
//
//        Page<Event> eventPage = eventController.listAll(null).getBody();
//
//        Assertions.assertThat(eventPage).isNotNull();
//
//        Assertions.assertThat(eventPage.toList())
//                .isNotEmpty()
//                .hasSize(1);
//
//        Assertions.assertThat(eventPage.toList().get(0).getSubtitle())
//                .isEqualTo(expectedSubtitle);
//
//    }
//
//    @Test
//    void getAllEvents_ReturnsListOfEvents(){
//        String expectedDescription = EventCreator.defaultEvent().getDescription();
//
//        List<Event> events = eventController.getAllEvents();
//
//        Assertions.assertThat(events)
//                .isNotNull()
//                .isNotEmpty()
//                .hasSize(1);
//
//        Assertions.assertThat(events.get(0).getDescription())
//                .isEqualTo(expectedDescription);
//    }

    @Test
    void getEventsByFilter_ReturnsEventByTitle(){
        String expectedTitle = EventCreator.defaultEvent().getTitle();

        Page<Event> events = eventController.getEventsByFilter(expectedTitle, 1L, "PRESENCIAL",
                "dateType", "initialDate", "finalDate",1,1);

        Assertions.assertThat(events)
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(events.toList().get(0).getTitle())
                .isNotNull()
                .isEqualTo(expectedTitle);

    }

    @Test
    void getEventsByFilter_ReturnsEmptyListOfEventByTitle(){
        BDDMockito.when(eventServiceMock.getEventsByFilter(ArgumentMatchers.anyString(), ArgumentMatchers.anyLong(),
                        ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString(),
                        ArgumentMatchers.anyString(), ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt()))
                .thenReturn(Page.empty());

        String expectedTitle = EventCreator.defaultEvent().getTitle();

        Page<Event> events = eventController.getEventsByFilter(expectedTitle, 1L, "PRESENCIAL",
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

        Page<Event> events = eventController.getEventsByFilter("Praia", expectedCategoryId, "PRESENCIAL",
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
        BDDMockito.when(eventServiceMock.getEventsByFilter(ArgumentMatchers.anyString(), ArgumentMatchers.anyLong(),
                        ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString(),
                        ArgumentMatchers.anyString(), ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt()))
                .thenReturn(Page.empty());

        Long expectedCategoryId = EventCreator.defaultEvent().getCategories().get(0).getId();

        Page<Event> events = eventController.getEventsByFilter("Praia", expectedCategoryId, "PRESENCIAL",
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

        Page<Event> events = eventController.getEventsByFilter("Praia", 1L, expectedModalityDescription,
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

        BDDMockito.when(eventServiceMock.getEventsByFilter(ArgumentMatchers.anyString(), ArgumentMatchers.anyLong(),
                        ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString(),
                        ArgumentMatchers.anyString(), ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt()))
                .thenReturn(eventPage);

        Page<Event> events = eventController.getEventsByFilter("Praia", 1L, expectedModalityDescription,
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

        Page<Event> events = eventController.getEventsByFilter("Praia", 1L, "PRESENCIAL",
                "dateType", initialDateExpected, finalDateExpected,1,1);

        Assertions.assertThat(events.toList())
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(events.toList().get(0).getTitle())
                .isNotNull()
                .isEqualTo(expectedTitle);


    }

//    @Test
//    void createEvent_doesNotThrowAnyException(){
//        EventDTO eventDTO = EventCreator.defaultEventDTO();
//
//        Assertions.assertThatCode(() -> eventController.createEvent(eventDTO))
//                .doesNotThrowAnyException();
//
//    }
//
//    @Test
//    void updateEvent_doesNotThrowAnyException(){
//        EventDTO eventDTO = EventCreator.defaultEventDTO();
//        int idToBeReplaced = 1;
//
//        Assertions.assertThatCode(() -> eventController.updateEvent(idToBeReplaced, eventDTO))
//                .doesNotThrowAnyException();
//
//    }

    @Test
    void deleteEvent_doesNotThrowAnyException(){
        int idToBedeleted = 1;

        Assertions.assertThatCode(() -> eventController.deleteEvent(idToBedeleted))
                .doesNotThrowAnyException();

    }
}