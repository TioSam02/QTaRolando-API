package br.ufpb.dcx.apps4society.qtarolando.api.dto;

import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserRoleDTO {

  private UUID userId;

  private List<Integer> idsRoles;

}