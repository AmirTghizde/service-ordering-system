import PaymentForm from "./components/PaymentForm.jsx";
import {BrowserRouter,Routes,Route} from "react-router-dom";
import './App.css'
import TimeOut from "./components/TimeOut.jsx";


function App() {
    return (
        <>
            <BrowserRouter>
                <Routes>
                    <Route path='/' element={<PaymentForm />} />
                    <Route path='/timeout' element={<TimeOut />} />
                </Routes>
            </BrowserRouter>
        </>
    )
}

export default App