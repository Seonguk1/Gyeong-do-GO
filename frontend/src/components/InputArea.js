import { Text, TextInput } from "react-native";

const InputArea = ({title, text})=>{
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
            placeholder={`${text}`}
      />
    </>
  )
}

export default InputArea;