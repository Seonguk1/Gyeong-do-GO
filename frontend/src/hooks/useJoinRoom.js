import { Alert } from "react-native";
import joinRoom from "../api/joinRoom";

const useJoinRoom = async (data, router) => {
    const responseData = await joinRoom(data);
    const roomId = responseData.roomId;
        router.push({
            pathname: `/room/${roomId}`,
            params: { 
                    roomId: responseData.data.roomId,
                    playerId: responseData.data.playerId,
                    roomCode: responseData.data.roomCode,
                    roomStatus: responseData.data.roomStatus
            }
        });
}
export default useJoinRoom;