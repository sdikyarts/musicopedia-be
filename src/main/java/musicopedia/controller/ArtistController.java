package musicopedia.controller;

import musicopedia.model.Artist;
import musicopedia.model.enums.ArtistType;
import musicopedia.service.ArtistService;
import musicopedia.dto.request.ArtistRequestDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/artists")
public class ArtistController {

    private final ArtistService artistService;

    public ArtistController(ArtistService artistService) {
        this.artistService = artistService;
    }

    @GetMapping
    public CompletableFuture<ResponseEntity<List<Artist>>> getAllArtists() {
        return artistService.findAllAsync()
                .thenApply(ResponseEntity::ok);
    }

    @GetMapping("/{id}")
    public CompletableFuture<ResponseEntity<Artist>> getArtistById(@PathVariable("id") UUID artistId) {
        return artistService.findByIdAsync(artistId)
                .thenApply(artist -> artist.map(ResponseEntity::ok)
                        .orElse(ResponseEntity.notFound().build()));
    }

    @GetMapping("/search")
    public CompletableFuture<ResponseEntity<List<Artist>>> searchArtistsByName(@RequestParam("name") String name) {
        return artistService.findByNameContainingAsync(name)
                .thenApply(ResponseEntity::ok);
    }

    @GetMapping("/spotify/{spotifyId}")
    public CompletableFuture<ResponseEntity<Artist>> getArtistBySpotifyId(@PathVariable("spotifyId") String spotifyId) {
        return artistService.findBySpotifyIdAsync(spotifyId)
                .thenApply(artist -> artist.map(ResponseEntity::ok)
                        .orElse(ResponseEntity.notFound().build()));
    }

    @GetMapping("/type/{type}")
    public CompletableFuture<ResponseEntity<List<Artist>>> getArtistsByType(@PathVariable("type") ArtistType type) {
        return artistService.findByTypeAsync(type)
                .thenApply(ResponseEntity::ok);
    }

    @PostMapping
    public CompletableFuture<ResponseEntity<Artist>> createArtist(@RequestBody ArtistRequestDTO createRequest) {
        return artistService.createArtistAsync(createRequest)
                .thenApply(savedArtist -> ResponseEntity.status(HttpStatus.CREATED).body(savedArtist))
                .exceptionally(throwable -> ResponseEntity.badRequest().build());
    }

    @PostMapping("/legacy")
    public CompletableFuture<ResponseEntity<Artist>> createArtistLegacy(@RequestBody Artist artist) {
        // Legacy endpoint for direct artist creation (deprecated in favor of factory pattern)
        return artistService.saveAsync(artist)
                .thenApply(savedArtist -> ResponseEntity.status(HttpStatus.CREATED).body(savedArtist));
    }

    @DeleteMapping("/{id}")
    public CompletableFuture<ResponseEntity<Void>> deleteArtist(@PathVariable("id") UUID artistId) {
        return artistService.existsByIdAsync(artistId)
                .thenCompose(exists -> {
                    if (!exists) {
                        return CompletableFuture.completedFuture(ResponseEntity.notFound().<Void>build());
                    }
                    return artistService.deleteByIdAsync(artistId)
                            .thenApply(v -> ResponseEntity.noContent().<Void>build());
                });
    }

    @PostMapping("/batch")
    public CompletableFuture<ResponseEntity<List<Artist>>> createArtistsBatch(@RequestBody List<ArtistRequestDTO> requests) {
        return artistService.processBatchAsync(requests)
                .thenApply(savedArtists -> ResponseEntity.status(HttpStatus.CREATED).body(savedArtists))
                .exceptionally(throwable -> ResponseEntity.badRequest().build());
    }
}
