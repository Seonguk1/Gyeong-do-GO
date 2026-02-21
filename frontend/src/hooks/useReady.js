import { Alert } from "react-native";
import ready from "../api/ready";

const useReady = async (roomId, data) => {
    const responseData = await ready(roomId, data);
}
export default useReady;