package musicopedia.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import musicopedia.dto.request.CreateMemberRequestDTO;
import musicopedia.dto.request.UpdateMemberRequestDTO;
import musicopedia.dto.response.MemberResponseDTO;
import musicopedia.dto.response.MemberSummaryDTO;
import musicopedia.mapper.MemberMapper;
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

    @Mock
    private MemberMapper memberMapper;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private UUID testId;
    private Member testMember;
    private MemberResponseDTO testMemberResponseDTO;
    private MemberSummaryDTO testMemberSummaryDTO;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        MemberController memberController = new MemberController(memberService, memberMapper);
        mockMvc = MockMvcBuilders.standaloneSetup(memberController).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        testId = UUID.randomUUID();
        testMember = new Member();
        testMember.setMemberId(testId);
        testMember.setFullName("Jisoo");
        testMember.setBirthDate(LocalDate.of(1995, 1, 3));
        
        // Setup DTOs
        testMemberResponseDTO = new MemberResponseDTO();
        testMemberResponseDTO.setMemberId(testId);
        testMemberResponseDTO.setFullName("Jisoo");
        testMemberResponseDTO.setBirthDate(LocalDate.of(1995, 1, 3));
        testMemberResponseDTO.setHasOfficialSoloDebut(false);
        
        testMemberSummaryDTO = new MemberSummaryDTO();
        testMemberSummaryDTO.setMemberId(testId);
        testMemberSummaryDTO.setFullName("Jisoo");
        testMemberSummaryDTO.setHasOfficialSoloDebut(false);
    }

    @Test
    void testGetAllMembers() throws Exception {
        List<Member> members = Arrays.asList(testMember);
        List<MemberSummaryDTO> memberSummaryDTOs = Arrays.asList(testMemberSummaryDTO);
        
        when(memberService.findAll()).thenReturn(members);
        when(memberMapper.toSummaryDTOList(members)).thenReturn(memberSummaryDTOs);

        mockMvc.perform(get("/api/members"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].memberId").value(testId.toString()))
                .andExpect(jsonPath("$[0].fullName").value("Jisoo"));

        verify(memberService, times(1)).findAll();
        verify(memberMapper, times(1)).toSummaryDTOList(members);
    }

    @Test
    void testGetMemberById() throws Exception {
        when(memberService.findById(testId)).thenReturn(Optional.of(testMember));
        when(memberMapper.toResponseDTO(testMember)).thenReturn(testMemberResponseDTO);

        mockMvc.perform(get("/api/members/{id}", testId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.memberId").value(testId.toString()))
                .andExpect(jsonPath("$.fullName").value("Jisoo"));

        verify(memberService, times(1)).findById(testId);
        verify(memberMapper, times(1)).toResponseDTO(testMember);
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
        List<MemberSummaryDTO> memberSummaryDTOs = Arrays.asList(testMemberSummaryDTO);
        
        when(memberService.findByNameContaining("Jisoo")).thenReturn(members);
        when(memberMapper.toSummaryDTOList(members)).thenReturn(memberSummaryDTOs);

        mockMvc.perform(get("/api/members/search")
                        .param("name", "Jisoo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].fullName").value("Jisoo"));

        verify(memberService, times(1)).findByNameContaining("Jisoo");
        verify(memberMapper, times(1)).toSummaryDTOList(members);
    }

    @Test
    void testGetMembersByBirthDateRange() throws Exception {
        List<Member> members = Arrays.asList(testMember);
        List<MemberSummaryDTO> memberSummaryDTOs = Arrays.asList(testMemberSummaryDTO);
        LocalDate startDate = LocalDate.of(1990, 1, 1);
        LocalDate endDate = LocalDate.of(1996, 12, 31);
        
        when(memberService.findByBirthDateBetween(startDate, endDate)).thenReturn(members);
        when(memberMapper.toSummaryDTOList(members)).thenReturn(memberSummaryDTOs);

        mockMvc.perform(get("/api/members/birthdate")
                        .param("start", "1990-01-01")
                        .param("end", "1996-12-31"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].memberId").value(testId.toString()));

        verify(memberService, times(1)).findByBirthDateBetween(startDate, endDate);
        verify(memberMapper, times(1)).toSummaryDTOList(members);
    }

    @Test
    void testGetMembersWithSoloCareer() throws Exception {
        List<Member> members = Arrays.asList(testMember);
        List<MemberSummaryDTO> memberSummaryDTOs = Arrays.asList(testMemberSummaryDTO);
        
        when(memberService.findBySoloArtistNotNull()).thenReturn(members);
        when(memberMapper.toSummaryDTOList(members)).thenReturn(memberSummaryDTOs);

        mockMvc.perform(get("/api/members/with-solo-career"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].memberId").value(testId.toString()));

        verify(memberService, times(1)).findBySoloArtistNotNull();
        verify(memberMapper, times(1)).toSummaryDTOList(members);
    }

    @Test
    void testCreateMember() throws Exception {
        CreateMemberRequestDTO createDTO = new CreateMemberRequestDTO();
        createDTO.setFullName("Jisoo");
        createDTO.setBirthDate(LocalDate.of(1995, 1, 3));
        
        when(memberMapper.toEntity(any(CreateMemberRequestDTO.class))).thenReturn(testMember);
        when(memberService.save(any(Member.class))).thenReturn(testMember);
        when(memberMapper.toResponseDTO(testMember)).thenReturn(testMemberResponseDTO);

        String jsonContent = objectMapper.writeValueAsString(createDTO);

        mockMvc.perform(post("/api/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.memberId").value(testId.toString()));

        verify(memberMapper, times(1)).toEntity(any(CreateMemberRequestDTO.class));
        verify(memberService, times(1)).save(any(Member.class));
        verify(memberMapper, times(1)).toResponseDTO(testMember);
    }

    @Test
    void testUpdateMember() throws Exception {
        UpdateMemberRequestDTO updateDTO = new UpdateMemberRequestDTO();
        updateDTO.setMemberId(testId);
        updateDTO.setFullName("Jisoo");
        updateDTO.setBirthDate(LocalDate.of(1995, 1, 3));
        
        when(memberService.findById(testId)).thenReturn(Optional.of(testMember));
        when(memberService.update(any(Member.class))).thenReturn(testMember);
        when(memberMapper.toResponseDTO(testMember)).thenReturn(testMemberResponseDTO);

        String jsonContent = objectMapper.writeValueAsString(updateDTO);

        mockMvc.perform(put("/api/members/{id}", testId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.memberId").value(testId.toString()));

        verify(memberService, times(1)).findById(testId);
        verify(memberMapper, times(1)).updateEntityFromDto(any(Member.class), any(UpdateMemberRequestDTO.class));
        verify(memberService, times(1)).update(any(Member.class));
        verify(memberMapper, times(1)).toResponseDTO(testMember);
    }

    @Test
    void testUpdateMemberWithMismatchedId() throws Exception {
        UUID differentId = UUID.randomUUID();
        UpdateMemberRequestDTO updateDTO = new UpdateMemberRequestDTO();
        updateDTO.setMemberId(differentId);

        String jsonContent = objectMapper.writeValueAsString(updateDTO);

        mockMvc.perform(put("/api/members/{id}", testId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isBadRequest());

        verify(memberService, never()).findById(any(UUID.class));
        verify(memberService, never()).update(any(Member.class));
    }

    @Test
    void testUpdateMemberNotFound() throws Exception {
        UpdateMemberRequestDTO updateDTO = new UpdateMemberRequestDTO();
        updateDTO.setMemberId(testId);
        
        when(memberService.findById(testId)).thenReturn(Optional.empty());

        String jsonContent = objectMapper.writeValueAsString(updateDTO);

        mockMvc.perform(put("/api/members/{id}", testId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isNotFound());

        verify(memberService, times(1)).findById(testId);
        verify(memberService, never()).update(any(Member.class));
    }

    @Test
    void testDeleteMember() throws Exception {
        doNothing().when(memberService).deleteById(testId);

        mockMvc.perform(delete("/api/members/{id}", testId))
                .andExpect(status().isNoContent());

        verify(memberService, times(1)).deleteById(testId);
    }
}
