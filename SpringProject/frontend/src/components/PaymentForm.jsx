import React, {useEffect, useState} from "react";
import './PaymentForm.css'
import useTimeLimit from "./useTimeLimit";
import {getCaptchaNumber, getOrderData, sendPaymentData} from "../services/OrderService.js";

function PaymentForm() {

    const orderId = 2; // Change this to change the order you are paying for

    const timeLimit = 10 * 60 * 1000;
    const redirectUrl = "/timeout";
    const {timeLeft, formatTime} = useTimeLimit(timeLimit, redirectUrl);

    const [captcha, setCaptcha] = useState('')
    const [amount, setAmount] = useState(0)
    const [dto, setDto] = useState({
        cardNumber: '',
        firstname: '',
        lastname: '',
        expirationMonth: '',
        expirationYear: '',
        cvv2: '',
        captcha: '',
        amount: 0,
        orderId: orderId,
    });

    useEffect(() => {
        getOrderData(orderId)
            .then((response) => {
                console.log("Acquiring payment data")
                const {amount} = response.data;
                const {captcha} = response.data;
                const parsedAmount = parseFloat(amount);
                setCaptcha(captcha);
                setAmount(parsedAmount);
            })
            .catch((error) => {
                const errorMessage = error.response.data.message;
                window.alert(errorMessage)
            });
    }, []);

    useEffect(() => {

    }, [amount]);

    const restCaptcha = (event) => {
        event.preventDefault();
        getCaptchaNumber()
            .then((response) => {
                if (response.status === 200) {
                    console.log(`Acquired new captcha ${response.data}`);
                    setCaptcha(response.data);
                }
                else {
                    console.log("invalid response statues")
                }
            })
            .catch((error) => {
                const errorMessage = error.response.data.message;
                window.alert(errorMessage);
            });
    };

    const handelFormSubmit = (e) => {
        e.preventDefault();
        const form = e.target;
        form.reset();

        const putDto = {
            cardNumber: dto.cardNumber,
            firstname: dto.firstname,
            lastname: dto.lastname,
            expirationMonth: dto.expirationMonth,
            expirationYear: dto.expirationYear,
            cvv2: dto.cvv2,
            captcha: dto.captcha,
            amount: amount,
            orderId: dto.orderId,
        };


        sendPaymentData(putDto)
            .then((response) => {
                if (response.status === 200) {
                    setCaptcha(response.data);
                    window.location.href = '/successful';
                }
                else {
                    console.log("invalid response statues")
                }
            })
            .catch((error) => {
                const errorMessage = error.response.data.message;
                window.alert(errorMessage)
            });
    };

    const handelInput = (e) => {
        console.log("Adding form data to dto")
        e.preventDefault();
        const {name, value} = e.target;
        setDto((prevData) => ({
            ...prevData,
            [name]: value,
        }));
    };


    return (
        <div className='container d-flex flex-column justify-content-center  min-vh-100'>
            <span className='d-block text-start text-black'>time left: {formatTime(timeLeft)}</span>
            <form onSubmit={handelFormSubmit} method="get" id="nameform" className='rounded'>
                <div className='d-flex flex-column gap-2'>
                    <div className='inputBox'>
                        <span className='d-block'>Card Number</span>
                        <input name='cardNumber' onChange={handelInput}
                               className='border border-white rounded-2 w-100'/>
                    </div>
                    <div className='card-holder d-flex justify-content-center'>
                        <div className='inputBox firstname w-100'>
                            <span className='d-block'>firstname</span>
                            <input name='firstname' onChange={handelInput} className='border border-white rounded-2 '/>
                        </div>
                        <div className='inputBox lastname w-100'>
                            <span className='d-block'>lastname</span>
                            <input name='lastname' onChange={handelInput} className='border border-white rounded-2 '/>
                        </div>
                    </div>
                    <div className='card-detail d-flex w-100'>
                        <div className='inputBox'>
                            <span className='d-block p-1 text-start'>expiration mm</span>
                            <select name='expirationMonth' onChange={handelInput}
                                    className='month-input w-100 p-1 rounded border border-white'>
                                <option value='month' defaultValue='disabled'>month</option>
                                <option value='01'>01</option>
                                <option value='02'>02</option>
                                <option value='03'>03</option>
                                <option value='04'>04</option>
                                <option value='05'>05</option>
                                <option value='06'>06</option>
                                <option value='07'>07</option>
                                <option value='08'>08</option>
                                <option value='09'>09</option>
                                <option value='10'>10</option>
                                <option value='11'>11</option>
                                <option value='12'>12</option>
                            </select>
                        </div>
                        <div className='inputBox'>
                            <span className='d-block p-1 text-start'>expiration yy</span>
                            <select name='expirationYear' onChange={handelInput}
                                    className='year-input w-100 p-1 rounded border border-white'>
                                <option value='year' defaultValue='disabled'>year</option>
                                <option value='2024'>2024</option>
                                <option value='2025'>2025</option>
                                <option value='2026'>2026</option>
                                <option value='2027'>2027</option>
                                <option value='2028'>2028</option>
                                <option value='2029'>2029</option>
                                <option value='2030'>2030</option>
                                <option value='2031'>2031</option>
                                <option value='2032'>2032</option>
                                <option value='2033'>2033</option>
                            </select>
                        </div>
                        <div className='inputBox'>
                            <span className='d-block p-1 text-star'>cvv2</span>
                            <input name='cvv2' onChange={handelInput} className='border border-white rounded-2 p-1 '/>
                        </div>
                    </div>
                    <div className='captcha-display d-flex justify-content-center'>
                        <div className='inputBox'>
                            <input readOnly={true} value={captcha} className='captcha border rounded-2 p-1 '/>
                        </div>
                        <button onClick={restCaptcha} className='rounded-4'>rest</button>
                    </div>
                    <div className='lower-part d-flex justify-content-center '>
                        <div className='inputBox w-100'>
                            <span className='d-block p-1 text-star'>captcha</span>
                            <input onChange={handelInput} name='captcha'
                                   className='border border-white rounded-2 p-1 w-100'/>
                        </div>
                    </div>
                    <button type='submit' className='rounded-4'>pay ${amount}</button>
                </div>
            </form>
        </div>
    )
}

export default PaymentForm