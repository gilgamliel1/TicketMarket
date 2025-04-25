package TicketMarket.demo.Rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LocationController {
    @PostMapping("/saveCity")
    public ResponseEntity<String> saveCity(@RequestBody CityRequest cityRequest) {
        return ResponseEntity.ok("City saved successfully!");
    }

    static class CityRequest {
        private String city;
        private String country;

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        @Override
        public String toString() {
            return "City: " + city + ", Country: " + country;
        }
    }

}
