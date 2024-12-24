import { useRouter } from "next/router";
/* eslint-disable react/prop-types */
const QuoteDisplayCard = ({quote}) => {
  const router = useRouter();
  const handleClick = () => {
  router.push(`/quotes/${quote.id}`);
  }
  const getStatusColor = () => {
    switch (quote.status) {
        case 'ACCEPTED':
            return 'green';
        case 'REJECTED':
            return 'red';
        default:
            return 'white';
    }
  }
    return (
        <div onClick={handleClick} style={{ border: '1px solid #ddd', padding: '10px', marginBottom:'8px'}} className="bg-blue-800 rounded-xl shadow shadow-lg text-white md:mx-auto md:w-1/2">
          <h1 className="font-bold">Quote Amount: {quote.amount}</h1>
          <h1 className="font-bold">Date Available:{quote.availableDate}</h1>
          <h1>Quoted by: {quote.ownerDetails}</h1>
          <span style={{color: getStatusColor(), fontWeight: 'lighter'}}>
          <p className="flex justify-end text-sm">Status: {quote.status} </p>
          </span>
         
        </div>
      );
}
export default QuoteDisplayCard;