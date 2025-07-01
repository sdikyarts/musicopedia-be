package musicopedia.repository;

import musicopedia.model.Artist;
import musicopedia.model.Solo;
import musicopedia.model.enums.ArtistGender;
import musicopedia.model.enums.ArtistType;
import musicopedia.repository.config.RepositoryTestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@Import(RepositoryTestConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class SoloRepositoryTest {

    @Autowired
    private SoloRepository soloRepository;

    @Autowired
    private ArtistRepository artistRepository;

    private Artist createSoloArtist(String name, String genre, String country, String language) {
        Artist soloArtist = new Artist();
        soloArtist.setArtistName(name);
        soloArtist.setType(ArtistType.SOLO);
        soloArtist.setGenre(genre);
        soloArtist.setOriginCountry(country);
        soloArtist.setPrimaryLanguage(language);
        return artistRepository.save(soloArtist);
    }

    private void createSoloEntity(Artist artist, LocalDate birthDate, LocalDate deathDate) {
        Solo solo = new Solo();
        solo.setArtistId(artist.getArtistId());
        solo.setArtist(artist);
        solo.setBirthDate(birthDate);
        solo.setDeathDate(deathDate);
        solo.setGender(ArtistGender.FEMALE);
    }

    @BeforeEach
    void setup() {
        Artist iu = createSoloArtist("IU", "K-pop", "KR", "Korean");
        Artist taylorSwift = createSoloArtist("Taylor Swift", "Pop", "US", "English");
        Artist johnLennon = createSoloArtist("John Lennon", "Rock", "UK", "English");
        
        createSoloEntity(iu, LocalDate.of(1993, 5, 16), null);
        createSoloEntity(taylorSwift, LocalDate.of(1989, 12, 13), null);
        createSoloEntity(johnLennon, LocalDate.of(1940, 10, 9), LocalDate.of(1980, 12, 8));

        Artist group = new Artist();
        group.setArtistName("BTS");
        group.setType(ArtistType.GROUP);
        group.setGenre("K-pop");
        group.setOriginCountry("KR");
        artistRepository.save(group);
    }

    @Test
    public void testFindByType() {
        List<Artist> soloArtists = soloRepository.findByType(ArtistType.SOLO);
        assertEquals(3, soloArtists.size());
    }

    @Test
    public void testFindSoloArtistsByGenre() {
        List<Artist> kpopArtists = soloRepository.findSoloArtistsByGenre(ArtistType.SOLO, "K-pop");
        assertEquals(1, kpopArtists.size());
        assertEquals("IU", kpopArtists.get(0).getArtistName());
        
        List<Artist> popArtists = soloRepository.findSoloArtistsByGenre(ArtistType.SOLO, "Pop");
        assertEquals(2, popArtists.size());
        assertTrue(popArtists.stream().anyMatch(artist -> artist.getArtistName().equals("Taylor Swift")));
    }

    @Test
    public void testFindSoloArtistsByCountry() {
        List<Artist> koreanArtists = soloRepository.findSoloArtistsByCountry(ArtistType.SOLO, "KR");
        assertEquals(1, koreanArtists.size());
        assertEquals("IU", koreanArtists.get(0).getArtistName());
        
        List<Artist> usArtists = soloRepository.findSoloArtistsByCountry(ArtistType.SOLO, "US");
        assertEquals(1, usArtists.size());
        assertEquals("Taylor Swift", usArtists.get(0).getArtistName());
    }

    @Test
    public void testFindSoloArtistsByNameContaining() {
        List<Artist> swiftArtists = soloRepository.findSoloArtistsByNameContaining(ArtistType.SOLO, "Swift");
        assertEquals(1, swiftArtists.size());
        assertEquals("Taylor Swift", swiftArtists.get(0).getArtistName());
        
        List<Artist> iuArtists = soloRepository.findSoloArtistsByNameContaining(ArtistType.SOLO, "IU");
        assertEquals(1, iuArtists.size());
        assertEquals("IU", iuArtists.get(0).getArtistName());
    }

    @Test
    public void testCountSoloArtistsByLanguage() {
        long englishArtists = soloRepository.countSoloArtistsByLanguage(ArtistType.SOLO, "English");
        assertEquals(2, englishArtists);
        
        long koreanArtists = soloRepository.countSoloArtistsByLanguage(ArtistType.SOLO, "Korean");
        assertEquals(1, koreanArtists);
    }
}
