import { FlatList, Text, View } from "react-native";
import { colors } from "@constants/colors";
import { typography } from "@constants/typography";

export default function PlayerList({ data, playerId }) {
  return (
    <FlatList
      data={data}
      extraData={data} // ✨ [핵심] 데이터 변경 시 리렌더링 강제
      renderItem={({ item }) => (
        <View style={{
          flexDirection: "row", gap: 9, backgroundColor: "#2E3748",
          borderRadius: 10, height: 40, alignItems: "center",
          paddingHorizontal: 12, marginBottom: 5,
            borderWidth: item.id == playerId ? 3 : 0, borderColor:"#fff"
        }}>
          <Text>{item.host ? "👑" : "👤"}</Text>
          <Text style={typography.nickname}>{item.nickname}</Text>
          <Text>{item.ready ? "🟢" : "🔴"}</Text>
        </View>
      )}
      keyExtractor={(item) => item.id ? item.id.toString() : Math.random().toString()}
      style={{
        flex: 1, backgroundColor: "#838790",
        borderRadius: 10, padding: 7
      }}
    />
  )
}