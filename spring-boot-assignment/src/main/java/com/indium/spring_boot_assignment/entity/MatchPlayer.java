package com.indium.spring_boot_assignment.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "matchplayer")
public class MatchPlayer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "match_player_id")
    private Integer matchPlayerId;

    @ManyToOne
    @JoinColumn(name = "match_id", nullable = false)
    private Match match;

    @ManyToOne
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @ManyToOne
    @JoinColumn(name = "player_id", nullable = false)
    private Players players;

    // Getters and Setters

    public Integer getMatchPlayerId() {
        return matchPlayerId;
    }

    public void setMatchPlayerId(Integer matchPlayerId) {
        this.matchPlayerId = matchPlayerId;
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

    public Players getPlayer() {
        return players;
    }

    public void setPlayer(Players player) {
        this.players = player;
    }
}

