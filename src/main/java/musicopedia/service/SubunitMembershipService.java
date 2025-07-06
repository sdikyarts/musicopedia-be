package musicopedia.service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import musicopedia.model.membership.SubunitMembership;

public interface SubunitMembershipService {
    CompletableFuture<List<SubunitMembership>> findBySubunitId(UUID subunitId);
    CompletableFuture<List<SubunitMembership>> findByMemberId(UUID memberId);
    CompletableFuture<Void> deleteBySubunitId(UUID subunitId);
    CompletableFuture<Void> deleteByMemberId(UUID memberId);
    CompletableFuture<Boolean> existsBySubunitIdAndMemberId(UUID subunitId, UUID memberId);
}
