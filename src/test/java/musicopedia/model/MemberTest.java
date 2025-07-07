package musicopedia.model;

import musicopedia.builder.MemberBuilder;
import musicopedia.builder.ArtistBuilder;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

public class MemberTest {

    @Test
    public void testConstructorAndGetters() {
        UUID memberId = UUID.randomUUID();
        String memberName = "Felix";
        String realName = "Felix Yongbok Lee";
        String description = "Lead dancer and rapper of Stray Kids";
        String image = "base64encodedimage";
        LocalDate birthDate = LocalDate.of(2000, 9, 15);
        String nationality = "AU";
        
        Artist soloArtist = new ArtistBuilder()
            .setArtistName("Felix")
            .build();
        soloArtist.setArtistId(UUID.randomUUID());
        Solo soloIdentity = new Solo();
        soloIdentity.setArtist(soloArtist);
        
        Member member = new MemberBuilder()
            .setMemberName(memberName)
            .setRealName(realName)
            .setDescription(description)
            .setImage(image)
            .setBirthDate(birthDate)
            .setNationality(nationality)
            .build();
        member.setMemberId(memberId);
        member.setSoloIdentities(java.util.List.of(soloIdentity));
        soloIdentity.setMember(member);
        
        assertEquals(memberId, member.getMemberId());
        assertEquals(memberName, member.getMemberName());
        assertEquals(realName, member.getRealName());
        assertEquals(description, member.getDescription());
        assertEquals(image, member.getImage());
        assertEquals(birthDate, member.getBirthDate());
        assertEquals(soloArtist, member.getSoloIdentities().get(0).getArtist());
        assertEquals(nationality, member.getNationality());
    }
    
    @Test
    public void testEquality() {
        UUID memberId = UUID.randomUUID();
        Member member1 = new Member();
        member1.setMemberId(memberId);
        member1.setMemberName("HAN");
        member1.setRealName("Han Ji-sung");
        Member member2 = new Member();
        member2.setMemberId(memberId);
        member2.setMemberName("HAN");
        member2.setRealName("Han Ji-sung");
        Member differentMember = new Member();
        differentMember.setMemberId(UUID.randomUUID());
        differentMember.setMemberName("Hyunjin");
        differentMember.setRealName("Hwang Hyun-jin");
        assertEquals(member1, member2);
        assertEquals(member1.hashCode(), member2.hashCode());
        assertNotEquals(member1, differentMember);
        assertNotEquals(member1.hashCode(), differentMember.hashCode());
    }
    
    @Test
    public void testMemberWithoutSoloArtist() {
        Member member = new Member();
        member.setMemberId(UUID.randomUUID());
        member.setMemberName("KIMCHAEWON");
        member.setRealName("Kim Chae-won");
        assertTrue(member.getSoloIdentities() == null || member.getSoloIdentities().isEmpty());
    }
    
    @Test
    public void testToString() {
        UUID memberId = UUID.randomUUID();
        String memberName = "Felix";
        Member member = new Member();
        member.setMemberId(memberId);
        member.setMemberName(memberName);
        member.setRealName("Felix Yongbok Lee");
        String toString = member.toString();
        assertTrue(toString.contains(memberId.toString()));
        assertTrue(toString.contains(memberName));
        assertTrue(toString.contains("Felix Yongbok Lee"));
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
    
    @Test
    public void testDeathDateAndIsDeceased() {
        Member member = new Member();
        assertFalse(member.isDeceased());
        assertNull(member.getDeathDate());
        LocalDate deathDate = LocalDate.of(2024, 1, 1);
        member.setDeathDate(deathDate);
        assertTrue(member.isDeceased());
        assertEquals(deathDate, member.getDeathDate());
    }
    
    @Test
    public void testNationalityField() {
        Member member = new Member();
        String nationality = "KR";
        member.setNationality(nationality);
        assertEquals(nationality, member.getNationality());
    }
}
