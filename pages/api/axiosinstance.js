import axios from "axios";
import { jwtDecode } from 'jwt-decode';
import Cookies from 'js-cookie';

const axiosInstance = axios.create({
  //baseURL: "http://localhost:8080",
  baseURL: "https://axle-ke.co.ke",
});

const isTokenExpired = (token) => {
  try {
    const decoded = jwtDecode(token);
    return decoded.exp * 1000 < Date.now();
  } catch (error) {
    console.log(error);
    
    return true;
  }
};

// Request Interceptor
axiosInstance.interceptors.request.use(
  (config) => {
    const openEndPoints = [
      "/api/v1/auth/register",
      "/api/v1/auth/driver-register",
      "/api/v1/auth/login",
      "/api/v1/auth/driver-login",
      "/api/v1/auth/refreshToken",
    ];
    const isVerifyOtpEndpoint = /\/api\/v1\/users\/\d+\/\w+\/verify-otp$/.test(config.url);
    const regOtpEndpoint = /\/api\/v1\/users\/\w+\/registration$/.test(config.url);
    const verifyClient = /\/api\/v1\/users\/\d+\/\w+\/verify-user$/.test(config.url);
    const clientVerify = /\/api\/v1\/users\/\w+\/client$/.test(config.url);

    if (!openEndPoints.includes(config.url) && !isVerifyOtpEndpoint && !regOtpEndpoint && !verifyClient && !clientVerify) {
      const token = Cookies.get("token"); // Get the token from cookies
      if (token && !isTokenExpired(token)) {
        config.headers.Authorization = `Bearer ${token}`;
        Cookies.set('subj', jwtDecode(token).sub); // Set subject in cookies if needed
      }
    }
    return config;
  },
  (error) => Promise.reject(error)
);

export default axiosInstance;
