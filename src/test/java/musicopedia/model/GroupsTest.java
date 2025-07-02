package musicopedia.model;

import musicopedia.model.enums.ArtistGender;
import musicopedia.model.enums.ArtistType;
import musicopedia.model.enums.GroupActivityStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class GroupsTest {

    @Test
    public void testConstructorAndGetters() {
        UUID artistId = UUID.randomUUID();
        
        Artist artist = new Artist();
        artist.setArtistId(artistId);
        artist.setArtistName("Test Group");
        artist.setType(ArtistType.GROUP);
        
        LocalDate formationDate = LocalDate.of(2015, 1, 1);
        LocalDate disbandDate = null;
        ArtistGender groupGender = ArtistGender.MIXED;
        GroupActivityStatus activityStatus = GroupActivityStatus.ACTIVE;
        
        Groups group = new Groups();
        group.setArtistId(artistId);
        group.setArtist(artist);
        group.setFormationDate(formationDate);
        group.setDisbandDate(disbandDate);
        group.setGroupGender(groupGender);
        group.setActivityStatus(activityStatus);
        
        assertEquals(artistId, group.getArtistId());
        assertEquals(artist, group.getArtist());
        assertEquals(formationDate, group.getFormationDate());
        assertNull(group.getDisbandDate());
        assertEquals(groupGender, group.getGroupGender());
        assertEquals(activityStatus, group.getActivityStatus());
    }
    
    @Test
    public void testEquality() {
        UUID artistId = UUID.randomUUID();
        
        Groups group1 = new Groups();
        group1.setArtistId(artistId);
        
        Groups group2 = new Groups();
        group2.setArtistId(artistId);
        
        Groups differentGroup = new Groups();
        differentGroup.setArtistId(UUID.randomUUID());
        assertEquals(group1, group2);
        assertEquals(group1.hashCode(), group2.hashCode());
        assertNotEquals(group1, differentGroup);
        assertNotEquals(group1.hashCode(), differentGroup.hashCode());
    }
    
    @Test
    public void testDisbandedGroup() {
        Groups group = new Groups();
        LocalDate formationDate = LocalDate.of(2015, 1, 1);
        LocalDate disbandDate = LocalDate.of(2023, 12, 31);
        
        group.setFormationDate(formationDate);
        group.setDisbandDate(disbandDate);
        group.setActivityStatus(GroupActivityStatus.DISBANDED);
        
        assertEquals(formationDate, group.getFormationDate());
        assertEquals(disbandDate, group.getDisbandDate());
        assertEquals(GroupActivityStatus.DISBANDED, group.getActivityStatus());
    }
    
    @Test
    public void testToString() {
        UUID artistId = UUID.randomUUID();
        LocalDate formationDate = LocalDate.of(2015, 1, 1);
        
        Groups group = new Groups();
        group.setArtistId(artistId);
        group.setFormationDate(formationDate);
        group.setGroupGender(ArtistGender.MIXED);
        group.setActivityStatus(GroupActivityStatus.ACTIVE);
        
        String toString = group.toString();
        
        assertTrue(toString.contains(artistId.toString()));
        assertTrue(toString.contains(formationDate.toString()));
        assertTrue(toString.contains(ArtistGender.MIXED.toString()));
        assertTrue(toString.contains(GroupActivityStatus.ACTIVE.toString()));
    }
}
