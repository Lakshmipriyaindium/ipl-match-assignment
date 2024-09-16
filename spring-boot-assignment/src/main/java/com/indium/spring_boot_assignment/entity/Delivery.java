package com.indium.spring_boot_assignment.entity;

import com.indium.spring_boot_assignment.entity.Innings;
import jakarta.persistence.*;

@Entity
@Table(name = "deliveries")
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "delivery_id")
    private Integer deliveryId;

    @ManyToOne
    @JoinColumn(name = "innings_id")
    private Innings innings;

    @ManyToOne
    @JoinColumn(name = "match_id")
    private Match match;

    @Column(name = "over_number")
    private Integer overNumber;

    @Column(name = "ball_number")
    private Integer ballNumber;

    @Column(name = "batter_name")
    private String batterName;

    @Column(name = "bowler_name")
    private String bowlerName;

    @Column(name = "non_striker_name")
    private String nonStrikerName;

    @Column(name = "runs_batter")
    private Integer runsBatter;

    @Column(name = "extras")
    private Integer extras;

    @Column(name = "total_runs")
    private Integer totalRuns;

    @Column(name = "wicket_kind")
    private String wicketKind;

    @Column(name = "wicket_player_out_name")
    private String wicketPlayerOutName;

    @Column(name = "wicket_fielder_name")
    private String wicketFielderName;

    public Integer getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(Integer deliveryId) {
        this.deliveryId = deliveryId;
    }

    public Innings getInnings() {
        return innings;
    }

    public void setInnings(Innings innings) {
        this.innings = innings;
    }

    public Integer getOverNumber() {
        return overNumber;
    }

    public void setOverNumber(Integer overNumber) {
        this.overNumber = overNumber;
    }

    public Integer getBallNumber() {
        return ballNumber;
    }

    public void setBallNumber(Integer ballNumber) {
        this.ballNumber = ballNumber;
    }

    public String getBatterName() {
        return batterName;
    }

    public void setBatterName(String batterName) {
        this.batterName = batterName;
    }

    public String getBowlerName() {
        return bowlerName;
    }

    public void setBowlerName(String bowlerName) {
        this.bowlerName = bowlerName;
    }

    public String getNonStrikerName() {
        return nonStrikerName;
    }

    public void setNonStrikerName(String nonStrikerName) {
        this.nonStrikerName = nonStrikerName;
    }

    public Integer getRunsBatter() {
        return runsBatter;
    }

    public void setRunsBatter(Integer runsBatter) {
        this.runsBatter = runsBatter;
    }

    public Integer getExtras() {
        return extras;
    }

    public void setExtras(Integer extras) {
        this.extras = extras;
    }

    public Integer getTotalRuns() {
        return totalRuns;
    }

    public void setTotalRuns(Integer totalRuns) {
        this.totalRuns = totalRuns;
    }

    public String getWicketPlayerOutName() {
        return wicketPlayerOutName;
    }

    public void setWicketPlayerOutName(String wicketPlayerOutName) {
        this.wicketPlayerOutName = wicketPlayerOutName;
    }

    public String getWicketKind() {
        return wicketKind;
    }

    public void setWicketKind(String wicketKind) {
        this.wicketKind = wicketKind;
    }

    public String getWicketFielderName() {
        return wicketFielderName;
    }

    public void setWicketFielderName(String wicketFielderName) {
        this.wicketFielderName = wicketFielderName;
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }
}
