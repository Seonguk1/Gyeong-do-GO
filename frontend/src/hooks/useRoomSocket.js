import { useSocket } from "@context/SocketContext";
import { useEffect, useMemo } from "react";

export default function useRoomSocket(roomId, playerId) {
<<<<<<< HEAD
<<<<<<< HEAD
  const { connect, connected, disconnect, roomData, timeLeft, prisonerNumber, catchTheif, isOutOfBounds, rescue, rescueSuccess, startVisible} = useSocket();
=======
  const { connect, connected, disconnect, roomData, timeLeft, prisonerNumber, catchTheif, isOutOfBounds, rescue, rescueSuccess} = useSocket();
>>>>>>> ede2ea40 (구출, 검거, 이탈 구현)
=======
  const { connect, connected, disconnect, roomData, timeLeft, prisonerNumber, catchTheif, isOutOfBounds, rescue, rescueSuccess, startVisible} = useSocket();
>>>>>>> de7f06e9 (game start popup)

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

<<<<<<< HEAD
<<<<<<< HEAD
  return { connected, disconnect, roomData, timeLeft, prisonerNumber, catchTheif, isOutOfBounds, rescue, rescueSuccess, startVisible, ...result };
=======
  return { connected, disconnect, roomData, timeLeft, prisonerNumber, catchTheif, isOutOfBounds, rescue, rescueSuccess, ...result };
>>>>>>> ede2ea40 (구출, 검거, 이탈 구현)
=======
  return { connected, disconnect, roomData, timeLeft, prisonerNumber, catchTheif, isOutOfBounds, rescue, rescueSuccess, startVisible, ...result };
>>>>>>> de7f06e9 (game start popup)
}