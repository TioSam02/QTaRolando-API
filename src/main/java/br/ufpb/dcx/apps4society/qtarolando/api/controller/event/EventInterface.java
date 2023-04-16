package br.ufpb.dcx.apps4society.qtarolando.api.controller.event;

import br.ufpb.dcx.apps4society.qtarolando.api.dto.EventDTO;
import br.ufpb.dcx.apps4society.qtarolando.api.model.Event;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

public interface EventInterface {

    @Operation(summary = "Pesquisa um evento pelo seu id",
            tags = {"event"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação feita com sucesso"),
            @ApiResponse(responseCode = "404", description = "Quando o evento não é encontrado")
    })
    public Event getEventById(@PathVariable("id") Integer id);


    @Operation(summary = "Filtro usado para a pesquisa de evento",
            description = "Caso não passe nenhum dos parametros a seguir ele irá retornar uma page com todos os eventos " +
                    "cadastrados. A pesquisa pode ser feita pelo titulo do evento, id da categoria ou modalidade. " +
                    "A pesquisa pela data de inicio e data final de um evento deve ser feita usando alguns dos dateType do sistema, " +
                    "também é possível especificar como deve ser o tamanho e a quantidade de paginas desejadas",
            tags = {"event"})
    @ApiResponse(responseCode = "200", description = "Operação feita com sucesso")
    public Page<Event> getEventsByFilter(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "categoryId", required = false) Long categoryId,
            @RequestParam(value = "modality", required = false) String modality,
            @RequestParam(value = "dateType", required = false) String dateType,
            @RequestParam(value = "initialDate", required = false) String initialDate,
            @RequestParam(value = "finalDate", required = false) String finalDate,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "pageSize", defaultValue = "24") Integer pageSize);


    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    @Operation(summary = "Cria um evento",
            description = "Cria um evento e o associa ao usuário logado",
            tags = {"event"})
    @ApiResponse(responseCode = "201", description = "Evento criado com sucesso")
    public void createEvent(@RequestHeader("Authorization") String token,
                            @Valid @RequestBody EventDTO eventDTO);


    @Operation(summary = "Atualiza os dados de um evento",
            description = "É preciso estar logado no sistema",
            tags = {"event"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Operação feita com sucesso"),
            @ApiResponse(responseCode = "401", description = "Quando o usuário não está logado"),
            @ApiResponse(responseCode = "403", description = "Quando não é o criador do evento"),
            @ApiResponse(responseCode = "404", description = "Quando o evento não é encontrado")
    })
    public void updateEvent(@PathVariable("id") Integer id, @RequestBody EventDTO newEventDTO);


    @Operation(summary = "Deleta os dados de um evento",
            description = "É preciso estar logado no sistema",
            tags = {"event"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Operação feita com sucesso"),
            @ApiResponse(responseCode = "401", description = "Quando o usuário não está logado"),
            @ApiResponse(responseCode = "403", description = "Quando não é o criador do evento"),
            @ApiResponse(responseCode = "404", description = "Quando o evento não é encontrado")
    })
    public void deleteEvent(@PathVariable("id") Integer id);
}
