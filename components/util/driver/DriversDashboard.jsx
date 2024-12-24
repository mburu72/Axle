

import Navbar from "../NavBar";
import { useParams } from "next/navigation";
import DriverProfile from "./DriverProfile";
import AllJobs from "./AllJobs";
import Head from "next/head";
const DriversDashboard = () => {
    const { id } = useParams();
    return (
        <div>
            <Head>
      <title>Drivers' Dashboard - Axle Logistics Kenya</title>
      <meta name="description" content="Access your dashboard and manage your tasks." />
    </Head>
            <Navbar />
            <main className="flex-grow flex flex-col p-4 overflow-y-auto bg-gray-200">
                <div className="flex justify-between items-center mb-4">
                        <DriverProfile id={id} />
                </div>
                <div className="bg-blue-300 shadow-md rounded-lg p-6 flex-grow">
                    <AllJobs />
                </div>
            </main>
        </div>
    )
}
export default DriversDashboard;