import JobList from "../components/util/client/JobList";
import { useEffect } from "react";
import { useRouter } from "next/router";
import { useAuth } from "../components/Auth/AuthContext";
export default function ClientJobsPage() {
    const { isAuthenticated } = useAuth();
    const router = useRouter();
  
    useEffect(() => {
      if (!isAuthenticated) {
        router.push('/'); // Redirect to home page if not authenticated
      }
    }, [isAuthenticated, router]);
  
    if (!isAuthenticated) return null; // Prevent rendering until redirect
  
    return <JobList />;
  }