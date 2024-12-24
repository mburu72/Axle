import QuoteDetailsScreen from '../../components/util/driver/QuoteDetailsScreen';
import { useRouter } from 'next/router';

export default function QuotesDetailsPage() {
  const router = useRouter();
  const { id } = router.query; // Access the dynamic 'id' from the URL

  return (
    <div>
      <h1>Driver Dashboard</h1>
      <p>Driver ID: {id}</p>
      <QuoteDetailsScreen id={id} />
    </div>
  );
}
