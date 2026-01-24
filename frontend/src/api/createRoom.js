import httpClient from "./httpClient";

const createRoom = async (data) =>{
    const responseData = await httpClient('POST','/api/rooms',data)
    
    return responseData
}
export default createRoom;