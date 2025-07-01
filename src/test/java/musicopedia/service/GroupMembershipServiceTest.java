package musicopedia.service;

import musicopedia.model.Artist;
import musicopedia.model.Member;
import musicopedia.model.enums.ArtistType;
import musicopedia.model.enums.MembershipStatus;
import musicopedia.model.membership.GroupMembership;
import musicopedia.model.membership.GroupMembershipId;
import musicopedia.repository.GroupMembershipRepository;
import musicopedia.service.config.ServiceTestConfig;
import musicopedia.service.impl.GroupMembershipServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Import(ServiceTestConfig.class)
public class GroupMembershipServiceTest {

    @Mock
    private GroupMembershipRepository groupMembershipRepository;

    private GroupMembershipService groupMembershipService;
    
    private Artist testGroup;
    private Member testMember;
    private GroupMembership testMembership;
    private UUID groupId;
    private UUID memberId;
    private GroupMembershipId membershipId;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        groupMembershipService = new GroupMembershipServiceImpl(groupMembershipRepository);

        groupId = UUID.randomUUID();
        memberId = UUID.randomUUID();
        
        testGroup = new Artist();
        testGroup.setArtistId(groupId);
        testGroup.setArtistName("BTS");
        testGroup.setType(ArtistType.GROUP);
        
        testMember = new Member();
        testMember.setMemberId(memberId);
        testMember.setFullName("Kim Namjoon");
        
        membershipId = new GroupMembershipId();
        membershipId.setGroupId(groupId);
        membershipId.setMemberId(memberId);
        
