package com.aviatickets.notifier.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "notifier_users")
@Getter
@Setter
public class NotifierUser {

    @Id
    private Long id;

    private String email;


}
