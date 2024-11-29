package ubuthebear.shop.domain.product.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ubuthebear.shop.domain.product.dto.request.CategoryRequest;
import ubuthebear.shop.domain.product.dto.response.CategoryResponse;
import ubuthebear.shop.domain.product.entity.Category;
import ubuthebear.shop.domain.product.repository.CategoryRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 카테고리 관리를 위한 서비스 클래스
 * 카테고리의 생성, 조회 등의 비즈니스 로직을 처리
 *
 * @author ubuthebear
 * @version 1.0
 * @see CategoryRepository
 * @see Category
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)  // 기본적으로 읽기 전용 트랜잭션 설정
public class CategoryService {

    private final CategoryRepository categoryRepository;

    /**
     * 새로운 카테고리를 생성
     * 동일한 이름의 카테고리가 존재하는 경우 예외가 발생
     *
     * @param request 카테고리 생성 요청 정보
     * @return CategoryResponse 생성된 카테고리 정보
     * @throws RuntimeException 동일한 이름의 카테고리가 이미 존재하는 경우
     */
    @Transactional
    public CategoryResponse createCategory(CategoryRequest request) {
        if (categoryRepository.existsByName(request.getName())) {
            throw new RuntimeException("Category already exists");
        }
        Category category = new Category();
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        return new CategoryResponse(categoryRepository.save(category));
    }

    /**
     * 모든 카테고리 목록을 조회
     *
     * @return List<CategoryResponse> 전체 카테고리 목록
     */
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(CategoryResponse::new)
                .collect(Collectors.toList());
    }
}