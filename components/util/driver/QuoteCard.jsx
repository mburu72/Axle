import { useEffect, useState } from "react";
import axiosInstance from "../../../pages/api/axiosinstance";
import toast, { Toaster } from "react-hot-toast";
import { HttpStatusCode } from "axios";
import { useParams } from "next/navigation";
import { useRouter } from "next/router";
import { useAuth } from "../../Auth/AuthContext"; // Assuming the AuthContext is in this path
import imageCompression from 'browser-image-compression';
import Cookies from 'js-cookie'
const QuoteCard = ({ closeModal }) => {
  const { id } = useParams();
  const [quote, setQuote] = useState({
    quoteAmount: '',
    executionDate: ''
  });
  const [image, setImage] = useState(null);
  const [imagePreviewUrl, setImagePreviewUrl] = useState('');
  const [errors, setErrors] = useState({
    quoteAmount: false,
    executionDate: false,
    image: false,
  });
  const router = useRouter();
  const { isAuthenticated, user } = useAuth(); // Use auth context for authentication state

  // Load saved quote data from localStorage (after login)
  useEffect(() => {
    const savedQuote = localStorage.getItem('savedQuote');
    if (savedQuote) {
      setQuote(JSON.parse(savedQuote)); // Restore saved quote data
      localStorage.removeItem('savedQuote'); // Clear the saved quote from storage
    }

    // Clear image preview since images can't be stored in localStorage
    setImage(null);
    setImagePreviewUrl('');
  }, []);

  // Handle image change and update preview URL
  useEffect(() => {
    if (image) {
      const url = URL.createObjectURL(image);
      setImagePreviewUrl(url);
      return () => URL.revokeObjectURL(url); // Clean up URL object on component unmount
    }
  }, [image]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setQuote({
      ...quote,
      [name]: value
    });
    setErrors({
      ...errors,
      [name]: false
    });
  };

  const handleImageChange = async (e) => {
    const selectedImage = e.target.files[0];
    if (selectedImage) {
        // Set the image before compression
        setErrors((prev) => ({ ...prev, image: '' }));

        // Compress the image
        try {
            const options = {
                maxSizeMB: 2, // Maximum size in MB
                maxWidthOrHeight: 1920, // Maximum width or height
                useWebWorker: true,
            };
            const compressedImage = await imageCompression(selectedImage, options);
            setImage(compressedImage);
        } catch (error) {
            console.error("Error compressing image:", error);
            toast.error("Error compressing image.");
        }
    }
};

  const handleSubmit = async (e) => {
    e.preventDefault();

    // If the user is not authenticated, store form data and redirect to login
    if (!isAuthenticated) {
      localStorage.setItem('savedQuote', JSON.stringify(quote));
      localStorage.setItem('redirectPath', window.location.pathname); // Save current path
      router.push("/login");
      return;
    }

    // Validate form fields
    const newErrors = {
      quoteAmount: !quote.quoteAmount,
      executionDate: !quote.executionDate,
      image: !image,
    };
    setErrors(newErrors);
    if (Object.values(newErrors).some((error) => error)) {
      toast.error("Please fill all the fields.");
      return;
    }

    const formData = new FormData();
    formData.append('quote', new Blob([JSON.stringify(quote)], { type: 'application/json' }));
    formData.append('image', image);
    const jobId = id;
    const haulerId = Cookies.get('haulerId');
    // Assuming you store haulerId in localStorage

    try {
      const response = await axiosInstance.post(`/api/quotes/${jobId}/${haulerId}/submit`, formData, {
        headers: {
          'Content-Type': 'multipart/form-data',
        },
      });
      if (response.status === HttpStatusCode.Ok) {
        toast.success("Quote submitted successfully!");
        setTimeout(() => {
          closeModal(); // Close the modal after successful submission
          localStorage.removeItem('savedQuote');
          localStorage.removeItem('redirectPath');
        }, 2000);
      } else {
        toast.error("Unexpected response from the server.");
      }
    } catch (err) {
      if (haulerId == null) {
        toast.error("Only drivers can quote jobs")
      }else{
        toast.error("Something went wrong!");
      }
      
    }
  };

  return (
    <div className="max-w-md mx-auto bg-white rounded-lg shadow-lg overflow-hidden h-full md:max-h-[80vh] md:overflow-y-auto">
      <form onSubmit={handleSubmit} className="p-6">
        <h1 className="text-center font-bold text-xl text-blue-800 mb-4">Quote</h1>
        <div className="text-left mb-4 md:mb-2">
          <label className="font-bold text-white mtext-blue-800">Quote Amount:</label>
          <input
            type="text"
            value={quote.quoteAmount}
            name="quoteAmount"
            onChange={handleChange}
            className={`focus:outline-none focus:ring focus:shadow-md bg-blue-100 w-full mb-2 p-2 rounded-lg ${errors.quoteAmount ? 'border-2 border-red-600' : ''}`}
            placeholder="Quote Amount"
          />
        </div>
        <div className="text-left mb-8 md:mb-2">
          <label className="font-bold text-white mdtext-blue-800">Date Available:</label>
          <input
            type="date"
            value={quote.executionDate}
            name="executionDate"
            onChange={handleChange}
            className={`focus:outline-none focus:ring focus:shadow-md bg-blue-100 w-full p-2 rounded-lg ${errors.executionDate ? 'border-2 border-red-600' : ''}`}
          />
        </div>
        <div className={`relative ${errors.image ? 'border-2 border-red-600' : ''}`}>
          <label className="font-bold text-gray-700">Upload Image:</label>
          <div className="flex justify-center items-center h-32 border-2 border-dashed border-blue-400 rounded-lg mt-1 cursor-pointer">
            {imagePreviewUrl ? (
              <img src={imagePreviewUrl} alt="Preview" className="rounded-lg w-full h-full object-cover" />
            ) : (
              <div className="flex flex-col justify-center items-center">
                <span className="text-4xl text-blue-500">+</span>
                <span className="text-gray-500">Photo of your vehicle</span>
                <input
                  type="file"
                  onChange={handleImageChange}
                  className="absolute inset-0 w-full h-full opacity-0 cursor-pointer"
                />
              </div>
            )}
          </div>
        </div>
        <div className="flex justify-between mt-6">
          <button
            type="button"
            className="px-4 py-2 text-red-600 border border-red-600 rounded-lg hover:bg-red-600 hover:text-white transition"
            onClick={closeModal}
          >
            Cancel
          </button>
          <button
            type="submit"
            className="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition"
          >
            Submit
          </button>
        </div>
        <Toaster />
      </form>
    </div>
  );
};

export default QuoteCard;
