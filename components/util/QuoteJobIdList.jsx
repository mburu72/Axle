import { useState } from "react";
import axiosInstance from "../../pages/api/axiosinstance";
import { useEffect } from "react";

import DriverQuotesDisplayCard from "../util/driver/DriverQuotesDisplayCard";
/* eslint-disable react/prop-types */

const QuotesJobIdList = ({id, haulerId}) => {
    const [quotes, setQuotes] = useState([]);
    const [loading, setLoading] = useState(true);  
    useEffect(() => {
        const fetchQuotes = async () => {
          try {
            const response = await axiosInstance.get(`/api/quotes/${haulerId}/${id}/quote`);
         setQuotes(response.data);
          } catch (err) {
         console.log(err);
          } finally {
            setLoading(false);
          }
        };
        fetchQuotes();
    }, []);
  
    if (loading) return <p>Loading...</p>;

    return (
      <div className="mx-auto mt-8 shadow-md p-2">
      <h1 className="text-center text-lg font-bold">Your Quotes For This Job</h1>
      {quotes.map((quote) => (
        <DriverQuotesDisplayCard key={quote.id} quote={quote}/>
      ))}
    </div>
      );
    };
    
    export default QuotesJobIdList;