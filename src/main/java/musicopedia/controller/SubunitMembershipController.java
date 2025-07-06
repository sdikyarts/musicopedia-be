package musicopedia.controller;

import musicopedia.model.membership.SubunitMembership;
import musicopedia.service.SubunitMembershipService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/subunit-memberships")
public class SubunitMembershipController {
    private final SubunitMembershipService service;

    public SubunitMembershipController(SubunitMembershipService service) {
        this.service = service;
    }

    @GetMapping("/subunit/{subunitId}")
    public CompletableFuture<ResponseEntity<List<SubunitMembership>>> getBySubunit(@PathVariable UUID subunitId) {
        return service.findBySubunitId(subunitId).thenApply(ResponseEntity::ok);
    }

    @GetMapping("/member/{memberId}")
    public CompletableFuture<ResponseEntity<List<SubunitMembership>>> getByMember(@PathVariable UUID memberId) {
        return service.findByMemberId(memberId).thenApply(ResponseEntity::ok);
    }

    @DeleteMapping("/subunit/{subunitId}")
    public CompletableFuture<ResponseEntity<Void>> deleteBySubunit(@PathVariable UUID subunitId) {
        return service.deleteBySubunitId(subunitId).thenApply(v -> ResponseEntity.noContent().build());
    }

    @DeleteMapping("/member/{memberId}")
    public CompletableFuture<ResponseEntity<Void>> deleteByMember(@PathVariable UUID memberId) {
        return service.deleteByMemberId(memberId).thenApply(v -> ResponseEntity.noContent().build());
    }

    @GetMapping("/exists")
    public CompletableFuture<ResponseEntity<Boolean>> exists(@RequestParam UUID subunitId, @RequestParam UUID memberId) {
        return service.existsBySubunitIdAndMemberId(subunitId, memberId).thenApply(ResponseEntity::ok);
    }
}
