package musicopedia.service;

import musicopedia.builder.ArtistBuilder;
import musicopedia.builder.MemberBuilder;
import musicopedia.model.Artist;
import musicopedia.model.Member;
import musicopedia.model.enums.ArtistType;
import musicopedia.repository.MemberRepository;
import musicopedia.service.config.ServiceTestConfig;
import musicopedia.service.impl.MemberServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Import(ServiceTestConfig.class)
public class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    private MemberService memberService;

    private UUID testId;
    private Member testMember;
    private Artist testSoloArtist;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        memberService = new MemberServiceImpl(memberRepository);

        testId = UUID.randomUUID();
        testSoloArtist = new ArtistBuilder()
            .setArtistName("IU")
            .setType(ArtistType.SOLO)
            .build();
        testSoloArtist.setArtistId(UUID.randomUUID());

        testMember = new MemberBuilder()
            .setMemberName("Lee Ji-eun")
            .setRealName("Lee Ji-eun")
            .setDescription("A talented South Korean singer-songwriter and actress")
            .setBirthDate(LocalDate.of(1993, 5, 16))
            .setSoloArtist(testSoloArtist)
            .build();
        testMember.setMemberId(testId);
    }

    @Test
    void testFindAll() {
        Member member1 = createMember("Felix", "Felix Yongbok Lee", LocalDate.of(2000, 9, 15));
        Member member2 = createMember("Hyunjin", "Hwang Hyun-jin", LocalDate.of(2000, 3, 20));
        List<Member> members = Arrays.asList(member1, member2);
        when(memberRepository.findAll()).thenReturn(members);
        CompletableFuture<List<Member>> future = memberService.findAll();
        List<Member> result = future.join();
        assertEquals(2, result.size());
        assertEquals("Felix", result.get(0).getMemberName());
        assertEquals("Felix Yongbok Lee", result.get(0).getRealName());
        assertEquals("Hyunjin", result.get(1).getMemberName());
        assertEquals("Hwang Hyun-jin", result.get(1).getRealName());
        verify(memberRepository, times(1)).findAll();
    }

    @Test
    void testFindById() {
        when(memberRepository.findById(testId)).thenReturn(Optional.of(testMember));
        CompletableFuture<Optional<Member>> future = memberService.findById(testId);
        Optional<Member> result = future.join();
        assertTrue(result.isPresent());
        assertEquals("Lee Ji-eun", result.get().getMemberName());
        assertEquals("Lee Ji-eun", result.get().getRealName());
        verify(memberRepository, times(1)).findById(testId);
    }

    @Test
    void testFindByNameContaining() {
        Member member1 = createMember("HAN", "Han Ji-sung", LocalDate.of(2000, 9, 14));
        Member member2 = createMember("KIMCHAEWON", "Kim Chae-won", LocalDate.of(2000, 8, 1));
        List<Member> members = Arrays.asList(member1, member2);
        when(memberRepository.findByMemberNameContainingIgnoreCase("HAN")).thenReturn(members);
        CompletableFuture<List<Member>> future = memberService.findByNameContaining("HAN");
        List<Member> result = future.join();
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(member -> member.getMemberName().equals("HAN") && member.getRealName().equals("Han Ji-sung")));
        assertTrue(result.stream().anyMatch(member -> member.getMemberName().equals("KIMCHAEWON") && member.getRealName().equals("Kim Chae-won")));
        verify(memberRepository, times(1)).findByMemberNameContainingIgnoreCase("HAN");
    }

    @Test
    void testFindByRealNameContaining() {
        Member member1 = createMember("Felix", "Felix Yongbok Lee", LocalDate.of(2000, 9, 15));
        Member member2 = createMember("Hyunjin", "Hwang Hyun-jin", LocalDate.of(2000, 3, 20));
        List<Member> members = Arrays.asList(member1, member2);
        when(memberRepository.findByRealNameContainingIgnoreCase("Lee")).thenReturn(members);
        CompletableFuture<List<Member>> future = memberService.findByRealNameContaining("Lee");
        List<Member> result = future.join();
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(member -> member.getMemberName().equals("Felix") && member.getRealName().equals("Felix Yongbok Lee")));
        assertTrue(result.stream().anyMatch(member -> member.getMemberName().equals("Hyunjin") && member.getRealName().equals("Hwang Hyun-jin")));
        verify(memberRepository, times(1)).findByRealNameContainingIgnoreCase("Lee");
    }

    @Test
    void testFindByBirthDateBetween() {
        Member member1 = createMember("Member 1", "Real 1", LocalDate.of(1990, 1, 1));
        Member member2 = createMember("Member 2", "Real 2", LocalDate.of(1995, 5, 5));
        Member member3 = createMember("Member 3", "Real 3", LocalDate.of(2000, 10, 10));
        Member memberWithNullBirthDate = createMember("Member Null", "Real Null", null);
        List<Member> members = Arrays.asList(member1, member2, member3, memberWithNullBirthDate);
        when(memberRepository.findAll()).thenReturn(members);
        CompletableFuture<List<Member>> future = memberService.findByBirthDateBetween(
            LocalDate.of(1994, 1, 1), 
            LocalDate.of(1996, 12, 31)
        );
        List<Member> result = future.join();
        assertEquals(1, result.size());
        assertEquals("Member 2", result.get(0).getMemberName());
    }

    @Test
    void testFindBySoloArtistNotNull() {
        Member memberWithSolo = createMember("HAN", "Han Ji-sung", LocalDate.of(2000, 9, 14));
        memberWithSolo.setSoloArtist(testSoloArtist);
        Member memberWithoutSolo = createMember("KIMCHAEWON", "Kim Chae-won", LocalDate.of(2000, 8, 1));
        List<Member> members = Arrays.asList(memberWithSolo, memberWithoutSolo);
        when(memberRepository.findAll()).thenReturn(members);
        CompletableFuture<List<Member>> future = memberService.findBySoloArtistNotNull();
        List<Member> result = future.join();
        assertEquals(1, result.size());
        assertEquals("HAN", result.get(0).getMemberName());
        assertEquals("Han Ji-sung", result.get(0).getRealName());
    }

    @Test
    void testSave() {
        testMember.setMemberName("Felix");
        testMember.setRealName("Felix Yongbok Lee");
        when(memberRepository.save(any(Member.class))).thenReturn(testMember);
        CompletableFuture<Member> future = memberService.save(testMember);
        Member savedMember = future.join();
        assertEquals(testId, savedMember.getMemberId());
        assertEquals("Felix", savedMember.getMemberName());
        assertEquals("Felix Yongbok Lee", savedMember.getRealName());
        verify(memberRepository, times(1)).save(testMember);
    }

    @Test
    void testUpdate() {
        when(memberRepository.existsById(testId)).thenReturn(true);
        when(memberRepository.save(testMember)).thenReturn(testMember);
        testMember.setMemberName("Hyunjin");
        testMember.setRealName("Hwang Hyun-jin");
        CompletableFuture<Member> future = memberService.update(testMember);
        Member updatedMember = future.join();
        assertNotNull(updatedMember);
        assertEquals("Hyunjin", updatedMember.getMemberName());
        assertEquals("Hwang Hyun-jin", updatedMember.getRealName());
        verify(memberRepository, times(1)).existsById(testId);
        verify(memberRepository, times(1)).save(testMember);
    }

    @Test
    void testUpdateNonExistent() {
        when(memberRepository.existsById(testId)).thenReturn(false);

        CompletableFuture<Member> future = memberService.update(testMember);
        Member updatedMember = future.join();

        assertNull(updatedMember);
        verify(memberRepository, times(1)).existsById(testId);
        verify(memberRepository, never()).save(any(Member.class));
    }

    @Test
    void testDeleteById() {
        doNothing().when(memberRepository).deleteById(testId);

        CompletableFuture<Void> future = memberService.deleteById(testId);
        future.join();

        verify(memberRepository, times(1)).deleteById(testId);
    }

    @Test
    void testExistsById() {
        when(memberRepository.existsById(testId)).thenReturn(true);
        
        CompletableFuture<Boolean> future = memberService.existsById(testId);
        Boolean exists = future.join();
        
        assertTrue(exists);
        verify(memberRepository, times(1)).existsById(testId);
    }

    @Test
    void testFindByNationality() {
        Member member1 = createMember("Felix", "Felix Yongbok Lee", LocalDate.of(2000, 9, 15));
        member1.setNationality("AU");
        Member member2 = createMember("Hyunjin", "Hwang Hyun-jin", LocalDate.of(2000, 3, 20));
        member2.setNationality("KR");
        when(memberRepository.findByNationality("AU")).thenReturn(Arrays.asList(member1));
        CompletableFuture<List<Member>> future = memberService.findByNationality("AU");
        List<Member> result = future.join();
        assertEquals(1, result.size());
        assertEquals("Felix", result.get(0).getMemberName());
        assertEquals("AU", result.get(0).getNationality());
        verify(memberRepository, times(1)).findByNationality("AU");
    }



    private Member createMember(String memberName, String realName, LocalDate birthDate) {
        return new MemberBuilder()
            .setMemberName(memberName)
            .setRealName(realName)
            .setBirthDate(birthDate)
            .build();
    }
}
