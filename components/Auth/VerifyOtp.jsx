import { useState } from "react";
import toast, { Toaster } from "react-hot-toast";
import axiosInstance from "../../pages/api/axiosinstance";
import { useAuth } from "./AuthContext";
import { HttpStatusCode } from "axios";
import { jwtDecode } from "jwt-decode";

const VerifyOTP = () => {
  const { login } = useAuth();
  const [code, setCode] = useState("");
  const [loading, setLoading] = useState(false); // Add loading state

  // Get the phone number from local storage
  const username = localStorage.getItem("username");
  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!code) {
      toast.error("Otp required!");
      return; // Return early if no code is provided
    }

    setLoading(true); // Set loading to true when starting the request

    try {
      const response = await axiosInstance.post(`/api/v1/users/${username}/${code}/verify-otp`);

      if (response.status === HttpStatusCode.Ok) {
        login(response.data);      
        
        toast.success("Account created successfully!");
      } else {
        toast.error("Unexpected response from the server.");
      }
    } catch (error) {
      console.log(error);
      toast.error("An error occurred. Please try again later");
    } finally {
      setLoading(false); // Reset loading state regardless of success or error
    }
  };

  return (
    <form className="md:w-1/4 h-full m-auto mt-32" onSubmit={handleSubmit}>
      <div className="text-center mb-8">
        <label className="font-bold">OTP Verification</label>
        {/* Display the message showing where the OTP was sent */}
        <p className="text-sm text-gray-600">OTP sent to: <strong>{username}</strong></p>
        <input
          type="text"
          value={code}
          onChange={(e) => setCode(e.target.value)}
          className="focus:outline-none focus:ring focus:shadow-md bg-blue-100 w-full mb-2 p-2 rounded-lg"
          placeholder="OTP Code"
          disabled={loading} // Disable input while loading
        />
      </div>
      <div className="active:shadow-md mt-4 w-full flex justify-center bg-blue-800 rounded-xl p-2 md:w-1/2 md:mx-auto mb-2">
        <button type="submit" className="text-center text-white font-bold" disabled={loading}>
          {loading ? "Verifying..." : "Verify"} {/* Show loading text */}
        </button>
      </div>
      <span className="text-sm flex justify-center">Haven't received your OTP code? <a className="text-blue-700">Resend</a></span>
      <Toaster />
    </form>
  );
};

export default VerifyOTP;
