import ScreenContainer from "@components/global/ScreenContainer";
import { KeyboardAvoidingView, Platform, StyleSheet, Text, TextInput, TouchableOpacity, View } from "react-native";
import { colors } from "@constants/colors";
import { typography } from "@constants/typography";
import { useRouter } from "expo-router";
import { useState } from "react";
import CustomInput from "@components/global/CustomInput";
import CustomBtn from "@components/global/CustomBtn";

export default function Entry_createRoom() {
    const router = useRouter();
    const [nickname, setNickname] = useState("");
    const [runawayTime, setRunawayTime] = useState("");
    const [playTime, setPlayTime] = useState("");
    const [mapRadius, setMapRadius] = useState("");
    const [prisonRadius, setPrisonRadius] = useState("");
    return (
        <ScreenContainer
            backgroundColor="#fff"
            style={{ paddingHorizontal: 0 }}
        >
            <View style={styles.header}>
                <TouchableOpacity style={styles.backButton} onPress={() => { router.canGoBack() ? router.back() : router.replace('/'); }}>
                    <Text style={styles.backText}>{"<"}</Text>
                </TouchableOpacity>

                <Text style={typography.header}>방 설정</Text>
            </View>
            <View style={styles.main}>
                <KeyboardAvoidingView
                    style={{ flex: 1 }}
                    behavior={Platform.OS === 'ios' ? 'padding' : 'height'}
                >
                    <View style={styles.innerContent}>
                        <View>
                            <View style={{ marginBottom: 40 }}>
                                <Text style={typography.body}>
                                    안녕하세요 방장님!{"\n"}
                                    경도GO에 오신 것을 환영합니다
                                </Text>
                            </View>
                            <View>
                                <View style={{ marginBottom: 20 }}>
                                    <Text style={[typography.inputTitle, { marginBottom: 10 }]}> 방장 닉네임 </Text>
                                    <CustomInput
                                        value={nickname}
                                        onChangeText={setNickname}
                                        placeholder="닉네임을 입력해주세요."
                                        style={{ marginBottom: 5 }}
                                    />
                                    <Text style={typography.caption}> 2~10자 이내 </Text>
                                </View>

                                <View style={{ flexDirection: "row", gap: 19, marginBottom: 41 }}>
                                    <View style={{ flex: 1 }}>
                                        <Text style={typography.inputTitle}> 도주 시간 </Text>
                                        <CustomInput
                                            value={runawayTime}
                                            onChangeText={setRunawayTime}
                                            placeholder=""
                                        // style={{width:'50%'}}
                                        />
                                    </View>
                                    <View style={{ flex: 1 }}>
                                        <Text style={typography.inputTitle}> 플레이 시간 </Text>
                                        <CustomInput
                                            value={playTime}
                                            onChangeText={setPlayTime}
                                            placeholder=""
                                        />
                                    </View>
                                </View>
                                <View style={{ marginBottom: 25 }}>
                                    <View style={{ flexDirection: "row", gap: 3, marginBottom: 0 }}>
                                        <Text style={typography.inputTitle}> 맵 설정 </Text>
                                        <Text style={[typography.caption, { marginTop: 5 }]}> 방장 위치 기준 </Text>
                                    </View>
                                    <View>
                                        {/* 지도 */}
                                    </View>
                                </View>
                                <View style={{ gap: 14 }}>
                                    <View style={{ flexDirection: "row", alignItems: "center", gap: 10, marginBottom: 0 }}>
                                        <View style={{ flex: 3 }}>
                                            <Text style={typography.inputTitle}> 맵 범위 </Text>
                                        </View>
                                        <CustomInput
                                            value={mapRadius}
                                            onChangeText={setMapRadius}
                                            placeholder=""
                                            style={{ flex: 10 }}
                                        />
                                    </View>
                                    <View style={{ flexDirection: "row", alignItems: "center", gap: 10, marginBottom: 0 }}>
                                        <View style={{ flex: 3 }}>
                                            <Text style={typography.inputTitle}> 감옥 범위 </Text>
                                        </View>
                                        <CustomInput
                                            value={prisonRadius}
                                            onChangeText={setPrisonRadius}
                                            placeholder=""
                                            style={{ flex: 10 }}
                                        />
                                    </View>
                                </View>
                            </View>
                        </View>
                        <View>
                            <CustomBtn
                                title={"방 생성하기"}
                                
                            />
                        </View>
                    </View>
                </KeyboardAvoidingView>
            </View>
        </ScreenContainer>
    )
}

const styles = StyleSheet.create({
    header: {
        flex: 1,
        backgroundColor: colors.background,
        flexDirection: 'row',
        justifyContent: 'center',
        alignItems: 'center',
        paddingHorizontal: 24
    },
    main: {
        flex: 10,
        paddingHorizontal: 24
    },
    innerContent: {
        flex: 1,
        justifyContent: 'space-between',
        paddingTop: 45,
        paddingBottom: 80
    },
    backButton: {
        position: 'absolute', // ✨ 공중부양
        left: 24,             // 왼쪽 벽에서 24px 떨어짐
        zIndex: 1,            // 제목보다 위에 올라오게 (터치 가능하게)
        padding: 10,          // 터치 영역 확보
    },
    backText: {
        color: '#fff',
        fontSize: 24,
        fontWeight: 'bold',
    },

})