import httpClient from "./httpClient";

const joinRoom = async (data) =>{
    const responseData = await httpClient('POST','/api/rooms/join',data)
    return responseData
}
export default joinRoom;
// "hostUserId": hostId,
// "title": title,
// "capacity": capacity,