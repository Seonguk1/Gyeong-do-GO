import { Client } from '@stomp/stompjs';
import { useRouter } from 'expo-router';
import 'fast-text-encoding';
import { getDistance } from 'geolib';
import { createContext, useContext, useEffect, useRef, useState } from 'react';
import { useLocation } from "../hooks/useLocation";

const SocketContext = createContext(null);

export const SocketProvider = ({ children }) => {
  const router = useRouter()
  const clientRef = useRef(null);
  const [connected, setConnected] = useState(false);
  const [roomData, setRoomData] = useState(null);
  const [timeLeft, setTimeLeft] = useState(0);
  const [startVisible, setStartVisible] = useState(false);
  const [prisonerNumber, setPrisonerNumber] = useState(null);
  const roomDataRef=useRef(null);
  const { getCurrentCoords } = useLocation();
  const outOfBoundsTimerRef = useRef(null);
  const [isOutOfBounds, setIsOutOfBounds] = useState(false);
  const [rescue, setRescue] = useState(false);
  const [myLocation, setMyLocation] = useState(null);

  const catchTheif = (number) => {//도둑 잡음 메세지
    clientRef.current.publish({
      destination: '/app/game/catch', 
      body: JSON.stringify({
        "targetNumber": number
      })
    });
  }

  useEffect(() => {//사용자 위치 이동 감지(구출 및 이탈)
    if (!connected || !roomDataRef.current || !myLocation) return;
    
    const rawData = roomDataRef.current;

    const centerLat = rawData.centerLat;
    const centerLon = rawData.centerLon;
    const mapRadius = rawData.mapRadius || 300;
    const prisonRadius = rawData.prisonRadius || 20;

    if (!centerLat || !centerLon) return;

    const distance = getDistance(
      { latitude: myLocation.latitude, longitude: myLocation.longitude },
      { latitude: centerLat, longitude: centerLon }
    );

    if (distance > mapRadius) {
      if (!outOfBoundsTimerRef.current) {
        setIsOutOfBounds(true);

        outOfBoundsTimerRef.current = setTimeout(() => {
          handleForceExit();
        }, 10000);
      }
    } 
    else if (distance < prisonRadius) {
      if (!rescue) {
        setRescue(true);
      }
    }
    else {
      if (outOfBoundsTimerRef.current) {
        clearTimeout(outOfBoundsTimerRef.current);
        outOfBoundsTimerRef.current = null;
        setIsOutOfBounds(false);
      }
      if (rescue) {
        setRescue(false);
      }
    }
    return () => {
      if (outOfBoundsTimerRef.current) clearTimeout(outOfBoundsTimerRef.current);
    };
  }, [myLocation]);

  const handleForceExit = () => {
    clientRef.current?.publish({
      destination: '/app/game/leave',
      body: JSON.stringify(),
    });
    disconnect();
    router.replace("/entry/main");
    alert("구역을 너무 멀리 벗어나 게임에서 제외되었습니다.");
  };

  const rescueSuccess = () => {
    clientRef.current?.publish({
          destination: '/app/game/rescue',
          body: JSON.stringify(),
        });
    alert("구출 완료!");
    if (rescue) {
      setRescue(false);
    }
  };


  useEffect(() => {//1초마다 위치 전송
  if (!connected || !clientRef.current?.connected) return;

  const locationTicker = setInterval(async () => {
      const coords = await getCurrentCoords(); 
      setMyLocation(coords);
      if (coords && clientRef.current?.connected) {
        clientRef.current.publish({
          destination: '/app/game/location',
          body: JSON.stringify({
            latitude: coords.latitude,
            longitude: coords.longitude,
          }),
        });
      }
  }, 1000);
  return () => clearInterval(locationTicker);
}, [connected, roomData]);


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

    const useLocal = process.env.EXPO_PUBLIC_USE_LOCALHOST === 'true';
    const host = useLocal ? (process.env.EXPO_PUBLIC_LOCALHOST_IP || 'localhost') : (process.env.EXPO_PUBLIC_SERVER_IP || '127.0.0.1');
    const port = process.env.EXPO_PUBLIC_API_PORT || '8080';
    const wsProtocol = process.env.EXPO_PUBLIC_WS_PROTOCOL || (useLocal ? 'ws' : 'ws');
    const WS_URL = process.env.EXPO_PUBLIC_WS_URL || `${wsProtocol}://${host}/ws`;

    console.debug('[SocketContext] WS_URL:', WS_URL);

    const client = new Client({
      brokerURL: WS_URL,
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
            roomDataRef.current = newData ;
            setRoomData({ ...newData });
          }
          else if (received.type === "ROOM_STATUS_CHANGE") {
            // 게임 시작 등의 상태 변경 처리
            const status = received.data?.roomStatus || received.roomStatus;
            console.log("방 상태 변경:", status);
            if (status == "STARTING") {
              //const serverTime = received.data?.startTime;
              //const diff = getSecondsDiff(serverTime);
              setTimeLeft(5);
              setStartVisible(true);
            }
            else if (status == "ROLE_CHECK") {
              setStartVisible(false);
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
              setTimeLeft(roomDataRef.current.runawayLimit);
              router.push({
                pathname: "/game/",
                params: {
                  roomId: roomId,
                  playerId: playerId
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
        client.publish({
          destination: '/app/game/leave',
          body: JSON.stringify({}),
        });
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
    <SocketContext.Provider value={{ connect, disconnect, connected, roomData, timeLeft, prisonerNumber, catchTheif, isOutOfBounds, rescue, rescueSuccess, startVisible}}>
      {children}
    </SocketContext.Provider>
  );
};

export const useSocket = () => useContext(SocketContext);