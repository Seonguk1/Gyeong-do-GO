import { Alert } from "react-native";
import changeRole from "../api/changeRole";

const useChangeRole = async (data) => {
    const responseData = await changeRole(data);
}
export default useChangeRole;