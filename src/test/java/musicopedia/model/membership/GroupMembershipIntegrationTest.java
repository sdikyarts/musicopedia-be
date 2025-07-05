package musicopedia.model.membership;

import musicopedia.model.Artist;
import musicopedia.model.Groups;
import musicopedia.model.Member;
import musicopedia.model.enums.ArtistGender;
import musicopedia.model.enums.ArtistType;
import musicopedia.model.enums.MembershipStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class GroupMembershipIntegrationTest {

    private Artist groupArtist;
    private Groups group;
    private List<Member> members;
    private List<GroupMembership> memberships;

    @BeforeEach
    public void setUp() {
        UUID groupId = UUID.randomUUID();
        groupArtist = new Artist();
        groupArtist.setArtistId(groupId);
        groupArtist.setArtistName("Test Music Group");
        groupArtist.setType(ArtistType.GROUP);
        
        group = new Groups();
        group.setArtistId(groupId);
        group.setArtist(groupArtist);
        group.setFormationDate(LocalDate.of(2010, 1, 1));
        group.setGroupGender(ArtistGender.MIXED);
        
        members = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            UUID memberId = UUID.randomUUID();
            Member member = new Member();
            member.setMemberId(memberId);
            member.setMemberName("Member " + (i + 1));
            member.setBirthDate(LocalDate.of(1990 + i, 1, 1));
            members.add(member);
        }
        
        memberships = new ArrayList<>();
        
        for (int i = 0; i < 2; i++) {
            GroupMembershipId id = new GroupMembershipId();
            id.setGroupId(groupId);
            id.setMemberId(members.get(i).getMemberId());
            
            GroupMembership membership = new GroupMembership();
            membership.setId(id);
            membership.setGroup(groupArtist);
            membership.setMember(members.get(i));
            membership.setStatus(MembershipStatus.CURRENT);
            membership.setJoinDate(LocalDate.of(2010, 1, 1));
            
            memberships.add(membership);
        }
        
        for (int i = 2; i < 4; i++) {
            GroupMembershipId id = new GroupMembershipId();
            id.setGroupId(groupId);
            id.setMemberId(members.get(i).getMemberId());
            
            GroupMembership membership = new GroupMembership();
            membership.setId(id);
            membership.setGroup(groupArtist);
            membership.setMember(members.get(i));
            membership.setStatus(MembershipStatus.FORMER);
            membership.setJoinDate(LocalDate.of(2010, 1, 1));
            membership.setLeaveDate(LocalDate.of(2020, 12, 31));
            
            memberships.add(membership);
        }
        
        GroupMembershipId id = new GroupMembershipId();
        id.setGroupId(groupId);
        id.setMemberId(members.get(4).getMemberId());
        
        GroupMembership membership = new GroupMembership();
        membership.setId(id);
        membership.setGroup(groupArtist);
        membership.setMember(members.get(4));
        membership.setStatus(MembershipStatus.INACTIVE);
        membership.setJoinDate(LocalDate.of(2010, 1, 1));
        
        memberships.add(membership);
    }
    
    @Test
    public void testMembershipStructure() {
        assertEquals(5, memberships.size());
        
        long currentCount = memberships.stream()
                .filter(m -> m.getStatus() == MembershipStatus.CURRENT)
                .count();
        long formerCount = memberships.stream()
                .filter(m -> m.getStatus() == MembershipStatus.FORMER)
                .count();
        long inactiveCount = memberships.stream()
                .filter(m -> m.getStatus() == MembershipStatus.INACTIVE)
                .count();
        
        assertEquals(2, currentCount);
        assertEquals(2, formerCount);
        assertEquals(1, inactiveCount);
    }
    
    @Test
    public void testCurrentMemberships() {
        List<GroupMembership> currentMemberships = memberships.stream()
                .filter(m -> m.getStatus() == MembershipStatus.CURRENT)
                .toList();
        
        for (GroupMembership membership : currentMemberships) {
            assertEquals(MembershipStatus.CURRENT, membership.getStatus());
            assertNotNull(membership.getJoinDate());
            assertNull(membership.getLeaveDate());
            assertEquals(groupArtist, membership.getGroup());
        }
    }
    
    @Test
    public void testFormerMemberships() {
        List<GroupMembership> formerMemberships = memberships.stream()
                .filter(m -> m.getStatus() == MembershipStatus.FORMER)
                .toList();
        
        for (GroupMembership membership : formerMemberships) {
            assertEquals(MembershipStatus.FORMER, membership.getStatus());
            assertNotNull(membership.getJoinDate());
            assertNotNull(membership.getLeaveDate());
            assertTrue(membership.getJoinDate().isBefore(membership.getLeaveDate()));
            assertEquals(groupArtist, membership.getGroup());
        }
    }
    
    @Test
    public void testInactiveMemberships() {
        List<GroupMembership> inactiveMemberships = memberships.stream()
                .filter(m -> m.getStatus() == MembershipStatus.INACTIVE)
                .toList();
        
        for (GroupMembership membership : inactiveMemberships) {
            assertEquals(MembershipStatus.INACTIVE, membership.getStatus());
            assertNotNull(membership.getJoinDate());
            assertNull(membership.getLeaveDate());
            assertEquals(groupArtist, membership.getGroup());
        }
    }
    
    @Test
    public void testMembershipLifecycle() {
        UUID newMemberId = UUID.randomUUID();
        Member newMember = new Member();
        newMember.setMemberId(newMemberId);
        newMember.setMemberName("Lifecycle Test Member");
        
        GroupMembershipId id = new GroupMembershipId();
        id.setGroupId(groupArtist.getArtistId());
        id.setMemberId(newMemberId);
        
        GroupMembership membership = new GroupMembership();
        membership.setId(id);
        membership.setGroup(groupArtist);
        membership.setMember(newMember);
        membership.setStatus(MembershipStatus.CURRENT);
        membership.setJoinDate(LocalDate.now().minusYears(1));
        
        assertEquals(MembershipStatus.CURRENT, membership.getStatus());
        assertNull(membership.getLeaveDate());
        
        membership.setStatus(MembershipStatus.INACTIVE);
        assertEquals(MembershipStatus.INACTIVE, membership.getStatus());
        assertNull(membership.getLeaveDate());
        
        LocalDate leaveDate = LocalDate.now();
        membership.setStatus(MembershipStatus.FORMER);
        membership.setLeaveDate(leaveDate);
        
        assertEquals(MembershipStatus.FORMER, membership.getStatus());
        assertEquals(leaveDate, membership.getLeaveDate());
    }
}
