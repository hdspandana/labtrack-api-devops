package edu.campus.labtrack.service;

import edu.campus.labtrack.model.LabAsset;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class AssetService {

    private final Map<Long, LabAsset> assets = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(0);

    public AssetService() {
        reset();
    }

    public void reset() {
        assets.clear();
        idGenerator.set(0);
        create(new LabAsset(null, "Digital Oscilloscope", "Electronics", "ECE Lab 101", "AVAILABLE"));
        create(new LabAsset(null, "3D Printer Prusa MK4", "Fabrication", "MakerSpace B2", "IN_USE"));
        create(new LabAsset(null, "Optical Microscope", "Biology", "BioLab 204", "MAINTENANCE"));
    }

    public List<LabAsset> findAll() {
        return new ArrayList<>(assets.values());
    }

    public Optional<LabAsset> findById(Long id) {
        return Optional.ofNullable(assets.get(id));
    }

    public LabAsset create(LabAsset asset) {
        long id = idGenerator.incrementAndGet();
        LabAsset saved = new LabAsset(id, asset.getName(), asset.getCategory(), asset.getLabLocation(), asset.getStatus());
        assets.put(id, saved);
        return saved;
    }

    public Optional<LabAsset> update(Long id, LabAsset updated) {
        if (!assets.containsKey(id)) {
            return Optional.empty();
        }
        LabAsset saved = new LabAsset(id, updated.getName(), updated.getCategory(), updated.getLabLocation(), updated.getStatus());
        assets.put(id, saved);
        return Optional.of(saved);
    }

    public boolean delete(Long id) {
        return assets.remove(id) != null;
    }
}
