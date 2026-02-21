import { View } from "react-native";

function CustomModal({ visible, children }) {
  
  // return 밖에서 조건부 처리를 하거나, return 안에서 { }를 제대로 써야 합니다.
  if (!visible) return null; 

  return (
    <View style={{
      position: "absolute",
      top: 0, left: 0, right: 0, bottom: 0,
      justifyContent: "center", 
      alignItems: "center",
      backgroundColor: "rgba(0,0,0,0.5)",
      zIndex: 1000, 
    }}>
      <View style={{
        width: "80%",
        backgroundColor: "white",
        padding: 20,
        borderRadius: 15,
        borderWidth: 1,
      }}>
        {/* children은 부모가 <CustomModal>여기에 쓴 내용</CustomModal>을 가져옵니다 */}
        {children}
      </View>
    </View>
  );
}

export default CustomModal;