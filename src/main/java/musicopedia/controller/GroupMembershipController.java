package musicopedia.controller;

import musicopedia.model.enums.MembershipStatus;
import musicopedia.model.membership.GroupMembership;
import musicopedia.service.GroupMembershipService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/memberships")
public class GroupMembershipController {

    private final GroupMembershipService groupMembershipService;

    public GroupMembershipController(GroupMembershipService groupMembershipService) {
        this.groupMembershipService = groupMembershipService;
    }

    @GetMapping("/group/{groupId}")
    public CompletableFuture<ResponseEntity<List<GroupMembership>>> getMembershipsByGroupId(@PathVariable("groupId") UUID groupId) {
        return groupMembershipService.findByGroupId(groupId)
                .thenApply(ResponseEntity::ok);
    }

    @GetMapping("/member/{memberId}")
    public CompletableFuture<ResponseEntity<List<GroupMembership>>> getMembershipsByMemberId(@PathVariable("memberId") UUID memberId) {
        return groupMembershipService.findByMemberId(memberId)
                .thenApply(ResponseEntity::ok);
    }

    @GetMapping("/group/{groupId}/status/{status}")
    public CompletableFuture<ResponseEntity<List<GroupMembership>>> getMembershipsByGroupIdAndStatus(
            @PathVariable("groupId") UUID groupId,
            @PathVariable("status") MembershipStatus status) {
        return groupMembershipService.findByGroupIdAndStatus(groupId, status)
                .thenApply(ResponseEntity::ok);
    }

    @GetMapping("/group/{groupId}/former-members")
    public CompletableFuture<ResponseEntity<List<GroupMembership>>> getFormerMembersByGroupId(@PathVariable("groupId") UUID groupId) {
        return groupMembershipService.findFormerMembersByGroupId(groupId)
                .thenApply(ResponseEntity::ok);
    }

    @GetMapping("/group/{groupId}/joined-after")
    public CompletableFuture<ResponseEntity<List<GroupMembership>>> getMembersJoinedAfter(
            @PathVariable("groupId") UUID groupId,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return groupMembershipService.findByGroupIdAndJoinDateAfter(groupId, date)
                .thenApply(ResponseEntity::ok);
    }

    @GetMapping("/group/{groupId}/left-before")
    public CompletableFuture<ResponseEntity<List<GroupMembership>>> getMembersLeftBefore(
            @PathVariable("groupId") UUID groupId,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return groupMembershipService.findByGroupIdAndLeaveDateBefore(groupId, date)
                .thenApply(ResponseEntity::ok);
    }

    @GetMapping("/group/{groupId}/count")
    public CompletableFuture<ResponseEntity<Long>> countMembersByGroupId(@PathVariable("groupId") UUID groupId) {
        return groupMembershipService.countByGroupId(groupId)
                .thenApply(ResponseEntity::ok);
    }

    @GetMapping("/group/{groupId}/count/{status}")
    public CompletableFuture<ResponseEntity<Long>> countMembersByGroupIdAndStatus(
            @PathVariable("groupId") UUID groupId,
            @PathVariable("status") MembershipStatus status) {
        return groupMembershipService.countByGroupIdAndStatus(groupId, status)
                .thenApply(ResponseEntity::ok);
    }

    @GetMapping("/member/{memberId}/groups")
    public CompletableFuture<ResponseEntity<List<GroupMembership>>> getGroupsForMember(@PathVariable("memberId") UUID memberId) {
        return groupMembershipService.findGroupsForMember(memberId)
                .thenApply(ResponseEntity::ok);
    }

    @PostMapping
    public CompletableFuture<ResponseEntity<GroupMembership>> createMembership(@RequestBody GroupMembership membership) {
        return groupMembershipService.save(membership)
                .thenApply(savedMembership -> ResponseEntity.status(HttpStatus.CREATED).body(savedMembership));
    }

    @PutMapping
    public CompletableFuture<ResponseEntity<GroupMembership>> updateMembership(@RequestBody GroupMembership membership) {
        return groupMembershipService.update(membership)
                .thenApply(ResponseEntity::ok);
    }

    @DeleteMapping
    public CompletableFuture<ResponseEntity<Void>> deleteMembership(@RequestBody GroupMembership membership) {
        return groupMembershipService.delete(membership)
                .thenApply(v -> ResponseEntity.noContent().<Void>build());
    }
}
