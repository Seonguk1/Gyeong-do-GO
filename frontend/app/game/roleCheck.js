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