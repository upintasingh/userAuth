package com.prac.user.managment.models;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class USer extends BaseModel {

    private String name;
    private String email;
    private String password;
    private String phoneNumber;
    @ManyToMany
    private List<Roles> roles = new ArrayList<>();

}
