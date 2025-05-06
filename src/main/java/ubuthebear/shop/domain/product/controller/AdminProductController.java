package ubuthebear.shop.domain.product.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ubuthebear.shop.domain.product.dto.response.ProductListResponse;
import ubuthebear.shop.domain.product.dto.response.ProductResponse;
import ubuthebear.shop.domain.product.service.ProductService;

@Tag(name = "Admin Product", description = "관리자용 상품 관리 API")
@RestController
@RequestMapping("/api/admin/products")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminProductController {

    private final ProductService productService;

    @Operation(summary = "관리자용 상품 목록 조회", description = "모든 상품 목록을 페이지네이션, 정렬, 필터링과 함께 조회합니다.")
    @GetMapping
    public ResponseEntity<Page<ProductListResponse>> getAdminProducts(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "createdAt,desc") String sort,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String keyword) {

        System.out.println("관리자 상품 목록 조회 요청 받음");
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort.split(",")[0]));
        return ResponseEntity.ok(productService.getAdminProducts(pageable, categoryId, keyword));
    }

    @Operation(summary = "상품 재고 수정", description = "관리자가 상품의 재고 수량을 직접 수정합니다.")
    @PutMapping("/{id}/stock")
    public ResponseEntity<ProductResponse> updateStock(
            @PathVariable Long id,
            @RequestParam int quantity) {

        return ResponseEntity.ok(productService.updateStock(id, quantity));
    }
}