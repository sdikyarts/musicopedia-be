package musicopedia.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

public class MemberTest {

    @Test
    public void testConstructorAndGetters() {
        UUID memberId = UUID.randomUUID();
        String fullName = "Test Member";
        String description = "This is a test member description";
        String image = "base64encodedimage";
        LocalDate birthDate = LocalDate.of(1995, 5, 15);
        
        Artist soloArtist = new Artist();
        soloArtist.setArtistId(UUID.randomUUID());
        
        Member member = new Member();
        member.setMemberId(memberId);
        member.setFullName(fullName);
        member.setDescription(description);
        member.setImage(image);
        member.setBirthDate(birthDate);
        member.setSoloArtist(soloArtist);
        
        assertEquals(memberId, member.getMemberId());
        assertEquals(fullName, member.getFullName());
        assertEquals(description, member.getDescription());
        assertEquals(image, member.getImage());
        assertEquals(birthDate, member.getBirthDate());
        assertEquals(soloArtist, member.getSoloArtist());
    }
    
    @Test
    public void testEquality() {
        UUID memberId = UUID.randomUUID();
        
        Member member1 = new Member();
        member1.setMemberId(memberId);
        member1.setFullName("Test Member");
        
        Member member2 = new Member();
        member2.setMemberId(memberId);
        member2.setFullName("Test Member");
        
        Member differentMember = new Member();
        differentMember.setMemberId(UUID.randomUUID());
        differentMember.setFullName("Different Member");
        assertEquals(member1, member2);
        assertEquals(member1.hashCode(), member2.hashCode());
        assertNotEquals(member1, differentMember);
        assertNotEquals(member1.hashCode(), differentMember.hashCode());
    }
    
    @Test
    public void testMemberWithoutSoloArtist() {
        Member member = new Member();
        member.setMemberId(UUID.randomUUID());
        member.setFullName("Group Member Only");
        
        assertNull(member.getSoloArtist());
    }
    
    @Test
    public void testToString() {
        UUID memberId = UUID.randomUUID();
        String fullName = "Test Member";
        
        Member member = new Member();
        member.setMemberId(memberId);
        member.setFullName(fullName);
        
        String toString = member.toString();
        
        assertTrue(toString.contains(memberId.toString()));
        assertTrue(toString.contains(fullName));
    }
    
    @Test
    public void testDescriptionField() {
        Member member = new Member();
        String description = "A talented member with years of experience in the music industry";
        
        member.setDescription(description);
        
        assertEquals(description, member.getDescription());
    }
    
    @Test
    public void testDescriptionCanBeNull() {
        Member member = new Member();
        member.setDescription(null);
        
        assertNull(member.getDescription());
    }
}
