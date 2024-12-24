import { useState, useRef, useEffect } from 'react';
import { useAuth } from '../Auth/AuthContext';
import { jwtDecode } from 'jwt-decode';
import { useRouter } from 'next/router';
import Contact from '../Contact';
import Cookies from 'js-cookie'
const Navbar = () => {


  const [isOpen, setIsOpen] = useState(false);
  const [isDropdownOpen, setDropdownOpen] = useState(false);
  const [isNotificationOpen, setNotificationOpen] = useState(false);
  const [isContactsOpen, setContactsOpen] = useState(false);
  const { isAuthenticated, user, logout } = useAuth();
  const formRef = useRef(null);
  const dropdownRef = useRef(null);
  const notificationRef = useRef(null);
  const contactsRef = useRef(null);
  const router = useRouter();

  const toggleMenu = () => {
    setIsOpen(!isOpen);
  };

  const toggleDropdown = () => {
    setDropdownOpen(prev => !prev);
    setNotificationOpen(false);
    setContactsOpen(false);
  };
  useEffect(() => {
    const handleClickOutside = (event) => {
        if (formRef.current && !formRef.current.contains(event.target)) {
          setContactsOpen(false);
        }
    };

    document.addEventListener('mousedown', handleClickOutside);
    return () => {
        document.removeEventListener('mousedown', handleClickOutside);
    };
}, []);


  const toggleNotificationDropdown = () => {
    setNotificationOpen(prev => !prev);
    setDropdownOpen(false);
    setContactsOpen(false);
  };
  const handleNameClick = () => {
    const token = Cookies.get('token'); // Get the token from cookies
    if (token) { // Check if the token exists
        const decoded = jwtDecode(token); // Decode the token

        // Navigate based on the decoded role
        if (decoded.role === 'CLIENT') {
            router.push(`/dashboard/${decoded.id}`);
        } else if (decoded.role === 'HAULER') {
            router.push(`/drivers-dashboard/${decoded.id}`);
        }
    }
};


  const toggleContactsDropdown = () => {
    setContactsOpen(prev => !prev);
    setDropdownOpen(false);
  };

  const handleLogout = (event) => {
    event.preventDefault(); // Prevent default behavior
    event.stopPropagation(); // Stop the click from bubbling up
    logout(); // Trigger the actual logout
  };


  return (
    <nav className="bg-blue-800 text-white shadow-md">
      <div className="max-w-6xl mx-auto px-4 py-3 flex justify-between items-center">
        <a href="/" className="text-2xl font-bold">Axle Logistics</a>

        <button
          className="block lg:hidden px-2 py-1 rounded-md text-gray-200 hover:bg-blue-700 focus:outline-none"
          onClick={toggleMenu}
        >
          <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M4 6h16M4 12h16M4 18h16" />
          </svg>
        </button>

        <div className="hidden lg:flex items-center space-x-4">
        
          <a href="/listings" className="hover:underline">Deliveries</a>
          <a href="/" className="hover:underline">About Us</a>
  
          <div className="relative" ref={contactsRef}>
            <button onClick={toggleContactsDropdown} className="focus:outline-none hover:underline">
              Contact Us
            </button>

            {isContactsOpen && (
          <Contact setContactsOpen={setContactsOpen} />
            )}
          </div>

          <div className="relative" ref={notificationRef}>
            <button onClick={toggleNotificationDropdown} className="focus:outline-none">
              <svg xmlns="http://www.w3.org/2000/svg" className="h-8 w-8 text-white" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M15 17h5l-1.405-1.405A2.032 2.032 0 0118 14.158V11a7.002 7.002 0 00-5-6.708V4a2 2 0 10-4 0v.292A7.002 7.002 0 004 11v3.159c0 .538-.214 1.055-.595 1.437L2 17h5m7 0v1a3 3 0 11-6 0v-1m6 0H9" />
              </svg>
            </button>

            {isNotificationOpen && (
              <div>
            {/*
              <QuoteAlerts />*/}
          </div>
            )}
          </div>

          <div className="relative" ref={dropdownRef}>
            <button onClick={toggleDropdown} className="focus:outline-none">
              <svg xmlns="http://www.w3.org/2000/svg" className="h-8 w-8 text-white" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M5.121 17.804A9.969 9.969 0 0112 15a9.969 9.969 0 016.879 2.804M12 11a4 4 0 100-8 4 4 0 000 8z" />
              </svg>
            </button>

            {isDropdownOpen && (
              <div className="absolute right-0 mt-2 w-48 bg-white rounded-lg shadow-lg z-50">
                <ul className="max-h-60 overflow-y-auto">
                  {isAuthenticated ? (
                    <>
                      <li className="p-2 hover:bg-gray-300 text-blue-800 text-center" onClick={handleNameClick}>Hello, {user?.firstName || 'User'}</li>
                      <li
  className="p-2 hover:bg-gray-300 text-red-700 text-center cursor-pointer logout-button"
  onClick={handleLogout}
>
  Log out
</li>

                    </>
                  ) : (
                    <li className="p-2 hover:bg-gray-300">
                      <a href="/login" className='text-blue-800'>Log in</a>
                    </li>
                  )}
                </ul>
              </div>
            )}
          </div>
        </div>

        {/* Mobile Menu */}
        <div className={`lg:hidden fixed inset-0 bg-blue-800 text-white z-40 transform ${isOpen ? 'translate-x-0' : 'translate-x-full'} transition-transform duration-300`}>
          <div className="flex justify-between items-center p-4">
            <a href="/" className="text-2xl font-bold text-white">Axle Logistics</a>
            <button className="text-gray-200 hover:bg-blue-700 p-2 rounded-md" onClick={toggleMenu}>
              <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M6 18L18 6M6 6l12 12" />
              </svg>
            </button>
          </div>
          <div className="flex flex-col p-4 space-y-2">
            <a href="/listings" className="p-2 text-white ">Deliveries</a>
            <a href="/" className="p-2 text-white">About Us</a>

            <div className="relative" ref={contactsRef}>
              <button onClick={toggleContactsDropdown} className="p-2 text-white focus:outline-none">Contact Us</button>

              {isContactsOpen && (
            
               <Contact/>
              )}
            </div>

            <div className="relative" ref={notificationRef}>
              <button onClick={toggleNotificationDropdown} className="p-2 text-white focus:outline-none">
                Notifications
              </button>

              {isNotificationOpen && (
                <div className="absolute right-0 mt-2 w-48 bg-white rounded-lg shadow-lg z-50">
                  <ul className="max-h-60 overflow-y-auto">
                    <li className="p-2 hover:bg-gray-300 text-blue-800">No new notifications</li>
                  </ul>
                </div>
              )}
            </div>

            <div className="relative" ref={dropdownRef}>
              <button onClick={toggleDropdown} className="p-2 text-white focus:outline-none">
                My Dashboard
              </button>

              {isDropdownOpen && (
                <div className="absolute right-0 mt-2 w-48 bg-white rounded-lg shadow-lg z-50">
                  <ul className="max-h-60 overflow-y-auto">
                    {isAuthenticated ? (
                      <>
                        <li className="p-2 hover:bg-gray-300 text-blue-800 text-center" onClick={handleNameClick}>Hello, {user?.firstName || 'User'}</li>
                        <li className="p-2 hover:bg-gray-300 text-red-700 text-center cursor-pointer" onClick={handleLogout}>Log out</li>
                      </>
                    ) : (
                      <li className="p-2 hover:bg-gray-300">
                        <a href="/login" className='text-blue-800'>Log in</a>
                      </li>
                    )}
                  </ul>
                </div>
              )}
            </div>
          </div>
        </div>
      </div>
    </nav>
  );
};

export default Navbar;
