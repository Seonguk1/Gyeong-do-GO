# Welcome to your Expo app 👋

This is an [Expo](https://expo.dev) project created with [`create-expo-app`](https://www.npmjs.com/package/create-expo-app).

## Environment Setup

⚠️ **먼저 환경 변수를 설정해주세요!**

환경 설정 가이드는 [ENV_SETUP.md](./ENV_SETUP.md)를 참조하세요.

```bash
# .env 파일 생성
cp .env.example .env

# .env 파일에서 다음 값들을 설정:
# - EXPO_PUBLIC_API_BASE_URL: 백엔드 API 주소
# - EXPO_PUBLIC_WS_URL: WebSocket 서버 주소
# - EXPO_PUBLIC_GOOGLE_MAPS_API_KEY: Google Maps API 키
```

## Get started

1. Install dependencies

   ```bash
   npm install
   ```

2. Setup environment variables

   ```bash
   cp .env.example .env
   # .env 파일 수정 (위의 Environment Setup 참조)
   ```

3. Start the app

   ```bash
   npx expo start
   ```

In the output, you'll find options to open the app in a

- [development build](https://docs.expo.dev/develop/development-builds/introduction/)
- [Android emulator](https://docs.expo.dev/workflow/android-studio-emulator/)
- [iOS simulator](https://docs.expo.dev/workflow/ios-simulator/)
- [Expo Go](https://expo.dev/go), a limited sandbox for trying out app development with Expo

You can start developing by editing the files inside the **app** directory. This project uses [file-based routing](https://docs.expo.dev/router/introduction).

## Get a fresh project

When you're ready, run:

```bash
npm run reset-project
```

This command will move the starter code to the **app-example** directory and create a blank **app** directory where you can start developing.

## Learn more

To learn more about developing your project with Expo, look at the following resources:

- [Expo documentation](https://docs.expo.dev/): Learn fundamentals, or go into advanced topics with our [guides](https://docs.expo.dev/guides).
- [Learn Expo tutorial](https://docs.expo.dev/tutorial/introduction/): Follow a step-by-step tutorial where you'll create a project that runs on Android, iOS, and the web.

## Join the community

Join our community of developers creating universal apps.

- [Expo on GitHub](https://github.com/expo/expo): View our open source platform and contribute.
- [Discord community](https://chat.expo.dev): Chat with Expo users and ask questions.
