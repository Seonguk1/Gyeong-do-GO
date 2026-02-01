import { useMemo, useEffect } from 'react';
import { useSocket } from '../context/SocketContext';
import { useLocalSearchParams } from 'expo-router';
const useWaitingRoom = (playerId) => {
  const { roomId } = useLocalSearchParams();
  const { roomData, connected, connectToRoom, disconnect } = useSocket();

  // 방에 진입할 때 연결 시작
  useEffect(() => {
    if (roomId && playerId) {
      connectToRoom(roomId, playerId);
    }
    // 페이지를 나갈 때 연결을 끊고 싶다면 아래 주석 해제
    // return () => disconnect();
  }, [roomId, playerId]);

  // roomData가 서버로부터 올 때마다 역할별로 자동 분류 (화면 갱신 준비)
  const participantsByRole = useMemo(() => {
    if (!roomData || !roomData.participants) {
      return { police: [], thief: [], host:null };
    }
    // 1. 방장 찾기 (ishost가 true인 사람 딱 한 명)
    const host = roomData.participants.find(p => p.isHost === true);
    // 2. 방장이 아닌 사람들만 먼저 걸러내기
    const nonHostParticipants = roomData.participants.filter(p => p.isHost !== true);

  return {
    host: host, // 방장 객체
    // 3. 방장이 아닌 사람들 중에서 경찰인 사람만 필터링
    police: nonHostParticipants.filter(p => p.role === 'POLICE'),
    // 4. 방장이 아닌 사람들 중에서 도둑인 사람만 필터링
    thief: nonHostParticipants.filter(p => p.role === 'THIEF')
  };
  }, [roomData]);

  // 내가 방장인지 여부 확인
  const isHost = roomData?.hostId === playerId;

  return {
    roomData,
    connected,
    police: participantsByRole.police,
    thief: participantsByRole.thief,
    host: participantsByRole.host,
    roomTitle: roomData?.title || '로딩 중...'
  };
};

export default useWaitingRoom;