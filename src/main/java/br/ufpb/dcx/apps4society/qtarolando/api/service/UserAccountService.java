package br.ufpb.dcx.apps4society.qtarolando.api.service;

import br.ufpb.dcx.apps4society.qtarolando.api.dto.UserAccountDTO;
import br.ufpb.dcx.apps4society.qtarolando.api.dto.UserAccountNewDTO;
import br.ufpb.dcx.apps4society.qtarolando.api.dto.UserPasswordDTO;
import br.ufpb.dcx.apps4society.qtarolando.api.model.Role;
import br.ufpb.dcx.apps4society.qtarolando.api.model.UserAccount;
import br.ufpb.dcx.apps4society.qtarolando.api.model.enums.Roles;
import br.ufpb.dcx.apps4society.qtarolando.api.repository.RoleRepository;
import br.ufpb.dcx.apps4society.qtarolando.api.repository.UserAccountRepository;
import br.ufpb.dcx.apps4society.qtarolando.api.security.UserPrincipal;
import br.ufpb.dcx.apps4society.qtarolando.api.service.exceptions.AuthorizationException;
import br.ufpb.dcx.apps4society.qtarolando.api.service.exceptions.DataIntegrityException;
import br.ufpb.dcx.apps4society.qtarolando.api.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class UserAccountService {
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserAccountRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    public UserAccount find(UUID id) {

        Optional<UserAccount> obj = userRepository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException(
                "Objeto não encontrado! Id: " + id + ", Tipo: " + UserAccount.class.getName()));
    }

    public List<UserAccount> findAll() {
        return userRepository.findAll();
    }

    public UserAccount findByEmail(String email) {

        UserAccount obj = userRepository.findByEmail(email);
        if (obj == null) {
            throw new ObjectNotFoundException("Usuário não encontrado!");
        }
        return obj;
    }

    public UserAccount findByUsername(String userName) {
        UserPrincipal user = getUserAuthenticated();
        if (user == null || !user.hasRole(Roles.ADMIN) && !userName.equals(user.getUsername())) {
            throw new AuthorizationException("Acesso negado 4");
        }

        UserAccount obj = userRepository.findByUsername(userName);
        if (obj == null) {
            throw new ObjectNotFoundException("Usuário não encontrado!");
        }
        return obj;
    }

    public Page<UserAccount> findPage(Integer page, Integer pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        return userRepository.findAll(pageable);
    }

    @Transactional
    public UserAccount insert(UserAccountNewDTO objDto) {
        if (userRepository.findByEmail(objDto.getEmail()) != null) {
            throw new DataIntegrityException("Email já registrado");
        }
        UserAccount obj = fromDTO(objDto);
        obj.setId(null);
        obj = userRepository.save(obj);
        return obj;
    }

    @Transactional
    public UserAccount update(UserAccount obj) {
        UserAccount newObj = find(obj.getId());
        updateData(newObj, obj);
        return userRepository.save(newObj);
    }

    public void delete(UUID id) {
        if (!userRepository.findById(id).get().getEvents().isEmpty()) {
            throw new DataIntegrityException("Não é possível excluir porque há eventos relacionados");
        }
        userRepository.delete(find(id));

    }

    public UserAccount fromDTO(UserAccountDTO objDto) {
        return new UserAccount(objDto.getEmail(), objDto.getUsername(), null);
    }

    public UserAccount fromDTO(UserAccountNewDTO objDto) {
        objDto.setPassword(passwordEncoder.encode(objDto.getPassword()));

        UserAccount userAccount = new UserAccount(objDto);
        Set<String> strRoles = objDto.getRoles();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(Roles.USER.getDescription())
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "ADMIN":
                        Role adminRole = roleRepository.findByName(Roles.ADMIN.getDescription())
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName(Roles.USER.getDescription())
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        userAccount.setRoles(roles);
        return userAccount;
    }

    private void updateData(UserAccount newObj, UserAccount obj) {
        newObj.setUsername(obj.getUsername());
        newObj.setEmail(obj.getEmail());
    }

    public UserAccount updatePassword(UserPasswordDTO userPasswordDTO) {
        UserPrincipal userAuthenticated = getUserAuthenticated();
        UserAccount userAccount = findByEmail(userAuthenticated.getEmail());

        userAccount.setPassword(passwordEncoder.encode(userPasswordDTO.getPassword()));

        return userRepository.save(userAccount);
    }

    public void updateUserEvents(UserAccount obj) {
        UserAccount newObj = findByEmail(obj.getEmail());
        newObj.setEvents(obj.getEvents());
        userRepository.save(newObj);
    }

    public static UserPrincipal getUserAuthenticated() {
        try {
            return (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (Exception e) {
            return null;
        }
    }
}