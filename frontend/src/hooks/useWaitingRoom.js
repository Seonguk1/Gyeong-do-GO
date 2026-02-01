import { useState, useMemo, useEffect } from 'react';
import { useSocket } from '../context/SocketContext'; // 위에서 만든 Context 사용

export const useWaitingRoom = () => {
  const { socket, lastMessage } = useSocket(); // 전역 소켓 꺼내기
  const [userList, setUserList] = useState([]);

  // 메시지 감시
  useEffect(() => {
    if (lastMessage?.type === 'UPDATE_LIST') {
      setUserList(lastMessage.users);
    }
    // 여기에 'GAME_START' 타입이 오면 네비게이션 이동 로직을 추가할 수 있음
  }, [lastMessage]);

  const changeRole = (newRole) => {
    if (socket?.readyState === WebSocket.OPEN) {
      socket.send(JSON.stringify({ type: 'CHANGE_ROLE', role: newRole }));
    }
  };

  // 기존 유저 분류 로직 (그대로 유지)
  const host = useMemo(() => userList.find(u => u.role === 'HOST'), [userList]);
  const policeTeam = useMemo(() => userList.filter(u => u.role === 'POLICE'), [userList]);
  const thiefTeam = useMemo(() => userList.filter(u => u.role === 'THIEF'), [userList]);

  return { userList, host, policeTeam, thiefTeam, changeRole };
};