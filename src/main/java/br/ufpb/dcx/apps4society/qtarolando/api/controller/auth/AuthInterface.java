package br.ufpb.dcx.apps4society.qtarolando.api.controller.auth;

import br.ufpb.dcx.apps4society.qtarolando.api.dto.CredentialsDTO;
import br.ufpb.dcx.apps4society.qtarolando.api.dto.UserAccountNewDTO;
import br.ufpb.dcx.apps4society.qtarolando.api.dto.UserInfoResponse;
import br.ufpb.dcx.apps4society.qtarolando.api.response.LoginResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

public interface AuthInterface {

    @Operation(summary = "Login é usado para o usuário entrar no sistema",
            description = "Feito isso ele poderá realizar operações que precisam de alguma autenticação",
            tags = {"auth"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação feita com sucesso"),
            @ApiResponse(responseCode = "400", description = "Quando o email ou a senha estão incorretos")
    })
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody CredentialsDTO credentials);


    @Operation(summary = "Sign Up é usado para um novo usuário se cadastrar",
            description = "O novo usuário não pode ter o mesmo email que um usuário já cadastrado",
            tags = {"auth"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Operação feita com sucesso"),
            @ApiResponse(responseCode = "400", description = "Quando um email já está cadastrado ou uma senha não contem 8 caracteres")
    })
    public ResponseEntity<Void> signUp(@Valid @RequestBody UserAccountNewDTO objDto);


    @Operation(summary = "Sign out é usado para o usuário se deslogar do sistema",
            tags = {"auth"})
    @ApiResponse(responseCode = "200", description = "Operação feita com sucesso")
    public ResponseEntity<?> logoutUser();
}
