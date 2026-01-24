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
  const [title, setTitle] = useState(null);
  const [hostId, setHostId] = useState(null);
  const [capacity, setCapacity] = useState(4);
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
        <InputArea title="방장 닉네임" value={hostId} onChangeText={setHostId} />
        <InputArea title="방 제목" value={title} onChangeText={setTitle}/>
        <InputArea title="인원 수" value={capacity} onChangeText={setCapacity} keyboardType="numeric"/>
        <Button title="방 만들기"
          onPress={()=>{
            // 1. post request 요청 -> 성공 시 서버가 방 코드 보내줌
            // 2. 서버한테 받은 정보를 가지고 router.push 진행

            // setRoomInfo();
            useCreateRoom({
              "hostUserId": hostId,
              "title": title,
              "capacity": capacity,
              // "code": "ABC12345", // 필수! 8자리 랜덤 문자열 (엔티티 length=8 기준)
              // "status": "WAITING"
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
