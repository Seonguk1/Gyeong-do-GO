import { useLocalSearchParams, useRouter } from 'expo-router';
import { StyleSheet, View, Text } from "react-native";
import CustomBtn from '@components/global/CustomBtn';
import { colors } from '@constants/colors';
import ScreenContainer from '@components/global/ScreenContainer';
import { typography } from '@constants/typography';
import { NaverMapView, NaverMapMarker, NaverMapMarkerOverlay } from '@mj-studio/react-native-naver-map';import { useLocation } from '../../src/hooks/useLocation';
import { useEffect, useRef, useState } from 'react';

export default function Game_Main() {
    const router = useRouter();
    const { roomId, playerId, roomData } = useLocalSearchParams();
    const [location, setLocation] = useState(null);
    const { getCurrentCoords } = useLocation();
    const mapRef = useRef(null);
    useEffect(() => {
        (async () => {
            let coords = await getCurrentCoords();
            setLocation(coords);
        })();
    }, []);

    console.log(location);
    if (!location) {
        return (
        <View style={{ flex: 1, justifyContent: 'center', alignItems: 'center' }}>
            <Text>위치 정보를 가져오고 있습니다...</Text>
        </View>
        );
    }
    return (
        <ScreenContainer>
            <View>
                <NaverMapView
                    ref={mapRef}
                    style={{ width: '100%', height: '100%' }}
                    // 내 위치 표시 활성화
                    isMyLocationEnabled={true}
                    // 초기 카메라 위치 (내 위치 중심)
                    initialCamera={{
                        latitude: location.latitude,
                        longitude: location.longitude,
                        zoom: 15,
                    }}
                >
                    {/* 예시 마커: 내 위치에 핀 꽂기 */}
                <NaverMapMarkerOverlay
                    latitude={location.latitude}
                    longitude={location.longitude}
                    caption={{ text: "내 위치" }}
                    onTap={() => console.log("마커 클릭됨")}
                />
            </NaverMapView>
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