
import { useEffect, useState } from "react";
import JobCreationCard from "./JobCreationCard";

import Navbar from "../NavBar";
import AllJobs from "../driver/AllJobs";

const Home = () => {
    const [isModalOpen, setIsModalOpen] = useState(false);

    const toggleModal = () => {
        setIsModalOpen(!isModalOpen);
    };

    // Check if the modal should open based on local storage
    useEffect(() => {
        const shouldOpenModal = localStorage.getItem('openJobModal');
        if (shouldOpenModal) {
            setIsModalOpen(true); // Open the modal
            localStorage.removeItem('openJobModal'); // Remove the flag
        }
    }, []);

    return (
        <div>
            <Navbar />
           <AllJobs/>

            {isModalOpen && (
                <div className="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50 z-50">
                    <div className="bg-white rounded-lg shadow-lg p-6 md:w-1/2">
                        <JobCreationCard closeModal={toggleModal} /> 
                    </div>
                </div>
            )}
        </div>
    );
};

export default Home;


