import { useEffect, useRef } from "react"
import { ReactComponent as ErrorIcon } from "../imgs/error.svg"

export default function FormError(props){

    const error = useRef(null);

    useEffect(()=>{
        const timer = setTimeout(()=>{
            error.current.style.display = "none";
        },4000);

        return () => clearTimeout(timer);

    },[]);

    return (
        <div className="error--component" ref={error} >
            <ErrorIcon width="25px" height="25px"/>
            <p>{props.text}</p>
        </div>
    );
}