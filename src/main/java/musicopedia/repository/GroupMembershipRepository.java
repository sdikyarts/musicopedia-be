package musicopedia.repository;

import musicopedia.model.Artist;
import musicopedia.model.Member;
import musicopedia.model.enums.MembershipStatus;
import musicopedia.model.membership.GroupMembership;
import musicopedia.model.membership.GroupMembershipId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface GroupMembershipRepository extends JpaRepository<GroupMembership, GroupMembershipId> {

    List<GroupMembership> findByGroup(Artist group);
    

    @Query("SELECT m FROM GroupMembership m WHERE m.id.groupId = :groupId")
    List<GroupMembership> findByGroupId(@Param("groupId") UUID groupId);
    

    List<GroupMembership> findByMember(Member member);
    

    @Query("SELECT m FROM GroupMembership m WHERE m.id.memberId = :memberId")
    List<GroupMembership> findByMemberId(@Param("memberId") UUID memberId);
    

    @Query("SELECT m FROM GroupMembership m WHERE m.id.groupId = :groupId AND m.status = :status")
    List<GroupMembership> findByGroupIdAndStatus(
            @Param("groupId") UUID groupId, 
            @Param("status") MembershipStatus status);
    

    @Query("SELECT m FROM GroupMembership m WHERE m.id.groupId = :groupId AND m.leaveDate IS NOT NULL")
    List<GroupMembership> findFormerMembersByGroupId(@Param("groupId") UUID groupId);
    

    @Query("SELECT m FROM GroupMembership m WHERE m.id.groupId = :groupId AND m.joinDate > :date")
    List<GroupMembership> findByGroupIdAndJoinDateAfter(
            @Param("groupId") UUID groupId, 
            @Param("date") LocalDate date);
    

    @Query("SELECT m FROM GroupMembership m WHERE m.id.groupId = :groupId AND m.leaveDate < :date")
    List<GroupMembership> findByGroupIdAndLeaveDateBefore(
            @Param("groupId") UUID groupId, 
            @Param("date") LocalDate date);
    

    @Query("SELECT COUNT(m) FROM GroupMembership m WHERE m.id.groupId = :groupId")
    long countByGroupId(@Param("groupId") UUID groupId);
    

    @Query("SELECT COUNT(m) FROM GroupMembership m WHERE m.id.groupId = :groupId AND m.status = :status")
    long countByGroupIdAndStatus(
            @Param("groupId") UUID groupId, 
            @Param("status") MembershipStatus status);
    

    @Query("SELECT m FROM GroupMembership m WHERE m.id.memberId = :memberId")
    List<GroupMembership> findGroupsForMember(@Param("memberId") UUID memberId);
}
