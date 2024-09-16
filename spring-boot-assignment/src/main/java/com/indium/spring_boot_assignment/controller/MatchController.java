package com.indium.spring_boot_assignment.controller;
//192.168.1.28:8761
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.indium.spring_boot_assignment.entity.*;
import com.indium.spring_boot_assignment.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDate;

import java.io.IOException;
import java.util.*;
import org.slf4j.Logger;

@RestController
@RequestMapping("/api/matches")
@Tag(name = "IPL match details", description = "Endpoints to get the match details")
public class MatchController {

    @Autowired
    CricketMatchService cricketMatchService;

    @Autowired
    MatchService matchService;

    @Autowired
    PlayerService playerService;

    @Autowired
    ScoreService scoreService;

    @Autowired
    WicketService wicketService;

    @Autowired
    TeamService teamService;

    @Autowired
    OfficialService officialService;

    @Autowired
    StrikeRateService strikeRateService;

    @Autowired
    MatchScoreService matchScoreService;

    @Autowired
    TopWicketTakerService topWicketTakerService;

    @Autowired
    TopPlayersService topPlayersService;

    @Autowired
    KafkaTemplate kafkaTemplate;

    @Autowired
    ObjectMapper objectMapper;

    Logger logger = org.slf4j.LoggerFactory.getLogger(MatchController.class);

    private void sendKafkaMessage(String key, Object value) throws JsonProcessingException {
        Map<String, Object> message = new HashMap<>();
        message.put("key", key);
        message.put("value", value);
        String jsonMessage = objectMapper.writeValueAsString(message);
        kafkaTemplate.send("match-final-logs-topic", jsonMessage);
    }

    @PostMapping("/matches")
    public Match createMatch(@RequestBody String jsonContent) throws IOException {
        logger.info("Creating new match");
        Match savedMatch = cricketMatchService.insertMatchData(jsonContent);
        cricketMatchService.clearCache();
        matchScoreService.clearCache();
        matchService.clearCache();
        officialService.clearCache();
        playerService.clearCache();
        scoreService.clearCache();
        strikeRateService.clearCache();
        teamService.clearCache();
        topPlayersService.clearCache();
        topWicketTakerService.clearCache();
        wicketService.clearCache();
        String logMessage = savedMatch != null ? "Match created successfully" : "Failed to create match";
        logger.info(logMessage);
        sendKafkaMessage("storedMatch", savedMatch != null ? savedMatch : "Match is null");
        return savedMatch;
    }

    @Operation(summary = "Get match by ID", parameters = {
            @Parameter(name = "id", description = "Match ID", example = "1")
    })
    @ApiResponse(description = "Returns the match with the given ID", responseCode = "200")
    @GetMapping("/{id}")
    public Match getMatchById(@PathVariable Integer id) throws JsonProcessingException {
        Match match = matchService.getMatchById(id);
        String logMessage = match != null ? "Match found with ID: " + id : "No match found with ID: " + id;
        logger.info(logMessage);
        sendKafkaMessage("retrievedMatch", match != null ? match : logMessage);
        return match;
    }


    @Operation(summary = "Get matches by player name", parameters = {
            @Parameter(name = "playerName", description = "Player name", example = "Virat Kohli")
    })
    @ApiResponse(description = "Returns matches played by the specified player", responseCode = "200")
    @GetMapping("/player/{playerName}")
    public List<Match> getMatchesByPlayerName(@PathVariable String playerName) throws JsonProcessingException {
        logger.info("Fetching matches for player: {}", playerName);
        List<Match> matches = playerService.findAllMatchesByPlayerName(playerName);
        String logMessage = matches != null && !matches.isEmpty() ?
                "Found " + matches.size() + " matches for player: " + playerName :
                "No matches found for player: " + playerName;
        logger.info(logMessage);
        sendKafkaMessage("PlayerMatches", matches != null ? matches : "Player is null");
        return matches;
    }

    @Operation(summary = "Get cumulative score by batter name", parameters = {
            @Parameter(name = "batterName", description = "Batter name", example = "Sachin Tendulkar")
    })
    @ApiResponse(description = "Returns cumulative score of the specified batter", responseCode = "200")
    @GetMapping("/cumulative-score")
    public String getCumulativeScore(@RequestParam("batterName") String batterName) throws JsonProcessingException {
        String scores = scoreService.getCumulativeScore(batterName);
        String logMessage = scores != null ?
                "Cumulative score retrieved for batter " + batterName :
                "No cumulative score found for batter: " + batterName;
        logger.info(logMessage);
        sendKafkaMessage("cumulativeScore", scores != null ? scores : logMessage);
        return scores;
    }

