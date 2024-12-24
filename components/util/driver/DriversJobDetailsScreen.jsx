import { useEffect, useState } from "react";
import { useParams } from "next/navigation";
import axiosInstance from "../../../pages/api/axiosinstance";
import QuoteCard from "./QuoteCard";
import Navbar from "../NavBar";
import QuotesJobIdList from "../QuoteJobIdList";
import Cookies from 'js-cookie'

const DriversJobDetailsScreen = () => {
    const { id } = useParams();
    const [job, setJob] = useState(null);
    const [isModalOpen, setIsModalOpen] = useState(false);
    const haulerId = Cookies.get('haulerId')

    const toggleModal = () => {
        setIsModalOpen(!isModalOpen);
    };
    useEffect(() => {
        const fetchJobDetails = async () => {
            try {
                const response = await axiosInstance.get(`/api/v1/jobs/${id}`);
                setJob(response.data);


            } catch (error) {
                console.log("Error fetching job", error);
            }
        };
        fetchJobDetails();
    }, [id]);

    if (!job) {
        return <div>Loading job details...</div>
    }
    return (
        <div className="bg-gray-100 min-h-screen">
             <Navbar />
             <div className="max-w-3xl mx-auto mt-8 p-6 bg-white rounded-lg shadow-md">
                <h1 className="text-3xl font-bold text-center text-blue-800 mb-4">Job Details</h1>
                <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                    <div className="flex justify-center items-center">
                        {job.imageUrl && (
                            <img
                                src={job.imageUrl}
                                alt={job.title}
                                className="rounded-lg shadow-md w-full max-w-xs"
                            />
                        )}
                    </div>
                    <div className="flex flex-col justify-between">
                        <p className="text-xl">
                            <span className="font-bold">From: </span>{job.pickupLocation}
                        </p>
                        <p className="text-xl">
                            <span className="font-bold">To: </span>{job.dropOffLocation}
                        </p>
                        <p className="text-xl">
                            <span className="font-bold">Details: </span>{job.cargoDetails}
                        </p>
                        <p className="text-xl">
                            <span className="font-bold">Deadline Date: </span>{job.executionDate}
                        </p>
                        <p className="text-xl">
                            <span className="font-bold">Created by: </span>{job.ownerDetails}
                        </p>
                    </div>
                </div>
                <div className="flex justify-center md:justify-end md:p-4">
                <div className=" active:shadow-md mt-4 w-1/2 flex justify-center bg-blue-800 rounded-xl p-2 md:w-1/4 mb-2">
                    <button type="button" onClick={toggleModal} className="text-center text-white font-bold"><span>Qoute this job</span></button>
                </div>
            </div>
            {isModalOpen && (
                <div className="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50 z-50">
                    <div className="bg-white rounded-lg shadow-lg p-6 md:w-1/2">
                        <QuoteCard closeModal={toggleModal} /> 
                    </div>

                </div>
            )}
            </div>
           <QuotesJobIdList id={id} haulerId={haulerId}/>
        </div>

    );
};
export default DriversJobDetailsScreen;