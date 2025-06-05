package TicketMarket.demo.Rest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/dev")
@Profile({"dev", "test"})
public class DatabaseResetController {

    private final JdbcTemplate jdbcTemplate;

    @Value("classpath:schema-postgres.sql")
    private org.springframework.core.io.Resource schemaResource;

    public DatabaseResetController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostMapping("/reset-db")
    @Transactional
    public ResponseEntity<?> resetDatabase() {
        try {
            String sql = new BufferedReader(new InputStreamReader(schemaResource.getInputStream(), StandardCharsets.UTF_8))
                    .lines()
                    .collect(Collectors.joining("\n"));
            jdbcTemplate.execute(sql);
            return ResponseEntity.ok().body("{\"success\": true}");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("{\"success\": false, \"error\": \"" + e.getMessage() + "\"}");
        }
    }
}
