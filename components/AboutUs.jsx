'use client'
import { useEffect, useState } from 'react';
import JobCreationCard from './util/client/JobCreationCard';
import { faMailBulk, faMailReply, faPhone, faPhoneAlt } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faFacebook, faMailchimp, faTiktok } from '@fortawesome/free-brands-svg-icons';
import { faEnvelope } from '@fortawesome/free-regular-svg-icons';
const AboutUs = () => {
  const [bgIndex, setBgIndex] = useState(0);
  const backgrounds = [
    'url("assets/onphone.jpg")',
    'url("assets/truck.jpg")',
    'url("assets/insidecab.jpg")',
    'url("assets/truckcab.jpg")',
  ];
  const [isMenuOpen, setIsMenuOpen] = useState(false);

  useEffect(() => {
    const interval = setInterval(() => {
      setBgIndex((prevIndex) => (prevIndex + 1) % backgrounds.length);
    }, 5000); // Change image every 5 seconds
    return () => clearInterval(interval);
  }, [backgrounds.length]);
  // Scroll to the next section on arrow click
  const handleScroll = () => {
    const nextSection = document.getElementById('why-choose-us');
    if (nextSection) {
      nextSection.scrollIntoView({ behavior: 'smooth' });
    }
  };
  const handleServiceScroll = () => {
    const nextSection = document.getElementById('services');
    if (nextSection) {
      nextSection.scrollIntoView({ behavior: 'smooth' });
    }
  };
  const toggleMenu = () => {
    setIsMenuOpen(!isMenuOpen);
  };
  return (
    <div className="text-gray-800">
      {/* Navbar */}
      <nav className="absolute top-0 left-0 w-full z-20 bg-transparent text-white">
        <div className="max-w-full px-4 sm:px-6 lg:px-8">
          <div className="flex items-center justify-between h-16">
            {/* Logo */}
            <div className="flex-shrink-0">
              <h1 className="text-xl sm:text-2xl font-bold">Axle Logistics</h1>
            </div>

            {/* Hamburger Icon for Mobile */}
            <div className="block lg:hidden">
              <button
                onClick={toggleMenu}
                className="text-white focus:outline-none"
              >
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  className="w-6 h-6"
                  fill="none"
                  stroke="currentColor"
                  viewBox="0 0 24 24"
                  strokeWidth="2"
                >
                  <path
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    d="M4 6h16M4 12h16M4 18h16"
                  />
                </svg>
              </button>
            </div>

            {/* Desktop Links */}
            <div className="hidden lg:flex space-x-4">
              <a href="/listings" className="hover:underline text-sm sm:text-base flex items-center">
                Deliveries
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  className="ml-2 w-4 h-4 transform transition-transform duration-200 group-hover:rotate-90"
                  fill="none"
                  stroke="currentColor"
                  viewBox="0 0 24 24"
                  strokeWidth="2"
                >
                  <path
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    d="M9 5l7 7-7 7"
                  />
                </svg>
              </a>
              <a href="/login" className="hover:underline text-sm sm:text-base flex items-center">
                Login
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  className="ml-2 w-4 h-4 transform transition-transform duration-200 group-hover:rotate-90"
                  fill="none"
                  stroke="currentColor"
                  viewBox="0 0 24 24"
                  strokeWidth="2"
                >
                  <path
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    d="M9 5l7 7-7 7"
                  />
                </svg>
              </a>
              <a onClick={handleServiceScroll} className="hover:underline text-sm sm:text-base flex items-center">
                Services
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  className="ml-2 w-4 h-4 transform transition-transform duration-200 group-hover:rotate-90"
                  fill="none"
                  stroke="currentColor"
                  viewBox="0 0 24 24"
                  strokeWidth="2"
                >
                  <path
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    d="M9 5l7 7-7 7"
                  />
                </svg>
              </a>
            </div>
          </div>

          {/* Mobile Menu */}
          <div
            className={`lg:hidden ${isMenuOpen ? 'block' : 'hidden'} fixed top-0 left-0 w-full h-full bg-gray-800 bg-opacity-80 z-30 p-8 transition-all duration-300 ease-in-out`}
          >
            {/* Close Button */}
            <button
              onClick={toggleMenu}
              className="absolute top-6 right-6 text-4xl text-white focus:outline-none"
            >
              &times; {/* Large cross (×) */}
            </button>

            {/* Menu Content */}
            <div className="flex flex-col items-center justify-start space-y-6 mt-16">
              {/* Align the items to the center horizontally, start vertically */}
              <a href="/listings" className="text-white text-lg hover:underline flex items-center">
                Deliveries
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  className="ml-2 w-4 h-4 transform transition-transform duration-200 group-hover:rotate-90"
                  fill="none"
                  stroke="currentColor"
                  viewBox="0 0 24 24"
                  strokeWidth="2"
                >
                  <path
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    d="M9 5l7 7-7 7"
                  />
                </svg>
              </a>
              <a href="/login" className="text-white text-lg hover:underline flex items-center">
                Login
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  className="ml-2 w-4 h-4 transform transition-transform duration-200 group-hover:rotate-90"
                  fill="none"
                  stroke="currentColor"
                  viewBox="0 0 24 24"
                  strokeWidth="2"
                >
                  <path
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    d="M9 5l7 7-7 7"
                  />
                </svg>
              </a>
              <a onClick={handleServiceScroll} className="text-white text-lg hover:underline flex items-center">
                Services
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  className="ml-2 w-4 h-4 transform transition-transform duration-200 group-hover:rotate-90"
                  fill="none"
                  stroke="currentColor"
                  viewBox="0 0 24 24"
                  strokeWidth="2"
                >
                  <path
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    d="M9 5l7 7-7 7"
                  />
                </svg>
              </a>
            </div>
          </div>
        </div>
      </nav>

      {/* Main Content */}
      <main className="relative w-full">
        {/* Static Image Background Section */}
        <section
          className={`relative h-screen w-full flex items-center justify-center bg-cover bg-center transition-all duration-1000`}
          style={{
            backgroundImage: backgrounds[bgIndex],
            animation: 'fadeZoom 15s infinite',
          }}
        >
          <div className="absolute inset-0 bg-black bg-opacity-50 "></div>
          {/* Overlay Content */}
          <div className="relative z-10 text-center text-white px-4 sm:px-0">
          
            <h2 className="text-3xl sm:text-4xl lg:text-5xl font-bold">Welcome to Axle Logistics</h2>
            <p className="text-base sm:text-lg md:text-2xl mt-4">
            Providing end-to-end logistics solutions for all your transportation needs. From agricultural goods to industrial materials, we ensure reliable, efficient service every time.
         </p>  
          </div>

          {/* Scroll Down Arrow */}
          <div className="absolute bottom-4 sm:bottom-8 left-1/2 transform -translate-x-1/2 z-10">
            <button onClick={handleScroll} className="text-white text-3xl animate-bounce border-2 border-blue-800 rounded-full p-2">
              ↓
            </button>
          </div>
        </section>
        <section id='services' className='md:w-1/2 mx-auto my-8 px-2 text-center'>
  <h1 className='text-2xl md:text-3xl font-semibold text-blue-800 mb-2 text-center'>Our Services</h1>
  <div className='flex flex-col md:flex-row'>
    <div className='rounded-lg py-4 px-2 m-2 bg-sky-900 shadow shadow-lg flex flex-col flex-1'>
      <h1 className='text-xl text-black font-bold'>General Haulage Services</h1>
      <p className='mb-2 flex-grow text-white'>Whether you need to move heavy cargo from ports to warehouses or deliver essential supplies to farms, our reliable and efficient logistics solutions are tailored to meet your needs. With a commitment to safety, timeliness, and professionalism, we ensure your goods are handled with care every step of the way.</p>
      <a href='/newjob' className='md:w-1/2 mx-auto m-2 p-2 mb-4 font-bold rounded-lg border border-white text-rose-800'>Get a quote now</a>
    </div>
  </div>
</section>

        <section className="my-8 px-4 sm:px-6 text-center md:w-1/2 mx-auto">

          <h2 className="text-2xl md:text-3xl font-semibold text-blue-800 mb-6">Our Mission & Vision</h2>
          
          <div className="bg-sky-900 p-6 rounded-lg shadow-lg">
            <h3 className="text-xl font-semibold text-black">Mission</h3>
            <p className="text-white mb-6">
              To provide reliable and efficient transportation solutions that empower businesses and individuals across Kenya and East Africa, fostering connections and facilitating trade.
            </p>
            <h3 className="text-xl font-semibold text-black">Vision</h3>
            <p className="text-white">
              To be the leading transportation platform in East Africa, revolutionizing logistics through technology and community engagement, ensuring safe and timely deliveries for all.
            </p>
          </div>
        </section>

        <section id="why-choose-us" className="my-8 px-4 sm:px-6  py-2">
          <h2 className="text-2xl md:text-3xl font-semibold text-rose-800 mb-6 text-center">Why Choose Axle?</h2>
          <div className='flex flex-col md:flex-row' >
            {/* Reliable Drivers */}
            <div className=" p-6 rounded-lg flex flex-col items-center text-center">
              <img src='/icons/driver-svgrepo-com.svg' alt="Reliable Drivers" className="w-8 h-8 mb-4" />
              <h3 className="text-xl font-semibold text-green-900">Reliable Drivers</h3>
              <p className="text-gray-700">Access to a network of reliable and vetted drivers.</p>
            </div>

            {/* Fast Delivery */}
            <div className="p-6 rounded-lg flex flex-col items-center text-center">
              <img src="/icons/delivery-svgrepo-com.svg" alt="Fast Delivery" className="w-8 h-8 mb-4" />
              <h3 className="text-xl font-semibold text-green-900">Fast Delivery</h3>
              <p className="text-gray-700">Receive your deliveries in record time.</p>
            </div>
            {/* Cost-Effective Solutions */}
            <div className="p-6 rounded-lg flex flex-col items-center text-center">
              <img src="/icons/budget-cost-svgrepo-com.svg" alt="Cost-Effective" className="w-8 h-8 mb-4" />
              <h3 className="text-xl font-semibold text-green-900">Cost-Effective Solutions</h3>
              <p className="text-gray-700">Get the best value for your money with competitive pricing and quality service.</p>
            </div>
            {/* Comprehensive Documentation */}
            <div className="p-6 rounded-lg flex flex-col items-center text-center">
              <img src="/icons/documentation-svgrepo-com.svg" alt="Record Keeping" className="w-8 h-8 mb-4" />
              <h3 className="text-xl font-semibold text-green-900">Comprehensive Documentation</h3>
              <p className="text-gray-700">Receive detailed records and job completion reports to keep your workflow organized.</p>
            </div>
            {/* Goods Protection */}
            <div className="p-6 rounded-lg flex flex-col items-center text-center">
              <img src="/icons/shield-check-svgrepo-com.svg" alt="Goods Protection" className="w-8 h-8 mb-4" />
              <h3 className="text-xl font-semibold text-green-900">Goods Protection</h3>
              <p className="text-gray-700">We prioritize the safety of your goods with reliable handling and tracking throughout the delivery process.</p>
            </div>


            {/* Wide Opportunities */}
            <div className="p-6 rounded-lg flex flex-col items-center text-center">
              <img src="/icons/i-groups-perspective-crowd-svgrepo-com.svg" alt="Clients" className="w-8 h-8 mb-4" />
              <h3 className="text-xl font-semibold text-green-900">Wide Opportunities</h3>
              <p className="text-gray-700">Access to a wide range of clients and job opportunities.</p>
            </div>

            {/* 24/7 Support */}
            <div className="p-6 rounded-lg flex flex-col items-center text-center">
              <img src='/icons/customer-service-24-hours-svgrepo-com.svg' alt="24/7 Support" className="w-8 h-8 mb-4" />
              <h3 className="text-xl font-semibold text-green-900">24/7 Support</h3>
              <p className="text-gray-700">Access to 24/7 support to assist you with any issues.</p>
            </div>
          </div>
        </section>


        {/* How Axle Works Section */}
        <section className="my-8 px-4 sm:px-6 relative py-2">


          {/* Background Image */}
          <div className="absolute inset-0 bg-cover bg-center h-full opacity-50" style={{ backgroundImage: 'url("assets/truck.jpg")' }}></div>

          {/* Translucent Card Container */}
          <div className="relative z-10 bg-white bg-opacity-80 rounded-lg shadow-md p-6 md:p-8">
            <h2 className="text-xl md:text-2xl font-semibold text-sky-800 mb-4 text-center">How Axle Works</h2>
            <div className="flex flex-col md:flex-row md:space-x-4">
              {/* Step 1 */}
              <div className="flex items-start mb-4 md:mb-0">
                <img src='/icons/one-open-svgrepo-com.svg' alt="Step 1" className="w-10 h-10 mr-4" />
                <div>
                  <h3 className="text-lg font-semibold text-sky-800">1. Request Quote</h3>
                  <p className="text-gray-700">Request quote for your delivery needs.</p>
                </div>
              </div>

              {/* Step 2 */}
              <div className="flex items-start mb-4 md:mb-0">
                <img src='/icons/two-open-svgrepo-com.svg' alt="Step 2" className="w-10 h-10 mr-4" />
                <div>
                  <h3 className="text-lg font-semibold text-sky-800">2. Get The Best Quote</h3>
                  <p className="text-gray-700">We give you the best quote for you.</p>
                </div>
              </div>

              {/* Step 3 */}
              <div className="flex items-start mb-4 md:mb-0">
                <img src='/icons/three-open-svgrepo-com.svg' alt="Step 3" className="w-10 h-10 mr-4" />
                <div>
                  <h3 className="text-lg font-semibold text-sky-800">3. You Get The Driver</h3>
                  <p className="text-gray-700">We send the most qualified driver for your specific goods.</p>
                </div>
              </div>
              <div className="flex items-start mb-4 md:mb-0">
                <img src='/icons/number3-svgrepo-com.svg' alt="Step 4" className="w-10 h-10 mr-4" />
                <div>
                  <h3 className="text-lg font-semibold text-sky-800">4. Sit Back & Relax</h3>
                  <p className="text-gray-700">Sit back and relax as we deliver your goods to the desired destination.</p>
                </div>
              </div>
            </div>
          </div>
        </section>


      </main>

      <section
         className="text-center my-0 bg-cover bg-center"
         style={{
          background: `linear-gradient(to bottom right, rgba(128, 128, 128, 0.1) 0%, rgba(107, 114, 128, 1) 50%), url('assets/insidecab.jpg')`,
        }}
      >
        <div className="absolute inset-0"></div> {/* Removed the overlay for better blending */}
        <div className="relative z-10 p-6">
          <h2 className="text-3xl font-semibold text-blue-800 mb-4">Join Axle Today!</h2>
          <p className="text-gray-700 mb-6">
            Become a part of our trusted network today.
          </p>
          <a
            href="/onboarding"
            className="bg-blue-800 text-white py-3 px-6 rounded-lg shadow-md hover:bg-blue-700 transition duration-300"
          >
            Join Us
          </a>
        </div>
        <footer className="m-auto">
          <div className="md:flex md:flex-row md:justify-between md:px-4 md:w-3/4 md:mx-auto py-2 px-2 border-t-2 border-t-black md:justify-between">
            <span className="flex flex-col   my-2 items-start">
              <h1 className="text-sky-900 font-bold">Services</h1>
              <a className='' href='/newjob'>General Haulage</a>
            </span>
            <span className="flex flex-col items-start">
              <h1 className="text-sky-900 font-bold">Contact Us</h1>
              <a href='tel:+254705220087' className='m-1 '><FontAwesomeIcon icon={faPhone}/> +254705220087</a>
              <a href='tel:+254708263028' className='m-1 '><FontAwesomeIcon icon={faPhone}/> +254708263028</a>
              <a href='mailto:axlelogsiticskenya@gmail.com' className='m-1 '><FontAwesomeIcon icon={faEnvelope}/>&nbsp;axlelogsiticskenya@gmail.com</a>
              <a href='https://www.facebook.com/profile.php?id=61570596030120' className='m-1 '><FontAwesomeIcon icon={faFacebook}/>&nbsp;Find us on Facebook</a>
              <a href='https://www.tiktok.com/@axlelogistic987?_t=8sSHsW6sbBN&_r=1' className='m-1 '><FontAwesomeIcon icon={faTiktok}/>&nbsp;Find us on TikTok</a>
            </span>
          </div>
          <p className="text-white text-center">&copy; 2024 Axle. All rights reserved.</p>
      
        </footer>

      </section>


    </div>
  );
};

export default AboutUs;
