import React, { createContext, useContext, useEffect, useRef, useState } from 'react';

const SocketContext = createContext(null);

export const SocketProvider = ({ children, roomId, playerId }) => {
  const socket = useRef(null);
  const [lastMessage, setLastMessage] = useState(null);

  useEffect(() => {
    // 1. 소켓 연결 (앱 실행 시 혹은 방 입장 시 딱 한 번)
    socket.current = new WebSocket(`ws://192.168.0.106:8080/room/${roomId}`);

    socket.current.onopen = () => {
        const enterMessage = {
            type: "ENTER_ROOM", // 백엔드와 약속한 타입 이름
            playerId: playerId,
            roomId: roomId
        };
        socket.current.send(JSON.stringify(enterMessage));
    };

    socket.current.onmessage = (event) => {
      const data = JSON.parse(event.data);
      setLastMessage(data); // 받은 데이터를 전역으로 흘려보냄
    };

    return () => {
      // 화면이 바뀌어도 닫지 않음. 앱을 완전히 나갈 때만 닫게 설정 가능
      // socket.current.close(); 
    };
  }, [roomId]);

  // 소켓 객체와 메시지를 하위 훅들이 쓸 수 있게 공유
  return (
    <SocketContext.Provider value={{ socket: socket.current, lastMessage }}>
      {children}
    </SocketContext.Provider>
  );
};

export const useSocket = () => useContext(SocketContext);