package com.indium.spring_boot_assignment.entity;

import jakarta.persistence.*;

// Innings.java
@Entity
@Table(name = "innings")
public class Innings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "innings_id")
    private Integer inningsId;

    @ManyToOne
    @JoinColumn(name = "match_id")
    private Match match;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    @Column(name = "overs")
    private Integer overs;

    @Column(name = "target_runs")
    private Integer targetRuns;

    public Integer getInningsId() {
        return inningsId;
    }

    public void setInningsId(Integer inningsId) {
        this.inningsId = inningsId;
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Integer getOvers() {
        return overs;
    }

    public void setOvers(Integer overs) {
        this.overs = overs;
    }

    public Integer getTargetRuns() {
        return targetRuns;
    }

    public void setTargetRuns(Integer targetRuns) {
        this.targetRuns = targetRuns;
    }
}

