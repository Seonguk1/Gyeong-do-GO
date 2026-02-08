import httpClient from "./httpClient";

const start = async (roomId, data) =>{
    const responseData = await httpClient('POST',`/api/rooms/${roomId}/start`,data)
    return responseData
}
export default start;