package musicopedia.service.impl;

import musicopedia.dto.request.SubunitRequestDTO;
import musicopedia.dto.response.SubunitResponseDTO;
import musicopedia.mapper.SubunitMapper;
import musicopedia.model.Subunit;
import musicopedia.repository.SubunitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SubunitServiceImplTest {
    @Mock
    private SubunitRepository subunitRepository;
    @Mock
    private SubunitMapper subunitMapper;
    @InjectMocks
    private SubunitServiceImpl subunitService;

    private Subunit testSubunit;
    private SubunitRequestDTO testRequestDTO;
    private SubunitResponseDTO testResponseDTO;
    private UUID testId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        subunitService = new SubunitServiceImpl(subunitRepository, subunitMapper);
        testId = UUID.randomUUID();
        testSubunit = new Subunit();
        testSubunit.setSubunitId(testId);
        testRequestDTO = new SubunitRequestDTO();
        testRequestDTO.setMainGroupId(UUID.randomUUID());
        testRequestDTO.setSubunitName("Test Subunit");
        testResponseDTO = new SubunitResponseDTO();
        testResponseDTO.setSubunitId(testId);
        testResponseDTO.setSubunitName("Test Subunit");
    }

    @Test
    void findAll_shouldReturnList() throws Exception {
        when(subunitRepository.findAll()).thenReturn(List.of(testSubunit));
        when(subunitMapper.toResponseDTO(testSubunit)).thenReturn(CompletableFuture.completedFuture(testResponseDTO));
        List<SubunitResponseDTO> result = subunitService.findAll().get();
        assertEquals(1, result.size());
        assertEquals("Test Subunit", result.get(0).getSubunitName());
    }

    @Test
    void findById_shouldReturnOptional() throws Exception {
        when(subunitRepository.findById(testId)).thenReturn(Optional.of(testSubunit));
        when(subunitMapper.toResponseDTO(testSubunit)).thenReturn(CompletableFuture.completedFuture(testResponseDTO));
        Optional<SubunitResponseDTO> result = subunitService.findById(testId).get();
        assertTrue(result.isPresent());
        assertEquals("Test Subunit", result.get().getSubunitName());
    }

    @Test
    void findById_shouldReturnEmpty() throws Exception {
        when(subunitRepository.findById(testId)).thenReturn(Optional.empty());
        Optional<SubunitResponseDTO> result = subunitService.findById(testId).get();
        assertTrue(result.isEmpty());
    }

    @Test
    void create_shouldReturnResponse() throws Exception {
        when(subunitMapper.toEntity(any(), any(), any())).thenReturn(CompletableFuture.completedFuture(testSubunit));
        when(subunitRepository.save(any())).thenReturn(testSubunit);
        when(subunitMapper.toResponseDTO(testSubunit)).thenReturn(CompletableFuture.completedFuture(testResponseDTO));
        SubunitResponseDTO result = subunitService.create(testRequestDTO).get();
        assertEquals("Test Subunit", result.getSubunitName());
    }

    @Test
    void create_shouldThrowIfMainGroupIdNull() {
        testRequestDTO.setMainGroupId(null);
        assertThrows(IllegalArgumentException.class, () -> subunitService.create(testRequestDTO).get());
    }

    @Test
    void update_shouldReturnResponse() throws Exception {
        when(subunitRepository.findById(testId)).thenReturn(Optional.of(testSubunit));
        when(subunitMapper.toEntity(any(), any(), any())).thenReturn(CompletableFuture.completedFuture(testSubunit));
        when(subunitRepository.save(any())).thenReturn(testSubunit);
        when(subunitMapper.toResponseDTO(testSubunit)).thenReturn(CompletableFuture.completedFuture(testResponseDTO));
        SubunitResponseDTO result = subunitService.update(testId, testRequestDTO).get();
        assertEquals("Test Subunit", result.getSubunitName());
    }

    @Test
    void update_shouldReturnNullIfNotFound() throws Exception {
        when(subunitRepository.findById(testId)).thenReturn(Optional.empty());
        SubunitResponseDTO result = subunitService.update(testId, testRequestDTO).get();
        assertNull(result);
    }

    @Test
    void update_shouldThrowIfMainGroupIdNull() {
        when(subunitRepository.findById(testId)).thenReturn(Optional.of(testSubunit));
        testRequestDTO.setMainGroupId(null);
        assertThrows(IllegalArgumentException.class, () -> subunitService.update(testId, testRequestDTO).get());
    }

    @Test
    void delete_shouldCallRepository() throws Exception {
        doNothing().when(subunitRepository).deleteById(testId);
        subunitService.delete(testId).get();
        verify(subunitRepository, times(1)).deleteById(testId);
    }

    @Test
    void findById_shouldThrowRuntimeExceptionOnMapperException() throws Exception {
        when(subunitRepository.findById(testId)).thenReturn(Optional.of(testSubunit));
        CompletableFuture<SubunitResponseDTO> failedFuture = new CompletableFuture<>();
        failedFuture.completeExceptionally(new InterruptedException("fail"));
        when(subunitMapper.toResponseDTO(testSubunit)).thenReturn(failedFuture);
        assertThrows(RuntimeException.class, () -> subunitService.findById(testId).get());
    }

    @Test
    void create_shouldThrowRuntimeExceptionOnMapperException() throws Exception {
        when(subunitMapper.toEntity(any(), any(), any())).thenReturn(CompletableFuture.completedFuture(testSubunit));
        when(subunitRepository.save(any())).thenReturn(testSubunit);
        CompletableFuture<SubunitResponseDTO> failedFuture = new CompletableFuture<>();
        failedFuture.completeExceptionally(new RuntimeException("fail"));
        when(subunitMapper.toResponseDTO(testSubunit)).thenReturn(failedFuture);
        ExecutionException ex = assertThrows(ExecutionException.class, () -> subunitService.create(testRequestDTO).get());
        assertTrue(ex.getCause() instanceof RuntimeException);
    }

    @Test
    void update_shouldThrowRuntimeExceptionOnMapperException() throws Exception {
        when(subunitRepository.findById(testId)).thenReturn(Optional.of(testSubunit));
        when(subunitMapper.toEntity(any(), any(), any())).thenReturn(CompletableFuture.completedFuture(testSubunit));
        when(subunitRepository.save(any())).thenReturn(testSubunit);
        CompletableFuture<SubunitResponseDTO> failedFuture = new CompletableFuture<>();
        failedFuture.completeExceptionally(new RuntimeException("fail"));
        when(subunitMapper.toResponseDTO(testSubunit)).thenReturn(failedFuture);
        ExecutionException ex = assertThrows(ExecutionException.class, () -> subunitService.update(testId, testRequestDTO).get());
        assertTrue(ex.getCause() instanceof RuntimeException);
    }

    @Test
    void findAll_shouldHandleMapperException() throws Exception {
        when(subunitRepository.findAll()).thenReturn(List.of(testSubunit));
        CompletableFuture<SubunitResponseDTO> failedFuture = new CompletableFuture<>();
        failedFuture.completeExceptionally(new InterruptedException("fail"));
        when(subunitMapper.toResponseDTO(testSubunit)).thenReturn(failedFuture);
        assertThrows(RuntimeException.class, () -> subunitService.findAll().get());
    }

    @Test
    void create_shouldThrowRuntimeExceptionOnToEntityException() throws Exception {
        CompletableFuture<Subunit> failedFuture = new CompletableFuture<>();
        failedFuture.completeExceptionally(new InterruptedException("fail"));
        when(subunitMapper.toEntity(any(), any(), any())).thenReturn(failedFuture);
        assertThrows(RuntimeException.class, () -> subunitService.create(testRequestDTO).get());
    }

    @Test
    void update_shouldThrowRuntimeExceptionOnToEntityException() throws Exception {
        when(subunitRepository.findById(testId)).thenReturn(Optional.of(testSubunit));
        CompletableFuture<Subunit> failedFuture = new CompletableFuture<>();
        failedFuture.completeExceptionally(new ExecutionException(new Exception("fail")));
        when(subunitMapper.toEntity(any(), any(), any())).thenReturn(failedFuture);
        assertThrows(RuntimeException.class, () -> subunitService.update(testId, testRequestDTO).get());
    }
}
