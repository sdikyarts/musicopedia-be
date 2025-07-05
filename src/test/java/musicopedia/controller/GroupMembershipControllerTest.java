package musicopedia.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import musicopedia.builder.ArtistBuilder;
import musicopedia.builder.MemberBuilder;
import musicopedia.model.Artist;
import musicopedia.model.Member;
import musicopedia.model.enums.ArtistType;
import musicopedia.model.enums.MembershipStatus;
import musicopedia.model.membership.GroupMembership;
import musicopedia.model.membership.GroupMembershipId;
import musicopedia.service.GroupMembershipService;
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
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;

@Import(ServiceTestConfig.class)
public class GroupMembershipControllerTest {

    @Mock
    private GroupMembershipService groupMembershipService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private UUID testGroupId;
    private UUID testMemberId;
    private Artist testGroup;
    private Member testMember;
    private GroupMembership testMembership;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        GroupMembershipController groupMembershipController = new GroupMembershipController(groupMembershipService);
        mockMvc = MockMvcBuilders.standaloneSetup(groupMembershipController).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        testGroupId = UUID.randomUUID();
        testMemberId = UUID.randomUUID();

        testGroup = new ArtistBuilder()
            .setArtistId(testGroupId)
            .setArtistName("BLACKPINK")
            .setType(ArtistType.GROUP)
            .build();

        testMember = new MemberBuilder()
            .setMemberName("Jisoo")
            .setBirthDate(LocalDate.of(1995, 1, 3))
            .build();
        testMember.setMemberId(testMemberId);

        GroupMembershipId membershipId = new GroupMembershipId();
        membershipId.setGroupId(testGroupId);
        membershipId.setMemberId(testMemberId);

