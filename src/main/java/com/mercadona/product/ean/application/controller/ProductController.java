package com.mercadona.product.ean.application.controller;

import com.mercadona.product.ean.application.controller.common.Error;
import com.mercadona.product.ean.application.controller.common.ErrorLevel;
import com.mercadona.product.ean.application.controller.common.GenericResponse;
import com.mercadona.product.ean.application.controller.dto.CreateProductRequestDTO;
import com.mercadona.product.ean.application.controller.dto.ProductResponseDTO;
import com.mercadona.product.ean.application.controller.dto.UpdateProductRequestDTO;
import com.mercadona.product.ean.application.controller.mapper.DtoMapper;
import com.mercadona.product.ean.domain.aggregate.Product;
import com.mercadona.product.ean.domain.factory.ProductFactory;
import com.mercadona.product.ean.domain.repository.IProductRepository;
import com.mercadona.product.ean.infrastructure.repository.exception.ResourceAlreadyExistsException;
import com.mercadona.product.ean.infrastructure.repository.exception.ResourceNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/product/ean")
@Validated
public class ProductController {

    private final ProductFactory productFactory;

    private final IProductRepository productRepository;

    public ProductController(ProductFactory productFactory, IProductRepository productRepository) {
        this.productFactory = productFactory;
        this.productRepository = productRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<GenericResponse<ProductResponseDTO>> getProduct(@PathVariable("id") @Positive Long id) {
        Product product;
        try {
            product = productRepository.findById(id);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(GenericResponse.infoResponse(e.getMessage()), HttpStatus.NOT_FOUND);
        }
        ProductResponseDTO productResponseDTO = DtoMapper.mapToProductResponseDTO(product);
        return ResponseEntity.ok(GenericResponse.dataResponse(productResponseDTO));
    }

    @GetMapping("/find")
    public ResponseEntity<GenericResponse<ProductResponseDTO>> getProductsByProductCode(
            @RequestParam @Pattern(regexp="^\\d{5}$", message="Product code must be numeric and with length of 5") String productCode,
            @RequestParam @Pattern(regexp="^\\d$", message="Destination code must be a numeric digit") String destinationCode,
            @RequestParam @Pattern(regexp="^\\d{7}$",message="Provider code must be numeric and with length of 7") String providerCode
    ) {
        Product product;
        try {
            product = productRepository
                    .findByDetails(productCode, destinationCode, providerCode);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(GenericResponse.infoResponse(e.getMessage()), HttpStatus.NOT_FOUND);
        }

        GenericResponse<ProductResponseDTO> response = GenericResponse.dataResponse(DtoMapper.mapToProductResponseDTO(product));
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<GenericResponse<List<ProductResponseDTO>>> getProductsByProductCode(
            @RequestParam @Pattern(regexp="^\\d{5}$", message="Product code must be numeric and with length of 5") String productCode) {
        List<ProductResponseDTO> products = productRepository.findAllByProductCode(productCode).stream()
                .map(DtoMapper::mapToProductResponseDTO)
                .toList();

        GenericResponse<List<ProductResponseDTO>> response = GenericResponse.dataResponse(products);
        if (products.isEmpty()) {
            response.addError(new Error("No products found with product code " + productCode, ErrorLevel.WARNING));
        }
        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<GenericResponse<ProductResponseDTO>> createProduct(
            @Valid @RequestBody CreateProductRequestDTO requestDTO, @AuthenticationPrincipal Jwt jwt) {
        Product product = productFactory.getProductFromEanCode(requestDTO.eanCode());
        try {
            product = productRepository.save(product);
        } catch (ResourceAlreadyExistsException e) {
            GenericResponse<ProductResponseDTO> response = GenericResponse.errorResponse(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }
        ProductResponseDTO productResponseDTO = DtoMapper.mapToProductResponseDTO(product);
        return ResponseEntity.created(URI.create("/product/ean/" + product.getId())).body(GenericResponse.dataResponse(productResponseDTO));
    }

    @PostMapping("/{id}")
    public ResponseEntity<GenericResponse<ProductResponseDTO>> updateProduct(@PathVariable("id") Long id, @Valid @RequestBody UpdateProductRequestDTO requestDTO) {
        Product product;
        try {
            product = productRepository.findById(id);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(GenericResponse.infoResponse(e.getMessage()), HttpStatus.NOT_FOUND);
        }
        product.setEanCode(requestDTO.eanCode());
        try {
            product = productRepository.update(product);
        } catch (ResourceAlreadyExistsException e) {
            GenericResponse<ProductResponseDTO> response = GenericResponse.errorResponse(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }
        ProductResponseDTO productResponseDTO = DtoMapper.mapToProductResponseDTO(product);
        return ResponseEntity.ok(GenericResponse.dataResponse(productResponseDTO));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> deleteProduct() {
        return new ResponseEntity<>(HttpStatusCode.valueOf(501));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<GenericResponse<Object>> handleValidationException(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        List<String> validationErrors = new ArrayList<>();
        for(ObjectError error : result.getAllErrors()) {
            if(error.getDefaultMessage() != null) {
                validationErrors.add(error.getDefaultMessage());
            }
        }
        return ResponseEntity.badRequest().body(GenericResponse.errorResponse(validationErrors));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<GenericResponse<Object>> handleValidationException(ConstraintViolationException ex) {
        List<String> validationErrors = new ArrayList<>();
        for(ConstraintViolation<?> constraintViolation : ex.getConstraintViolations()) {
            if(constraintViolation.getMessage() != null) {
                validationErrors.add(constraintViolation.getMessage());
            }
        }
        return ResponseEntity.badRequest().body(GenericResponse.errorResponse(validationErrors));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<GenericResponse<Object>> handleValidationException(MissingServletRequestParameterException ex) {
        return ResponseEntity.badRequest().body(GenericResponse.errorResponse(ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<GenericResponse<Object>> handleValidationException(MethodArgumentTypeMismatchException ex) {
        return ResponseEntity.badRequest().body(GenericResponse.errorResponse(ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<GenericResponse<Object>> handleValidationException(Exception ex) {
        return ResponseEntity.internalServerError().body(GenericResponse.errorResponse(ex.getMessage()));
    }
}
