import httpClient from "./httpClient";

const settingRoom = async (roomId, data) =>{
    const responseData = await httpClient('PUT',`/api/rooms/${roomId}/settings`,data)
    return responseData
}
export default settingRoom;
// "hostUserId": hostId,
// "title": title,
// "capacity": capacity,