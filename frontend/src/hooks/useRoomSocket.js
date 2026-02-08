import { useEffect, useMemo } from "react";
import { useSocket } from "@context/SocketContext";

export default function useRoomSocket(roomId, playerId) {
  const { connect, connected, roomData, timeLeft, prisonerNumber } = useSocket();

  // 1. 진입 시 연결 요청 (이미 연결돼 있으면 무시됨)
  useEffect(() => {
    if (roomId && playerId) {
      connect(roomId, playerId);
    }
  }, [roomId, playerId]);

  // 2. 데이터 가공 (내 정보 찾기)
  const result = useMemo(() => {
    const players = roomData?.players || roomData?.data?.players || [];
    const myInfo = players.find(p => Number(p.id) === Number(playerId));

    return {
      police: players.filter(p => p.role === 'POLICE'),
      thief: players.filter(p => p.role === 'THIEF'),
      me: myInfo,
      isHost: myInfo?.host === true,
      isReady: myInfo?.ready === true
    };
  }, [roomData, playerId]);

  return { connected, roomData, timeLeft, prisonerNumber, ...result };
}