    @Operation(summary = "Get wickets summary by bowler name", parameters = {
            @Parameter(name = "bowlerName", description = "Bowler name", example = "Anil Kumble")
    })
    @ApiResponse(description = "Returns wickets summary for the specified bowler", responseCode = "200")
    @GetMapping("/wickets")
    public String getWicketsSummaryByBowler(@RequestParam("bowlerName") String bowlerName) throws JsonProcessingException {
        logger.info("Fetching wickets summary for bowler: {}", bowlerName);
        String summary = wicketService.getWicketsSummaryByBowler(bowlerName);
        String logMessage = summary != null && !summary.isEmpty() ?
                "Wickets summary retrieved for bowler: " + bowlerName :
                "No wickets summary found for bowler: " + bowlerName;
        logger.info(logMessage);
        sendKafkaMessage("wicketsSummary", summary != null && !summary.isEmpty() ? summary : "Wickets summary for bowler is null");
        return summary;
    }

    @Operation(summary = "Get players info by team and match number", parameters = {
            @Parameter(name = "teamName", description = "Team name", example = "India"),
            @Parameter(name = "eventMatchNumber", description = "Event match number", example = "2")
    })
    @ApiResponse(description = "Returns players information for the specified team and match number", responseCode = "200")
    @GetMapping("/players")
    public List<List<String>> getPlayersInfo(@RequestParam String teamName, @RequestParam Integer eventMatchNumber) throws JsonProcessingException {
        logger.info("Fetching players info for team: {} and match number: {}", teamName, eventMatchNumber);
        List<List<String>> playerInfoList = teamService.getPlayersInfo(teamName, eventMatchNumber);
        String logMessage = playerInfoList != null && !playerInfoList.isEmpty() ?
                "Players info retrieved for team: " + teamName + " and match number: " + eventMatchNumber :
                "No players info found for team: " + teamName + " and match number: " + eventMatchNumber;
        logger.info(logMessage);
        sendKafkaMessage("playersInfo", playerInfoList != null && !playerInfoList.isEmpty() ?
                Map.of("teamName", teamName, "eventMatchNumber", eventMatchNumber, "playerInfo", playerInfoList) :
                "Player info is null");
        return playerInfoList;
    }

    @Operation(summary = "Get referees by match number", parameters = {
            @Parameter(name = "matchNumber", description = "Match number", example = "3")
    })
    @ApiResponse(description = "Returns referees for the specified match number", responseCode = "200")
    @GetMapping("/referees")
    public List<Official> getRefereesByMatchNumber(@RequestParam Integer matchNumber) throws JsonProcessingException {
        logger.info("Fetching referees for match number: {}", matchNumber);
        List<Official> officials = officialService.findRefereesByMatchNumber(matchNumber);
        String logMessage = officials != null && !officials.isEmpty() ?
                "Referees retrieved for match number: " + matchNumber :
                "No referees found for match number: " + matchNumber;
        logger.info(logMessage);
        sendKafkaMessage("referees", officials != null ? officials : "Referees name is null");
        return officials;
    }

    @Operation(summary = "Get strike rate by batter name and match number", parameters = {
            @Parameter(name = "batterName", description = "Batter name", example = "Rohit Sharma"),
            @Parameter(name = "matchNumber", description = "Match number", example = "5")
    })
    @ApiResponse(description = "Returns the strike rate of the specified batter in the specified match", responseCode = "200")
    @GetMapping("/strike-rate")
    public String getStrikeRate(@RequestParam String batterName, @RequestParam Integer matchNumber) throws JsonProcessingException {
        logger.info("Fetching strike rate for batter: {} in match number: {}", batterName, matchNumber);
        String strikeRate = strikeRateService.getStrikeRate(batterName, matchNumber);
        String logMessage = strikeRate != null && !strikeRate.isEmpty() ?
                "Strike rate retrieved for batter: " + batterName + " in match number: " + matchNumber :
                "No strike rate found for batter: " + batterName + " in match number: " + matchNumber;
        logger.info(logMessage);
        sendKafkaMessage("strikeRate", strikeRate != null && !strikeRate.isEmpty() ?
                Map.of("batterName", batterName, "matchNumber", matchNumber, "strikeRate", strikeRate) :
                "Strike rate is null or empty");
        return strikeRate;
    }

