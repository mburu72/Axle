import { useState } from "react";
import Link from "next/link";
import { useRouter } from "next/router";
import toast, { Toaster } from "react-hot-toast";
import axiosInstance from "../../pages/api/axiosinstance";
import { HttpStatusCode } from "axios";

const OnboardingPage = () => {
  const [userType, setUserType] = useState("CLIENT");
  const [user, setUser] = useState({
    firstName: '',
    lastName: '',
    email: '',
    phoneNumber: '',
    password: '',
    idNumber: '', // For drivers
  });
  const [isLoading, setIsLoading] = useState(false);
  const [errors, setErrors] = useState({});
  const router = useRouter();

  const handleChange = (e) => {
    const { name, value } = e.target;
    setUser({ ...user, [name]: value });
  };

  const handleUserTypeChange = (type) => {
    setUserType(type);
    setUser({
      firstName: '',
      lastName: '',
      email: '',
      phoneNumber: '',
      password: '',
      idNumber: '',
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setIsLoading(true);
    setErrors({});

    const validationErrors = {};
    if (!user.firstName) validationErrors.firstName = "First name is required";
    if (!user.lastName) validationErrors.lastName = "Last name is required";
    if (!user.phoneNumber) validationErrors.phoneNumber = "Phone number is required";
    if (!user.password) validationErrors.password = "Password is required";

    if (userType === "HAULER" && !user.idNumber) {
      validationErrors.idNumber = "ID number is required for drivers";
    }

    if (Object.keys(validationErrors).length) {
      setErrors(validationErrors);
      toast.error("Please fill in all required fields!");
      setIsLoading(false);
      return;
    }

    try {
      let endpoint = userType === "CLIENT" ? "/api/v1/auth/register" : "/api/v1/auth/driver-register";
      const response = await axiosInstance.post(endpoint, user);

      if (response.status === HttpStatusCode.Ok) {
        toast.success("Please wait for the code");
        localStorage.setItem("email", user.email);
        const redirectPath = userType === "CLIENT" ? "/otp/client" : "/otp-verification/reg";
        setTimeout(() => router.push(redirectPath), 2000);
      } else {
        toast.error("Unexpected response from the server.");
      }
    } catch (error) {
      console.error(error);
      const errorMessage = error.response?.data?.message || "Network error or server did not respond.";
      toast.error(errorMessage);
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="h-screen flex flex-col">
      {/* Hero Area */}
      <div className="bg-blue-600 text-white p-10 flex flex-col justify-center items-center text-center">
        <h1 className="text-4xl font-bold mb-4">Join Our Community!</h1>
        <p className="mb-6">Whether you're a driver or a client, we have the perfect solution for you.</p>

      </div>

      <div className="flex-grow p-6 flex flex-col justify-center items-center bg-gradient-to-tl from-blue-50 to-white">
        <h2 className="text-3xl font-bold mb-4">Create Your Account</h2>
        <p className="mb-6">Select your role and fill in the details below.</p>

        <div className="flex space-x-4 mb-6">
          <button
            className={`p-2 rounded ${userType === "CLIENT" ? "bg-blue-600 text-white" : "bg-gray-200"}`}
            onClick={() => handleUserTypeChange("CLIENT")}
          >
            Client
          </button>
          <button
            className={`p-2 rounded ${userType === "HAULER" ? "bg-blue-600 text-white" : "bg-gray-200"}`}
            onClick={() => handleUserTypeChange("HAULER")}
          >
            Driver
          </button>
        </div>

        <form onSubmit={handleSubmit} className="w-full max-w-md">
          {["firstName", "lastName", "email", "phoneNumber", "password"].map((field, idx) => (
            <div key={idx} className="mb-2">
              <label className="capitalize block text-sm font-medium mb-1">
                {field.replace(/([A-Z])/g, " $1")}
              </label>
              <input
                type={field === "password" ? "password" : field === "email" ? "email" : field === "phoneNumber" ? "tel" : "text"}
                name={field}
                value={user[field]}
                onChange={handleChange}
                className={`focus:outline-none focus:ring-0 focus:border-blue-400 bg-transparent w-full mb-1 p-2 border-b ${
                  errors[field] ? "border-red-500" : "border-gray-300"
                }`}
                placeholder={`Enter your ${field}`}
                required={field !== "email"}
              />
              {errors[field] && <p className="text-red-500 text-sm">{errors[field]}</p>}
            </div>
          ))}

          {userType === "HAULER" && (
            <div className="mb-4">
              <label className="block text-sm font-medium mb-1">ID Number</label>
              <input
                type="text"
                name="idNumber"
                value={user.idNumber}
                onChange={handleChange}
                className={`w-full p-2 border-b focus:outline-none focus:border-blue-400 ${
                  errors.idNumber ? "border-red-500" : "border-gray-300"
                }`}
                placeholder="Enter your ID number"
                required
              />
              {errors.idNumber && <p className="text-red-500 text-sm">{errors.idNumber}</p>}
            </div>
          )}

          <button
            type="submit"
            className={`w-full flex justify-center bg-blue-600 text-white rounded-lg p-2 font-semibold ${
              isLoading ? "opacity-50 cursor-not-allowed" : ""
            }`}
            disabled={isLoading}
          >
            {isLoading ? "Signing up..." : "Sign Up"}
          </button>
        </form>

        <div className="mt-4 text-center">
          <span>Already have an account? </span>
          <Link href="/login" className="text-blue-700 font-bold">
            Login
          </Link>
        </div>

        <Toaster />
      </div>
    </div>
  );
};

export default OnboardingPage;
