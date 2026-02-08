// LocalTime 문자열("HH:mm:ss")을 받아서 현재 시간과의 차이(초)를 반환
export const getSecondsDiff = (timeString) => {
  if (!timeString) return 0;
  
  const now = new Date();
  const [hours, minutes, seconds] = timeString.split(':').map(Number);
  
  // 오늘 날짜에 해당 시간을 설정
  const target = new Date(now.getFullYear(), now.getMonth(), now.getDate(), hours, minutes, seconds);
  
  // (옵션) 만약 타겟 시간이 현재보다 과거라면, 내일로 간주해야 할 수도 있음 (밤 11:59 -> 00:00:05 상황 등)
  // 여기서는 단순하게 오늘 날짜 기준으로 계산
  
  const diffMs = target.getTime() - now.getTime();
  return Math.max(0, Math.ceil(diffMs / 1000)); // 밀리초 -> 초 변환 (음수 방지)
};