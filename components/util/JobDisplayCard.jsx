import { useRouter } from "next/router";
import PropTypes from "prop-types";
/* eslint-disable react/prop-types */
// Utility function to calculate time ago
const timeAgo = (date) => {
  const now = new Date();
  const diffInMs = now - new Date(date);
  const diffInMinutes = Math.floor(diffInMs / (1000 * 60));

  if (diffInMinutes < 60) {
    return `${diffInMinutes} minutes ago`;
  } else {
    const diffInHours = Math.floor(diffInMinutes / 60);
    if (diffInHours > 1) {
      return `${diffInHours} hours ago`;
    }
    return `${diffInHours} hour ago`;
  }
};

const JobDisplayCard = ({ job }) => {
  const router = useRouter()

  const handleClick = () => {
    router.push(`/jobs/${job.id}`);
  };
  if(job.jobStatus == 'QUOTED'){
    return(
      <div 
      className="cursor-pointer bg-gray-400 border border-t-gray-300 p-2 sm:p-3 mb-0 flex flex-col md:flex-row sm:items-start sm:justify-between w-full md:w-full lg:w-full mx-auto"
    >
  
  
      {/* Job details */}
      <div className="flex-1">
        <div className="flex justify-between items-center mb-2">
          {/* Pickup and Dropoff locations */}
          <div>
            <h2 className="text-sm font-semibold text-gray-800">
              {job.pickupLocation} → {job.dropOffLocation}
            </h2>
            <p className="text-xs text-gray-500">{job.distance} Km</p>
          </div>
  
          {/* Price or Bid Info */}
          <div className="text-right">
          <h3 className="text-red-600 text-sm font-bold">
    {'Bidding closed'}
  </h3>
  
            <p className="text-xs text-gray-500">Posted {timeAgo(job.creationDate)}</p>
          </div>
        </div>
  
        {/* Cargo details and status */}
        <p className="text-xs text-gray-600 mt-1">
          <span className="font-semibold">Items:</span> {job.cargoDetails}
        </p>
        <p className="text-xs text-red-600 mt-1">Deadline: {job.executionDate}</p>
    
        
      </div>
    </div>
    )
  }

  return (
    <div 
      onClick={handleClick} 
      className="cursor-pointer bg-gray-200 border border-t-gray-300 p-2 sm:p-3 mb-0 shadow-sm hover:shadow-lg transition-shadow duration-300 flex flex-col md:flex-row sm:items-start sm:justify-between w-full md:w-full lg:w-full mx-auto"
    >
     <div className="flex justify-center items-center space-x-4">
  {job.imageUrls && job.imageUrls.length > 0 ? (
    <img
      src={job.imageUrls[0]} // Access the first image
      alt="Job Image" // Provide a descriptive alt text
      className="rounded-lg w-full max-w-xs h-24 object-cover sm:w-24 sm:mr-3 mb-2 sm:mb-0"
    />
  ) : (
    <img
      src="NO IMAGE" // Fallback image if no images are available
      alt="Placeholder"
      className="rounded-lg shadow-md w-full max-w-xs"
    />
  )}
</div>
      {/* Job details */}
      <div className="flex-1">
        <div className="flex justify-between items-center mb-2">
          {/* Pickup and Dropoff locations */}
          <div>
            <h2 className="text-sm font-semibold text-gray-800">
              {job.pickupLocation} → {job.dropOffLocation}
            </h2>
            <p className="text-xs text-gray-500">{job.distance} Km</p>
          </div>

          {/* Price or Bid Info */}
          <div className="text-right">
          <h3 className="text-green-600 text-sm font-bold">
    {job.budget && !isNaN(job.budget) ? `Ksh ${job.budget}` : 'Open for bids'}
</h3>

            <p className="text-xs text-gray-500">Posted {timeAgo(job.creationDate)}</p>
          </div>
        </div>

        {/* Cargo details and status */}
        <p className="text-xs text-gray-600 mt-1">
          <span className="font-semibold">Items:</span> {job.cargoDetails}
        </p>
        <p className="text-xs text-red-600 mt-1">Deadline: {job.executionDate}</p>
        <p className="text-xs text-green-500 mt-1">Status: {job.jobStatus}</p>
        
      </div>
    </div>
  );
};

JobDisplayCard.PropTypes = {
  job: PropTypes.shape({
    imageUrls: PropTypes.arrayOf(PropTypes.string),
    pickupLocation: PropTypes.string.isRequired,
    dropOffLocation: PropTypes.string.isRequired,
    distance: PropTypes.number.isRequired,
    budget: PropTypes.number.isRequired,
    creationDate: PropTypes.string,
    cargoDetails: PropTypes.string,
    executionDate: PropTypes.string,
    jobStatus: PropTypes.string,
  }).isRequired,
}

export default JobDisplayCard;
