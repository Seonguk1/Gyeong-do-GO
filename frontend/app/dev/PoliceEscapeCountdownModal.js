// 사용 예시
// src/screens/GamePlayPoliceScreen.js

import React, { useState } from "react";
import { View } from "react-native";
import PoliceEscapeCountdownModal from "@components/game/PoliceEscapeCountdownModal";

export default function GamePlayPoliceScreen() {
  const [open, setOpen] = useState(true);

  return (
    <View style={{ flex: 1, backgroundColor: "#eee", justifyContent: "center", alignItems: "center" }}>
      <PoliceEscapeCountdownModal
        visible={open}
        seconds={15}
        onDone={() => setOpen(false)}
      />
    </View>
  );
}