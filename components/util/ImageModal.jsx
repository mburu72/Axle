import React from 'react';
/* eslint-disable react/prop-types */
const ImageModal = ({ isOpen, onClose, imageUrl }) => {
    if (!isOpen) return null;

    return (
        <div className="fixed inset-0 flex items-center justify-center bg-black bg-opacity-75">
            <div className="relative">
                <img src={imageUrl} alt="Preview" className="max-w-full max-h-screen" />
                <button 
                    onClick={onClose} 
                    className="absolute top-2 right-2 bg-red-500 text-white rounded-full p-2"
                >
                    X
                </button>
            </div>
        </div>
    );
};

export default ImageModal;