        testMembership = new GroupMembership();
        testMembership.setId(membershipId);
        testMembership.setGroup(testGroup);
        testMembership.setMember(testMember);
        testMembership.setStatus(MembershipStatus.CURRENT);
        testMembership.setJoinDate(LocalDate.of(2016, 8, 8));
    }

    @Test
    void testGetMembershipsByGroupId() throws Exception {
        List<GroupMembership> memberships = Arrays.asList(testMembership);
        when(groupMembershipService.findByGroupId(testGroupId)).thenReturn(CompletableFuture.completedFuture(memberships));

        var mvcResult = mockMvc.perform(get("/api/memberships/group/{groupId}", testGroupId))
                .andExpect(request().asyncStarted())
                .andReturn();
                
        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id.groupId").value(testGroupId.toString()));

        verify(groupMembershipService, times(1)).findByGroupId(testGroupId);
    }

    @Test
    void testGetMembershipsByMemberId() throws Exception {
        List<GroupMembership> memberships = Arrays.asList(testMembership);
        when(groupMembershipService.findByMemberId(testMemberId)).thenReturn(CompletableFuture.completedFuture(memberships));

        var mvcResult = mockMvc.perform(get("/api/memberships/member/{memberId}", testMemberId))
                .andExpect(request().asyncStarted())
                .andReturn();
                
        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id.memberId").value(testMemberId.toString()));

        verify(groupMembershipService, times(1)).findByMemberId(testMemberId);
    }

    @Test
    void testGetMembershipsByGroupIdAndStatus() throws Exception {
        List<GroupMembership> memberships = Arrays.asList(testMembership);
        when(groupMembershipService.findByGroupIdAndStatus(testGroupId, MembershipStatus.CURRENT))
                .thenReturn(CompletableFuture.completedFuture(memberships));

        var mvcResult = mockMvc.perform(get("/api/memberships/group/{groupId}/status/{status}", testGroupId, "CURRENT"))
                .andExpect(request().asyncStarted())
                .andReturn();
                
        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].status").value("CURRENT"));

        verify(groupMembershipService, times(1)).findByGroupIdAndStatus(testGroupId, MembershipStatus.CURRENT);
    }

    @Test
    void testGetFormerMembersByGroupId() throws Exception {
        GroupMembership formerMember = new GroupMembership();
        GroupMembershipId formerMembershipId = new GroupMembershipId();
        formerMembershipId.setGroupId(testGroupId);
        formerMembershipId.setMemberId(UUID.randomUUID());
        formerMember.setId(formerMembershipId);
        formerMember.setStatus(MembershipStatus.FORMER);

        List<GroupMembership> formerMemberships = Arrays.asList(formerMember);
        when(groupMembershipService.findFormerMembersByGroupId(testGroupId)).thenReturn(CompletableFuture.completedFuture(formerMemberships));

        var mvcResult = mockMvc.perform(get("/api/memberships/group/{groupId}/former-members", testGroupId))
                .andExpect(request().asyncStarted())
                .andReturn();
                
        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].status").value("FORMER"));

        verify(groupMembershipService, times(1)).findFormerMembersByGroupId(testGroupId);
    }

    @Test
    void testGetMembersJoinedAfter() throws Exception {
        LocalDate afterDate = LocalDate.of(2016, 1, 1);
        List<GroupMembership> memberships = Arrays.asList(testMembership);
        when(groupMembershipService.findByGroupIdAndJoinDateAfter(testGroupId, afterDate))
                .thenReturn(CompletableFuture.completedFuture(memberships));

        var mvcResult = mockMvc.perform(get("/api/memberships/group/{groupId}/joined-after", testGroupId)
                        .param("date", "2016-01-01"))
                .andExpect(request().asyncStarted())
                .andReturn();
                
        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id.groupId").value(testGroupId.toString()));

        verify(groupMembershipService, times(1)).findByGroupIdAndJoinDateAfter(testGroupId, afterDate);
    }

    @Test
    void testGetMembersLeftBefore() throws Exception {
        LocalDate beforeDate = LocalDate.of(2020, 1, 1);
        List<GroupMembership> memberships = Arrays.asList(testMembership);
        when(groupMembershipService.findByGroupIdAndLeaveDateBefore(testGroupId, beforeDate))
                .thenReturn(CompletableFuture.completedFuture(memberships));

        var mvcResult = mockMvc.perform(get("/api/memberships/group/{groupId}/left-before", testGroupId)
                        .param("date", "2020-01-01"))
                .andExpect(request().asyncStarted())
                .andReturn();
                
        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id.groupId").value(testGroupId.toString()));

        verify(groupMembershipService, times(1)).findByGroupIdAndLeaveDateBefore(testGroupId, beforeDate);
    }

    @Test
    void testCountMembersByGroupId() throws Exception {
        when(groupMembershipService.countByGroupId(testGroupId)).thenReturn(CompletableFuture.completedFuture(4L));

        var mvcResult = mockMvc.perform(get("/api/memberships/group/{groupId}/count", testGroupId))
                .andExpect(request().asyncStarted())
                .andReturn();
                
        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(content().string("4"));

        verify(groupMembershipService, times(1)).countByGroupId(testGroupId);
    }

    @Test
    void testCountMembersByGroupIdAndStatus() throws Exception {
        when(groupMembershipService.countByGroupIdAndStatus(testGroupId, MembershipStatus.CURRENT)).thenReturn(CompletableFuture.completedFuture(3L));

        var mvcResult = mockMvc.perform(get("/api/memberships/group/{groupId}/count/{status}", testGroupId, "CURRENT"))
                .andExpect(request().asyncStarted())
                .andReturn();
                
        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(content().string("3"));

        verify(groupMembershipService, times(1)).countByGroupIdAndStatus(testGroupId, MembershipStatus.CURRENT);
    }

    @Test
    void testGetGroupsForMember() throws Exception {
        List<GroupMembership> memberships = Arrays.asList(testMembership);
        when(groupMembershipService.findGroupsForMember(testMemberId)).thenReturn(CompletableFuture.completedFuture(memberships));

        var mvcResult = mockMvc.perform(get("/api/memberships/member/{memberId}/groups", testMemberId))
                .andExpect(request().asyncStarted())
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id.memberId").value(testMemberId.toString()));

        verify(groupMembershipService, times(1)).findGroupsForMember(testMemberId);
    }

    @Test
    void testCreateMembership() throws Exception {
        when(groupMembershipService.save(any(GroupMembership.class))).thenReturn(CompletableFuture.completedFuture(testMembership));

        String jsonContent = objectMapper.writeValueAsString(testMembership);

        var mvcResult = mockMvc.perform(post("/api/memberships")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(request().asyncStarted())
                .andReturn();
                
        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id.groupId").value(testGroupId.toString()));

        verify(groupMembershipService, times(1)).save(any(GroupMembership.class));
    }

    @Test
    void testUpdateMembership() throws Exception {
        when(groupMembershipService.update(any(GroupMembership.class))).thenReturn(CompletableFuture.completedFuture(testMembership));

        String jsonContent = objectMapper.writeValueAsString(testMembership);

        var mvcResult = mockMvc.perform(put("/api/memberships")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(request().asyncStarted())
                .andReturn();
                
        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id.groupId").value(testGroupId.toString()));

        verify(groupMembershipService, times(1)).update(any(GroupMembership.class));
    }

    @Test
    void testDeleteMembership() throws Exception {
        when(groupMembershipService.delete(any(GroupMembership.class)))
                .thenReturn(CompletableFuture.completedFuture(null));

        String jsonContent = objectMapper.writeValueAsString(testMembership);

        var mvcResult = mockMvc.perform(delete("/api/memberships")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(request().asyncStarted())
                .andReturn();
                
        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isNoContent());

        verify(groupMembershipService, times(1)).delete(any(GroupMembership.class));
    }
}
