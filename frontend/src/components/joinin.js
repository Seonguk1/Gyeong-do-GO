import { Children } from "react";
import { Modal, StyleSheet, View } from "react-native";


const JoinIn = ({ visible, onClose}) => {
  return (
    <Modal
      transparent={true}
      visible={visible}
      onRequestClose={onClose}
    >
      <View style={styles.overlay}>
        <View style={styles.modalContainer}>
            {Children}
        </View>
        <TouchableOpacity style={styles.closeButton} onPress={onClose}>
        <Text style={styles.buttonText}>닫기</Text>
        </TouchableOpacity>
      </View>
    </Modal>
  );
};
const styles = StyleSheet.create({
  overlay: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: 'rgba(0,0,0,0.5)',
  },
  modalContainer: {
    width: '85%',
    backgroundColor: 'white',
    borderRadius: 20,
    padding: 20,
    alignItems: 'center',
    elevation: 5,
  },
  closeButton: {
    marginTop: 20,
    backgroundColor: 'powderblue',
    paddingVertical: 10,
    paddingHorizontal: 20,
    borderRadius: 10,
  },
  buttonText: { color: 'black', fontWeight: 'bold' }
});
export default JoinIn;