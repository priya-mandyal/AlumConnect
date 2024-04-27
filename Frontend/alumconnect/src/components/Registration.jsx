import React, { useEffect, useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import toast, { Toaster, ToastBar } from 'react-hot-toast';

const RegistrationForm = props => {
  const [formData, setFormData] = useState({
    studentId: "",
    firstName: "",
    lastName: "",
    emailId: "",
    gender: "",
    password: "",
    confirmPassword: "",
    role: props.role,
  });
  const navigate = useNavigate();
  useEffect(() => {
    setFormData({ ...formData, role: props.role });
  }, [props.role]);

  const [formErrorMessage, setFormErrorMessage] = useState({
    emailIdError: "",
    passwordError: "",
    confirmPasswordError: "",
  });

  const [formValidity, setFormValidity] = useState({
    emailIdField: false,
    passwordField: false,
    confirmPasswordField: false,
  });

  const [showPassword, setShowPassword] = useState(false);
  const [showConfirmPassword, setShowConfirmPassword] = useState(false);

  const handleTogglePassword = () => {
    setShowPassword(!showPassword);
  };

  const handleToggleConfirmPassword = () => {
    setShowConfirmPassword(!showConfirmPassword);
  };

  const validateField = (fieldName, value, updatedFormData) => {
    let error = "";
    let newFormErrorMessage = { ...formErrorMessage };
    let newFormValidity = { ...formValidity };
    const emailRegexStudent = /^[A-Za-z0-9._%+-]+@dal\.ca$/;
    const emailRegexOthers = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    const passwordRegex =
      /^(?=.*[A-Z])(?=.*[a-z])(?=.*\d)(?=.*[!@#$%^&*()_+])[A-Za-z\d!@#$%^&*()_+]{8,}$/;

    if (fieldName === "emailId") {
      const emailRegex =
        props.type === "Student" ? emailRegexStudent : emailRegexOthers;
      if (!emailRegex.test(value)) {
        error = `Please enter a valid ${props.type === "Student" ? "Dalhousie" : "email"} id`;
        newFormValidity.emailIdField = false;
      } else if (error === "") {
        newFormValidity.emailIdField = true;
      }

      newFormErrorMessage.emailIdError = error;
    }

    if (fieldName === "password") {
      if (!passwordRegex.test(value)) {
        error = "Password does not meet the criteria";
        newFormValidity.passwordField = false;
        newFormValidity.confirmPasswordField = false;
      } else if (error === "") {
        newFormValidity.passwordField = true;
      }

      if (updatedFormData.password === updatedFormData.confirmPassword) {
        newFormValidity.confirmPasswordField = true;
      }

      if (updatedFormData.password !== updatedFormData.confirmPassword) {
        newFormValidity.confirmPasswordField = false;
      }

      newFormErrorMessage.passwordError = error;
    }

    if (fieldName === "confirmPassword") {
      if (updatedFormData.password !== value) {
        error = "Your Password does not match";
        newFormValidity.confirmPasswordField = false;
      } else if (error === "") {
        newFormValidity.confirmPasswordField = true;
      }

      newFormErrorMessage.confirmPasswordError = error;
    }

    setFormErrorMessage(newFormErrorMessage);
    setFormValidity(newFormValidity);
  };

  const handlechange = (event) => {
    let { name, value } = event.target;

    setFormData((prevData) => {
      const updatedFormData = { ...prevData, [name]: value };
      validateField(name, value, updatedFormData);
      return updatedFormData;
    });
  };

  const handleSubmit = (event) => {
    event.preventDefault();
    const { emailIdField, passwordField, confirmPasswordField } = formValidity;

    if (props.type === "Student") {
      const {
        studentId,
        emailId,
        gender,
        password,
        confirmPassword,
        firstName,
        lastName,
      } = formData;

      if (
        !studentId ||
        !emailId ||
        !gender ||
        !password ||
        !confirmPassword ||
        !firstName ||
        !lastName
      ) {
        // alert("Field cannot be empty");
        toast.error("Field cannot be empty");
        return;
      }
    } else if (props.type === "Alumni" || props.type === "Admin") {
      const {
        emailId,
        gender,
        password,
        confirmPassword,
        firstName,
        lastName,
      } = formData;

      if (
        !emailId ||
        !gender ||
        !password ||
        !confirmPassword ||
        !firstName ||
        !lastName
      ) {
        // alert("Field cannot be empty");
        toast.error("Field cannot be empty");
        return;
      }
    } else if (!emailIdField) {
      // alert("Email Form is incorrect and hence cannot submit Form");
      toast.error("Email Form is incorrect and hence cannot submit Form");
    } else if (!passwordField || !confirmPasswordField) {
      // alert("Your password field is wrong cannot submit");
      toast.error("Your password field is wrong cannot submit");
    }

    if (emailIdField && passwordField && confirmPasswordField) {
    
      console.log("Repsonse", formData);
      const baseUrl = import.meta.env.VITE_BASE_URL;
      const apiUrl = `${baseUrl}/api/auth/register`;
      axios
        .post(apiUrl, formData)
        .then((response) => {
          if (response.data.success === true) {
            props.parentModalRef.current.close();
            // alert(response.data.message);
            toast.success(response.data.message);
          } else if (response.data.success === false) {
            // alert(response.data.message);
            toast.error(response.data.message);
          }
        })
        .catch((error) => {
        
          console.error("Error making POST request:", error);
        });

      return;
    }

    if (!emailIdField || !passwordField || !confirmPasswordField) {
      // alert("please enter all details")
      toast("please enter all details")
      return;
    }
  };

  return (
    <React.Fragment>
      <form className="card-body" onSubmit={handleSubmit}>
        <h2 className="text-2xl font-bold mb-4 text-center">
          {props.type} Registration
        </h2>
        {props.type === "Student" ? (
          <div className="mb-4">
            <label className="label" htmlFor="StudentId">
              <span className="label-text text-md font-bold">Student Id</span>
            </label>
            <input
              type="text"
              className="input input-bordered w-full"
              name="studentId"
              value={formData.studentId}
              onChange={handlechange}
            />
          </div>
        ) : null}

        <div className="mb-4 flex flex-1 justify-between">
          <div className="form-control w-1/2 mr-1">
            <label className="label" htmlFor="firstName">
              <span className="label-text text-md font-bold">First Name</span>
            </label>
            <input
              type="text"
              className="input input-bordered"
              name="firstName"
              value={formData.firstName}
              onChange={handlechange}
            />
          </div>

          <div className="form-control w-1/2">
            <label className="label" htmlFor="lastName">
              <span className="label-text text-md font-bold">Last Name</span>
            </label>
            <input
              type="text"
              className="input input-bordered"
              name="lastName"
              value={formData.lastName}
              onChange={handlechange}
            />
          </div>
        </div>

        <div className="mb-4">
          <label className="label" htmlFor="emailId">
            <span className="label-text text-md font-bold">Email Id</span>
          </label>
          <input
            type="text"
            className={`input input-bordered w-full ${
              formData.emailId !== "" && !formValidity.emailIdField
                ? "border-red-500"
                : formValidity.emailIdField
                  ? "border-green-500"
                  : ""
            }`}
            name="emailId"
            value={formData.emailId}
            onChange={handlechange}
          />

          {formData.emailId !== "" && (
            <div
              className={`mt-2 text-xs ${formValidity.emailIdField ? "text-green-500" : "text-red-500"}`}
            >
              {formValidity.emailIdField ? (
                <span className="flex items-center">
                  Email-id format correct ✓
                </span>
              ) : (
                <span className="flex items-center">
                  {formErrorMessage.emailIdError}
                </span>
              )}
            </div>
          )}
        </div>

        <div className="mb-4">
          <label className="label" htmlFor="gender">
            <span className="label-text text-md font-bold">Gender</span>
          </label>
          <select
            name="gender"
            className="input input-bordered w-full"
            value={formData.gender}
            onChange={handlechange}
          >
            <option value="" disabled>
              Select Gender
            </option>
            <option value="MALE">Male</option>
            <option value="FEMALE">Female</option>
          </select>
        </div>

        <div className="mb-4">
          <label className="label" htmlFor="password">
            <span className="label-text text-md font-bold">Password</span>
          </label>
          <div className="relative">
            <input
              type={showPassword ? "text" : "password"}
              className={`input input-bordered w-full ${
                formData.password !== "" && !formValidity.passwordField
                  ? "border-red-500"
                  : formValidity.passwordField
                    ? "border-green-500"
                    : ""
              }`}
              name="password"
              value={formData.password}
              onChange={handlechange}
            />
            <button
              type="button"
              className="absolute inset-y-0 right-0 flex items-center pr-2"
              onClick={handleTogglePassword}
            >
              {showPassword ? (
                <svg
                  className="h-5 w-5 text-gray-500 cursor-pointer"
                  fill="none"
                  stroke="currentColor"
                  viewBox="0 0 24 24"
                  xmlns="http://www.w3.org/2000/svg"
                >
                  <path
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    strokeWidth="2"
                    d="M15 12a3 3 0 11-6 0 3 3 0 016 0z"
                  ></path>
                  <path
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    strokeWidth="2"
                    d="M2 12s3 8 10 8 10-8 10-8-3-8-10-8-10 8-10 8z"
                  ></path>
                </svg>
              ) : (
                <svg
                  className="h-5 w-5 text-gray-500 cursor-pointer"
                  fill="none"
                  stroke="currentColor"
                  viewBox="0 0 24 24"
                  xmlns="http://www.w3.org/2000/svg"
                >
                  <path
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    strokeWidth="2"
                    d="M15 12a3 3 0 11-6 0 3 3 0 016 0z"
                  ></path>
                  <path
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    strokeWidth="2"
                    d="M2 12s3 8 10 8 10-8 10-8-3-8-10-8-10 8-10 8z"
                  ></path>
                  <path
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    strokeWidth="2"
                    d="M15 15l-3-3m0 0l-3 3m3-3l3 3m-3-3l3-3"
                  ></path>
                </svg>
              )}
            </button>
          </div>
          {formData.password !== "" && (
            <div
              className={`mt-2 text-xs ${formValidity.passwordField ? "text-green-500" : "text-red-500"}`}
            >
              {formValidity.passwordField ? (
                <span className="flex items-center">
                  Password strength is good ✓
                </span>
              ) : (
                <span className="flex items-center">Weak password ✗</span>
              )}
            </div>
          )}
        </div>

        <div className="mb-6">
          <label className="label" htmlFor="confirmPassword">
            <span className="label-text text-md font-bold">
              Confirm Password
            </span>
          </label>
          <div className="relative">
            <input
              type={showConfirmPassword ? "text" : "password"}
              name="confirmPassword"
              className={`input input-bordered w-full ${
                formData.confirmPassword !== "" &&
                !formValidity.confirmPasswordField
                  ? "border-red-500"
                  : formValidity.confirmPasswordField
                    ? "border-green-500"
                    : ""
              }`}
              value={formData.confirmPassword}
              onChange={handlechange}
            />
            <button
              type="button"
              className="absolute inset-y-0 right-0 flex items-center pr-2"
              onClick={handleToggleConfirmPassword}
            >
              {showConfirmPassword ? (
                <svg
                  className="h-5 w-5 text-gray-500 cursor-pointer"
                  fill="none"
                  stroke="currentColor"
                  viewBox="0 0 24 24"
                  xmlns="http://www.w3.org/2000/svg"
                >
                  <path
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    strokeWidth="2"
                    d="M15 12a3 3 0 11-6 0 3 3 0 016 0z"
                  ></path>
                  <path
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    strokeWidth="2"
                    d="M2 12s3 8 10 8 10-8 10-8-3-8-10-8-10 8-10 8z"
                  ></path>
                </svg>
              ) : (
                <svg
                  className="h-5 w-5 text-gray-500 cursor-pointer"
                  fill="none"
                  stroke="currentColor"
                  viewBox="0 0 24 24"
                  xmlns="http://www.w3.org/2000/svg"
                >
                  <path
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    strokeWidth="2"
                    d="M15 12a3 3 0 11-6 0 3 3 0 016 0z"
                  ></path>
                  <path
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    strokeWidth="2"
                    d="M2 12s3 8 10 8 10-8 10-8-3-8-10-8-10 8-10 8z"
                  ></path>
                  <path
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    strokeWidth="2"
                    d="M15 15l-3-3m0 0l-3 3m3-3l3 3m-3-3l3-3"
                  ></path>
                </svg>
              )}
            </button>
          </div>

          {formData.confirmPassword !== "" && (
            <div
              className={`mt-2 text-xs ${formValidity.confirmPasswordField ? "text-green-500" : "text-red-500"}`}
            >
              {formValidity.confirmPasswordField ? (
                <span className="flex items-center">Passwords match ✓</span>
              ) : (
                <span className="flex items-center">
                  Passwords do not match ✗
                </span>
              )}
            </div>
          )}
        </div>

        <div className="flex items-center justify-between">
          <button type="submit" className="btn btn-primary w-full">
            {" "}
            Register{" "}
          </button>
        </div>
      </form>
    </React.Fragment>
  );
};

const Registration = ({ type, role }) => {
  return (
    <div className="card shrink-0 w-full max-w-md shadow-2xl bg-base-100">
      <RegistrationForm type={type} role={role} />
    </div>
  );
};

export { RegistrationForm };
export default Registration;
