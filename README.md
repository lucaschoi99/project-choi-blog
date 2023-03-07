<h1 align="center">
  Choi's Blog Project
  <br>
</h1>

<h4 align="center"> SpringBoot toy project - 블로그 API 개발</h4>

<p align="center">
  <a href="#about">About the project</a> •
  <a href="#key-features">Key Features</a> •
  <a href="#development-process">Development Process</a>
</p>

// TODO: Update api-docs screenshot when project ready

[comment]: <> (![screenshot]&#40;https://raw.githubusercontent.com/amitmerchant1990/electron-markdownify/master/app/img/markdownify.gif&#41;)

## About the Project

#### Project Introduction
게시글 CRUD 기능을 **API 개발로 구현**하고 **문서화 작업**, **배포**까지 한 스프링 부트 프로젝트 입니다.

더불어, 로그인/회원가입 기능과 비밀번호 암호화, 수정,삭제 권한을 위한 인증/검증 기능을 구현했습니다.

특히, 커뮤니티성 개인 프로젝트를 진행함에 있어 `테스트 케이스 작성`, `배포`, `API 개발`, `꼼꼼한 Exception 처리` 와 같은 점들을 공부할 수 있어 많은 도움이 되었습니다.

#### Environment
- Spring Boot `2.7.6`
- Java `11`
- `Junit5`
- Gradle `7.5.1`

#### KeyWord
Back-End
- `Rest Api`
- `Spring Boot`
- `API Docs`

[comment]: <> (Front-End)

[comment]: <> (- `Vue.js`)

Deployment
- `AWS-EC2`

Config
- `WebMvcConfig` (ArgumentResolver)
- `QueryDslConfig`

Test
- `Junit5`
- Controller, Service 통합 테스트

## Key Features
#### `CRUD` 기능
- 글 작성, 조회, 수정, 삭제 기능 - API 개발

#### `API Docs` 문서화 작업
- 테스트 케이스 관리 - CRUD API 기능 문서화 작업  

#### `인증/검증` 기능
- 글 수정/삭제 권한, 회원가입/로그인 검증, 세션, 쿠키, (JWT)

#### `로그인/회원가입` 기능
- 로그인 시 유저 세션을 발급 받도록 설계
- 발급된 세션 토큰은 Header의 `Cookie`에 담도록 설계

#### `비밀번호 암호화` 기능
- 회원가입 시 raw password를 `Scrypt` 암호화 라이브러리를 이용해 암호화 후 저장

#### `Exception` 처리
- 인증/검증 예외처리: `UnauthorizedUser`, `DuplicateEmailException`, `InvalidSignIn`, `InvalidRequest`
- Status 404 에러(존재하지 않는 글에 대한 처리): `PostNotFound`
- 최상위 Exception 추상 클래스 `ProjectChoiException`을 상속받은 클래스로 설계


## Development Process

#### 개발 중 궁금증과 해결 정리

- [Part1](https://velog.io/@lucaschoi/%EA%B0%9C%EC%9D%B8-%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-%EC%A7%84%ED%96%89-%EC%A4%91-%EA%B6%81%EA%B8%88%ED%96%88%EB%8D%98-%EA%B0%9C%EB%85%90-%EC%B6%94%EA%B0%80-%EA%B3%B5%EB%B6%80) - 프로젝트에 필요한 개발 개념, 스프링 어노테이션에 대하여

- [Part2](https://velog.io/@lucaschoi/%EA%B0%9C%EC%9D%B8-%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-%EA%B6%81%EA%B8%88%ED%96%88%EB%8D%98-%EA%B0%9C%EB%85%90-%EC%B6%94%EA%B0%80-%EA%B3%B5%EB%B6%80-2) - JPA 제공 기능과 Stream, 디자인 패턴에 대하여

- [Part3](https://velog.io/@lucaschoi/%EB%B8%94%EB%A1%9C%EA%B7%B8-%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-API-%EC%9D%B8%EC%A6%9D) - 인증 / 검증 로직에 대하여


---

> GitHub [@lucaschoi99](https://github.com/lucaschoi99) &nbsp;&middot;&nbsp;
> Velog [@lucaschoi](https://velog.io/@lucaschoi)

