// src/app/room/[id].js
import { useLocalSearchParams } from 'expo-router';
import { useMemo, useState } from 'react';
import { FlatList, Text, TouchableOpacity, View } from 'react-native';
import  useWaitingRoom  from '../../../src/hooks/useWaitingRoom';
// SocketProvider 임포트 경로를 확인해주세요!
import { SocketProvider } from '../../../src/context/SocketContext'; 
import useChangeRole from '../../../src/hooks/useChangeRole';
import { Button } from '@react-navigation/elements';
import CustomModal from '../../../src/components/CustomModal';
import InputArea from '../../../src/components/InputArea';
import { useLocation } from '../../../src/hooks/useLocation';
import useSettingRoom from '../../../src/hooks/useSettingRoom';

const renderUserItem = ({ item }) => (
  <View>
    <Text>{item.nickname}</Text>
    {/* 여기서 host 변수는 위에서 정의되지 않아 에러가 날 수 있으므로 주의가 필요합니다 */}
  </View>
);

// 로직과 UI를 담은 내부 컴포넌트
function RoomContent({ data }) {
  const { host, police, thief} = useWaitingRoom(data.playerId);
  const [visible, setVisible] = useState(false);
  const [map, setMap] = useState(useWaitingRoom(data.mapRadius));
  const [prison, setPrison] = useState(useWaitingRoom(data.prisonRadius));
  const [playTime, setPlayTime] = useState(useWaitingRoom(data.timeLimit));
  const [prepTime, setPrepTime] = useState(useWaitingRoom(data.runawayLimit));
  const { getCurrentCoords } = useLocation();
  return (
    <View style={{flex:1, justifyContent: 'center', alignItems: 'center', backgroundColor: '#2E3748'}}>
      <CustomModal
                visible={visible}
                setVisible={setVisible}
              >
                <InputArea title="플레이 시간(초)" value={playTime} onChangeText={setPlayTime}/>
                <InputArea title="준비 시간(초)" value={prepTime} onChangeText={setPrepTime}/>
                <InputArea title="맵 범위" value={map} onChangeText={setMap} />
                <InputArea title="감옥 범위" value={prison} onChangeText={setPrison} />

                <TouchableOpacity
                  onPress={async () => {
                    // 1. post request 요청 -> 성공 시 서버가 방 코드 보내줌
                    // 2. 서버한테 받은 정보를 가지고 router.push 진행
                    const coords = await getCurrentCoords();
                    useSettingRoom(data.roomId,{
                      "centerLat": coords.latitude,
                      "centerLng": coords.longitude,
                      "mapRadius": map,
                      "prisonRadius": prison,
                      "timeLimit": playTime,
                      "runawayLimit": prepTime
                    });
                    setVisible(false);
                  }}>
                    <Text style={{justifyContent: 'center',}}>방 설정</Text>
                  </TouchableOpacity>
      </CustomModal>
      <View style={{flex:20, justifyContent: 'center', alignItems: 'center', marginTop:20, marginBottom:40}}>
        <Text style={{ fontSize: 24 }}>방 참가 코드: {data.roomCode}</Text>
      </View>

      <View style={{flex:10, flexDirection: 'row', justifyContent: 'space-between', marginVertical:10, fontSize: 18, gap:5}}>
        <TouchableOpacity 
          onPress={()=>{
            useChangeRole(data.roomId,{
              "playerId": data.playerId,
              "role": "THIEF"
            });
          }}
        >
          <View style={{flex:1, padding:10, backgroundColor: '#CD5352'}}>
            <Text>도둑</Text>
          </View>
        </TouchableOpacity>
        <TouchableOpacity 
          onPress={()=>{
            useChangeRole(data.roomId,{
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

      <View style ={{flex:10}}>
        {data.playerId && host?.id ? (
          /* 2. 데이터가 있다면, 방장 ID와 내 ID를 비교 */
          host.id === Number(data.playerId) ? (
            <View style={{flexDirection: 'row', justifyContent: 'space-between', marginVertical:10, gap:5}} >
              <TouchableOpacity style={{ backgroundColor: 'blue' }}
              onPress={() => {setVisible(true)}}
              >
                <Text>방 설정</Text>
              </TouchableOpacity>

              <TouchableOpacity style={{ backgroundColor: 'blue' }}>
                <Text>게임 시작</Text>
              </TouchableOpacity>
            </View>
          ) : (
            <View style={{flexDirection: 'row', justifyContent: 'space-between', marginVertical:10, gap:5}} >
              <TouchableOpacity style={{ backgroundColor: 'gray' }}>
                <Text>준비하기</Text>
              </TouchableOpacity>
            </View>
          )
        ) : (
          /* 3. 데이터가 아직 안 들어왔을 때 */
          <Text>데이터를 불러오는 중입니다...</Text>
        )}
      </View>
      <View style={{flex:25}}>

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