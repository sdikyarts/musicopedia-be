package musicopedia.repository;

import musicopedia.model.Artist;
import musicopedia.model.enums.ArtistType;
import musicopedia.repository.config.RepositoryTestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@Import(RepositoryTestConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class ArtistRepositoryTest {

    @Autowired
    private ArtistRepository artistRepository;

    @Test
    public void testSaveAndFindArtist() {
        Artist artist = new Artist();
        artist.setArtistName("Test Artist");
        artist.setType(ArtistType.Solo);
        artist.setGenre("Pop");
        artist.setOriginCountry("US");
        artist.setPrimaryLanguage("English");
        artist.setSpotifyId("testspotifyid123");

        Artist savedArtist = artistRepository.save(artist);
        assertNotNull(savedArtist.getArtistId());

        Optional<Artist> foundArtist = artistRepository.findById(savedArtist.getArtistId());
        assertTrue(foundArtist.isPresent());
        assertEquals("Test Artist", foundArtist.get().getArtistName());
        assertEquals(ArtistType.Solo, foundArtist.get().getType());
    }

    @Test
    public void testFindByArtistNameContainingIgnoreCase() {
        Artist artist1 = new Artist();
        artist1.setArtistName("BTS");
        artist1.setType(ArtistType.Group);
        artist1.setOriginCountry("KR");
        artistRepository.save(artist1);

        Artist artist2 = new Artist();
        artist2.setArtistName("Blackpink");
        artist2.setType(ArtistType.Group);
        artist2.setOriginCountry("KR");
        artistRepository.save(artist2);

        List<Artist> foundArtists = artistRepository.findByArtistNameContainingIgnoreCase("bts");
        assertEquals(1, foundArtists.size());
        assertEquals("BTS", foundArtists.get(0).getArtistName());
    }

    @Test
    public void testFindByType() {
        Artist soloArtist = new Artist();
        soloArtist.setArtistName("IU");
        soloArtist.setType(ArtistType.Solo);
        artistRepository.save(soloArtist);

        Artist groupArtist = new Artist();
        groupArtist.setArtistName("Twice");
        groupArtist.setType(ArtistType.Group);
        artistRepository.save(groupArtist);

        List<Artist> soloArtists = artistRepository.findByType(ArtistType.Solo);
        assertEquals(1, soloArtists.size());
        assertEquals("IU", soloArtists.get(0).getArtistName());

        List<Artist> groupArtists = artistRepository.findByType(ArtistType.Group);
        assertEquals(1, groupArtists.size());
        assertEquals("Twice", groupArtists.get(0).getArtistName());
    }

    @Test
    public void testFindByGenreContainingIgnoreCase() {
        Artist artist1 = new Artist();
        artist1.setArtistName("Artist1");
        artist1.setType(ArtistType.Solo);
        artist1.setGenre("K-pop");
        artistRepository.save(artist1);

        Artist artist2 = new Artist();
        artist2.setArtistName("Artist2");
        artist2.setType(ArtistType.Solo);
        artist2.setGenre("Rock");
        artistRepository.save(artist2);

        List<Artist> kpopArtists = artistRepository.findByGenreContainingIgnoreCase("k-pop");
        assertEquals(1, kpopArtists.size());
        assertEquals("Artist1", kpopArtists.get(0).getArtistName());
    }

    @Test
    public void testFindByOriginCountry() {
        Artist artist1 = new Artist();
        artist1.setArtistName("Artist1");
        artist1.setType(ArtistType.Solo);
        artist1.setOriginCountry("JP");
        artistRepository.save(artist1);

        Artist artist2 = new Artist();
        artist2.setArtistName("Artist2");
        artist2.setType(ArtistType.Solo);
        artist2.setOriginCountry("US");
        artistRepository.save(artist2);

        List<Artist> jpArtists = artistRepository.findByOriginCountry("JP");
        assertEquals(1, jpArtists.size());
        assertEquals("Artist1", jpArtists.get(0).getArtistName());
    }

    @Test
    public void testFindByPrimaryLanguageIgnoreCase() {
        Artist artist1 = new Artist();
        artist1.setArtistName("Artist1");
        artist1.setType(ArtistType.Solo);
        artist1.setPrimaryLanguage("Korean");
        artistRepository.save(artist1);

        Artist artist2 = new Artist();
        artist2.setArtistName("Artist2");
        artist2.setType(ArtistType.Solo);
        artist2.setPrimaryLanguage("Japanese");
        artistRepository.save(artist2);

        List<Artist> koreanArtists = artistRepository.findByPrimaryLanguageIgnoreCase("korean");
        assertEquals(1, koreanArtists.size());
        assertEquals("Artist1", koreanArtists.get(0).getArtistName());
    }

    @Test
    public void testCountByType() {
        Artist artist1 = new Artist();
        artist1.setArtistName("Solo1");
        artist1.setType(ArtistType.Solo);
        artistRepository.save(artist1);

        Artist artist2 = new Artist();
        artist2.setArtistName("Solo2");
        artist2.setType(ArtistType.Solo);
        artistRepository.save(artist2);

        Artist artist3 = new Artist();
        artist3.setArtistName("Group1");
        artist3.setType(ArtistType.Group);
        artistRepository.save(artist3);

        long soloCount = artistRepository.countByType(ArtistType.Solo);
        assertEquals(2, soloCount);

        long groupCount = artistRepository.countByType(ArtistType.Group);
        assertEquals(1, groupCount);
    }

    @Test
    public void testFindAndExistsBySpotifyId() {
        String spotifyId = "spotify12345";
        Artist artist = new Artist();
        artist.setArtistName("Spotify Artist");
        artist.setType(ArtistType.Solo);
        artist.setSpotifyId(spotifyId);
        artistRepository.save(artist);

        assertTrue(artistRepository.existsBySpotifyId(spotifyId));
        Optional<Artist> foundArtist = artistRepository.findBySpotifyId(spotifyId);
        assertTrue(foundArtist.isPresent());
        assertEquals("Spotify Artist", foundArtist.get().getArtistName());
    }
}
