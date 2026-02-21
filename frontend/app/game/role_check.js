// // src/app/room/[id].js
// import { useLocalSearchParams } from 'expo-router';
// import { useEffect, useState } from 'react';
// import { Text, View } from 'react-native';
// // SocketProvider 임포트 경로를 확인해주세요!


// // 로직과 UI를 담은 내부 컴포넌트

// function RoomContent({ data }) {
//     const myPrivateData = usePrivateSubscription(playerId);
//     const [timeLeft, setTimeLeft] = useState(30);

//     useEffect(() => {
//         // 0초가 되면 타이머 정지
//         if (timeLeft <= 0) return;

//         const timer = setInterval(() => {
//         // 함수형 업데이트를 써야 리렌더링에 안전함
//         setTimeLeft((prev) => prev - 1);
//         }, 1000);

//         // 컴포넌트 나갈 때 타이머 확실히 제거 (메모리 누수 방지)
//         return () => clearInterval(timer);
//     }, [timeLeft]);

//     return (
//         <View>
//             <Text>당신의 도둑 식별 번호: {myPrivateData?.data}</Text>
//             <Text>남은 시간</Text>
//             <Text style={[timeLeft <= 5 && { color: 'red' }]}>{timeLeft}</Text>
//         </View>
//     );
// }

// // 메인 엔트리 포인트
// export default function RoomDetailScreen() {
//   const data = useLocalSearchParams();

//   return (
//       <RoomContent data={data} />
//   );
// }

import { useLocalSearchParams } from "expo-router";
import { View, Text, StyleSheet, Image } from "react-native";
import ScreenContainer from "@components/global/ScreenContainer";
import useRoomSocket from "@hooks/useRoomSocket";
import { typography } from "../../src/constants/typography";

export default function RoleCheckScreen() {
    const { roomId, playerId } = useLocalSearchParams();

    const { me, timeLeft, prisonerNumber } = useRoomSocket(roomId, Number(playerId));

    // 데이터 로딩 중일 때 처리
    if (!me) {
        return (
            <ScreenContainer>
                <Text style={{ color: 'white', alignSelf: 'center' }}>정보를 불러오는 중...</Text>
            </ScreenContainer>
        );
    }
    
    return (
        <ScreenContainer>
            <View style={styles.container}>
                <Text style={typography.title}>역할을 확인해주세요</Text>


                <View style={styles.roleBox}>
                    {me.role === "POLICE" ? (<Image source={require('@assets/images/role_police.png')} />
                    ) : (<Image source={require('@assets/images/role_thief.png')} style={{ width: 308, height: 347 }} />)}
                </View>

                {me.role === "THIEF" && (
                    <View style={styles.numberContainer}>
                        <Text style={typography.title}>{prisonerNumber || "----"}</Text>
                    </View>
                )}

                <View style={{ alignItems: "center" }}>
                    <Text style={typography.title}>도주 시간까지</Text>
                    {/* 여기서 30초부터 줄어드는 숫자가 자동으로 보임 */}
                    <Text style={[typography.title, { color: timeLeft < 10 ? "red" : "#fff" }]}>
                        00:{timeLeft < 10 ? `0${timeLeft}` : timeLeft}
                    </Text>
                </View>


            </View>
        </ScreenContainer>
    );
}

const styles = StyleSheet.create({
    container: { flex: 1, justifyContent: 'center', alignItems: 'center', gap: 20 },
    title: { fontSize: 24, color: 'white', fontWeight: 'bold' },
    roleBox: { alignItems: 'center', gap: 10 },
    policeText: { fontSize: 40, color: '#4D96FF', fontWeight: 'bold' }, // 파란색
    thiefText: { fontSize: 40, color: '#FF6B6B', fontWeight: 'bold' },   // 빨간색
    desc: { fontSize: 16, color: '#ccc' }
});