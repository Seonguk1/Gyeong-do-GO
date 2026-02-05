import { Alert } from "react-native";
import settingRoom from "../api/settingRoom";

const useSettingRoom = async (roomId, data) => {
    const responseData = await settingRoom(roomId, data);
    console.log(responseData)
}
export default useSettingRoom;