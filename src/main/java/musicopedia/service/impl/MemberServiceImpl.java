package musicopedia.service.impl;

import musicopedia.model.Member;
import musicopedia.repository.MemberRepository;
import musicopedia.service.MemberService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@Transactional
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    @Async("memberProcessingExecutor")
    @Transactional(readOnly = true)
    public CompletableFuture<List<Member>> findAll() {
        List<Member> members = memberRepository.findAll();
        return CompletableFuture.completedFuture(members);
    }

    @Override
    @Async("memberProcessingExecutor")
    @Transactional(readOnly = true)
    public CompletableFuture<Optional<Member>> findById(UUID memberId) {
        Optional<Member> member = memberRepository.findById(memberId);
        return CompletableFuture.completedFuture(member);
    }

    @Override
    @Async("memberProcessingExecutor")
    @Transactional(readOnly = true)
    public CompletableFuture<List<Member>> findByNameContaining(String name) {
        List<Member> members = memberRepository.findByMemberNameContainingIgnoreCase(name);
        return CompletableFuture.completedFuture(members);
    }

    @Override
    @Async("memberProcessingExecutor")
    @Transactional(readOnly = true)
    public CompletableFuture<List<Member>> findByRealNameContaining(String realName) {
        List<Member> members = memberRepository.findByRealNameContainingIgnoreCase(realName);
        return CompletableFuture.completedFuture(members);
    }

    @Override
    @Async("memberProcessingExecutor")
    @Transactional(readOnly = true)
    public CompletableFuture<List<Member>> findByBirthDateBetween(LocalDate startDate, LocalDate endDate) {
        List<Member> members = memberRepository.findAll().stream()
                .filter(member -> {
                    LocalDate birthDate = member.getBirthDate();
                    return birthDate != null && 
                           !birthDate.isBefore(startDate) && 
                           !birthDate.isAfter(endDate);
                })
                .toList();
        return CompletableFuture.completedFuture(members);
    }

    @Override
    @Async("memberProcessingExecutor")
    public CompletableFuture<Member> save(Member member) {
        Member savedMember = memberRepository.save(member);
        return CompletableFuture.completedFuture(savedMember);
    }

    @Override
    @Async("memberProcessingExecutor")
    public CompletableFuture<Member> update(Member member) {
        if (memberRepository.existsById(member.getMemberId())) {
            Member updatedMember = memberRepository.save(member);
            return CompletableFuture.completedFuture(updatedMember);
        }
        return CompletableFuture.completedFuture(null);
    }

    @Override
    @Async("memberProcessingExecutor")
    public CompletableFuture<Void> deleteById(UUID memberId) {
        memberRepository.deleteById(memberId);
        return CompletableFuture.completedFuture(null);
    }

    @Override
    @Async("memberProcessingExecutor")
    @Transactional(readOnly = true)
    public CompletableFuture<Boolean> existsById(UUID memberId) {
        boolean exists = memberRepository.existsById(memberId);
        return CompletableFuture.completedFuture(exists);
    }

    @Override
    @Async("memberProcessingExecutor")
    @Transactional(readOnly = true)
    public CompletableFuture<List<Member>> findByNationality(String nationality) {
        List<Member> members = memberRepository.findByNationality(nationality);
        return CompletableFuture.completedFuture(members);
    }

    @Override
    @Async("memberProcessingExecutor")
    @Transactional(readOnly = true)
    public CompletableFuture<List<Member>> findWithSoloIdentities() {
        List<Member> members = memberRepository.findAll().stream()
                .filter(member -> member.getSoloIdentities() != null && !member.getSoloIdentities().isEmpty())
                .toList();
        return CompletableFuture.completedFuture(members);
    }
}
