package musicopedia.service;

import musicopedia.model.Artist;
import musicopedia.model.enums.ArtistType;
import musicopedia.repository.ArtistRepository;
import musicopedia.service.impl.ArtistServiceImpl;
import musicopedia.factory.ArtistFactoryManager;
import musicopedia.dto.request.ArtistRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import musicopedia.builder.ArtistBuilder;

public class ArtistServiceTest {

    @Mock
    private ArtistRepository artistRepository;
    
    @Mock
    private ArtistFactoryManager artistFactoryManager;

    private ArtistService artistService;

    private UUID testId;
    private Artist testArtist;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        artistService = new ArtistServiceImpl(artistRepository, artistFactoryManager);

        testId = UUID.randomUUID();
        testArtist = new ArtistBuilder()
            .setArtistName("Test Artist")
            .setType(ArtistType.SOLO)
            .setGenre("Pop")
            .setSpotifyId("spotifyid123")
            .build();
        testArtist.setArtistId(testId);
    }

    @Test
    void testFindAll() {
        Artist artist1 = new ArtistBuilder()
            .setArtistName("Artist 1")
            .build();
        Artist artist2 = new ArtistBuilder()
            .setArtistName("Artist 2")
            .build();
        List<Artist> artists = Arrays.asList(artist1, artist2);

        when(artistRepository.findAll()).thenReturn(artists);

        CompletableFuture<List<Artist>> resultFuture = artistService.findAllAsync();
        List<Artist> result = resultFuture.join();

        assertEquals(2, result.size());
        verify(artistRepository, times(1)).findAll();
    }

    @Test
    void testFindById() {
        when(artistRepository.findById(testId)).thenReturn(Optional.of(testArtist));

        CompletableFuture<Optional<Artist>> resultFuture = artistService.findByIdAsync(testId);
        Optional<Artist> result = resultFuture.join();

        assertTrue(result.isPresent());
        assertEquals("Test Artist", result.get().getArtistName());
        verify(artistRepository, times(1)).findById(testId);
    }

    @Test
    void testFindBySpotifyId() {
        when(artistRepository.findBySpotifyId("spotifyid123")).thenReturn(Optional.of(testArtist));

        CompletableFuture<Optional<Artist>> resultFuture = artistService.findBySpotifyIdAsync("spotifyid123");
        Optional<Artist> result = resultFuture.join();

        assertTrue(result.isPresent());
        assertEquals(testId, result.get().getArtistId());
        verify(artistRepository, times(1)).findBySpotifyId("spotifyid123");
    }

    @Test
    void testFindByNameContaining() {
        List<Artist> artistList = Arrays.asList(testArtist);
        when(artistRepository.findByArtistNameContainingIgnoreCase("Test")).thenReturn(artistList);

        CompletableFuture<List<Artist>> resultFuture = artistService.findByNameContainingAsync("Test");
        List<Artist> result = resultFuture.join();

        assertEquals(1, result.size());
        assertEquals("Test Artist", result.get(0).getArtistName());
        verify(artistRepository, times(1)).findByArtistNameContainingIgnoreCase("Test");
    }

    @Test
    void testFindByType() {
        List<Artist> soloArtists = Arrays.asList(testArtist);
        when(artistRepository.findByType(ArtistType.SOLO)).thenReturn(soloArtists);

        CompletableFuture<List<Artist>> resultFuture = artistService.findByTypeAsync(ArtistType.SOLO);
        List<Artist> result = resultFuture.join();

        assertEquals(1, result.size());
        assertEquals(ArtistType.SOLO, result.get(0).getType());
        verify(artistRepository, times(1)).findByType(ArtistType.SOLO);
    }

    @Test
    void testSave() {
        when(artistRepository.save(any(Artist.class))).thenReturn(testArtist);

        CompletableFuture<Artist> resultFuture = artistService.saveAsync(testArtist);
        Artist result = resultFuture.join();

        assertEquals(testId, result.getArtistId());
        assertEquals("Test Artist", result.getArtistName());
        verify(artistRepository, times(1)).save(testArtist);
    }

    @Test
    void testDeleteById() {
        doNothing().when(artistRepository).deleteById(testId);

        CompletableFuture<Void> resultFuture = artistService.deleteByIdAsync(testId);
        resultFuture.join();

        verify(artistRepository, times(1)).deleteById(testId);
    }

    @Test
    void testExistsById() {
        UUID nonExistentId = UUID.randomUUID();
        when(artistRepository.existsById(testId)).thenReturn(true);
        when(artistRepository.existsById(nonExistentId)).thenReturn(false);

        CompletableFuture<Boolean> existsFuture = artistService.existsByIdAsync(testId);
        CompletableFuture<Boolean> notExistsFuture = artistService.existsByIdAsync(nonExistentId);
        boolean exists = existsFuture.join();
        boolean notExists = notExistsFuture.join();

        assertTrue(exists);
        assertFalse(notExists);
        verify(artistRepository, times(2)).existsById(any()); // Changed to expect 2 calls
    }

    @Test
    void testCreateArtist() {
        ArtistRequestDTO dto = new ArtistRequestDTO();
        dto.setArtistName("New Artist");
        dto.setType(ArtistType.SOLO);
        dto.setGenre("Pop");
        dto.setPrimaryLanguage("English");

        when(artistFactoryManager.createArtist(any(ArtistRequestDTO.class))).thenReturn(testArtist);
        when(artistRepository.save(any(Artist.class))).thenReturn(testArtist);

        CompletableFuture<Artist> resultFuture = artistService.createArtistAsync(dto);
        Artist result = resultFuture.join();

        assertEquals(testId, result.getArtistId());
        assertEquals("Test Artist", result.getArtistName());
        verify(artistFactoryManager, times(1)).validateArtistData(dto);
        verify(artistFactoryManager, times(1)).createArtist(dto);
        verify(artistRepository, times(1)).save(testArtist);
    }



    @Test
    void testProcessBatchAsync() {
        // Create two DTOs
        ArtistRequestDTO dto1 = new ArtistRequestDTO();
        dto1.setArtistName("Artist 1");
        dto1.setType(ArtistType.SOLO);
        
        ArtistRequestDTO dto2 = new ArtistRequestDTO();
        dto2.setArtistName("Artist 2");
        dto2.setType(ArtistType.GROUP);
        
        List<ArtistRequestDTO> dtos = Arrays.asList(dto1, dto2);
        
        // Create expected artists
        Artist artist1 = new ArtistBuilder()
            .setArtistName("Artist 1")
            .setType(ArtistType.SOLO)
            .build();
        artist1.setArtistId(UUID.randomUUID());
        Artist artist2 = new ArtistBuilder()
            .setArtistName("Artist 2")
            .setType(ArtistType.GROUP)
            .build();
        artist2.setArtistId(UUID.randomUUID());
        
        // Mock behavior
        when(artistFactoryManager.createArtist(dto1)).thenReturn(artist1);
        when(artistFactoryManager.createArtist(dto2)).thenReturn(artist2);
        when(artistRepository.save(artist1)).thenReturn(artist1);
        when(artistRepository.save(artist2)).thenReturn(artist2);
        
        // Call the method
        CompletableFuture<List<Artist>> future = artistService.processBatchAsync(dtos);
        List<Artist> result = future.join();
        
        // Verify
        assertEquals(2, result.size());
        assertEquals("Artist 1", result.get(0).getArtistName());
        assertEquals("Artist 2", result.get(1).getArtistName());
        assertEquals(ArtistType.SOLO, result.get(0).getType());
        assertEquals(ArtistType.GROUP, result.get(1).getType());
        
        // Verify interactions
        verify(artistFactoryManager, times(1)).validateArtistData(dto1);
        verify(artistFactoryManager, times(1)).validateArtistData(dto2);
        verify(artistFactoryManager, times(1)).createArtist(dto1);
        verify(artistFactoryManager, times(1)).createArtist(dto2);
        verify(artistRepository, times(1)).save(artist1);
        verify(artistRepository, times(1)).save(artist2);
    }
    
    @Test
    void testEnrichArtistDataAsync_Success() {
        // Mock behavior
        when(artistRepository.findById(testId)).thenReturn(Optional.of(testArtist));
        
        // Call the method
        CompletableFuture<Artist> future = artistService.enrichArtistDataAsync(testId);
        Artist result = future.join();
        
        // Verify
        assertNotNull(result);
        assertEquals(testId, result.getArtistId());
        assertEquals("Test Artist", result.getArtistName());
        verify(artistRepository, times(1)).findById(testId);
    }
    
    @Test
    void testEnrichArtistDataAsync_NotFound() {
        UUID unknownId = UUID.randomUUID();
        
        // Mock behavior
        when(artistRepository.findById(unknownId)).thenReturn(Optional.empty());
        
        // Call the method
        CompletableFuture<Artist> future = artistService.enrichArtistDataAsync(unknownId);
        
        // Verify exception is thrown
        Exception exception = assertThrows(RuntimeException.class, () -> {
            future.join();
        });
        
        assertTrue(exception.getCause() instanceof RuntimeException);
        assertEquals("Artist not found", exception.getCause().getMessage());
        verify(artistRepository, times(1)).findById(unknownId);
    }
}
