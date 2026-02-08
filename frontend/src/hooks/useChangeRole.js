import { Alert } from "react-native";
import changeRole from "../api/changeRole";

const useChangeRole = async (roomId, data) => {
    const responseData = await changeRole(roomId, data);
}
export default useChangeRole;