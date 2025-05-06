package ubuthebear.shop.domain.product.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ubuthebear.shop.domain.product.dto.request.ProductRequest;
import ubuthebear.shop.domain.product.dto.response.ProductDetailResponse;
import ubuthebear.shop.domain.product.dto.response.ProductListResponse;
import ubuthebear.shop.domain.product.dto.response.ProductResponse;
import ubuthebear.shop.domain.product.dto.search.ProductSearchRequest;
import ubuthebear.shop.domain.product.entity.Category;
import ubuthebear.shop.domain.product.entity.Product;
import ubuthebear.shop.domain.product.entity.ProductDetail;
import ubuthebear.shop.domain.product.repository.ProductRepository;
import ubuthebear.shop.domain.product.repository.CategoryRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 상품 관리를 위한 서비스 클래스
 * 상품의 CRUD 및 검색 관련 비즈니스 로직을 처리
 *
 * @author ubuthebear
 * @version 1.0
 * @see ProductRepository
 * @see CategoryRepository
 * @see Product
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)  // 기본적으로 읽기 전용 트랜잭션 설정
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    /**
     * 새로운 상품을 등록
     * 상품의 기본 정보와 상세 정보를 함께 저장
     *
     * @param request 상품 등록 요청 정보
     * @return ProductResponse 등록된 상품 정보
     * @throws RuntimeException 카테고리를 찾을 수 없는 경우
     */
    @Transactional
    public ProductResponse createProduct(ProductRequest request) {
        // 1. 카테고리 찾기
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        // 2. 상품 기본 정보 설정
        Product product = new Product();
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setDescription(request.getDescription());
        product.setStockQuantity(request.getStockQuantity());
        product.setCategory(category);

        // 3. 상품 상세 정보 설정
        ProductDetail productDetail = new ProductDetail();
        productDetail.setManufacturer(request.getManufacturer());
        productDetail.setOrigin(request.getOrigin());
        productDetail.setMaterial(request.getMaterial());
        productDetail.setSize(request.getSize());
        productDetail.setWeight(request.getWeight());
        productDetail.setProduct(product);

        product.setProductDetail(productDetail);

        // 4. 상품 저장 및 응답 반환
        return new ProductResponse(productRepository.save(product));
    }

    /**
     * 전체 상품 목록을 조회
     *
     * @return List<ProductListResponse> 전체 상품 목록
     */
    public List<ProductListResponse> getAllProducts() {
        // stream() : 조회된 Product 리스트를 Stream으로 변환 (데이터를 순차적으로 처리 가능)
        return productRepository.findAll().stream()
                // 각 Product 엔티티를 ProductListResponse 객체로 변환
                // 메서드 레퍼런스를 사용한 것으로, 각 Product에 대해 ProductListResponse의 생성자를 호출
                .map(ProductListResponse::new)
                // 변환된 ProductListResponse 객체들을 다시 List로 모음
                // 최종적으로 List<ProductListResponse>가 반환
                .collect(Collectors.toList());
    }

    /**
     * 특정 카테고리에 속한 상품 목록을 조회
     *
     * @param categoryId 카테고리 ID
     * @return List<ProductListResponse> 해당 카테고리의 상품 목록
     */
    public List<ProductListResponse> getProductsByCategory(Long categoryId) {
        return productRepository.findByCategoryCategoryId(categoryId).stream()
                .map(ProductListResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * 상품의 기본 정보를 조회
     *
     * @param id 상품 ID
     * @return ProductResponse 상품 정보
     * @throws RuntimeException 상품을 찾을 수 없는 경우
     */
    public ProductResponse getProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return new ProductResponse(product);
    }

    /**
     * 상품의 상세 정보를 조회하는 헬퍼 메서드
     *
     * @param productId 상품 ID
     * @return ProductDetail 상품 상세 정보
     * @throws RuntimeException 상품 또는 상세 정보를 찾을 수 없는 경우
     */
    private ProductDetail findProductDetail(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        return Optional.ofNullable(product.getProductDetail())
                .orElseThrow(() -> new RuntimeException("Product detail not found"));
    }

    /**
     * 상품의 상세 정보를 조회하고 조회수를 증가
     *
     * @param productId 상품 ID
     * @return ProductDetailResponse 상품 상세 정보
     */
    @Transactional
    public ProductDetailResponse getProductDetail(Long productId) {
        ProductDetail detail = findProductDetail(productId);
        detail.setViewCount(detail.getViewCount() + 1);
        return new ProductDetailResponse(detail);
    }

    /**
     * 상품 정보를 수정
     *
     * @param id 상품 ID
     * @param request 수정할 상품 정보
     * @return ProductResponse 수정된 상품 정보
     * @throws RuntimeException 상품을 찾을 수 없는 경우
     */
    @Transactional
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // 상품 기본 정보 업데이트
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setDescription(request.getDescription());
        product.setStockQuantity(request.getStockQuantity());

        // 카테고리 업데이트
        if (!product.getCategory().getCategoryId().equals(request.getCategoryId())) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            product.setCategory(category);
        }

        // 상품 상세 정보 업데이트
        ProductDetail detail = product.getProductDetail();
        if (detail != null) {
            detail.setManufacturer(request.getManufacturer());
            detail.setOrigin(request.getOrigin());
            detail.setMaterial(request.getMaterial());
            detail.setSize(request.getSize());
            detail.setWeight(request.getWeight());
        }

        // 변경사항 저장
        product = productRepository.save(product);

        return new ProductResponse(product);
    }

    /**
     * 상품을 삭제
     *
     * @param id 삭제할 상품 ID
     */
    @Transactional
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    /**
     * 키워드로 상품을 검색
     *
     * @param keyword 검색 키워드
     * @return List<ProductListResponse> 검색된 상품 목록
     */
    public List<ProductListResponse> searchProducts(String keyword) {
        return productRepository.findByNameContaining(keyword).stream()
                .map(ProductListResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * 다양한 조건으로 상품을 검색
     * TODO: QueryDSL을 사용한 동적 쿼리 구현 필요
     *
     * @param request 검색 조건
     * @return List<ProductResponse> 검색된 상품 목록
     */
    public List<ProductResponse> searchProductsWithFilter(ProductSearchRequest request) {
        return productRepository.findByNameContaining(request.getKeyword())
                .stream()
                .map(ProductResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * 관리자용 전체 상품 목록 조회 (페이지네이션, 정렬, 필터링 포함)
     */
    public Page<ProductListResponse> getAdminProducts(Pageable pageable, Long categoryId, String keyword) {
        Page<Product> productPage;

        if (categoryId != null) {
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new RuntimeException("카테고리를 찾을 수 없습니다: " + categoryId));
            productPage = productRepository.findByCategory(category, pageable);
        } else if (keyword != null && !keyword.isEmpty()) {
            productPage = productRepository.findByNameContainingOrDescriptionContaining(keyword, keyword, pageable);
        } else {
            productPage = productRepository.findAll(pageable);
        }

        return productPage.map(this::convertToProductListResponse);
    }

    /**
     * 상품 재고 수정
     */
    public ProductResponse updateStock(Long productId, int quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다: " + productId));

        if (quantity < 0) {
            throw new IllegalArgumentException("재고 수량은 0 이상이어야 합니다.");
        }

        product.setStockQuantity(quantity);
        product = productRepository.save(product);

        return convertToProductResponse(product);
    }

    private ProductListResponse convertToProductListResponse(Product product) {
        return new ProductListResponse(product);
    }

    private ProductResponse convertToProductResponse(Product product) {
        return new ProductResponse(product);
    }
}