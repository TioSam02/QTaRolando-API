package br.ufpb.dcx.apps4society.qtarolando.api.model;

import br.ufpb.dcx.apps4society.qtarolando.api.dto.EventDTO;
import br.ufpb.dcx.apps4society.qtarolando.api.model.enums.EventModality;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "events")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(example = "1")
    private Integer id;

    @Schema(example = "Passeio turistico")
    private String title;

    @Schema(example = "Pontos turisticos de João Pessoa")
    private String subtitle;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "event_categories", joinColumns = @JoinColumn(name = "event_id"), inverseJoinColumns = @JoinColumn(name = "category_id"))
    private List<Category> categories = new ArrayList<>();

    @Lob
    @Schema(example = "Passeio feito através dos pontos turisticos da capital da Paraíba")
    private String description;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    @Schema(example = "01/01/2023 08:00")
    private LocalDateTime initialDate;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    @Schema(example = "01/01/2023 19:00")
    private LocalDateTime finalDate;

    @Lob
    @Schema(example = "caminho da imagem")
    private String imagePath;

    @Schema(example = "PRESENCIAL")
    private String modality;

    @Schema(example = "Lagoa no centro de João Pessoa")
    private String location;

    @Schema(example = "83000000000")
    private String phone;

    @Schema(example = "www.exemplo.com.br")
    private String site;

    public Event(EventDTO eventDTO) {
        this.title = eventDTO.getTitle();
        this.subtitle = eventDTO.getSubtitle();
        this.categories = eventDTO.getCategories();
        this.description = eventDTO.getDescription();
        this.initialDate = eventDTO.getInitialDate();
        this.finalDate = eventDTO.getFinalDate();
        this.imagePath = eventDTO.getImagePath();
        this.modality = eventDTO.getModality();
        this.location = eventDTO.getLocation();
    }

    public Event(String title, String subtitle, List<Category> categories, String description, LocalDateTime initialDateConverted,
                 LocalDateTime finalDateConverted, String imagePath, String eventModality,
                 String location, String phone, String site) {

        this.title = title;
        this.subtitle = subtitle;
        this.categories = categories;
        this.description = description;
        this.initialDate = initialDateConverted;
        this.finalDate = finalDateConverted;
        this.imagePath = imagePath;
        this.modality = eventModality;
        this.location = location;
        this.phone = phone;
        this.site = site;

    }

    public void setModality(String modality) {
        this.modality = EventModality.validate(modality);
    }

    public EventModality getModality() {
        return EventModality.toEnum(modality);
    }

}
