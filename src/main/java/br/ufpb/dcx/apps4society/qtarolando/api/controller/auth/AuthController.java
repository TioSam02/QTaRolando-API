package br.ufpb.dcx.apps4society.qtarolando.api.controller.auth;

import br.ufpb.dcx.apps4society.qtarolando.api.dto.CredentialsDTO;
import br.ufpb.dcx.apps4society.qtarolando.api.dto.UserAccountNewDTO;
import br.ufpb.dcx.apps4society.qtarolando.api.dto.UserInfoResponse;
import br.ufpb.dcx.apps4society.qtarolando.api.security.UserPrincipal;
import br.ufpb.dcx.apps4society.qtarolando.api.security.jwt.JWTUtils;
import br.ufpb.dcx.apps4society.qtarolando.api.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

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
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody CredentialsDTO credentials) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(credentials.getEmail(), credentials.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserPrincipal user = (UserPrincipal) authentication.getPrincipal();

        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(user);

        List<String> roles = user.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .body(new UserInfoResponse(user.getId(),
                        user.getUsername(),
                        user.getEmail(),
                        roles));
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
