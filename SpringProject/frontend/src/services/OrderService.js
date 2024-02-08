import axios from "axios";

axios.defaults.headers.common['Access-Control-Allow-Origin'] = 'http://localhost:3000';

const REST_API_FETCH_ORDER_DATA_URL = "http://localhost:8080/orders/payment"
const REST_API_POST_PAYMENT_DATA_URL = "http://localhost:8080/orders/payment/onlinePayment"
const REST_API_FETCH_CAPTCHA_URL = "http://localhost:8080/orders/payment/getCaptcha"

export const getOrderData = (id) => axios.get(REST_API_FETCH_ORDER_DATA_URL, {
    params: {
        id: Number(id)
    }
});

export const getCaptchaNumber = () => axios.get(REST_API_FETCH_CAPTCHA_URL);

export const sendPaymentData = (cardPaymentDto) => axios.put(REST_API_POST_PAYMENT_DATA_URL,cardPaymentDto );