package ubuthebear.shop.domain.product.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ubuthebear.shop.domain.product.dto.request.CategoryRequest;
import ubuthebear.shop.domain.product.dto.response.CategoryResponse;
import ubuthebear.shop.domain.product.service.CategoryService;

import java.util.List;

/**
 * 카테고리 관리를 위한 REST API 컨트롤러 클래스
 * 카테고리의 생성 및 조회 기능을 제공하는 엔드포인트들을 정의
 *
 * @author ubuthebear
 * @version 1.0
 * @see CategoryService
 * @see CategoryRequest
 * @see CategoryResponse
 */
@Tag(name = "Category", description = "카테고리 관리 API")
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * 새로운 카테고리를 등록
     * HTTP POST /api/categories
     *
     * @param request 카테고리 생성 요청 정보 {@link CategoryRequest}
     * @return ResponseEntity<CategoryResponse> 생성된 카테고리 정보
     * @throws RuntimeException 동일한 이름의 카테고리가 이미 존재하는 경우
     */
    @Operation(summary = "카테고리 등록", description = "새로운 카테고리를 등록합니다.")
    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(@RequestBody CategoryRequest request) {
        return ResponseEntity.ok(categoryService.createCategory(request));
    }

    /**
     * 모든 카테고리 목록을 조회
     * HTTP GET /api/categories
     *
     * @return ResponseEntity<List<CategoryResponse>> 전체 카테고리 목록
     */
    @Operation(summary = "카테고리 목록 조회", description = "모든 카테고리를 조회합니다.")
    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }
}