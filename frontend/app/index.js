import { useState } from "react";
import { Button, StyleSheet, View } from "react-native";
import CustomModal from "../src/components/CustomModal";
import InputArea from "../src/components/InputArea";


export default function Index() {
  const [visible, setVisible] = useState(false);
  const [joinVisible,setJoinVisible] = useState(false);
  return(
    <View
      style={{
        flex:1,
        justifyContent:"center",
        alignItems:"center"
      }}
    >
      <Button
        title="방 생성" 
        onPress={() => {setVisible(true)}}
      />
      <CustomModal
        visible={visible}
        setVisible={setVisible}
      >
        <InputArea title="최대 인원" text="최대 인원"/>
        <InputArea title="시간(분)" text="시간(분)"/>
        <InputArea title="경찰 수" text="경찰 수"/>
        <Button title="방 만들기"/>
      </CustomModal>

      <Button
        title="방 참가" 
        onPress={() => {setJoinVisible(true)}}
      />
      <CustomModal
        visible={joinVisible}
        setVisible={setJoinVisible}
      >
        <InputArea title="방 코드" text="방 코드"/>
        <InputArea title="닉네임" text="닉네임"/>
        <Button title="참가"/>
      </CustomModal>
    </View>
  );
}

// export default function Index() {
//   const router = useRouter();
//   const [setVisible, setSetVisible] = useState(false);
//   const [joinVisible, setJoinVisible] = useState(false);

//   return (
//     <View
//       style={{
//         flex: 1,
//         justifyContent: "center",
//         alignItems: "center",
//       }}
//     >


//       <Modal
//           transparent={true}
//           visible={setVisible}
//           onRequestClose={() => {
//             Alert.alert('Modal has been closed.');
//             setSetVisible(!setVisible);
//           }}>
//           <View style={styles.centeredView}>
//             <View style={styles.setView}>
//               <Text style={styles.setText}>Hello World!</Text>
//               <Pressable
//                 style={[styles.button, styles.buttonClose]}
//                 onPress={() => setSetVisible(!setVisible)}>
//                 <Text style={styles.textStyle}>Hide Modal</Text>
//               </Pressable>
//             </View>
//           </View>
//       </Modal>

  

//       <View style={styles.container}>
//       <Button 
//         title="방 생성" 
//         onPress={() => setSetVisible(true)} 
//       />
      
//       </View>
      


//       <Modal
//           transparent={true}
//           visible={joinVisible}
//           onRequestClose={() => {
//             Alert.alert('Modal has been closed.');
//             setJoinVisible(!joinVisible);
//           }}>
//           <View style={styles.centeredView}>
//             <View style={styles.joinView}>
              
              
              
        
//               <Pressable
//                 style={[styles.button, styles.buttonClose]}
//                 onPress={() => setJoinVisible(!joinVisible)}>
//                 <Text style={styles.textStyle}>Hide Modal</Text>
//               </Pressable>
              
//             </View>
//           </View>
//       </Modal>
//       <TouchableOpacity
//         style={{
//           justifyContent: "center",
//           alignItems: "center",
//           width: "50%",
//           height: "10%",
//           backgroundColor: 'powderblue'
//         }}
//         onPress={()=>{setJoinVisible(true)}}
//       >
//         <Text>방 참가</Text>
//         </TouchableOpacity>

        
//         <TouchableOpacity 
//           style={{
//             justifyContent: "center",
//             alignItems: "center",
//             width: "50%",
//             height: "10%",
//             backgroundColor: 'powderblue'
//           }}
//           onPress={() => setJoinVisible(true)}>
//         <Text>방 참가</Text>
//         </TouchableOpacity>
//         <JoinIn
//         visible={joinVisible} 
//         onClose={() => setIsJoinVisible(false)}
//         />


//       <View>

//       </View>

//     </View>
//   );
// }
const styles = StyleSheet.create({
  centeredView: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
  setView: {
    margin: 20,
    backgroundColor: 'white',
    borderRadius: 10,
    padding: 30,
    alignItems: 'center',
    shadowColor: '#000',
    shadowOffset: {
      width: 0,
      height: 2,
    },
    shadowOpacity: 0.25,
    shadowRadius: 4,
    elevation: 5,
  },
  joinView: {
    margin: 20,
    backgroundColor: 'white',
    borderRadius: 10,
    padding: 35,
    alignItems: 'center',
    shadowColor: '#000',
    shadowOffset: {
      width: 0,
      height: 2,
    },
    shadowOpacity: 0.25,
    shadowRadius: 4,
    elevation: 5,
  },
  button: {
    borderRadius: 20,
    padding: 10,
    elevation: 2,
  },
  buttonOpen: {
    backgroundColor: '#F194FF',
  },
  buttonClose: {
    backgroundColor: '#2196F3',
  },
  textStyle: {
    color: 'white',
    fontWeight: 'bold',
    textAlign: 'center',
  },
  modalText: {
    marginBottom: 15,
    textAlign: 'center',
  },
});

