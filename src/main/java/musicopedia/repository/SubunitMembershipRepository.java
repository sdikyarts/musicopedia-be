package musicopedia.repository;

import musicopedia.model.membership.SubunitMembership;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface SubunitMembershipRepository extends JpaRepository<SubunitMembership, SubunitMembership.SubunitMembershipId> {
    List<SubunitMembership> findBySubunitId(UUID subunitId);
    List<SubunitMembership> findByMemberId(UUID memberId);
    void deleteBySubunitId(UUID subunitId);
    void deleteByMemberId(UUID memberId);
    boolean existsBySubunitIdAndMemberId(UUID subunitId, UUID memberId);
}
