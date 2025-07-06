package musicopedia.controller;

import musicopedia.model.membership.SubunitMembership;
import musicopedia.service.SubunitMembershipService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SubunitMembershipControllerTest {
    @Mock
    private SubunitMembershipService subunitMembershipService;
    @InjectMocks
    private SubunitMembershipController controller;

    private UUID subunitId;
    private UUID memberId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        subunitId = UUID.randomUUID();
        memberId = UUID.randomUUID();
    }

    @Test
    void testGetBySubunit_async() throws Exception {
        List<SubunitMembership> memberships = List.of(mock(SubunitMembership.class));
        when(subunitMembershipService.findBySubunitId(subunitId)).thenReturn(CompletableFuture.completedFuture(memberships));
        CompletableFuture<ResponseEntity<List<SubunitMembership>>> future = controller.getBySubunit(subunitId);
        ResponseEntity<List<SubunitMembership>> response = future.get();
        assertEquals(memberships, response.getBody());
        assertEquals(ResponseEntity.ok().build().getStatusCode(), response.getStatusCode());
        verify(subunitMembershipService).findBySubunitId(subunitId);
    }

    @Test
    void testGetByMember_async() throws Exception {
        List<SubunitMembership> memberships = List.of(mock(SubunitMembership.class));
        when(subunitMembershipService.findByMemberId(memberId)).thenReturn(CompletableFuture.completedFuture(memberships));
        CompletableFuture<ResponseEntity<List<SubunitMembership>>> future = controller.getByMember(memberId);
        ResponseEntity<List<SubunitMembership>> response = future.get();
        assertEquals(memberships, response.getBody());
        assertEquals(ResponseEntity.ok().build().getStatusCode(), response.getStatusCode());
        verify(subunitMembershipService).findByMemberId(memberId);
    }

    @Test
    void testDeleteBySubunit_async() throws Exception {
        when(subunitMembershipService.deleteBySubunitId(subunitId)).thenReturn(CompletableFuture.completedFuture(null));
        CompletableFuture<ResponseEntity<Void>> future = controller.deleteBySubunit(subunitId);
        ResponseEntity<Void> response = future.get();
        assertEquals(ResponseEntity.noContent().build().getStatusCode(), response.getStatusCode());
        verify(subunitMembershipService).deleteBySubunitId(subunitId);
    }

    @Test
    void testDeleteByMember_async() throws Exception {
        when(subunitMembershipService.deleteByMemberId(memberId)).thenReturn(CompletableFuture.completedFuture(null));
        CompletableFuture<ResponseEntity<Void>> future = controller.deleteByMember(memberId);
        ResponseEntity<Void> response = future.get();
        assertEquals(ResponseEntity.noContent().build().getStatusCode(), response.getStatusCode());
        verify(subunitMembershipService).deleteByMemberId(memberId);
    }

    @Test
    void testExists_async() throws Exception {
        when(subunitMembershipService.existsBySubunitIdAndMemberId(subunitId, memberId)).thenReturn(CompletableFuture.completedFuture(true));
        CompletableFuture<ResponseEntity<Boolean>> future = controller.exists(subunitId, memberId);
        ResponseEntity<Boolean> response = future.get();
        assertTrue(response.getBody());
        assertEquals(ResponseEntity.ok().build().getStatusCode(), response.getStatusCode());
        verify(subunitMembershipService).existsBySubunitIdAndMemberId(subunitId, memberId);
    }
}
