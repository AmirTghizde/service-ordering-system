import React from "react";
import gif from "./resource/money.gif";

function successfulPayment(){

    return (
        <div>
            <img src={gif}  alt='emoji fade' width='200px'/>
            <h3 className='mt-3 '>Thank you for choosing us</h3>
            <p className='border border-success text-success'>payment was successful</p>
        </div>
    )
}

export default successfulPayment