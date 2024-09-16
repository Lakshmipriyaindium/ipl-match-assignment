package com.indium.spring_boot_assignment.entity;

import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.ToString;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "matchs")
@Data
@ToString
public class Match implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "match_id")
    private Integer matchId;

    @Column(name = "data_version")
    private String dataVersion;

    @Column(name = "created")
    private LocalDate created;

    @Column(name = "revision")
    private Integer revision;

    @Column(name = "city")
    private String city;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "event_name")
    private String eventName;

    @Column(name = "event_match_number")
    private Integer eventMatchNumber;

    @Column(name = "gender")
    private String gender;

    @Column(name = "match_type")
    private String matchType;

    @Column(name = "overs")
    private Integer overs;

    @Column(name = "player_of_match")
    private String playerOfMatch;

    @Column(name = "winner")
    private String winner;

    @Column(name = "win_by_runs")
    private Integer winByRuns;

    @Column(name = "venue")
    private String venue;

    @Column(name = "toss_winner")
    private String tossWinner;

    @Column(name = "toss_decision")
    private String tossDecision;

    public Integer getMatchId() {
        return matchId;
    }

    public void setMatchId(Integer matchId) {
        this.matchId = matchId;
    }

    public String getDataVersion() {
        return dataVersion;
    }

    public void setDataVersion(String dataVersion) {
        this.dataVersion = dataVersion;
    }

    public LocalDate getCreated() {
        return created;
    }

    public void setCreated(LocalDate created) {
        this.created = created;
    }

    public Integer getRevision() {
        return revision;
    }

    public void setRevision(Integer revision) {
        this.revision = revision;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public Integer getEventMatchNumber() {
        return eventMatchNumber;
    }

    public void setEventMatchNumber(Integer eventMatchNumber) {
        this.eventMatchNumber = eventMatchNumber;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMatchType() {
        return matchType;
    }

    public void setMatchType(String matchType) {
        this.matchType = matchType;
    }

    public Integer getOvers() {
        return overs;
    }

    public void setOvers(Integer overs) {
        this.overs = overs;
    }

    public String getPlayerOfMatch() {
        return playerOfMatch;
    }

    public void setPlayerOfMatch(String playerOfMatch) {
        this.playerOfMatch = playerOfMatch;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public Integer getWinByRuns() {
        return winByRuns;
    }

    public void setWinByRuns(Integer winByRuns) {
        this.winByRuns = winByRuns;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getTossWinner() {
        return tossWinner;
    }

    public void setTossWinner(String tossWinner) {
        this.tossWinner = tossWinner;
    }

    public String getTossDecision() {
        return tossDecision;
    }

    public void setTossDecision(String tossDecision) {
        this.tossDecision = tossDecision;
    }

}
