import React from "react";
import gif from './resource/emoji.gif'


function timeOut(){

    const goBack = (event) => {
        event.preventDefault();
        window.location.href = '/';
    };

return(
    <div>
        <img src={gif}  alt='emoji fade' width='60px'/>
        <h3>Time's up!</h3>
        <button className='rounded-5' onClick={goBack}> Try again</button>
    </div>
)
}

export default timeOut