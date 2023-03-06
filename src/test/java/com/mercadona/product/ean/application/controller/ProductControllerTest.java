package com.mercadona.product.ean.application.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadona.product.ean.application.controller.dto.CreateProductRequestDTO;
import com.mercadona.product.ean.configuration.TestSecurityConfig;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.jdbc.SqlMergeMode;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.StreamUtils;

import java.nio.charset.Charset;

import static org.springframework.test.context.jdbc.SqlMergeMode.MergeMode.MERGE;
import static org.springframework.test.context.jdbc.SqlMergeMode.MergeMode.OVERRIDE;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = TestSecurityConfig.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@SqlGroup({
        @Sql(scripts = { "/sql/loadTestData.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(scripts = { "/sql/cleanTestData.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Value("classpath:json/responses/product1.json")
    Resource product1;

    @Value("classpath:json/responses/product-not-found.json")
    Resource productNotFound;

    @Value("classpath:json/responses/invalid-product-code-and-provider-code.json")
    Resource invalidProductCodeAndProviderCode;

    @Value("classpath:json/responses/product-already-exists.json")
    Resource productAlreadyExists;

    @Value("classpath:json/responses/invalid-product-ean.json")
    Resource invalidProductEan;

    @Value("classpath:json/responses/product1-updated.json")
    Resource product1Updated;

    @Value("classpath:json/responses/product1-created.json")
    Resource product1Created;

    @Value("classpath:json/responses/inconsistent-ean.json")
    Resource inconsistentEan;

    private static ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll() {
        objectMapper = new ObjectMapper();
    }

    @Test

    void givenExistingProduct_whenGetProductsById_thenReturnsOkAndProduct() throws Exception {
        String response = StreamUtils.copyToString(product1.getInputStream(), Charset.defaultCharset());
        mockMvc.perform(MockMvcRequestBuilders.get("/product/ean/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void givenNonExistingProduct_whenGetProductsById_thenReturnsNotFoundAndWarning() throws Exception {
        String response = StreamUtils.copyToString(productNotFound.getInputStream(), Charset.defaultCharset());
        mockMvc.perform(MockMvcRequestBuilders.get("/product/ean/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().json(response));
    }

    @Test
    void givenInvalidPathParam_whenGetProductsById_thenReturnsBadRequestAndError() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/product/ean/abc")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isArray());
    }

    @Test
    void givenExistingProduct_whenGetProductsByProductCode_thenReturnsOkAndProduct() throws Exception {

        String response = StreamUtils.copyToString(product1.getInputStream(), Charset.defaultCharset());

        mockMvc.perform(MockMvcRequestBuilders.get("/product/ean/find")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("productCode", "45905")
                        .param("destinationCode", "1")
                        .param("providerCode", "8437008"))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void givenNonExistingProduct_whenGetProductByCodes_thenReturnsNotFoundAndWarning() throws Exception {

        String response = StreamUtils.copyToString(productNotFound.getInputStream(), Charset.defaultCharset());

        mockMvc.perform(MockMvcRequestBuilders.get("/product/ean/find")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("productCode", "45901")
                        .param("destinationCode", "1")
                        .param("providerCode", "8437008"))
                .andExpect(status().isNotFound())
                .andExpect(content().json(response));
    }

    @Test
    void givenInvalidRequest_whenGetProductByCodes_thenReturnsBadRequestAndError() throws Exception {

        String response = StreamUtils.copyToString(invalidProductCodeAndProviderCode.getInputStream(), Charset.defaultCharset());

        mockMvc.perform(MockMvcRequestBuilders.get("/product/ean/find")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("productCode", "1")
                        .param("destinationCode", "1")
                        .param("providerCode", "1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(response));
    }

    @Test
    void givenExistingProductEan_whenSave_thenReturnsConflictAndError() throws Exception {

        String request = objectMapper.writeValueAsString(new CreateProductRequestDTO("8437008459051"));

        String response = StreamUtils.copyToString(productAlreadyExists.getInputStream(), Charset.defaultCharset());

        mockMvc.perform(MockMvcRequestBuilders.put("/product/ean")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isConflict())
                .andExpect(content().json(response));
    }

    @Test
    @Sql("/sql/cleanTestData.sql")
    void givenNonExistingProductEan_whenSave_thenReturnsCreatedAndProduct() throws Exception {

        String request = objectMapper.writeValueAsString(new CreateProductRequestDTO("8437008459051"));

        String response = StreamUtils.copyToString(product1Created.getInputStream(), Charset.defaultCharset());

        mockMvc.perform(MockMvcRequestBuilders.put("/product/ean")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated())
                .andExpect(content().json(response));
    }

    @Test
    void givenInvalidRequest_whenSave_thenReturnsBadRequestAndError() throws Exception {

        String request = objectMapper.writeValueAsString(new CreateProductRequestDTO("8437008459a"));

        String response = StreamUtils.copyToString(invalidProductEan.getInputStream(), Charset.defaultCharset());

        mockMvc.perform(MockMvcRequestBuilders.put("/product/ean")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(response));
    }

    @Test
    void givenExistingProductEan_whenUpdate_thenReturnsOkAndUpdated() throws Exception {

        String request = objectMapper.writeValueAsString(new CreateProductRequestDTO("8437008459050"));

        String response = StreamUtils.copyToString(product1Updated.getInputStream(), Charset.defaultCharset());

        mockMvc.perform(MockMvcRequestBuilders.post("/product/ean/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    @Sql("/sql/cleanTestData.sql")
    void givenNonExistingProductEan_whenUpdate_thenReturnsNotFoundAndError() throws Exception {

        String request = objectMapper.writeValueAsString(new CreateProductRequestDTO("8437008459051"));

        String response = StreamUtils.copyToString(productNotFound.getInputStream(), Charset.defaultCharset());

        mockMvc.perform(MockMvcRequestBuilders.post("/product/ean/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isNotFound())
                .andExpect(content().json(response));
    }

    @Test
    void givenInvalidRequest_whenUpdate_thenReturnsBadRequestAndError() throws Exception {

        String request = objectMapper.writeValueAsString(new CreateProductRequestDTO("8437008459a"));

        String response = StreamUtils.copyToString(invalidProductEan.getInputStream(), Charset.defaultCharset());

        mockMvc.perform(MockMvcRequestBuilders.post("/product/ean/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(response));
    }

    @Test
    @Disabled
    @Sql(scripts = "/sql/alterDestinationCode.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @SqlMergeMode(MERGE)
    public void givenDestinationCodeChanged_whenGetProduct_thenReturnsInconsistentEanProduct() throws Exception {

        String response = StreamUtils.copyToString(inconsistentEan.getInputStream(), Charset.defaultCharset());

        mockMvc.perform(MockMvcRequestBuilders.get("/product/ean/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

}