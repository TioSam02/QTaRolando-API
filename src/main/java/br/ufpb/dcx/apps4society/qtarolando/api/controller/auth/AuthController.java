package br.ufpb.dcx.apps4society.qtarolando.api.controller.auth;

import br.ufpb.dcx.apps4society.qtarolando.api.dto.CredentialsDTO;
import br.ufpb.dcx.apps4society.qtarolando.api.dto.UserAccountNewDTO;
import br.ufpb.dcx.apps4society.qtarolando.api.response.LoginResponse;
import br.ufpb.dcx.apps4society.qtarolando.api.security.jwt.JWTUtils;
import br.ufpb.dcx.apps4society.qtarolando.api.service.JWTService;
import br.ufpb.dcx.apps4society.qtarolando.api.service.UserAccountService;
import br.ufpb.dcx.apps4society.qtarolando.api.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("api/auth")
public class AuthController implements AuthInterface {

    @Autowired
    private UserAccountService service;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private JWTService jwtService;

    @Override
    @PostMapping(value = "/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody CredentialsDTO credentialsDTO) {
        try {
            return new ResponseEntity<LoginResponse>(jwtService.authenticate(credentialsDTO), HttpStatus.OK);
        } catch (ObjectNotFoundException exception) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @Override
    @PostMapping(value = "/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> signUp(@Valid @RequestBody UserAccountNewDTO objDto) {
        service.insert(objDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Override
    @PostMapping("/signout")
    public ResponseEntity<?> logoutUser() {
        ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).build();
    }
}
