<a href="https://club-project-one.vercel.app/" target="_blank">
<img width="1448" alt="coupong 메인 페이지@2x" src="https://github.com/user-attachments/assets/d797ff95-42d9-43a7-9301-7f41786aaf76">
</a>

<br/>
<br/>

# 0. Getting Started (시작하기)
```bash
$ ./gradlew clean build -x test 
$ docker compose up --build -d
```
[서비스 링크](https://coupong.netlify.app/)

<br/>
<br/>

# 1. Project Overview (프로젝트 개요)
- 프로젝트 이름: Coupong
- 프로젝트 설명: 선착순 쿠폰 이벤트 플랫폼

<br/>
<br/>

# 2. Team Members (팀원 및 팀 소개)
| 진명인 | 백효석 | 심지혜 | 강희준 |
|:------:|:------:|:------:|:------:|
| <img src="https://avatars.githubusercontent.com/myeonginjin" alt="진명인" width="150"> | <img src="https://avatars.githubusercontent.com/alexization" alt="백효석" width="150"> | <img src="https://avatars.githubusercontent.com/sapientia1007" alt="심지혜" width="150"> | <img src="https://avatars.githubusercontent.com/dhfkdlsj" alt="강희준" width="150"> |
| BE | BE | BE | BE |
| [GitHub](https://github.com/myeonginjin) | [GitHub](https://github.com/alexization) | [GitHub](https://github.com/sapientia1007) | [GitHub](https://github.com/dhfkdlsj) |

<br/>
<br/>

# 3. Key Features (주요 기능)
- **인증/인가**:
  - 일반 유저는 쿠폰 이벤트 메인페이지, 관리자는 이벤트 등록 페이지로 이동합니다.
  - 카카오톡, 네이버 그리고 플랫폼 자체 로그인 총 3가지의 로그인 방식이 있습니다.

- **쿠폰 이벤트**:
  - 매일 밤 자정, 이벤트 목록이 초기화됩니다.
  - 이벤트는 카테고리별(커피, 피자, 햄버거, 치킨)로 동일한 날짜, 동일한 시간대에 여러 이벤트가 진행될 수 있습니다.

- **실시간 채팅**:
  - 이벤트가 진행 중이거나 진행 중이지 않을 때에도, 유저끼리 실시간 채팅 서비스를 즐길 수 있습니다.
  - 금칙어 필터링 시스템이 실시간으로 부적절한 챗 메시지를 잡아냅니다. 이때, 허용어는 예외처리하고 우회단어는 동일하게 잡아냅니다.

- **리더보드**:
  - 쿠폰 이벤트에 당첨된 사람은 실시간으로 리더보드에 등록됩니다.
  - 이벤트 카테고리별로 당첨자들의 순위를 매깁니다. 기준은 발행 요청 시각입니다.

<br/>
<br/>

# 4. Tasks & Responsibilities (작업 및 역할 분담)
|  |  |  |
|-----------------|-----------------|-----------------|
| 진명인    |  <img src="https://avatars.githubusercontent.com/myeonginjin" alt="진명인" width="100"> | <ul><li>프로젝트 기획 및 리딩</li><li>인프라 구축 (WAS)</li><li>쿠폰 이벤트 시스템</li><li>금칙어 필터링 시스템</li></ul>     |
| 백효석   |  <img src="https://avatars.githubusercontent.com/alexization" alt="백효석" width="100">| <ul><li>인증/인가 시스템</li><li>서버 부하 테스트</li><li>금칙어 필터링 시스템</li></ul> |
| 심지혜   |  <img src="https://avatars.githubusercontent.com/sapientia1007" alt="심지혜" width="100">    |<ul><li>실시간 채팅 시스템</li><li>개발 문서 정리</li><li>QA</li></ul>  |
| 강희준    |  <img src="https://avatars.githubusercontent.com/dhfkdlsj" alt="강희준" width="100">    | <ul><li>리더보드 시스템</li><li>인프라 구축 (Web Server)</li><li>QA</li></ul>    |

<br/>
<br/>

# 5. Technology Stack (기술 스택)
## 5.1 Backend
|  |  |
|-----------------|-----------------|
| JAVA          |<img src="https://github.com/user-attachments/assets/5df80afe-5c3d-47c1-9f6d-1549d1b2fc42" alt="JAVA" width="200">| 
| Spring Boot   |   <img src="https://github.com/user-attachments/assets/4d0c279a-0dfe-4253-b79e-c615935f9aff" alt="Spring Boot" width="200">|

<br/>

## 5.2 Frotend
|  |  |  |
|-----------------|-----------------|-----------------|
| React    |  <img src="https://github.com/user-attachments/assets/e3b49dbb-981b-4804-acf9-012c854a2fd2" alt="React" width="100"> | 18.3.1    |
| CSS3    |   <img src="https://github.com/user-attachments/assets/c531b03d-55a3-40bf-9195-9ff8c4688f13" alt="CSS3" width="100">|
| Javascript    |  <img src="https://github.com/user-attachments/assets/4a7d7074-8c71-48b4-8652-7431477669d1" alt="Javascript" width="100"> | 

<br/>

## 5.3 DBMS
|  |  |  |
|-----------------|-----------------|-----------------|
| Firebase    |  <img src="https://github.com/user-attachments/assets/1694e458-9bb0-4a0b-8fe6-8efc6e675fa1" alt="Firebase" width="100">    | 10.12.5    |

<br/>

## 5.4 Cooperation
|  |  |
|-----------------|-----------------|
| Git    |  <img src="https://github.com/user-attachments/assets/483abc38-ed4d-487c-b43a-3963b33430e6" alt="git" width="100">    |
| Git Kraken    |  <img src="https://github.com/user-attachments/assets/32c615cb-7bc0-45cd-91ea-0d1450bfc8a9" alt="git kraken" width="100">    |
| Notion    |  <img src="https://github.com/user-attachments/assets/34141eb9-deca-416a-a83f-ff9543cc2f9a" alt="Notion" width="100">    |

<br/>

# 6. Project Structure (프로젝트 구조)
```plaintext
project/
├── public/
│   ├── index.html           # HTML 템플릿 파일
│   └── favicon.ico          # 아이콘 파일
├── src/
│   ├── assets/              # 이미지, 폰트 등 정적 파일
│   ├── components/          # 재사용 가능한 UI 컴포넌트
│   ├── hooks/               # 커스텀 훅 모음
│   ├── pages/               # 각 페이지별 컴포넌트
│   ├── App.js               # 메인 애플리케이션 컴포넌트
│   ├── index.js             # 엔트리 포인트 파일
│   ├── index.css            # 전역 css 파일
│   ├── firebaseConfig.js    # firebase 인스턴스 초기화 파일
│   package-lock.json    # 정확한 종속성 버전이 기록된 파일로, 일관된 빌드를 보장
│   package.json         # 프로젝트 종속성 및 스크립트 정의
├── .gitignore               # Git 무시 파일 목록
└── README.md                # 프로젝트 개요 및 사용법
```

<br/>
<br/>

# 7. Development Workflow (개발 워크플로우)
## 브랜치 전략 (Branch Strategy)
우리의 브랜치 전략은 Git Flow를 기반으로 하며, 다음과 같은 브랜치를 사용합니다.

- Main Branch
  - 배포 가능한 상태의 코드를 유지합니다.
  - 모든 배포는 이 브랜치에서 이루어집니다.
  
- {name} Branch
  - 팀원 각자의 개발 브랜치입니다.
  - 모든 기능 개발은 이 브랜치에서 이루어집니다.

<br/>
<br/>

# 8. Coding Convention
## 문장 종료
```
// 세미콜론(;)
console.log("Hello World!");
```

<br/>


## 명명 규칙
* 상수 : 영문 대문자 + 스네이크 케이스
```
const NAME_ROLE;
```
* 변수 & 함수 : 카멜케이스
```
// state
const [isLoading, setIsLoading] = useState(false);
const [isLoggedIn, setIsLoggedIn] = useState(false);
const [errorMessage, setErrorMessage] = useState('');
const [currentUser, setCurrentUser] = useState(null);

// 배열 - 복수형 이름 사용
const datas = [];

// 정규표현식: 'r'로 시작
const = rName = /.*/;

// 이벤트 핸들러: 'on'으로 시작
const onClick = () => {};
const onChange = () => {};

// 반환 값이 불린인 경우: 'is'로 시작
const isLoading = false;

// Fetch함수: method(get, post, put, del)로 시작
const getEnginList = () => {...}
```

<br/>

## 블록 구문
```
// 한 줄짜리 블록일 경우라도 {}를 생략하지 않고, 명확히 줄 바꿈 하여 사용한다
// good
if(true){
  return 'hello'
}

// bad
if(true) return 'hello'
```

<br/>

## 함수
```
함수는 함수 표현식을 사용하며, 화살표 함수를 사용한다.
// Good
const fnName = () => {};

// Bad
function fnName() {};
```

<br/>

## 태그 네이밍
Styled-component태그 생성 시 아래 네이밍 규칙을 준수하여 의미 전달을 명확하게 한다.<br/>
태그명이 길어지더라도 의미 전달의 명확성에 목적을 두어 작성한다.<br/>
전체 영역: Container<br/>
영역의 묶음: {Name}Area<br/>
의미없는 태그: <><br/>
```
<Container>
  <ContentsArea>
    <Contents>...</Contents>
    <Contents>...</Contents>
  </ContentsArea>
</Container>
```

<br/>

## 폴더 네이밍
카멜 케이스를 기본으로 하며, 컴포넌트 폴더일 경우에만 파스칼 케이스로 사용한다.
```
// 카멜 케이스
camelCase
// 파스칼 케이스
PascalCase
```

<br/>

## 파일 네이밍
```
컴포넌트일 경우만 .jsx 확장자를 사용한다. (그 외에는 .js)
customHook을 사용하는 경우 : use + 함수명
```

<br/>
<br/>

# 9. 커밋 컨벤션
## 기본 구조
```
type : subject

body 
```

<br/>

## type 종류
```
feat : 새로운 기능 추가
fix : 버그 수정
docs : 문서 수정
style : 코드 포맷팅, 세미콜론 누락, 코드 변경이 없는 경우
refactor : 코드 리펙토링
test : 테스트 코드, 리펙토링 테스트 코드 추가
chore : 빌드 업무 수정, 패키지 매니저 수정
```

<br/>

## 커밋 이모지
```
== 코드 관련
📝	코드 작성
🔥	코드 제거
🔨	코드 리팩토링
💄	UI / style 변경

== 문서&파일
📰	새 파일 생성
🔥	파일 제거
📚	문서 작성

== 버그
🐛	버그 리포트
🚑	버그를 고칠 때

== 기타
🐎	성능 향상
✨	새로운 기능 구현
💡	새로운 아이디어
🚀	배포
```

<br/>

## 커밋 예시
```
== ex1
✨Feat: "회원 가입 기능 구현"

SMS, 이메일 중복확인 API 개발

== ex2
📚chore: styled-components 라이브러리 설치

UI개발을 위한 라이브러리 styled-components 설치
```

<br/>
<br/>

# 10. 컨벤션 수행 결과
<img width="100%" alt="코드 컨벤션" src="https://github.com/user-attachments/assets/0dc218c0-369f-45d2-8c6d-cdedc81169b4">
<img width="100%" alt="깃플로우" src="https://github.com/user-attachments/assets/2a4d1332-acc2-4292-9815-d122f5aea77c">
