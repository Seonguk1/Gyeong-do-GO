import { useRouter } from "expo-router";
import { useState } from "react";
import { Alert, Modal, Pressable, Text, TouchableOpacity, View } from "react-native";



export default function Index() {
  const router = useRouter();
  const [isSetupVisible, setIsSetupVisible] = useState(false);
  return (
    <View
      style={{
        flex: 1,
        justifyContent: "center",
        alignItems: "center",
      }}
    >
      <Modal
          animationType="slide"
          transparent={true}
          visible={isSetupVisible}
          onRequestClose={() => {
            Alert.alert('Modal has been closed.');
            setIsSetupVisible(!isSetupVisible);
          }}>
          <View style={styles.centeredView}>
            <View style={styles.modalView}>
              <Text style={styles.modalText}>Hello World!</Text>
              <Pressable
                style={[styles.button, styles.buttonClose]}
                onPress={() => setModalVisible(!modalVisible)}>
                <Text style={styles.textStyle}>Hide Modal</Text>
              </Pressable>
            </View>
          </View>
      </Modal>
      <TouchableOpacity
        style={{
          justifyContent: "center",
          alignItems: "center",
          width: "50%",
          height: "10%",
          backgroundColor: 'powderblue',
          marginBottom: "5%"
        }}
        onPress={()=>{setIsModalVisible(true)}}
      >
        <Text>방 생성</Text>
      </TouchableOpacity>
      
      <TouchableOpacity
        style={{
          justifyContent: "center",
          alignItems: "center",
          width: "50%",
          height: "10%",
          backgroundColor: 'powderblue'
        }}
        onPress={()=>{router.navigate("/home")}}
      >
        <Text>방 참가</Text>
      </TouchableOpacity>



      <View>

      </View>

    </View>
  );
  
}
