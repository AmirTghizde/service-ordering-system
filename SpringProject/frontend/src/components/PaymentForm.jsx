import React from "react";
import './PaymentForm.css'
import {useState} from "react";

function PaymentForm() {
    const [amount,setAmount] = useState(0)
    return (
        <div className='container d-flex flex-column justify-content-center  min-vh-100'>
            <form className='rounded'>
                <div className='inputBox'>
                    <span className='d-block p-1 text-start'>card number</span>
                    <input type="text" maxLength="16" className="card-number-input w-100 p-1 rounded border border-white"/>
                </div>
                <div className='inputBox'>
                    <span className='d-block p-1 text-start'>card holder</span>
                    <input type="text" className="card-holder-input w-100 p-1 rounded border border-white"/>
                </div>
                <div className='inputBox'>
                    <span className='d-block p-1 text-start'>card holder</span>
                    <input type="text" className="card-holder-input w-100 p-1 rounded border border-white"/>
                </div>
                <div className='flexbox d-flex'>
                    <div className='inputBox'>
                        <span className='d-block p-1 text-start'>expiration mm</span>
                        <select className='month-input w-100 p-1 rounded border border-white'>
                            <option value='month' selected='disabled'>month</option>
                            <option value='01' >01</option>
                            <option value='02' >02</option>
                            <option value='03' >03</option>
                            <option value='04' >04</option>
                            <option value='05' >05</option>
                            <option value='06' >06</option>
                            <option value='07' >07</option>
                            <option value='08' >08</option>
                            <option value='09' >09</option>
                            <option value='10' >10</option>
                            <option value='11' >11</option>
                            <option value='12' >12</option>
                        </select>
                    </div>
                    <div className='inputBox'>
                        <span className='d-block p-1 text-start'>expiration yy</span>
                        <select className='year-input w-100 p-1 rounded border border-white'>
                            <option value='year' selected='disabled'>year</option>
                            <option value='2021' >2021</option>
                            <option value='2022' >2022</option>
                            <option value='2023' >2023</option>
                            <option value='2024' >2024</option>
                            <option value='2025' >2025</option>
                            <option value='2026' >2026</option>
                            <option value='2027' >2027</option>
                            <option value='2028' >2028</option>
                            <option value='2029' >2029</option>
                            <option value='2030' >2030</option>
                        </select>
                    </div>
                    <div className='inputBox '>
                        <span className='d-block p-1 text-start'>cvv2</span>
                        <input type="text" className="cvv2-input w-100 p-1 rounded border border-white" maxLength='4'/>
                    </div>
                </div>
                <input type='submit' value={`pay $${amount}`} className='submit-btn w-100 p-1 rounded-5'/>
            </form>
        </div>
    )
}

export default PaymentForm