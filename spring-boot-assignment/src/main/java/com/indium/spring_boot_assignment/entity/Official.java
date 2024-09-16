package com.indium.spring_boot_assignment.entity;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "official")
public class Official implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "official_id")
    private Integer officialId;

    @ManyToOne
    @JoinColumn(name = "match_id")
    private Match match;

    @Column(name = "role")
    private String role;

    @Column(name = "name")
    private String name;

    public Integer getOfficialId() {
        return officialId;
    }

    public void setOfficialId(Integer officialId) {
        this.officialId = officialId;
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}



