import { useMemo, useEffect } from 'react';
import { useSocket } from '../context/SocketContext';
import { useLocalSearchParams } from 'expo-router';

const useWaitingRoom = (playerId) => {
  const { roomId } = useLocalSearchParams();
  const { roomData, connected, connectToRoom, leaveRoom } = useSocket();

  // 방에 진입할 때 연결 시작
  useEffect(() => {
    if (roomId && playerId) {
      connectToRoom(roomId, playerId);
    } else {
      console.log('roomId 또는 playerId가 없어서 실행 안 됨');
    }
    return () => {
      leaveRoom(roomId, playerId); // 서버에 나가기 알림 + 연결 해제
  };
  }, [roomId, playerId]);

  // roomData가 서버로부터 올 때마다 역할별로 자동 분류
  const participantsByRole = useMemo(() => {
    // ⚠️ 중요: 서버 데이터 구조는 roomData.data.players 입니다 (s가 붙음)
    const players = roomData?.data?.players;
    console.log("지금 분류중인 players 목록:", players);
    if (!players || !Array.isArray(players)) {
      return { police: [], thief: [], host: null };
    }

    // 1. 방장 찾기 (서버 데이터에 isHost 필드가 없다면 보통 첫 번째 사람이거나 별도 로직 필요)
    // 일단 기존 코드 형식대로 isHost를 찾습니다.
    return {
      host: players.find(p => p.host === true),
      // 2. 경찰 필터링
      police: players.filter(p => p.role === 'POLICE'),
      // 3. 도둑 필터링
      thief: players.filter(p => p.role === 'THIEF')
    };
  }, [roomData]);

  // 내가 방장인지 여부 확인 (서버 로그의 hostId 또는 host의 id와 비교)
  return {
    roomData: roomData?.data, // 실제 데이터부만 반환
    connected,
    police: participantsByRole.police,
    thief: participantsByRole.thief,
    host: participantsByRole.host,
  };
};

export default useWaitingRoom;