package com.br.lukisDEV.ifscore.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class UserDto {
    String nome;
    String email;
    String senha;
}
