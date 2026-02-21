import httpClient from "./httpClient";

const changeRole = async (roomId, data) =>{
    const responseData = await httpClient('PATCH',`/api/rooms/${roomId}/players/role`,data)
    return responseData
}
export default changeRole;