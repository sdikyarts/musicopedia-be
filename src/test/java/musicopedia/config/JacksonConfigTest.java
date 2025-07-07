package musicopedia.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class JacksonConfigTest {
    @Test
    void testJacksonConfigDisablesWriteDatesAsTimestamps() {
        JacksonConfig config = new JacksonConfig();
        Jackson2ObjectMapperBuilderCustomizer customizer = config.jsonCustomizer();
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
        customizer.customize(builder);
        ObjectMapper mapper = builder.build();
        assertFalse(mapper.isEnabled(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS),
            "WRITE_DATES_AS_TIMESTAMPS should be disabled by JacksonConfig");
    }

    @Test
    void testLocalDateSerializesAsIsoString() throws Exception {
        JacksonConfig config = new JacksonConfig();
        Jackson2ObjectMapperBuilderCustomizer customizer = config.jsonCustomizer();
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
        customizer.customize(builder);
        ObjectMapper mapper = builder.build();
        LocalDate date = LocalDate.of(2020, 5, 17);
        String json = mapper.writeValueAsString(date);
        assertEquals("\"2020-05-17\"", json, "LocalDate should serialize as ISO string");
    }
}
