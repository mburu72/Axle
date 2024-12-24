import { useEffect, useState } from "react";
import { useParams } from "next/navigation";
import axiosInstance from "../../../pages/api/axiosinstance";
import toast, { Toaster } from "react-hot-toast";

import { HttpStatusCode } from "axios";

const QuoteDetailsScreen = () => {
    const { id } = useParams();
    const [quote, setQuote] = useState(null);
    const [isLoading, setIsLoading] = useState(false); // New state for loading
   

    const getStatusColor = () => {
        switch (quote.status) {
            case 'ACCEPTED':
                return 'green';
            case 'REJECTED':
                return 'red';
            default:
                return 'white';
        }
    };

    useEffect(() => {
        const fetchQuoteDetails = async () => {
            try {
                const response = await axiosInstance.get(`/api/quotes/${id}`);
                setQuote(response.data);
            } catch (error) {
                console.log("Error fetching quote", error);
                toast.error("Failed to load quote details.");
            }
        };
        fetchQuoteDetails();
    }, [id]);

    const handleAccept = async () => {
        setIsLoading(true); // Start loading
        try {
            const response = await axiosInstance.put(`/api/quotes/${id}/accept`);
            if (response.status === HttpStatusCode.Ok) {
                toast.success("Quote Accepted!!");
                // Optionally, you might want to navigate away or refresh quote details
            } else {
                toast.error("Unexpected response from the server.");
            }
        } catch (error) {
            if (error.response && error.response.data) {
                toast.error(error.response.data.message || "Error accepting quote.");
            } else {
                toast.error("Unexpected response from the server.");
            }
        } finally {
            setIsLoading(false); // Stop loading
        }
    };

    const handleReject = async () => {
        setIsLoading(true); // Start loading
        try {
            const response = await axiosInstance.put(`/api/quotes/${id}/reject`);
            if (response.status === HttpStatusCode.Ok) {
                toast.success("Quote Rejected!");
                // Optionally, you might want to navigate away or refresh quote details
            } else {
                toast.error("Unexpected response from the server.");
            }
        } catch (error) {
            if (error.response && error.response.data) {
                toast.error(error.response.data.message || "Error rejecting quote.");
            } else {
                toast.error("Unexpected response from the server.");
            }
        } finally {
            setIsLoading(false); // Stop loading
        }
    };

    if (!quote) {
        return <div>Loading quote details...</div>;
    }

    return (
        <div>
            <h1 className="p-2 bg-blue-800 text-white text-center text-2xl font-bold">Quote Details</h1>
            <div className="grid grid-cols-1 gap-y-4 gap-x-4 md:grid-cols-2 p-4 shadow-lg">
                <div key={quote.id}>
                    {quote.imageUrl && <img src={quote.imageUrl} alt={quote.title} className="rounded-lg" />}
                </div>
                <div>
                    <h1 className="font-bold">Quote Amount: {quote.amount}</h1>
                    <h1 className="font-bold">Date Available: {quote.availableDate}</h1>
                    <h1>Quoted by: {quote.ownerDetails}</h1>
                    <span style={{ color: getStatusColor(), fontWeight: 'lighter' }}>
                        <p className="flex justify-end md:justify-start text-sm">Status: {quote.status} </p>
                    </span>
                </div>
            </div>
            <Toaster />
            <div className="flex justify-around align-end">
                <div className="active:shadow-md mt-4 flex justify-center bg-blue-800 rounded-xl p-2 md:w-1/4 mb-2">
                    <button
                        type="button"
                        onClick={handleReject}
                        className="text-center text-red-500 font-bold"
                        disabled={isLoading} // Disable button while loading
                    >
                        {isLoading ? "Rejecting..." : "Reject Quote"}
                    </button>
                </div>
                <div className="active:shadow-md mt-4 flex justify-center bg-blue-800 rounded-xl p-2 md:w-1/4 mb-2">
                    <button
                        type="button"
                        onClick={handleAccept}
                        className="text-center text-white font-bold"
                        disabled={isLoading} // Disable button while loading
                    >
                        {isLoading ? "Accepting..." : "Accept Quote"}
                    </button>
                </div>
            </div>
        </div>
    );
};

export default QuoteDetailsScreen;
