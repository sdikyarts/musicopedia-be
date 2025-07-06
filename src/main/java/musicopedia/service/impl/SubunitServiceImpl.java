package musicopedia.service.impl;

import musicopedia.dto.request.SubunitRequestDTO;
import musicopedia.dto.response.SubunitResponseDTO;
import musicopedia.mapper.SubunitMapper;
import musicopedia.model.Subunit;
import musicopedia.repository.SubunitRepository;
import musicopedia.service.SubunitService;
import musicopedia.exception.SubunitServiceException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
@Transactional
public class SubunitServiceImpl implements SubunitService {
    private final SubunitRepository subunitRepository;
    private final SubunitMapper subunitMapper;

    public SubunitServiceImpl(SubunitRepository subunitRepository, SubunitMapper subunitMapper) {
        this.subunitRepository = subunitRepository;
        this.subunitMapper = subunitMapper;
    }

    @Override
    @Async
    public CompletableFuture<List<SubunitResponseDTO>> findAll() {
        List<Subunit> subunits = subunitRepository.findAll();
        List<SubunitResponseDTO> dtos = subunits.stream()
            .map(subunit -> {
                try {
                    return subunitMapper.toResponseDTO(subunit).get();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new SubunitServiceException("Interrupted while mapping Subunit", e);
                } catch (ExecutionException e) {
                    throw new SubunitServiceException("Execution error while mapping Subunit", e);
                }
            })
            .toList();
        return CompletableFuture.completedFuture(dtos);
    }

    @Override
    @Async
    public CompletableFuture<Optional<SubunitResponseDTO>> findById(UUID subunitId) {
        Optional<Subunit> subunit = subunitRepository.findById(subunitId);
        if (subunit.isEmpty()) return CompletableFuture.completedFuture(Optional.empty());
        try {
            return CompletableFuture.completedFuture(Optional.of(subunitMapper.toResponseDTO(subunit.get()).get()));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new SubunitServiceException("Interrupted while mapping Subunit", e);
        } catch (ExecutionException e) {
            throw new SubunitServiceException("Execution error while mapping Subunit", e);
        }
    }

    @Override
    @Async
    public CompletableFuture<SubunitResponseDTO> create(SubunitRequestDTO dto) {
        if (dto.getMainGroupId() == null) {
            throw new IllegalArgumentException("Main group is required");
        }
        try {
            Subunit subunit = subunitMapper.toEntity(dto, null, null).get();
            Subunit saved = subunitRepository.save(subunit);
            return subunitMapper.toResponseDTO(saved);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new SubunitServiceException("Interrupted while creating Subunit", e);
        } catch (ExecutionException e) {
            throw new SubunitServiceException("Execution error while creating Subunit", e);
        }
    }

    @Override
    @Async
    public CompletableFuture<SubunitResponseDTO> update(UUID subunitId, SubunitRequestDTO dto) {
        Optional<Subunit> existing = subunitRepository.findById(subunitId);
        if (existing.isEmpty()) {
            return CompletableFuture.completedFuture(null);
        }
        if (dto.getMainGroupId() == null) {
            throw new IllegalArgumentException("Main group is required");
        }
        try {
            Subunit subunit = subunitMapper.toEntity(dto, null, null).get();
            subunit.setSubunitId(subunitId);
            Subunit saved = subunitRepository.save(subunit);
            return subunitMapper.toResponseDTO(saved);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new SubunitServiceException("Interrupted while updating Subunit", e);
        } catch (ExecutionException e) {
            throw new SubunitServiceException("Execution error while updating Subunit", e);
        }
    }

    @Override
    @Async
    public CompletableFuture<Void> delete(UUID subunitId) {
        subunitRepository.deleteById(subunitId);
        return CompletableFuture.completedFuture(null);
    }
}
