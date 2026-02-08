import { Stack } from "expo-router";
import { useFonts } from 'expo-font';
import * as SplashScreen from 'expo-splash-screen';
import { SafeAreaProvider } from "react-native-safe-area-context";
import { useEffect } from "react";

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
    <SafeAreaProvider>
        <Stack screenOptions={{ headerShown: false }} />
    </SafeAreaProvider>
  )
}
