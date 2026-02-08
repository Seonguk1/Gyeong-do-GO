import React from 'react';
import { TouchableOpacity, Text, StyleSheet } from 'react-native';
import { typography } from '@constants/typography';

// props로 onPress(함수), text(글자), style(추가 스타일)을 받음
const CustomBtn = ({ onPress, title, style }) => {
  return (
    <TouchableOpacity 
      style={[styles.button, style]} // 기본 스타일 + 넘어온 스타일 합치기
      activeOpacity={0.8} // 터치했을 때 깜빡임 효과
      onPress={onPress}
    >
      <Text style={typography.button}>{title}</Text>
    </TouchableOpacity>
  );
};

const styles = StyleSheet.create({
  button: {
    backgroundColor: '#0088FF',
    height: 62,        
    borderRadius: 15,           // 둥근 모서리
    alignItems: 'center',       // 텍스트 가로 정렬
    justifyContent: 'center',   // 텍스트 세로 정렬
    width: '100%',              // 부모 너비 꽉 채우기  
    marginBottom: 0,           // 버튼 사이 간격
  },
  text: {
    color: '#000000',
    fontSize: 20,
    fontWeight: 'bold',
  }
});

export default CustomBtn;