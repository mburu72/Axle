import { useState } from "react";
import axiosInstance from "../../pages/api/axiosinstance";
import { HttpStatusCode } from "axios";
import toast, { Toaster } from "react-hot-toast";
import { useRouter } from "next/router";

const PayNow = ({isModalOpen, setIsModalOpen, job_id}) => {
  const [isMpesaSelected, setIsMpesaSelected] = useState(true);
  const [paylaterIsSelected, setPaylaterIsSelected] = useState(false);
  const [payment, setPayment] = useState({ phoneNumber: '', amount: '', jobId: job_id });
  const [isLoading, setIsLoading] = useState(false);
  const router = useRouter()

  const handleRadioChange = () => {
    setIsMpesaSelected(true); // Select M-pesa
    setPaylaterIsSelected(false); // Unselect Pay later
  };
  const laterHandleRadioChange = () => {
    setIsMpesaSelected(false); // Unselect M-pesa
    setPaylaterIsSelected(true); // Select Pay later
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setPayment({ ...payment, [name]: value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setIsLoading(true);

    if (!payment.phoneNumber || !payment.amount) {
      toast.error("Please fill in all fields!");
      setIsLoading(false);
      return;
    }

    try {
      const response = await axiosInstance.post('/api/payment/pay-now', payment);
          
      if (response.status === HttpStatusCode.Ok) {
        toast.success("Payment successful, you'll receive a confirmation message!");
        setTimeout(() => {
          router.push("/confirm-payment");
        }, 2000);
      } else {
        toast.error("Unexpected response from the server.");
      }
    } catch (error) {
      const errorMessage = error.response?.data || "Network error or server did not respond.";
      toast.error(errorMessage);
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div>
     

      {/* Modal */}
      {isModalOpen && (
        <div className="fixed inset-0 flex items-center justify-center bg-gray-500 bg-opacity-50 z-50">
          <div className="bg-white p-6 rounded-lg shadow-lg w-96 relative">
            {/* Close Button (cross) */}
            <button
              onClick={() => setIsModalOpen(false)} // Close the modal
              className="absolute top-2 right-2 text-xl font-bold"
            >
              ×
            </button>

            <h1 className="text-2xl font-bold text-blue-800 text-center mb-4">Pay Now</h1>
            <form onSubmit={handleSubmit} className="flex flex-col justify-center items-center">
              <div className="shadow-xl rounded-xl w-full p-2 flex flex-col justify-center items-center mb-4">
                <span className="text-center">You are confirming payment of:</span>
                <input
                  type="text"
                  name="amount"
                  value={payment.amount}
                  onChange={handleChange}
                  className="w-full p-2 focus:outline-none focus:ring-0 text-2xl text-center font-bold"
                  placeholder="0 Ksh"
                  required
                />
              </div>
<div>
              <div className="mb-4 flex items-center align-center">
                <input
                  type="radio"
                  name="Pay using mpesa"
                  onClick={handleRadioChange}
                  checked={isMpesaSelected}
                  className="mr-4 align-middle"
                />
                <span className="align-middle">Pay using M-pesa</span>
              </div>

              {isMpesaSelected && (
                <div className="mb-4">
                  <input
                    type="tel"
                    name="phoneNumber"
                    value={payment.phoneNumber}
                    onChange={handleChange}
                    className="w-full p-2 border-b border-gray-300 focus:outline-none focus:ring-0 focus:border-blue-300"
                    placeholder="Enter your phone number"
                    required
                  />
                   
                </div>
              )}
              <div className="mb-4 flex items-center align-center">
                <input
                  type="radio"
                  name="Pay later"
                  onClick={laterHandleRadioChange}
                  checked={paylaterIsSelected}
                  className="mr-4 align-middle"
                />
                <span className="align-middle">Pay later</span>
              </div>
              </div>
{paylaterIsSelected &&(
 <div>
 <p>To pay later using m-pesa: </p>
 <span className="flex flex-row m-2 items-center text-sm">
     <p className="mr-2 text-right rounded-full w-6 h-6 bg-green-500 flex items-center justify-center text-white">1</p>&nbsp;Go to Mpesa Menu
 </span>
 <span className="flex flex-row m-2 items-center text-sm">
     <p className="mr-2 text-center rounded-full w-6 h-6 bg-green-500 flex items-center justify-center text-white">2</p>&nbsp;Select&nbsp;    
     <p className="text-blue-800 font-bold">Buy Goods and Services</p>
 </span>
 <span className="flex flex-row m-2 items-center text-sm">
     <p className="mr-2 text-center rounded-full w-6 h-6 bg-green-500 flex items-center justify-center text-white">3</p>&nbsp;Enter Till Number:&nbsp;    
     <p className="text-blue-800 font-bold">Till Number</p>
 </span>
 <span className="flex flex-row m-2 items-center text-sm">
     <p className="mr-2 text-center rounded-full w-6 h-6 bg-green-500 flex items-center justify-center text-white">4</p>&nbsp;Amount to be Paid: &nbsp;    
     <p className="text-blue-800 font-bold">0 KES</p>
 </span>
 <span className="flex flex-row m-2 items-center text-sm">
     <p className="mr-2 text-center rounded-full w-6 h-6 bg-green-500 flex items-center justify-center text-white">5</p>&nbsp;Enter Your Mpesa PIN and Proceed
 </span>
</div>

)}
              <button
                type="submit"
                className={`w-full bg-blue-800 text-white font-bold py-2 rounded-lg transition ${isLoading ? "opacity-50 cursor-not-allowed" : ""}`}
                disabled={isLoading}
              >
                {isLoading ? "Initiating payment..." : "Pay now"}
              </button>
      <Toaster/>

            </form>
            <div className="text-center mt-4">
              <a href="https://intasend.com/security" target="_blank">
                <img src="https://intasend-prod-static.s3.amazonaws.com/img/trust-badges/intasend-trust-badge-with-mpesa-hr-dark.png" width="375px" alt="IntaSend Secure Payments (PCI-DSS Compliant)" />
              </a>
              <strong>
                <a href="https://intasend.com/security" target="_blank" className="text-sm mt-2 block">
                  Secured by IntaSend Payments
                </a>
              </strong>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default PayNow;
