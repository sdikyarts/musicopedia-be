package musicopedia.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import musicopedia.model.Artist;
import musicopedia.model.Solo;
import musicopedia.model.enums.ArtistGender;
import musicopedia.model.enums.ArtistType;
import musicopedia.service.SoloService;
import musicopedia.service.config.ServiceTestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(ServiceTestConfig.class)
public class SoloControllerTest {

    @Mock
    private SoloService soloService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private UUID testId;
    private Artist testArtist;
    private Solo testSolo;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        SoloController soloController = new SoloController(soloService);
        mockMvc = MockMvcBuilders.standaloneSetup(soloController).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        testId = UUID.randomUUID();
        testArtist = new Artist();
        testArtist.setArtistId(testId);
        testArtist.setArtistName("IU");
        testArtist.setType(ArtistType.SOLO);
        testArtist.setOriginCountry("KR");
        testArtist.setPrimaryLanguage("Korean");

        testSolo = new Solo();
        testSolo.setArtistId(testId);
        testSolo.setArtist(testArtist);
        testSolo.setBirthDate(LocalDate.of(1993, 5, 16));
        testSolo.setGender(ArtistGender.FEMALE);
    }

    @Test
    void testGetAllSoloists() throws Exception {
        List<Solo> soloists = Arrays.asList(testSolo);
        when(soloService.findAll()).thenReturn(soloists);

        mockMvc.perform(get("/api/soloists"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].artistId").value(testId.toString()));

        verify(soloService, times(1)).findAll();
    }

    @Test
    void testGetSoloistById() throws Exception {
        when(soloService.findById(testId)).thenReturn(Optional.of(testSolo));

        mockMvc.perform(get("/api/soloists/{id}", testId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.artistId").value(testId.toString()))
                .andExpect(jsonPath("$.artist.artistName").value("IU"));

        verify(soloService, times(1)).findById(testId);
    }

    @Test
    void testGetSoloistByIdNotFound() throws Exception {
        when(soloService.findById(testId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/soloists/{id}", testId))
                .andExpect(status().isNotFound());

        verify(soloService, times(1)).findById(testId);
    }

    @Test
    void testGetSoloistsByBirthDateRange() throws Exception {
        List<Solo> soloists = Arrays.asList(testSolo);
        LocalDate startDate = LocalDate.of(1990, 1, 1);
        LocalDate endDate = LocalDate.of(1995, 12, 31);
        when(soloService.findByBirthDateBetween(startDate, endDate)).thenReturn(soloists);

        mockMvc.perform(get("/api/soloists/birthdate")
                        .param("start", "1990-01-01")
                        .param("end", "1995-12-31"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].artistId").value(testId.toString()));

        verify(soloService, times(1)).findByBirthDateBetween(startDate, endDate);
    }

    @Test
    void testGetSoloistsByGender() throws Exception {
        List<Solo> soloists = Arrays.asList(testSolo);
        when(soloService.findByGender(ArtistGender.FEMALE)).thenReturn(soloists);

        mockMvc.perform(get("/api/soloists/gender/{gender}", "FEMALE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].artistId").value(testId.toString()));

        verify(soloService, times(1)).findByGender(ArtistGender.FEMALE);
    }

    @Test
    void testGetActiveSoloists() throws Exception {
        List<Solo> soloists = Arrays.asList(testSolo);
        when(soloService.findActiveSoloArtists()).thenReturn(soloists);

        mockMvc.perform(get("/api/soloists/active"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].artistId").value(testId.toString()));

        verify(soloService, times(1)).findActiveSoloArtists();
    }

    @Test
    void testGetDeceasedSoloists() throws Exception {
        List<Solo> soloists = Arrays.asList(testSolo);
        when(soloService.findDeceasedSoloArtists()).thenReturn(soloists);

        mockMvc.perform(get("/api/soloists/deceased"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].artistId").value(testId.toString()));

        verify(soloService, times(1)).findDeceasedSoloArtists();
    }

    @Test
    void testCreateSoloist() throws Exception {
        when(soloService.save(any(Solo.class), any(Artist.class))).thenReturn(testSolo);

        String jsonContent = objectMapper.writeValueAsString(testSolo);

        mockMvc.perform(post("/api/soloists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.artistId").value(testId.toString()));

        verify(soloService, times(1)).save(any(Solo.class), any(Artist.class));
    }

    @Test
    void testCreateSoloistWithoutArtist() throws Exception {
        Solo soloWithoutArtist = new Solo();
        soloWithoutArtist.setArtistId(testId);
        soloWithoutArtist.setBirthDate(LocalDate.of(1993, 5, 16));
        soloWithoutArtist.setGender(ArtistGender.FEMALE);

        String jsonContent = objectMapper.writeValueAsString(soloWithoutArtist);

        mockMvc.perform(post("/api/soloists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isBadRequest());

        verify(soloService, never()).save(any(Solo.class), any(Artist.class));
    }

    @Test
    void testUpdateSoloist() throws Exception {
        when(soloService.update(any(Solo.class))).thenReturn(testSolo);

        String jsonContent = objectMapper.writeValueAsString(testSolo);

        mockMvc.perform(put("/api/soloists/{id}", testId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.artistId").value(testId.toString()));

        verify(soloService, times(1)).update(any(Solo.class));
    }

    @Test
    void testUpdateSoloistWithMismatchedId() throws Exception {
        UUID differentId = UUID.randomUUID();
        testSolo.setArtistId(differentId);

        String jsonContent = objectMapper.writeValueAsString(testSolo);

        mockMvc.perform(put("/api/soloists/{id}", testId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isBadRequest());

        verify(soloService, never()).update(any(Solo.class));
    }

    @Test
    void testUpdateSoloistNotFound() throws Exception {
        when(soloService.update(any(Solo.class))).thenReturn(null);

        String jsonContent = objectMapper.writeValueAsString(testSolo);

        mockMvc.perform(put("/api/soloists/{id}", testId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isNotFound());

        verify(soloService, times(1)).update(any(Solo.class));
    }

    @Test
    void testDeleteSoloist() throws Exception {
        doNothing().when(soloService).deleteById(testId);

        mockMvc.perform(delete("/api/soloists/{id}", testId))
                .andExpect(status().isNoContent());

        verify(soloService, times(1)).deleteById(testId);
    }
}
