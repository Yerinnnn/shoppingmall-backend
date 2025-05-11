# 🛍️ 쇼핑몰 웹사이트 포트폴리오 (Spring Boot + React)

## 📌 프로젝트 개요

- **프로젝트명**: 쇼핑몰 웹사이트 (개인 프로젝트)
- **진행 기간**: 2024년 11월 ~ 2025년 4월
- **목표**: Spring Boot와 React를 활용한 풀스택 쇼핑몰 웹 애플리케이션 개발

---

## ⚙️ 기술 스택

- **Backend**: Spring Boot, Spring Security, JWT, JPA, MariaDB
- **Frontend**: React, React Router, Axios
- **DevOps**: AWS EC2, Nginx, GitHub Actions (CI/CD), pm2, Ubuntu
- **형상관리**: Git & GitHub

---

## 💡 주요 기능

| 기능 | 설명 |
| --- | --- |
| 회원가입 / 로그인 | JWT를 이용한 인증 및 보안 처리 |
| 상품 목록 / 상세조회 | 프론트에서 REST API로 데이터를 호출하여 표시 |
| 장바구니 / 주문 기능 | 상품 추가, 삭제, 주문 생성, 결제 등의 기능 구현 |
| 관리자 기능 (개발 중) | 상품 관리, 회원 관리, 주문 관리, 할인/적립금 관리 등 |
| CI/CD 자동 배포 | GitHub Actions로 push 시 EC2 무중단 배포 자동화 |

---

## 🖥️ 화면 설명

- **메인 페이지**
    
    ![screencapture-localhost-3000-2025-05-11-22_08_40.png](attachment:47e34142-a9d6-4dd2-9d49-9045f82aedcc:screencapture-localhost-3000-2025-05-11-22_08_40.png)
    
    - 전체 상품 목록과 카테고리 표시
    
- **상품 상세 페이지**
    
    ![screencapture-localhost-3000-products-1-2025-05-11-22_13_23.png](attachment:fbb89c2f-22a7-4fb6-aacf-fb5e8eb2c069:screencapture-localhost-3000-products-1-2025-05-11-22_13_23.png)
    
    - 상품 이미지, 설명, 가격, 장바구니 버튼
    
- **장바구니**
    
    ![screencapture-localhost-3000-cart-2025-05-11-22_09_43.png](attachment:c967c819-8eca-4e05-b253-79e0aac4149c:screencapture-localhost-3000-cart-2025-05-11-22_09_43.png)
    
    - 수량 조절, 삭제, 총 금액 확인
    
- **주문/결제 페이지**
    
    ![screencapture-localhost-3000-orders-new-2025-05-11-22_09_57.png](attachment:9292457d-af0b-44a9-8701-0c0b2fbeea57:screencapture-localhost-3000-orders-new-2025-05-11-22_09_57.png)
    
    ![스크린샷 2025-05-11 오후 10.10.53.png](attachment:f4ad39f2-fd90-4a6c-88a6-163fe4671585:스크린샷_2025-05-11_오후_10.10.53.png)
    
    ![스크린샷 2025-05-11 오후 10.11.12.png](attachment:a79a5916-b669-49bb-a365-dedf80dc4125:스크린샷_2025-05-11_오후_10.11.12.png)
    
    ![스크린샷 2025-05-11 오후 10.11.38.png](attachment:541506bd-108e-40ed-a24f-1ce5b7bbc8da:스크린샷_2025-05-11_오후_10.11.38.png)
    
    ![screencapture-localhost-3000-payments-success-2025-05-11-22_11_58.png](attachment:de92f87e-6faf-4268-85d9-e39314726443:screencapture-localhost-3000-payments-success-2025-05-11-22_11_58.png)
    
    - 장바구니에 담긴 상품을 기반으로 **주문 요청 생성**
    - 사용자가 배송 정보를 입력한 후, **Toss Payments 테스트 API 연동**을 통해 결제 시뮬레이션 가능
    
- **회원가입/로그인 페이지**
    
    ![screencapture-localhost-3000-signup-2025-05-11-22_16_13.png](attachment:1ca6059c-2815-4872-8701-cd42408a245d:screencapture-localhost-3000-signup-2025-05-11-22_16_13.png)
    
    ![스크린샷 2025-05-11 오후 10.08.09.png](attachment:c45004f0-d80a-46e9-89a2-599aa155feca:스크린샷_2025-05-11_오후_10.08.09.png)
    
    ![스크린샷 2025-05-11 오후 10.08.23.png](attachment:b361c7d4-8462-490a-818d-a9944dbc4d1c:스크린샷_2025-05-11_오후_10.08.23.png)
    
    - JWT를 통한 로그인 및 토큰 저장
    

---

## 🔗 GitHub Repository

- **Backend**: https://github.com/Yerinnnn/shoppingmall-backend
- **Frontend**: https://github.com/Yerinnnn/shoppingmall-frontend

---

## 🚀 배포 주소

- http://ubu-the-bear.shop/

---

## 🧩 추가 설명

- GitHub Actions로 CI/CD 자동화 파이프라인 구성
- 프론트와 백엔드를 분리하여 API 통신 구조 설계
- 에러 처리 및 사용자 친화적인 UX 고려
