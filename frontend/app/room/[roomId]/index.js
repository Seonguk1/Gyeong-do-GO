import ready from "@api/ready";
import start from "@api/start";
import PlayerList from "@components/game/PlayerList";
import RoleChangeBtn from "@components/game/RoleChangeBtn";
import CustomBtn from "@components/global/CustomBtn";
import ScreenContainer from "@components/global/ScreenContainer";
import { colors } from "@constants/colors";
import { typography } from "@constants/typography";
import useRoomSocket from "@hooks/useRoomSocket";
import { useLocalSearchParams, useRouter } from "expo-router";
import 'fast-text-encoding';
import { useEffect } from "react";
import { Dimensions, StyleSheet, Text, TouchableOpacity, View } from "react-native";
import Modal from "react-native-modal";


export default function WaitingRoom() {
  const router = useRouter();
  const { width: SCREEN_WIDTH, height: SCREEN_HEIGHT } = Dimensions.get('window');
  const { roomId, playerId, roomCode } = useLocalSearchParams();
  const {
    roomData, police, thief,
    isHost, isReady, disconnect, startVisible, timeLeft
  } = useRoomSocket(roomId, Number(playerId));

  useEffect(() => {
    // 이 화면이 처음 켜질 때는 아무것도 안 함 (이미 useRoomSocket 내부에서 connect 할 테니까)
    return () => {
      // 🟢 사용자가 뒤로가기를 누르거나 다른 화면으로 이동해서 이 컴포넌트가 사라질 때 실행됨
      console.log("🏃 방에서 나감: 구독 해제 및 소켓 연결 종료");
      if (disconnect) {
        disconnect();
      }
    };
  }, []);
  
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
          onPress={() => {router.canGoBack() ? router.back() : router.replace('/')
          }}
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
          
            <Modal
              isVisible={startVisible}
              backdropColor="black"
              backdropOpacity={0.7}
              // coverScreen과 hasBackdrop은 기본값이 true이므로 굳이 안 써도 됩니다.
              // 대신 아래 style이 중요합니다.
              style={{ 
                margin: 0, 
                justifyContent: 'center', 
                alignItems: 'center',
              }} 
            >
              {/* flex: 1을 절대 쓰지 마세요! View가 화면을 꽉 채우면 뒤가 안 보입니다. */}
              <View style={{ alignItems: "center", justifyContent: "center" }}>
                <Text style={[typography.title, { color: "white" }]}>게임이 곧 시작됩니다!</Text>
                <Text style={[typography.title, { color: "red", fontSize: 60, marginTop: 10 }]}>
                  {timeLeft}
                </Text>
              </View>
            </Modal>
          
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
  title: { fontSize: 24, color: 'white', fontWeight: 'bold' }
});