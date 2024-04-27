import AuthenticationPage from './AuthenticationPage';
import logo from '../assets/A.svg';
import { useState, useEffect } from 'react';
import axios from 'axios';
import toast, { Toaster, ToastBar } from 'react-hot-toast';

const AuthCard = (props) => {
    return (
        <div className="card shrink-0 w-full max-w-sm shadow-2xl bg-base-100">
            {props.children}
        </div>
    );
};

const ResetPassword = () => {
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [passwordError, setPasswordError] = useState('');
    const [confirmPasswordError, setConfirmPasswordError] = useState('');
    const [token, setToken] = useState('');


    useEffect(() => {
        const searchParams = new URLSearchParams(window.location.search);
        const tokenParam = searchParams.get('token');
 
        if (tokenParam ) {
            setToken(tokenParam);
       
        } else {
            console.error('Token or email is missing from URL.');
        }
    }, []);

    const handlePasswordChange = (e) => {
        const newPassword = e.target.value;
        setPassword(newPassword);
        validatePassword(newPassword);
    };

    const handleConfirmPasswordChange = (e) => {
        const newConfirmPassword = e.target.value;
        setConfirmPassword(newConfirmPassword);
        validateConfirmPassword(newConfirmPassword);
    };

    const validatePassword = (newPassword) => {
        if (!newPassword.match(/^(?=.*[A-Z])(?=.*[a-z])(?=.*\d)(?=.*[!@#$%^&*])[A-Za-z\d!@#$%^&*]{8,}$/)) {
            setPasswordError('Password does not meet criteria');
        } else {
            setPasswordError('');
        }
    };

    const validateConfirmPassword = (newConfirmPassword) => {
        if (newConfirmPassword !== password) {
            setConfirmPasswordError("Passwords don't match");
        } else {
            setConfirmPasswordError('');
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        
        if (password !== confirmPassword) {
            // alert("Passwords don't match");
            toast.error("Passwords don't match");
            return;
        }
        if (passwordError || confirmPasswordError) {
            // alert('Please fix password errors before submitting.');
            toast.error('Please fix password errors before submitting.');
            return;
        }

        try {
            const postData = {
                "newPassword": password,
              };
              console.log("postData",postData)
            const baseUrl = import.meta.env.VITE_BASE_URL;
            const apiUrl = `${baseUrl}/api/user/reset-password/token?token=${token}`;
            const response = await axios.post(apiUrl,postData );
            // alert("Password reset successfully")
            toast.success("Password reset successfully")
            console.log('Server response:', response);
  
        } catch (error) {
            console.log("ss")
            // alert("something went wrong")
            toast.error("something went wrong")
            console.error('Error submitting password:', error);
        }
    };

    return (
        <>
            <Toaster />
            <AuthenticationPage>
                <img src={logo} alt="Logo" className="m-8" />
                <div className="text-center lg:text-left grow">
                    <h1 className="text-5xl font-bold">AlumConnect</h1>
                    <p className="py-6">Forgot Password</p>
                </div>

                <AuthCard>
                    <form className='card-body' onSubmit={handleSubmit}>
                      
                        <div className="form-control relative">
                            <label className="label">
                                <span className="label-text text-md font-bold">Password</span>
                            </label>
                            <input
                                type="password"
                                placeholder="password"
                                className="input input-bordered pr-12"
                                required
                                value={password}
                                onChange={handlePasswordChange}
                            />
                            {passwordError && <span className="text-red-500">{passwordError}</span>}
                        </div>

                        <div className="form-control relative">
                            <label className="label">
                                <span className="label-text text-md font-bold">Confirm Password</span>
                            </label>
                            <input
                                type="password"
                                placeholder="password"
                                className="input input-bordered pr-12"
                                required
                                value={confirmPassword}
                                onChange={handleConfirmPasswordChange}
                            />
                            {confirmPasswordError && <span className="text-red-500">{confirmPasswordError}</span>}
                        </div>

                        <button type="submit" className="btn btn-primary mt-4">Submit</button>

                    </form>
                </AuthCard>
            </AuthenticationPage>
        </>
    );
};

export default ResetPassword;