    @Operation(summary = "Get match scores by date", parameters = {
            @Parameter(name = "date", description = "Match date", example = "2024-09-13")
    })
    @ApiResponse(description = "Returns the match scores on the specified date", responseCode = "200")
    @GetMapping("/scores")
    public List<Map<String, Object>> getMatchScores(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate matchDate) throws JsonProcessingException {
        logger.info("Fetching match scores for date: {}", matchDate);
        List<Map<String, Object>> matchScores = matchScoreService.getMatchScoresByDate(matchDate);
        String logMessage = matchScores != null && !matchScores.isEmpty() ?
                "Match scores retrieved for date: " + matchDate :
                "No match scores found for date: " + matchDate;
        logger.info(logMessage);
        sendKafkaMessage("matchScores", matchScores != null && !matchScores.isEmpty() ?
                Map.of("date", matchDate, "scores", matchScores) :
                "Match scores are null or empty");
        return matchScores;
    }

    @Operation(summary = "Get top players by match", parameters = {
            @Parameter(name = "page", description = "Page number", example = "0"),
            @Parameter(name = "size", description = "Page size", example = "10")
    })
    @ApiResponse(description = "Returns a paginated list of top players by match", responseCode = "200")
    @GetMapping("/top-players")
    public Page<Map<String, Object>> getTopPlayersByMatch(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) throws JsonProcessingException {
        logger.info("Fetching top players by match for page: {} and size: {}", page, size);
        Pageable pageable = PageRequest.of(page, size);
        Page<Map<String, Object>> topPlayers = topPlayersService.getTopPlayersByMatch(pageable);
        logger.info("Retrieved top players by match for page: {} and size: {}", page, size);
        sendKafkaMessage("topPlayers", topPlayers != null ? topPlayers : "Top players is null");
        return topPlayers;
    }

    @Operation(summary = "Get top wicket takers", parameters = {
            @Parameter(name = "page", description = "Page number", example = "0"),
            @Parameter(name = "size", description = "Page size", example = "10")
    })
    @ApiResponse(description = "Returns a paginated list of top wicket takers", responseCode = "200")
    @GetMapping("/top-wicket-takers")
    public Page<Map<String, Object>> getTopWicketTakers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) throws JsonProcessingException {
        logger.info("Fetching top wicket takers for page: {} and size: {}", page, size);
        Pageable pageable = PageRequest.of(page, size);
        Page<Map<String, Object>> topWicketTakers = topWicketTakerService.getTopWicketTakers(pageable);
        logger.info("Retrieved top wicket takers for page: {} and size: {}", page, size);
        sendKafkaMessage("topWicketTakers", topWicketTakers != null ? topWicketTakers : "Top wicket takers is null");
        return topWicketTakers;
    }

