import Link from 'next/link'; // Replace react-router-dom
import { useRouter } from 'next/router';
import axiosInstance from '../../pages/api/axiosinstance';
import { useState } from 'react';
import { HttpStatusCode } from 'axios';
import toast, { Toaster } from 'react-hot-toast';

const LoginPage = () => {
  const [user, setUser] = useState({ username: '', password: '' });
  const [isLoading, setIsLoading] = useState(false);
  const router = useRouter();

  const handleChange = (e) => {
    const { name, value } = e.target;
    setUser({ ...user, [name]: value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setIsLoading(true);

    // Basic validation
    if (!user.username || !user.password) {
      toast.error('Please fill in all fields!');
      setIsLoading(false);
      return;
    }

    try {
      const response = await axiosInstance.post('/api/v1/auth/login', user);
      if (response.status === HttpStatusCode.Ok) {
        localStorage.setItem('username', user.username);
        toast.success('Login successful!');

        setTimeout(() => {
          router.push('/otp/verifyotp');
        }, 2000);
      } else {
        toast.error('Unexpected response from the server.');
      }
    } catch (error) {
      const errorMessage = error.response?.data || 'Network error or server did not respond.';
      toast.error(errorMessage);
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div
      className="p-6 h-screen flex flex-col justify-center items-center bg-cover bg-center"
      style={{ backgroundImage: "url('/assets/truckcab.jpg')" }}
    >
      <div className="flex flex-col justify-center items-center bg-white rounded-lg shadow-lg p-6 w-full max-w-sm">
        <img src="assets/Axle_transparent.png" className="w-32 h-32" alt="Logo" />
        <h2 className="text-xl font-bold text-blue-800 text-center mb-4">Login to Your Account</h2>

        <form onSubmit={handleSubmit}>
          <div className="mb-4">
            <label className="block text-sm font-medium mb-1">Phone Number or Email</label>
            <input
              type="text"
              name="username"
              value={user.username}
              onChange={handleChange}
              className="w-full p-2 border-b border-gray-300 focus:outline-none focus:ring-0 focus:border-blue-300"
              placeholder="Enter your phone number or Email"
              required
            />
          </div>

          <div className="mb-4">
            <label className="block text-sm font-medium mb-1">Password</label>
            <input
              type="password"
              name="password"
              value={user.password}
              onChange={handleChange}
              className="w-full p-2 border-b border-gray-300 focus:outline-none focus:ring-0 focus:border-blue-300"
              placeholder="Enter your password"
              required
            />
          </div>

          <button
            type="submit"
            className={`w-full bg-blue-800 text-white font-bold py-2 rounded-lg transition ${
              isLoading ? 'opacity-50 cursor-not-allowed' : ''
            }`}
            disabled={isLoading}
          >
            {isLoading ? 'Logging in...' : 'Login'}
          </button>
        </form>

        <div className="text-right mt-2">
          <Link href="/forgot-password" className="text-blue-700">
            Forgot password?
          </Link>
        </div>

        <div className="mt-4 text-center">
          <span>New to Axle? </span>
          <Link href="/onboarding" className="text-blue-700 font-bold">
            Create account
          </Link>
        </div>
      </div>

      <Toaster />
    </div>
  );
};

export default LoginPage;
