# 🛍️ 쇼핑몰 웹사이트 포트폴리오 (Spring Boot + React)

<br/>

## 📌 프로젝트 개요

- **프로젝트명**: 쇼핑몰 웹사이트 (개인 프로젝트)
- **진행 기간**: 2024년 11월 ~ 2025년 4월
- **목표**: Spring Boot와 React를 활용한 풀스택 쇼핑몰 웹 애플리케이션 개발

<br/>

## ⚙️ 기술 스택

- **Backend**: Spring Boot, Spring Security, JWT, JPA, MariaDB
- **Frontend**: React, React Router, Axios
- **DevOps**: AWS EC2, Nginx, GitHub Actions (CI/CD), pm2, Ubuntu
- **형상관리**: Git & GitHub

<br/>

## 💡 주요 기능

| 기능 | 설명 |
| --- | --- |
| 회원가입 / 로그인 | JWT를 이용한 인증 및 보안 처리 |
| 상품 목록 / 상세조회 | 프론트에서 REST API로 데이터를 호출하여 표시 |
| 장바구니 / 주문 기능 | 상품 추가, 삭제, 주문 생성, 결제 등의 기능 구현 |
| 관리자 기능 (개발 중) | 상품 관리, 회원 관리, 주문 관리, 할인/적립금 관리 등 |
| CI/CD 자동 배포 | GitHub Actions로 push 시 EC2 무중단 배포 자동화 |

<br/>

## 🖥️ 화면 설명

- **메인 페이지**
    
    ![Image](https://github.com/user-attachments/assets/23de54d8-af2d-4b98-bc39-accb3a5f87c0)
    
    - 전체 상품 목록과 카테고리 표시

    <br/>
    
- **상품 상세 페이지**
    
    ![Image](https://github.com/user-attachments/assets/745858b7-d17b-424e-a173-a5258f3d948d)
    
    - 상품 이미지, 설명, 가격, 장바구니 버튼

    <br/>
    
- **장바구니**
    
    ![Image](https://github.com/user-attachments/assets/11dc2384-bb4a-4302-8406-2a574217c506)
    
    - 수량 조절, 삭제, 총 금액 확인

    <br/>
    
- **주문/결제 페이지**
    
    ![Image](https://github.com/user-attachments/assets/0864ad3d-906d-417b-9465-32c0148c1af7)
    
    <img width="1440" alt="Image" src="https://github.com/user-attachments/assets/ccf525e9-fa0c-43f1-a52b-9b245c3a386d" />
    <img width="1440" alt="Image" src="https://github.com/user-attachments/assets/62d5489d-c7bb-4baa-a283-3698a0c586d6" />
    <img width="1440" alt="Image" src="https://github.com/user-attachments/assets/fd912d16-6f06-46ec-81e4-25f59fab42b0" />
    <img width="1440" alt="Image" src="https://github.com/user-attachments/assets/96135f40-7892-4db4-9bbf-69c480927912" />
    
    ![Image](https://github.com/user-attachments/assets/ca9e5553-8bb2-4614-a062-7c50b8916991)
    
    - 장바구니에 담긴 상품을 기반으로 **주문 요청 생성**
    - 사용자가 배송 정보를 입력한 후, **Toss Payments 테스트 API 연동**을 통해 결제 시뮬레이션 가능

    <br/>
    
- **회원가입/로그인 페이지**
    
    ![Image](https://github.com/user-attachments/assets/9a1ef121-8265-4558-addb-f24ff9583ecc)

    <img width="1440" alt="Image" src="https://github.com/user-attachments/assets/080e64dc-f0da-4757-9655-f2f0b46fc1ec" />
    
    <img width="1440" alt="Image" src="https://github.com/user-attachments/assets/37cec9ea-cfe2-4fbc-9bcf-0eeb3220a829" />
    
    - JWT를 통한 로그인 및 토큰 저장
    

<br/>

## 🔗 GitHub Repository

- **Backend**: https://github.com/Yerinnnn/shoppingmall-backend
- **Frontend**: https://github.com/Yerinnnn/shoppingmall-frontend

<br/>

## 🚀 배포 주소

- http://ubu-the-bear.shop/

<br/>

## 🧩 추가 설명

- GitHub Actions로 CI/CD 자동화 파이프라인 구성
- 프론트와 백엔드를 분리하여 API 통신 구조 설계
- 에러 처리 및 사용자 친화적인 UX 고려
