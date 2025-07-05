package musicopedia.service;

import musicopedia.model.Artist;
import musicopedia.model.Member;
import musicopedia.model.enums.MembershipStatus;
import musicopedia.model.membership.GroupMembership;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface GroupMembershipService {
    
    CompletableFuture<List<GroupMembership>> findByGroup(Artist group);
    
    CompletableFuture<List<GroupMembership>> findByGroupId(UUID groupId);
    
    CompletableFuture<List<GroupMembership>> findByMember(Member member);
    
    CompletableFuture<List<GroupMembership>> findByMemberId(UUID memberId);
    
    CompletableFuture<List<GroupMembership>> findByGroupIdAndStatus(UUID groupId, MembershipStatus status);
    
    CompletableFuture<List<GroupMembership>> findFormerMembersByGroupId(UUID groupId);
    
    CompletableFuture<List<GroupMembership>> findByGroupIdAndJoinDateAfter(UUID groupId, LocalDate date);
    
    CompletableFuture<List<GroupMembership>> findByGroupIdAndLeaveDateBefore(UUID groupId, LocalDate date);
    
    CompletableFuture<Long> countByGroupId(UUID groupId);
    
    CompletableFuture<Long> countByGroupIdAndStatus(UUID groupId, MembershipStatus status);
    
    CompletableFuture<List<GroupMembership>> findGroupsForMember(UUID memberId);
    
    CompletableFuture<GroupMembership> save(GroupMembership membership);
    
    CompletableFuture<GroupMembership> update(GroupMembership membership);
    
    CompletableFuture<Void> delete(GroupMembership membership);
}
