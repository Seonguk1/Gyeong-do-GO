import { TextInput } from "react-native";


const CustomInput = ({ value, onChangeText, style, placeholder, keyboardType = 'default' }) => {
    return (
        <TextInput
            value={value}
            onChangeText={onChangeText}
            style={[
                {
                    height: 49,
                    width: '100%',
                    borderWidth: 1,
                    borderRadius: 5,
                    paddingLeft: 20,
                    color: value.length === 0 ? 'gray' : 'black'
                },
                style
            ]}
            placeholder={placeholder}
            keyboardType={keyboardType}
        />
    )
}

export default CustomInput;