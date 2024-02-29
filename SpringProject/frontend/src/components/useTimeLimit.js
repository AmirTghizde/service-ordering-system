import React, {useEffect, useState} from "react";

const useTimeLimit = (timeLimit, redirectUrl) => {
    const [timeLeft, setTimeLeft] = useState(timeLimit);

    useEffect(() => {
        const timer = setInterval(() => {
            setTimeLeft((prevTimeLeft) => prevTimeLeft - 1000);
        }, 1000);

        return () => {
            clearInterval(timer);
        };
    }, []);

    useEffect(() => {
        if (timeLeft <= 0) {
            window.location.href = redirectUrl;
        }
    }, [timeLeft, redirectUrl]);

    const formatTime = (milliseconds) => {
        const minutes = Math.floor(milliseconds / 60000);
        const seconds = Math.floor((milliseconds % 60000) / 1000);
        return `${minutes.toString().padStart(2, "0")}:${seconds.toString().padStart(2, "0")}`;
    };

    return {
        timeLeft,
        formatTime,
    };
};

export default useTimeLimit;