package musicopedia.repository;

import musicopedia.model.Subunit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface SubunitRepository extends JpaRepository<Subunit, UUID> {
    // Custom queries can be added here
}
