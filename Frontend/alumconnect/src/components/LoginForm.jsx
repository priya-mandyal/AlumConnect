import { useState, useEffect, ReactComponentElement, useRef, useCallback } from "react";
import { RegistrationForm } from "./Registration";
import axios from "axios";
import Home from "./Home";
import { useNavigate } from "react-router-dom";
import { EyeSlashIcon } from "@heroicons/react/24/solid";
import logo from '../assets/A.svg';
import toast, { Toaster, ToastBar } from 'react-hot-toast';


const RegistrationModal = (props) => {
  return (
    <>
      <dialog ref={props.parentModalRef} id="RegistrationModal" className="modal">
        <div className="modal-box">
          <form method="dialog">
            <button className="btn btn-sm btn-circle btn-ghost absolute right-2 top-2">
              ✕
            </button>
          </form>
          <form action="">
            <h3 className="font-bold text-3xl text-center">Register as?</h3>
          </form>
          <div className="flex justify-around mt-5">
            <button className="btn" onClick={() => props.changeUser("Student")}>
              Student
            </button>
            <button className="btn" onClick={() => props.changeUser("Alumni")}>
              Alumni
            </button>
          </div>
          {props.children}
        </div>
        <Toaster />
      </dialog>
    </>
  );
};

const ForgotPasswordForm = (props) => {
  const [email, setEmail] = useState("");
  const handleEmailChange = (e) => {
    setEmail(e.target.value);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const baseUrl = import.meta.env.VITE_BASE_URL;
    const apiUrl = `${baseUrl}/api/user/forgot-password`;
    await axios
      .post(apiUrl + "?email=" + email)
      .then((response) => {
        console.log(response.status);
        if (response.data.message == "Email Id is not registered") {
          // alert("No Email Id is registered with this Emaild");
          toast.error("No Email Id is registered with this Emaild");
        } else if (response.status == 200) {
          toast.success("Password Reset form is sent to your Email-Id");
        }
      })
      .catch((error) => {
        toast.error("Internal Error Occured");
      });
  };
  return (
    <dialog id="forgotPasswordForm" className="modal">
      <Toaster />
      <div className="modal-box max-w-sm">
        <form method="dialog">
          <button className="btn btn-sm btn-circle btn-ghost absolute right-2 top-2">
            ✕
          </button>
        </form>
        <form action="" onSubmit={handleSubmit}>
          <h3 className="font-bold text-lg text-center">
            Forgot your password?
          </h3>
          <div className="form-control">
            <label className="label justify-center">
              <span className="label-text">
                An email will be sent to your email address
              </span>
            </label>
            <input
              type="email"
              placeholder="email"
              className="input input-bordered my-2"
              required
              value={email}
              onChange={handleEmailChange}
            />
            <button className="btn btn-primary">Submit</button>
          </div>
        </form>
      </div>
    </dialog>
  );
};

const LoginForm = (props) => {
  const navigate = useNavigate();
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  const handleEmailChange = (e) => {
    setEmail(e.target.value);
  };
  const handlePasswordChange = (e) => {
    setPassword(e.target.value);
  };
  const [token, setToken] = useState("");
  const [emailId, setemail] = useState("");
  const [redirectToHome, setRedirectToHome] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();

    const formData = {
      email: email,
      password: password,
    };
    const baseUrl = import.meta.env.VITE_BASE_URL;
    const apiUrl = `${baseUrl}/api/auth/login`;

    try {
      const response = await axios.post(apiUrl, formData);

      const { success, data } = response.data;
      if (success) {
        localStorage.setItem("token", data);
        localStorage.setItem("email", formData.email);
        localStorage.setItem("role", response.data.role);
        localStorage.setItem("firstName", response.data.firstName);
        localStorage.setItem("lastName", response.data.lastName);
        localStorage.setItem("userId", response.data.userId);
        localStorage.setItem("projectId", response.data.projectID);
        setToken(data);
        setemail(formData.email);
        setRedirectToHome(true);

        if (response.data.role === "STUDENT") {
          const student_res = await axios.get(
            `${baseUrl}/v1/student/get/${response.data.userId}`,
          );
          if (
            student_res.data.data.academicDetails.length === 0 &&
            student_res.data.data.interests.length === 0 &&
            student_res.data.data.hobbyForm === null &&
            student_res.data.data.studentSummary === null
          ) {
            navigate("/studentprofile");
          } else {
            navigate("/home");
          }
        } else if (response.data.role === "ALUMNI") {
          const alum_res = await axios.get(
            `${baseUrl}/api/alumni-profiles/${response.data.userId}`,
          );
          if (
            alum_res.data.data.academicDetails.length === 0 &&
            alum_res.data.data.interests.length === 0 &&
            alum_res.data.data.availability === null &&
            alum_res.data.data.professionalJourney === null
          ) {
            navigate("/alumniprofile");
          } else {
            navigate("/home");
          }
        }
      }
    } catch (error) {
      console.error("Error during login:", error);
      if (/Incorrect password/.test(error.response.data)) {
      //  alert("Password is incorrect");
       console.log("Password is incorrect")
       toast.error("Password is incorrect");
      } else if (/User not found/.test(error.response.data)) {
        // alert("User not found");
        toast.error("User not found");
      } 
    }
    
    
  }

  return (
    <div className="card-body">
      <Toaster />
      <form onSubmit={handleSubmit}>
    
        <div className="form-control">
          <label className="label">
            <span className="label-text text-md font-bold">Email</span>
          </label>
          <input
            type="email"
            placeholder="email"
            className="input input-bordered"
            required
            value={email}
            onChange={handleEmailChange}
          />
        </div>
        <div className="form-control">
          <label className="label">
            <span className="label-text text-md font-bold">Password</span>
          </label>
          <input
            type="password"
            placeholder="password"
            className="input input-bordered"
            required
            value={password}
            onChange={handlePasswordChange}
          />
          <label className="label">
            <button
              onClick={() =>
                document.getElementById("forgotPasswordForm").showModal()
              }
            >
              <a href="#" className="label-text-alt link link-hover">
                Forgot password?
              </a>
            </button>
          </label>
        </div>
        <div className="form-control mt-6">
          <button className="btn btn-primary mb-3">Login</button>
        </div>
      </form>
      <button
        onClick={() =>
          document.getElementById("RegistrationModal").showModal()
        }
      >
        Register
      </button>
    </div>
  );
};

const AuthCard = (props) => {
  return (
    <div className="card shrink-0 w-full max-w-sm shadow-2xl bg-base-100">
      {props.children}
    </div>
  );
};

function LoginAuth(props) {
  const [registerUser, setRegisterUser] = useState("Student");
  const parentModalRef = useRef(null);

  return (
    <>
      <img src={logo} alt="Logo" className="m-8" />
      <div className="text-center lg:text-left grow">
        <h1 className="text-5xl font-bold">AlumConnect</h1>
        <p className="py-6">Welcome to AlumConnect</p>
      </div>

      <AuthCard>
        <LoginForm />
      </AuthCard>
      <ForgotPasswordForm />

      <RegistrationModal changeUser={setRegisterUser} parentModalRef={parentModalRef}>
        <RegistrationForm
          type={registerUser}
          role={registerUser.toUpperCase()}
          parentModalRef={parentModalRef}
        />
      </RegistrationModal>
    </>
  );
}

export { AuthCard, LoginForm, RegistrationModal };
export default LoginAuth;
