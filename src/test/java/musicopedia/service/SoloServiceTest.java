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
        Solo solo1 = createSoloWithArtist("Taylor Swift", LocalDate.of(1989, 12, 13), ArtistGender.FEMALE);
        Solo solo2 = createSoloWithArtist("Jung Kook", LocalDate.of(1997, 9, 1), ArtistGender.MALE);
        List<Solo> solos = Arrays.asList(solo1, solo2);
        when(soloRepository.findAll()).thenReturn(solos);
        CompletableFuture<List<Solo>> future = soloService.findAll();
        List<Solo> result = future.join();
        assertEquals(2, result.size());
        assertEquals("Taylor Swift", result.get(0).getArtist().getArtistName());
        assertEquals("Jung Kook", result.get(1).getArtist().getArtistName());
        verify(soloRepository, times(1)).findAll();
    }

    @Test
    void testFindById() {
        when(soloRepository.findById(testId)).thenReturn(Optional.of(testSolo));
        CompletableFuture<Optional<Solo>> future = soloService.findById(testId);
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
    void testFindByBirthDateBetween() {
        Solo solo1 = createSoloWithArtist("Solo 1", LocalDate.of(1990, 1, 1), ArtistGender.FEMALE);
        Solo solo2 = createSoloWithArtist("Solo 2", LocalDate.of(1995, 5, 10), ArtistGender.MALE);
        Solo solo3 = createSoloWithArtist("Solo 3", LocalDate.of(2000, 10, 15), ArtistGender.FEMALE);
        Solo soloWithNullBirthDate = createSoloWithArtist("Solo Null Date", null, ArtistGender.MALE);
        List<Solo> solos = Arrays.asList(solo1, solo2, solo3, soloWithNullBirthDate);
        when(soloRepository.findAll()).thenReturn(solos);
        CompletableFuture<List<Solo>> future = soloService.findByBirthDateBetween(
            LocalDate.of(1994, 1, 1), 
            LocalDate.of(1996, 12, 31)
        );
        List<Solo> result = future.join();
        assertEquals(1, result.size());
        assertEquals("Solo 2", result.get(0).getArtist().getArtistName());
    }

    @Test
    void testFindByGender() {
        Solo solo1 = createSoloWithArtist("Female Solo", LocalDate.of(1990, 1, 1), ArtistGender.FEMALE);
        Solo solo2 = createSoloWithArtist("Male Solo", LocalDate.of(1985, 5, 10), ArtistGender.MALE);
        List<Solo> solos = Arrays.asList(solo1, solo2);
        when(soloRepository.findAll()).thenReturn(solos);
        CompletableFuture<List<Solo>> future = soloService.findByGender(ArtistGender.FEMALE);
        List<Solo> result = future.join();
        assertEquals(1, result.size());
        assertEquals("Female Solo", result.get(0).getArtist().getArtistName());
    }

    @Test
    void testFindActiveSoloArtists() {
        Solo active = createSoloWithArtist("Active", LocalDate.of(1990, 1, 1), ArtistGender.FEMALE);
        active.setDeathDate(null);
        Solo deceased = createSoloWithArtist("Deceased", LocalDate.of(1950, 5, 10), ArtistGender.MALE);
        deceased.setDeathDate(LocalDate.of(2010, 12, 1));
        List<Solo> solos = Arrays.asList(active, deceased);
        when(soloRepository.findAll()).thenReturn(solos);
        CompletableFuture<List<Solo>> future = soloService.findActiveSoloArtists();
        List<Solo> result = future.join();
        assertEquals(1, result.size());
        assertEquals("Active", result.get(0).getArtist().getArtistName());
    }

    @Test
    void testFindDeceasedSoloArtists() {
        Solo active = createSoloWithArtist("Active", LocalDate.of(1990, 1, 1), ArtistGender.FEMALE);
        active.setDeathDate(null);
        Solo deceased = createSoloWithArtist("Deceased", LocalDate.of(1950, 5, 10), ArtistGender.MALE);
        deceased.setDeathDate(LocalDate.of(2010, 12, 1));
        List<Solo> solos = Arrays.asList(active, deceased);
        when(soloRepository.findAll()).thenReturn(solos);
        CompletableFuture<List<Solo>> future = soloService.findDeceasedSoloArtists();
        List<Solo> result = future.join();
        assertEquals(1, result.size());
        assertEquals("Deceased", result.get(0).getArtist().getArtistName());
    }

    @Test
    void testSave() {
        when(soloRepository.save(any(Solo.class))).thenReturn(testSolo);
        CompletableFuture<Solo> future = soloService.save(testSolo, testArtist);
        Solo savedSolo = future.join();
        assertEquals(testId, savedSolo.getArtistId());
        assertEquals("IU", savedSolo.getArtist().getArtistName());
        assertEquals(ArtistType.SOLO, testArtist.getType());
        verify(soloRepository, times(1)).save(testSolo);
    }

    @Test
    void testUpdate() {
        when(soloRepository.existsById(testId)).thenReturn(true);
        when(soloRepository.save(testSolo)).thenReturn(testSolo);
        CompletableFuture<Solo> future = soloService.update(testSolo);
        Solo updatedSolo = future.join();
        assertNotNull(updatedSolo);
        verify(soloRepository, times(1)).existsById(testId);
        verify(soloRepository, times(1)).save(testSolo);
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
    void testFindByRealNameContaining_AllBranches() {
        // artist is null
        Solo soloWithNullArtist = new Solo();
        soloWithNullArtist.setArtist(null);
        // artistName is null
        Artist artistWithNullName = new Artist();
        artistWithNullName.setArtistName(null);
        Solo soloWithNullArtistName = new Solo();
        soloWithNullArtistName.setArtist(artistWithNullName);
        // artistName does not contain realName
        Artist artistNoMatch = new Artist();
        artistNoMatch.setArtistName("NoMatch");
        Solo soloNoMatch = new Solo();
        soloNoMatch.setArtist(artistNoMatch);
        // artistName contains realName
        Artist artistMatch = new Artist();
        artistMatch.setArtistName("Nicki Minaj");
        Solo soloMatch = new Solo();
        soloMatch.setArtist(artistMatch);
        List<Solo> allSolos = Arrays.asList(soloWithNullArtist, soloWithNullArtistName, soloNoMatch, soloMatch);
        when(soloRepository.findAll()).thenReturn(allSolos);
        CompletableFuture<List<Solo>> future = soloService.findByRealNameContaining("Minaj");
        List<Solo> result = future.join();
        assertEquals(1, result.size());
        assertEquals("Nicki Minaj", result.get(0).getArtist().getArtistName());
        verify(soloRepository, times(1)).findAll();
    }

    // Helper for test data
    private Solo createSoloWithArtist(String artistName, LocalDate birthDate, ArtistGender gender) {
        UUID artistId = UUID.randomUUID();
        Artist artist = new Artist();
        artist.setArtistId(artistId);
        artist.setArtistName(artistName);
        artist.setType(ArtistType.SOLO);
        Solo solo = new Solo();
        solo.setArtistId(artistId);
        solo.setArtist(artist);
        solo.setBirthDate(birthDate);
        solo.setGender(gender);
        return solo;
    }
}
