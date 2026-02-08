import React from 'react';
import { View, StyleSheet, Platform, StatusBar } from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context'; 
import { colors } from '@constants/colors';

const ScreenContainer = ({ children, style, backgroundColor }) => {
  return (
    <SafeAreaView 
      style={[
        styles.safeArea, 
        { backgroundColor: backgroundColor || colors.background } 
      ]} 
      edges={['top', 'left', 'right']}
    >
      <View style={[styles.container, style]}>
        {children}
      </View>
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  safeArea: {
    flex: 1,
  },
  container: {
    flex: 1,
    paddingHorizontal: 20,
  },
});

export default ScreenContainer;