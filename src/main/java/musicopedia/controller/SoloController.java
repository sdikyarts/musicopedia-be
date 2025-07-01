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
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/soloists")
public class SoloController {

    private final SoloService soloService;

    public SoloController(SoloService soloService) {
        this.soloService = soloService;
    }

    @GetMapping
    public ResponseEntity<List<Solo>> getAllSoloists() {
        return ResponseEntity.ok(soloService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Solo> getSoloistById(@PathVariable("id") UUID soloId) {
        Optional<Solo> solo = soloService.findById(soloId);
        return solo.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/birthdate")
    public ResponseEntity<List<Solo>> getSoloistsByBirthDateRange(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(soloService.findByBirthDateBetween(startDate, endDate));
    }

    @GetMapping("/gender/{gender}")
    public ResponseEntity<List<Solo>> getSoloistsByGender(@PathVariable("gender") ArtistGender gender) {
        return ResponseEntity.ok(soloService.findByGender(gender));
    }

    @GetMapping("/active")
    public ResponseEntity<List<Solo>> getActiveSoloists() {
        return ResponseEntity.ok(soloService.findActiveSoloArtists());
    }

    @GetMapping("/deceased")
    public ResponseEntity<List<Solo>> getDeceasedSoloists() {
        return ResponseEntity.ok(soloService.findDeceasedSoloArtists());
    }

    @PostMapping
    public ResponseEntity<Solo> createSoloist(@RequestBody Solo solo) {
        // For simplicity, we'll extract the artist from the solo object
        Artist artist = solo.getArtist();
        if (artist == null) {
            return ResponseEntity.badRequest().build();
        }
        Solo savedSolo = soloService.save(solo, artist);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedSolo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Solo> updateSoloist(@PathVariable("id") UUID soloId, @RequestBody Solo solo) {
        if (!solo.getArtistId().equals(soloId)) {
            return ResponseEntity.badRequest().build();
        }
        
        Solo updatedSolo = soloService.update(solo);
        if (updatedSolo == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedSolo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSoloist(@PathVariable("id") UUID soloId) {
        soloService.deleteById(soloId);
        return ResponseEntity.noContent().build();
    }
}
