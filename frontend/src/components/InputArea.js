import { Text, TextInput } from "react-native";



const InputArea = ({title, onChangeText, value, keyboardType = 'default' })=>{
  return(
    <>
      <Text>{title}</Text>
      <TextInput style={{marginTop: 10,
            marginBottom: 10,
            paddingHorizontal: 50,
            height: 40,
            borderRadius: 10,
            borderColor: 'gray',
            borderWidth: 1
            }}
            value={value}
            onChangeText={onChangeText}
            placeholder={`${title}`}
            keyboardType={keyboardType}
      />
    </>
  )
}

export default InputArea;