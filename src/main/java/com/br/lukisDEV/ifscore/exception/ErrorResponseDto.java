package com.br.lukisDEV.ifscore.exception;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorResponseDto {
    private String message;
    private Integer status;
}
