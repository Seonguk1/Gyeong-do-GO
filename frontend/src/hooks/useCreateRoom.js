import { Alert } from "react-native";
import createRoom from "../api/createRoom";

const useCreateRoom = async (data, router) => {
    const responseData = await createRoom(data);

    //오류 처리
    if(responseData.error != null){
        // if(responseData.error.code == 'INTERNAL_ERROR'){
        
        // }
        // else if(responseData.error.code == 'INVALID_REQUEST'){
        //     if(responseData.error.data[0].field=='hostUserId')
        //         Alert.alert('오류','방장 닉네임을 입력해주세요')
        //     else if(responseData.error.data.field=='title')
        //         Alert.alert('오류','방 제목 100자 초과')
        //     else if(responseData.error.data.field=='capacity')
        //         Alert.alert('오류','인원 100명 초과')
        // }
    }
    //정상 실행
    //else{
        console.log(responseData)
        router.push({
            pathname: `/room/${responseData.data.roomId}`,
            params: { 
                    roomId: responseData.data.roomId,
                    playerId: responseData.data.playerId,
                    roomCode: responseData.data.roomCode,
                    roomStatus: responseData.data.roomStatus
            }
        });
    //}
}
export default useCreateRoom;