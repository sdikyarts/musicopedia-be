package musicopedia.service;

import musicopedia.model.Artist;
import musicopedia.model.Solo;
import musicopedia.model.enums.ArtistGender;
import musicopedia.model.enums.ArtistType;
import musicopedia.repository.SoloRepository;
import musicopedia.service.config.ServiceTestConfig;
import musicopedia.service.impl.SoloServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Import(ServiceTestConfig.class)
public class SoloServiceTest {

    @Mock
    private SoloRepository soloRepository;

    private SoloService soloService;

    private UUID testId;
    private Artist testArtist;
    private Solo testSolo;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        soloService = new SoloServiceImpl(soloRepository);

        testId = UUID.randomUUID();
        testArtist = new Artist();
        testArtist.setArtistId(testId);
        testArtist.setArtistName("IU");
        testArtist.setType(ArtistType.Solo);
        testArtist.setOriginCountry("KR");
        testArtist.setPrimaryLanguage("Korean");

        testSolo = new Solo();
        testSolo.setArtistId(testId);
        testSolo.setArtist(testArtist);
        testSolo.setBirthDate(LocalDate.of(1993, 5, 16));
        testSolo.setGender(ArtistGender.Female);
        
        // Extended entity is handled through reflection or other mechanisms
    }

    @Test
    void testFindAll() {
        Artist artist1 = createSoloArtist("Solo 1", LocalDate.of(1990, 1, 1), null, ArtistGender.Female);
        Artist artist2 = createSoloArtist("Solo 2", LocalDate.of(1985, 5, 10), null, ArtistGender.Male);

        List<Artist> artists = Arrays.asList(artist1, artist2);
        
        when(soloRepository.findByType(ArtistType.Solo)).thenReturn(artists);

        List<Solo> result = soloService.findAll();

        assertEquals(2, result.size());
        assertEquals("Solo 1", result.get(0).getArtist().getArtistName());
        assertEquals("Solo 2", result.get(1).getArtist().getArtistName());
        verify(soloRepository, times(1)).findByType(ArtistType.Solo);
    }

    @Test
    void testFindById() {
        // Create a spy of SoloServiceImpl to intercept method calls
        SoloServiceImpl soloServiceSpy = spy((SoloServiceImpl)soloService);
        
        // Mock the repository
        when(soloRepository.findById(testId)).thenReturn(Optional.of(testArtist));
        
        // Create a Solo object with correct birthdate
        Solo mockSolo = new Solo();
        mockSolo.setArtistId(testId);
        mockSolo.setArtist(testArtist);
        mockSolo.setBirthDate(LocalDate.of(1993, 5, 16));
        mockSolo.setGender(ArtistGender.Female);
        
        // Mock the convertToSolo method to return our mock Solo
        doReturn(mockSolo).when(soloServiceSpy).convertToSolo(testArtist);

        Optional<Solo> result = soloServiceSpy.findById(testId);

        assertTrue(result.isPresent());
        assertEquals("IU", result.get().getArtist().getArtistName());
        assertEquals(LocalDate.of(1993, 5, 16), result.get().getBirthDate());
        verify(soloRepository, times(1)).findById(testId);
    }

    @Test
    void testFindByBirthDateBetween() {
        // Create test data
        Artist artist1 = new Artist();
        artist1.setArtistId(UUID.randomUUID());
        artist1.setArtistName("Solo 1");
        artist1.setType(ArtistType.Solo);
        
        Artist artist2 = new Artist();
        artist2.setArtistId(UUID.randomUUID());
        artist2.setArtistName("Solo 2");
        artist2.setType(ArtistType.Solo);
        
        Artist artist3 = new Artist();
        artist3.setArtistId(UUID.randomUUID());
        artist3.setArtistName("Solo 3");
        artist3.setType(ArtistType.Solo);

        when(soloRepository.findByType(ArtistType.Solo))
            .thenReturn(Arrays.asList(artist1, artist2, artist3));
        
        // Create a spy of SoloServiceImpl to override convertToSolo
        SoloServiceImpl soloServiceSpy = spy((SoloServiceImpl)soloService);
        
        Solo solo1 = new Solo();
        solo1.setArtistId(artist1.getArtistId());
        solo1.setArtist(artist1);
        solo1.setBirthDate(LocalDate.of(1990, 1, 1));
        solo1.setGender(ArtistGender.Female);
        
        Solo solo2 = new Solo();
        solo2.setArtistId(artist2.getArtistId());
        solo2.setArtist(artist2);
        solo2.setBirthDate(LocalDate.of(1995, 5, 10));
        solo2.setGender(ArtistGender.Male);
        
        Solo solo3 = new Solo();
        solo3.setArtistId(artist3.getArtistId());
        solo3.setArtist(artist3);
        solo3.setBirthDate(LocalDate.of(2000, 10, 15));
        solo3.setGender(ArtistGender.Female);
        
        // Mock convertToSolo for each artist
        doReturn(solo1).when(soloServiceSpy).convertToSolo(artist1);
        doReturn(solo2).when(soloServiceSpy).convertToSolo(artist2);
        doReturn(solo3).when(soloServiceSpy).convertToSolo(artist3);

        List<Solo> result = soloServiceSpy.findByBirthDateBetween(
            LocalDate.of(1994, 1, 1), 
            LocalDate.of(1996, 12, 31)
        );

        assertEquals(1, result.size());
        assertEquals("Solo 2", result.get(0).getArtist().getArtistName());
    }

    @Test
    void testFindByGender() {
        // Create test data
        Artist artist1 = new Artist();
        artist1.setArtistId(UUID.randomUUID());
        artist1.setArtistName("Female Solo");
        artist1.setType(ArtistType.Solo);
        
        Artist artist2 = new Artist();
        artist2.setArtistId(UUID.randomUUID());
        artist2.setArtistName("Male Solo");
        artist2.setType(ArtistType.Solo);

        when(soloRepository.findByType(ArtistType.Solo))
            .thenReturn(Arrays.asList(artist1, artist2));

        // Create a spy of SoloServiceImpl to override convertToSolo
        SoloServiceImpl soloServiceSpy = spy((SoloServiceImpl)soloService);
        
        Solo solo1 = new Solo();
        solo1.setArtistId(artist1.getArtistId());
        solo1.setArtist(artist1);
        solo1.setBirthDate(LocalDate.of(1990, 1, 1));
        solo1.setGender(ArtistGender.Female);
        
        Solo solo2 = new Solo();
        solo2.setArtistId(artist2.getArtistId());
        solo2.setArtist(artist2);
        solo2.setBirthDate(LocalDate.of(1985, 5, 10));
        solo2.setGender(ArtistGender.Male);
        
        // Mock convertToSolo for each artist
        doReturn(solo1).when(soloServiceSpy).convertToSolo(artist1);
        doReturn(solo2).when(soloServiceSpy).convertToSolo(artist2);

        List<Solo> result = soloServiceSpy.findByGender(ArtistGender.Female);

        assertEquals(1, result.size());
        assertEquals("Female Solo", result.get(0).getArtist().getArtistName());
    }

    @Test
    void testFindActiveSoloArtists() {
        // Create test data
        Artist activeArtist = new Artist();
        activeArtist.setArtistId(UUID.randomUUID());
        activeArtist.setArtistName("Active");
        activeArtist.setType(ArtistType.Solo);
        
        Artist deceasedArtist = new Artist();
        deceasedArtist.setArtistId(UUID.randomUUID());
        deceasedArtist.setArtistName("Deceased");
        deceasedArtist.setType(ArtistType.Solo);

        when(soloRepository.findByType(ArtistType.Solo))
            .thenReturn(Arrays.asList(activeArtist, deceasedArtist));

        // Create a spy of SoloServiceImpl to override convertToSolo
        SoloServiceImpl soloServiceSpy = spy((SoloServiceImpl)soloService);
        
        Solo active = new Solo();
        active.setArtistId(activeArtist.getArtistId());
        active.setArtist(activeArtist);
        active.setBirthDate(LocalDate.of(1990, 1, 1));
        active.setGender(ArtistGender.Female);
        active.setDeathDate(null);
        
        Solo deceased = new Solo();
        deceased.setArtistId(deceasedArtist.getArtistId());
        deceased.setArtist(deceasedArtist);
        deceased.setBirthDate(LocalDate.of(1950, 5, 10));
        deceased.setDeathDate(LocalDate.of(2010, 12, 1));
        deceased.setGender(ArtistGender.Male);
        
        // Mock convertToSolo for each artist
        doReturn(active).when(soloServiceSpy).convertToSolo(activeArtist);
        doReturn(deceased).when(soloServiceSpy).convertToSolo(deceasedArtist);

        List<Solo> result = soloServiceSpy.findActiveSoloArtists();

        assertEquals(1, result.size());
        assertEquals("Active", result.get(0).getArtist().getArtistName());
    }

    @Test
    void testFindDeceasedSoloArtists() {
        // Create test data
        Artist activeArtist = new Artist();
        activeArtist.setArtistId(UUID.randomUUID());
        activeArtist.setArtistName("Active");
        activeArtist.setType(ArtistType.Solo);
        
        Artist deceasedArtist = new Artist();
        deceasedArtist.setArtistId(UUID.randomUUID());
        deceasedArtist.setArtistName("Deceased");
        deceasedArtist.setType(ArtistType.Solo);

        when(soloRepository.findByType(ArtistType.Solo))
            .thenReturn(Arrays.asList(activeArtist, deceasedArtist));

        // Create a spy of SoloServiceImpl to override convertToSolo
        SoloServiceImpl soloServiceSpy = spy((SoloServiceImpl)soloService);
        
        Solo active = new Solo();
        active.setArtistId(activeArtist.getArtistId());
        active.setArtist(activeArtist);
        active.setBirthDate(LocalDate.of(1990, 1, 1));
        active.setGender(ArtistGender.Female);
        active.setDeathDate(null);
        
        Solo deceased = new Solo();
        deceased.setArtistId(deceasedArtist.getArtistId());
        deceased.setArtist(deceasedArtist);
        deceased.setBirthDate(LocalDate.of(1950, 5, 10));
        deceased.setDeathDate(LocalDate.of(2010, 12, 1));
        deceased.setGender(ArtistGender.Male);
        
        // Mock convertToSolo for each artist
        doReturn(active).when(soloServiceSpy).convertToSolo(activeArtist);
        doReturn(deceased).when(soloServiceSpy).convertToSolo(deceasedArtist);

        List<Solo> result = soloServiceSpy.findDeceasedSoloArtists();

        assertEquals(1, result.size());
        assertEquals("Deceased", result.get(0).getArtist().getArtistName());
    }

    @Test
    void testSave() {
        when(soloRepository.save(any(Artist.class))).thenReturn(testArtist);

        Solo savedSolo = soloService.save(testSolo, testArtist);

        assertEquals(testId, savedSolo.getArtistId());
        assertEquals("IU", savedSolo.getArtist().getArtistName());
        assertEquals(ArtistType.Solo, testArtist.getType());
        verify(soloRepository, times(1)).save(testArtist);
    }

    @Test
    void testUpdate() {
        when(soloRepository.existsById(testId)).thenReturn(true);
        when(soloRepository.save(testArtist)).thenReturn(testArtist);

        Solo updatedSolo = soloService.update(testSolo);

        assertNotNull(updatedSolo);
        verify(soloRepository, times(1)).existsById(testId);
        verify(soloRepository, times(1)).save(testArtist);
    }

    @Test
    void testDeleteById() {
        doNothing().when(soloRepository).deleteById(testId);

        soloService.deleteById(testId);

        verify(soloRepository, times(1)).deleteById(testId);
    }

    private Artist createSoloArtist(String name, LocalDate birthDate, LocalDate deathDate, ArtistGender gender) {
        UUID artistId = UUID.randomUUID();
        Artist artist = new Artist();
        artist.setArtistId(artistId);
        artist.setArtistName(name);
        artist.setType(ArtistType.Solo);
        
        Solo solo = new Solo();
        solo.setArtistId(artistId);
        solo.setArtist(artist);
        solo.setBirthDate(birthDate);
        solo.setDeathDate(deathDate);
        solo.setGender(gender);
        
        // Mock the repository to return the data needed by SoloServiceImpl
        when(soloRepository.findById(artistId)).thenAnswer(invocation -> {
            return Optional.of(artist);
        });
        
        doAnswer(invocation -> {
            Solo customSolo = new Solo();
            customSolo.setArtistId(artistId);
            customSolo.setArtist(artist);
            customSolo.setBirthDate(birthDate);
            customSolo.setDeathDate(deathDate);
            customSolo.setGender(gender);
            return Optional.of(customSolo);
        }).when(soloRepository).findById(artistId);
        
        return artist;
    }
}
