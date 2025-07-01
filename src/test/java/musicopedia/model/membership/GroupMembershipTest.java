package musicopedia.model.membership;

import musicopedia.model.Artist;
import musicopedia.model.Member;
import musicopedia.model.enums.MembershipStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class GroupMembershipTest {

    @Test
    public void testConstructorAndGetters() {
        GroupMembershipId id = new GroupMembershipId();
        id.setGroupId(UUID.randomUUID());
        id.setMemberId(UUID.randomUUID());
        
        Artist group = new Artist();
        group.setArtistId(id.getGroupId());
        group.setArtistName("Test Group");
        
        Member member = new Member();
        member.setMemberId(id.getMemberId());
        member.setFullName("Test Member");
        
        MembershipStatus status = MembershipStatus.CURRENT;
        LocalDate joinDate = LocalDate.of(2020, 1, 1);
        LocalDate leaveDate = null; // Current member
        
        GroupMembership membership = new GroupMembership();
        membership.setId(id);
        membership.setGroup(group);
        membership.setMember(member);
        membership.setStatus(status);
        membership.setJoinDate(joinDate);
        membership.setLeaveDate(leaveDate);
        
        assertEquals(id, membership.getId());
        assertEquals(group, membership.getGroup());
        assertEquals(member, membership.getMember());
        assertEquals(status, membership.getStatus());
        assertEquals(joinDate, membership.getJoinDate());
        assertNull(membership.getLeaveDate());
    }
    
    @Test
    public void testEquality() {
        GroupMembershipId id1 = new GroupMembershipId();
        id1.setGroupId(UUID.randomUUID());
        id1.setMemberId(UUID.randomUUID());
        
        GroupMembership membership1 = new GroupMembership();
        membership1.setId(id1);
        membership1.setStatus(MembershipStatus.CURRENT);
        membership1.setJoinDate(LocalDate.of(2020, 1, 1));
        
        GroupMembership membership2 = new GroupMembership();
        membership2.setId(id1);
        membership2.setStatus(MembershipStatus.CURRENT);
        membership2.setJoinDate(LocalDate.of(2020, 1, 1));
        
        GroupMembershipId id2 = new GroupMembershipId();
        id2.setGroupId(UUID.randomUUID());
        id2.setMemberId(UUID.randomUUID());
        
        GroupMembership differentMembership = new GroupMembership();
        differentMembership.setId(id2);
        
        assertEquals(membership1, membership2);
        assertEquals(membership1.hashCode(), membership2.hashCode());
        assertNotEquals(membership1, differentMembership);
    }
    
    @Test
    public void testFormerMemberWithLeaveDate() {
        GroupMembership membership = new GroupMembership();
        LocalDate joinDate = LocalDate.of(2020, 1, 1);
        LocalDate leaveDate = LocalDate.of(2023, 12, 31);
        
        membership.setStatus(MembershipStatus.FORMER);
        membership.setJoinDate(joinDate);
        membership.setLeaveDate(leaveDate);
        
        assertEquals(MembershipStatus.FORMER, membership.getStatus());
        assertEquals(joinDate, membership.getJoinDate());
        assertEquals(leaveDate, membership.getLeaveDate());
    }
    
    @Test
    public void testInactiveMember() {
        GroupMembership membership = new GroupMembership();
        LocalDate joinDate = LocalDate.of(2020, 1, 1);
        
        membership.setStatus(MembershipStatus.INACTIVE);
        membership.setJoinDate(joinDate);
        
        assertEquals(MembershipStatus.INACTIVE, membership.getStatus());
        assertEquals(joinDate, membership.getJoinDate());
        assertNull(membership.getLeaveDate());
    }
    
    @Test
    public void testToString() {
        GroupMembershipId id = new GroupMembershipId();
        UUID groupId = UUID.randomUUID();
        UUID memberId = UUID.randomUUID();
        id.setGroupId(groupId);
        id.setMemberId(memberId);
        
        GroupMembership membership = new GroupMembership();
        membership.setId(id);
        membership.setStatus(MembershipStatus.CURRENT);
        
        String toString = membership.toString();
        
        assertTrue(toString.contains(groupId.toString()));
        assertTrue(toString.contains(memberId.toString()));
        assertTrue(toString.contains(MembershipStatus.CURRENT.toString()));
    }
}
