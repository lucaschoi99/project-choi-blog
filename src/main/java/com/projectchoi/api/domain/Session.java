package com.projectchoi.api.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import java.util.UUID;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
public class Session {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String accessToken;

    @ManyToOne
    private Users user;

    @Builder
    public Session(Users users) {
        this.accessToken = UUID.randomUUID().toString();
        this.user = users;
    }
}
