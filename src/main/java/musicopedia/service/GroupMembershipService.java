package musicopedia.service;

import musicopedia.model.Artist;
import musicopedia.model.Member;
import musicopedia.model.enums.MembershipStatus;
import musicopedia.model.membership.GroupMembership;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface GroupMembershipService {
    
    List<GroupMembership> findByGroup(Artist group);
    
    List<GroupMembership> findByGroupId(UUID groupId);
    
    List<GroupMembership> findByMember(Member member);
    
    List<GroupMembership> findByMemberId(UUID memberId);
    
    List<GroupMembership> findByGroupIdAndStatus(UUID groupId, MembershipStatus status);
    
    List<GroupMembership> findFormerMembersByGroupId(UUID groupId);
    
    List<GroupMembership> findByGroupIdAndJoinDateAfter(UUID groupId, LocalDate date);
    
    List<GroupMembership> findByGroupIdAndLeaveDateBefore(UUID groupId, LocalDate date);
    
    long countByGroupId(UUID groupId);
    
    long countByGroupIdAndStatus(UUID groupId, MembershipStatus status);
    
    List<GroupMembership> findGroupsForMember(UUID memberId);
    
    GroupMembership save(GroupMembership membership);
    
    GroupMembership update(GroupMembership membership);
    
    void delete(GroupMembership membership);
}
