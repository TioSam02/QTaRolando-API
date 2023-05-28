package br.ufpb.dcx.apps4society.qtarolando.api.model;

import br.ufpb.dcx.apps4society.qtarolando.api.dto.UserAccountNewDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;


import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.util.*;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAccount {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "ID", updatable = false, nullable = false)
    @ColumnDefault("gen_random_uuid()")
    @Type(type = "uuid-char")
    @Getter
    @Setter
    private UUID id;

    @Email
    @Column(unique = true)
    private String email;

    private String username;

    @Size(min = 8, message = "Senha deve conter no minimo 8 caracteres")
    @JsonIgnore
    private String password;

    @OneToMany
    @JoinColumn(name = "user_account_id")
    private List<Event> events = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof UserAccount))
            return false;
        UserAccount that = (UserAccount) o;
        return id.equals(that.id);
    }

    public UserAccount(UserAccountNewDTO userAccountNewDTO) {
        this.email = userAccountNewDTO.getEmail();
        this.username = userAccountNewDTO.getUsername();
        this.password = userAccountNewDTO.getPassword();
    }

    public UserAccount(String email, String username, String password, List<Event> events, Set<Role> roles) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.events = events;
        this.roles = roles;
    }

    public UserAccount(String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public void addRole(Role role) {
        this.roles.add(role);
    }

}
