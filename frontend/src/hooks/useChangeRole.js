import changeRole from "@api/changeRole";

const useChangeRole = async (roomId, data) => {
    try {
        const responseData = await changeRole(roomId, data);
        return responseData; // ✨ 중요: 결과를 뱉어줘야 함!
    } catch (error) {
        console.error("Change Role Error:", error);
        throw error; // 에러를 뱉어야 컴포넌트가 실패한 줄 앎
    }
}
export default useChangeRole;