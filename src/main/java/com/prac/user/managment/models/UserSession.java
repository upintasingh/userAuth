package com.prac.user.managment.models;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class UserSession extends BaseModel{
    private String token;
    @ManyToOne
    private USer user;

}
