package musicopedia.service.impl;

import musicopedia.model.Artist;
import musicopedia.model.Member;
import musicopedia.model.enums.MembershipStatus;
import musicopedia.model.membership.GroupMembership;
import musicopedia.repository.GroupMembershipRepository;
import musicopedia.service.GroupMembershipService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@Transactional
public class GroupMembershipServiceImpl implements GroupMembershipService {

    private final GroupMembershipRepository groupMembershipRepository;

    public GroupMembershipServiceImpl(GroupMembershipRepository groupMembershipRepository) {
        this.groupMembershipRepository = groupMembershipRepository;
    }

    @Override
    @Async("taskExecutor")
    @Transactional(readOnly = true)
    public CompletableFuture<List<GroupMembership>> findByGroup(Artist group) {
        List<GroupMembership> memberships = groupMembershipRepository.findByGroup(group);
        return CompletableFuture.completedFuture(memberships);
    }

    @Override
    @Async("taskExecutor")
    @Transactional(readOnly = true)
    public CompletableFuture<List<GroupMembership>> findByGroupId(UUID groupId) {
        List<GroupMembership> memberships = groupMembershipRepository.findByGroupId(groupId);
        return CompletableFuture.completedFuture(memberships);
    }

    @Override
    @Async("taskExecutor")
    @Transactional(readOnly = true)
    public CompletableFuture<List<GroupMembership>> findByMember(Member member) {
        List<GroupMembership> memberships = groupMembershipRepository.findByMember(member);
        return CompletableFuture.completedFuture(memberships);
    }

    @Override
    @Async("taskExecutor")
    @Transactional(readOnly = true)
    public CompletableFuture<List<GroupMembership>> findByMemberId(UUID memberId) {
        List<GroupMembership> memberships = groupMembershipRepository.findByMemberId(memberId);
        return CompletableFuture.completedFuture(memberships);
    }

    @Override
    @Async("taskExecutor")
    @Transactional(readOnly = true)
    public CompletableFuture<List<GroupMembership>> findByGroupIdAndStatus(UUID groupId, MembershipStatus status) {
        List<GroupMembership> memberships = groupMembershipRepository.findByGroupIdAndStatus(groupId, status);
        return CompletableFuture.completedFuture(memberships);
    }

    @Override
    @Async("taskExecutor")
    @Transactional(readOnly = true)
    public CompletableFuture<List<GroupMembership>> findFormerMembersByGroupId(UUID groupId) {
        List<GroupMembership> memberships = groupMembershipRepository.findFormerMembersByGroupId(groupId);
        return CompletableFuture.completedFuture(memberships);
    }

    @Override
    @Async("taskExecutor")
    @Transactional(readOnly = true)
    public CompletableFuture<List<GroupMembership>> findByGroupIdAndJoinDateAfter(UUID groupId, LocalDate date) {
        List<GroupMembership> memberships = groupMembershipRepository.findByGroupIdAndJoinDateAfter(groupId, date);
        return CompletableFuture.completedFuture(memberships);
    }

    @Override
    @Async("taskExecutor")
    @Transactional(readOnly = true)
    public CompletableFuture<List<GroupMembership>> findByGroupIdAndLeaveDateBefore(UUID groupId, LocalDate date) {
        List<GroupMembership> memberships = groupMembershipRepository.findByGroupIdAndLeaveDateBefore(groupId, date);
        return CompletableFuture.completedFuture(memberships);
    }

    @Override
    @Async("taskExecutor")
    @Transactional(readOnly = true)
    public CompletableFuture<Long> countByGroupId(UUID groupId) {
        long count = groupMembershipRepository.countByGroupId(groupId);
        return CompletableFuture.completedFuture(count);
    }

    @Override
    @Async("taskExecutor")
    @Transactional(readOnly = true)
    public CompletableFuture<Long> countByGroupIdAndStatus(UUID groupId, MembershipStatus status) {
        long count = groupMembershipRepository.countByGroupIdAndStatus(groupId, status);
        return CompletableFuture.completedFuture(count);
    }

    @Override
    @Async("taskExecutor")
    @Transactional(readOnly = true)
    public CompletableFuture<List<GroupMembership>> findGroupsForMember(UUID memberId) {
        List<GroupMembership> memberships = groupMembershipRepository.findGroupsForMember(memberId);
        return CompletableFuture.completedFuture(memberships);
    }

    @Override
    @Async("taskExecutor")
    public CompletableFuture<GroupMembership> save(GroupMembership membership) {
        GroupMembership savedMembership = groupMembershipRepository.save(membership);
        return CompletableFuture.completedFuture(savedMembership);
    }

    @Override
    @Async("taskExecutor")
    public CompletableFuture<GroupMembership> update(GroupMembership membership) {
        GroupMembership updatedMembership = groupMembershipRepository.save(membership);
        return CompletableFuture.completedFuture(updatedMembership);
    }

    @Override
    @Async("taskExecutor")
    public CompletableFuture<Void> delete(GroupMembership membership) {
        groupMembershipRepository.delete(membership);
        return CompletableFuture.completedFuture(null);
    }
}
