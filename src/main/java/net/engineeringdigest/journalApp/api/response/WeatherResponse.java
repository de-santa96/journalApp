package net.engineeringdigest.journalApp.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class WeatherResponse{
    private Current current;

    @Getter
    @Setter
    public class Current{
        private int temperature;

        // in json, field name is weather_description but we used camelCase in pojo field
        // so we can specify using @JsonProperty
        @JsonProperty("weather_description")
        private List<String> weatherDescriptions;
        private int humidity;
        private int feelslike;
    }
}

