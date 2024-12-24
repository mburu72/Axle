import { useEffect, useState } from "react";
import axiosInstance from "../../../pages/api/axiosinstance";
import toast, { Toaster } from "react-hot-toast";
import { HttpStatusCode } from "axios";
import { useRouter } from "next/router";
import imageCompression from 'browser-image-compression';
import ImageModal from "../ImageModal";
import IndustryTextfield from "../IndustryTextfield";
const JobCreationCard = () => {
    const [fragileSelected, setFragileSelected] = useState(false);
  const [perishableSelected, setPerishableSelected] = useState(false);
    const [job, setJob] = useState({
        pickupLocation: '',
        dropOffLocation: '',
        isFragile: false,
        isPerishable: false,
        industry: '',
        budget: '',
        cargoDetails: '',
        executionDate: ''
    });
    const industries = [
        'Construction',
        'Agriculture',
        'Industrial',
        'Gas & Liquid',
        'Plastic',
        'Other'
    ]
    const handleRadioChange = () => {
        setFragileSelected((prev) => !prev);
        setJob((prevJob) => ({
            ...prevJob,
            isFragile: !fragileSelected, // Toggle isFragile based on the current state
        }));
    };
    const handlePerishableRadioChange = () => {
        setPerishableSelected((prev) => !prev);
        setJob((prevJob) => ({
            ...prevJob,
            isPerishable: !perishableSelected, // Toggle isPerishable based on the current state
        }));
    };
    
    const [client, setClient] = useState({
        firstName: '',
        lastName: '',
        phoneNumber: '',
        email: ''
    })
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [selectedImage, setSelectedImage] = useState('');
    const [images, setImages] = useState([]);
    const [imagePreviews, setImagePreviews] = useState([]);
    const [loading, setLoading] = useState(false);
    const [errors, setErrors] = useState({});
    
    const handleImageClick = (preview) => {
        setSelectedImage(preview);
        setIsModalOpen(true);
    };

const router = useRouter()
    useEffect(() => {
        const clearImages = () => {
            setImages([]);
            setImagePreviews([]);
        };
    }, []);

    useEffect(() => {
        // Create previews when images change
        if (images.length > 0) {
            const newPreviews = images.map(image => URL.createObjectURL(image));
            setImagePreviews(newPreviews);
            
            // Clean up function to revoke object URLs
            return () => {
                newPreviews.forEach(url => URL.revokeObjectURL(url));
            };
        } else {
            setImagePreviews([]); // Clear previews if no images
        }
    }, [images]);

    const handleChange = (key, value) => {
        
        setJob({
            ...job,
            [key]: value
        });
        setClient({
            ...client,
            [key]:value
        })
        setErrors((prev) => ({ ...prev, [name]: '' }));
    };

    const handleImageChange = async (e) => {
        const selectedFiles = Array.from(e.target.files);
        
        // Check if the user is trying to upload more than 3 images total
        if (selectedFiles.length + images.length > 3) {
            toast.error("You can only upload up to 3 images.");
            return;
        }
    
        const newImages = [];
    
        for (const file of selectedFiles) {
            try {
                const options = {
                    maxSizeMB: 2, // Maximum size in MB
                    maxWidthOrHeight: 1920, // Maximum width or height
                    useWebWorker: true,
                };
                const compressedImage = await imageCompression(file, options);
                newImages.push(compressedImage);
            } catch (error) {
                console.error("Error compressing image:", error);
                toast.error("Error compressing image.");
            }
        }
    
        // Update images and previews
        setImages((prev) => [...prev, ...newImages]);
        setImagePreviews((prev) => [
            ...prev,
            ...selectedFiles.map(file => URL.createObjectURL(file))
        ]);
    };
    
    
    const handleRemoveImage = (index) => {
        setImages((prev) => prev.filter((_, i) => i !== index));
        setImagePreviews((prev) => prev.filter((_, i) => i !== index));
    };
    
    const handleSubmit = async (e) => {
        console.log(client);
        
        e.preventDefault();
        setLoading(true);
    
        const newErrors = {};
    
        // Validate each field
        if (!job.pickupLocation) {
            newErrors.pickupLocation = 'Pickup location is required.';
        }
        if (!job.dropOffLocation) {
            newErrors.dropOffLocation = 'Drop-off location is required.';
        }
        if (!job.cargoDetails) {
            newErrors.cargoDetails = 'Cargo details are required.';
        }
        if (!job.executionDate) {
            newErrors.executionDate = 'Deadline date is required.';
        }
    
        if (images.length === 0) {
            newErrors.image = 'At least one image is required.';
        }
        
        if (job.budget && isNaN(job.budget)) {
            newErrors.budget = 'Budget must be a valid number.';
        }
        
        setErrors(newErrors);
    
        if (Object.keys(newErrors).length > 0) {
            setLoading(false);
            return;
        }
    
        try {
            const formData = new FormData();
            formData.append('client', new Blob([JSON.stringify(client)], { type: 'application/json' }));
            formData.append('job', new Blob([JSON.stringify({
                ...job,
                budget: job.budget.trim() === '' ? null : job.budget // Send null if budget is empty
            })], { type: 'application/json' }));
    
            // Append all images to FormData
            images.forEach((image) => {
                formData.append('image', image); // Append all images to the same key
            });
    
            const response = await axiosInstance.post(`/api/v1/jobs/new`, formData, {
                headers: {
                    'Content-Type': 'multipart/form-data',
                },
            });
    
            if (response.status === HttpStatusCode.Created) {
                toast.success("Job created successfully!");
                setTimeout(() => {
                    localStorage.setItem('email', client.email);
                    router.push("/otp/clientverify");
                }, 2000);
            } else {
                toast.error("Unexpected response from the server.");
            }
        } catch (err) {
            console.log(err);
            toast.error(err.response.data);
        } finally {
            setLoading(false);
        }
    };
    

    return (
        <div className="relative max-w-4xl mx-auto bg-white rounded-lg shadow-lg overflow-hidden p-6" style={{ backgroundImage: 'url(/path/to/your/background-image.jpg)', backgroundSize: 'cover', backgroundPosition: 'center' }}>
            <form onSubmit={handleSubmit}>
                <h1 className="text-center font-bold text-xl text-blue-800 mb-4">Tell Us What You'd Like Moved!</h1>

                <div className="flex flex-col  mb-4">
                <h1 className="font-bold text-yellow-900 md:ml-28">Client Info:</h1>
                    <div className=" bg-white rounded-lg py-2 p-2 md:p-2 flex md:w-3/4 md:m-auto flex-col md:flex-row flex-wrap items-center justify-between shadow shadow-lg">
                        
                <div className="mt-2">
                <label className="font-bold text-black">First name:</label>
                       <input 
                            type="text" 
                            value={client.firstName} 
                            name="firstName" 
                            onChange={(e) => handleChange('firstName', e.target.value)} 
                            className={`block p-1 rounded-md bg-white focus:outline-none ${errors.dropOffLocation ? 'border-red-500' : ''}`} 
                            placeholder="First Name" 
                        />
                    </div>
                    <div className="mt-2">
                    <label className="font-bold text-black">Last name:</label>
                        <input 
                            type="text" 
                            value={client.lastName} 
                            name="lastName" 
                            onChange={(e) => handleChange('lastName', e.target.value)} 
                            className={`block p-1 rounded-md bg-white focus:outline-none ${errors.dropOffLocation ? 'border-red-500' : ''}`} 
                            placeholder="Last Name" 
                        />
                    </div>
                    
                    <div className="flex flex-col mt-2">
                    <label className="font-bold text-black">Email:</label>
                      <input 
                            type="text" 
                            value={client.email} 
                            name="email" 
                            onChange={(e) => handleChange('email', e.target.value)} 
                            className={`block p-1 rounded-md bg-white focus:outline-none ${errors.dropOffLocation ? 'border-red-500' : ''}`} 
                            placeholder="Email" 
                        />
                    </div>
                    <div className="flex flex-col mt-2 mb-2">
                    <label className="font-bold text-black">Phone:</label>
                      <input 
                            type="text" 
                            value={client.phoneNumber} 
                            name="phoneNumber" 
                            onChange={(e) => handleChange('phoneNumber', e.target.value)} 
                            className={`block p-1 rounded-md bg-white focus:outline-none ${errors.dropOffLocation ? 'border-red-500' : ''}`} 
                            placeholder="Phone Number" 
                        />
                    </div>
                    </div>
                    <p className="font-bold text-yellow-900 mt-4">Job Info:</p>
                    <div className="flex flex-wrap mt-2">
                    <div className="mr-4">
                        <label className="font-bold text-gray-700">From:</label>
                        <input 
                            type="text" 
                            value={job.pickupLocation} 
                            name="pickupLocation" 
                            onChange={(e) => handleChange('pickupLocation', e.target.value)} 
                            className={`mt-1 block w- p-1 border rounded-lg bg-gray-100 focus:outline-none focus:ring focus:ring-blue-400 ${errors.pickupLocation ? 'border-red-500' : ''}`} 
                            placeholder="From" 
                        />
                    </div>
                    <div>
                        <label className="font-bold text-gray-700">To:</label>
                        <input 
                            type="text" 
                            value={job.dropOffLocation} 
                            name="dropOffLocation" 
                            onChange={(e) => handleChange('dropOffLocation', e.target.value)} 
                            className={`mt-1 block w- p-1 border rounded-lg bg-gray-100 focus:outline-none focus:ring focus:ring-blue-400 ${errors.dropOffLocation ? 'border-red-500' : ''}`} 
                            placeholder="Destination" 
                        />
                    </div>
                    </div>
                    <div>
                        <label className="font-bold text-gray-700">Industry:</label>
                           <IndustryTextfield options={industries} value={job.industry} onChange={(value) => handleChange('industry', value)}  className={`mt-1 block w- p-1 border rounded-lg bg-gray-100 focus:outline-none focus:ring focus:ring-blue-400 ${errors.dropOffLocation ? 'border-red-500' : ''}`} />
                    </div>
                </div>
                <div className="mb-4">
                    <label className="font-bold text-gray-700">Goods Details:</label>
                    <textarea 
                        name="cargoDetails" 
                        value={job.cargoDetails} 
                        onChange={(e) => handleChange('cargoDetails', e.target.value)} 
                        className={`mt-1 block w-full p-2 border rounded-lg bg-gray-100 focus:outline-none focus:ring focus:ring-blue-400 ${errors.cargoDetails ? 'border-red-500' : ''}`} 
                        placeholder="What are you moving" 
                    />
                  <div className="mb-2 mt-2 flex items-center align-center">
                <input
                  type="radio"
                  name="Fragile"
                  onClick={handleRadioChange}
                  checked={fragileSelected}
                  className="mr-4 align-middle "
                />
                <span className="align-middle text-gray-700">Fragile Goods</span>
              </div>
              <div className="mb-4 flex items-center align-center">
                <input
                  type="radio"
                  name="Perishable"
                  onClick={handlePerishableRadioChange}
                  checked={perishableSelected}
                  className="mr-4 align-middle"
                />
                <span className="align-middle text-gray-700">Perishable Goods</span>
              </div>
                </div>
                <div className="grid grid-cols-1 gap-4 md:grid-cols-2 mb-4">
                    <div>
                        <label className="font-bold text-gray-700">Deadline Date:</label>
                        <input 
                            type="date" 
                            name="executionDate" 
                            value={job.executionDate} 
                            onChange={(e) => handleChange('executionDate', e.target.value)} 
                            className={`block w-full p-1 border rounded-lg bg-gray-100 focus:outline-none focus:ring focus:ring-blue-400 ${errors.executionDate ? 'border-red-500' : ''}`} 
                        />
                    </div>
                    <div>
                        <label className="font-bold text-gray-700">Do you have a budget:</label>
                        <input 
                            type="text" 
                            name="budget" 
                            value={job.budget} 
                            onChange={(e) => handleChange('budget', e.target.value)} 
                            className={`block w-full p-1 border rounded-lg bg-gray-100 focus:outline-none focus:ring focus:ring-blue-400`} 
                        />
                    </div>
                    <div className="relative">
    <label className="font-bold text-gray-700 m-2">Upload Image (up to 3 images):</label>
    <div className={`flex justify-center items-center px-1 py-2 border-2 border-dashed rounded-lg mt-1 cursor-pointer ${errors.image ? 'border-red-500' : 'border-blue-400'}`}>
        {imagePreviews.length > 0 ? (
            <div className="flex overflow-x-auto space-x-1"> 
                {imagePreviews.map((preview, index) => (
                    <div key={index} className="relative"> 
                        <img 
                            src={preview} 
                            alt={`Preview ${index}`} 
                            className="rounded-lg w-full h-full object-contain cursor-pointer" // Use object-contain to maintain aspect ratio
                            onClick={() => handleImageClick(preview)} // Open modal on click
                        />
                        <button 
                            type="button" 
                            onClick={() => handleRemoveImage(index)} 
                            className="absolute top-2 right-2 bg-blue-800 text-white w-6 h-6 rounded-full p-1"
                        >
                            X
                        </button>
                    </div>
                ))}
            </div>
        ) : (
            <div className="flex flex-col justify-center items-center">
                <span className="text-4xl text-blue-500">+</span>
                <span className="text-gray-500">Add an image of what you're moving</span>
                <input 
                    type="file" 
                    onChange={handleImageChange} 
                    className="absolute inset-0 w-full h-full p-2 opacity-0 cursor-pointer" 
                    accept="image/*" 
                    multiple 
                />
            </div>
        )}
    </div>
    {errors.image && <p className="text-red-500 text-xs mt-1">{errors.image}</p>}
</div>



                </div>
                <div className="flex justify-between mt-16">
                    <button 
                        type="submit" 
                        className="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition"
                        disabled={loading}
                    >
                        {loading ? "Submitting..." : "Get Quotes"}
                    </button>
                </div>
                
                <Toaster />
            </form>
            <ImageModal 
                isOpen={isModalOpen} 
                onClose={() => setIsModalOpen(false)} 
                imageUrl={selectedImage} 
            />
        </div>
    );
};

export default JobCreationCard;
