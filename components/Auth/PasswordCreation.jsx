import { useState } from "react";
import toast, { Toaster } from "react-hot-toast";
import axiosInstance from "../../pages/api/axiosinstance";
import { useAuth } from "./AuthContext";
import { HttpStatusCode } from "axios";

const PasswordCreation = ({ isOpen, onClose }) => {
  const { login } = useAuth();
  const [newPassword, setNewPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [loading, setLoading] = useState(false);

  const username = localStorage.getItem('email');
  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!newPassword || !confirmPassword) {
      toast.error("Password and confirmation are required!");
      return;
    }
    if (newPassword !== confirmPassword) {
      toast.error("Passwords do not match!");
      return;
    }

    setLoading(true);

    try {
      const response = await axiosInstance.post(`/api/v1/users/setup-password`, {
        email: username,
        newPassword,
      });

      // Check the response status to handle different server responses
      if (response.status === HttpStatusCode.Ok) {
        toast.success("Password created successfully! You can now log in.");
        onClose(); // Close modal on success
      } 
    } catch (error) {
      console.error(error);
      if (error.response?.status === HttpStatusCode.NotFound) {
        toast.error("User not found");
      } else {
        toast.error("Failed to create password. Please try again later.");
      }
    } finally {
      setLoading(false);
    }
  };

  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black bg-opacity-50">
      <div className="bg-white rounded-lg shadow-lg p-6 w-full max-w-md mx-4">
        <div className="flex justify-between items-center mb-6">
          <h2 className="text-xl font-bold">Create Password</h2>
          <button onClick={onClose} className="text-gray-500 hover:text-gray-800">&times;</button>
        </div>

        <form onSubmit={handleSubmit}>
          <input
            type="password"
            value={newPassword}
            onChange={(e) => setNewPassword(e.target.value)}
            className="focus:outline-none focus:ring focus:shadow-md bg-blue-100 w-full mb-2 p-2 rounded-lg"
            placeholder="New Password"
            disabled={loading}
          />
          <input
            type="password"
            value={confirmPassword}
            onChange={(e) => setConfirmPassword(e.target.value)}
            className="focus:outline-none focus:ring focus:shadow-md bg-blue-100 w-full mb-2 p-2 rounded-lg"
            placeholder="Confirm Password"
            disabled={loading}
          />

          <div className="mt-4">
            <button type="submit" className="w-full bg-blue-800 text-white font-bold py-2 rounded-lg" disabled={loading}>
              {loading ? "Please wait..." : "Create Password"}
            </button>
          </div>
        </form>
        <Toaster />
      </div>
    </div>
  );
};

export default PasswordCreation;
