package musicopedia.service;

import musicopedia.model.Artist;
import musicopedia.model.enums.ArtistType;
import musicopedia.dto.request.ArtistRequestDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface ArtistService {
    
    /**
     * Asynchronously find all artists
     */
    CompletableFuture<List<Artist>> findAllAsync();
    
    /**
     * Asynchronously find artist by ID
     */
    CompletableFuture<Optional<Artist>> findByIdAsync(UUID artistId);
    
    /**
     * Asynchronously find artist by Spotify ID
     */
    CompletableFuture<Optional<Artist>> findBySpotifyIdAsync(String spotifyId);
    
    /**
     * Asynchronously search artists by name
     */
    CompletableFuture<List<Artist>> findByNameContainingAsync(String name);
    
    /**
     * Asynchronously find artists by type
     */
    CompletableFuture<List<Artist>> findByTypeAsync(ArtistType type);
    
    /**
     * Asynchronously save an artist
     */
    CompletableFuture<Artist> saveAsync(Artist artist);
    
    /**
     * Creates and saves an artist using the factory pattern with type-specific validation
     * @param dto The creation request containing artist data
     * @return The created and saved Artist entity
     */
    CompletableFuture<Artist> createArtistAsync(ArtistRequestDTO dto);
    
    /**
     * Asynchronously delete an artist by ID
     */
    CompletableFuture<Void> deleteByIdAsync(UUID artistId);
    
    /**
     * Asynchronously check if artist exists
     */
    CompletableFuture<Boolean> existsByIdAsync(UUID artistId);
    
    /**
     * Process multiple artists in batch asynchronously
     */
    CompletableFuture<List<Artist>> processBatchAsync(List<ArtistRequestDTO> dtos);
    
    /**
     * Perform heavy processing operations asynchronously
     * (e.g., data enrichment, external API calls)
     */
    CompletableFuture<Artist> enrichArtistDataAsync(UUID artistId);
}
