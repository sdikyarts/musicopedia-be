package musicopedia.controller;

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
@CrossOrigin(origins = "*")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping
    public ResponseEntity<List<Member>> getAllMembers() {
        return ResponseEntity.ok(memberService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Member> getMemberById(@PathVariable("id") UUID memberId) {
        Optional<Member> member = memberService.findById(memberId);
        return member.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<List<Member>> searchMembersByName(@RequestParam("name") String name) {
        return ResponseEntity.ok(memberService.findByNameContaining(name));
    }

    @GetMapping("/birthdate")
    public ResponseEntity<List<Member>> getMembersByBirthDateRange(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(memberService.findByBirthDateBetween(startDate, endDate));
    }

    @GetMapping("/with-solo-career")
    public ResponseEntity<List<Member>> getMembersWithSoloCareer() {
        return ResponseEntity.ok(memberService.findBySoloArtistNotNull());
    }

    @PostMapping
    public ResponseEntity<Member> createMember(@RequestBody Member member) {
        Member savedMember = memberService.save(member);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedMember);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Member> updateMember(@PathVariable("id") UUID memberId, @RequestBody Member member) {
        if (!member.getMemberId().equals(memberId)) {
            return ResponseEntity.badRequest().build();
        }
        
        Member updatedMember = memberService.update(member);
        if (updatedMember == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedMember);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable("id") UUID memberId) {
        memberService.deleteById(memberId);
        return ResponseEntity.noContent().build();
    }
}
