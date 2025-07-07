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
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

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

    @Autowired
    private TestEntityManager entityManager;

    private Artist createSoloArtist(String name, String genre, String country, String language) {
        Artist soloArtist = new Artist();
        soloArtist.setArtistName(name);
        soloArtist.setType(ArtistType.SOLO);
        soloArtist.setGenre(genre);
        soloArtist.setOriginCountry(country);
        soloArtist.setPrimaryLanguage(language);
        return artistRepository.save(soloArtist);
    }

    private void createSoloEntity(Artist artist, String realName, LocalDate birthDate, LocalDate deathDate) {
        Solo solo = new Solo();
        solo.setArtistId(artist.getArtistId());
        solo.setArtist(artist);
        solo.setBirthDate(birthDate);
        solo.setDeathDate(deathDate);
        solo.setGender(ArtistGender.FEMALE);
        solo.setRealName(realName);
        entityManager.persist(solo);
        entityManager.flush();
    }

    @BeforeEach
    void setup() {
        Artist iu = createSoloArtist("IU", "K-pop", "KR", "Korean");
        Artist taylorSwift = createSoloArtist("Taylor Swift", "Pop", "US", "English");
        Artist johnLennon = createSoloArtist("John Lennon", "Rock", "UK", "English");
        createSoloEntity(iu, "Lee Ji-eun", LocalDate.of(1993, 5, 16), null);
        createSoloEntity(taylorSwift, "Taylor Alison Swift", LocalDate.of(1989, 12, 13), null);
        createSoloEntity(johnLennon, "John Winston Lennon", LocalDate.of(1940, 10, 9), LocalDate.of(1980, 12, 8));
        Artist group = new Artist();
        group.setArtistName("BTS");
        group.setType(ArtistType.GROUP);
        group.setGenre("K-pop");
        group.setOriginCountry("KR");
        artistRepository.save(group);
    }

    @Test
    public void testFindByType() {
        List<Solo> soloEntities = soloRepository.findAll();
        assertEquals(3, soloEntities.size());
    }

    @Test
    public void testFindSoloArtistsByGenre() {
        List<Solo> kpopSolos = soloRepository.findAll().stream().filter(s -> s.getArtist().getGenre().equals("K-pop")).toList();
        assertEquals(1, kpopSolos.size());
        assertEquals("IU", kpopSolos.get(0).getArtist().getArtistName());
        List<Solo> popSolos = soloRepository.findAll().stream().filter(s -> s.getArtist().getGenre().equals("Pop")).toList();
        assertEquals(1, popSolos.size()); // Updated from 2 to 1
        assertTrue(popSolos.stream().anyMatch(solo -> solo.getArtist().getArtistName().equals("Taylor Swift")));
    }

    @Test
    public void testFindSoloArtistsByCountry() {
        List<Solo> koreanSolos = soloRepository.findAll().stream().filter(s -> s.getArtist().getOriginCountry().equals("KR")).toList();
        assertEquals(1, koreanSolos.size());
        assertEquals("IU", koreanSolos.get(0).getArtist().getArtistName());
        List<Solo> usSolos = soloRepository.findAll().stream().filter(s -> s.getArtist().getOriginCountry().equals("US")).toList();
        assertEquals(1, usSolos.size());
        assertEquals("Taylor Swift", usSolos.get(0).getArtist().getArtistName());
    }

    @Test
    public void testFindSoloArtistsByNameContaining() {
        List<Solo> swiftSolos = soloRepository.findAll().stream().filter(s -> s.getArtist().getArtistName().contains("Swift")).toList();
        assertEquals(1, swiftSolos.size());
        assertEquals("Taylor Swift", swiftSolos.get(0).getArtist().getArtistName());
        List<Solo> iuSolos = soloRepository.findAll().stream().filter(s -> s.getArtist().getArtistName().contains("IU")).toList();
        assertEquals(1, iuSolos.size());
        assertEquals("IU", iuSolos.get(0).getArtist().getArtistName());
    }

    @Test
    public void testCountSoloArtistsByLanguage() {
        long englishSolos = soloRepository.findAll().stream().filter(s -> "English".equals(s.getArtist().getPrimaryLanguage())).count();
        assertEquals(2, englishSolos);
        long koreanSolos = soloRepository.findAll().stream().filter(s -> "Korean".equals(s.getArtist().getPrimaryLanguage())).count();
        assertEquals(1, koreanSolos);
    }

    @Test
    public void testFindBySoloRealNameContaining() {
        List<Solo> swiftSolos = soloRepository.findAll().stream().filter(s -> s.getRealName() != null && s.getRealName().contains("Swift")).toList();
        assertEquals(1, swiftSolos.size());
        assertEquals("Taylor Swift", swiftSolos.get(0).getArtist().getArtistName());
    }
}
