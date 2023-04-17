package br.ufpb.dcx.apps4society.qtarolando.api.dto;

import br.ufpb.dcx.apps4society.qtarolando.api.model.Category;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventDTO implements Serializable {

    @NotEmpty(message = "Preenchimento obrigatório")
    @Schema(description = "Titulo para o evento", example = "Passeio turistico")
    private String title;

    @NotEmpty(message = "Preenchimento obrigatório")
    @Schema(description = "Subtitulo que o evento terá", example = "Pontos turisticos de João Pessoa")
    private String subtitle;

    @NotEmpty(message = "Preenchimento obrigatório")
    private List<Category> categories = new ArrayList<>();

    @NotEmpty(message = "Preenchimento obrigatório")
    @Column(length = 2000)
    @Schema(description = "Descrição do evento", example = "Passeio feito através dos pontos turisticos da capital da Paraíba")
    private String description;

    @NotNull
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    @Schema(description = "Data de início do evento", example = "01/01/2023 08:00")
    private LocalDateTime initialDate;

    @NotNull
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    @Schema(description = "Data de encerramento do evento", example = "01/01/2023 19:00")
    private LocalDateTime finalDate;

    @NotEmpty(message = "Preenchimento obrigatório")
    @Schema(description = "Caminho da imagem que representará o evento", example = "caminho da imagem")
    private String imagePath;

    @NotEmpty(message = "Preenchimento obrigatório")
    @Schema(description = "Modalidade que o evento terá, PRESENCIAL ou ONLINE", example = "PRESENCIAL")
    private String modality;

    @NotEmpty(message = "Preenchimento obrigatório")
    @Schema(description = "Local ao qual o evento ocorrerá", example = "Lagoa no centro de João Pessoa")
    private String location;

    @Schema(description = "Número para contato", example = "83000000000")
    private String phone;

    @Schema(description = "Url do site caso o evento tenha um", example = "www.exemplo.com.br")
    private String site;
}
