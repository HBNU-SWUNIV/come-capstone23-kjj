# 식재료 절약단

- 2023 한밭대학교 컴퓨터공학과 캡스톤디자인 KJJ팀 - Web Frontend Part
- 프로젝트 기간 2022.12 ~ 2023.11

## 협업 도구들

- pigma
- docker
- postman
- ssh server build(cyber duck)
- Swagger

## 언어

- CSS
- JavaScript
- HTML

## 프레임워크

- React

## 사용한 라이브러리들

- apexchart
- mui bootstrap
- keycloak-js
- react-keycloak/web
- axios
- date-fns
- react-cookie
- react-router-dom
- react-swipeable-views
- recoil
- recoil-persist
- styled-components

## 주요 기능들

- 로그인(키클락 연계 및 식재료 절약단 서버 자체), 회원가입
- 메뉴 CRUD
- 공공 데이터 포털 공휴일 정보
- mui - modal, skelton 등
- 버튼 loading Spinner
- useQuery, useMutation - api 통신
- Slider - SwipeableViews 라이브러리

## naming rules

- 디렉토리 파일명은 소문자로 한다.
- CamelCase로 작성한다.
- 디렉토리자체가 react 컴포넌트가 바로들어 있는 폴더라면 폴더이름을 대문자로 시작할 것
- 직접적으로 바로 react컴포넌트가 들어있지 않은 간접적인 관계의 폴더라면 소문자 단수로 표기
- loginSection2 같은 불분명한 의미는 짓지 않는다. 명확한 이름으로 표시를 한다. 어쩔수 없이 길어져도 괜찮다.

### components

- 재사용 가능한 컴포넌트
- 다양한 곳에 사용가능한 범용가능한 컴포넌트

### components / atoms

- 더이상 쪼갤 수 없는 가장 작은 단위의 컴포넌트

### domains

- 주제별로 구분할 수 있는 컴포넌트
- domains/accounts/login or domains/accounts/signup 등

### containers

- 재사용이 불가능 한 컴포넌트(홈페이지, 약관등의 페이지)

## git version 관련 타이밍

- 기능별로 개발을 완료했을 때마다 commit한다.
- 종류별로 만들 부분이 있을 때마다 branch를 나눠서 개발함.
- 나중에 merge해서 한번에 많은 기능을 가진채로 개발하는 것을 막음.
