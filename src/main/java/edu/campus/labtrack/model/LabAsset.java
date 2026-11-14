package edu.campus.labtrack.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class LabAsset {

    private Long id;

    @NotBlank(message = "Asset name is required")
    @Size(max = 100, message = "Asset name must be at most 100 characters")
    private String name;

    @NotBlank(message = "Category is required")
    @Size(max = 50, message = "Category must be at most 50 characters")
    private String category;

    @NotBlank(message = "Lab location is required")
    @Size(max = 100, message = "Lab location must be at most 100 characters")
    private String labLocation;

    @NotBlank(message = "Status is required")
    private String status;

    public LabAsset() {
    }

    public LabAsset(Long id, String name, String category, String labLocation, String status) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.labLocation = labLocation;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLabLocation() {
        return labLocation;
    }

    public void setLabLocation(String labLocation) {
        this.labLocation = labLocation;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
