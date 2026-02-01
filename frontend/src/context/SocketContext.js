import 'fast-text-encoding';
import React, { createContext, useContext, useRef, useState } from 'react';
import { Client } from '@stomp/stompjs';

const SocketContext = createContext(null);

export const SocketProvider = ({ children }) => {
  const [roomData, setRoomData] = useState(null); // 서버에서 받은 전체 방 데이터
  const [connected, setConnected] = useState(false);
  const client = useRef(null);

  const connectToRoom = (roomId, playerId) => {
    if (client.current?.connected) return; // 이미 연결되어 있으면 중복 실행 방지

    client.current = new Client({
      brokerURL: 'ws://192.168.201.137:8080/ws', // 2. 웹소켓 업그레이드 요청 주소
      reconnectDelay: 5000,
      onConnect: () => {
        setConnected(true);
        console.log('Connected to WebSocket');

        // 3. /topic/room/{roomId} 구독 (서버가 보내는 메시지 리스닝)
        client.current.subscribe(`/topic/room/${roomId}`, (message) => {
          const data = JSON.parse(message.body);
          console.log('5. 받은 소켓 메시지로 데이터 갱신:', data);
          setRoomData(data); // 서버가 준 UPDATE_ROOM 이벤트 데이터 저장
        });

        // 4. /app/game/join으로 입장 완료 메시지 전송
        client.current.publish({
          destination: '/app/game/join',
          body: JSON.stringify({ "roomId": roomId, "playerId": playerId }),
        });
      },
      onDisconnect: () => {
        setConnected(false);
        setRoomData(null);
      }
    });

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