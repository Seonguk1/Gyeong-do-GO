import { useLocalSearchParams } from "expo-router";
import { View, Text, StyleSheet, Image } from "react-native";
import ScreenContainer from "@components/global/ScreenContainer";
import useRoomSocket from "@hooks/useRoomSocket"; // ✨ 훅 재사용!

export default function RoleCheckScreen() {
    const { roomId, playerId } = useLocalSearchParams();

    // ✨ 여기서 다시 훅을 부르면, 최신 'me'(내 정보)를 줍니다.
    const { me, connected } = useRoomSocket(roomId, Number(playerId));

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
                <Text style={styles.title}>당신의 역할은?</Text>

                {/* ✨ 역할에 따라 다른 UI 보여주기 */}
                {me.role === "POLICE" ? (
                    <View style={styles.roleBox}>
                        <Text style={styles.policeText}>👮‍♂️ 경찰 👮‍♂️</Text>
                        <Text style={styles.desc}>도둑을 모두 잡으세요!</Text>
                    </View>
                ) : (
                    <View style={styles.roleBox}>
                        <Text style={styles.thiefText}>💰 도둑 💰</Text>
                        <Text style={styles.desc}>경찰을 피해 도망치세요!</Text>
                    </View>
                )}

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