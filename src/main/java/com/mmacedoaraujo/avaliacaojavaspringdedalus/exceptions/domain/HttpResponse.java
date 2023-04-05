package com.mmacedoaraujo.avaliacaojavaspringdedalus.exceptions.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class HttpResponse {

    private Long timestamp;
    private int status;
    private String error;
    private String message;
}
