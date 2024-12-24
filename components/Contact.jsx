import { useState } from "react";
import axiosInstance from "../pages/api/axiosinstance";
import toast, { Toaster } from "react-hot-toast";
const Contact = ({setContactsOpen}) => {
    const [name, setName] = useState('');
    const [contactInfo, setContactInfo] = useState('');
    const [message, setMessage] = useState('');
    const [status, setStatus] = useState('');
    const handleSubmit = async (e) => {
        e.preventDefault();
      
        try { 
            const response = await axiosInstance.post('/api/v1/email', {
                name,
                contactInfo,
                message,
            }, {
                headers: {
                    'Content-Type': 'application/json',
                },
            });
      
            if (response.status === 200) {
                setStatus('Message sent successfully!');
                setName('');
                setContactInfo('');
                setMessage('');
                toast.success("Message sent successfully!")
                
                setTimeout(() => {
                    setContactsOpen(false);
                  }, 2000);
            }
        } catch (error) {
            console.error('Error sending message:', error);
            setStatus('Failed to send message.');
        }
      };
    return (
        <div className="absolute right-0 mt-2 w-80 bg-white rounded-lg shadow-lg z-50 p-4">
        <form onSubmit={handleSubmit}>
            <label className="block mb-1" htmlFor="name">Name:</label>
            <input
                type="text"
                id="name"
                className="w-full p-2 border border-gray-300 text-gray-600  rounded mb-2"
                value={name}
                onChange={(e) => setName(e.target.value)}
                placeholder='Your name'
                required
            />
            
            <label className="block mb-1" htmlFor="contact">Email or Phone Number:</label>
            <input
                type="text"
                id="contactInfo"
                className="w-full p-2 border border-gray-300 text-gray-600  rounded mb-2"
                value={contactInfo}
                onChange={(e) => setContactInfo(e.target.value)}
                 placeholder='Your email or phone number'
                required
            />
            
            <label className="block mb-1" htmlFor="message">Message:</label>
            <textarea
                id="message"
                className="w-full p-2 border border-gray-300 text-gray-600 rounded mb-2"
                rows="4"
                value={message}
                onChange={(e) => setMessage(e.target.value)}
                 placeholder='Message'
                required
            />
            
            <button
                type="submit"
                className="w-full bg-blue-500 text-white py-2 rounded hover:bg-blue-600"
            >
                Send Message
            </button>
        
            {status && <p className="mt-2 text-red-800">{status}</p>}
        </form>
        <Toaster/>
    </div>
    )
}
export default Contact;