package com.indium.spring_boot_assignment.service;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.indium.spring_boot_assignment.entity.*;
import com.indium.spring_boot_assignment.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class CricketMatchService {

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private PlayersRepository playersRepository;

    @Autowired
    private OfficialRepository officialRepository;

    @Autowired
    private InningsRepository inningsRepository;

    @Autowired
    private DeliveryRepository deliveryRepository;

    @Autowired
    private ExtraRepository extraRepository;

    @Autowired
    private PowerPlayRepository powerPlayRepository;

    private final ObjectMapper objectMapper;

    public CricketMatchService() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }


    @Transactional
    @Cacheable(value = "matchdetailsCache", key = "#jsonContent")
    public Match insertMatchData(String jsonContent) throws IOException {
        JsonNode rootNode = objectMapper.readTree(jsonContent);

        // Create and save the Match entity
        Match match = new Match();
        match.setDataVersion(rootNode.path("meta").path("data_version").asText());
        match.setCreated(LocalDate.parse(rootNode.path("meta").path("created").asText()));
        match.setRevision(rootNode.path("meta").path("revision").asInt());

        JsonNode infoNode = rootNode.path("info");
        match.setCity(infoNode.path("city").asText());
        match.setDate(LocalDate.parse(infoNode.path("dates").get(0).asText()));
        match.setEventName(infoNode.path("event").path("name").asText());
        match.setEventMatchNumber(infoNode.path("event").path("match_number").asInt());
        match.setGender(infoNode.path("gender").asText());
        match.setMatchType(infoNode.path("match_type").asText());
        match.setOvers(infoNode.path("overs").asInt());
        match.setPlayerOfMatch(infoNode.path("player_of_match").get(0).asText());
        match.setWinner(infoNode.path("outcome").path("winner").asText());
        match.setWinByRuns(infoNode.path("outcome").path("by").path("runs").asInt());
        match.setVenue(infoNode.path("venue").asText());
        match.setTossWinner(infoNode.path("toss").path("winner").asText());
        match.setTossDecision(infoNode.path("toss").path("decision").asText());

        Match savedMatch = matchRepository.save(match);

        // Save Teams
        JsonNode teamsNode = infoNode.path("teams");
        List<Team> teams = new ArrayList<>();
        for (JsonNode teamName : teamsNode) {
            Team team = new Team();
            team.setMatch(match);
            team.setTeamName(teamName.asText());
            teams.add(teamRepository.save(team));
        }

        // Save Players
        JsonNode playersNode = infoNode.path("players");
        List<Players> playersList = new ArrayList<>();
        playersNode.fields().forEachRemaining(entry -> {
            String teamName = entry.getKey();
            Team team = teams.stream().filter(t -> t.getTeamName().equals(teamName)).findFirst().orElse(null);
            if (team != null) {
                for (JsonNode playerNameNode : entry.getValue()) {
                    String playerName = playerNameNode.asText();

                    // Create the Players entity directly
                    Players playerMatch = new Players();
                    playerMatch.setTeam(team);
                    playerMatch.setMatch(savedMatch);  // Use savedMatch which is effectively final
                    playerMatch.setPlayerName(playerName);  // Set the player name directly in Players entity
                    playersList.add(playersRepository.save(playerMatch));
                }
            }
        });


        // Save Officials
        JsonNode officialsNode = infoNode.path("officials");
        List<Official> officials = new ArrayList<>();
        officialsNode.fields().forEachRemaining(entry -> {
            String role = entry.getKey();
            entry.getValue().forEach(nameNode -> {
                Official official = new Official();
                official.setMatch(match);
                official.setRole(role);
                official.setName(nameNode.asText());
                officials.add(officialRepository.save(official));
            });
        });

        // Save Innings
        JsonNode inningsNode = rootNode.path("innings");
        List<Innings> inningsList = new ArrayList<>();
        for (JsonNode inningNode : inningsNode) {
            Innings inning = new Innings();
            inning.setMatch(match);

            String teamName = inningNode.path("team").asText();
            Team team = teams.stream().filter(t -> t.getTeamName().equals(teamName)).findFirst().orElse(null);
            inning.setTeam(team);

            inning.setOvers(inningNode.path("overs").size());

            JsonNode targetNode = inningNode.path("target");
            Integer targetRuns = targetNode.path("runs").asInt();
            inning.setTargetRuns(targetRuns);
            inningsList.add(inningsRepository.save(inning));


// Save Deliveries
            List<Delivery> deliveriesList = new ArrayList<>();
            for (JsonNode overNode : inningNode.path("overs")) {
                int overNumber = overNode.path("over").asInt();
                for (JsonNode deliveryNode : overNode.path("deliveries")) {
                    Delivery delivery = new Delivery();
                    delivery.setMatch(match);
                    delivery.setInnings(inning);
                    delivery.setOverNumber(overNumber);
// Accessing simple fields
                    delivery.setBallNumber(deliveryNode.path("ball").asInt());
                    delivery.setBatterName(deliveryNode.path("batter").asText());
                    delivery.setBowlerName(deliveryNode.path("bowler").asText());
                    delivery.setNonStrikerName(deliveryNode.path("non_striker").asText());
                    delivery.setRunsBatter(deliveryNode.path("runs").path("batter").asInt());
                    delivery.setExtras(deliveryNode.path("runs").path("extras").asInt());
                    delivery.setTotalRuns(deliveryNode.path("runs").path("total").asInt());

                    if (deliveryNode.has("wickets")) {
                        JsonNode wicketsNode = deliveryNode.path("wickets");

                        for (JsonNode wicketNode : wicketsNode) {
                            delivery.setWicketKind(wicketNode.path("kind").asText(null));
                            delivery.setWicketPlayerOutName(wicketNode.path("player_out").asText(null));

                            JsonNode fieldersNode = wicketNode.path("fielders");
                            if (fieldersNode.isArray() && fieldersNode.size() > 0) {
                                delivery.setWicketFielderName(fieldersNode.get(0).path("name").asText(null));
                            }
                        }
                    }

                    delivery = deliveryRepository.save(delivery);
                    deliveriesList.add(delivery);


                    // Save Extras if present
                    if (deliveryNode.has("extras")) {
                        Extra extra = new Extra();
                        extra.setDelivery(delivery);
                        extra.setMatch(match);
                        extra.setLegbyes(deliveryNode.path("extras").path("legbyes").asInt(0));
                        extra.setWides(deliveryNode.path("extras").path("wides").asInt(0));
                        extraRepository.save(extra);
                    }
                }
            }

            // Save Powerplays
            JsonNode powerplaysNode = inningsNode.get(0).path("powerplays");
            List<PowerPlay> powerplaysList = new ArrayList<>();
            for (JsonNode powerplayNode : powerplaysNode) {
                PowerPlay powerplay = new PowerPlay();
                powerplay.setMatch(match);
                powerplay.setInnings(inningsList.get(0));  // Assuming powerplays are associated with the first innings

                // Convert from and to over values to integers
                powerplay.setFromOver(powerplayNode.path("from").asInt());
                powerplay.setToOver(powerplayNode.path("to").asInt());

                powerplay.setType(powerplayNode.path("type").asText());
                powerplaysList.add(powerPlayRepository.save(powerplay));
            }

        }
        return match;
    }

    @CacheEvict(value = {"matchdetailsCache"}, allEntries = true)
    public void clearCache() {

    }

}

