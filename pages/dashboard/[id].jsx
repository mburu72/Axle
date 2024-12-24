import Dashboard from '../../components/util/client/Dashboard';
import { useRouter } from 'next/router';

export default function DashboardPage() {
  const router = useRouter();
  const { id } = router.query; // Access the dynamic 'id' from the URL

  return (
    <div>
      <Dashboard id={id} />
    </div>
  );
}
