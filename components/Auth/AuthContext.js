import { createContext, useContext, useState, useEffect } from 'react';
import { useRouter } from 'next/router';
import { jwtDecode } from 'jwt-decode'; 
import axiosInstance from '@/pages/api/axiosinstance';
import Cookies from 'js-cookie';
const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
    const [isAuthenticated, setIsAuthenticated] = useState(false);
    const [userAuth, setUserAuth] = useState(null);
    const [user, setUser] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [showPasswordModal, setShowPasswordModal] = useState(false);
    const router = useRouter();

    const login = (token) => {
        Cookies.set('token', token.jwtToken, { expires: 7, secure: true, sameSite: 'Strict' });
        Cookies.set('refreshToken', token.refreshToken, { expires: 7, secure: true, sameSite: 'Strict' });
        Cookies.set('tokenId', token.tokenId, { expires: 7, secure: true, sameSite: 'Strict' });


        const decoded = jwtDecode(token.jwtToken);
        setUserAuth(decoded);
        setIsAuthenticated(true);


        if (decoded.role === "CLIENT") {
            Cookies.set('clientId', decoded.id, { expires: 7, secure: true, sameSite: 'Strict' });
            router.push(`/dashboard/${decoded.id}`);
        } else if (decoded.role === "HAULER") {
            Cookies.set('haulerId', decoded.id, { expires: 7, secure: true, sameSite: 'Strict' });
            router.push(`/drivers-dashboard/${decoded.id}`);
        }
        

        fetchUser();

        const redirectPath = localStorage.getItem('redirectPath');
        if (redirectPath) {
            localStorage.removeItem('redirectPath');
            router.push(redirectPath);
        }
    };

    const fetchUser = async () => {
        try {
            const response = await axiosInstance.get('/api/v1/users/me');
            setUser(response.data);

            // Check if password is null to show the password modal
            if (response.data.password === null) {
                setShowPasswordModal(true);
            }
        } catch (err) {
            setError(err);
            console.error("Error fetching user details:", err);
        } finally {
            setLoading(false);
        }
    };

    const logout = async () => {
        const tokenId = Cookies.get('tokenId');
    
        // Remove cookies
        Cookies.remove('token');
        Cookies.remove('refreshToken');
        Cookies.remove('tokenId');
        Cookies.remove('clientId');
        Cookies.remove('haulerId');
    
        setIsAuthenticated(false);
        setUserAuth(null);
        setUser(null);
    
        try {
            await axiosInstance.post(`/api/v1/auth/logout/${tokenId}`);
            router.push('/');
        } catch (error) {
            console.error("Error during logout:", error);
        }
    };
    

    useEffect(() => {
        let refreshInterval;
    
        if (isAuthenticated && userAuth) {
            const expiresIn = userAuth.exp * 1000 - Date.now() - 60 * 1000; // Refresh 1 minute before expiration
    
            if (expiresIn > 0) {
                refreshInterval = setTimeout(async () => {
                    const refreshToken = Cookies.get('refreshToken');
                    const tokenId = Cookies.get('tokenId');
    
                    if (refreshToken) {
                        try {
                            const response = await axiosInstance.post('/api/v1/auth/refreshToken', {
                                token: refreshToken,
                                tokenId: tokenId,
                            });
                            const newToken = response.data.jwtToken;
                            const newRefreshToken = response.data.refreshToken;
    
                            // Store new tokens in cookies
                            Cookies.set('token', newToken, { secure: true, sameSite: 'Strict' });
                            Cookies.set('refreshToken', newRefreshToken, { secure: true, sameSite: 'Strict' });
    
                            setUserAuth(jwtDecode(newToken));
                        } catch (error) {
                            console.error("Error refreshing token:", error);
                            logout();
                        }
                    }
                }, expiresIn);
            }
        }
    
        return () => clearTimeout(refreshInterval);
    }, [isAuthenticated, userAuth]);
    

    useEffect(() => {
        const token = Cookies.get('token');
        if (token) {
            try {
                const decoded = jwtDecode(token);
                const currentTime = Date.now() / 1000;
    
                if (decoded.exp < currentTime) {
                    logout();
                } else {
                    setUserAuth(decoded);
                    setIsAuthenticated(true);
                    fetchUser();
                }
            } catch (error) {
                console.error('Error decoding token or invalid token', error);
                logout();
            }
        } else {
            setLoading(false);
        }
    }, []);
    

    return (
        <AuthContext.Provider value={{ isAuthenticated, userAuth, user, loading, error, login, logout, showPasswordModal, setShowPasswordModal }}>
            {loading ? <div>Loading...</div> : children}
        </AuthContext.Provider>
    );
};

// Custom hook to use the AuthContext
export const useAuth = () => useContext(AuthContext);
