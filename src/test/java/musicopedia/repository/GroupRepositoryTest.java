package musicopedia.repository;

import musicopedia.model.Artist;
import musicopedia.model.Groups;
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
public class GroupRepositoryTest {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private ArtistRepository artistRepository;

    private Artist createGroup(String name, String genre, String country) {
        Artist group = new Artist();
        group.setArtistName(name);
        group.setType(ArtistType.Group);
        group.setGenre(genre);
        group.setOriginCountry(country);
        group.setPrimaryLanguage("Korean");
        return artistRepository.save(group);
    }

    private void createGroupEntity(Artist artist, String formationDate) {
        Groups groups = new Groups();
        groups.setArtistId(artist.getArtistId());
        groups.setArtist(artist);
        groups.setFormationDate(LocalDate.parse(formationDate));
        groups.setGroupGender(ArtistGender.MIXED);
    }

    @BeforeEach
    void setup() {
        Artist group1 = createGroup("BTS", "K-pop", "KR");
        Artist group2 = createGroup("Blackpink", "K-pop, Dance", "KR");
        Artist group3 = createGroup("OneRepublic", "Pop Rock", "US");

        createGroupEntity(group1, "2013-06-13");
        createGroupEntity(group2, "2016-08-08");
        createGroupEntity(group3, "2002-01-01");

        Artist soloArtist = new Artist();
        soloArtist.setArtistName("IU");
        soloArtist.setType(ArtistType.Solo);
        soloArtist.setGenre("K-pop");
        soloArtist.setOriginCountry("KR");
        artistRepository.save(soloArtist);
    }

    @Test
    public void testFindByType() {
        List<Artist> groups = groupRepository.findByType(ArtistType.Group);
        assertEquals(3, groups.size());
    }

    @Test
    public void testFindGroupsByGenre() {
        List<Artist> kpopGroups = groupRepository.findGroupsByGenre(ArtistType.Group, "K-pop");
        assertEquals(2, kpopGroups.size());
        
        List<Artist> rockGroups = groupRepository.findGroupsByGenre(ArtistType.Group, "Rock");
        assertEquals(1, rockGroups.size());
        assertEquals("OneRepublic", rockGroups.get(0).getArtistName());
    }

    @Test
    public void testFindGroupsByCountry() {
        List<Artist> koreanGroups = groupRepository.findGroupsByCountry(ArtistType.Group, "KR");
        assertEquals(2, koreanGroups.size());
        
        List<Artist> usGroups = groupRepository.findGroupsByCountry(ArtistType.Group, "US");
        assertEquals(1, usGroups.size());
        assertEquals("OneRepublic", usGroups.get(0).getArtistName());
    }

    @Test
    public void testFindGroupsByNameContaining() {
        List<Artist> btsGroups = groupRepository.findGroupsByNameContaining(ArtistType.Group, "BTS");
        assertEquals(1, btsGroups.size());
        assertEquals("BTS", btsGroups.get(0).getArtistName());
        
        List<Artist> pinkGroups = groupRepository.findGroupsByNameContaining(ArtistType.Group, "pink");
        assertEquals(1, pinkGroups.size());
        assertEquals("Blackpink", pinkGroups.get(0).getArtistName());
    }

    @Test
    public void testCountGroupsByLanguage() {
        long koreanGroups = groupRepository.countGroupsByLanguage(ArtistType.Group, "Korean");
        assertEquals(3, koreanGroups);
    }
}
