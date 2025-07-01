package musicopedia.service.impl;

import musicopedia.model.Member;
import musicopedia.repository.MemberRepository;
import musicopedia.service.MemberService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Member> findById(UUID memberId) {
        return memberRepository.findById(memberId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Member> findByNameContaining(String name) {
        return memberRepository.findByFullNameContainingIgnoreCase(name);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Member> findByBirthDateBetween(LocalDate startDate, LocalDate endDate) {
        return memberRepository.findAll().stream()
                .filter(member -> {
                    LocalDate birthDate = member.getBirthDate();
                    return birthDate != null && 
                           !birthDate.isBefore(startDate) && 
                           !birthDate.isAfter(endDate);
                })
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Member> findBySoloArtistNotNull() {
        return memberRepository.findAll().stream()
                .filter(member -> member.getSoloArtist() != null)
                .toList();
    }

    @Override
    public Member save(Member member) {
        return memberRepository.save(member);
    }

    @Override
    public Member update(Member member) {
        if (memberRepository.existsById(member.getMemberId())) {
            return memberRepository.save(member);
        }
        return null;
    }

    @Override
    public void deleteById(UUID memberId) {
        memberRepository.deleteById(memberId);
    }
}
