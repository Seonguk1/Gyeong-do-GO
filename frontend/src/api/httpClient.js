const BASE_URL = 'http://10.50.46.99:8080';

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