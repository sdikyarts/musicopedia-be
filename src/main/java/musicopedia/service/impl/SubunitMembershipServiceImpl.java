package musicopedia.service.impl;

import musicopedia.model.membership.SubunitMembership;
import musicopedia.repository.SubunitMembershipRepository;
import musicopedia.service.SubunitMembershipService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@Transactional
public class SubunitMembershipServiceImpl implements SubunitMembershipService {
    private final SubunitMembershipRepository repository;

    public SubunitMembershipServiceImpl(SubunitMembershipRepository repository) {
        this.repository = repository;
    }

    @Override
    @Async
    public CompletableFuture<List<SubunitMembership>> findBySubunitId(UUID subunitId) {
        return CompletableFuture.completedFuture(repository.findBySubunitId(subunitId));
    }

    @Override
    @Async
    public CompletableFuture<List<SubunitMembership>> findByMemberId(UUID memberId) {
        return CompletableFuture.completedFuture(repository.findByMemberId(memberId));
    }

    @Override
    @Async
    public CompletableFuture<Void> deleteBySubunitId(UUID subunitId) {
        repository.deleteBySubunitId(subunitId);
        return CompletableFuture.completedFuture(null);
    }

    @Override
    @Async
    public CompletableFuture<Void> deleteByMemberId(UUID memberId) {
        repository.deleteByMemberId(memberId);
        return CompletableFuture.completedFuture(null);
    }

    @Override
    @Async
    public CompletableFuture<Boolean> existsBySubunitIdAndMemberId(UUID subunitId, UUID memberId) {
        return CompletableFuture.completedFuture(repository.existsBySubunitIdAndMemberId(subunitId, memberId));
    }
}
