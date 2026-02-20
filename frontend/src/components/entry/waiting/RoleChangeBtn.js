import CustomBtn from "@components/global/CustomBtn";
import useChangeRole from "@hooks/useChangeRole";

export default function RoleChangeBtn({ roomId, playerId, title, role, style }) {
  return (
    <CustomBtn
      title={title}
      onPress={async () => {
        try {
          console.log(`🔘 ${role}로 역할 변경 요청 보냄...`);
          // 1. 서버에 HTTP 요청 (DB 업데이트)
          await useChangeRole(roomId, {
            "playerId": playerId,
            "role": role
          });
          // 2. 화면 갱신은 안 함! (소켓 메시지가 오면 알아서 됨)
        } catch (e) {
          console.error("역할 변경 실패:", e);
        }
      }}
      style={[{
        flex: 1,
        alignItems: "flex-start",
        paddingHorizontal: 15,
        height: 41,
        borderTopLeftRadius: 5,
        borderTopRightRadius: 5,
        borderBottomLeftRadius: 0,
        borderBottomRightRadius: 0,
        marginBottom: 7
      }, style]}
    />
  )
}