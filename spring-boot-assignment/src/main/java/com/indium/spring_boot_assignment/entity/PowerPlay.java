package com.indium.spring_boot_assignment.entity;

import jakarta.persistence.*;

// Powerplay.java
@Entity
@Table(name = "powerplay")
public class PowerPlay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "powerplay_id")
    private Integer powerplayId;

    @ManyToOne
    @JoinColumn(name = "match_id")
    private Match match;

    @ManyToOne
    @JoinColumn(name = "innings_id")
    private Innings innings;

    @Column(name = "from_over")
    private Integer fromOver;  // Changed from BigDecimal to Integer

    @Column(name = "to_over")
    private Integer toOver;    // Changed from BigDecimal to Integer

    @Column(name = "type")
    private String type;

    public Integer getPowerplayId() {
        return powerplayId;
    }

    public void setPowerplayId(Integer powerplayId) {
        this.powerplayId = powerplayId;
    }

    public Innings getInnings() {
        return innings;
    }

    public void setInnings(Innings innings) {
        this.innings = innings;
    }

    public Integer getFromOver() {
        return fromOver;
    }

    public void setFromOver(Integer fromOver) {
        this.fromOver = fromOver;
    }

    public Integer getToOver() {
        return toOver;
    }

    public void setToOver(Integer toOver) {
        this.toOver = toOver;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }
}