package musicopedia.controller;

import musicopedia.dto.request.SubunitRequestDTO;
import musicopedia.dto.response.SubunitResponseDTO;
import musicopedia.service.SubunitService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SubunitControllerTest {
    @Mock
    private SubunitService subunitService;
    @InjectMocks
    private SubunitController controller;

    private UUID subunitId;
    private SubunitRequestDTO requestDTO;
    private SubunitResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        subunitId = UUID.randomUUID();
        requestDTO = new SubunitRequestDTO();
        responseDTO = new SubunitResponseDTO();
    }

    @Test
    void testGetAllSubunits() throws Exception {
        List<SubunitResponseDTO> list = List.of(responseDTO);
        when(subunitService.findAll()).thenReturn(CompletableFuture.completedFuture(list));
        CompletableFuture<ResponseEntity<List<SubunitResponseDTO>>> future = controller.getAllSubunits();
        ResponseEntity<List<SubunitResponseDTO>> response = future.get();
        assertEquals(list, response.getBody());
        assertEquals(ResponseEntity.ok().build().getStatusCode(), response.getStatusCode());
        verify(subunitService).findAll();
    }

    @Test
    void testGetSubunitById_found() throws Exception {
        when(subunitService.findById(subunitId)).thenReturn(CompletableFuture.completedFuture(Optional.of(responseDTO)));
        CompletableFuture<ResponseEntity<SubunitResponseDTO>> future = controller.getSubunitById(subunitId);
        ResponseEntity<SubunitResponseDTO> response = future.get();
        assertEquals(responseDTO, response.getBody());
        assertEquals(ResponseEntity.ok().build().getStatusCode(), response.getStatusCode());
        verify(subunitService).findById(subunitId);
    }

    @Test
    void testGetSubunitById_notFound() throws Exception {
        when(subunitService.findById(subunitId)).thenReturn(CompletableFuture.completedFuture(Optional.empty()));
        CompletableFuture<ResponseEntity<SubunitResponseDTO>> future = controller.getSubunitById(subunitId);
        ResponseEntity<SubunitResponseDTO> response = future.get();
        assertNull(response.getBody());
        assertEquals(ResponseEntity.notFound().build().getStatusCode(), response.getStatusCode());
        verify(subunitService).findById(subunitId);
    }

    @Test
    void testCreateSubunit() throws Exception {
        when(subunitService.create(requestDTO)).thenReturn(CompletableFuture.completedFuture(responseDTO));
        CompletableFuture<ResponseEntity<SubunitResponseDTO>> future = controller.createSubunit(requestDTO);
        ResponseEntity<SubunitResponseDTO> response = future.get();
        assertEquals(responseDTO, response.getBody());
        assertEquals(201, response.getStatusCode().value());
        verify(subunitService).create(requestDTO);
    }

    @Test
    void testUpdateSubunit_found() throws Exception {
        when(subunitService.update(subunitId, requestDTO)).thenReturn(CompletableFuture.completedFuture(responseDTO));
        CompletableFuture<ResponseEntity<SubunitResponseDTO>> future = controller.updateSubunit(subunitId, requestDTO);
        ResponseEntity<SubunitResponseDTO> response = future.get();
        assertEquals(responseDTO, response.getBody());
        assertEquals(ResponseEntity.ok().build().getStatusCode(), response.getStatusCode());
        verify(subunitService).update(subunitId, requestDTO);
    }

    @Test
    void testUpdateSubunit_notFound() throws Exception {
        when(subunitService.update(subunitId, requestDTO)).thenReturn(CompletableFuture.completedFuture(null));
        CompletableFuture<ResponseEntity<SubunitResponseDTO>> future = controller.updateSubunit(subunitId, requestDTO);
        ResponseEntity<SubunitResponseDTO> response = future.get();
        assertNull(response.getBody());
        assertEquals(ResponseEntity.notFound().build().getStatusCode(), response.getStatusCode());
        verify(subunitService).update(subunitId, requestDTO);
    }

    @Test
    void testDeleteSubunit() throws Exception {
        when(subunitService.delete(subunitId)).thenReturn(CompletableFuture.completedFuture(null));
        CompletableFuture<ResponseEntity<Void>> future = controller.deleteSubunit(subunitId);
        ResponseEntity<Void> response = future.get();
        assertEquals(ResponseEntity.noContent().build().getStatusCode(), response.getStatusCode());
        verify(subunitService).delete(subunitId);
    }
}
