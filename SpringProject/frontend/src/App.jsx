import PaymentForm from "./components/PaymentForm.jsx";
import {BrowserRouter,Routes,Route} from "react-router-dom";
import './App.css'
import TimeOut from "./components/TimeOut.jsx";
import SuccessfulPayment from "./components/SuccessfulPayment.jsx";


function App() {
    return (
        <>
            <BrowserRouter>
                <Routes>
                    <Route path='/' element={<PaymentForm />} />
                    <Route path='/timeout' element={<TimeOut />} />
                    <Route path='/successful' element={<SuccessfulPayment />} />
                </Routes>
            </BrowserRouter>
        </>
    )
}

export default App