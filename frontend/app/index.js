import { useRouter } from 'expo-router';
import { useState } from "react";
import { Button, View } from "react-native";
import CustomModal from "../src/components/CustomModal";
import InputArea from "../src/components/InputArea";
import useCreateRoom from '../src/hooks/useCreateRoom';

export default function Index() {
  const router = useRouter();
  const [visible, setVisible] = useState(false);
  const [joinVisible,setJoinVisible] = useState(false);
  //create
  const [hostNickName, setHostNickName] = useState(null);
  const [playTime, setPlayTime] = useState(15); // 기본 60초로 설정
  const [prepTime, setPrepTime] = useState(3);
  //join
  const [roomCode, setRoomCode] = useState(null);
  const [joinerId, setJoinerId] = useState(null);



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
        onPress={() => {setVisible(true)}}
      />
      <CustomModal
        visible={visible}
        setVisible={setVisible}
      >
        <InputArea title="방장 닉네임" value={hostNickName} onChangeText={setHostNickName} />
        <InputArea title="플레이 시간(분)" value={playTime} onChangeText={setPlayTime} keyboardType="numeric"/>
        <InputArea title="준비 시간(분)" value={prepTime} onChangeText={setPrepTime} keyboardType="numeric"/>
        <Button title="방 만들기"
          onPress={()=>{
            // 1. post request 요청 -> 성공 시 서버가 방 코드 보내줌
            // 2. 서버한테 받은 정보를 가지고 router.push 진행
            useCreateRoom({
              "nickname": hostNickName,
              "roomSettings":{
                "playTime":playTime,
                "prepTime":prepTime
              }
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
        <InputArea title="닉네임" value={joinerId} onChangeText={setJoinerId} keyboardType="numeric"/>
        {/* <Button title="참가"
          onPress={()=>{
            handleJoinRoom({
              "userId": joinerId,
              "roomCode": roomCode,
              "code":8749
            },router);
          }}
        /> */}
      </CustomModal>
    </View>
  );
}
