import { useEffect, useState } from 'react';
import {useParams} from 'react-router-dom';
import axios from "axios";

//comps
import MessageDefaultRedirect from '../comps/MessageDefaultRedirect';

export default function RegistrationConfirmationPage() {

    const {token} = useParams();
    const [message,setMessage] = useState(null);

    useEffect(() => {
        console.log("asd");
        axios.get(`http://localhost:8080/api/registration/confirm?token=${token}`)
        .catch((error) => {
            setMessage(error.response.data.message);
        })
        .then((response) => {
            if (response !== undefined){
                setMessage("Account confirmation was successful");
            }
        });
    },[]);

    return (
        <div>
            <MessageDefaultRedirect message={message} />
        </div>
    );


}