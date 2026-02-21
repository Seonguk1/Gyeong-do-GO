# Frontend Environment Configuration Guide

## Overview
이 프로젝트는 `.env` 파일을 사용하여 환경 변수를 관리합니다. 보안상 민감한 정보(API 키, 서버 URL 등)는 .env 파일에서 관리해야 합니다.

## Setup

### 1. .env 파일 생성
프로젝트 루트에서 `.env.example`을 복사하여 `.env` 파일을 생성합니다:

```bash
cp .env.example .env
```

### 2. 환경 변수 설정
`.env` 파일을 열어 다음 값들을 설정합니다:

```env
# Backend API Configuration
EXPO_PUBLIC_API_BASE_URL=http://your_backend_ip:8080
EXPO_PUBLIC_WS_URL=ws://your_backend_ip:8080/ws

# Google Maps API Key
EXPO_PUBLIC_GOOGLE_MAPS_API_KEY=your_google_maps_api_key

# Environment
EXPO_PUBLIC_ENV=development
```

#### 각 변수 설명:

| 변수명 | 설명 | 예시 |
|--------|------|------|
| `EXPO_PUBLIC_API_BASE_URL` | REST API 서버 주소 | `http://192.168.1.100:8080` |
| `EXPO_PUBLIC_WS_URL` | WebSocket 서버 주소 | `ws://192.168.1.100:8080/ws` |
| `EXPO_PUBLIC_GOOGLE_MAPS_API_KEY` | Google Maps API 키 | `AIzaSyD...` |
| `EXPO_PUBLIC_ENV` | 환경 (development/production) | `development` |

### 3. 환경 변수 규칙

**중요:** Expo에서 클라이언트 코드로 직접 접근할 수 있는 환경 변수는 **`EXPO_PUBLIC_`** 접두어를 붙여야 합니다.

- ✅ 올바른 예: `EXPO_PUBLIC_API_BASE_URL`
- ❌ 잘못된 예: `API_BASE_URL` (클라이언트에서 접근 불가)

## 사용 방법

### JavaScript/TypeScript 코드에서 접근:

```javascript
const API_URL = process.env.EXPO_PUBLIC_API_BASE_URL || 'http://localhost:8080';
const WS_URL = process.env.EXPO_PUBLIC_WS_URL || 'ws://localhost:8080/ws';
const MAPS_API_KEY = process.env.EXPO_PUBLIC_GOOGLE_MAPS_API_KEY;
```

## 파일 구조

```
frontend/
├── .env                 # 실제 환경 변수 (Git에 업로드되지 않음)
├── .env.example         # 템플릿 파일 (Git에 업로드됨)
├── app.config.js        # Expo 설정 (환경 변수 로드)
├── app.json             # Expo 메타데이터
├── src/
│   ├── api/
│   │   └── httpClient.js     # REST API 클라이언트
│   └── context/
│       └── SocketContext.js  # WebSocket 클라이언트
└── package.json
```

## Git 설정

`.gitignore`에 이미 `.env` 파일이 등록되어 있으므로 실제 환경 변수는 Git에 업로드되지 않습니다.

```gitignore
# .gitignore
.env
.env*.local
```

## 환경별 설정

### 로컬 개발 환경
```env
EXPO_PUBLIC_ENV=development
EXPO_PUBLIC_API_BASE_URL=http://localhost:8080
EXPO_PUBLIC_WS_URL=ws://localhost:8080/ws
```

### 개발 서버 (Network)
```env
EXPO_PUBLIC_ENV=development
EXPO_PUBLIC_API_BASE_URL=http://192.168.1.100:8080
EXPO_PUBLIC_WS_URL=ws://192.168.1.100:8080/ws
```

### 프로덕션 환경
```env
EXPO_PUBLIC_ENV=production
EXPO_PUBLIC_API_BASE_URL=https://api.example.com
EXPO_PUBLIC_WS_URL=wss://api.example.com/ws
```

## 주의사항

⚠️ **.env 파일은 절대 Git에 커밋하면 안 됩니다!**

민감한 정보를 포함하므로:
- ✅ `.env.example`은 커밋 (템플릿만 포함)
- ❌ `.env`는 커밋하지 않음 (실제 값 포함)

## 문제 해결

### 환경 변수가 로드되지 않는 경우

1. `.env` 파일이 프로젝트 루트에 있는지 확인
2. Expo 캐시 초기화:
   ```bash
   expo reset-project
   ```
3. 개발 서버 재시작:
   ```bash
   npm start
   ```

### 특수 문자가 포함된 경우

특수 문자가 있으면 따옴표로 감싸세요:
```env
EXPO_PUBLIC_API_BASE_URL="http://example.com?key=value&other=123"
```

## 참고 링크

- [Expo Environment Variables](https://docs.expo.dev/guides/environment-variables/)
- [Google Maps API Key Setup](https://developers.google.com/maps/documentation/javascript/get-api-key)
