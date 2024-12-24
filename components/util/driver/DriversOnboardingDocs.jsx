import React, { useState } from "react";

const DriversOnboardingDocs = () => {
    const [frontID, setFrontID] = useState(null);
    const [backID, setBackID] = useState(null);
    const [frontDL, setFrontDL] = useState(null);
    const [backDL, setBackDL] = useState(null);
    const [passportPhoto, setPassportPhoto] = useState(null);
const driver = localStorage.getItem('driver')
console.log(driver.toString());

    const handleFileChange = (event, setter) => {
        const file = event.target.files[0];
        if (file) {
            setter(file);
        }
    };

    const handleSubmit = () => {
        const formData = new FormData();
        formData.append("frontID", frontID);
        formData.append("backID", backID);
        formData.append("frontDL", frontDL);
        formData.append("backDL", backDL);
        formData.append("passportPhoto", passportPhoto);

        // Submit formData to backend
        console.log("Files ready for submission", formData);
    };

    return (
        <div className="flex justify-center items-center bg-gray-300 min-h-screen">
            <div className="w-full sm:w-11/12 lg:w-2/3 xl:w-1/2 p-6 bg-white shadow-md rounded-md">
                <h1 className="text-3xl font-bold text-center text-blue-700 mb-6">
                    Verification
                </h1>
                <p className="text-center text-gray-700 mb-8">
                    The information you provide here will be used for verification purposes only; it won't be public.
                </p>

                {/* ID Card Upload */}
                <section className="mb-6">
                    <p className="font-semibold text-gray-800 mb-2">Upload your ID card:</p>
                    <div className="flex flex-col md:flex-row gap-4">
                        <label className="flex flex-col w-full">
                            <span className="text-sm font-medium text-gray-700">Front Side</span>
                            <input
                                type="file"
                                onChange={(e) => handleFileChange(e, setFrontID)}
                                className="border border-gray-300 rounded-md p-2 text-sm"
                                accept="image/*"
                            />
                        </label>
                        <label className="flex flex-col w-full">
                            <span className="text-sm font-medium text-gray-700">Back Side</span>
                            <input
                                type="file"
                                onChange={(e) => handleFileChange(e, setBackID)}
                                className="border border-gray-300 rounded-md p-2 text-sm"
                                accept="image/*"
                            />
                        </label>
                    </div>
                </section>

                {/* Driver's License Upload */}
                <section className="mb-6">
                    <p className="font-semibold text-gray-800 mb-2">Upload your Driver's License:</p>
                    <div className="flex flex-col md:flex-row gap-4">
                        <label className="flex flex-col w-full">
                            <span className="text-sm font-medium text-gray-700">Front Side</span>
                            <input
                                type="file"
                                onChange={(e) => handleFileChange(e, setFrontDL)}
                                className="border border-gray-300 rounded-md p-2 text-sm"
                                accept="image/*"
                            />
                        </label>
                        <label className="flex flex-col w-full">
                            <span className="text-sm font-medium text-gray-700">Back Side</span>
                            <input
                                type="file"
                                onChange={(e) => handleFileChange(e, setBackDL)}
                                className="border border-gray-300 rounded-md p-2 text-sm"
                                accept="image/*"
                            />
                        </label>
                    </div>
                </section>

                {/* Passport Photo Upload */}
                <section className="mb-6">
                    <p className="font-semibold text-gray-800 mb-2">Upload a Passport Size Photo:</p>
                    <label className="flex flex-col w-full">
                        <span className="text-sm font-medium text-gray-700">Passport Photo</span>
                        <input
                            type="file"
                            onChange={(e) => handleFileChange(e, setPassportPhoto)}
                            className="border border-gray-300 rounded-md p-2 text-sm"
                            accept="image/*"
                        />
                    </label>
                </section>

                <button
                    onClick={handleSubmit}
                    className="w-full bg-blue-600 text-white py-2 px-4 rounded-md hover:bg-blue-700 transition"
                >
                    Submit
                </button>
            </div>
        </div>
    );
};

export default DriversOnboardingDocs;
