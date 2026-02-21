import start from "../api/start";

const useStart = async (roomId, data) => {
    const responseData = await start(roomId, data);
    console.log(responseData)
}
export default useStart;