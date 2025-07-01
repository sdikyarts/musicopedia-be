package musicopedia.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import musicopedia.model.Member;
import musicopedia.service.MemberService;
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
public class MemberControllerTest {

    @Mock
    private MemberService memberService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private UUID testId;
    private Member testMember;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        MemberController memberController = new MemberController(memberService);
        mockMvc = MockMvcBuilders.standaloneSetup(memberController).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        testId = UUID.randomUUID();
        testMember = new Member();
        testMember.setMemberId(testId);
        testMember.setFullName("Jisoo");
        testMember.setBirthDate(LocalDate.of(1995, 1, 3));
    }

    @Test
    void testGetAllMembers() throws Exception {
        List<Member> members = Arrays.asList(testMember);
        when(memberService.findAll()).thenReturn(members);

        mockMvc.perform(get("/api/members"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].memberId").value(testId.toString()));

        verify(memberService, times(1)).findAll();
    }

    @Test
    void testGetMemberById() throws Exception {
        when(memberService.findById(testId)).thenReturn(Optional.of(testMember));

        mockMvc.perform(get("/api/members/{id}", testId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.memberId").value(testId.toString()))
                .andExpect(jsonPath("$.fullName").value("Jisoo"));

        verify(memberService, times(1)).findById(testId);
    }

    @Test
    void testGetMemberByIdNotFound() throws Exception {
        when(memberService.findById(testId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/members/{id}", testId))
                .andExpect(status().isNotFound());

        verify(memberService, times(1)).findById(testId);
    }

    @Test
    void testSearchMembersByName() throws Exception {
        List<Member> members = Arrays.asList(testMember);
        when(memberService.findByNameContaining("Jisoo")).thenReturn(members);

        mockMvc.perform(get("/api/members/search")
                        .param("name", "Jisoo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].fullName").value("Jisoo"));

        verify(memberService, times(1)).findByNameContaining("Jisoo");
    }

    @Test
    void testGetMembersByBirthDateRange() throws Exception {
        List<Member> members = Arrays.asList(testMember);
        LocalDate startDate = LocalDate.of(1990, 1, 1);
        LocalDate endDate = LocalDate.of(1996, 12, 31);
        when(memberService.findByBirthDateBetween(startDate, endDate)).thenReturn(members);

        mockMvc.perform(get("/api/members/birthdate")
                        .param("start", "1990-01-01")
                        .param("end", "1996-12-31"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].memberId").value(testId.toString()));

        verify(memberService, times(1)).findByBirthDateBetween(startDate, endDate);
    }

    @Test
    void testGetMembersWithSoloCareer() throws Exception {
        List<Member> members = Arrays.asList(testMember);
        when(memberService.findBySoloArtistNotNull()).thenReturn(members);

        mockMvc.perform(get("/api/members/with-solo-career"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].memberId").value(testId.toString()));

        verify(memberService, times(1)).findBySoloArtistNotNull();
    }

    @Test
    void testCreateMember() throws Exception {
        when(memberService.save(any(Member.class))).thenReturn(testMember);

        String jsonContent = objectMapper.writeValueAsString(testMember);

        mockMvc.perform(post("/api/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.memberId").value(testId.toString()));

        verify(memberService, times(1)).save(any(Member.class));
    }

    @Test
    void testUpdateMember() throws Exception {
        when(memberService.update(any(Member.class))).thenReturn(testMember);

        String jsonContent = objectMapper.writeValueAsString(testMember);

        mockMvc.perform(put("/api/members/{id}", testId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.memberId").value(testId.toString()));

        verify(memberService, times(1)).update(any(Member.class));
    }

    @Test
    void testUpdateMemberWithMismatchedId() throws Exception {
        UUID differentId = UUID.randomUUID();
        testMember.setMemberId(differentId);

        String jsonContent = objectMapper.writeValueAsString(testMember);

        mockMvc.perform(put("/api/members/{id}", testId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isBadRequest());

        verify(memberService, never()).update(any(Member.class));
    }

    @Test
    void testUpdateMemberNotFound() throws Exception {
        when(memberService.update(any(Member.class))).thenReturn(null);

        String jsonContent = objectMapper.writeValueAsString(testMember);

        mockMvc.perform(put("/api/members/{id}", testId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isNotFound());

        verify(memberService, times(1)).update(any(Member.class));
    }

    @Test
    void testDeleteMember() throws Exception {
        doNothing().when(memberService).deleteById(testId);

        mockMvc.perform(delete("/api/members/{id}", testId))
                .andExpect(status().isNoContent());

        verify(memberService, times(1)).deleteById(testId);
    }
}
