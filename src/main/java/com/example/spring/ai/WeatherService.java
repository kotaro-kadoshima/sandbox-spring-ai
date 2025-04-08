package com.example.spring.ai;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class WeatherService {

    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    public WeatherService() {
        this.restClient = RestClient.builder()
                .baseUrl("https://api.weather.gov")
                .defaultHeader("Accept", "application/geo+json")
                .defaultHeader("User-Agent", "WeatherApiClient/1.0 (kadoroshima@gmail.com)")
                .build();
        this.objectMapper = new ObjectMapper();
    }

    @Tool(description = "Get weather forecast for a specific latitude/longitude")
    public String getWeatherForecastByLocation(
            double latitude,
            double longitude
    ) {
        try {
            // Step 1: Get gridpoint info
            String pointsUrl = String.format("/points/%.4f,%.4f", latitude, longitude);
            String pointResponse = restClient.get()
                    .uri(pointsUrl)
                    .retrieve()
                    .body(String.class);

            JsonNode pointJson = objectMapper.readTree(pointResponse);
            String forecastUrl = pointJson
                    .path("properties")
                    .path("forecast")
                    .asText();

            // Step 2: Fetch forecast
            String forecastResponse = restClient.get()
                    .uri(forecastUrl)
                    .retrieve()
                    .body(String.class);

            JsonNode forecastJson = objectMapper.readTree(forecastResponse);
            JsonNode periods = forecastJson
                    .path("properties")
                    .path("periods");

            if (!periods.isArray()) {
                return "Forecast data is unavailable.";
            }

            StringBuilder result = new StringBuilder();
            for (int i = 0; i < Math.min(2, periods.size()); i++) {
                JsonNode period = periods.get(i);
                result.append(String.format(
                        "%s: %s %s, %s, Wind: %s %s\n",
                        period.path("name").asText(),
                        period.path("temperature").asText(),
                        period.path("temperatureUnit").asText(),
                        period.path("detailedForecast").asText(),
                        period.path("windSpeed").asText(),
                        period.path("windDirection").asText()
                ));
            }

            return result.toString();

        } catch (Exception e) {
            return "Error retrieving weather forecast: " + e.getMessage();
        }
    }

    @Tool(description = "Get weather alerts for a US state")
    public String getAlerts(
            @ToolParam(description = "Two-letter US state code (e.g. CA, NY)") String state
    ) {
        try {
            String url = UriComponentsBuilder
                    .fromPath("/alerts/active")
                    .queryParam("area", state.toUpperCase())
                    .build()
                    .toUriString();

            String response = restClient.get()
                    .uri(url)
                    .retrieve()
                    .body(String.class);

            JsonNode root = objectMapper.readTree(response);
            JsonNode features = root.path("features");

            if (!features.isArray() || features.size() == 0) {
                return "No active alerts for " + state;
            }

            StringBuilder alerts = new StringBuilder();
            for (JsonNode feature : features) {
                JsonNode alert = feature.path("properties");
                alerts.append(String.format(
                        "Event: %s\nArea: %s\nSeverity: %s\nDescription: %s\nInstructions: %s\n\n",
                        alert.path("event").asText("N/A"),
                        alert.path("areaDesc").asText("Unknown area"),
                        alert.path("severity").asText("N/A"),
                        alert.path("description").asText("No description"),
                        alert.path("instruction").asText("No instructions")
                ));
            }

            return alerts.toString();

        } catch (Exception e) {
            return "Error retrieving alerts: " + e.getMessage();
        }
    }
}
