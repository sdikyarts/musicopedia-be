package musicopedia.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import musicopedia.builder.ArtistBuilder;
import musicopedia.builder.SoloBuilder;
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
import java.util.concurrent.CompletableFuture;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;

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
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mockMvc = MockMvcBuilders.standaloneSetup(soloController)
            .setMessageConverters(new org.springframework.http.converter.json.MappingJackson2HttpMessageConverter(objectMapper))
            .build();

        testId = UUID.randomUUID();
        testArtist = new ArtistBuilder()
            .setArtistId(testId)
            .setArtistName("Taylor Swift")
            .setType(ArtistType.SOLO)
            .setOriginCountry("US")
            .setGenre("Pop")
            .setPrimaryLanguage("English")
            .build();

        testSolo = new SoloBuilder()
            .setArtistId(testId)
            .setArtist(testArtist)
            .setBirthDate(LocalDate.of(1989, 12, 13))
            .setGender(ArtistGender.FEMALE)
            .setRealName("Taylor Alison Swift")
            .buildSolo();
    }

    @Test
    void testGetAllSoloists() throws Exception {
        List<Solo> soloists = Arrays.asList(testSolo);
        when(soloService.findAll()).thenReturn(CompletableFuture.completedFuture(soloists));

        var mvcResult = mockMvc.perform(get("/api/soloists"))
                .andExpect(request().asyncStarted())
                .andReturn();
                
        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].artistId").value(testId.toString()))
                .andExpect(jsonPath("$[0].artist.artistName").value("Taylor Swift"))
                .andExpect(jsonPath("$[0].realName").value("Taylor Alison Swift"));

        verify(soloService, times(1)).findAll();
    }

    @Test
    void testGetSoloistById() throws Exception {
        when(soloService.findById(testId)).thenReturn(CompletableFuture.completedFuture(Optional.of(testSolo)));

        var mvcResult = mockMvc.perform(get("/api/soloists/{id}", testId))
                .andExpect(request().asyncStarted())
                .andReturn();
                
        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.artistId").value(testId.toString()))
                .andExpect(jsonPath("$.artist.artistName").value("Taylor Swift"))
                .andExpect(jsonPath("$.realName").value("Taylor Alison Swift"));

        verify(soloService, times(1)).findById(testId);
    }

    @Test
    void testGetSoloistByIdNotFound() throws Exception {
        when(soloService.findById(testId)).thenReturn(CompletableFuture.completedFuture(Optional.empty()));

        var mvcResult = mockMvc.perform(get("/api/soloists/{id}", testId))
                .andExpect(request().asyncStarted())
                .andReturn();
                
        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isNotFound());

        verify(soloService, times(1)).findById(testId);
    }

    @Test
    void testGetSoloistsByBirthDateRange() throws Exception {
        List<Solo> soloists = Arrays.asList(testSolo);
        LocalDate startDate = LocalDate.of(1990, 1, 1);
        LocalDate endDate = LocalDate.of(1995, 12, 31);
        when(soloService.findByBirthDateBetween(startDate, endDate)).thenReturn(CompletableFuture.completedFuture(soloists));

        var mvcResult = mockMvc.perform(get("/api/soloists/birthdate")
                        .param("start", "1990-01-01")
                        .param("end", "1995-12-31"))
                .andExpect(request().asyncStarted())
                .andReturn();
                
        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].artistId").value(testId.toString()));

        verify(soloService, times(1)).findByBirthDateBetween(startDate, endDate);
    }

    @Test
    void testGetSoloistsByGender() throws Exception {
        List<Solo> soloists = Arrays.asList(testSolo);
        when(soloService.findByGender(ArtistGender.FEMALE)).thenReturn(CompletableFuture.completedFuture(soloists));

        var mvcResult = mockMvc.perform(get("/api/soloists/gender/{gender}", "FEMALE"))
                .andExpect(request().asyncStarted())
                .andReturn();
                
        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].artistId").value(testId.toString()));

        verify(soloService, times(1)).findByGender(ArtistGender.FEMALE);
    }

    @Test
    void testGetActiveSoloists() throws Exception {
        List<Solo> soloists = Arrays.asList(testSolo);
        when(soloService.findActiveSoloArtists()).thenReturn(CompletableFuture.completedFuture(soloists));

        var mvcResult = mockMvc.perform(get("/api/soloists/active"))
                .andExpect(request().asyncStarted())
                .andReturn();
                
        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].artistId").value(testId.toString()));

        verify(soloService, times(1)).findActiveSoloArtists();
    }

    @Test
    void testGetDeceasedSoloists() throws Exception {
        List<Solo> soloists = Arrays.asList(testSolo);
        when(soloService.findDeceasedSoloArtists()).thenReturn(CompletableFuture.completedFuture(soloists));

        var mvcResult = mockMvc.perform(get("/api/soloists/deceased"))
                .andExpect(request().asyncStarted())
                .andReturn();
                
        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].artistId").value(testId.toString()));

        verify(soloService, times(1)).findDeceasedSoloArtists();
    }

    @Test
    void testCreateSoloist() throws Exception {
        when(soloService.save(any(Solo.class), any(Artist.class))).thenReturn(CompletableFuture.completedFuture(testSolo));

        String jsonContent = objectMapper.writeValueAsString(testSolo);

        var mvcResult = mockMvc.perform(post("/api/soloists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(request().asyncStarted())
                .andReturn();
                
        mockMvc.perform(asyncDispatch(mvcResult))
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

        var mvcResult = mockMvc.perform(post("/api/soloists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(request().asyncStarted())
                .andReturn();
                
        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isBadRequest());

        verify(soloService, never()).save(any(Solo.class), any(Artist.class));
    }

    @Test
    void testUpdateSoloist() throws Exception {
        when(soloService.update(any(Solo.class))).thenReturn(CompletableFuture.completedFuture(testSolo));

        String jsonContent = objectMapper.writeValueAsString(testSolo);

        var mvcResult = mockMvc.perform(put("/api/soloists/{id}", testId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(request().asyncStarted())
                .andReturn();
                
        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.artistId").value(testId.toString()));

        verify(soloService, times(1)).update(any(Solo.class));
    }

    @Test
    void testUpdateSoloistWithMismatchedId() throws Exception {
        UUID differentId = UUID.randomUUID();
        testSolo.setArtistId(differentId);

        String jsonContent = objectMapper.writeValueAsString(testSolo);

        var mvcResult = mockMvc.perform(put("/api/soloists/{id}", testId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(request().asyncStarted())
                .andReturn();
                
        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isBadRequest());

        verify(soloService, never()).update(any(Solo.class));
    }

    @Test
    void testUpdateSoloistNotFound() throws Exception {
        when(soloService.update(any(Solo.class))).thenReturn(CompletableFuture.completedFuture(null));

        String jsonContent = objectMapper.writeValueAsString(testSolo);

        var mvcResult = mockMvc.perform(put("/api/soloists/{id}", testId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(request().asyncStarted())
                .andReturn();
                
        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isNotFound());

        verify(soloService, times(1)).update(any(Solo.class));
    }

    @Test
    void testDeleteSoloist() throws Exception {
        when(soloService.deleteById(testId)).thenReturn(CompletableFuture.completedFuture(null));

        var mvcResult = mockMvc.perform(delete("/api/soloists/{id}", testId))
                .andExpect(request().asyncStarted())
                .andReturn();
                
        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isNoContent());

        verify(soloService, times(1)).deleteById(testId);
    }

    @Test
    void testSearchSoloistsByRealName() throws Exception {
        List<Solo> soloists = Arrays.asList(testSolo);
        when(soloService.findByRealNameContaining("Taylor Alison Swift")).thenReturn(CompletableFuture.completedFuture(soloists));

        var mvcResult = mockMvc.perform(get("/api/soloists/search/realname")
                        .param("realName", "Taylor Alison Swift"))
                .andExpect(request().asyncStarted())
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].artistId").value(testId.toString()))
                .andExpect(jsonPath("$[0].realName").value("Taylor Alison Swift"))
                .andExpect(jsonPath("$[0].artist.artistName").value("Taylor Swift"));

        verify(soloService, times(1)).findByRealNameContaining("Taylor Alison Swift");
    }

    @Test
    void testGetSoloistsByDebutDate() throws Exception {
        List<Solo> soloists = Arrays.asList(testSolo);
        LocalDate debutDate = LocalDate.of(2006, 6, 19);
        testSolo.setDebutDate(debutDate);
        when(soloService.findByDebutDate(debutDate)).thenReturn(CompletableFuture.completedFuture(soloists));

        var mvcResult = mockMvc.perform(get("/api/soloists/debutdate")
                        .param("date", "2006-06-19"))
                .andExpect(request().asyncStarted())
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].artistId").value(testId.toString()))
                .andExpect(jsonPath("$[0].debutDate").value("2006-06-19"));

        verify(soloService, times(1)).findByDebutDate(debutDate);
    }

    @Test
    void testGetSoloistsByDebutDateRange() throws Exception {
        List<Solo> soloists = Arrays.asList(testSolo);
        LocalDate startDate = LocalDate.of(2005, 1, 1);
        LocalDate endDate = LocalDate.of(2007, 12, 31);
        testSolo.setDebutDate(LocalDate.of(2006, 6, 19));
        when(soloService.findByDebutDateBetween(startDate, endDate)).thenReturn(CompletableFuture.completedFuture(soloists));

        var mvcResult = mockMvc.perform(get("/api/soloists/debutdate/range")
                        .param("start", "2005-01-01")
                        .param("end", "2007-12-31"))
                .andExpect(request().asyncStarted())
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].artistId").value(testId.toString()))
                .andExpect(jsonPath("$[0].debutDate").value("2006-06-19"));

        verify(soloService, times(1)).findByDebutDateBetween(startDate, endDate);
    }
}
