import { useFonts } from 'expo-font';
import { Stack } from "expo-router";
import * as SplashScreen from 'expo-splash-screen';
import { useEffect } from "react";
import { SafeAreaProvider } from "react-native-safe-area-context";

// 폰트 로딩 전까지 스플래시 화면이 사라지지 않게 막음
SplashScreen.preventAutoHideAsync();

export default function RootLayout() {
  // 폰트 로드
  const [loaded, error] = useFonts({
    'Pretendard-Bold': require('@assets/fonts/Pretendard-Bold.otf'),
    'Pretendard-SemiBold': require('@assets/fonts/Pretendard-SemiBold.otf'),
    'Pretendard-Medium': require('@assets/fonts/Pretendard-Medium.otf'),
  });

  useEffect(() => {
    if (loaded || error) {
      SplashScreen.hideAsync();
    }
  }, [loaded, error]);

  if (!loaded && !error) {
    return null;
  }

  return (
    <SocketProvider>
      <SafeAreaProvider>
          <Stack screenOptions={{ headerShown: false }} >
            <Stack.Screen name="index" />
            <Stack.Screen name="room/[roomId]/index" />
            <Stack.Screen name="game/[roomId]/index" />
          </Stack>
      </SafeAreaProvider>
    </SocketProvider>
  )
}
