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
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

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
    public CompletableFuture<ResponseEntity<List<MemberResponseDTO>>> getAllMembers() {
        return memberService.findAll()
                .thenApply(members -> ResponseEntity.ok(memberMapper.toSummaryDTOList(members)));
    }

    @GetMapping("/{id}")
    public CompletableFuture<ResponseEntity<MemberResponseDTO>> getMemberById(@PathVariable("id") UUID memberId) {
        return memberService.findById(memberId)
                .thenApply(member -> member.map(m -> ResponseEntity.ok(memberMapper.toResponseDTO(m)))
                        .orElse(ResponseEntity.notFound().build()));
    }

    @GetMapping("/search")
    public CompletableFuture<ResponseEntity<List<MemberResponseDTO>>> searchMembersByName(@RequestParam("name") String name) {
        return memberService.findByNameContaining(name)
                .thenApply(members -> ResponseEntity.ok(memberMapper.toSummaryDTOList(members)));
    }

    @GetMapping("/birthdate")
    public CompletableFuture<ResponseEntity<List<MemberResponseDTO>>> getMembersByBirthDateRange(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return memberService.findByBirthDateBetween(startDate, endDate)
                .thenApply(members -> ResponseEntity.ok(memberMapper.toSummaryDTOList(members)));
    }

    @GetMapping("/with-solo-career")
    public CompletableFuture<ResponseEntity<List<MemberResponseDTO>>> getMembersWithSoloCareer() {
        return memberService.findBySoloArtistNotNull()
                .thenApply(members -> ResponseEntity.ok(memberMapper.toSummaryDTOList(members)));
    }

    @PostMapping
    public CompletableFuture<ResponseEntity<MemberResponseDTO>> createMember(@RequestBody MemberRequestDTO createMemberRequestDTO) {
        return memberMapper.toEntity(createMemberRequestDTO)
                .thenCompose(memberService::save)
                .thenApply(savedMember -> ResponseEntity.status(HttpStatus.CREATED).body(memberMapper.toResponseDTO(savedMember)));
    }

    @PutMapping("/{id}")
    public CompletableFuture<ResponseEntity<MemberResponseDTO>> updateMember(@PathVariable("id") UUID memberId, @RequestBody MemberRequestDTO updateMemberRequestDTO) {
        // If memberId is provided in the request body, it must match the path variable
        if (updateMemberRequestDTO.getMemberId() != null) {
            if (!updateMemberRequestDTO.getMemberId().equals(memberId)) {
                return CompletableFuture.completedFuture(ResponseEntity.badRequest().build());
            }
        } else {
            // Set the memberId from the path variable if not provided in the request body
            updateMemberRequestDTO.setMemberId(memberId);
        }
        
        return memberService.findById(memberId)
                .thenCompose(existingMember -> {
                    if (existingMember.isEmpty()) {
                        return CompletableFuture.completedFuture(ResponseEntity.notFound().<MemberResponseDTO>build());
                    }
                    
                    Member member = existingMember.get();
                    return memberMapper.updateEntityFromDto(member, updateMemberRequestDTO)
                            .thenCompose(v -> memberService.update(member))
                            .thenApply(updatedMember -> ResponseEntity.ok(memberMapper.toResponseDTO(updatedMember)));
                });
    }

    @DeleteMapping("/{id}")
    public CompletableFuture<ResponseEntity<Void>> deleteMember(@PathVariable("id") UUID memberId) {
        return memberService.deleteById(memberId)
                .thenApply(v -> ResponseEntity.noContent().<Void>build());
    }
}