        testMembership = new GroupMembership();
        testMembership.setId(membershipId);
        testMembership.setGroup(testGroup);
        testMembership.setMember(testMember);
        testMembership.setStatus(MembershipStatus.CURRENT);
        testMembership.setJoinDate(LocalDate.of(2013, 6, 13));
    }

    @Test
    void testFindByGroup() {
        List<GroupMembership> memberships = Arrays.asList(testMembership);
        when(groupMembershipRepository.findByGroup(testGroup)).thenReturn(memberships);

        List<GroupMembership> result = groupMembershipService.findByGroup(testGroup);

        assertEquals(1, result.size());
        assertEquals("Kim Namjoon", result.get(0).getMember().getFullName());
        assertEquals("BTS", result.get(0).getGroup().getArtistName());
        verify(groupMembershipRepository, times(1)).findByGroup(testGroup);
    }

    @Test
    void testFindByGroupId() {
        List<GroupMembership> memberships = Arrays.asList(testMembership);
        when(groupMembershipRepository.findByGroupId(groupId)).thenReturn(memberships);

        List<GroupMembership> result = groupMembershipService.findByGroupId(groupId);

        assertEquals(1, result.size());
        assertEquals(groupId, result.get(0).getId().getGroupId());
        verify(groupMembershipRepository, times(1)).findByGroupId(groupId);
    }

    @Test
    void testFindByMember() {
        List<GroupMembership> memberships = Arrays.asList(testMembership);
        when(groupMembershipRepository.findByMember(testMember)).thenReturn(memberships);

        List<GroupMembership> result = groupMembershipService.findByMember(testMember);

        assertEquals(1, result.size());
        assertEquals("Kim Namjoon", result.get(0).getMember().getFullName());
        verify(groupMembershipRepository, times(1)).findByMember(testMember);
    }

    @Test
    void testFindByMemberId() {
        List<GroupMembership> memberships = Arrays.asList(testMembership);
        when(groupMembershipRepository.findByMemberId(memberId)).thenReturn(memberships);

        List<GroupMembership> result = groupMembershipService.findByMemberId(memberId);

        assertEquals(1, result.size());
        assertEquals(memberId, result.get(0).getId().getMemberId());
        verify(groupMembershipRepository, times(1)).findByMemberId(memberId);
    }

    @Test
    void testFindByGroupIdAndStatus() {
        List<GroupMembership> memberships = Arrays.asList(testMembership);
        when(groupMembershipRepository.findByGroupIdAndStatus(groupId, MembershipStatus.CURRENT))
            .thenReturn(memberships);

        List<GroupMembership> result = groupMembershipService.findByGroupIdAndStatus(
            groupId, MembershipStatus.CURRENT);

        assertEquals(1, result.size());
        assertEquals(MembershipStatus.CURRENT, result.get(0).getStatus());
        verify(groupMembershipRepository, times(1))
            .findByGroupIdAndStatus(groupId, MembershipStatus.CURRENT);
    }

    @Test
    void testFindFormerMembersByGroupId() {
        GroupMembership formerMembership = createMembership(MembershipStatus.FORMER);
        List<GroupMembership> memberships = Arrays.asList(formerMembership);
        
        when(groupMembershipRepository.findFormerMembersByGroupId(groupId)).thenReturn(memberships);

        List<GroupMembership> result = groupMembershipService.findFormerMembersByGroupId(groupId);

        assertEquals(1, result.size());
        verify(groupMembershipRepository, times(1)).findFormerMembersByGroupId(groupId);
    }

    @Test
    void testFindByGroupIdAndJoinDateAfter() {
        LocalDate date = LocalDate.of(2010, 1, 1);
        List<GroupMembership> memberships = Arrays.asList(testMembership);
        
        when(groupMembershipRepository.findByGroupIdAndJoinDateAfter(groupId, date))
            .thenReturn(memberships);

        List<GroupMembership> result = groupMembershipService.findByGroupIdAndJoinDateAfter(groupId, date);

        assertEquals(1, result.size());
        verify(groupMembershipRepository, times(1)).findByGroupIdAndJoinDateAfter(groupId, date);
    }

    @Test
    void testFindByGroupIdAndLeaveDateBefore() {
        LocalDate date = LocalDate.of(2020, 1, 1);
        GroupMembership formerMembership = createMembership(MembershipStatus.FORMER);
        formerMembership.setLeaveDate(LocalDate.of(2019, 1, 1));
        
        List<GroupMembership> memberships = Arrays.asList(formerMembership);
        
        when(groupMembershipRepository.findByGroupIdAndLeaveDateBefore(groupId, date))
            .thenReturn(memberships);

        List<GroupMembership> result = groupMembershipService.findByGroupIdAndLeaveDateBefore(groupId, date);

        assertEquals(1, result.size());
        verify(groupMembershipRepository, times(1)).findByGroupIdAndLeaveDateBefore(groupId, date);
    }

    @Test
    void testCountByGroupId() {
        when(groupMembershipRepository.countByGroupId(groupId)).thenReturn(5L);

        long result = groupMembershipService.countByGroupId(groupId);

        assertEquals(5, result);
        verify(groupMembershipRepository, times(1)).countByGroupId(groupId);
    }

    @Test
    void testCountByGroupIdAndStatus() {
        when(groupMembershipRepository.countByGroupIdAndStatus(groupId, MembershipStatus.CURRENT))
            .thenReturn(3L);

        long result = groupMembershipService.countByGroupIdAndStatus(groupId, MembershipStatus.CURRENT);

        assertEquals(3, result);
        verify(groupMembershipRepository, times(1))
            .countByGroupIdAndStatus(groupId, MembershipStatus.CURRENT);
    }

    @Test
    void testFindGroupsForMember() {
        List<GroupMembership> memberships = Arrays.asList(testMembership);
        when(groupMembershipRepository.findGroupsForMember(memberId)).thenReturn(memberships);

        List<GroupMembership> result = groupMembershipService.findGroupsForMember(memberId);

        assertEquals(1, result.size());
        verify(groupMembershipRepository, times(1)).findGroupsForMember(memberId);
    }

    @Test
    void testSave() {
        when(groupMembershipRepository.save(any(GroupMembership.class))).thenReturn(testMembership);

        GroupMembership savedMembership = groupMembershipService.save(testMembership);

        assertEquals(groupId, savedMembership.getId().getGroupId());
        assertEquals(memberId, savedMembership.getId().getMemberId());
        verify(groupMembershipRepository, times(1)).save(testMembership);
    }

    @Test
    void testUpdate() {
        testMembership.setStatus(MembershipStatus.FORMER);
        testMembership.setLeaveDate(LocalDate.now());
        
        when(groupMembershipRepository.save(any(GroupMembership.class))).thenReturn(testMembership);

        GroupMembership updatedMembership = groupMembershipService.update(testMembership);

        assertEquals(MembershipStatus.FORMER, updatedMembership.getStatus());
        assertNotNull(updatedMembership.getLeaveDate());
        verify(groupMembershipRepository, times(1)).save(testMembership);
    }

    @Test
    void testDelete() {
        doNothing().when(groupMembershipRepository).delete(testMembership);

        groupMembershipService.delete(testMembership);

        verify(groupMembershipRepository, times(1)).delete(testMembership);
    }
    
    private GroupMembership createMembership(MembershipStatus status) {
        UUID groupId = UUID.randomUUID();
        UUID memberId = UUID.randomUUID();
        
        Artist group = new Artist();
        group.setArtistId(groupId);
        group.setArtistName("Test Group");
        
        Member member = new Member();
        member.setMemberId(memberId);
        member.setFullName("Test Member");
        
        GroupMembershipId id = new GroupMembershipId();
        id.setGroupId(groupId);
        id.setMemberId(memberId);
        
        GroupMembership membership = new GroupMembership();
        membership.setId(id);
        membership.setGroup(group);
        membership.setMember(member);
        membership.setStatus(status);
        membership.setJoinDate(LocalDate.of(2015, 1, 1));
        
        if (status == MembershipStatus.FORMER) {
            membership.setLeaveDate(LocalDate.of(2020, 1, 1));
        }
        
        return membership;
    }
}