//    @GetMapping("/{id}")
//    public Match getMatchById(@PathVariable Integer id) throws JsonProcessingException {
//        Match match = matchService.getMatchById(id);
//
//        // Log to Kafka
//        if (match != null) {
//            String matchJson = objectMapper.writeValueAsString(match);
//            kafkaTemplate.send("match-logs-topic", "Retrieved match with ID: " + matchJson);
//        } else {
//            kafkaTemplate.send("match-logs-topic", "No match found with ID: " + id);
//        }
//
//        return match;
//    }
//
//    @PostMapping("/matches")
//    public Match createMatch(@RequestBody String jsonContent) throws IOException {
//        Match savedMatch = cricketMatchService.insertMatchData(jsonContent);
//        cricketMatchService.clearCache();
//        if (savedMatch != null) {
//            String matchJson = objectMapper.writeValueAsString(savedMatch);
//            kafkaTemplate.send("match-logs-topic", "Stored match data" + matchJson);
//        } else {
//            kafkaTemplate.send("match-logs-topic", " Match is null");
//        }
//        return savedMatch;
//    }
//
//    @GetMapping("/matches/player/{playerName}")
//    public List<Match> getMatchesByPlayerName(@PathVariable String playerName) throws JsonProcessingException {
//        List<Match> matches = playerService.findAllMatchesByPlayerName(playerName);
//        if (matches != null) {
//            String matchJson = objectMapper.writeValueAsString(matches);
//            kafkaTemplate.send("match-logs-topic", "Stored player data" + matchJson);
//        } else {
//            kafkaTemplate.send("match-logs-topic", " Player is null");
//        }
//        return matches;
//    }
//
//    @GetMapping("/cumulative-score")
//    public String getCumulativeScore(@RequestParam("batterName") String batterName) throws JsonProcessingException {
//        String scores = scoreService.getCumulativeScore(batterName);
//        if (scores != null) {
//            String matchJson = objectMapper.writeValueAsString(scores);
//            kafkaTemplate.send("match-logs-topic", "Stored score of batter name data" + matchJson);
//        } else {
//            kafkaTemplate.send("match-logs-topic", " Score of batter name is null");
//        }
//        return scores;
//    }
//
//    @GetMapping("/wickets")
//    public String getWicketsSummaryByBowler(@RequestParam("bowlerName") String bowlerName) throws JsonProcessingException {
//        String summary = wicketService.getWicketsSummaryByBowler(bowlerName);
//        if (summary != null && !summary.isEmpty()) {
//            String summaryJson = objectMapper.writeValueAsString(summary);
//            kafkaTemplate.send("match-logs-topic", "Stored wickets summary for bowler: " + summaryJson);
//        } else {
//            kafkaTemplate.send("match-logs-topic", "Wickets summary for bowler is null");
//        }
//        return summary;
//    }
//
//    @GetMapping("/players")
//    public List<List<String>> getPlayersInfo(@RequestParam String teamName, @RequestParam Integer eventMatchNumber) throws JsonProcessingException {
//        List<List<String>> playerInfoList = teamService.getPlayersInfo(teamName, eventMatchNumber);
//        if (playerInfoList != null && !playerInfoList.isEmpty()) {
//            String playerInfoJson = objectMapper.writeValueAsString(playerInfoList);
//            kafkaTemplate.send("match-logs-topic", "Stored players info for team: " + teamName + ", match number: " + eventMatchNumber + " | Data: " + playerInfoJson);
//        } else {
//            kafkaTemplate.send("match-logs-topic", "Player info is null");
//        }
//
//        return playerInfoList;
//    }
//
//
//    @GetMapping("/referees")
//    public List<Official> getRefereesByMatchNumber(@RequestParam Integer matchNumber) throws JsonProcessingException {
//        List<Official> officials = officialService.findRefereesByMatchNumber(matchNumber);
//        if (officials != null) {
//            String matchJson = objectMapper.writeValueAsString(officials);
//            kafkaTemplate.send("match-logs-topic", "Stored referees data" + matchJson);
//        } else {
//            kafkaTemplate.send("match-logs-topic", " Referees name is null");
//        }
//        return officials;
//    }
//
//    @GetMapping("/strike-rate")
//    public String getStrikeRate(@RequestParam String batterName, @RequestParam Integer matchNumber) throws JsonProcessingException {
//        String strikeRate = strikeRateService.getStrikeRate(batterName, matchNumber);
//
//        if (strikeRate != null && !strikeRate.isEmpty()) {
//            String strikeRateJson = objectMapper.writeValueAsString(strikeRate);
//            kafkaTemplate.send("match-logs-topic", "Stored strike rate for batter: " + batterName + ", match number: " + matchNumber + " | Data: " + strikeRateJson);
//        } else {
//            kafkaTemplate.send("match-logs-topic", "Strike rate is null or empty for batter: " + batterName + ", match number: " + matchNumber);
//        }
//
//        return strikeRate;
//    }
//
//    @GetMapping("/scores")
//    public List<Map<String, Object>> getMatchScores(
//            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate matchDate) throws JsonProcessingException {
//
//        List<Map<String, Object>> matchScores = matchScoreService.getMatchScoresByDate(matchDate);
//
//        if (matchScores != null && !matchScores.isEmpty()) {
//            String matchScoresJson = objectMapper.writeValueAsString(matchScores);
//            kafkaTemplate.send("match-logs-topic", "Stored match scores for date: " + matchDate + " | Data: " + matchScoresJson);
//        } else {
//            kafkaTemplate.send("match-logs-topic", "Match scores are null or empty for date: " + matchDate);
//        }
//
//        return matchScores;
//    }

}


//    @PostMapping("/upload")
//    public ResponseEntity<Match> uploadMatchData(@RequestBody String jsonContent) throws IOException {
//            Match savedMatch = matchService.insertMatchData(jsonContent);
//            List<Team> savedTeam = teamService.insertTeamData(jsonContent,savedMatch);
//            //playerService.insertPlayerData(jsonContent);
//            //MatchPlayer savedMatchPlayer = matchPlayerService.insertMatchPlayerData(savedMatch,savedTeam,savedTeam);
//            return ResponseEntity.ok(savedMatch);
//    }