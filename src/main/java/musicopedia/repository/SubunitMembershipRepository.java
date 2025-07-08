package musicopedia.repository;

import musicopedia.model.membership.SubunitMembership;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface SubunitMembershipRepository extends JpaRepository<SubunitMembership, SubunitMembership.SubunitMembershipId> {
    List<SubunitMembership> findBySubunit_SubunitId(UUID subunitId);
    List<SubunitMembership> findByMember_MemberId(UUID memberId);
    void deleteBySubunit_SubunitId(UUID subunitId);
    void deleteByMember_MemberId(UUID memberId);
    boolean existsBySubunit_SubunitIdAndMember_MemberId(UUID subunitId, UUID memberId);
}
