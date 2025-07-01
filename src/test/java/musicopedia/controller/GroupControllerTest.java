package musicopedia.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import musicopedia.model.Artist;
import musicopedia.model.Groups;
import musicopedia.model.enums.ArtistGender;
import musicopedia.model.enums.ArtistType;
import musicopedia.service.GroupService;
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
public class GroupControllerTest {

    @Mock
    private GroupService groupService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private UUID testId;
    private Artist testArtist;
    private Groups testGroup;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        GroupController groupController = new GroupController(groupService);
        mockMvc = MockMvcBuilders.standaloneSetup(groupController).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        testId = UUID.randomUUID();
        testArtist = new Artist();
        testArtist.setArtistId(testId);
        testArtist.setArtistName("BLACKPINK");
        testArtist.setType(ArtistType.Group);
        testArtist.setOriginCountry("KR");
        testArtist.setPrimaryLanguage("Korean");

        testGroup = new Groups();
        testGroup.setArtistId(testId);
        testGroup.setArtist(testArtist);
        testGroup.setFormationDate(LocalDate.of(2016, 8, 8));
        testGroup.setGroupGender(ArtistGender.Female);
    }

    @Test
    void testGetAllGroups() throws Exception {
        List<Groups> groups = Arrays.asList(testGroup);
        when(groupService.findAll()).thenReturn(groups);

        mockMvc.perform(get("/api/groups"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].artistId").value(testId.toString()));

        verify(groupService, times(1)).findAll();
    }

    @Test
    void testGetGroupById() throws Exception {
        when(groupService.findById(testId)).thenReturn(Optional.of(testGroup));

        mockMvc.perform(get("/api/groups/{id}", testId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.artistId").value(testId.toString()))
                .andExpect(jsonPath("$.artist.artistName").value("BLACKPINK"));

        verify(groupService, times(1)).findById(testId);
    }

    @Test
    void testGetGroupByIdNotFound() throws Exception {
        when(groupService.findById(testId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/groups/{id}", testId))
                .andExpect(status().isNotFound());

        verify(groupService, times(1)).findById(testId);
    }

    @Test
    void testGetGroupsByFormationDateRange() throws Exception {
        List<Groups> groups = Arrays.asList(testGroup);
        LocalDate startDate = LocalDate.of(2015, 1, 1);
        LocalDate endDate = LocalDate.of(2017, 12, 31);
        when(groupService.findByFormationDateBetween(startDate, endDate)).thenReturn(groups);

        mockMvc.perform(get("/api/groups/formation-date")
                        .param("start", "2015-01-01")
                        .param("end", "2017-12-31"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].artistId").value(testId.toString()));

        verify(groupService, times(1)).findByFormationDateBetween(startDate, endDate);
    }

    @Test
    void testGetActiveGroups() throws Exception {
        List<Groups> groups = Arrays.asList(testGroup);
        when(groupService.findActiveGroups()).thenReturn(groups);

        mockMvc.perform(get("/api/groups/active"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].artistId").value(testId.toString()));

        verify(groupService, times(1)).findActiveGroups();
    }

    @Test
    void testGetDisbandedGroups() throws Exception {
        List<Groups> groups = Arrays.asList(testGroup);
        when(groupService.findDisbandedGroups()).thenReturn(groups);

        mockMvc.perform(get("/api/groups/disbanded"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].artistId").value(testId.toString()));

        verify(groupService, times(1)).findDisbandedGroups();
    }

    @Test
    void testGetGroupsByGender() throws Exception {
        List<Groups> groups = Arrays.asList(testGroup);
        when(groupService.findByGroupGender(ArtistGender.Female)).thenReturn(groups);

        mockMvc.perform(get("/api/groups/gender/{gender}", "Female"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].artistId").value(testId.toString()));

        verify(groupService, times(1)).findByGroupGender(ArtistGender.Female);
    }

    @Test
    void testCreateGroup() throws Exception {
        when(groupService.save(any(Groups.class), any(Artist.class))).thenReturn(testGroup);

        String jsonContent = objectMapper.writeValueAsString(testGroup);

        mockMvc.perform(post("/api/groups")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.artistId").value(testId.toString()));

        verify(groupService, times(1)).save(any(Groups.class), any(Artist.class));
    }

    @Test
    void testCreateGroupWithoutArtist() throws Exception {
        Groups groupWithoutArtist = new Groups();
        groupWithoutArtist.setArtistId(testId);
        groupWithoutArtist.setFormationDate(LocalDate.of(2016, 8, 8));
        groupWithoutArtist.setGroupGender(ArtistGender.Female);

        String jsonContent = objectMapper.writeValueAsString(groupWithoutArtist);

        mockMvc.perform(post("/api/groups")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isBadRequest());

        verify(groupService, never()).save(any(Groups.class), any(Artist.class));
    }

    @Test
    void testUpdateGroup() throws Exception {
        when(groupService.update(any(Groups.class))).thenReturn(testGroup);

        String jsonContent = objectMapper.writeValueAsString(testGroup);

        mockMvc.perform(put("/api/groups/{id}", testId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.artistId").value(testId.toString()));

        verify(groupService, times(1)).update(any(Groups.class));
    }

    @Test
    void testUpdateGroupWithMismatchedId() throws Exception {
        UUID differentId = UUID.randomUUID();
        testGroup.setArtistId(differentId);

        String jsonContent = objectMapper.writeValueAsString(testGroup);

        mockMvc.perform(put("/api/groups/{id}", testId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isBadRequest());

        verify(groupService, never()).update(any(Groups.class));
    }

    @Test
    void testUpdateGroupNotFound() throws Exception {
        when(groupService.update(any(Groups.class))).thenReturn(null);

        String jsonContent = objectMapper.writeValueAsString(testGroup);

        mockMvc.perform(put("/api/groups/{id}", testId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isNotFound());

        verify(groupService, times(1)).update(any(Groups.class));
    }

    @Test
    void testDeleteGroup() throws Exception {
        doNothing().when(groupService).deleteById(testId);

        mockMvc.perform(delete("/api/groups/{id}", testId))
                .andExpect(status().isNoContent());

        verify(groupService, times(1)).deleteById(testId);
    }
}
