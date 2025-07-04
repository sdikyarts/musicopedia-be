package musicopedia.controller;

import musicopedia.model.Artist;
import musicopedia.model.enums.ArtistType;
import musicopedia.service.ArtistService;
import musicopedia.dto.request.ArtistRequestDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/artists")
public class ArtistController {

    private final ArtistService artistService;

    public ArtistController(ArtistService artistService) {
        this.artistService = artistService;
    }

    @GetMapping
    public ResponseEntity<List<Artist>> getAllArtists() {
        return ResponseEntity.ok(artistService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Artist> getArtistById(@PathVariable("id") UUID artistId) {
        Optional<Artist> artist = artistService.findById(artistId);
        return artist.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<List<Artist>> searchArtistsByName(@RequestParam("name") String name) {
        return ResponseEntity.ok(artistService.findByNameContaining(name));
    }

    @GetMapping("/spotify/{spotifyId}")
    public ResponseEntity<Artist> getArtistBySpotifyId(@PathVariable("spotifyId") String spotifyId) {
        Optional<Artist> artist = artistService.findBySpotifyId(spotifyId);
        return artist.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<Artist>> getArtistsByType(@PathVariable("type") ArtistType type) {
        return ResponseEntity.ok(artistService.findByType(type));
    }

    @PostMapping
    public ResponseEntity<Artist> createArtist(@RequestBody ArtistRequestDTO createRequest) {
        try {
            // Using factory pattern through service layer for type-specific creation and validation
            Artist savedArtist = artistService.createArtist(createRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedArtist);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/legacy")
    public ResponseEntity<Artist> createArtistLegacy(@RequestBody Artist artist) {
        // Legacy endpoint for direct artist creation (deprecated in favor of factory pattern)
        Artist savedArtist = artistService.save(artist);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedArtist);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArtist(@PathVariable("id") UUID artistId) {
        if (!artistService.existsById(artistId)) {
            return ResponseEntity.notFound().build();
        }
        artistService.deleteById(artistId);
        return ResponseEntity.noContent().build();
    }
}
