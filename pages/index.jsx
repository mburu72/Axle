import Head from "next/head";
import AboutUs from "../components/AboutUs";

export default function HomePage() {
  const structuredData = {
    "@context": "https://schema.org",
    "@type": "Organization",
    "name": "Axle Logistics Kenya",
    "url": "https://axle-ke.co.ke",
    "logo": "https://axle-ke.co.ke/logo.png",
    "contactPoint": {
      "@type": "ContactPoint",
      "telephone": "+254-700-123-456",
      "contactType": "Customer Service",
      "areaServed": "KE",
      "availableLanguage": "en"
    },
    "sameAs": [
      "https://www.facebook.com/axlelogistics",
      "https://twitter.com/axlelogistics",
      "https://www.linkedin.com/company/axlelogistics"
    ]
  };

  return (
    <>
      <Head>
        <title>Axle Logistics Kenya</title>
        <meta name="description" content="Welcome to Axle Logistics Kenya, your go-to platform for reliable transportation services." />
        
        {/* Add Structured Data */}
        <script
          type="application/ld+json"
          dangerouslySetInnerHTML={{ __html: JSON.stringify(structuredData) }}
        />
      </Head>
      
      <AboutUs />
    </>
  );
}
