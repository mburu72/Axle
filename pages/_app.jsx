
import '../app/globals.css'

import {AuthProvider } from '../components/Auth/AuthContext'
import SplashScreen from '../components/SplashScreen';
import { useState } from 'react';
import { useEffect } from 'react';
function MyApp({ Component, pageProps }) {
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    // Simulate loading until all resources are loaded
    const handleLoad = () => setIsLoading(false);

    // Check if the page has already loaded
    if (document.readyState === 'complete') {
        handleLoad();
    } else {
        window.addEventListener('load', handleLoad);
    }
    const timer = setTimeout(() => setIsLoading(false), 2000); // 2 seconds
   
    return () => window.removeEventListener('load', handleLoad);
}, []);

if (isLoading) {
    return <SplashScreen />;
}
  return (
    <AuthProvider>
      <Component {...pageProps} />
    </AuthProvider>
  );
}

export default MyApp;
