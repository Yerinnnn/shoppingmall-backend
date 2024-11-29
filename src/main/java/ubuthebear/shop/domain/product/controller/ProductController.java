package ubuthebear.shop.domain.product.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ubuthebear.shop.domain.product.dto.request.ProductRequest;
import ubuthebear.shop.domain.product.dto.response.ProductDetailResponse;
import ubuthebear.shop.domain.product.dto.response.ProductListResponse;
import ubuthebear.shop.domain.product.dto.response.ProductResponse;
import ubuthebear.shop.domain.product.service.ProductService;

import java.util.List;

/**
 * 상품 관리를 위한 REST API 컨트롤러 클래스
 * 상품의 CRUD 및 검색 기능을 제공하는 엔드포인트들을 정의
 *
 * @author ubuthebear
 * @version 1.0
 * @see ProductService
 * @see ProductRequest
 * @see ProductResponse
 * @see ProductListResponse
 * @see ProductDetailResponse
 */
// @Tag : Swagger/OpenAPI 문서화를 위한 어노테이션
@Tag(name = "Product", description = "상품 관리 API")
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    /**
     * 새로운 상품을 등록\
     * HTTP POST /api/products
     *
     * @param request 상품 등록 요청 정보 {@link ProductRequest}
     * @return ResponseEntity<ProductResponse> 등록된 상품 정보
     * @throws RuntimeException 카테고리를 찾을 수 없는 경우
     */
    // @Operation : 각 API 엔드포인트의 설명을 위한 Swagger/OpenAPI 어노테이션
    @Operation(summary = "상품 등록", description = "새로운 상품을 등록합니다.")
    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@RequestBody ProductRequest request) {
        return ResponseEntity.ok(productService.createProduct(request));
    }

    /**
     * 전체 상품 목록을 조회
     * HTTP GET /api/products
     *
     * @return ResponseEntity<List<ProductListResponse>> 전체 상품 목록
     */
    @GetMapping
    public ResponseEntity<List<ProductListResponse>> getProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    /**
     * 특정 카테고리에 속한 상품 목록을 조회
     * HTTP GET /api/products/category/{categoryId}
     *
     * @param categoryId 카테고리 ID
     * @return ResponseEntity<List<ProductListResponse>> 해당 카테고리의 상품 목록
     */
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ProductListResponse>> getProductsByCategory(@PathVariable Long categoryId) {
        return ResponseEntity.ok(productService.getProductsByCategory(categoryId));
    }

    /**
     * 특정 상품의 기본 정보를 조회
     * HTTP GET /api/products/{id}
     *
     * @param id 상품 ID
     * @return ResponseEntity<ProductResponse> 상품 정보
     * @throws RuntimeException 상품을 찾을 수 없는 경우
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProduct(id));
    }

    /**
     * 특정 상품의 상세 정보를 조회
     * 조회 시 해당 상품의 조회수가 증가
     * HTTP GET /api/products/{id}/detail
     *
     * @param id 상품 ID
     * @return ResponseEntity<ProductDetailResponse> 상품 상세 정보
     * @throws RuntimeException 상품 또는 상세 정보를 찾을 수 없는 경우
     */
    @GetMapping("/{id}/detail")
    public ResponseEntity<ProductDetailResponse> getProductDetail(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductDetail(id));
    }

    /**
     * 상품 정보를 수정
     * HTTP PUT /api/products/{id}
     *
     * @param id 수정할 상품 ID
     * @param request 수정할 상품 정보 {@link ProductRequest}
     * @return ResponseEntity<ProductResponse> 수정된 상품 정보
     * @throws RuntimeException 상품을 찾을 수 없는 경우
     */
    @Operation(summary = "상품 수정", description = "상품 정보를 수정합니다.")
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(
            @Parameter(description = "상품 ID", required = true) @PathVariable Long id,
            @RequestBody ProductRequest request) {
        return ResponseEntity.ok(productService.updateProduct(id, request));
    }

    /**
     * 상품을 삭제
     * HTTP DELETE /api/products/{id}
     *
     * @param id 삭제할 상품 ID
     * @return ResponseEntity<Void> 삭제 성공 시 200 OK
     */
    @Operation(summary = "상품 삭제", description = "상품을 삭제합니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(
            @Parameter(description = "상품 ID", required = true) @PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok().build();
    }

    /**
     * 키워드로 상품을 검색
     * HTTP GET /api/products/search?keyword={keyword}
     *
     * @param keyword 검색 키워드
     * @return ResponseEntity<List<ProductListResponse>> 검색된 상품 목록
     */
    @GetMapping("/search")
    public ResponseEntity<List<ProductListResponse>> searchProducts(@RequestParam String keyword) {
        return ResponseEntity.ok(productService.searchProducts(keyword));
    }
}