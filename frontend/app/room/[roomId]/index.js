// src/app/room/[id].js
import { useLocalSearchParams } from 'expo-router';
import { useMemo } from 'react';
import { FlatList, Text, TouchableOpacity, View } from 'react-native';
import  useWaitingRoom  from '../../../src/hooks/useWaitingRoom';
// SocketProvider 임포트 경로를 확인해주세요!
import { SocketProvider } from '../../../src/context/SocketContext'; 
import useChangeRole from '../../../src/hooks/useChangeRole';

const renderUserItem = ({ item }) => (
  <View>
    <Text>{item.nickname}</Text>
    {/* 여기서 host 변수는 위에서 정의되지 않아 에러가 날 수 있으므로 주의가 필요합니다 */}
  </View>
);

// 로직과 UI를 담은 내부 컴포넌트
function RoomContent({ data }) {
  const { host, police, thief} = useWaitingRoom(data.playerId);

  return (
    <View style={{flex:1, justifyContent: 'center', alignItems: 'center', backgroundColor: '#2E3748'}}>
      <View style={{flex:20, justifyContent: 'center', alignItems: 'center', marginTop:20, marginBottom:40}}>
        <Text style={{ fontSize: 24 }}>방 참가 코드: {data.roomCode}</Text>
      </View>

      <View style={{flex:10, flexDirection: 'row', justifyContent: 'space-between', marginVertical:10, fontSize: 18, gap:5}}>
        <TouchableOpacity onPress={()=>{useChangeRole({
            "playerId": data.playerId,
            "role": "THIEF"
          });
        }}>
          <View style={{flex:1, padding:10, backgroundColor: '#CD5352'}}>
            <Text>도둑</Text>
          </View>
        </TouchableOpacity>
        <TouchableOpacity 
          onPress={()=>{
            useChangeRole({
              "playerId": data.playerId,
              "role": "POLICE"
            });
          }}
        >
          <View style={{flex:1, padding:10, backgroundColor: '#007ED2'}}>
            <Text>경찰</Text>
          </View>
        </TouchableOpacity>
      </View>

      <View style={{flex:35, flexDirection: 'row', justifyContent: 'space-between'}}>
        <View style={{marginHorizontal:5, backgroundColor:'#D9D9D9', padding:5}}>
          <FlatList
            data={thief}
            renderItem={renderUserItem}
            keyExtractor={(item) => item.id.toString()}
            ListEmptyComponent={<Text>참가자가 없습니다.</Text>}
          />
        </View>
        <View style={{marginHorizontal:5, backgroundColor:'#D9D9D9', padding:5}}>
          <FlatList
            data={police}
            renderItem={renderUserItem}
            keyExtractor={(item) => item.id.toString()}
            ListEmptyComponent={<Text>참가자가 없습니다.</Text>}
          />
        </View>
      </View>

      <View style={{flex:35}}>
        <Text></Text>
      </View>
    </View>
  );
}

// 메인 엔트리 포인트
export default function RoomDetailScreen() {
  const data = useLocalSearchParams();

  return (
    <SocketProvider roomId={data.roomId} playerId={data.playerId}>
      <RoomContent data={data} />
    </SocketProvider>
  );
}