package com.fullstackbackend.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Proxy;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;

@Entity
@Setter
@Getter
@Proxy(lazy = false)
public class User {
    @Id
    @GeneratedValue
    private Integer id;

    @NotNull
    private String name;
    @Pattern(regexp = "^(0?[1-9]|[12][0-9]|3[01]).(0?[1-9]|1[012]).((19|20)\\\\d\\\\d)$")
    private Date date_of_birth;

    @NotNull
    @Length(min = 8, max = 500)
    private String password;
}
