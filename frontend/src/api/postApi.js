const BASE_URL = 'http://10.50.46.99:8080';

const postApi = async (data, endPoint) => {
  try {
    const response = await fetch(`${BASE_URL}/api/${endPoint}`, {
      method: 'POST', 
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(data), 
    });
    // console.log(response);
    // console.log(response.ok);

    if (!response.ok) {
      const errorData = await response.json();
      console.log('서버 에러 상세:', errorData);
      throw new Error(errorData.message || '방 생성 중 오류 발생');
    }
    const responseData = await response.json();
    if (responseData.result == "ERROR"){

    }
    return responseData;
  } catch (error) {
    console.error('API 호출 에러:', error.message);
    throw error;
  }
};

export default postApi;