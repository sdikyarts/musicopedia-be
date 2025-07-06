package musicopedia.service;

import musicopedia.dto.request.SubunitRequestDTO;
import musicopedia.dto.response.SubunitResponseDTO;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface SubunitService {
    CompletableFuture<List<SubunitResponseDTO>> findAll();
    CompletableFuture<Optional<SubunitResponseDTO>> findById(UUID subunitId);
    CompletableFuture<SubunitResponseDTO> create(SubunitRequestDTO dto);
    CompletableFuture<SubunitResponseDTO> update(UUID subunitId, SubunitRequestDTO dto);
    CompletableFuture<Void> delete(UUID subunitId);
}
