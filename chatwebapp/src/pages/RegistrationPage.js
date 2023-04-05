import { useNavigate } from "react-router-dom";
import { useEffect, useRef, useState } from "react";
import axios from "axios";
import FormError from "../comps/FormError.js";

//images
import { ReactComponent as ErrorIcon } from "../imgs/error.svg";
import { ReactComponent as CheckmarkIcon } from "../imgs/checkmark.svg";


export default function RegistrationPage() {

    const form = useRef(null);
    const btn = useRef(null);
    const email = useRef(null);
    const username = useRef(null);
    const password = useRef(null);
    const confirm = useRef(null);
    const errorIcon = useRef(null);
    const checkmarkIcon = useRef(null);
    const errorBox = useRef(null);
    const [errorQue, setErrorQue] = useState([]);
    const navigate = useNavigate();

    useEffect(()=>{
        form.current.style.marginLeft = `-${form.current.scrollWidth/2}px`;
        form.current.style.marginTop = `-${form.current.offsetHeight/2}px`;
        errorBox.current.style.marginLeft = `${form.current.getBoundingClientRect().left+(form.current.offsetWidth/2)-(errorBox.current.offsetWidth/2)}px`;
        errorBox.current.style.marginTop = `${form.current.getBoundingClientRect().top-errorBox.current.offsetHeight-8}px`;
        btn.current.style.gridColumn = `span 20`;
    })

    const createAccount = (e) => {
        e.preventDefault();
        if (password.current.value !== confirm.current.value){
            let errorComponent = {
                text: "You failed to confirm your password!"
            }
            setErrorQue((prevErrorQue)=>[...prevErrorQue,errorComponent])
            return;
        }
        let request = {
            username: username.current.value,
            email: email.current.value,
            password: password.current.value
        }
        axios.post("http://localhost:8080/api/registration",request)
        .catch((error)=>{
            if (error.response.data.message.includes("<"+email.current.value+"> is not a valid")){
                let errorComponent = {
                    text: "E-mail adress is not valid"
                }
                setErrorQue((prevErrorQue)=>[...prevErrorQue,errorComponent])
                return;
            }
            let errorComponent = {
                text: error.response.data.message
            }
            setErrorQue((prevErrorQue)=>[...prevErrorQue,errorComponent])
        })
        .then((response)=>{
            if (response !== undefined){
                navigate("/registration/completed");
            }
        });
    }

    const matchPassword = (e) => {
        if (confirm.current.value === "" || password.current.value === "") {
            checkmarkIcon.current.style.display="none";
            errorIcon.current.style.display="none";
            return
        }
        if (password.current.value === confirm.current.value) {
            checkmarkIcon.current.style.display="block";
            errorIcon.current.style.display="none";
        } else {
            checkmarkIcon.current.style.display="none";
            errorIcon.current.style.display="block";
        }
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
                <p>E-Mail</p>
            </div>
            <div className="img">
            </div>
            <div className="inp">
                <input type="text" ref={email} ></input>
            </div>
            <div className="par">
                <p>Password</p>
            </div>
            <div className="img">
            </div>
            <div className="inp">
                <input type="password" ref={password} onChange={matchPassword}></input>
            </div>
            <div className="par">
                <p>Confirm Password</p>
            </div>
            <div className="img">
                <ErrorIcon className="icon" ref={errorIcon} display="none" height="40px" width="40px"/>
                <CheckmarkIcon className="icon" ref={checkmarkIcon} display="none" height="40px" width="40px"/>
            </div>
            <div className="inp">
                <input type="password" ref={confirm} onChange={matchPassword} ></input>
            </div>
            <div className="btn" ref={btn} >
                <button onClick={createAccount}>Create Account</button>
            </div>
        </div>
        </div>

    );
}