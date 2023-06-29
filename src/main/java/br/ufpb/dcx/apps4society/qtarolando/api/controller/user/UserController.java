package br.ufpb.dcx.apps4society.qtarolando.api.controller.user;

import br.ufpb.dcx.apps4society.qtarolando.api.dto.CreateUserRoleDTO;
import br.ufpb.dcx.apps4society.qtarolando.api.dto.UserAccountDTO;
import br.ufpb.dcx.apps4society.qtarolando.api.dto.UserPasswordDTO;
import br.ufpb.dcx.apps4society.qtarolando.api.model.UserAccount;
import br.ufpb.dcx.apps4society.qtarolando.api.service.CreateRoleUserService;
import br.ufpb.dcx.apps4society.qtarolando.api.service.UserAccountService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "api/users")
@Log4j2
public class UserController implements UserInterface {

    @Autowired
    private UserAccountService service;

    @Autowired
    CreateRoleUserService createRoleUserService;

    @Override
    @GetMapping(value = "/page")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<Page<UserAccountDTO>> findPage(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "pageSize", defaultValue = "24") Integer pageSize) {

        Page<UserAccount> list = service.findPage(page, pageSize);
        Page<UserAccountDTO> listDto = list.map(obj -> new UserAccountDTO(obj));
        return ResponseEntity.ok().body(listDto);
    }

    //org.springframework.http.converter.HttpMessageNotWritableException: Could not write JSON: Unable to access lob stream
    // problema no banco Postgres quando pesquisa um user que tem um event associado a ele que não acontece no H2
    @Override
    @GetMapping(value = "/{id}")
    public ResponseEntity<UserAccount> findById(@PathVariable String id) {
        UserAccount obj = service.find(UUID.fromString(id));
        return ResponseEntity.ok().body(obj);
    }

    @Override
    @GetMapping(value = "/email")
    public ResponseEntity<UserAccount> findByEmail(@RequestParam(value = "value") String email) {
        UserAccount obj = service.findByEmail(email);
        return ResponseEntity.ok().body(obj);
    }

    //TODO: consertar erro
    //NonUniqueResultException: query did not return a unique result: 2
    @Override
    @GetMapping(value = "/username")
    public UserAccount findByUsername(@RequestParam(value = "value") String userName) {
        UserAccount obj = service.findByUsername(userName);
        return obj;
    }

    //TODO:
    //O user pode mudar as roles de outros users?
    //Está mudando a role mesmo sem ter um user logado
    @Override
    @PostMapping("/role")
    public UserAccount role(@RequestBody CreateUserRoleDTO createUserRoleDTO) {
        return createRoleUserService.execute(createUserRoleDTO);
    }

    @Override
    @PutMapping(value = "/{id}")
    public ResponseEntity<Void> update(@Valid @RequestBody UserAccountDTO objDto, @PathVariable String id) {
        UserAccount obj = service.fromDTO(objDto);
        obj.setId(UUID.fromString(id));
        service.update(obj);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PatchMapping(value = "/password")
    public ResponseEntity<Void> updatePassword(@Valid @RequestBody UserPasswordDTO userPasswordDTO) {
        service.updatePassword(userPasswordDTO);
        return ResponseEntity.noContent().build();
    }

    @Override
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(UUID.fromString(id));
        return ResponseEntity.noContent().build();
    }
}
