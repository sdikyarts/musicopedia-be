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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

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
        when(artistService.findAllAsync()).thenReturn(CompletableFuture.completedFuture(artists));

        MvcResult mvcResult = mockMvc.perform(get("/api/artists"))
                .andExpect(request().asyncStarted())
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].artistId").value(testId.toString()));

        verify(artistService, times(1)).findAllAsync();
    }

    @Test
    void testGetArtistById() throws Exception {
        when(artistService.findByIdAsync(testId)).thenReturn(CompletableFuture.completedFuture(Optional.of(testArtist)));

        MvcResult mvcResult = mockMvc.perform(get("/api/artists/{id}", testId))
                .andExpect(request().asyncStarted())
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.artistId").value(testId.toString()))
                .andExpect(jsonPath("$.artistName").value("IU"));

        verify(artistService, times(1)).findByIdAsync(testId);
    }

    @Test
    void testGetArtistByIdNotFound() throws Exception {
        when(artistService.findByIdAsync(testId)).thenReturn(CompletableFuture.completedFuture(Optional.empty()));

        MvcResult mvcResult = mockMvc.perform(get("/api/artists/{id}", testId))
                .andExpect(request().asyncStarted())
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isNotFound());

        verify(artistService, times(1)).findByIdAsync(testId);
    }

    @Test
    void testSearchArtistsByName() throws Exception {
        List<Artist> artists = Arrays.asList(testArtist);
        when(artistService.findByNameContainingAsync("IU")).thenReturn(CompletableFuture.completedFuture(artists));

        MvcResult mvcResult = mockMvc.perform(get("/api/artists/search")
                        .param("name", "IU"))
                .andExpect(request().asyncStarted())
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].artistName").value("IU"));

        verify(artistService, times(1)).findByNameContainingAsync("IU");
    }

    @Test
    void testGetArtistBySpotifyId() throws Exception {
        when(artistService.findBySpotifyIdAsync("spotify123")).thenReturn(CompletableFuture.completedFuture(Optional.of(testArtist)));

        MvcResult mvcResult = mockMvc.perform(get("/api/artists/spotify/{spotifyId}", "spotify123"))
                .andExpect(request().asyncStarted())
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.artistId").value(testId.toString()))
                .andExpect(jsonPath("$.spotifyId").value("spotify123"));

        verify(artistService, times(1)).findBySpotifyIdAsync("spotify123");
    }

    @Test
    void testGetArtistBySpotifyIdNotFound() throws Exception {
        when(artistService.findBySpotifyIdAsync("spotify123")).thenReturn(CompletableFuture.completedFuture(Optional.empty()));

        MvcResult mvcResult = mockMvc.perform(get("/api/artists/spotify/{spotifyId}", "spotify123"))
                .andExpect(request().asyncStarted())
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isNotFound());

        verify(artistService, times(1)).findBySpotifyIdAsync("spotify123");
    }

    @Test
    void testGetArtistsByType() throws Exception {
        List<Artist> artists = Arrays.asList(testArtist);
        when(artistService.findByTypeAsync(ArtistType.SOLO)).thenReturn(CompletableFuture.completedFuture(artists));

        MvcResult mvcResult = mockMvc.perform(get("/api/artists/type/{type}", "SOLO"))
                .andExpect(request().asyncStarted())
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].type").value("SOLO"));

        verify(artistService, times(1)).findByTypeAsync(ArtistType.SOLO);
    }

    @Test
    void testCreateArtist() throws Exception {
        // Create a proper DTO for the new factory-based endpoint
        ArtistRequestDTO createRequest = new ArtistRequestDTO();
        createRequest.setArtistName("IU");
        createRequest.setType(ArtistType.SOLO);
        createRequest.setGenre("K-Pop");
        createRequest.setPrimaryLanguage("Korean");
        createRequest.setOriginCountry("KR");

        when(artistService.createArtistAsync(any(ArtistRequestDTO.class))).thenReturn(CompletableFuture.completedFuture(testArtist));

        String jsonContent = objectMapper.writeValueAsString(createRequest);

        MvcResult mvcResult = mockMvc.perform(post("/api/artists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(request().asyncStarted())
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.artistId").value(testId.toString()))
                .andExpect(jsonPath("$.artistName").value("IU"));

        verify(artistService, times(1)).createArtistAsync(any(ArtistRequestDTO.class));
    }

    @Test
    void testCreateArtistValidationError() throws Exception {
        // Test error handling
        ArtistRequestDTO createRequest = new ArtistRequestDTO();
        createRequest.setArtistName("IU");
        createRequest.setType(ArtistType.SOLO);

        CompletableFuture<Artist> failedFuture = new CompletableFuture<>();
        failedFuture.completeExceptionally(new IllegalArgumentException("Validation failed"));
        when(artistService.createArtistAsync(any(ArtistRequestDTO.class))).thenReturn(failedFuture);

        String jsonContent = objectMapper.writeValueAsString(createRequest);

        MvcResult mvcResult = mockMvc.perform(post("/api/artists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(request().asyncStarted())
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isBadRequest());

        verify(artistService, times(1)).createArtistAsync(any(ArtistRequestDTO.class));
    }

    @Test
    void testCreateArtistLegacy() throws Exception {
        // Test the legacy endpoint
        when(artistService.saveAsync(any(Artist.class))).thenReturn(CompletableFuture.completedFuture(testArtist));

        String jsonContent = objectMapper.writeValueAsString(testArtist);

        MvcResult mvcResult = mockMvc.perform(post("/api/artists/legacy")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(request().asyncStarted())
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.artistId").value(testId.toString()));

        verify(artistService, times(1)).saveAsync(any(Artist.class));
    }

    @Test
    void testDeleteArtistExists() throws Exception {
        when(artistService.existsByIdAsync(testId)).thenReturn(CompletableFuture.completedFuture(true));
        when(artistService.deleteByIdAsync(testId)).thenReturn(CompletableFuture.completedFuture(null));

        MvcResult mvcResult = mockMvc.perform(delete("/api/artists/{id}", testId))
                .andExpect(request().asyncStarted())
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isNoContent());

        verify(artistService, times(1)).existsByIdAsync(testId);
        verify(artistService, times(1)).deleteByIdAsync(testId);
    }

    @Test
    void testDeleteArtistNotFound() throws Exception {
        when(artistService.existsByIdAsync(testId)).thenReturn(CompletableFuture.completedFuture(false));

        MvcResult mvcResult = mockMvc.perform(delete("/api/artists/{id}", testId))
                .andExpect(request().asyncStarted())
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isNotFound());

        verify(artistService, times(1)).existsByIdAsync(testId);
        verify(artistService, never()).deleteByIdAsync(testId);
    }

    @Test
    void testCreateArtistsBatch_Success() throws Exception {
        ArtistRequestDTO dto1 = new ArtistRequestDTO();
        dto1.setArtistName("IU");
        dto1.setType(ArtistType.SOLO);
        dto1.setGenre("K-Pop");
        dto1.setPrimaryLanguage("Korean");
        dto1.setOriginCountry("KR");

        ArtistRequestDTO dto2 = new ArtistRequestDTO();
        dto2.setArtistName("BTS");
        dto2.setType(ArtistType.GROUP);
        dto2.setGenre("K-Pop");
        dto2.setPrimaryLanguage("Korean");
        dto2.setOriginCountry("KR");

        Artist artist1 = new Artist();
        artist1.setArtistId(UUID.randomUUID());
        artist1.setArtistName("IU");
        artist1.setType(ArtistType.SOLO);

        Artist artist2 = new Artist();
        artist2.setArtistId(UUID.randomUUID());
        artist2.setArtistName("BTS");
        artist2.setType(ArtistType.GROUP);

        when(artistService.processBatchAsync(anyList()))
            .thenReturn(CompletableFuture.completedFuture(Arrays.asList(artist1, artist2)));

        String jsonContent = objectMapper.writeValueAsString(Arrays.asList(dto1, dto2));

        MvcResult mvcResult = mockMvc.perform(post("/api/artists/batch")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonContent))
            .andExpect(request().asyncStarted())
            .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$[0].artistName").value("IU"))
            .andExpect(jsonPath("$[1].artistName").value("BTS"));

        verify(artistService, times(1)).processBatchAsync(anyList());
    }

    @Test
    void testCreateArtistsBatch_ValidationError() throws Exception {
        ArtistRequestDTO dto = new ArtistRequestDTO();
        dto.setArtistName(""); // Invalid

        CompletableFuture<List<Artist>> failedFuture = new CompletableFuture<>();
        failedFuture.completeExceptionally(new IllegalArgumentException("Validation failed"));
        when(artistService.processBatchAsync(anyList())).thenReturn(failedFuture);

        String jsonContent = objectMapper.writeValueAsString(Arrays.asList(dto));

        MvcResult mvcResult = mockMvc.perform(post("/api/artists/batch")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonContent))
            .andExpect(request().asyncStarted())
            .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
            .andExpect(status().isBadRequest());

        verify(artistService, times(1)).processBatchAsync(anyList());
    }
}
