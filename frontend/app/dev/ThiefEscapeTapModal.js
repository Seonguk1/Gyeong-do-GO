// src/screens/GamePlayThiefScreen.js

import React, { useState } from "react";
import { View } from "react-native";
import ThiefEscapeTapModal from "@components/game/ThiefEscapeTapModal";

export default function GamePlayThiefScreen() {
  const [open, setOpen] = useState(true);

  return (
    <View style={{ flex: 1 }}>
      <ThiefEscapeTapModal visible={open} onDismiss={() => setOpen(false)} />
    </View>
  );
}