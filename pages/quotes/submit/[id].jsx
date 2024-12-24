import QuoteCard from '../../../components/util/driver/QuoteCard';
import { useRouter } from 'next/router';

export default function QuoteCardPage() {
  const router = useRouter();
  const { id } = router.query; // Access the dynamic 'id' from the URL

  return (
    <div>
      <h1>Driver Dashboard</h1>
      <p>Driver ID: {id}</p>
      <QuoteCard id={id} />
    </div>
  );
}
