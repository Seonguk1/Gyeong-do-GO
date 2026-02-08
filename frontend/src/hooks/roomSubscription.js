import { useLocalSearchParams } from 'expo-router';
import { useEffect, useMemo } from 'react';
import { useSocket } from '../context/SocketContext';

const roomSubscription = (playerId) => {
  const { roomId } = useLocalSearchParams();
  const { client, roomData, setRoomData, startVisible, setStartVisible, connected, connectToRoom, leaveRoom } = useSocket();

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

  useEffect(() => {
    if (connected && client && client.connected && roomId) {
      const subscription = client.subscribe(`/topic/room/${roomId}`, (message) => {
        console.log('📩 소켓 메시지 도착:', message.body);
        const data = JSON.parse(message.body);
          if (data.type == "UPDATE_ROOM"){
            setRoomData(data); 
          }
          else if (data.type == "ROOM_STATUS_CHANGE"){
            if (data.data.roomStatus == "STARTING"){
              setStartVisible(true);
            }
            else if (data.roomStatus == "ROLE_CHECK"){

            }
            else if (data.roomStatus == "RUNAWAY"){
              
            }
            else if (data.roomStatus == "PLAYING"){
              
            }
            else if (data.roomStatus == "FINISHED"){
              
            }
          }
      });

      client.publish({
        destination: '/app/game/join',
        body: JSON.stringify({ "roomId": roomId, "playerId": playerId }),
      });

      return () => {
        subscription.unsubscribe();
      };
    }
  }, [connected, client, roomId]);

  const participantsByRole = useMemo(() => {
    const players = roomData?.data?.players;
    if (!players || !Array.isArray(players)) {
      return { police: [], thief: [], host: null };
    }

    return {
      host: players.find(p => p.host === true),
      police: players.filter(p => p.role === 'POLICE'),
      thief: players.filter(p => p.role === 'THIEF'),
    };
  }, [roomData]);

  const roomSource = useMemo(() => {
    const source = roomData?.data;
    const list = roomData?.data?.players;
    return {
      roomData: source,
      players: list
    };
  }, [roomData]);

  const start = useMemo(() => {
    return {
      modalBool: startVisible
    };
  }, [startVisible]);

  return {
    roomData: roomSource.roomData,
    players: roomSource.players,
    connected,
    modalBool: start.modalBool,
    police: participantsByRole.police,
    thief: participantsByRole.thief,
    host: participantsByRole.host,
  };
};

export default roomSubscription;