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
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/groups")
public class GroupController {

    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping
    public ResponseEntity<List<Groups>> getAllGroups() {
        return ResponseEntity.ok(groupService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Groups> getGroupById(@PathVariable("id") UUID groupId) {
        Optional<Groups> group = groupService.findById(groupId);
        return group.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/formation-date")
    public ResponseEntity<List<Groups>> getGroupsByFormationDateRange(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(groupService.findByFormationDateBetween(startDate, endDate));
    }

    @GetMapping("/active")
    public ResponseEntity<List<Groups>> getActiveGroups() {
        return ResponseEntity.ok(groupService.findActiveGroups());
    }

    @GetMapping("/disbanded")
    public ResponseEntity<List<Groups>> getDisbandedGroups() {
        return ResponseEntity.ok(groupService.findDisbandedGroups());
    }

    @GetMapping("/gender/{gender}")
    public ResponseEntity<List<Groups>> getGroupsByGender(@PathVariable("gender") ArtistGender gender) {
        return ResponseEntity.ok(groupService.findByGroupGender(gender));
    }

    @PostMapping
    public ResponseEntity<Groups> createGroup(@RequestBody Groups group) {
        // For simplicity, we'll extract the artist from the group object
        Artist artist = group.getArtist();
        if (artist == null) {
            return ResponseEntity.badRequest().build();
        }
        Groups savedGroup = groupService.save(group, artist);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedGroup);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Groups> updateGroup(@PathVariable("id") UUID groupId, @RequestBody Groups group) {
        if (!group.getArtistId().equals(groupId)) {
            return ResponseEntity.badRequest().build();
        }
        
        Groups updatedGroup = groupService.update(group);
        if (updatedGroup == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedGroup);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGroup(@PathVariable("id") UUID groupId) {
        groupService.deleteById(groupId);
        return ResponseEntity.noContent().build();
    }
}
