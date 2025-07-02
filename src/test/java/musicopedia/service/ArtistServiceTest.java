package musicopedia.service;

import musicopedia.model.Artist;
import musicopedia.model.enums.ArtistType;
import musicopedia.repository.ArtistRepository;
import musicopedia.service.impl.ArtistServiceImpl;
import musicopedia.factory.ArtistFactoryManager;
import musicopedia.dto.request.CreateArtistRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
        testArtist = new Artist();
        testArtist.setArtistId(testId);
        testArtist.setArtistName("Test Artist");
        testArtist.setType(ArtistType.SOLO);
        testArtist.setGenre("Pop");
        testArtist.setSpotifyId("spotifyid123");
    }

    @Test
    void testFindAll() {
        Artist artist1 = new Artist();
        artist1.setArtistName("Artist 1");
        Artist artist2 = new Artist();
        artist2.setArtistName("Artist 2");
        List<Artist> artists = Arrays.asList(artist1, artist2);

        when(artistRepository.findAll()).thenReturn(artists);

        List<Artist> result = artistService.findAll();

        assertEquals(2, result.size());
        verify(artistRepository, times(1)).findAll();
    }

    @Test
    void testFindById() {
        when(artistRepository.findById(testId)).thenReturn(Optional.of(testArtist));

        Optional<Artist> result = artistService.findById(testId);

        assertTrue(result.isPresent());
        assertEquals("Test Artist", result.get().getArtistName());
        verify(artistRepository, times(1)).findById(testId);
    }

    @Test
    void testFindBySpotifyId() {
        when(artistRepository.findBySpotifyId("spotifyid123")).thenReturn(Optional.of(testArtist));

        Optional<Artist> result = artistService.findBySpotifyId("spotifyid123");

        assertTrue(result.isPresent());
        assertEquals(testId, result.get().getArtistId());
        verify(artistRepository, times(1)).findBySpotifyId("spotifyid123");
    }

    @Test
    void testFindByNameContaining() {
        List<Artist> artistList = Arrays.asList(testArtist);
        when(artistRepository.findByArtistNameContainingIgnoreCase("Test")).thenReturn(artistList);

        List<Artist> result = artistService.findByNameContaining("Test");

        assertEquals(1, result.size());
        assertEquals("Test Artist", result.get(0).getArtistName());
        verify(artistRepository, times(1)).findByArtistNameContainingIgnoreCase("Test");
    }

    @Test
    void testFindByType() {
        List<Artist> soloArtists = Arrays.asList(testArtist);
        when(artistRepository.findByType(ArtistType.SOLO)).thenReturn(soloArtists);

        List<Artist> result = artistService.findByType(ArtistType.SOLO);

        assertEquals(1, result.size());
        assertEquals(ArtistType.SOLO, result.get(0).getType());
        verify(artistRepository, times(1)).findByType(ArtistType.SOLO);
    }

    @Test
    void testSave() {
        when(artistRepository.save(any(Artist.class))).thenReturn(testArtist);

        Artist result = artistService.save(testArtist);

        assertEquals(testId, result.getArtistId());
        assertEquals("Test Artist", result.getArtistName());
        verify(artistRepository, times(1)).save(testArtist);
    }

    @Test
    void testDeleteById() {
        doNothing().when(artistRepository).deleteById(testId);

        artistService.deleteById(testId);

        verify(artistRepository, times(1)).deleteById(testId);
    }

    @Test
    void testExistsById() {
        UUID nonExistentId = UUID.randomUUID();
        when(artistRepository.existsById(testId)).thenReturn(true);
        when(artistRepository.existsById(nonExistentId)).thenReturn(false);

        boolean exists = artistService.existsById(testId);
        boolean notExists = artistService.existsById(nonExistentId);

        assertTrue(exists);
        assertFalse(notExists);
        verify(artistRepository, times(2)).existsById(any()); // Changed to expect 2 calls
    }

    @Test
    void testCreateArtist() {
        CreateArtistRequestDTO dto = new CreateArtistRequestDTO();
        dto.setArtistName("New Artist");
        dto.setType(ArtistType.SOLO);
        dto.setGenre("Pop");
        dto.setPrimaryLanguage("English");

        when(artistFactoryManager.createArtist(any(CreateArtistRequestDTO.class))).thenReturn(testArtist);
        when(artistRepository.save(any(Artist.class))).thenReturn(testArtist);

        Artist result = artistService.createArtist(dto);

        assertEquals(testId, result.getArtistId());
        assertEquals("Test Artist", result.getArtistName());
        verify(artistFactoryManager, times(1)).validateArtistData(dto);
        verify(artistFactoryManager, times(1)).createArtist(dto);
        verify(artistRepository, times(1)).save(testArtist);
    }
}
