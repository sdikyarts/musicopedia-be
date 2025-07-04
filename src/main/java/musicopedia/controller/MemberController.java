package musicopedia.controller;

import musicopedia.dto.request.MemberRequestDTO;
import musicopedia.dto.response.MemberResponseDTO;

import musicopedia.mapper.MemberMapper;
import musicopedia.model.Member;
import musicopedia.service.MemberService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;
    private final MemberMapper memberMapper;

    public MemberController(MemberService memberService, MemberMapper memberMapper) {
        this.memberService = memberService;
        this.memberMapper = memberMapper;
    }

    @GetMapping
    public ResponseEntity<List<MemberResponseDTO>> getAllMembers() {
        List<Member> members = memberService.findAll();
        return ResponseEntity.ok(memberMapper.toSummaryDTOList(members));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemberResponseDTO> getMemberById(@PathVariable("id") UUID memberId) {
        Optional<Member> member = memberService.findById(memberId);
        return member.map(m -> ResponseEntity.ok(memberMapper.toResponseDTO(m)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<List<MemberResponseDTO>> searchMembersByName(@RequestParam("name") String name) {
        List<Member> members = memberService.findByNameContaining(name);
        return ResponseEntity.ok(memberMapper.toSummaryDTOList(members));
    }

    @GetMapping("/birthdate")
    public ResponseEntity<List<MemberResponseDTO>> getMembersByBirthDateRange(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<Member> members = memberService.findByBirthDateBetween(startDate, endDate);
        return ResponseEntity.ok(memberMapper.toSummaryDTOList(members));
    }

    @GetMapping("/with-solo-career")
    public ResponseEntity<List<MemberResponseDTO>> getMembersWithSoloCareer() {
        List<Member> members = memberService.findBySoloArtistNotNull();
        return ResponseEntity.ok(memberMapper.toSummaryDTOList(members));
    }

    @PostMapping
    public ResponseEntity<MemberResponseDTO> createMember(@RequestBody MemberRequestDTO createMemberRequestDTO) {
        Member member = memberMapper.toEntity(createMemberRequestDTO);
        Member savedMember = memberService.save(member);
        return ResponseEntity.status(HttpStatus.CREATED).body(memberMapper.toResponseDTO(savedMember));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MemberResponseDTO> updateMember(@PathVariable("id") UUID memberId, @RequestBody MemberRequestDTO updateMemberRequestDTO) {
        // If memberId is provided in the request body, it must match the path variable
        if (updateMemberRequestDTO.getMemberId() != null) {
            if (!updateMemberRequestDTO.getMemberId().equals(memberId)) {
                return ResponseEntity.badRequest().build();
            }
        } else {
            // Set the memberId from the path variable if not provided in the request body
            updateMemberRequestDTO.setMemberId(memberId);
        }
        
        Optional<Member> existingMember = memberService.findById(memberId);
        if (existingMember.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        Member member = existingMember.get();
        memberMapper.updateEntityFromDto(member, updateMemberRequestDTO);
        Member updatedMember = memberService.update(member);
        return ResponseEntity.ok(memberMapper.toResponseDTO(updatedMember));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable("id") UUID memberId) {
        memberService.deleteById(memberId);
        return ResponseEntity.noContent().build();
    }
}
