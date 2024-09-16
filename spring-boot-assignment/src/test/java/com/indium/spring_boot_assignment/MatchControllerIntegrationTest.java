package com.indium.spring_boot_assignment;

import com.indium.spring_boot_assignment.entity.Match;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.post;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MatchControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private TestRestTemplate restTemplate;

    private String baseUrl;

    @Test
    public void testGetMatchById() {
        // Assuming a match with ID 1 exists in your test database
        String url = "http://localhost:" + port + "/api/matches/1";
        Match response = testRestTemplate.getForObject(url, Match.class);
        assertNotNull(response);
        assertEquals(1, response.getMatchId()); // Adjust according to your Match class properties
    }

    @Test
    public void testCreateMatch() throws Exception {
        String url = "http://localhost:" + port + "/api/matches/matches";
        String jsonData = new String(Files.readAllBytes(Paths.get("src/test/resources/SampleDataMatch.json")));
        ResponseEntity<Match> response = testRestTemplate.postForEntity(url, jsonData, Match.class);
        System.out.println("Response Status Code: " + response.getStatusCode());
        System.out.println("Response Body: " + response.getBody());
        assertThat(response.getStatusCode())
                .isIn(HttpStatus.CREATED, HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        Match createdMatch = response.getBody();
    }

    @Test
    public void testGetMatchesByPlayerName() {
        // Assuming a player named "Virat Kohli" exists in your test database
        String url = "http://localhost:" + port + "/api/matches/player/Virat%20Kohli";
        List<Match> response = testRestTemplate.getForObject(url, List.class);
        assertNotNull(response);
        // Add more assertions based on expected response
    }

    @Test
    public void testGetCumulativeScore() {
        String url = "http://localhost:" + port + "/api/matches/cumulative-score?batterName=Sachin%20Tendulkar";
        String response = testRestTemplate.getForObject(url, String.class);
        assertNotNull(response);
        // Add more assertions based on expected response
    }

    @Test
    public void testGetWicketsSummaryByBowler() {
        String url = "http://localhost:" + port + "/api/matches/wickets?bowlerName=Anil%20Kumble";
        String response = testRestTemplate.getForObject(url, String.class);
        assertNotNull(response);
        // Add more assertions based on expected response
    }

    @Test
    public void testGetPlayersInfo() {
        String url = "http://localhost:" + port + "/api/matches/players?teamName=India&eventMatchNumber=2";
        List<List<String>> response = testRestTemplate.getForObject(url, List.class);
        assertNotNull(response);
        // Add more assertions based on expected response
    }

    @Test
    public void testGetRefereesByMatchNumber() {
        String url = "http://localhost:" + port + "/api/matches/referees?matchNumber=3";
        List<Object> response = testRestTemplate.getForObject(url, List.class);
        assertNotNull(response);
        assertFalse(response.isEmpty());
    }

    @Test
    public void testGetStrikeRate() {
        String url = "http://localhost:" + port + "/api/matches/strike-rate?batterName=Rohit%20Sharma&matchNumber=5";
        String response = testRestTemplate.getForObject(url, String.class);
        assertNotNull(response);
        // Add more assertions based on expected response
    }

    @Test
    public void testGetMatchScores() {
        String url = "http://localhost:" + port + "/api/matches/scores?date=2024-09-13";
        List<Map<String, Object>> response = testRestTemplate.getForObject(url, List.class);
        assertNotNull(response);
        // Add more assertions based on expected response
    }

    @Test
    public void testGetTopPlayersByMatch() {
        String url = "http://localhost:" + port + "/api/matches/top-players?page=0&size=10";
        Map<String, Object> response = testRestTemplate.getForObject(url, Map.class);
        assertNotNull(response);
        // Add more assertions based on expected response
    }

    @Test
    public void testGetTopWicketTakers() {
        String url = "http://localhost:" + port + "/api/matches/top-wicket-takers?page=0&size=10";
        Map<String, Object> response = testRestTemplate.getForObject(url, Map.class);
        assertNotNull(response);
        // Add more assertions based on expected response
    }
}

