package edu.campus.labtrack;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.campus.labtrack.model.LabAsset;
import edu.campus.labtrack.service.AssetService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AssetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AssetService assetService;

    @BeforeEach
    void setUp() {
        assetService.reset();
    }

    @Test
    void rootEndpointReturnsServiceInfo() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.service").value("LabTrack API"))
                .andExpect(jsonPath("$.status").value("RUNNING"));
    }

    @Test
    void healthEndpointReturnsUp() throws Exception {
        mockMvc.perform(get("/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"))
                .andExpect(jsonPath("$.service").value("labtrack-api"));
    }

    @Test
    void getAllAssetsReturnsSeededList() throws Exception {
        mockMvc.perform(get("/api/assets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(3))));
    }

    @Test
    void createAssetReturnsCreated() throws Exception {
        LabAsset asset = new LabAsset(null, "Logic Analyzer", "Electronics", "ECE Lab 102", "AVAILABLE");

        mockMvc.perform(post("/api/assets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(asset)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("Logic Analyzer"));
    }

    @Test
    void createAssetWithBlankFieldsReturnsBadRequest() throws Exception {
        LabAsset invalid = new LabAsset(null, "", "", "", "");

        mockMvc.perform(post("/api/assets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation failed"));
    }

    @Test
    void updateAssetReturnsOkWhenFoundAndNotFoundWhenMissing() throws Exception {
        LabAsset updated = new LabAsset(null, "Updated Microscope", "Biology", "BioLab 205", "AVAILABLE");

        mockMvc.perform(put("/api/assets/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Microscope"));

        mockMvc.perform(put("/api/assets/9999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteAssetReturnsOkWhenFoundAndNotFoundWhenMissing() throws Exception {
        mockMvc.perform(delete("/api/assets/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Asset deleted"));

        mockMvc.perform(delete("/api/assets/9999"))
                .andExpect(status().isNotFound());
    }
}
