import { useLocalSearchParams, useRouter } from "expo-router";
import { StyleSheet, Text, TouchableOpacity, View } from "react-native";
import 'fast-text-encoding';
import ScreenContainer from "@components/global/ScreenContainer";
import { colors } from "@constants/colors";
import { typography } from "@constants/typography";
import useRoomSocket from "@hooks/useRoomSocket";
import RoleChangeBtn from "@components/game/RoleChangeBtn";
import PlayerList from "@components/game/PlayerList";
import CustomBtn from "@components/global/CustomBtn";
import ready from "@api/ready";
import start from "@api/start";

export default function WaitingRoom() {
  const router = useRouter();
  const { roomId, playerId, roomCode } = useLocalSearchParams();
  console.log(`roomCode : ${roomCode}`)

  const {
    roomData, police, thief,
    isHost, isReady
  } = useRoomSocket(roomId, Number(playerId));
  
  const handleReady = async () => {
    console.log(isReady ? "준비 취소 요청" : "준비 완료 요청");
    await ready(roomId, {"playerId":playerId, "isReady":isReady})
  };

  const handleStart = async () => {
    console.log("게임 시작 요청");
    await start(roomId, {"playerId":playerId})
  };

  const handleSetting = () => {
    console.log("방 설정 모달 열기");
    // setVisible(true);
  };

  return (
    <ScreenContainer>
      <View style={styles.header}>
        <TouchableOpacity
          style={styles.backButton}
          onPress={() => router.canGoBack() ? router.back() : router.replace('/')}
        >
          <Text style={styles.backText}>{"<"}</Text>
        </TouchableOpacity>
        <Text style={typography.header}>대기실</Text>
      </View>

      <View style={styles.main}>
        <Text style={[typography.subheader, { alignSelf: "center" }]}>
          방 코드: #{roomCode}
        </Text>

        {/* 역할 선택 버튼 */}
        <View style={{ flex: 7 }}>
          <View style={{ flexDirection: "row", gap: 7 }}>
            <RoleChangeBtn
              roomId={roomId}
              playerId={playerId}
              title="경찰1"
              role="POLICE"
            />
            <RoleChangeBtn
              roomId={roomId}
              playerId={playerId}
              title="도둑"
              role="THIEF"
              style={{ backgroundColor: "#CD5352" }}
            />
          </View>

          {/* 플레이어 리스트 */}
          <View style={{ flex: 1, flexDirection: "row", gap: 7 }}>
            <PlayerList data={police} playerId={playerId} />
            <PlayerList data={thief} playerId={playerId} />
          </View>
        </View>

        <View style={{ flex: 2 }}>
          <Text style={typography.body}>
             {isHost 
               ? "플레이어가 다 모이면 시작하세요." 
               : "준비가 되면 준비 버튼을 눌러주세요."}
          </Text>
        </View>
        {isHost ? (
            // [CASE A] 방장일 때: 방 설정 & 게임 시작 버튼
            <View style={{ flexDirection: "row", gap: 10 }}>
              <View style={{ flex: 1 }}>
                <CustomBtn 
                    title="방 설정" 
                    onPress={handleSetting} 
                    style={{ backgroundColor: "#555" }} // 회색 등 구별되는 색
                />
              </View>
              <View style={{ flex: 1 }}>
                <CustomBtn 
                    title="게임 시작" 
                    onPress={handleStart} 
                />
              </View>
            </View>
          ) : (
            // [CASE B] 일반 유저일 때: 준비하기 / 준비취소 버튼
            <View>
              <CustomBtn 
                title={isReady ? "준비 취소" : "준비하기"} 
                onPress={handleReady}
                style={{ backgroundColor: isReady ? "#999" : colors.primary }} // 준비 완료 시 회색 처리
              />
            </View>
          )}
      </View>
    </ScreenContainer>
  )
}

const styles = StyleSheet.create({
  header: {
    flex: 1, backgroundColor: colors.background, flexDirection: 'row',
    justifyContent: 'center', alignItems: 'center', paddingHorizontal: 24
  },
  main: { flex: 10, paddingHorizontal: 24 },
  backButton: {
    position: 'absolute', left: 24, zIndex: 1, padding: 10,
  },
  backText: { color: '#fff', fontSize: 24, fontWeight: 'bold' },
});