const buildBaseUrl = () => {
  if (process.env.EXPO_PUBLIC_API_BASE_URL) return process.env.EXPO_PUBLIC_API_BASE_URL;

  const useLocal = process.env.EXPO_PUBLIC_USE_LOCALHOST === 'true';
  const host = useLocal ? (process.env.EXPO_PUBLIC_LOCALHOST_IP || 'localhost') : (process.env.EXPO_PUBLIC_SERVER_IP || '127.0.0.1');
  const port = process.env.EXPO_PUBLIC_API_PORT || '8080';

  return `http://${host}`;
};

const BASE_URL = buildBaseUrl();

const httpClient = async (method, endPoint, data) => {
    const url = `${BASE_URL}${endPoint}`;
    console.debug('[httpClient] request URL:', url);
    try {
        const response = await fetch(url, {
            method: method,
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(data)
        });

        if (!response.ok) {
            const text = await response.text().catch(() => null);
            const err = new Error(`HTTP ${response.status} ${response.statusText} - ${text}`);
            err.status = response.status;
            throw err;
        }

        const responseData = await response.json().catch(() => null);
        return responseData;
    } catch (e) {
        console.error('[httpClient] Network error:', e.message || e);
        throw e;
    }
}
export default httpClient;