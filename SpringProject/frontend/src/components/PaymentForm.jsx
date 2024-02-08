import React from "react";
import './PaymentForm.css'
import {useState} from "react";
import useTimeLimit from "./useTimeLimit";

function PaymentForm() {
    const [amount, setAmount] = useState(0)
    const timeLimit = 10 * 60 * 1000;
    const redirectUrl = "/new-page";

    const {timeLeft, formatTime} = useTimeLimit(timeLimit, redirectUrl);
    return (
        <div className='container d-flex flex-column justify-content-center  min-vh-100'>
            <span className='d-block text-start text-black'>time left: {formatTime(timeLeft)}</span>
            <form className='rounded'>
                <div className='d-flex flex-column gap-2'>
                    <div className='inputBox'>
                        <span className='d-block'>Card Number</span>
                        <input className='border border-white rounded-2 w-100'/>
                    </div>
                    <div className='card-holder d-flex justify-content-center'>
                        <div className='inputBox firstname w-100'>
                            <span className='d-block'>firstname</span>
                            <input className='border border-white rounded-2 '/>
                        </div>
                        <div className='inputBox lastname w-100'>
                            <span className='d-block'>lastname</span>
                            <input className='border border-white rounded-2 '/>
                        </div>
                    </div>
                    <div className='card-detail d-flex w-100'>
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
                        <div className='inputBox'>
                            <span className='d-block p-1 text-star'>cvv2</span>
                            <input className='border border-white rounded-2 p-1 '/>
                        </div>
                    </div>
                    <div className='captcha-display d-flex justify-content-center'>
                        <div className='inputBox'>
                            <input readOnly={true} className='captcha border rounded-2 p-1 '/>
                        </div>
                        <button className='rounded-4'>rest</button>
                    </div>
                    <div className='lower-part d-flex justify-content-center '>
                        <div className='inputBox w-100'>
                            <span className='d-block p-1 text-star'>captcha</span>
                            <input className='border border-white rounded-2 p-1 w-100'/>
                        </div>
                        <div className='inputBox w-50'>
                            <span className='d-block p-1 text-star'>order id</span>
                            <input className='border border-white rounded-2 p-1 w-100'/>
                        </div>
                    </div>
                    <button className='rounded-4'>pay ${amount}</button>
                </div>
            </form>
        </div>
    )
}

export default PaymentForm