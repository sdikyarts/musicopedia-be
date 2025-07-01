package musicopedia.service.impl;

import musicopedia.model.Artist;
import musicopedia.model.Member;
import musicopedia.model.enums.MembershipStatus;
import musicopedia.model.membership.GroupMembership;
import musicopedia.repository.GroupMembershipRepository;
import musicopedia.service.GroupMembershipService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class GroupMembershipServiceImpl implements GroupMembershipService {

    private final GroupMembershipRepository groupMembershipRepository;

    public GroupMembershipServiceImpl(GroupMembershipRepository groupMembershipRepository) {
        this.groupMembershipRepository = groupMembershipRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<GroupMembership> findByGroup(Artist group) {
        return groupMembershipRepository.findByGroup(group);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GroupMembership> findByGroupId(UUID groupId) {
        return groupMembershipRepository.findByGroupId(groupId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GroupMembership> findByMember(Member member) {
        return groupMembershipRepository.findByMember(member);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GroupMembership> findByMemberId(UUID memberId) {
        return groupMembershipRepository.findByMemberId(memberId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GroupMembership> findByGroupIdAndStatus(UUID groupId, MembershipStatus status) {
        return groupMembershipRepository.findByGroupIdAndStatus(groupId, status);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GroupMembership> findFormerMembersByGroupId(UUID groupId) {
        return groupMembershipRepository.findFormerMembersByGroupId(groupId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GroupMembership> findByGroupIdAndJoinDateAfter(UUID groupId, LocalDate date) {
        return groupMembershipRepository.findByGroupIdAndJoinDateAfter(groupId, date);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GroupMembership> findByGroupIdAndLeaveDateBefore(UUID groupId, LocalDate date) {
        return groupMembershipRepository.findByGroupIdAndLeaveDateBefore(groupId, date);
    }

    @Override
    @Transactional(readOnly = true)
    public long countByGroupId(UUID groupId) {
        return groupMembershipRepository.countByGroupId(groupId);
    }

    @Override
    @Transactional(readOnly = true)
    public long countByGroupIdAndStatus(UUID groupId, MembershipStatus status) {
        return groupMembershipRepository.countByGroupIdAndStatus(groupId, status);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GroupMembership> findGroupsForMember(UUID memberId) {
        return groupMembershipRepository.findGroupsForMember(memberId);
    }

    @Override
    public GroupMembership save(GroupMembership membership) {
        return groupMembershipRepository.save(membership);
    }

    @Override
    public GroupMembership update(GroupMembership membership) {
        return groupMembershipRepository.save(membership);
    }

    @Override
    public void delete(GroupMembership membership) {
        groupMembershipRepository.delete(membership);
    }
}
