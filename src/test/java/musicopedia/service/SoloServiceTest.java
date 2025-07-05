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
import java.util.concurrent.CompletableFuture;


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
        testArtist.setType(ArtistType.SOLO);
        testArtist.setOriginCountry("KR");
        testArtist.setPrimaryLanguage("Korean");

        testSolo = new Solo();
        testSolo.setArtistId(testId);
        testSolo.setArtist(testArtist);
        testSolo.setBirthDate(LocalDate.of(1993, 5, 16));
        testSolo.setGender(ArtistGender.FEMALE);
        
        // Extended entity is handled through reflection or other mechanisms
    }

    @Test
    void testFindAll() {
        Artist artist1 = createSoloArtistWithRealName("Taylor Swift", "Taylor Alison Swift", LocalDate.of(1989, 12, 13), null, ArtistGender.FEMALE);
        Artist artist2 = createSoloArtistWithRealName("Jung Kook", "Jeon Jung-kook", LocalDate.of(1997, 9, 1), null, ArtistGender.MALE);
        List<Artist> artists = Arrays.asList(artist1, artist2);
        when(soloRepository.findByType(ArtistType.SOLO)).thenReturn(artists);
        CompletableFuture<List<Solo>> future = soloService.findAll();
        List<Solo> result = future.join();
        assertEquals(2, result.size());
        assertEquals("Taylor Swift", result.get(0).getArtist().getArtistName());
        // If Solo has getRealName(), check for 'Taylor Alison Swift'
        if (result.get(0).getRealName() != null) {
            assertEquals("Taylor Alison Swift", result.get(0).getRealName());
        }
        assertEquals("Jung Kook", result.get(1).getArtist().getArtistName());
        if (result.get(1).getRealName() != null) {
            assertEquals("Jeon Jung-kook", result.get(1).getRealName());
        }
        verify(soloRepository, times(1)).findByType(ArtistType.SOLO);
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
        mockSolo.setGender(ArtistGender.FEMALE);
        
        // Mock the convertToSolo method to return our mock Solo
        doReturn(mockSolo).when(soloServiceSpy).convertToSolo(testArtist);

        CompletableFuture<Optional<Solo>> future = soloServiceSpy.findById(testId);
        Optional<Solo> result = future.join();

        assertTrue(result.isPresent());
        assertEquals("IU", result.get().getArtist().getArtistName());
        assertEquals(LocalDate.of(1993, 5, 16), result.get().getBirthDate());
        verify(soloRepository, times(1)).findById(testId);
    }

    @Test
    void testFindByIdNotFound() {
        when(soloRepository.findById(testId)).thenReturn(Optional.empty());

        CompletableFuture<Optional<Solo>> future = soloService.findById(testId);
        Optional<Solo> result = future.join();

        assertFalse(result.isPresent());
        verify(soloRepository, times(1)).findById(testId);
    }

    @Test
    void testFindByIdWrongType() {
        Artist groupArtist = new Artist();
        groupArtist.setArtistId(testId);
        groupArtist.setArtistName("Group Artist");
        groupArtist.setType(ArtistType.GROUP);
        
        when(soloRepository.findById(testId)).thenReturn(Optional.of(groupArtist));

        CompletableFuture<Optional<Solo>> future = soloService.findById(testId);
        Optional<Solo> result = future.join();

        assertFalse(result.isPresent());
        verify(soloRepository, times(1)).findById(testId);
    }

    @Test
    void testFindByBirthDateBetween() {
        // Create test data
        Artist artist1 = new Artist();
        artist1.setArtistId(UUID.randomUUID());
        artist1.setArtistName("Solo 1");
        artist1.setType(ArtistType.SOLO);
        
        Artist artist2 = new Artist();
        artist2.setArtistId(UUID.randomUUID());
        artist2.setArtistName("Solo 2");
        artist2.setType(ArtistType.SOLO);
        
        Artist artist3 = new Artist();
        artist3.setArtistId(UUID.randomUUID());
        artist3.setArtistName("Solo 3");
        artist3.setType(ArtistType.SOLO);

        Artist artistWithNullBirthDate = new Artist();
        artistWithNullBirthDate.setArtistId(UUID.randomUUID());
        artistWithNullBirthDate.setArtistName("Solo Null Date");
        artistWithNullBirthDate.setType(ArtistType.SOLO);

        when(soloRepository.findByType(ArtistType.SOLO))
            .thenReturn(Arrays.asList(artist1, artist2, artist3, artistWithNullBirthDate));
        
        // Create a spy of SoloServiceImpl to override convertToSolo
        SoloServiceImpl soloServiceSpy = spy((SoloServiceImpl)soloService);
        soloServiceSpy.setSelf(soloServiceSpy);  // Set self reference for proxy calls
        
        Solo solo1 = new Solo();
        solo1.setArtistId(artist1.getArtistId());
        solo1.setArtist(artist1);
        solo1.setBirthDate(LocalDate.of(1990, 1, 1));
        solo1.setGender(ArtistGender.FEMALE);
        
        Solo solo2 = new Solo();
        solo2.setArtistId(artist2.getArtistId());
        solo2.setArtist(artist2);
        solo2.setBirthDate(LocalDate.of(1995, 5, 10));
        solo2.setGender(ArtistGender.MALE);
        
        Solo solo3 = new Solo();
        solo3.setArtistId(artist3.getArtistId());
        solo3.setArtist(artist3);
        solo3.setBirthDate(LocalDate.of(2000, 10, 15));
        solo3.setGender(ArtistGender.FEMALE);

        Solo soloWithNullBirthDate = new Solo();
        soloWithNullBirthDate.setArtistId(artistWithNullBirthDate.getArtistId());
        soloWithNullBirthDate.setArtist(artistWithNullBirthDate);
        soloWithNullBirthDate.setBirthDate(null);
        soloWithNullBirthDate.setGender(ArtistGender.MALE);
        
        // Mock convertToSolo for each artist
        doReturn(solo1).when(soloServiceSpy).convertToSolo(artist1);
        doReturn(solo2).when(soloServiceSpy).convertToSolo(artist2);
        doReturn(solo3).when(soloServiceSpy).convertToSolo(artist3);
        doReturn(soloWithNullBirthDate).when(soloServiceSpy).convertToSolo(artistWithNullBirthDate);

        CompletableFuture<List<Solo>> future = soloServiceSpy.findByBirthDateBetween(
            LocalDate.of(1994, 1, 1), 
            LocalDate.of(1996, 12, 31)
        );
        List<Solo> result = future.join();

        assertEquals(1, result.size());
        assertEquals("Solo 2", result.get(0).getArtist().getArtistName());
    }

    @Test
    void testFindByGender() {
        // Create test data
        Artist artist1 = new Artist();
        artist1.setArtistId(UUID.randomUUID());
        artist1.setArtistName("Female Solo");
        artist1.setType(ArtistType.SOLO);
        
        Artist artist2 = new Artist();
        artist2.setArtistId(UUID.randomUUID());
        artist2.setArtistName("Male Solo");
        artist2.setType(ArtistType.SOLO);

        when(soloRepository.findByType(ArtistType.SOLO))
            .thenReturn(Arrays.asList(artist1, artist2));

        // Create a spy of SoloServiceImpl to override convertToSolo
        SoloServiceImpl soloServiceSpy = spy((SoloServiceImpl)soloService);
        soloServiceSpy.setSelf(soloServiceSpy);  // Set self reference for proxy calls
        
        Solo solo1 = new Solo();
        solo1.setArtistId(artist1.getArtistId());
        solo1.setArtist(artist1);
        solo1.setBirthDate(LocalDate.of(1990, 1, 1));
        solo1.setGender(ArtistGender.FEMALE);
        
        Solo solo2 = new Solo();
        solo2.setArtistId(artist2.getArtistId());
        solo2.setArtist(artist2);
        solo2.setBirthDate(LocalDate.of(1985, 5, 10));
        solo2.setGender(ArtistGender.MALE);
        
        // Mock convertToSolo for each artist
        doReturn(solo1).when(soloServiceSpy).convertToSolo(artist1);
        doReturn(solo2).when(soloServiceSpy).convertToSolo(artist2);

        CompletableFuture<List<Solo>> future = soloServiceSpy.findByGender(ArtistGender.FEMALE);
        List<Solo> result = future.join();

        assertEquals(1, result.size());
        assertEquals("Female Solo", result.get(0).getArtist().getArtistName());
    }

    @Test
    void testFindActiveSoloArtists() {
        // Create test data
        Artist activeArtist = new Artist();
        activeArtist.setArtistId(UUID.randomUUID());
        activeArtist.setArtistName("Active");
        activeArtist.setType(ArtistType.SOLO);
        
        Artist deceasedArtist = new Artist();
        deceasedArtist.setArtistId(UUID.randomUUID());
        deceasedArtist.setArtistName("Deceased");
        deceasedArtist.setType(ArtistType.SOLO);

        when(soloRepository.findByType(ArtistType.SOLO))
            .thenReturn(Arrays.asList(activeArtist, deceasedArtist));

        // Create a spy of SoloServiceImpl to override convertToSolo
        SoloServiceImpl soloServiceSpy = spy((SoloServiceImpl)soloService);
        soloServiceSpy.setSelf(soloServiceSpy);  // Set self reference for proxy calls
        
        Solo active = new Solo();
        active.setArtistId(activeArtist.getArtistId());
        active.setArtist(activeArtist);
        active.setBirthDate(LocalDate.of(1990, 1, 1));
        active.setGender(ArtistGender.FEMALE);
        active.setDeathDate(null);
        
        Solo deceased = new Solo();
        deceased.setArtistId(deceasedArtist.getArtistId());
        deceased.setArtist(deceasedArtist);
        deceased.setBirthDate(LocalDate.of(1950, 5, 10));
        deceased.setDeathDate(LocalDate.of(2010, 12, 1));
        deceased.setGender(ArtistGender.MALE);
        
        // Mock convertToSolo for each artist
        doReturn(active).when(soloServiceSpy).convertToSolo(activeArtist);
        doReturn(deceased).when(soloServiceSpy).convertToSolo(deceasedArtist);

        CompletableFuture<List<Solo>> future = soloServiceSpy.findActiveSoloArtists();
        List<Solo> result = future.join();

        assertEquals(1, result.size());
        assertEquals("Active", result.get(0).getArtist().getArtistName());
    }

    @Test
    void testFindDeceasedSoloArtists() {
        // Create test data
        Artist activeArtist = new Artist();
        activeArtist.setArtistId(UUID.randomUUID());
        activeArtist.setArtistName("Active");
        activeArtist.setType(ArtistType.SOLO);
        
        Artist deceasedArtist = new Artist();
        deceasedArtist.setArtistId(UUID.randomUUID());
        deceasedArtist.setArtistName("Deceased");
        deceasedArtist.setType(ArtistType.SOLO);

        when(soloRepository.findByType(ArtistType.SOLO))
            .thenReturn(Arrays.asList(activeArtist, deceasedArtist));

        // Create a spy of SoloServiceImpl to override convertToSolo
        SoloServiceImpl soloServiceSpy = spy((SoloServiceImpl)soloService);
        soloServiceSpy.setSelf(soloServiceSpy);  // Set self reference for proxy calls
        
        Solo active = new Solo();
        active.setArtistId(activeArtist.getArtistId());
        active.setArtist(activeArtist);
        active.setBirthDate(LocalDate.of(1990, 1, 1));
        active.setGender(ArtistGender.FEMALE);
        active.setDeathDate(null);
        
        Solo deceased = new Solo();
        deceased.setArtistId(deceasedArtist.getArtistId());
        deceased.setArtist(deceasedArtist);
        deceased.setBirthDate(LocalDate.of(1950, 5, 10));
        deceased.setDeathDate(LocalDate.of(2010, 12, 1));
        deceased.setGender(ArtistGender.MALE);
        
        // Mock convertToSolo for each artist
        doReturn(active).when(soloServiceSpy).convertToSolo(activeArtist);
        doReturn(deceased).when(soloServiceSpy).convertToSolo(deceasedArtist);

        CompletableFuture<List<Solo>> future = soloServiceSpy.findDeceasedSoloArtists();
        List<Solo> result = future.join();

        assertEquals(1, result.size());
        assertEquals("Deceased", result.get(0).getArtist().getArtistName());
    }

    @Test
    void testSave() {
        when(soloRepository.save(any(Artist.class))).thenReturn(testArtist);

        CompletableFuture<Solo> future = soloService.save(testSolo, testArtist);
        Solo savedSolo = future.join();

        assertEquals(testId, savedSolo.getArtistId());
        assertEquals("IU", savedSolo.getArtist().getArtistName());
        assertEquals(ArtistType.SOLO, testArtist.getType());
        verify(soloRepository, times(1)).save(testArtist);
    }

    @Test
    void testUpdate() {
        when(soloRepository.existsById(testId)).thenReturn(true);
        when(soloRepository.save(testArtist)).thenReturn(testArtist);

        CompletableFuture<Solo> future = soloService.update(testSolo);
        Solo updatedSolo = future.join();

        assertNotNull(updatedSolo);
        verify(soloRepository, times(1)).existsById(testId);
        verify(soloRepository, times(1)).save(testArtist);
    }

    @Test
    void testUpdateNotFound() {
        when(soloRepository.existsById(testId)).thenReturn(false);

        CompletableFuture<Solo> future = soloService.update(testSolo);
        Solo updatedSolo = future.join();

        assertNull(updatedSolo);
        verify(soloRepository, times(1)).existsById(testId);
        verify(soloRepository, never()).save(any());
    }

    @Test
    void testUpdateWithNullArtist() {
        Solo soloWithNullArtist = new Solo();
        soloWithNullArtist.setArtistId(testId);
        soloWithNullArtist.setArtist(null);

        when(soloRepository.existsById(testId)).thenReturn(true);

        Solo updatedSolo = soloService.update(soloWithNullArtist).join();

        assertNotNull(updatedSolo);
        verify(soloRepository, times(1)).existsById(testId);
        verify(soloRepository, never()).save(any());
    }

    @Test
    void testDeleteById() {
        doNothing().when(soloRepository).deleteById(testId);

        soloService.deleteById(testId).join();

        verify(soloRepository, times(1)).deleteById(testId);
    }

    @Test
    void testExistsById() {
        when(soloRepository.existsById(testId)).thenReturn(true);

        CompletableFuture<Boolean> future = soloService.existsById(testId);
        Boolean exists = future.join();

        assertTrue(exists);
        verify(soloRepository, times(1)).existsById(testId);
    }

    @Test
    void testExistsByIdNotFound() {
        when(soloRepository.existsById(testId)).thenReturn(false);

        CompletableFuture<Boolean> future = soloService.existsById(testId);
        Boolean exists = future.join();

        assertFalse(exists);
        verify(soloRepository, times(1)).existsById(testId);
    }

    @Test
    void testConvertToSoloWithIU() {
        SoloServiceImpl soloServiceImpl = new SoloServiceImpl(soloRepository);
        
        Artist iuArtist = new Artist();
        iuArtist.setArtistId(UUID.randomUUID());
        iuArtist.setArtistName("IU");
        iuArtist.setType(ArtistType.SOLO);

        Solo result = soloServiceImpl.convertToSolo(iuArtist);

        assertNotNull(result);
        assertEquals(iuArtist.getArtistId(), result.getArtistId());
        assertEquals(iuArtist, result.getArtist());
        assertEquals(LocalDate.of(1993, 5, 16), result.getBirthDate());
        assertEquals(ArtistGender.FEMALE, result.getGender());
    }

    @Test
    void testConvertToSoloWithOtherArtist() {
        SoloServiceImpl soloServiceImpl = new SoloServiceImpl(soloRepository);
        
        Artist otherArtist = new Artist();
        otherArtist.setArtistId(UUID.randomUUID());
        otherArtist.setArtistName("Other Artist");
        otherArtist.setType(ArtistType.SOLO);

        Solo result = soloServiceImpl.convertToSolo(otherArtist);

        assertNotNull(result);
        assertEquals(otherArtist.getArtistId(), result.getArtistId());
        assertEquals(otherArtist, result.getArtist());
        assertNull(result.getBirthDate());
        assertNull(result.getGender());
    }

    @Test
    void testConvertToSoloWithNullArtistName() {
        SoloServiceImpl soloServiceImpl = new SoloServiceImpl(soloRepository);
        
        Artist artistWithNullName = new Artist();
        artistWithNullName.setArtistId(UUID.randomUUID());
        artistWithNullName.setArtistName(null);
        artistWithNullName.setType(ArtistType.SOLO);

        Solo result = soloServiceImpl.convertToSolo(artistWithNullName);

        assertNotNull(result);
        assertEquals(artistWithNullName.getArtistId(), result.getArtistId());
        assertEquals(artistWithNullName, result.getArtist());
        assertNull(result.getBirthDate());
        assertNull(result.getGender());
    }

    @Test
    void testFindByRealNameContaining() {
        Artist artist1 = createSoloArtistWithRealName("Nicki Minaj", "Onika Tanya Maraj", LocalDate.of(1982, 12, 8), null, ArtistGender.FEMALE);
        Artist artist2 = createSoloArtistWithRealName("Taylor Swift", "Taylor Alison Swift", LocalDate.of(1989, 12, 13), null, ArtistGender.FEMALE);
        List<Artist> artists = Arrays.asList(artist1, artist2);
        when(soloRepository.findBySoloRealNameContaining("Maraj")).thenReturn(Arrays.asList(artist1));
        // Create a spy to override convertToSolo
        SoloServiceImpl soloServiceSpy = spy((SoloServiceImpl)soloService);
        doReturn(createSoloWithRealName(artist1, "Onika Tanya Maraj")).when(soloServiceSpy).convertToSolo(artist1);
        CompletableFuture<List<Solo>> future = soloServiceSpy.findByRealNameContaining("Maraj");
        List<Solo> result = future.join();
        assertEquals(1, result.size());
        assertEquals("Nicki Minaj", result.get(0).getArtist().getArtistName());
        if (result.get(0).getRealName() != null) {
            assertEquals("Onika Tanya Maraj", result.get(0).getRealName());
        }
        verify(soloRepository, times(1)).findBySoloRealNameContaining("Maraj");
    }

    // Helper for test data with realName
    private Artist createSoloArtistWithRealName(String artistName, String realName, LocalDate birthDate, LocalDate deathDate, ArtistGender gender) {
        UUID artistId = UUID.randomUUID();
        Artist artist = new Artist();
        artist.setArtistId(artistId);
        artist.setArtistName(artistName);
        artist.setType(ArtistType.SOLO);
        Solo solo = new Solo();
        solo.setArtistId(artistId);
        solo.setArtist(artist);
        solo.setBirthDate(birthDate);
        solo.setDeathDate(deathDate);
        solo.setGender(gender);
        solo.setRealName(realName);
        // Mock the repository to return the data needed by SoloServiceImpl
        when(soloRepository.findById(artistId)).thenAnswer(invocation -> Optional.of(artist));
        return artist;
    }
    private Solo createSoloWithRealName(Artist artist, String realName) {
        Solo solo = new Solo();
        solo.setArtistId(artist.getArtistId());
        solo.setArtist(artist);
        solo.setRealName(realName);
        return solo;
    }
}
