// src/app/room/[id].js
import { useLocalSearchParams } from 'expo-router';
import { useEffect, useState } from 'react';
import { Text, View } from 'react-native';
// SocketProvider 임포트 경로를 확인해주세요!


// 로직과 UI를 담은 내부 컴포넌트

function RoomContent({ data }) {
    const myPrivateData = usePrivateSubscription(playerId);
    const [timeLeft, setTimeLeft] = useState(30);

    useEffect(() => {
        // 0초가 되면 타이머 정지
        if (timeLeft <= 0) return;

        const timer = setInterval(() => {
        // 함수형 업데이트를 써야 리렌더링에 안전함
        setTimeLeft((prev) => prev - 1);
        }, 1000);

        // 컴포넌트 나갈 때 타이머 확실히 제거 (메모리 누수 방지)
        return () => clearInterval(timer);
    }, [timeLeft]);

    return (
        <View>
            <Text>당신의 도둑 식별 번호: {myPrivateData?.data}</Text>
            <Text>남은 시간</Text>
            <Text style={[timeLeft <= 5 && { color: 'red' }]}>{timeLeft}</Text>
        </View>
    );
}

// 메인 엔트리 포인트
export default function RoomDetailScreen() {
  const data = useLocalSearchParams();

  return (
      <RoomContent data={data} />
  );
}