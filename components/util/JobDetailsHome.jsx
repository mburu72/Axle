import { useEffect, useState } from "react";
import { useParams } from "next/navigation";
import axiosInstance from "../../pages/api/axiosinstance";
import QuoteCard from "../util/driver/QuoteCard";
import Navbar from "./NavBar";

const JobDetailsHome = () => {
  const { id } = useParams();
  const [job, setJob] = useState(null);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [currentIndex, setCurrentIndex] = useState(0);

  // Function to go to the previous image
  const prevImage = () => {
    setCurrentIndex((prevIndex) =>
      prevIndex === 0 ? job.imageUrls.length - 1 : prevIndex - 1
    );
  };

  // Function to go to the next image
  const nextImage = () => {
    setCurrentIndex((prevIndex) =>
      prevIndex === job.imageUrls.length - 1 ? 0 : prevIndex + 1
    );
  };

  const toggleModal = () => {
    setIsModalOpen(!isModalOpen);
  };

  useEffect(() => {
    const fetchJobDetails = async () => {
      try {
        const response = await axiosInstance.get(`/api/v1/jobs/${id}`);
        setJob(response.data);
      } catch (error) {
        // Handle error
      }
    };
    fetchJobDetails();
  }, [id]);

  if (!job) {
    return <div>Loading job details...</div>;
  }

  return (
    <div className="bg-gray-100 min-h-screen">
      <Navbar />
      <div className="max-w-8xl mx-auto p-6 bg-white rounded-lg shadow-md">
        <h1 className="text-3xl font-bold text-center text-green-700 mb-4">Delivery Summary</h1>
        <div className="grid grid-cols-1 gap-6">
          <div className="flex justify-center flex-col md:w-1/2 w-full m-auto">
            <div className="flex">
              <p className="text-2xl font-bold">
                <span className="text-gray-500 text-sm">From: </span>{job.pickupLocation}
              </p>
              &nbsp; &nbsp;
              <p className="text-2xl font-bold">
                <span className="text-gray-500 text-sm">To: </span>{job.dropOffLocation}
              </p>
            </div>
            <div className="shadow-lg p-2 rounded-sm">
              <h1 className="font-bold text-md text-green-500">Trip summary</h1>
              <p className="text-sm">
                <span className="font-bold">Deadline Date: </span>{job.executionDate}
              </p>
              <p className="text-sm">
                <span className="font-bold">Created by: </span>{job.ownerDetails}
              </p>
            </div>
          </div>
          <div className="flex justify-center items-center space-x-4 relative">
            {job.imageUrls && job.imageUrls.length > 0 ? (
              <div className="relative">
                {/* Left Arrow */}
                {job.imageUrls.length > 1 && (
                  <button
                    onClick={prevImage}
                    className="absolute left-0 top-1/2 transform -translate-y-1/2 bg-transparent text-white p-2 rounded-full"
                  >
                    &lt;
                  </button>
                )}

                {/* Image with smooth opacity transition */}
                <div
                  className="transition-opacity duration-500 ease-in-out"
                  style={{ opacity: 1 }}
                >
                  <img
                    key={currentIndex}
                    src={job.imageUrls[currentIndex]}
                    alt={`Image ${currentIndex + 1}`}
                    className="rounded-lg shadow-md w-full max-w-xs"
                  />
                </div>

                {/* Right Arrow */}
                {job.imageUrls.length > 1 && (
                  <button
                    onClick={nextImage}
                    className="absolute right-0 top-1/2 transform -translate-y-1/2 bg-transparent text-white p-2 rounded-full"
                  >
                    &gt;
                  </button>
                )}
              </div>
            ) : (
              <img
                src="/path/to/placeholder.jpg"
                alt="Placeholder"
                className="rounded-lg shadow-md w-full max-w-xs"
              />
            )}
          </div>

          <div className="flex justify-center flex-col md:w-1/2 w-full m-auto shadow-lg p-2 rounded-sm">
            <h1 className="font-bold text-md text-green-500">Details</h1>
            <p className="text-xl">{job.cargoDetails}</p>
            <p className="text-sm">
              <span className="font-bold">Industry: </span>{job.industry}
            </p>
            {(job.isPerishable || job.isFragile) && (
              <p className="text-sm">
                <span className="font-bold">Type: </span>
                {job.isPerishable && job.isFragile
                  ? 'Perishable & Fragile'
                  : job.isPerishable
                  ? 'Perishable'
                  : 'Fragile'}
              </p>
            )}
          </div>
        </div>

        <div className="flex justify-center md:justify-end md:p-4">
          <div className="active:shadow-md mt-4 w-1/2 flex justify-center bg-blue-800 rounded-xl p-2 md:w-1/4 mb-2">
            <button type="button" onClick={toggleModal} className="text-center text-white font-bold">
              <span>Quote this job</span>
            </button>
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
    </div>
  );
};

export default JobDetailsHome;
