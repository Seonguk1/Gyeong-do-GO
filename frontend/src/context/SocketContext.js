import { createContext, useContext, useEffect, useRef, useState, useMemo } from 'react';
import { Client } from '@stomp/stompjs';
import 'fast-text-encoding';
import { useRouter } from 'expo-router';
import { getSecondsDiff } from '@utils/timeUtils'
const SocketContext = createContext(null);

export const SocketProvider = ({ children }) => {
  const router = useRouter()
  const clientRef = useRef(null);
  const [connected, setConnected] = useState(false);
  const [roomData, setRoomData] = useState(null);
  const [timeLeft, setTimeLeft] = useState(0);
  const [prisonerNumber, setPrisonerNumber] = useState(null);

  useEffect(() => {
    if (timeLeft <= 0) return;
    const timerId = setInterval(() => {
      setTimeLeft((prev) => prev - 1);
    }, 1000);
    return () => clearInterval(timerId);
  }, [timeLeft]);

  const connect = (roomId, playerId) => {
    if (clientRef.current?.active) return;

    console.log(`🔌 [Global Socket] 연결 시작: Room ${roomId}`);

    const client = new Client({
      brokerURL: 'ws://152.69.225.125/ws', // IP 확인 필수
      forceBinaryWSFrames: true,
      appendMissingNULLonIncoming: true,

      onConnect: () => {
        console.log('✅ [Global Socket] 연결 성공!');
        setConnected(true);

        // 1. 구독 (데이터 수신)
        client.subscribe(`/topic/room/${roomId}`, (message) => {
          console.log('📩 소켓 메시지 도착:', message.body);
          const received = JSON.parse(message.body);
          if (received.type === "UPDATE_ROOM") {
            // API 구조와 소켓 구조가 다를 수 있으므로 안전하게 처리
            const newData = received.data ? received.data : received;
            setRoomData({ ...newData });
          }
          else if (received.type === "ROOM_STATUS_CHANGE") {
            // 게임 시작 등의 상태 변경 처리
            const status = received.data?.roomStatus || received.roomStatus;
            console.log("방 상태 변경:", status);
            if (status == "STARTING") {
              const serverTime = received.data?.startTime;
              const diff = getSecondsDiff(serverTime);
              setTimeLeft(diff > 0 ? diff : 5);
            }
            else if (status == "ROLE_CHECK") {
              setTimeLeft(10);
              router.push({
                pathname: "/game/role_check",
                params: {
                  roomId: roomId,
                  playerId: playerId
                }
              });
            }
            else if (status == "RUNAWAY") {
              setTimeLeft(60);
              router.push({
                pathname: "/game/",
                params: {
                  roomId: roomId,
                  playerId: playerId,
                  roomData: roomData
                }
              });
            }
          }
        });

        client.subscribe(`/queue/player/${playerId}`, (message) => {
          const received = JSON.parse(message.body);
          if (received.type === "PRISONER_NUMBER") {
            console.log("🔢 죄수 번호 수신:", received.data);
            setPrisonerNumber(received.data); // 죄수 번호 저장
          }
        });

        // 2. 입장 메시지 전송
        client.publish({
          destination: '/app/game/join',
          body: JSON.stringify({ roomId, playerId }),
        });
      },
      onWebSocketClose: () => {
        console.log('⚠️ [Global Socket] 연결 끊김');
        setConnected(false);
      },
    });

    client.activate();
    clientRef.current = client;
  };

  const disconnect = () => {

    if (clientRef.current) {
      clientRef.current.deactivate();
      setConnected(false);
      setRoomData(null);
      setPrisonerNumber(null);
    }
  };

  return (
    <SocketContext.Provider value={{ connect, disconnect, connected, roomData, timeLeft, prisonerNumber }}>
      {children}
    </SocketContext.Provider>
  );
};

export const useSocket = () => useContext(SocketContext);