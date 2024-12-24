import DriversDashboard from '../../components/util/driver/DriversDashboard';
import { useRouter } from 'next/router';

export default function DriversDashboardPage() {
  const router = useRouter();
  const { id } = router.query; // Access the dynamic 'id' from the URL

  return (
    <div>
      <DriversDashboard id={id} />
    </div>
  );
}
