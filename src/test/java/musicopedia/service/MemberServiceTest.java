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
            .setFullName("Lee Ji-eun")
            .setDescription("A talented South Korean singer-songwriter and actress")
            .setBirthDate(LocalDate.of(1993, 5, 16))
            .setSoloArtist(testSoloArtist)
            .build();
        testMember.setMemberId(testId);
    }

    @Test
    void testFindAll() {
        Member member1 = createMember("Member 1", LocalDate.of(1990, 1, 1));
        Member member2 = createMember("Member 2", LocalDate.of(1995, 5, 5));
        
        List<Member> members = Arrays.asList(member1, member2);
        
        when(memberRepository.findAll()).thenReturn(members);

        CompletableFuture<List<Member>> future = memberService.findAll();
        List<Member> result = future.join();

        assertEquals(2, result.size());
        assertEquals("Member 1", result.get(0).getFullName());
        assertEquals("Member 2", result.get(1).getFullName());
        verify(memberRepository, times(1)).findAll();
    }

    @Test
    void testFindById() {
        when(memberRepository.findById(testId)).thenReturn(Optional.of(testMember));

        CompletableFuture<Optional<Member>> future = memberService.findById(testId);
        Optional<Member> result = future.join();

        assertTrue(result.isPresent());
        assertEquals("Lee Ji-eun", result.get().getFullName());
        verify(memberRepository, times(1)).findById(testId);
    }

    @Test
    void testFindByNameContaining() {
        Member member1 = createMember("Kim Namjoon", LocalDate.of(1994, 9, 12));
        Member member2 = createMember("Kim Taehyung", LocalDate.of(1995, 12, 30));
        
        List<Member> kimMembers = Arrays.asList(member1, member2);
        
        when(memberRepository.findByFullNameContainingIgnoreCase("Kim")).thenReturn(kimMembers);

        CompletableFuture<List<Member>> future = memberService.findByNameContaining("Kim");
        List<Member> result = future.join();

        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(member -> member.getFullName().equals("Kim Namjoon")));
        assertTrue(result.stream().anyMatch(member -> member.getFullName().equals("Kim Taehyung")));
        verify(memberRepository, times(1)).findByFullNameContainingIgnoreCase("Kim");
    }

    @Test
    void testFindByBirthDateBetween() {
        Member member1 = createMember("Member 1", LocalDate.of(1990, 1, 1));
        Member member2 = createMember("Member 2", LocalDate.of(1995, 5, 5));
        Member member3 = createMember("Member 3", LocalDate.of(2000, 10, 10));
        Member memberWithNullBirthDate = createMember("Member Null", null);
        
        List<Member> members = Arrays.asList(member1, member2, member3, memberWithNullBirthDate);
        
        when(memberRepository.findAll()).thenReturn(members);

        CompletableFuture<List<Member>> future = memberService.findByBirthDateBetween(
            LocalDate.of(1994, 1, 1), 
            LocalDate.of(1996, 12, 31)
        );
        List<Member> result = future.join();

        assertEquals(1, result.size());
        assertEquals("Member 2", result.get(0).getFullName());
    }

    @Test
    void testFindBySoloArtistNotNull() {
        Member memberWithSolo = createMember("With Solo", LocalDate.of(1990, 1, 1));
        memberWithSolo.setSoloArtist(testSoloArtist);
        
        Member memberWithoutSolo = createMember("No Solo", LocalDate.of(1995, 5, 5));
        
        List<Member> members = Arrays.asList(memberWithSolo, memberWithoutSolo);
        
        when(memberRepository.findAll()).thenReturn(members);

        CompletableFuture<List<Member>> future = memberService.findBySoloArtistNotNull();
        List<Member> result = future.join();

        assertEquals(1, result.size());
        assertEquals("With Solo", result.get(0).getFullName());
    }

    @Test
    void testSave() {
        when(memberRepository.save(any(Member.class))).thenReturn(testMember);

        CompletableFuture<Member> future = memberService.save(testMember);
        Member savedMember = future.join();

        assertEquals(testId, savedMember.getMemberId());
        assertEquals("Lee Ji-eun", savedMember.getFullName());
        verify(memberRepository, times(1)).save(testMember);
    }

    @Test
    void testUpdate() {
        when(memberRepository.existsById(testId)).thenReturn(true);
        when(memberRepository.save(testMember)).thenReturn(testMember);

        testMember.setFullName("Lee Ji-eun (Updated)");
        CompletableFuture<Member> future = memberService.update(testMember);
        Member updatedMember = future.join();

        assertNotNull(updatedMember);
        assertEquals("Lee Ji-eun (Updated)", updatedMember.getFullName());
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



    private Member createMember(String name, LocalDate birthDate) {
        return new MemberBuilder()
            .setFullName(name)
            .setBirthDate(birthDate)
            .build();
    }
}
