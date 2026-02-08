import { useEffect, useState, useRef, useMemo } from "react";
import { Client } from '@stomp/stompjs';
import { useRouter } from "expo-router";

export default function useRoomSocket(roomId, playerId) {
  const router = useRouter()
  const stompClient = useRef(null);
  const { connect, connected, roomData } = useSocket();

   useEffect(() => {
     if (!roomId || !playerId) return;
 
     console.log(`🔌 [Socket] ${roomId}번 방 연결 시도...`);
 
     const client = new Client({
       brokerURL: 'ws://10.50.100.57:8080/ws', // ⚠️ 본인 IP 확인
       forceBinaryWSFrames: true,
       appendMissingNULLonIncoming: true,
       
       // 1. 연결 성공 시 실행되는 함수
       onConnect: () => {
         console.log('✅ [Socket] 연결 성공! 구독 시작합니다.');
         setConnected(true);
 
         // 2. 구독 (Subscribe) - 메시지가 오면 여기서 받음
         client.subscribe(`/topic/room/${roomId}`, (message) => {
           const received = JSON.parse(message.body);
           console.log(`📩 [Msg] 타입: ${received.type}`);
           // 3. 데이터 업데이트 (불변성 유지 필수)
           if (received.type === "UPDATE_ROOM") {
             // API 구조와 소켓 구조가 다를 수 있으므로 안전하게 처리
             const newData = received.data ? received.data : received;
             setRoomData({ ...newData }); 
           } 
           else if (received.type === "ROOM_STATUS_CHANGE") {
             // 게임 시작 등의 상태 변경 처리
              const status = received.data?.roomStatus || received.roomStatus;
              console.log("방 상태 변경:", status);

              if(status == "ROLE_CHECK"){
                router.replace({
                pathname: "/game/roleCheck",
                params: { 
                    roomId: roomId, 
                    playerId: playerId 
                }
            });
              }
           }
         });
 
         // 4. 입장 메시지 전송 (Publish)
         client.publish({
           destination: '/app/game/join',
           body: JSON.stringify({ "roomId": roomId, "playerId": playerId }),
         });
       },
 
       onStompError: (frame) => {
         console.error('❌ [Socket] 에러 발생:', frame.headers['message']);
       },
       onWebSocketClose: () => {
         console.log('⚠️ [Socket] 연결 끊김');
         setConnected(false);
       }
     });
 
     // 클라이언트 활성화
     client.activate();
     stompClient.current = client;
 
     // 언마운트 시(뒤로가기 등) 연결 해제
     return () => {
       console.log('👋 [Socket] 연결 해제');
       if (stompClient.current) {
         stompClient.current.deactivate();
       }
     };
   }, [roomId, playerId]); // roomId나 playerId가 바뀌면 다시 연결

  // 데이터 가공 로직도 여기서 처리해서 내보냄
 const { police, thief, me, isHost, isReady } = useMemo(() => {
    // roomData 구조 방어 로직
    const players = roomData?.players || roomData?.data?.players || [];
    
    // 1. 내 정보 찾기 (playerId는 숫자형으로 비교 추천)
    const myInfo = players.find(p => Number(p.id) === Number(playerId));

    return {
      police: players.filter(p => p.role === 'POLICE'),
      thief: players.filter(p => p.role === 'THIEF'),
      me: myInfo,
      isHost: myInfo?.host === true, // 내가 방장인가?
      isReady: myInfo?.ready === true // 내가 준비했나?
    };
  }, [roomData, playerId]);

  // UI에서 필요한 것만 리턴
  return { 
    connected, 
    roomData, 
    police, 
    thief, 
    me,      // 내 정보 객체
    isHost,  // 방장 여부 (true/false)
    isReady  // 준비 여부 (true/false)
  };
}