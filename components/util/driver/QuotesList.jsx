import { useState } from "react";
import axiosInstance from "../../../pages/api/axiosinstance";
import { useEffect } from "react";
import QuoteDisplayCard from "./QuoteDisplayCard";
/* eslint-disable react/prop-types */

const QuotesList = ({id}) => {
    const [quotes, setQuotes] = useState([]);
    const [loading, setLoading] = useState(true);
  
    useEffect(() => {
        const fetchQuotes = async () => {
          try {
            const response = await axiosInstance.get(`/api/quotes/${id}/quote`);
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
      <h1 className="text-center text-lg font-bold">Quotes</h1>
      {quotes.map((quote) => (
        <QuoteDisplayCard key={quote.id} quote={quote}/>
        
      ))}
      
    </div>
      );
    };
    
    export default QuotesList;