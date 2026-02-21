// src/components/modals/ThiefEscapeTapModal.js

import React from "react";
import { Modal, Pressable, View, Text, StyleSheet } from "react-native";
import { typography } from "@constants/typography";

/**
 * 도둑용: 타이머 없음, 한 번 탭하면 닫힘
 * - visible: 표시 여부
 * - onDismiss: 탭 시 호출 (부모에서 visible false 처리)
 * - texts: 문구 커스터마이즈 가능
 */
export default function ThiefEscapeTapModal({
  visible,
  onDismiss,
  texts = {
    title: "도주시간입니다",
    subtitle: "경찰을 피해 도망가세요!",
    hint: "화면을 탭하면 시작합니다",
  },
}) {
  return (
    <Modal visible={visible} transparent animationType="fade" statusBarTranslucent>
      <Pressable style={s.overlay} onPress={onDismiss}>
        <Pressable style={s.card} onPress={onDismiss}>
          <View style={s.iconWrap}>
            <Text style={s.iconText}>✳</Text>
          </View>

          <Text style={s.title}>{texts.title}</Text>

          <View style={s.subRow}>
            <View style={s.infoDot} />
            <Text style={s.subtitle}>{texts.subtitle}</Text>
          </View>

          <Text style={s.hint}>{texts.hint}</Text>
        </Pressable>
      </Pressable>
    </Modal>
  );
}

const s = StyleSheet.create({
  overlay: {
    flex: 1,
    backgroundColor: "rgba(0,0,0,0.35)",
    justifyContent: "center",
    alignItems: "center",
    paddingHorizontal: 20,
  },
  card: {
    width: "100%",
    maxWidth: 360,
    backgroundColor: "rgba(255,255,255,0.92)",
    borderRadius: 18,
    paddingVertical: 26,
    paddingHorizontal: 18,
    alignItems: "center",
  },

  iconWrap: {
    width: 64,
    height: 64,
    borderRadius: 32,
    borderWidth: 3,
    borderColor: "#FF4B4B",
    backgroundColor: "rgba(255,75,75,0.15)",
    justifyContent: "center",
    alignItems: "center",
    marginBottom: 14,
  },
  iconText: {
    fontSize: 28,
    color: "#FF4B4B",
    fontFamily: "Pretendard-Bold",
  },

  title: {
    ...typography.subheader,
    color: "#111",
    textAlign: "center",
    marginBottom: 10,
  },
  subRow: {
    flexDirection: "row",
    alignItems: "center",
    gap: 8,
    marginBottom: 18,
  },
  infoDot: {
    width: 10,
    height: 10,
    borderRadius: 5,
    backgroundColor: "#FF4B4B",
  },
  subtitle: {
    fontFamily: "Pretendard-Medium",
    fontSize: 13,
    color: "#6B7280",
  },

  hint: {
    fontFamily: "Pretendard-Bold",
    fontSize: 12,
    color: "#FF4B4B",
    marginTop: 6,
  },
});