import { useEffect, useState } from "react";
import axiosInstance from "../../../pages/api/axiosinstance";
/* eslint-disable react/prop-types */
const DriverProfile = () => {
    const [user, setUser] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);


    useEffect(() => {
        const fetchUser = async () => {
            try {
                const response = await axiosInstance.get('/api/v1/users/me');
                setUser(response.data);
            } catch (err) {
                setError(err);
                console.error("Error fetching user details:", err);
            } finally {
                setLoading(false);
            }
        };

        fetchUser();
    }, []); // Empty array ensures this only runs once on mount

    // Loading state
    if (loading) {
        return (
            <div className="flex items-center justify-center h-screen">
                <div className="animate-spin rounded-full h-16 w-16 border-t-4 border-blue-500"></div>
            </div>
        );
    }

    // Error state
    if (error) {
        return (
            <div className="flex items-center justify-center h-screen">
                <div className="bg-red-500 text-white p-4 rounded shadow-md">
                    <p>Error fetching user details: {error.message}</p>
                </div>
            </div>
        );
    }

    // User details
    return (
        <div className="max-w-md mx-auto bg-white shadow-lg rounded-lg p-6 mt-6">
            <h2 className="text-2xl font-bold text-teal-300 mb-4">
                Welcome, {user?.firstName} {user?.lastName}!
            </h2>
            <p className="text-teal-300 mb-2"><strong>Phone:</strong> {user?.phoneNumber}</p>
            <p className="text-teal-300 mb-2"><strong>Type:</strong> {user?.role}</p>
            {/* Add more user fields as necessary */}
        </div>
    );
}

export default DriverProfile;
