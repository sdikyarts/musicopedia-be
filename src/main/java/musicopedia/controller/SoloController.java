package musicopedia.controller;

import musicopedia.model.Artist;
import musicopedia.model.Solo;
import musicopedia.model.enums.ArtistGender;
import musicopedia.service.SoloService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/soloists")
public class SoloController {

    private final SoloService soloService;

    public SoloController(SoloService soloService) {
        this.soloService = soloService;
    }

    @GetMapping
    public CompletableFuture<ResponseEntity<List<Solo>>> getAllSoloists() {
        return soloService.findAll()
                .thenApply(ResponseEntity::ok);
    }

    @GetMapping("/{id}")
    public CompletableFuture<ResponseEntity<Solo>> getSoloistById(@PathVariable("id") UUID soloId) {
        return soloService.findById(soloId)
                .thenApply(solo -> solo.map(ResponseEntity::ok)
                        .orElse(ResponseEntity.notFound().build()));
    }

    @GetMapping("/birthdate")
    public CompletableFuture<ResponseEntity<List<Solo>>> getSoloistsByBirthDateRange(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return soloService.findByBirthDateBetween(startDate, endDate)
                .thenApply(ResponseEntity::ok);
    }

    @GetMapping("/gender/{gender}")
    public CompletableFuture<ResponseEntity<List<Solo>>> getSoloistsByGender(@PathVariable("gender") ArtistGender gender) {
        return soloService.findByGender(gender)
                .thenApply(ResponseEntity::ok);
    }

    @GetMapping("/active")
    public CompletableFuture<ResponseEntity<List<Solo>>> getActiveSoloists() {
        return soloService.findActiveSoloArtists()
                .thenApply(ResponseEntity::ok);
    }

    @GetMapping("/deceased")
    public CompletableFuture<ResponseEntity<List<Solo>>> getDeceasedSoloists() {
        return soloService.findDeceasedSoloArtists()
                .thenApply(ResponseEntity::ok);
    }

    @PostMapping
    public CompletableFuture<ResponseEntity<Solo>> createSoloist(@RequestBody Solo solo) {
        // For simplicity, we'll extract the artist from the solo object
        Artist artist = solo.getArtist();
        if (artist == null) {
            return CompletableFuture.completedFuture(ResponseEntity.badRequest().build());
        }
        return soloService.save(solo, artist)
                .thenApply(savedSolo -> ResponseEntity.status(HttpStatus.CREATED).body(savedSolo));
    }

    @PutMapping("/{id}")
    public CompletableFuture<ResponseEntity<Solo>> updateSoloist(@PathVariable("id") UUID soloId, @RequestBody Solo solo) {
        if (!solo.getArtistId().equals(soloId)) {
            return CompletableFuture.completedFuture(ResponseEntity.badRequest().build());
        }
        
        return soloService.update(solo)
                .thenApply(updatedSolo -> {
                    if (updatedSolo == null) {
                        return ResponseEntity.notFound().<Solo>build();
                    }
                    return ResponseEntity.ok(updatedSolo);
                });
    }

    @DeleteMapping("/{id}")
    public CompletableFuture<ResponseEntity<Void>> deleteSoloist(@PathVariable("id") UUID soloId) {
        return soloService.deleteById(soloId)
                .thenApply(v -> ResponseEntity.noContent().<Void>build());
    }

    @GetMapping("/search/realname")
    public CompletableFuture<ResponseEntity<List<Solo>>> searchSoloistsByRealName(@RequestParam("realName") String realName) {
        return soloService.findByRealNameContaining(realName)
                .thenApply(ResponseEntity::ok);
    }

    @GetMapping("/debutdate")
    public CompletableFuture<ResponseEntity<List<Solo>>> getSoloistsByDebutDate(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate debutDate) {
        return soloService.findByDebutDate(debutDate)
                .thenApply(ResponseEntity::ok);
    }

    @GetMapping("/debutdate/range")
    public CompletableFuture<ResponseEntity<List<Solo>>> getSoloistsByDebutDateRange(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return soloService.findByDebutDateBetween(startDate, endDate)
                .thenApply(ResponseEntity::ok);
    }
}
