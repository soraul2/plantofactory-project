// frontend/src/api/axiosInstance.js

import axios from 'axios';

//1. axios 기본 인스턴스 생성
const axiosInstance = axios.create({
    baseUrl: '/',
    timeout: 5000,
});


//2. [핵심] 요청 인터셉터(Interceptor) 설정
// 모든 요청이 서버로 가기 전에 이 코드를 거치게 됩니다.
axiosInstance.interceptors.request.use(
    (config) => {
        //3. localStorage에서 저장된 토큰을 가져옵니다.
        const token = localStorage.getItem('accessToken');

        //4, 토큰이 존재하면, 모든 요청 헤더에 'Authorization'을 첨부합니다.
        if(token){
            config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

export default axiosInstance;
