package musicopedia.service.impl;

import musicopedia.model.membership.SubunitMembership;
import musicopedia.repository.SubunitMembershipRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SubunitMembershipServiceImplTest {
    @Mock
    private SubunitMembershipRepository repository;

    @InjectMocks
    private SubunitMembershipServiceImpl service;

    private UUID subunitId;
    private UUID memberId;
    private SubunitMembership membership;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        subunitId = UUID.randomUUID();
        memberId = UUID.randomUUID();
        membership = new SubunitMembership();
    }

    @Test
    void findBySubunitId_returnsMemberships() throws Exception {
        List<SubunitMembership> memberships = Arrays.asList(membership);
        when(repository.findBySubunit_SubunitId(subunitId)).thenReturn(memberships);
        CompletableFuture<List<SubunitMembership>> future = service.findBySubunitId(subunitId);
        assertEquals(memberships, future.get());
        verify(repository).findBySubunit_SubunitId(subunitId);
    }

    @Test
    void findByMemberId_returnsMemberships() throws Exception {
        List<SubunitMembership> memberships = Arrays.asList(membership);
        when(repository.findByMember_MemberId(memberId)).thenReturn(memberships);
        CompletableFuture<List<SubunitMembership>> future = service.findByMemberId(memberId);
        assertEquals(memberships, future.get());
        verify(repository).findByMember_MemberId(memberId);
    }

    @Test
    void deleteBySubunitId_deletes() throws Exception {
        doNothing().when(repository).deleteBySubunit_SubunitId(subunitId);
        CompletableFuture<Void> future = service.deleteBySubunitId(subunitId);
        assertNull(future.get());
        verify(repository).deleteBySubunit_SubunitId(subunitId);
    }

    @Test
    void deleteByMemberId_deletes() throws Exception {
        doNothing().when(repository).deleteByMember_MemberId(memberId);
        CompletableFuture<Void> future = service.deleteByMemberId(memberId);
        assertNull(future.get());
        verify(repository).deleteByMember_MemberId(memberId);
    }

    @Test
    void existsBySubunitIdAndMemberId_returnsBoolean() throws Exception {
        when(repository.existsBySubunit_SubunitIdAndMember_MemberId(subunitId, memberId)).thenReturn(true);
        CompletableFuture<Boolean> future = service.existsBySubunitIdAndMemberId(subunitId, memberId);
        assertTrue(future.get());
        verify(repository).existsBySubunit_SubunitIdAndMember_MemberId(subunitId, memberId);
    }
}
