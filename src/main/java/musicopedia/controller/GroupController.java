package musicopedia.controller;

import musicopedia.model.Artist;
import musicopedia.model.Groups;
import musicopedia.model.enums.ArtistGender;
import musicopedia.service.GroupService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/groups")
public class GroupController {

    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping
    public CompletableFuture<ResponseEntity<List<Groups>>> getAllGroups() {
        return groupService.findAll()
                .thenApply(ResponseEntity::ok);
    }

    @GetMapping("/{id}")
    public CompletableFuture<ResponseEntity<Groups>> getGroupById(@PathVariable("id") UUID groupId) {
        return groupService.findById(groupId)
                .thenApply(group -> group.map(ResponseEntity::ok)
                        .orElse(ResponseEntity.notFound().build()));
    }

    @GetMapping("/formation-date")
    public CompletableFuture<ResponseEntity<List<Groups>>> getGroupsByFormationDateRange(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return groupService.findByFormationDateBetween(startDate, endDate)
                .thenApply(ResponseEntity::ok);
    }

    @GetMapping("/active")
    public CompletableFuture<ResponseEntity<List<Groups>>> getActiveGroups() {
        return groupService.findActiveGroups()
                .thenApply(ResponseEntity::ok);
    }

    @GetMapping("/disbanded")
    public CompletableFuture<ResponseEntity<List<Groups>>> getDisbandedGroups() {
        return groupService.findDisbandedGroups()
                .thenApply(ResponseEntity::ok);
    }

    @GetMapping("/gender/{gender}")
    public CompletableFuture<ResponseEntity<List<Groups>>> getGroupsByGender(@PathVariable("gender") ArtistGender gender) {
        return groupService.findByGroupGender(gender)
                .thenApply(ResponseEntity::ok);
    }

    @PostMapping
    public CompletableFuture<ResponseEntity<Groups>> createGroup(@RequestBody Groups group) {
        // For simplicity, we'll extract the artist from the group object
        Artist artist = group.getArtist();
        if (artist == null) {
            return CompletableFuture.completedFuture(ResponseEntity.badRequest().build());
        }
        return groupService.save(group, artist)
                .thenApply(savedGroup -> ResponseEntity.status(HttpStatus.CREATED).body(savedGroup));
    }

    @PutMapping("/{id}")
    public CompletableFuture<ResponseEntity<Groups>> updateGroup(@PathVariable("id") UUID groupId, @RequestBody Groups group) {
        if (!group.getArtistId().equals(groupId)) {
            return CompletableFuture.completedFuture(ResponseEntity.badRequest().build());
        }
        
        return groupService.update(group)
                .thenApply(updatedGroup -> {
                    if (updatedGroup == null) {
                        return ResponseEntity.notFound().<Groups>build();
                    }
                    return ResponseEntity.ok(updatedGroup);
                });
    }

    @DeleteMapping("/{id}")
    public CompletableFuture<ResponseEntity<Void>> deleteGroup(@PathVariable("id") UUID groupId) {
        return groupService.deleteById(groupId)
                .thenApply(v -> ResponseEntity.noContent().<Void>build());
    }
}
