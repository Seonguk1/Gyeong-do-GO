// src/app/room/[id].js
import { useLocalSearchParams } from 'expo-router';
import { FlatList, Text, View } from 'react-native';

const DATA = [
  { id: '1', title: '🚀 리액트 네이티브 정복하기' },
  { id: '2', title: '💻 백엔드 기다리며 공부중' },
  { id: '3', title: '📱 플랫리스트 마스터' },
  { id: '4', title: '🎨 스타일링 연습' },
  { id: '5', title: '🔥 열정 넘치는 개발자' },
];

const Item = ({ title }) => ( // 개별 아이템 디자인
  <View>
    <Text>{title}</Text>
  </View>
);

export default function RoomDetailScreen() {
  const data = useLocalSearchParams(); // URL에서 roomCode(id)를 가져옴
  console.log(data)
  return (
    <View style={{flex:1,
                  justifyContent: 'center',
                  alignItems: 'center',
                  backgroundColor: '#2E3748'}}>

      <View style={{flex:20,
                    justifyContent: 'center',
                    alignItems: 'center',
                    marginTop:20,
                    marginBottom:40}}>
        <Text style={{ fontSize: 24 }}>방 참가 코드: {data.roomCode}</Text>
        <Text style={{ fontSize: 18 }}>플레이어 수: </Text>
      </View>

      <View style={{flex:10,
                    flexDirection: 'row',
                    justifyContent: 'space-between',
                    marginVertical:10,
                    fontSize: 18,
                    gap:5
                    }}>
        <View style={{flex:1,
                      padding:10,
                      backgroundColor: '#CD5352'
                      }}>
          <Text>도둑</Text>
        </View>
        <View style={{flex:1,
                      padding:10,
                      backgroundColor: '#007ED2'
                      }}>
          <Text>경찰</Text>
        </View>

      </View>

      <View style={{flex:35,
                    flexDirection: 'row',
                    justifyContent: 'space-between',
                    }}>
        <View style={{marginHorizontal:5,
                      backgroundColor:'#D9D9D9',
                      padding:5
        }}>
          <FlatList
          data={DATA}
          renderItem={({ item }) => <Item title={item.title} />}
          keyExtractor={item => item.id}
          />
        </View>
        <View style={{marginHorizontal:5,
                      backgroundColor:'#D9D9D9',
                      padding:5,
                      }}>
          <FlatList
          data={DATA}
          renderItem={({ item }) => <Item title={item.title} />}
          keyExtractor={item => item.id}
          />
        </View>
      </View>

      <View style={{flex:35}}>
        <Text></Text>
      </View>
    </View>
  );
}