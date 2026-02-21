// src/components/modals/PoliceEscapeCountdownModal.js

import React, { useEffect, useMemo, useRef, useState } from "react";
import { Modal, View, Text, StyleSheet } from "react-native";
import { colors } from "@constants/colors";
import { typography } from "@constants/typography";

const clampInt = (v) => Math.max(0, Math.floor(Number(v) || 0));
const pad2 = (n) => (n < 10 ? `0${n}` : `${n}`);

const formatMMSS = (totalSeconds) => {
  const s = clampInt(totalSeconds);
  const mm = Math.floor(s / 60);
  const ss = s % 60;
  return `${pad2(mm)}:${pad2(ss)}`;
};

/**
 * 경찰용: 타이머가 끝나야 닫힘 (사용자 터치로 닫기 불가)
 * - visible: 표시 여부
 * - seconds: 시작 초
 * - onDone: 0초 되면 호출 (부모에서 visible false 처리)
 * - texts: 문구 커스터마이즈 가능
 */
export default function PoliceEscapeCountdownModal({
  visible,
  seconds = 55,
  onDone,
  texts = {
    title: "도둑이 도주 중입니다",
    subtitle: "작전 구역 내에서 대기하십시오.",
    label: "게임 시작까지",
  },
}) {
  const [remain, setRemain] = useState(clampInt(seconds));
  const timerRef = useRef(null);

  useEffect(() => {
    if (!visible) {
      if (timerRef.current) clearInterval(timerRef.current);
      timerRef.current = null;
      return;
    }

    setRemain(clampInt(seconds));

    if (timerRef.current) clearInterval(timerRef.current);
    timerRef.current = setInterval(() => {
      setRemain((prev) => Math.max(0, prev - 1));
    }, 1000);

    return () => {
      if (timerRef.current) clearInterval(timerRef.current);
      timerRef.current = null;
    };
  }, [visible, seconds]);

  useEffect(() => {
    if (!visible) return;
    if (remain !== 0) return;
    if (timerRef.current) clearInterval(timerRef.current);
    timerRef.current = null;
    if (typeof onDone === "function") onDone();
  }, [remain, visible, onDone]);

  const timeText = useMemo(() => formatMMSS(remain), [remain]);

  return (
    <Modal visible={visible} transparent animationType="fade" statusBarTranslucent>
      <View style={s.overlay} pointerEvents="auto">
        <View style={s.card}>
          <View style={s.iconWrap}>
            <Text style={s.iconText}>✳</Text>
          </View>

          <Text style={s.title}>{texts.title}</Text>

          <View style={s.subRow}>
            <View style={s.infoDot} />
            <Text style={s.subtitle}>{texts.subtitle}</Text>
          </View>

          <Text style={s.label}>{texts.label}</Text>
          <Text style={s.timer}>
            <Text style={s.timerMain}>{timeText.slice(0, 2)}</Text>
            <Text style={s.timerColon}>:</Text>
            <Text style={s.timerAccent}>{timeText.slice(3)}</Text>
          </Text>
        </View>
      </View>
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
    borderColor: "#2F6BFF",
    backgroundColor: "rgba(47,107,255,0.15)",
    justifyContent: "center",
    alignItems: "center",
    marginBottom: 14,
  },
  iconText: {
    fontSize: 28,
    color: "#2F6BFF",
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
    backgroundColor: "#2F6BFF",
  },
  subtitle: {
    fontFamily: "Pretendard-Medium",
    fontSize: 13,
    color: "#6B7280",
  },

  label: {
    fontFamily: "Pretendard-Bold",
    fontSize: 12,
    color: "#2F6BFF",
    marginBottom: 6,
  },
  timer: {
    fontFamily: "Pretendard-ExtraBold",
    fontSize: 56,
    letterSpacing: 1,
  },
  timerMain: {
    color: "#111",
    fontFamily: "Pretendard-ExtraBold",
  },
  timerColon: {
    color: "#111",
    fontFamily: "Pretendard-ExtraBold",
  },
  timerAccent: {
    color: "#2F6BFF",
    fontFamily: "Pretendard-ExtraBold",
  },
});