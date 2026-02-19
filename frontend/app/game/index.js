import CustomInput from "@components/global/CustomInput";
import ScreenContainer from '@components/global/ScreenContainer';
import { colors } from '@constants/colors';
import { typography } from '@constants/typography';
import useRoomSocket from "@hooks/useRoomSocket";
import { useLocalSearchParams, useRouter } from 'expo-router';
import { useEffect, useState } from 'react';
import { StyleSheet, Text, View } from "react-native";
import MapView, { Circle, PROVIDER_GOOGLE } from 'react-native-maps';
import CustomBtn from '../../src/components/global/CustomBtn';
import CustomModal from '../../src/components/global/CustomModal';


export default function Game_Main() {
    const router = useRouter();
    const {roomId, playerId} = useLocalSearchParams();
    const { me, timeLeft, prisonerNumber, catchTheif, isOutOfBounds, roomData} = useRoomSocket(roomId, Number(playerId));
    const [map,setMap] = useState({centerLat:37.6358,
                                    centerLon:127.0710,
                                    mapRadius:null,
                                    prisonRadius:null})
    const [secondsLeft, setSecondsLeft] = useState(null);
    const [catchVisible, setCatchVisible] = useState(false);
    const [catchNumber, setCatchNumber] = useState(1);
    

    useEffect (()=>{ //roomData는 단 한번만 받으므로, 한번만 실행됨
        //const parsedData = roomData;
        console.log("📍 진짜 데이터 내용:", roomData);
        if (!roomData) return;
        setMap({centerLat:roomData?.centerLat,
            centerLon:roomData?.centerLon,
            mapRadius:roomData?.mapRadius,
            prisonRadius:roomData?.prisonRadius
        })
    },[roomData])

    useEffect (()=> {//상단 타이머
        if (roomData && timeLeft === 0){
        const parsedData = roomData;
        setSecondsLeft(parsedData?.timeLimit)}
    },[roomData,timeLeft])
    useEffect(() => {
        if (secondsLeft <= 0) return;

        const timerId = setInterval(() => {
        setSecondsLeft((prev) => prev - 1);
        }, 1000);

        return () => clearInterval(timerId);
    }, [secondsLeft]);
    const formatTime = (totalSeconds) => {
        const min = Math.floor(totalSeconds / 60);
        const sec = totalSeconds % 60;
        return `${min.toString().padStart(2, '0')}:${sec.toString().padStart(2, '0')}`;
    };

    useEffect(()=>{

    },[])
    

    if (!map.mapRadius) {
        return (
        <View style={{ flex: 1, justifyContent: 'center', alignItems: 'center' }}>
            <Text>위치 정보를 가져오고 있습니다...</Text>
        </View>
        );
    }
    return (
        <ScreenContainer>
            <View style={{ flex: 1}}>
                <Text style={[typography.title, { color: formatTime.min < 1 ? "red" : "#fff" }]}>
                    {formatTime(secondsLeft)}
                </Text>
            </View>
            <View style={{ flex: 9}}>
                <MapView
                    style={{ flex: 1}}
                    provider={PROVIDER_GOOGLE}
                    // 지도가 처음 켜졌을 때 보여줄 중심 위치
                    initialRegion={{
                    latitude: map.centerLat,
                    longitude: map.centerLon,
                    latitudeDelta: 0.01,
                    longitudeDelta: 0.01,
                    }}
                    // ★ 현재 내 위치를 지도에 파란색 점으로 표시
                    showsUserLocation={true}
                    // 내 위치를 찾는 버튼 활성화 (iOS/Android 상단에 표시됨)
                    showsMyLocationButton={true}
                    // 내 위치 추적 모드 (사용자의 이동에 따라 화면을 움직이고 싶다면 사용)
                    followsUserLocation={false} 
                >
                    <View>
                        {me.role==="THIEF" ? (
                            <View>
                                <Text>도둑</Text>
                                <Text>#{prisonerNumber}</Text>
                                <Text>{me?.nickname}</Text>
                            </View>
                        ) : (
                            <View>
                                <Text>경찰</Text>
                                <Text>{me?.nickname}</Text>
                            </View>
                        )}
                    </View>
                    <View>
                        {me.role==="THIEF" ? (
                            <View>
                            </View>
                        ) : (
                            <View style={[styles.section, styles.buttonSection]}>
                                <CustomBtn
                                    title="검거하기"
                                    onPress={() => {setCatchVisible(true)}}
                                />
                            </View>
                        )}
                    </View>
                    <Circle
                        center={{
                            latitude: map.centerLat,
                            longitude: map.centerLon,
                        }}
                        radius={map.prisonRadius} 
                        strokeColor="rgba(255, 0, 0, 0.7)"
                        fillColor="rgba(255, 0, 0, 0.2)"
                        strokeWidth={2}
                    />
                    <Circle
                        center={{
                            latitude: map.centerLat,
                            longitude: map.centerLon,
                        }}
                        radius={map.mapRadius} 
                        strokeColor="rgba(0, 150, 255, 0.7)"
                        fillColor="rgba(0, 150, 255, 0.2)"
                        strokeWidth={2}
                    />
                </MapView>
                <CustomModal
                    visible={catchVisible}
                >
                    <Text style={styles.subText}>도둑 개인 식별 번호 입력</Text>
                    <CustomInput
                        value={catchNumber}
                        onChangeText={setCatchNumber}
                        placeholder="# 0000"
                        style={{ marginBottom: 5 }}
                    />
                    <CustomBtn
                            title={"검거하기"}
                            onPress={() => {
                                catchTheif(catchNumber);
                                setCatchVisible(false);
                            }}
                    />
                    <CustomBtn
                            title={"닫기"}
                            onPress={() => {
                                setCatchVisible(false);
                            }}
                    />

                </CustomModal>
                <CustomModal
                    visible={isOutOfBounds}
                >
                    <Text style={styles.subText}>맵 범위를 벗어났습니다.</Text>
                    <Text style={styles.subText}>10초 이내로 복귀하지 않을 시 탈주로 간주됩니다.</Text>
                </CustomModal>
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