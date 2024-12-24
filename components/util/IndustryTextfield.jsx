import { useState } from "react"
/* eslint-disable react/prop-types */
const IndustryTextfield =({className, options, onChange, value})=>{

    const [showDropdown, setShowDropdown] = useState(false)
   
    const handleInputChange = (e) => {
        onChange(e.target.value);

        setShowDropdown(e.target.value.length > 0)
    }
    const handleOptionClick = (option) => {
       onChange(option)
        setShowDropdown(false)
    }
    const filteredOptions = options.filter((option) =>
    option.includes(value));
    return(
        <div style={{position: 'relative', width:'200px'}}>
            <input
            type="text"
            className={className} 
            value={value}
            onChange={handleInputChange}
            onFocus={() => setShowDropdown(true)}
            onBlur={() => setTimeout(() => setShowDropdown(false), 200)}
            style={{width:'100%', padding:'2px', boxSizing:'border-box'}}
            />
            {showDropdown && filteredOptions.length > 0 && (
                <ul
                style={{
                    listStyle: 'none',
                    margin: 0,padding: 0,border: '1px solid #ccc',
                    position: 'absolute', width:'100%', maxHeight:'150px',
                    overflowY:'auto', backgroundColor:'white', zIndex: 1
                }}
                >{filteredOptions.map((option, index) => (
                    <li key={index}
                    onClick={() => handleOptionClick(option)}
                    style={{padding:'8px', cursor:'pointer'}}
                    onMouseDown={(e) => e.preventDefault()}>
                        {option}
                    </li>))}</ul>
            )}
        </div>
    )
}
export default IndustryTextfield