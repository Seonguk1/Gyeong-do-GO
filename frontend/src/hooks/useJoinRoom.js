import { Alert } from "react-native";
import joinRoom from "../api/joinRoom";

const useJoinRoom = async (data, router) => {
    const roomCode = data.roomCode;
        router.push({
            pathname: `/room/${roomCode}`,
            params: { 
                    roomId: responseData.data.roomId,
                    playerId: responseData.data.playerId,
                    roomCode: responseData.data.roomCode,
                    roomStatus: responseData.data.roomStatus
            }
        });
}
export default useJoinRoom;