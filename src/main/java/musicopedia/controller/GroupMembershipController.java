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

@RestController
@RequestMapping("/api/memberships")
public class GroupMembershipController {

    private final GroupMembershipService groupMembershipService;

    public GroupMembershipController(GroupMembershipService groupMembershipService) {
        this.groupMembershipService = groupMembershipService;
    }

    @GetMapping("/group/{groupId}")
    public ResponseEntity<List<GroupMembership>> getMembershipsByGroupId(@PathVariable("groupId") UUID groupId) {
        return ResponseEntity.ok(groupMembershipService.findByGroupId(groupId));
    }

    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<GroupMembership>> getMembershipsByMemberId(@PathVariable("memberId") UUID memberId) {
        return ResponseEntity.ok(groupMembershipService.findByMemberId(memberId));
    }

    @GetMapping("/group/{groupId}/status/{status}")
    public ResponseEntity<List<GroupMembership>> getMembershipsByGroupIdAndStatus(
            @PathVariable("groupId") UUID groupId,
            @PathVariable("status") MembershipStatus status) {
        return ResponseEntity.ok(groupMembershipService.findByGroupIdAndStatus(groupId, status));
    }

    @GetMapping("/group/{groupId}/former-members")
    public ResponseEntity<List<GroupMembership>> getFormerMembersByGroupId(@PathVariable("groupId") UUID groupId) {
        return ResponseEntity.ok(groupMembershipService.findFormerMembersByGroupId(groupId));
    }

    @GetMapping("/group/{groupId}/joined-after")
    public ResponseEntity<List<GroupMembership>> getMembersJoinedAfter(
            @PathVariable("groupId") UUID groupId,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(groupMembershipService.findByGroupIdAndJoinDateAfter(groupId, date));
    }

    @GetMapping("/group/{groupId}/left-before")
    public ResponseEntity<List<GroupMembership>> getMembersLeftBefore(
            @PathVariable("groupId") UUID groupId,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(groupMembershipService.findByGroupIdAndLeaveDateBefore(groupId, date));
    }

    @GetMapping("/group/{groupId}/count")
    public ResponseEntity<Long> countMembersByGroupId(@PathVariable("groupId") UUID groupId) {
        return ResponseEntity.ok(groupMembershipService.countByGroupId(groupId));
    }

    @GetMapping("/group/{groupId}/count/{status}")
    public ResponseEntity<Long> countMembersByGroupIdAndStatus(
            @PathVariable("groupId") UUID groupId,
            @PathVariable("status") MembershipStatus status) {
        return ResponseEntity.ok(groupMembershipService.countByGroupIdAndStatus(groupId, status));
    }

    @GetMapping("/member/{memberId}/groups")
    public ResponseEntity<List<GroupMembership>> getGroupsForMember(@PathVariable("memberId") UUID memberId) {
        return ResponseEntity.ok(groupMembershipService.findGroupsForMember(memberId));
    }

    @PostMapping
    public ResponseEntity<GroupMembership> createMembership(@RequestBody GroupMembership membership) {
        GroupMembership savedMembership = groupMembershipService.save(membership);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedMembership);
    }

    @PutMapping
    public ResponseEntity<GroupMembership> updateMembership(@RequestBody GroupMembership membership) {
        GroupMembership updatedMembership = groupMembershipService.update(membership);
        return ResponseEntity.ok(updatedMembership);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteMembership(@RequestBody GroupMembership membership) {
        groupMembershipService.delete(membership);
        return ResponseEntity.noContent().build();
    }
}
