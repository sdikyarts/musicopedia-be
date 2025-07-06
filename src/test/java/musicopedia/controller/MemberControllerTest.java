package musicopedia.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import musicopedia.builder.MemberBuilder;
import musicopedia.dto.request.MemberRequestDTO;
import musicopedia.dto.response.MemberResponseDTO;

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
import java.util.concurrent.CompletableFuture;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;

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
    private MemberResponseDTO testMemberSummaryDTO;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        MemberController memberController = new MemberController(memberService, memberMapper);
        mockMvc = MockMvcBuilders.standaloneSetup(memberController).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        testId = UUID.randomUUID();
        testMember = new MemberBuilder()
            .setMemberName("Felix")
            .setRealName("Felix Yongbok Lee")
            .setBirthDate(LocalDate.of(2000, 9, 15))
            .build();
        testMember.setMemberId(testId);
        
        // Setup DTOs
        testMemberResponseDTO = new MemberResponseDTO();
        testMemberResponseDTO.setMemberId(testId);
        testMemberResponseDTO.setMemberName("Felix");
        testMemberResponseDTO.setRealName("Felix Yongbok Lee");
        testMemberResponseDTO.setBirthDate(LocalDate.of(2000, 9, 15));
        testMemberResponseDTO.setHasOfficialSoloDebut(false);
        
        testMemberSummaryDTO = new MemberResponseDTO();
        testMemberSummaryDTO.setMemberId(testId);
        testMemberSummaryDTO.setMemberName("Felix");
        testMemberSummaryDTO.setRealName("Felix Yongbok Lee");
        testMemberSummaryDTO.setHasOfficialSoloDebut(false);
    }

    @Test
    void testGetAllMembers() throws Exception {
        List<Member> members = Arrays.asList(testMember);
        List<MemberResponseDTO> memberSummaryDTOs = Arrays.asList(testMemberSummaryDTO);
        
        when(memberService.findAll()).thenReturn(CompletableFuture.completedFuture(members));
        when(memberMapper.toSummaryDTOList(members)).thenReturn(memberSummaryDTOs);

        var result = mockMvc.perform(get("/api/members"))
                .andExpect(request().asyncStarted());

        mockMvc.perform(asyncDispatch(result.andReturn()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].memberId").value(testId.toString()))
                .andExpect(jsonPath("$[0].memberName").value("Felix"))
                .andExpect(jsonPath("$[0].realName").value("Felix Yongbok Lee"));

        verify(memberService, times(1)).findAll();
        verify(memberMapper, times(1)).toSummaryDTOList(members);
    }

    @Test
    void testGetMemberById() throws Exception {
        when(memberService.findById(testId)).thenReturn(CompletableFuture.completedFuture(Optional.of(testMember)));
        when(memberMapper.toResponseDTO(testMember)).thenReturn(testMemberResponseDTO);

        var result = mockMvc.perform(get("/api/members/{id}", testId))
                .andExpect(request().asyncStarted());

        mockMvc.perform(asyncDispatch(result.andReturn()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.memberId").value(testId.toString()))
                .andExpect(jsonPath("$.memberName").value("Felix"))
                .andExpect(jsonPath("$.realName").value("Felix Yongbok Lee"));

        verify(memberService, times(1)).findById(testId);
        verify(memberMapper, times(1)).toResponseDTO(testMember);
    }

    @Test
    void testGetMemberByIdNotFound() throws Exception {
        when(memberService.findById(testId)).thenReturn(CompletableFuture.completedFuture(Optional.empty()));

        var result = mockMvc.perform(get("/api/members/{id}", testId))
                .andExpect(request().asyncStarted());

        mockMvc.perform(asyncDispatch(result.andReturn()))
                .andExpect(status().isNotFound());

        verify(memberService, times(1)).findById(testId);
    }

    @Test
    void testSearchMembersByName() throws Exception {
        List<Member> members = Arrays.asList(testMember);
        List<MemberResponseDTO> memberSummaryDTOs = Arrays.asList(testMemberSummaryDTO);
        
        when(memberService.findByNameContaining("Felix")).thenReturn(CompletableFuture.completedFuture(members));
        when(memberMapper.toSummaryDTOList(members)).thenReturn(memberSummaryDTOs);

        var result = mockMvc.perform(get("/api/members/search")
                        .param("name", "Felix"))
                .andExpect(request().asyncStarted());

        mockMvc.perform(asyncDispatch(result.andReturn()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].memberName").value("Felix"))
                .andExpect(jsonPath("$[0].realName").value("Felix Yongbok Lee"));

        verify(memberService, times(1)).findByNameContaining("Felix");
        verify(memberMapper, times(1)).toSummaryDTOList(members);
    }

    @Test
    void testSearchMembersByRealName() throws Exception {
        List<Member> members = Arrays.asList(testMember);
        List<MemberResponseDTO> memberSummaryDTOs = Arrays.asList(testMemberSummaryDTO);
        
        when(memberService.findByRealNameContaining("Felix Yongbok Lee")).thenReturn(CompletableFuture.completedFuture(members));
        when(memberMapper.toSummaryDTOList(members)).thenReturn(memberSummaryDTOs);

        var result = mockMvc.perform(get("/api/members/search/realname")
                        .param("realName", "Felix Yongbok Lee"))
                .andExpect(request().asyncStarted());

        mockMvc.perform(asyncDispatch(result.andReturn()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].memberName").value("Felix"))
                .andExpect(jsonPath("$[0].realName").value("Felix Yongbok Lee"));

        verify(memberService, times(1)).findByRealNameContaining("Felix Yongbok Lee");
        verify(memberMapper, times(1)).toSummaryDTOList(members);
    }

    @Test
    void testGetMembersByBirthDateRange() throws Exception {
        List<Member> members = Arrays.asList(testMember);
        List<MemberResponseDTO> memberSummaryDTOs = Arrays.asList(testMemberSummaryDTO);
        LocalDate startDate = LocalDate.of(1990, 1, 1);
        LocalDate endDate = LocalDate.of(1996, 12, 31);
        
        when(memberService.findByBirthDateBetween(startDate, endDate)).thenReturn(CompletableFuture.completedFuture(members));
        when(memberMapper.toSummaryDTOList(members)).thenReturn(memberSummaryDTOs);

        var result = mockMvc.perform(get("/api/members/birthdate")
                        .param("start", "1990-01-01")
                        .param("end", "1996-12-31"))
                .andExpect(request().asyncStarted());

        mockMvc.perform(asyncDispatch(result.andReturn()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].memberId").value(testId.toString()));

        verify(memberService, times(1)).findByBirthDateBetween(startDate, endDate);
        verify(memberMapper, times(1)).toSummaryDTOList(members);
    }

    @Test
    void testGetMembersWithSoloCareer() throws Exception {
        List<Member> members = Arrays.asList(testMember);
        List<MemberResponseDTO> memberSummaryDTOs = Arrays.asList(testMemberSummaryDTO);
        
        when(memberService.findBySoloArtistNotNull()).thenReturn(CompletableFuture.completedFuture(members));
        when(memberMapper.toSummaryDTOList(members)).thenReturn(memberSummaryDTOs);

        var result = mockMvc.perform(get("/api/members/with-solo-career"))
                .andExpect(request().asyncStarted());

        mockMvc.perform(asyncDispatch(result.andReturn()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].memberId").value(testId.toString()));

        verify(memberService, times(1)).findBySoloArtistNotNull();
        verify(memberMapper, times(1)).toSummaryDTOList(members);
    }

    @Test
    void testCreateMember() throws Exception {
        MemberRequestDTO createDTO = new MemberRequestDTO();
        createDTO.setMemberName("Felix");
        createDTO.setRealName("Felix Yongbok Lee");
        createDTO.setBirthDate(LocalDate.of(2000, 9, 15));
        
        when(memberMapper.toEntity(any(MemberRequestDTO.class))).thenReturn(CompletableFuture.completedFuture(testMember));
        when(memberService.save(any(Member.class))).thenReturn(CompletableFuture.completedFuture(testMember));
        when(memberMapper.toResponseDTO(testMember)).thenReturn(testMemberResponseDTO);

        String jsonContent = objectMapper.writeValueAsString(createDTO);

        var result = mockMvc.perform(post("/api/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(request().asyncStarted());

        mockMvc.perform(asyncDispatch(result.andReturn()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.memberId").value(testId.toString()));

        verify(memberMapper, times(1)).toEntity(any(MemberRequestDTO.class));
        verify(memberService, times(1)).save(any(Member.class));
        verify(memberMapper, times(1)).toResponseDTO(testMember);
    }

    @Test
    void testUpdateMember() throws Exception {
        MemberRequestDTO updateDTO = new MemberRequestDTO();
        updateDTO.setMemberId(testId);
        updateDTO.setMemberName("Felix");
        updateDTO.setRealName("Felix Yongbok Lee");
        updateDTO.setBirthDate(LocalDate.of(2000, 9, 15));
        
        when(memberService.findById(testId)).thenReturn(CompletableFuture.completedFuture(Optional.of(testMember)));
        when(memberMapper.updateEntityFromDto(any(Member.class), any(MemberRequestDTO.class))).thenReturn(CompletableFuture.completedFuture(null));
        when(memberService.update(any(Member.class))).thenReturn(CompletableFuture.completedFuture(testMember));
        when(memberMapper.toResponseDTO(testMember)).thenReturn(testMemberResponseDTO);

        String jsonContent = objectMapper.writeValueAsString(updateDTO);

        var result = mockMvc.perform(put("/api/members/{id}", testId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(request().asyncStarted());

        mockMvc.perform(asyncDispatch(result.andReturn()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.memberId").value(testId.toString()));

        verify(memberService, times(1)).findById(testId);
        verify(memberMapper, times(1)).updateEntityFromDto(any(Member.class), any(MemberRequestDTO.class));
        verify(memberService, times(1)).update(any(Member.class));
        verify(memberMapper, times(1)).toResponseDTO(testMember);
    }

    @Test
    void testUpdateMemberWithMismatchedId() throws Exception {
        UUID differentId = UUID.randomUUID();
        MemberRequestDTO updateDTO = new MemberRequestDTO();
        updateDTO.setMemberId(differentId);

        String jsonContent = objectMapper.writeValueAsString(updateDTO);

        var result = mockMvc.perform(put("/api/members/{id}", testId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(request().asyncStarted());

        mockMvc.perform(asyncDispatch(result.andReturn()))
                .andExpect(status().isBadRequest());

        verify(memberService, never()).findById(any(UUID.class));
        verify(memberService, never()).update(any(Member.class));
    }

    @Test
    void testUpdateMemberNotFound() throws Exception {
        MemberRequestDTO updateDTO = new MemberRequestDTO();
        updateDTO.setMemberId(testId);
        
        when(memberService.findById(testId)).thenReturn(CompletableFuture.completedFuture(Optional.empty()));

        String jsonContent = objectMapper.writeValueAsString(updateDTO);

        var result = mockMvc.perform(put("/api/members/{id}", testId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(request().asyncStarted());

        mockMvc.perform(asyncDispatch(result.andReturn()))
                .andExpect(status().isNotFound());

        verify(memberService, times(1)).findById(testId);
        verify(memberService, never()).update(any(Member.class));
    }

    @Test
    void testUpdateMemberWithNullMemberIdInBody() throws Exception {
        MemberRequestDTO updateDTO = new MemberRequestDTO();
        updateDTO.setMemberId(null); // memberId is null in request body
        updateDTO.setMemberName("Updated Name");
        updateDTO.setRealName("Updated Real Name");
        updateDTO.setBirthDate(LocalDate.of(2000, 9, 15));
        
        when(memberService.findById(testId)).thenReturn(CompletableFuture.completedFuture(Optional.of(testMember)));
        when(memberMapper.updateEntityFromDto(any(Member.class), any(MemberRequestDTO.class))).thenReturn(CompletableFuture.completedFuture(null));
        when(memberService.update(any(Member.class))).thenReturn(CompletableFuture.completedFuture(testMember));
        when(memberMapper.toResponseDTO(testMember)).thenReturn(testMemberResponseDTO);

        String jsonContent = objectMapper.writeValueAsString(updateDTO);

        var result = mockMvc.perform(put("/api/members/{id}", testId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(request().asyncStarted());

        mockMvc.perform(asyncDispatch(result.andReturn()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.memberId").value(testId.toString()));

        verify(memberService, times(1)).findById(testId);
        verify(memberMapper, times(1)).updateEntityFromDto(any(Member.class), any(MemberRequestDTO.class));
        verify(memberService, times(1)).update(any(Member.class));
        verify(memberMapper, times(1)).toResponseDTO(testMember);
    }

    @Test
    void testDeleteMember() throws Exception {
        when(memberService.deleteById(testId)).thenReturn(CompletableFuture.completedFuture(null));

        var result = mockMvc.perform(delete("/api/members/{id}", testId))
                .andExpect(request().asyncStarted());

        mockMvc.perform(asyncDispatch(result.andReturn()))
                .andExpect(status().isNoContent());

        verify(memberService, times(1)).deleteById(testId);
    }

    @Test
    void testSearchMembersByNationality() throws Exception {
        List<Member> members = Arrays.asList(testMember);
        List<MemberResponseDTO> memberSummaryDTOs = Arrays.asList(testMemberSummaryDTO);
        when(memberService.findByNationality("KR")).thenReturn(CompletableFuture.completedFuture(members));
        when(memberMapper.toSummaryDTOList(members)).thenReturn(memberSummaryDTOs);

        var result = mockMvc.perform(get("/api/members/search/nationality")
                        .param("nationality", "KR"))
                .andExpect(request().asyncStarted());

        mockMvc.perform(asyncDispatch(result.andReturn()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].memberName").value("Felix"));

        verify(memberService, times(1)).findByNationality("KR");
        verify(memberMapper, times(1)).toSummaryDTOList(members);
    }
}
