import { useNavigate } from "react-router-dom";
import { useEffect, useRef, useState } from "react";
import axios from "axios";

//comps
import FormError from "../comps/FormError";

export default function LoginPage() {


    const username = useRef(null);
    const password = useRef(null);
    const form = useRef(null);
    const [errorQue, setErrorQue] = useState([]);
    const errorBox = useRef(null);
    const navigate = useNavigate();

    useEffect(()=>{
        form.current.style.marginLeft = `-${form.current.scrollWidth/2}px`;
        form.current.style.marginTop = `-${form.current.offsetHeight/2}px`;
        errorBox.current.style.marginLeft = `${form.current.getBoundingClientRect().left+(form.current.offsetWidth/2)-(errorBox.current.offsetWidth/2)}px`;
        errorBox.current.style.marginTop = `${form.current.getBoundingClientRect().top-errorBox.current.offsetHeight-8}px`;
    });

    const registerButton = (e) => {
        e.preventDefault();
        navigate("/registration");
    }

    const loginButton = (e) => {
        console.log(username.current.value+":"+password.current.value)
        let config = {
            headers: {
                "Authorization": "Basic "+btoa(username.current.value+":"+password.current.value)
            }
        }
        axios.get("http://localhost:8080/api/auth",config)
        .then((response)=>{
            console.log(response);
        })
        .catch((error)=>{
            console.log(error);
            let errorComponent = {
                text: "Incorrect Username or Password"
            }
            setErrorQue((prevErrorQue)=>[...prevErrorQue,errorComponent])
        });
    }


    return (

        <div>
        <div className="error--box" ref={errorBox}>
            {errorQue.map((item,index) => {
                return (<FormError key={index} text={item.text}/>)
            })}
        </div>
        <div className="login--form" ref={form} >
            <div className="par">
            <p>Username</p>
            </div>
            <div className="img">
            </div>
            <div className="inp">
            <input type="text" ref={username} ></input>
            </div>
            <div className="par">
            <p>Password</p>
            </div>
            <div className="img">
            </div>
            <div className="inp" >
            <input type="password" ref={password} ></input>
            </div>
            <div className="btn">
            <button onClick={loginButton} >Login</button>
            </div>
            <div className="btn">
            <button onClick={registerButton} >Register</button>
            </div>
        </div>
        </div>

    );
}