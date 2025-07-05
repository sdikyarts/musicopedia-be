package musicopedia.service.impl;

import musicopedia.model.Artist;
import musicopedia.model.enums.ArtistType;
import musicopedia.repository.ArtistRepository;
import musicopedia.service.ArtistService;
import musicopedia.factory.ArtistFactoryManager;
import musicopedia.dto.request.ArtistRequestDTO;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@Transactional
public class ArtistServiceImpl implements ArtistService {

    private final ArtistRepository artistRepository;
    private final ArtistFactoryManager artistFactoryManager;

    public ArtistServiceImpl(ArtistRepository artistRepository, ArtistFactoryManager artistFactoryManager) {
        this.artistRepository = artistRepository;
        this.artistFactoryManager = artistFactoryManager;
    }

    @Override
    @Async("artistProcessingExecutor")
    @Transactional(readOnly = true)
    public CompletableFuture<List<Artist>> findAllAsync() {
        List<Artist> artists = artistRepository.findAll();
        return CompletableFuture.completedFuture(artists);
    }

    @Override
    @Async("artistProcessingExecutor")
    @Transactional(readOnly = true)
    public CompletableFuture<Optional<Artist>> findByIdAsync(UUID artistId) {
        Optional<Artist> artist = artistRepository.findById(artistId);
        return CompletableFuture.completedFuture(artist);
    }

    @Override
    @Async("artistProcessingExecutor")
    @Transactional(readOnly = true)
    public CompletableFuture<Optional<Artist>> findBySpotifyIdAsync(String spotifyId) {
        Optional<Artist> artist = artistRepository.findBySpotifyId(spotifyId);
        return CompletableFuture.completedFuture(artist);
    }

    @Override
    @Async("artistProcessingExecutor")
    @Transactional(readOnly = true)
    public CompletableFuture<List<Artist>> findByNameContainingAsync(String name) {
        List<Artist> artists = artistRepository.findByArtistNameContainingIgnoreCase(name);
        return CompletableFuture.completedFuture(artists);
    }

    @Override
    @Async("artistProcessingExecutor")
    @Transactional(readOnly = true)
    public CompletableFuture<List<Artist>> findByTypeAsync(ArtistType type) {
        List<Artist> artists = artistRepository.findByType(type);
        return CompletableFuture.completedFuture(artists);
    }

    @Override
    @Async("artistProcessingExecutor")
    public CompletableFuture<Artist> saveAsync(Artist artist) {
        Artist savedArtist = artistRepository.save(artist);
        return CompletableFuture.completedFuture(savedArtist);
    }

    @Override
    @Async("artistProcessingExecutor")
    public CompletableFuture<Artist> createArtistAsync(ArtistRequestDTO dto) {
        // Validate BEFORE creation using factory pattern
        artistFactoryManager.validateArtistData(dto);
        
        // Create using factory pattern (without validation since we already validated)
        Artist artist = artistFactoryManager.createArtist(dto);
        
        // Save and return
        Artist savedArtist = artistRepository.save(artist);
        return CompletableFuture.completedFuture(savedArtist);
    }

    @Override
    @Async("artistProcessingExecutor")
    public CompletableFuture<Void> deleteByIdAsync(UUID artistId) {
        artistRepository.deleteById(artistId);
        return CompletableFuture.completedFuture(null);
    }

    @Override
    @Async("artistProcessingExecutor")
    @Transactional(readOnly = true)
    public CompletableFuture<Boolean> existsByIdAsync(UUID artistId) {
        boolean exists = artistRepository.existsById(artistId);
        return CompletableFuture.completedFuture(exists);
    }

    @Override
    @Async("artistProcessingExecutor")
    public CompletableFuture<List<Artist>> processBatchAsync(List<ArtistRequestDTO> dtos) {
        List<CompletableFuture<Artist>> futures = dtos.stream()
                .map(this::createArtistAsync)
                .toList();
        
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(v -> futures.stream()
                        .map(CompletableFuture::join)
                        .toList());
    }

    @Override
    @Async("artistProcessingExecutor")
    @Transactional(readOnly = true)
    public CompletableFuture<Artist> enrichArtistDataAsync(UUID artistId) {
        Optional<Artist> artistOpt = artistRepository.findById(artistId);
        if (artistOpt.isEmpty()) {
            return CompletableFuture.failedFuture(new RuntimeException("Artist not found"));
        }
        
        Artist artist = artistOpt.get();
        // Simulate data enrichment operations
        // In a real application, this might involve external API calls
        // For now, just return the artist as-is
        return CompletableFuture.completedFuture(artist);
    }
}
