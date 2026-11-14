package edu.campus.labtrack.controller;

import edu.campus.labtrack.model.LabAsset;
import edu.campus.labtrack.service.AssetService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
public class AssetController {

    private final AssetService assetService;

    public AssetController(AssetService assetService) {
        this.assetService = assetService;
    }

    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> root() {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("service", "LabTrack API");
        body.put("description", "Campus Laboratory Asset Management System");
        body.put("version", "1.0.0");
        body.put("status", "RUNNING");
        return ResponseEntity.ok(body);
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> body = new LinkedHashMap<>();
        body.put("status", "UP");
        body.put("service", "labtrack-api");
        return ResponseEntity.ok(body);
    }

    @GetMapping("/api/assets")
    public ResponseEntity<List<LabAsset>> getAllAssets() {
        return ResponseEntity.ok(assetService.findAll());
    }

    @PostMapping("/api/assets")
    public ResponseEntity<LabAsset> createAsset(@Valid @RequestBody LabAsset asset) {
        LabAsset created = assetService.create(asset);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/api/assets/{id}")
    public ResponseEntity<?> updateAsset(@PathVariable Long id, @Valid @RequestBody LabAsset asset) {
        return assetService.update(id, asset)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Asset not found", "id", id)));
    }

    @DeleteMapping("/api/assets/{id}")
    public ResponseEntity<Map<String, Object>> deleteAsset(@PathVariable Long id) {
        if (assetService.delete(id)) {
            return ResponseEntity.ok(Map.of("message", "Asset deleted", "id", id));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "Asset not found", "id", id));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> fieldErrors = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(err -> fieldErrors.put(err.getField(), err.getDefaultMessage()));
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("error", "Validation failed");
        response.put("details", fieldErrors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
