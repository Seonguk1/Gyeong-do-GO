const BASE_URL = 'http://10.50.100.57:8080';

const httpClient= async (method, endPoint, data) => {
    const response = await fetch( `${BASE_URL+endPoint}` , {
        method: method,
        headers: {
        'Content-Type': 'application/json',
      },
        body: JSON.stringify(data)
    })
    const responseData = await response.json()
    return responseData;
}
export default httpClient;