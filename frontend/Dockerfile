# --- 1. 빌드(Build) 스테이지 ---
# Node.js 20 (또는 18) 이미지를 사용합니다.
FROM node:20-alpine AS build

WORKDIR /app

# package.json과 package-lock.json(또는 yarn.lock)을 먼저 복사
COPY package.json package-lock.json ./

# 의존성을 설치합니다.
RUN npm install

# 나머지 소스 코드를 복사합니다.
COPY . .

# React 앱을 빌드합니다. (결과물은 /app/dist 폴더에 생성됨 - Vite 기준)
# (만약 Create-React-App을 쓰셨다면 RUN npm run build 이고, 아래 COPY 경로도 /app/build 입니다)
RUN npm run build

# --- 2. 서빙(Serve) 스테이지 ---
# 가벼운 Nginx 이미지를 사용합니다.
FROM nginx:1.27-alpine

# Nginx의 기본 설정을 삭제합니다.
RUN rm /etc/nginx/conf.d/default.conf

# 우리가 만든 Nginx 설정을 복사합니다. (다음 단계에서 채울 파일)
COPY nginx.conf /etc/nginx/conf.d/default.conf

# 빌드 스테이지(build)에서 생성된 /app/dist 폴더의 내용물을
# Nginx의 기본 HTML 서빙 폴더로 복사합니다.
# (Create-React-App은 --from=build /app/build ...)
COPY --from=build /app/dist /usr/share/nginx/html

# Nginx는 80 포트를 사용합니다.
EXPOSE 80

# 컨테이너 시작 시 Nginx를 포그라운드에서 실행합니다.
CMD ["nginx", "-g", "daemon off;"]