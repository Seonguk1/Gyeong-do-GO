const palette = {
  navyDark: '#0B162C',
  navyLight: '#162544',
  white: '#FFFFFF',
  grey100: '#CCCCCC',
  grey500: '#999999',
  redError: '#FF4444',
};

// 시맨틱: 실제 코드에서 사용할 역할 이름
export const colors = {
  // 배경 관련
  background: palette.navyDark,
  surface: palette.navyLight, // 카드나 모달 배경 등

  // 텍스트 관련
  textMain: palette.white,
  textSub: palette.grey100,
  textPlaceholder: palette.grey500,

  // 버튼/기능 관련
  btnPrimary: palette.white,    // 메인 버튼 배경
  btnPrimaryText: palette.navyDark, // 메인 버튼 글자

  btnSecondary: palette.navyLight,
  
  // 상태
  error: palette.redError,
};