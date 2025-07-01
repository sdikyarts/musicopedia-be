package musicopedia.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import musicopedia.model.Artist;
import musicopedia.model.enums.ArtistType;
import musicopedia.service.ArtistService;
import musicopedia.service.config.ServiceTestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.Import;
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

@Import(ServiceTestConfig.class)
public class ArtistControllerTest {

    @Mock
    private ArtistService artistService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private UUID testId;
    private Artist testArtist;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        ArtistController artistController = new ArtistController(artistService);
        mockMvc = MockMvcBuilders.standaloneSetup(artistController).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        testId = UUID.randomUUID();
        testArtist = new Artist();
        testArtist.setArtistId(testId);
        testArtist.setArtistName("IU");
        testArtist.setType(ArtistType.Solo);
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
        when(artistService.findByType(ArtistType.Solo)).thenReturn(artists);

        mockMvc.perform(get("/api/artists/type/{type}", "Solo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].type").value("Solo"));

        verify(artistService, times(1)).findByType(ArtistType.Solo);
    }

    @Test
    void testCreateArtist() throws Exception {
        when(artistService.save(any(Artist.class))).thenReturn(testArtist);

        String jsonContent = objectMapper.writeValueAsString(testArtist);

        mockMvc.perform(post("/api/artists")
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
