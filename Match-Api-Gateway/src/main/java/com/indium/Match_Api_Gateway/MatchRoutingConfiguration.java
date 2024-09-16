package com.indium.Match_Api_Gateway;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MatchRoutingConfiguration {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("get-match-by-id", r -> r.path("/api/matches/{id}")
                        .uri("http://localhost:8088"))  // Forward to the cricket service
                .route("create-match", r -> r.path("/api/matches/matches")
                        .uri("http://localhost:8088"))  // Forward to the cricket service
                .route("get-matches-by-player-name", r -> r.path("/api/matches/player/**")
                        .uri("http://localhost:8088"))  // Forward to the cricket service
                .route("get-cumulative-score", r -> r.path("/api/matches/cumulative-score")
                        .uri("http://localhost:8088"))  // Forward to the cricket service
                .route("get-wickets-summary-by-bowler", r -> r.path("/api/matches/wickets")
                        .uri("http://localhost:8088"))  // Forward to the cricket service
                .route("get-players-info", r -> r.path("/api/matches/players")
                        .uri("http://localhost:8088"))  // Forward to the cricket service
                .route("get-referees-by-match-number", r -> r.path("/api/matches/referees")
                        .uri("http://localhost:8088"))  // Forward to the cricket service
                .route("get-strike-rate", r -> r.path("/api/matches/strike-rate")
                        .uri("http://localhost:8088"))  // Forward to the cricket service
                .route("get-scores-by-date", r -> r.path("/api/matches/scores")
                        .uri("http://localhost:8088"))  // Forward to the cricket service
                .route("get-top-players", r -> r.path("/api/matches/top-players")
                        .uri("http://localhost:8088"))  // Forward to the cricket service
                .route("get-top-wicket-takers", r -> r.path("/api/matches/top-wicket-takers")
                        .uri("http://localhost:8088"))  // Forward to the cricket service
                .build();
    }
}
