package musicopedia.model;

import musicopedia.model.enums.ArtistGender;
import musicopedia.model.enums.ArtistType;
import musicopedia.model.enums.MembershipStatus;
import musicopedia.model.membership.GroupMembership;
import musicopedia.model.membership.GroupMembershipId;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ModelIntegrationTest {

    @Test
    public void testGroupMembershipRelationships() {
        UUID groupArtistId = UUID.randomUUID();
        Artist groupArtist = new Artist();
        groupArtist.setArtistId(groupArtistId);
        groupArtist.setArtistName("Test Group");
        groupArtist.setType(ArtistType.GROUP);
        
        Groups group = new Groups();
        group.setArtistId(groupArtistId);
        group.setArtist(groupArtist);
        group.setFormationDate(LocalDate.of(2015, 1, 1));
        group.setGroupGender(ArtistGender.MIXED);
        
        UUID memberId = UUID.randomUUID();
        Member member = new Member();
        member.setMemberId(memberId);
        member.setFullName("Test Member");
        member.setBirthDate(LocalDate.of(1995, 5, 15));
        
        UUID soloArtistId = UUID.randomUUID();
        Artist soloArtist = new Artist();
        soloArtist.setArtistId(soloArtistId);
        soloArtist.setArtistName("Solo Artist Name");
        soloArtist.setType(ArtistType.SOLO);
        
        Solo solo = new Solo();
        solo.setArtistId(soloArtistId);
        solo.setArtist(soloArtist);
        solo.setBirthDate(LocalDate.of(1995, 5, 15));
        solo.setGender(ArtistGender.FEMALE);
        
        member.setSoloArtist(soloArtist);
        
        GroupMembershipId membershipId = new GroupMembershipId();
        membershipId.setGroupId(groupArtistId);
        membershipId.setMemberId(memberId);
        
        GroupMembership membership = new GroupMembership();
        membership.setId(membershipId);
        membership.setGroup(groupArtist);
        membership.setMember(member);
        membership.setStatus(MembershipStatus.CURRENT);
        membership.setJoinDate(LocalDate.of(2020, 1, 1));
        
        assertEquals(groupArtistId, membership.getId().getGroupId());
        assertEquals(memberId, membership.getId().getMemberId());
        
        assertEquals(groupArtist, membership.getGroup());
        assertEquals(member, membership.getMember());
        
        assertEquals(MembershipStatus.CURRENT, membership.getStatus());
        
        assertEquals("Test Group", membership.getGroup().getArtistName());
        assertEquals("Test Member", membership.getMember().getFullName());
        
        assertEquals(soloArtist, membership.getMember().getSoloArtist());
        assertEquals("Solo Artist Name", membership.getMember().getSoloArtist().getArtistName());
    }
    
    @Test
    public void testFormerMemberRelationship() {
        UUID groupId = UUID.randomUUID();
        Artist groupArtist = new Artist();
        groupArtist.setArtistId(groupId);
        groupArtist.setArtistName("Former Member Test Group");
        
        UUID memberId = UUID.randomUUID();
        Member member = new Member();
        member.setMemberId(memberId);
        member.setFullName("Former Member");
        
        GroupMembershipId membershipId = new GroupMembershipId();
        membershipId.setGroupId(groupId);
        membershipId.setMemberId(memberId);
        
        LocalDate joinDate = LocalDate.of(2015, 1, 1);
        LocalDate leaveDate = LocalDate.of(2020, 12, 31);
        
        GroupMembership membership = new GroupMembership();
        membership.setId(membershipId);
        membership.setGroup(groupArtist);
        membership.setMember(member);
        membership.setStatus(MembershipStatus.FORMER);
        membership.setJoinDate(joinDate);
        membership.setLeaveDate(leaveDate);
        
        assertEquals(MembershipStatus.FORMER, membership.getStatus());
        assertEquals(joinDate, membership.getJoinDate());
        assertEquals(leaveDate, membership.getLeaveDate());
        
        assertEquals(groupArtist, membership.getGroup());
        assertEquals(member, membership.getMember());
    }
    
    @Test
    public void testInactiveMemberRelationship() {
        UUID groupId = UUID.randomUUID();
        Artist groupArtist = new Artist();
        groupArtist.setArtistId(groupId);
        
        UUID memberId = UUID.randomUUID();
        Member member = new Member();
        member.setMemberId(memberId);
        
        GroupMembershipId membershipId = new GroupMembershipId();
        membershipId.setGroupId(groupId);
        membershipId.setMemberId(memberId);
        
        GroupMembership membership = new GroupMembership();
        membership.setId(membershipId);
        membership.setGroup(groupArtist);
        membership.setMember(member);
        membership.setStatus(MembershipStatus.INACTIVE);
        membership.setJoinDate(LocalDate.of(2018, 5, 10));
        
        assertEquals(MembershipStatus.INACTIVE, membership.getStatus());
        assertNotNull(membership.getJoinDate());
        assertNull(membership.getLeaveDate());
    }
}
