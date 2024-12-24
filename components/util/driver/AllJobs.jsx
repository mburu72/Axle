import { useState, useEffect } from "react";
import axiosInstance from "../../../pages/api/axiosinstance";
import HomeJobCard from "../HomeJobCard";

const AllJobs = () => {
  const [jobs, setJobs] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchJobs = async () => {
      try {
        const response = await axiosInstance.get(`/api/v1/jobs`);
        setJobs(response.data);
      } catch (err) {
        console.error(err);
      } finally {
        setLoading(false);
      }
    };
    fetchJobs();
  }, []);

  if (loading) return <p className="text-center mt-4 text-lg">Loading...</p>;

  return (
    <div className="w-full flex justify-center px-4 ">
      {/* Container for mobile-first */}
      <div className="w-full max-w-4xl">
        <h1 className="text-center mt-4 text-xl sm:text-2xl font-bold text-gray-800">Available Deliveries</h1>

        <div className="mt-4 sm:mt-6 shadow shadow-md rounded-lg">
          {/* Map over jobs and display each one */}
          {jobs.length > 0 ? (
            jobs.map((job) => (
              <HomeJobCard key={job.id} job={job} />
            ))
          ) : (
            <p className="text-center text-gray-500">No deliveries available at the moment.</p>
          )}
        </div>
      </div>
    </div>
  );
};

export default AllJobs;
