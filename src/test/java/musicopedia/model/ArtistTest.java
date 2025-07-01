package musicopedia.model;

import musicopedia.model.enums.ArtistType;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

public class ArtistTest {

    @Test
    public void testConstructorAndGetters() {
        UUID artistId = UUID.randomUUID();
        String spotifyId = "1234567890abcdefghijk";
        String artistName = "Test Artist";
        String description = "This is a test artist description";
        String image = "base64encodedimage";
        ArtistType type = ArtistType.Group;
        String primaryLanguage = "English";
        String genre = "Pop";
        String originCountry = "US";
        
        Artist artist = new Artist();
        artist.setArtistId(artistId);
        artist.setSpotifyId(spotifyId);
        artist.setArtistName(artistName);
        artist.setDescription(description);
        artist.setImage(image);
        artist.setType(type);
        artist.setPrimaryLanguage(primaryLanguage);
        artist.setGenre(genre);
        artist.setOriginCountry(originCountry);
        
        assertEquals(artistId, artist.getArtistId());
        assertEquals(spotifyId, artist.getSpotifyId());
        assertEquals(artistName, artist.getArtistName());
        assertEquals(description, artist.getDescription());
        assertEquals(image, artist.getImage());
        assertEquals(type, artist.getType());
        assertEquals(primaryLanguage, artist.getPrimaryLanguage());
        assertEquals(genre, artist.getGenre());
        assertEquals(originCountry, artist.getOriginCountry());
    }
    
    @Test
    public void testEquality() {
        UUID artistId = UUID.randomUUID();
        
        Artist artist1 = new Artist();
        artist1.setArtistId(artistId);
        artist1.setArtistName("Test Artist");
        
        Artist artist2 = new Artist();
        artist2.setArtistId(artistId);
        artist2.setArtistName("Test Artist");
        
        Artist differentArtist = new Artist();
        differentArtist.setArtistId(UUID.randomUUID());
        differentArtist.setArtistName("Different Artist");
        assertEquals(artist1, artist2);
        assertEquals(artist1.hashCode(), artist2.hashCode());
        assertNotEquals(artist1, differentArtist);
        assertNotEquals(artist1.hashCode(), differentArtist.hashCode());
    }
    
    @Test
    public void testToString() {
        UUID artistId = UUID.randomUUID();
        String artistName = "Test Artist";
        
        Artist artist = new Artist();
        artist.setArtistId(artistId);
        artist.setArtistName(artistName);
        
        String toString = artist.toString();
        
        assertTrue(toString.contains(artistId.toString()));
        assertTrue(toString.contains(artistName));
    }
}
