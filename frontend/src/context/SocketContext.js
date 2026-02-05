import 'fast-text-encoding';
import React, { createContext, useContext, useRef, useState } from 'react';
import { Client, Versions} from '@stomp/stompjs';

const SocketContext = createContext(null);

export const SocketProvider = ({ children }) => {
  const [roomData, setRoomData] = useState(null); // 서버에서 받은 전체 방 데이터
  const [connected, setConnected] = useState(false);
  const client = useRef(null);

  const connectToRoom = (roomId, playerId) => {
    if (client.current?.connected) return; // 이미 연결되어 있으면 중복 실행 방지

    client.current = new Client({
  // 1. 주소는 원래대로 (웹소켓 빼기)
  //brokerURL: 'ws://192.168.201.137:8080/ws',
  webSocketFactory: () => new WebSocket('ws://192.168.201.137:8080/ws'),

  // 2. 버전 협상 에러 방지 (아까 말씀하신 그 코드)
  stompVersions: new Versions(['1.2', '1.1']),

  // 3. 리액트 네이티브 필수 옵션 (데이터 깨짐 방지)
  forceBinaryWSFrames: true,
  appendMissingNULLonIncoming: true,

  onConnect: () => {
    console.log('✅ 드디어 연결 성공!',roomId);
    setConnected(true);
    
    // 구독 및 입장 메시지 전송
    client.current.subscribe(`/topic/room/${roomId}`, (message) => {
      console.log('📩 소켓 메시지 도착:', message.body);
      const data = JSON.parse(message.body);
      setRoomData(data);
    });

    client.current.publish({
      destination: '/app/game/join',
      body: JSON.stringify({ "roomId": roomId, "playerId": playerId }),
    });
  },

  onWebSocketError: (error) => console.log('❌ 웹소켓 에러:', error),
  onWebSocketClose: (event) => console.log('⚠️ 연결 닫힘 코드:', event.code)
});

    console.log('🚀 activate() 호출됨'); // 활성화 시점 확인
    client.current.activate();
  };

  const disconnect = () => {
    if (client.current) {
      client.current.deactivate();
    }
  };

  return (
    <SocketContext.Provider value={{ roomData, connected, connectToRoom, disconnect }}>
      {children}
    </SocketContext.Provider>
  );
};

export const useSocket = () => useContext(SocketContext);