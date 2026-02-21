import { useRouter } from 'expo-router';
import { StyleSheet, View, Text } from "react-native";
import CustomBtn from '@components/global/CustomBtn';
import { colors } from '@constants/colors';
import ScreenContainer from '@components/global/ScreenContainer';
import { typography } from '@constants/typography';

export default function Entry_Main() {
    const router = useRouter();
    return (
        <ScreenContainer>
            <View style={styles.contentContainer}>

                {/* 로고 이미지 영역 */}
                <View style={styles.section}>
                    {/* 임시 텍스트 */}
                    <Text style={styles.logo}>경도GO</Text>
                </View>

                {/* 설명 텍스트 영역 */}
                <View style={styles.section}>
                    <Text style={styles.subText}>경찰과 도둑 게임</Text>
                </View>

                {/* 버튼 영역 */}
                <View style={[styles.section, styles.buttonSection]}>
                    <CustomBtn
                        title="방 참가"
                        onPress={() => { router.push('entry/joinRoom') }}
                    />
                    <CustomBtn
                        title="방 생성"
                        onPress={() => { router.push('entry/createRoom') }}
                    />
                </View>
            </View>
        </ScreenContainer>
    );
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: colors.background,

    },
    contentContainer: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
        paddingHorizontal: 52,
        gap: 40
    },
    section: {
        alignItems: 'center',
        width: '100%'
    },

    logo: {
        fontSize: 48,
        fontWeight: '900',
        color: '#ffffff',
        marginBottom: 8,
    },
    subText: {
        fontSize: typography.subheading,
        color: '#cccccc',
    },
    buttonSection: {
        gap: 12
    },
});