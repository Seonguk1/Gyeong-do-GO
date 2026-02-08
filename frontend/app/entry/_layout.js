import { Stack } from 'expo-router';

export default function EntryLayout() {
  return (
    <Stack
      screenOptions={{
        headerShown: false, // 기본적으로 헤더 숨김 (필요한 화면에서만 켬)
        contentStyle: { backgroundColor: '#fff' }, // 전환 시 배경색 깜빡임 방지
        animation: 'slide_from_right', // 아이폰 스타일 슬라이드 애니메이션
      }}
    >
      <Stack.Screen name="main" />
      <Stack.Screen name="joinRoom" />
      <Stack.Screen name="createRoom" />
    </Stack>
  );
}