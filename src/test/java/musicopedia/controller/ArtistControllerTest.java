package musicopedia.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import musicopedia.model.Artist;
import musicopedia.model.enums.ArtistType;
import musicopedia.dto.request.CreateArtistRequestDTO;
import musicopedia.service.ArtistService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class ArtistControllerTest {

    @Mock
    private ArtistService artistService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private UUID testId;
    private Artist testArtist;

    @BeforeEach
    void setup() {
        ArtistController artistController = new ArtistController(artistService);
        mockMvc = MockMvcBuilders.standaloneSetup(artistController).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        testId = UUID.randomUUID();
        testArtist = new Artist();
        testArtist.setArtistId(testId);
        testArtist.setArtistName("IU");
        testArtist.setType(ArtistType.SOLO);
        testArtist.setOriginCountry("KR");
        testArtist.setPrimaryLanguage("Korean");
        testArtist.setSpotifyId("spotify123");
    }

    @Test
    void testGetAllArtists() throws Exception {
        List<Artist> artists = Arrays.asList(testArtist);
        when(artistService.findAll()).thenReturn(artists);

        mockMvc.perform(get("/api/artists"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].artistId").value(testId.toString()));

        verify(artistService, times(1)).findAll();
    }

    @Test
    void testGetArtistById() throws Exception {
        when(artistService.findById(testId)).thenReturn(Optional.of(testArtist));

        mockMvc.perform(get("/api/artists/{id}", testId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.artistId").value(testId.toString()))
                .andExpect(jsonPath("$.artistName").value("IU"));

        verify(artistService, times(1)).findById(testId);
    }

    @Test
    void testGetArtistByIdNotFound() throws Exception {
        when(artistService.findById(testId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/artists/{id}", testId))
                .andExpect(status().isNotFound());

        verify(artistService, times(1)).findById(testId);
    }

    @Test
    void testSearchArtistsByName() throws Exception {
        List<Artist> artists = Arrays.asList(testArtist);
        when(artistService.findByNameContaining("IU")).thenReturn(artists);

        mockMvc.perform(get("/api/artists/search")
                        .param("name", "IU"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].artistName").value("IU"));

        verify(artistService, times(1)).findByNameContaining("IU");
    }

    @Test
    void testGetArtistBySpotifyId() throws Exception {
        when(artistService.findBySpotifyId("spotify123")).thenReturn(Optional.of(testArtist));

        mockMvc.perform(get("/api/artists/spotify/{spotifyId}", "spotify123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.artistId").value(testId.toString()))
                .andExpect(jsonPath("$.spotifyId").value("spotify123"));

        verify(artistService, times(1)).findBySpotifyId("spotify123");
    }

    @Test
    void testGetArtistBySpotifyIdNotFound() throws Exception {
        when(artistService.findBySpotifyId("spotify123")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/artists/spotify/{spotifyId}", "spotify123"))
                .andExpect(status().isNotFound());

        verify(artistService, times(1)).findBySpotifyId("spotify123");
    }

    @Test
    void testGetArtistsByType() throws Exception {
        List<Artist> artists = Arrays.asList(testArtist);
        when(artistService.findByType(ArtistType.SOLO)).thenReturn(artists);

        mockMvc.perform(get("/api/artists/type/{type}", "SOLO"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].type").value("SOLO"));

        verify(artistService, times(1)).findByType(ArtistType.SOLO);
    }

    @Test
    void testCreateArtist() throws Exception {
        // Create a proper DTO for the new factory-based endpoint
        CreateArtistRequestDTO createRequest = new CreateArtistRequestDTO();
        createRequest.setArtistName("IU");
        createRequest.setType(ArtistType.SOLO);
        createRequest.setGenre("K-Pop");
        createRequest.setPrimaryLanguage("Korean");
        createRequest.setOriginCountry("KR");

        when(artistService.createArtist(any(CreateArtistRequestDTO.class))).thenReturn(testArtist);

        String jsonContent = objectMapper.writeValueAsString(createRequest);

        mockMvc.perform(post("/api/artists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andDo(result -> {
                    System.out.println("Status: " + result.getResponse().getStatus());
                    System.out.println("Content: " + result.getResponse().getContentAsString());
                });

        verify(artistService, times(1)).createArtist(any(CreateArtistRequestDTO.class));
    }

    @Test
    void testCreateArtistValidationError() throws Exception {
        // Test error handling
        CreateArtistRequestDTO createRequest = new CreateArtistRequestDTO();
        createRequest.setArtistName("IU");
        createRequest.setType(ArtistType.SOLO);

        when(artistService.createArtist(any(CreateArtistRequestDTO.class)))
                .thenThrow(new IllegalArgumentException("Validation failed"));

        String jsonContent = objectMapper.writeValueAsString(createRequest);

        mockMvc.perform(post("/api/artists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isBadRequest());

        verify(artistService, times(1)).createArtist(any(CreateArtistRequestDTO.class));
    }

    @Test
    void testCreateArtistLegacy() throws Exception {
        // Test the legacy endpoint
        when(artistService.save(any(Artist.class))).thenReturn(testArtist);

        String jsonContent = objectMapper.writeValueAsString(testArtist);

        mockMvc.perform(post("/api/artists/legacy")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.artistId").value(testId.toString()));

        verify(artistService, times(1)).save(any(Artist.class));
    }

    @Test
    void testDeleteArtistExists() throws Exception {
        when(artistService.existsById(testId)).thenReturn(true);
        doNothing().when(artistService).deleteById(testId);

        mockMvc.perform(delete("/api/artists/{id}", testId))
                .andExpect(status().isNoContent());

        verify(artistService, times(1)).existsById(testId);
        verify(artistService, times(1)).deleteById(testId);
    }

    @Test
    void testDeleteArtistNotFound() throws Exception {
        when(artistService.existsById(testId)).thenReturn(false);

        mockMvc.perform(delete("/api/artists/{id}", testId))
                .andExpect(status().isNotFound());

        verify(artistService, times(1)).existsById(testId);
        verify(artistService, never()).deleteById(testId);
    }
}
