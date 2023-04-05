import { useEffect, useRef, useState } from "react"
import { useNavigate } from "react-router-dom";

export default function MessageDefaultRedirect(props) {

    const navigate = useNavigate();
    const mainRect = useRef(null);
    const [counter,setCounter] = useState(20);

    useEffect(() => {
        mainRect.current.style.marginLeft = `-${mainRect.current.scrollWidth/2}px`;
        mainRect.current.style.marginTop = `-${mainRect.current.scrollHeight/2}px`;
        console.log(mainRect.current.scrollWidth/2);
        console.log(mainRect.current.offsetWidth/2);
    });

    useEffect(() => {
        const timeout = setTimeout(()=>{
            setCounter(counter-1);
        },1000);
        
        if (counter ===0){
            navigate("/");
        }

        return () => clearTimeout(timeout);
    },[counter]);


    return (
        <div className="message--default--redirect" ref={mainRect}>
            <p>{props.message}</p>
            <p>You will be redirected to the login page in {counter}.</p>
            <p>Click <a href="http://localhost:3000">here</a> to go to the login page now</p>
        </div>
    );
}