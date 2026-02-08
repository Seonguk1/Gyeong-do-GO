import { useLocalSearchParams } from 'expo-router';
import { useEffect, useMemo } from 'react';
import { useSocket } from '../context/SocketContext';

const roomSubscription = (playerId) => {
  const { roomId } = useLocalSearchParams();
  const { 
    client, 
    roomData, 
    setRoomData, 
    startVisible, 
    setStartVisible, 
    connected, 
    connectToRoom, 
    leaveRoom 
  } = useSocket();

  // 1. 방 입장 및 퇴장 처리
  useEffect(() => {
    if (roomId && playerId) {
      connectToRoom(roomId, playerId);
    } else {
      console.log('roomId 또는 playerId가 없어서 실행 안 됨');
    }
    return () => {
      leaveRoom(roomId, playerId);
    };
  }, [roomId, playerId]);

  // 2. 소켓 구독 및 메시지 수신 처리 (핵심 로직)
  useEffect(() => {
    console.log("구독 시작 전")
    console.log(connected)
    console.log(client)
    console.log(roomId)
    // 연결이 확실히 되었을 때만 구독 시작
    if (connected && client && client.connected && roomId) {
      console.log(`🔌 [Socket] ${roomId}번 방 구독 시작`);

      const subscription = client.subscribe(`/topic/room/${roomId}`, (message) => {
        const receivedData = JSON.parse(message.body);
        console.log('📩 [Socket] 메시지 도착:', receivedData.type);

        if (receivedData.type === "UPDATE_ROOM") {
          // ✨ [핵심 수정 1] 데이터 구조 통일
          // 소켓으로 온 데이터가 'data' 껍데기 없이 올 경우를 대비해 구조를 맞춰준다.
          // (API는 보통 { data: {...} } 형태이므로 이에 맞춤)
          const standardizedData = receivedData.data 
            ? receivedData 
            : { ...receivedData, data: receivedData }; // data 키가 없으면 통째로 data 안에 넣음

          // ✨ [핵심 수정 2] 불변성 유지 (강제 리렌더링)
          // 단순히 setRoomData(standardizedData)라고 하면 주소값이 같을 때 리액트가 무시할 수 있음.
          // 전개 연산자(...)를 써서 "새로운 객체"로 인식하게 만듦.
          setRoomData({ ...standardizedData }); 
        } 
        else if (receivedData.type === "ROOM_STATUS_CHANGE") {
          // 구조 분해 할당으로 안전하게 접근
          const status = receivedData.data?.roomStatus || receivedData.roomStatus;

          if (status === "STARTING") {
            setStartVisible(true);
          }
          else if (status === "ROLE_CHECK") {
            // 역할 확인 로직
          }
          else if (status === "RUNAWAY") {
            // 도망 로직
          }
          else if (status === "PLAYING") {
            // 게임 시작 로직
          }
          else if (status === "FINISHED") {
            // 게임 종료 로직
          }
        }
      });

      // 구독 완료 후, "나 들어왔어" 메시지 전송
      client.publish({
        destination: '/app/game/join',
        body: JSON.stringify({ "roomId": roomId, "playerId": playerId }),
      });

      // 언마운트 시 구독 해제
      return () => {
        console.log(`🔌 [Socket] ${roomId}번 방 구독 해제`);
        subscription.unsubscribe();
      };
    }
  }, [connected, client, roomId, playerId]); // playerId 의존성 추가 (안전하게)

  // 3. 데이터 가공 (화면 렌더링용)
  const participantsByRole = useMemo(() => {
    // ✨ [핵심 수정 3] 데이터 소스 안전하게 찾기
    // roomData.data가 있으면 쓰고, 없으면 roomData 자체를 씀
    const sourceData = roomData?.data || roomData;
    const players = sourceData?.players;

    if (!players || !Array.isArray(players)) {
      return { police: [], thief: [], host: null };
    }

    return {
      host: players.find(p => p.host === true),
      police: players.filter(p => p.role === 'POLICE'),
      thief: players.filter(p => p.role === 'THIEF'),
    };
  }, [roomData]); // roomData가 바뀌면(위에서 setRoomData({...}) 했으므로) 무조건 재계산됨

  const roomSource = useMemo(() => {
    const sourceData = roomData?.data || roomData;
    return {
      roomData: sourceData,
      players: sourceData?.players || []
    };
  }, [roomData]);

  const start = useMemo(() => {
    return {
      modalBool: startVisible
    };
  }, [startVisible]);

  // 훅의 리턴값
  return {
    roomData: roomSource.roomData,
    players: roomSource.players,
    connected,
    modalBool: start.modalBool,
    police: participantsByRole.police,
    thief: participantsByRole.thief,
    host: participantsByRole.host,
    
    // 필요하다면 수동 갱신 함수도 내보낼 수 있음 (지금은 소켓으로 해결되니 생략)
  };
};

export default roomSubscription;