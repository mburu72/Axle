import { useState } from "react";
import toast, { Toaster } from "react-hot-toast";
import axiosInstance from "../../pages/api/axiosinstance";
import { useAuth } from "./AuthContext";
import { HttpStatusCode } from "axios";

const ClientVerification = () => {
  const { login } = useAuth();
  const [otp, setOtp] = useState("");
  const [loading, setLoading] = useState(false);
  const [resendLoading, setResendLoading] = useState(false);

  // Get the phone number from local storage
  const email = localStorage.getItem('email');

  

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!otp) {
      toast.error("OTP required!");
      return;
    }

    setLoading(true);
    try {
      const response = await axiosInstance.post(`/api/v1/users/${email}/${otp}/verify-user`);
      if (response.status === HttpStatusCode.Ok) {
        login(response.data);
        
        toast.success("Account created successfully!");
      } else {
      
        toast.error("Unexpected response from the server.");
      }
    } catch (error) {
      console.error(error);
          
      toast.error("An error occurred. Please try again later.");
    } finally {
      setLoading(false);
    }
  };

  const handleResendOtp = async () => {
    setResendLoading(true);
    try {
      const response = await axiosInstance.post("/api/v1/users/resend-otp");
      if (response.status === HttpStatusCode.Ok) {
        toast.success("OTP code resent successfully!");
      } else {
        toast.error("Unexpected response from the server.");
      }
    } catch (error) {
      console.error(error);
      toast.error("An error occurred. Please try again later.");
    } finally {
      setResendLoading(false);
    }
  };

  return (
    <form className="md:w-1/4 h-full m-auto mt-32" onSubmit={handleSubmit}>
      <div className="text-center mb-8">
        <label className="font-bold">OTP Verification</label>
        {/* Display the message showing where the OTP was sent */}
        <p className="text-sm text-gray-600">OTP sent to: <strong>{email}</strong></p>
        <input
          type="text"
          value={otp}
          onChange={(e) => setOtp(e.target.value)}
          className="focus:outline-none focus:ring focus:shadow-md bg-blue-100 w-full mb-2 p-2 rounded-lg"
          placeholder="OTP Code"
          disabled={loading} // Disable input while loading
        />
      </div>
      <div className="active:shadow-md mt-4 w-full flex justify-center bg-blue-800 rounded-xl p-2 md:w-1/2 md:mx-auto mb-2">
        <button
          type="submit"
          className="text-center text-white font-bold"
          disabled={loading}
        >
          {loading ? (
            <span>Verifying...</span> // Updated loading text
          ) : (
            <span>Verify</span>
          )}
        </button>
      </div>
      <span className="text-sm flex justify-center">
        Haven't received your OTP code?{" "}
        <button
          type="button"
          className="text-blue-700 ml-2"
          onClick={handleResendOtp}
          disabled={resendLoading}
        >
          {resendLoading ? "Resending..." : "Resend"}
        </button>
      </span>
      <Toaster />
    </form>
  );
};

export default ClientVerification;
