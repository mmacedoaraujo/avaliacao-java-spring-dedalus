package com.mmacedoaraujo.userapi.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import jakarta.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "table_users")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(hidden = true)
    private Long id;
    @NotEmpty(message = "the field 'name' cannot be empty")
    @Schema( type = "string", example = "Clark")
    private String name;
    @NotEmpty(message = "the field 'lastName' cannot be empty")
    @Schema( type = "string", example = "Kent")
    private String lastName;
    @NotNull(message = "the field 'birthDate' cannot be empty")
    @Schema( type = "LocalDate", example = "yyyy-mm-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate birthDate;
    @NotEmpty(message = "the field 'nationality' cannot be empty")
    @Schema( type = "string", example = "Brazillian")
    private String nationality;
}
