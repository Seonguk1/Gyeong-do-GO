// app/_layout.js
import { Stack } from 'expo-router';
import { SocketProvider } from '../src/context/SocketContext';

export default function RootLayout() {
  return (
    <SocketProvider>
      <Stack screenOptions={{ headerShown: false }}>
        <Stack.Screen name="index" />
        <Stack.Screen name="room/[roomId]/index" />
        <Stack.Screen name="game/[roomId]/index" />
      </Stack>
    </SocketProvider>
  );
}