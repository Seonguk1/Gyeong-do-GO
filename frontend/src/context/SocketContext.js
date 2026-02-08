import { Client, Versions } from '@stomp/stompjs';
import 'fast-text-encoding';
import { createContext, useContext, useRef, useState } from 'react';

const SocketContext = createContext(null);

export const SocketProvider = ({ children }) => {
  const [roomData, setRoomData] = useState(null); 
  const [connected, setConnected] = useState(false);
  const client = useRef(null);

  const connectToRoom = (roomId, playerId) => {
    if (client.current?.connected) return; 

    client.current = new Client({
      webSocketFactory: () => new WebSocket('ws://172.30.1.61:8080/ws'),
      stompVersions: new Versions(['1.2', '1.1']),
      forceBinaryWSFrames: true,
      appendMissingNULLonIncoming: true,

      onConnect: () => {
        console.log('✅ 드디어 연결 성공!', roomId);
        setConnected(true);
      },

      onWebSocketError: (error) => console.log('❌ 웹소켓 에러:', error),
      onWebSocketClose: (event) => {
        setConnected(false);
        console.log('⚠️ 연결 닫힘 코드:', event.code);
      }
    });

    console.log('🚀 activate() 호출됨');
    client.current.activate();
  };

  const leaveRoom = (roomId, playerId) => {
    if (client.current && client.current.connected) {
      client.current.publish({
        destination: '/app/game/leave',
        body: JSON.stringify({}),
      });
      console.log(`📤 나가기 요청 전송: 방 ${roomId}, 플레이어 ${playerId}`);

      setTimeout(() => {
        client.current.deactivate();
        setConnected(false);
        setRoomData(null);
      }, 100);
    }
  };

  return (
    <SocketContext.Provider value={{ 
      client: client.current, 
      roomData, 
      setRoomData, 
      connected, 
      connectToRoom, 
      leaveRoom 
    }}>
      {children}
    </SocketContext.Provider>
  );
};

export const useSocket = () => useContext(SocketContext);