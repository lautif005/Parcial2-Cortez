package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.DnaRequest;
import org.example.dto.StatsResponse;
import org.example.service.MutantService;
import org.example.service.StatsService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MutantController.class)
class MutantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MutantService mutantService;

    @MockBean
    private StatsService statsService;

    private final String[] mutantDna = {"ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG"};
    private final String[] humanDna = {"ATGCGA", "CAGTGC", "TTATTT", "AGACGG", "GCGTCA", "TCACTG"};

    @Test
    @DisplayName("POST /mutant -> 200 OK cuando es Mutante")
    void testCheckMutant_ReturnOk_WhenIsMutant() throws Exception {
        when(mutantService.isMutant(any())).thenReturn(true);
        DnaRequest request = new DnaRequest();
        request.setDna(mutantDna);

        mockMvc.perform(post("/mutant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /mutant -> 403 Forbidden cuando es Humano")
    void testCheckMutant_ReturnForbidden_WhenIsHuman() throws Exception {
        when(mutantService.isMutant(any())).thenReturn(false);
        DnaRequest request = new DnaRequest();
        request.setDna(humanDna);

        mockMvc.perform(post("/mutant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("POST /mutant -> 400 Bad Request por DNA nulo (Validación @NonNull)")
    void testCheckMutant_ReturnBadRequest_ForNullDna() throws Exception {
        String invalidJson = "{\"dna\": null}";

        mockMvc.perform(post("/mutant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("POST /mutant -> 400 Bad Request por DNA inválido (Custom Validation)")
    void testCheckMutant_ReturnBadRequest_ForInvalidCharacters() throws Exception {
        String[] invalidDna = {"ATGC", "CAGT", "TTA", "AGAC"};
        DnaRequest request = new DnaRequest();
        request.setDna(invalidDna);

        mockMvc.perform(post("/mutant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("POST /mutant -> 400 Bad Request por matriz 3x3 (Validación tamaño mínimo)")
    void testCheckMutant_ReturnBadRequest_ForTooSmallMatrix() throws Exception {
        String[] tooSmallDna = {"ATG", "CAG", "TTA"};
        DnaRequest request = new DnaRequest();
        request.setDna(tooSmallDna);

        mockMvc.perform(post("/mutant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("GET /stats -> 200 OK y JSON correcto")
    void testGetStats_ReturnOk_AndCorrectJson() throws Exception {
        StatsResponse mockResponse = StatsResponse.builder()
                .countMutantDna(40L)
                .countHumanDna(100L)
                .ratio(0.4)
                .build();
        when(statsService.getStats()).thenReturn(mockResponse);

        mockMvc.perform(get("/stats")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.countMutantDna").value(40L))
                .andExpect(jsonPath("$.countHumanDna").value(100L))
                .andExpect(jsonPath("$.ratio").value(0.4));
    }

    @Test
    @DisplayName("GET /stats -> 200 OK con datos vacíos")
    void testGetStats_ReturnOk_WithNoData() throws Exception {
        StatsResponse mockResponse = StatsResponse.builder()
                .countMutantDna(0L)
                .countHumanDna(0L)
                .ratio(0.0)
                .build();
        when(statsService.getStats()).thenReturn(mockResponse);

        mockMvc.perform(get("/stats")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ratio").value(0.0));
    }

    @Test
    @DisplayName("POST /mutant -> 415 Unsupported Media Type si no es JSON")
    void testCheckMutant_ReturnUnsupportedMediaType() throws Exception {
        mockMvc.perform(post("/mutant")
                        .contentType(MediaType.TEXT_PLAIN)
                        .content("Some content"))
                .andExpect(status().isUnsupportedMediaType());
    }
}