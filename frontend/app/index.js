import { useRouter } from 'expo-router';
import { useState } from "react";
import { Button, View } from "react-native";
import CustomModal from "../src/components/CustomModal";
import InputArea from "../src/components/InputArea";
import useCreateRoom from '../src/hooks/useCreateRoom';
import { useLocation } from '../src/hooks/useLocation';

export default function Index() {
  const router = useRouter();
  const [createVisible, setCreateVisible] = useState(false);
  const [joinVisible,setJoinVisible] = useState(false);
  //create
  const [hostNickName, setHostNickName] = useState(null);
  const [playTime, setPlayTime] = useState(600);
  const [prepTime, setPrepTime] = useState(180);
  const { getCurrentCoords } = useLocation();
  //join
  const [roomCode, setRoomCode] = useState(null);
  const [joinerNickName, setJoinerNickName] = useState(null);



  return(
    <View
      style={{
        flex:1,
        justifyContent:"center",
        alignItems:"center"
      }}
    >
      <Button
        title="방 생성" 
        onPress={() => {setCreateVisible(true)}}
      />
      <CustomModal
        visible={createVisible}
        setVisible={setCreateVisible}
      >
        <InputArea title="방장 닉네임" value={hostNickName} onChangeText={setHostNickName} />
        <InputArea title="플레이 시간(초)" value={playTime} onChangeText={setPlayTime} keyboardType="numeric"/>
        <InputArea title="준비 시간(초)" value={prepTime} onChangeText={setPrepTime} keyboardType="numeric"/>

        <Button title="방 만들기"
          onPress={async () => {
            // 1. post request 요청 -> 성공 시 서버가 방 코드 보내줌
            // 2. 서버한테 받은 정보를 가지고 router.push 진행
            const coords = await getCurrentCoords();
            useCreateRoom({
              "nickname": hostNickName,
              "timeLimit":playTime,
              "runawayLimit":prepTime,
              "latitude":coords.latitude,
              "longitude":coords.longitude
            },router);
          }}
        />
      </CustomModal>

      <Button
        title="방 참가" 
        onPress={() => {setJoinVisible(true)}}
      />
      <CustomModal
        visible={joinVisible}
        setVisible={setJoinVisible}
      >
        <InputArea title="방 코드" value={roomCode} onChangeText={setRoomCode}/>
        <InputArea title="닉네임" value={joinerNickName} onChangeText={setJoinerNickName} keyboardType="numeric"/>
        <Button title="참가"
          onPress={()=>{
            useJoinRoom({
              "nickname": joinerNickName,
              "roomCode": roomCode
            },router);
          }}
        />
      </CustomModal>
    </View>
  );
}
