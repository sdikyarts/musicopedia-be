package musicopedia.repository;

import musicopedia.model.Artist;
import musicopedia.model.Member;
import musicopedia.model.enums.ArtistType;
import musicopedia.model.enums.MembershipStatus;
import musicopedia.model.membership.GroupMembership;
import musicopedia.model.membership.GroupMembershipId;
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
public class GroupMembershipRepositoryTest {

    @Autowired
    private GroupMembershipRepository groupMembershipRepository;
    
    @Autowired
    private ArtistRepository artistRepository;
    
    @Autowired
    private MemberRepository memberRepository;
    
    private Artist group1;
    private Artist group2;
    private Member member1;
    private Member member2;
    private Member member3;

    @BeforeEach
    void setup() {
        group1 = new Artist();
        group1.setArtistName("BTS");
        group1.setType(ArtistType.GROUP);
        artistRepository.save(group1);
        
        group2 = new Artist();
        group2.setArtistName("Blackpink");
        group2.setType(ArtistType.GROUP);
        artistRepository.save(group2);
        
        member1 = new Member();
        member1.setFullName("Member 1");
        memberRepository.save(member1);
        
        member2 = new Member();
        member2.setFullName("Member 2");
        memberRepository.save(member2);
        
        member3 = new Member();
        member3.setFullName("Member 3");
        memberRepository.save(member3);
        
        createMembership(group1, member1, MembershipStatus.Current, 
                LocalDate.of(2013, 6, 13), null);
        
        createMembership(group1, member2, MembershipStatus.Current,
                LocalDate.of(2013, 6, 13), null);
        
        createMembership(group2, member3, MembershipStatus.Current,
                LocalDate.of(2016, 8, 8), null);
        
        createMembership(group2, member1, MembershipStatus.Former,
                LocalDate.of(2015, 1, 1), LocalDate.of(2016, 1, 1));
    }
    
    private void createMembership(Artist group, Member member, MembershipStatus status, 
                                 LocalDate joinDate, LocalDate leaveDate) {
        GroupMembershipId id = new GroupMembershipId();
        id.setGroupId(group.getArtistId());
        id.setMemberId(member.getMemberId());
        
        GroupMembership membership = new GroupMembership();
        membership.setId(id);
        membership.setGroup(group);
        membership.setMember(member);
        membership.setStatus(status);
        membership.setJoinDate(joinDate);
        membership.setLeaveDate(leaveDate);
        
        groupMembershipRepository.save(membership);
    }

    @Test
    public void testFindByGroup() {
        List<GroupMembership> memberships = groupMembershipRepository.findByGroup(group1);
        assertEquals(2, memberships.size());
    }

    @Test
    public void testFindByGroupId() {
        List<GroupMembership> memberships = groupMembershipRepository.findByGroupId(group1.getArtistId());
        assertEquals(2, memberships.size());
    }

    @Test
    public void testFindByMember() {
        List<GroupMembership> memberships = groupMembershipRepository.findByMember(member1);
        assertEquals(2, memberships.size());
    }

    @Test
    public void testFindByMemberId() {
        List<GroupMembership> memberships = groupMembershipRepository.findByMemberId(member1.getMemberId());
        assertEquals(2, memberships.size());
    }

    @Test
    public void testFindByGroupIdAndStatus() {
        List<GroupMembership> currentMembers = groupMembershipRepository.findByGroupIdAndStatus(
                group1.getArtistId(), MembershipStatus.Current);
        assertEquals(2, currentMembers.size());
        
        List<GroupMembership> formerMembers = groupMembershipRepository.findByGroupIdAndStatus(
                group1.getArtistId(), MembershipStatus.Former);
        assertEquals(0, formerMembers.size());
    }

    @Test
    public void testFindFormerMembersByGroupId() {
        List<GroupMembership> formerMembers = groupMembershipRepository.findFormerMembersByGroupId(group2.getArtistId());
        assertEquals(1, formerMembers.size());
        assertEquals(member1.getFullName(), formerMembers.get(0).getMember().getFullName());
    }

    @Test
    public void testFindByGroupIdAndJoinDateAfter() {
        LocalDate date = LocalDate.of(2015, 6, 1);
        List<GroupMembership> memberships = groupMembershipRepository.findByGroupIdAndJoinDateAfter(
                group2.getArtistId(), date);
        assertEquals(1, memberships.size());
        assertEquals("Member 3", memberships.get(0).getMember().getFullName());
    }

    @Test
    public void testFindByGroupIdAndLeaveDateBefore() {
        LocalDate date = LocalDate.of(2017, 1, 1);
        List<GroupMembership> memberships = groupMembershipRepository.findByGroupIdAndLeaveDateBefore(
                group2.getArtistId(), date);
        assertEquals(1, memberships.size());
        assertEquals("Member 1", memberships.get(0).getMember().getFullName());
    }

    @Test
    public void testCountByGroupId() {
        long count = groupMembershipRepository.countByGroupId(group1.getArtistId());
        assertEquals(2, count);
    }

    @Test
    public void testCountByGroupIdAndStatus() {
        long count = groupMembershipRepository.countByGroupIdAndStatus(
                group1.getArtistId(), MembershipStatus.Current);
        assertEquals(2, count);
    }

    @Test
    public void testFindGroupsForMember() {
        List<GroupMembership> memberships = groupMembershipRepository.findGroupsForMember(member1.getMemberId());
        assertEquals(2, memberships.size());
    }
}
