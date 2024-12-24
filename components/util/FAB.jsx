const FAB = ({ onClick }) => {
    return (
        <button
            onClick={onClick}  // Use the passed onClick function
            style={{
                position: 'fixed',
                bottom: '50px',
                right: '40px',
                padding: '8px',
                backgroundColor: 'blue',
                color: 'white',
                border: 'none',
                boxShadow: '0 4px 8px rgba(0,0,0,0.2)',
                cursor: 'pointer'
            }} className="text-xl font-bold rounded-xl text-center">New job</button>
    );
}

export default FAB;
