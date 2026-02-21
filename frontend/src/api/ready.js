import httpClient from "./httpClient";

const ready = async (roomId, data) =>{
    const responseData = await httpClient('PATCH',`/api/rooms/${roomId}/players/ready`,data)
    return responseData
}
export default ready;