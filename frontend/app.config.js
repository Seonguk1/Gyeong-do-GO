require('dotenv').config();

export default ({ config }) => {
  const expo = (config && config.expo) ? config.expo : {};

  return {
    ...config,
    expo: {
      ...expo,
      ios: {
        ...(expo.ios || {}),
        config: {
          ...((expo.ios && expo.ios.config) || {}),
          googleMapsApiKey: process.env.EXPO_PUBLIC_GOOGLE_MAPS_API_KEY,
        },
      },
      android: {
        ...(expo.android || {}),
        config: {
          ...((expo.android && expo.android.config) || {}),
          googleMaps: {
            ...((expo.android && expo.android.config && expo.android.config.googleMaps) || {}),
            apiKey: process.env.EXPO_PUBLIC_GOOGLE_MAPS_API_KEY,
          },
        },
      },
    },
  };
};
