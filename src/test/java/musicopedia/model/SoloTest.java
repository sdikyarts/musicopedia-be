package musicopedia.model;

import musicopedia.model.enums.ArtistGender;
import musicopedia.model.enums.ArtistType;
import musicopedia.model.enums.GroupAffiliationStatus;
import musicopedia.builder.SoloBuilder;
import musicopedia.builder.ArtistBuilder;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class SoloTest {

    @Test
    public void testConstructorAndGetters() {
        UUID artistId = UUID.randomUUID();
        Artist artist = new ArtistBuilder()
            .setArtistName("Taylor Swift")
            .setType(ArtistType.SOLO)
            .build();
        artist.setArtistId(artistId);
        LocalDate birthDate = LocalDate.of(1989, 12, 13);
        LocalDate deathDate = null;
        LocalDate debutDate = LocalDate.of(2006, 6, 19);
        ArtistGender gender = ArtistGender.FEMALE;
        GroupAffiliationStatus groupAffiliationStatus = GroupAffiliationStatus.NEVER_IN_A_GROUP;
        String realName = "Taylor Alison Swift";
        Solo solo = new SoloBuilder()
            .setArtistName(artist.getArtistName())
            .setType(artist.getType())
            .setBirthDate(birthDate)
            .setDeathDate(deathDate)
            .setDebutDate(debutDate)
            .setGender(gender)
            .setGroupAffiliationStatus(groupAffiliationStatus)
            .setRealName(realName)
            .buildSolo();
        solo.setArtistId(artistId);
        solo.setArtist(artist);
        assertEquals(artistId, solo.getArtistId());
        assertEquals(artist, solo.getArtist());
        assertEquals(birthDate, solo.getBirthDate());
        assertNull(solo.getDeathDate());
        assertEquals(debutDate, solo.getDebutDate());
        assertEquals(gender, solo.getGender());
        assertEquals(groupAffiliationStatus, solo.getGroupAffiliationStatus());
        assertEquals(realName, solo.getRealName());
        assertEquals("Taylor Swift", solo.getArtist().getArtistName());
    }

    @Test
    public void testDebutDateRange() {
        Solo solo = new Solo();
        LocalDate debutDate = LocalDate.of(2010, 1, 1);
        solo.setDebutDate(debutDate);
        assertEquals(debutDate, solo.getDebutDate());
        // Out of range
        LocalDate start = LocalDate.of(2011, 1, 1);
        LocalDate end = LocalDate.of(2012, 1, 1);
        assertFalse(debutDate.isAfter(start) && debutDate.isBefore(end));
        // In range
        start = LocalDate.of(2009, 1, 1);
        end = LocalDate.of(2011, 12, 31);
        assertTrue((!debutDate.isBefore(start)) && (!debutDate.isAfter(end)));
    }
    
    @Test
    public void testEquality() {
        UUID artistId = UUID.randomUUID();
        Solo solo1 = new Solo();
        solo1.setArtistId(artistId);
        solo1.setRealName("Onika Tanya Maraj");
        solo1.setArtist(new ArtistBuilder().setArtistName("Nicki Minaj").setType(ArtistType.SOLO).build());
        Solo solo2 = new Solo();
        solo2.setArtistId(artistId);
        solo2.setRealName("Onika Tanya Maraj");
        solo2.setArtist(new ArtistBuilder().setArtistName("Nicki Minaj").setType(ArtistType.SOLO).build());
        Solo differentSolo = new Solo();
        differentSolo.setArtistId(UUID.randomUUID());
        differentSolo.setRealName("Jeon Jung-kook");
        differentSolo.setArtist(new ArtistBuilder().setArtistName("Jung Kook").setType(ArtistType.SOLO).build());
        assertEquals(solo1, solo2);
        assertEquals(solo1.hashCode(), solo2.hashCode());
        assertNotEquals(solo1, differentSolo);
        assertNotEquals(solo1.hashCode(), differentSolo.hashCode());
    }
    
    @Test
    public void testDeceasedArtist() {
        Solo solo = new Solo();
        LocalDate birthDate = LocalDate.of(1940, 10, 9);
        LocalDate deathDate = LocalDate.of(1980, 12, 8);
        
        solo.setBirthDate(birthDate);
        solo.setDeathDate(deathDate);
        assertEquals(birthDate, solo.getBirthDate());
        assertEquals(deathDate, solo.getDeathDate());
    }
    
    @Test
    public void testToString() {
        UUID artistId = UUID.randomUUID();
        LocalDate birthDate = LocalDate.of(1990, 3, 15);
        
        Solo solo = new Solo();
        solo.setArtistId(artistId);
        solo.setBirthDate(birthDate);
        solo.setGender(ArtistGender.FEMALE);
        solo.setGroupAffiliationStatus(GroupAffiliationStatus.WAS_IN_A_GROUP);
        
        String toString = solo.toString();
        
        assertTrue(toString.contains(artistId.toString()));
        assertTrue(toString.contains(birthDate.toString()));
        assertTrue(toString.contains(ArtistGender.FEMALE.toString()));
        assertTrue(toString.contains(GroupAffiliationStatus.WAS_IN_A_GROUP.toString()));
    }
    
    @Test
    public void testEqualsBranches() {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        Artist artist1 = new ArtistBuilder().setArtistName("A").setType(ArtistType.SOLO).build();
        artist1.setArtistId(id1);
        Artist artist2 = new ArtistBuilder().setArtistName("B").setType(ArtistType.SOLO).build();
        artist2.setArtistId(id2);

        Solo solo = new Solo();
        solo.setArtistId(id1);
        solo.setRealName("Name");
        solo.setArtist(artist1);

        // same object
        assertEquals(solo, solo);
        // null
        assertNotEquals(solo, null);
        // different class
        assertNotEquals(solo, "not a solo");
        // different artistId
        Solo diffId = new Solo();
        diffId.setArtistId(id2);
        diffId.setRealName("Name");
        diffId.setArtist(artist1);
        assertNotEquals(solo, diffId);
        // different realName
        Solo diffName = new Solo();
        diffName.setArtistId(id1);
        diffName.setRealName("Other");
        diffName.setArtist(artist1);
        assertNotEquals(solo, diffName);
        // different artist
        Solo diffArtist = new Solo();
        diffArtist.setArtistId(id1);
        diffArtist.setRealName("Name");
        diffArtist.setArtist(artist2);
        assertNotEquals(solo, diffArtist);
        // all equal
        Solo equalSolo = new Solo();
        equalSolo.setArtistId(id1);
        equalSolo.setRealName("Name");
        equalSolo.setArtist(artist1);
        assertEquals(solo, equalSolo);
    }
}
