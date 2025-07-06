package musicopedia.controller;

import musicopedia.dto.request.SubunitRequestDTO;
import musicopedia.dto.response.SubunitResponseDTO;
import musicopedia.service.SubunitService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/subunits")
public class SubunitController {
    private final SubunitService subunitService;

    public SubunitController(SubunitService subunitService) {
        this.subunitService = subunitService;
    }

    @GetMapping
    public CompletableFuture<ResponseEntity<List<SubunitResponseDTO>>> getAllSubunits() {
        return subunitService.findAll()
                .thenApply(ResponseEntity::ok);
    }

    @GetMapping("/{id}")
    public CompletableFuture<ResponseEntity<SubunitResponseDTO>> getSubunitById(@PathVariable("id") UUID subunitId) {
        return subunitService.findById(subunitId)
                .thenApply(opt -> opt.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build()));
    }

    @PostMapping
    public CompletableFuture<ResponseEntity<SubunitResponseDTO>> createSubunit(@RequestBody SubunitRequestDTO dto) {
        return subunitService.create(dto)
                .thenApply(saved -> ResponseEntity.status(201).body(saved));
    }

    @PutMapping("/{id}")
    public CompletableFuture<ResponseEntity<SubunitResponseDTO>> updateSubunit(@PathVariable("id") UUID subunitId, @RequestBody SubunitRequestDTO dto) {
        return subunitService.update(subunitId, dto)
                .thenApply(updated -> updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public CompletableFuture<ResponseEntity<Void>> deleteSubunit(@PathVariable("id") UUID subunitId) {
        return subunitService.delete(subunitId)
                .thenApply(v -> ResponseEntity.noContent().build());
    }
}
