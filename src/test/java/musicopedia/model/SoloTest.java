package musicopedia.model;

import musicopedia.model.enums.ArtistGender;
import musicopedia.model.enums.ArtistType;
import musicopedia.model.enums.GroupAffiliationStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class SoloTest {

    @Test
    public void testConstructorAndGetters() {
        UUID artistId = UUID.randomUUID();
        
        Artist artist = new Artist();
        artist.setArtistId(artistId);
        artist.setArtistName("Solo Artist");
        artist.setType(ArtistType.SOLO);
        
        LocalDate birthDate = LocalDate.of(1990, 3, 15);
        LocalDate deathDate = null;
        ArtistGender gender = ArtistGender.MALE;
        GroupAffiliationStatus groupAffiliationStatus = GroupAffiliationStatus.NEVER_IN_A_GROUP;
        
        Solo solo = new Solo();
        solo.setArtistId(artistId);
        solo.setArtist(artist);
        solo.setBirthDate(birthDate);
        solo.setDeathDate(deathDate);
        solo.setGender(gender);
        solo.setGroupAffiliationStatus(groupAffiliationStatus);
        
        assertEquals(artistId, solo.getArtistId());
        assertEquals(artist, solo.getArtist());
        assertEquals(birthDate, solo.getBirthDate());
        assertNull(solo.getDeathDate());
        assertEquals(gender, solo.getGender());
        assertEquals(groupAffiliationStatus, solo.getGroupAffiliationStatus());
    }
    
    @Test
    public void testEquality() {
        UUID artistId = UUID.randomUUID();
        
        Solo solo1 = new Solo();
        solo1.setArtistId(artistId);
        
        Solo solo2 = new Solo();
        solo2.setArtistId(artistId);
        
        Solo differentSolo = new Solo();
        differentSolo.setArtistId(UUID.randomUUID());
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
}
