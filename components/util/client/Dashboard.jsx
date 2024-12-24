import { useParams } from 'next/navigation';
import Profile from '../Profile';
import JobList from './JobList';
import FAB from '../FAB';
import Navbar from '../NavBar';
import { useState } from 'react';
import JobCreationAuthed from './JobCreationAuthed';
import PasswordCreation from '../../Auth/PasswordCreation';
import { useAuth } from '../../Auth/AuthContext';
import Head from 'next/head';
const Dashboard = () => {
    const params = useParams()
    const { id } = params
    const [isJobModalOpen, setIsJobModalOpen] = useState(false);
    const { showPasswordModal, setShowPasswordModal } = useAuth();

    const toggleJobModal = () => {
        setIsJobModalOpen(prev => !prev);
    };

    return (
        <div className="flex flex-col h-screen">
            <Head>
      <title>Client Dashboard - Axle Logistics Kenya</title>
      <meta name="description" content="Access your dashboard and manage your tasks." />
    </Head>
            <Navbar />
            <main className="flex-grow flex flex-col p-4 overflow-y-auto bg-gray-100">
                <div className="flex justify-between items-center mb-4">
                    <div className="bg-sky-800 shadow-md rounded-lg p-6 flex-grow">
                        <Profile id={id} />
                    </div>
                </div>
                <div className="bg-white shadow-md rounded-lg p-6 flex-grow">
                    <JobList />
                </div>
            </main>
            <FAB onClick={toggleJobModal} />

            {isJobModalOpen && (
                <div className="fixed md:relative inset-0 w-full max-w-screen h-full max-h-screen flex items-center justify-center bg-black bg-opacity-50 z-50">
                    <div className="bg-white max-w-2xl w-full rounded-lg shadow-lg p-6 md:w-1/2">
                        <JobCreationAuthed 
                            isModalOpen={isJobModalOpen} 
                            setIsModalOpen={setIsJobModalOpen} 
                        />
                    </div>
                </div>
            )}
            <PasswordCreation
                isOpen={showPasswordModal}
                onClose={() => setShowPasswordModal(false)}
            />
        </div>
    );
};

export default Dashboard;
