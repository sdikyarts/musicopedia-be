package musicopedia.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import musicopedia.model.Artist;
import musicopedia.model.enums.ArtistType;
import musicopedia.dto.request.ArtistRequestDTO;
import musicopedia.service.ArtistService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class ArtistControllerSimpleTest {

    @Mock
    private ArtistService artistService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        ArtistController artistController = new ArtistController(artistService);
        mockMvc = MockMvcBuilders.standaloneSetup(artistController).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void testCreateArtist() throws Exception {
        UUID testId = UUID.randomUUID();
        Artist testArtist = new Artist();
        testArtist.setArtistId(testId);
        testArtist.setArtistName("IU");
        testArtist.setType(ArtistType.SOLO);

        ArtistRequestDTO createRequest = new ArtistRequestDTO();
        createRequest.setArtistName("IU");
        createRequest.setType(ArtistType.SOLO);
        createRequest.setGenre("K-Pop");
        createRequest.setPrimaryLanguage("Korean");
        createRequest.setOriginCountry("KR");

        when(artistService.createArtist(any(ArtistRequestDTO.class))).thenReturn(testArtist);

        String jsonContent = objectMapper.writeValueAsString(createRequest);

        mockMvc.perform(post("/api/artists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.artistId").value(testId.toString()));

        verify(artistService, times(1)).createArtist(any(ArtistRequestDTO.class));
    }
}
