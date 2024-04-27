import React, { useEffect, useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import { useParams } from "react-router-dom";
import NewNavBar from "./NavBar";
import Footer from "./Footer";
import toast, { Toaster, ToastBar } from 'react-hot-toast';
const ChangePasswordForm = () => {
  const [formData, setFormData] = useState({
    previousPassword: "",
    newPassword: "",
    confirmPassword: "",
  });
  const navigate = useNavigate();
  const { queryParam } = useParams();
  const [formErrorMessage, setFormErrorMessage] = useState({
    newPasswordError: "",
    confirmPasswordError: "",
  });

  const [formValidity, setFormValidity] = useState({
    newPasswordField: false,
    confirmPasswordField: false,
  });

  const [showPreviousPassword, setShowPreviousPassword] = useState(false);
  const [showPassword, setShowPassword] = useState(false);
  const [showConfirmPassword, setShowConfirmPassword] = useState(false);

  const [passwordCriteriaVisible, setPasswordCriteriaVisible] = useState(false);
 const [imageUrl,setImageUrl]=useState("")
  const handleTogglePreviousPassword = () => {
    setShowPreviousPassword(!showPreviousPassword);
  };

  const handleTogglePassword = () => {
    setShowPassword(!showPassword);
  };

  const handleToggleConfirmPassword = () => {
    setShowConfirmPassword(!showConfirmPassword);
  };

  const showPasswordCriteria = () => {
    setPasswordCriteriaVisible(true);
  };

  const hidePasswordCriteria = () => {
    setPasswordCriteriaVisible(false);
  };

  const validateField = (fieldName, value, updatedFormData) => {
    if (fieldName === "newPassword") {
      const passwordRegex =
        /^(?=.*[A-Z])(?=.*[a-z])(?=.*\d)(?=.*[!@#$%^&*()_+])[A-Za-z\d!@#$%^&*()_+]{8,}$/;

      let error = "";
      let newFormErrorMessage = { ...formErrorMessage };
      let newFormValidity = { ...formValidity };

      if (!passwordRegex.test(value)) {
        error =
          "Password must contain at least 8 characters, 1 uppercase letter, 1 lowercase letter, 1 number, and 1 special character.";
        newFormValidity.newPasswordField = false;
      } else {
        newFormValidity.newPasswordField = true;
      }

      newFormErrorMessage.newPasswordError = error;
      setFormErrorMessage(newFormErrorMessage);
      setFormValidity(newFormValidity);
    }

    if (fieldName === "confirmPassword") {
      let error = "";
      let newFormErrorMessage = { ...formErrorMessage };
      let newFormValidity = { ...formValidity };

      if (updatedFormData.newPassword !== value) {
        error = "New password and confirm password do not match.";
        newFormValidity.confirmPasswordField = false;
      } else {
        newFormValidity.confirmPasswordField = true;
      }

      newFormErrorMessage.confirmPasswordError = error;
      setFormErrorMessage(newFormErrorMessage);
      setFormValidity(newFormValidity);
    }
  };
  const [token, setToken] = useState("");
  const [email, setemail] = useState("");
  const handlechange = (event) => {
    let { name, value } = event.target;

    setFormData((prevData) => {
      const updatedFormData = { ...prevData, [name]: value };
      validateField(name, value, updatedFormData);
      return updatedFormData;
    });
  };
  useEffect(() => {
    console.log("edsse", queryParam);
    const jwtToken = localStorage.getItem("token");
    const emailid = localStorage.getItem("email");
    const imageUrl=localStorage.getItem("imageUrl")
    setToken(jwtToken);
    setemail(emailid);
    setImageUrl(imageUrl);
  }, []);

  const post = {
    email: email,
    currentPassword: formData.previousPassword,
    newPassword: formData.newPassword,
  };
  const config = {
    headers: {
      token: token,
    },
  };
  const handleSubmit = async (event) => {
    event.preventDefault();

    const { previousPassword, newPassword, confirmPassword } = formData;

    if (!previousPassword || !newPassword || !confirmPassword) {
      // alert("Please enter all fields.");
      toast.error("Please enter all fields.");
      return;
    }
    const baseUrl = import.meta.env.VITE_BASE_URL;
    const apiUrl = `${baseUrl}/api/user/reset-password`;
    await axios
      .post(apiUrl, post, config)
      .then((response) => {
        if (response.status == 200) {
          // alert("Password Changed Successfully");
          toast.success("Password Changed Successfully");
          navigate("/");
        } else if (response.status == 500) {
          toast.error("Incorrect password");
        }
      })
      .catch((error) => {
        toast.error("Error Occured");
      });
  };

  return (
    <React.Fragment>
      <NewNavBar ImageUrl={imageUrl}/>
      <div className="flex w-full bg-base-300">
      <div className="grid w-4/6 flex-grow card bg-base-300 rounded-box place-items-center grid-cols-1 gap-6 my-6 px-4 md:px-6 lg:px-8">
        <form className="card-body w-full max-w-md" onSubmit={handleSubmit}>
          <h2 className="text-2xl font-bold mb-4 text-center">
            Change Password
          </h2>

          <div className="mb-4">
            <label className="label" htmlFor="previousPassword">
              <span className="label-text text-md font-bold">
                Previous Password
              </span>
            </label>
            <div className="relative">
              <input
                type={showPreviousPassword ? "text" : "password"}
                className="input input-bordered w-full"
                name="previousPassword"
                value={formData.previousPassword}
                onChange={handlechange}
                onFocus={showPasswordCriteria}
                onBlur={hidePasswordCriteria}
              />
              <button
                type="button"
                className="absolute inset-y-0 right-0 flex items-center pr-2"
                onClick={handleTogglePreviousPassword}
              >
                {showPreviousPassword ? (
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
                      d="M21 12a9 9 0 11-18 0 9 9 0 0118 0z"
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
          </div>

          <div className="mb-4">
            <label className="label" htmlFor="newPassword">
              <span className="label-text text-md font-bold">New Password</span>
            </label>
            <div className="relative">
              <input
                type={showPassword ? "text" : "password"}
                className={`input input-bordered w-full ${
                  formData.newPassword !== "" && !formValidity.newPasswordField
                    ? "border-red-500"
                    : formValidity.newPasswordField
                      ? "border-green-500"
                      : ""
                }`}
                name="newPassword"
                value={formData.newPassword}
                onChange={handlechange}
                onFocus={showPasswordCriteria}
                onBlur={hidePasswordCriteria}
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
                      d="M21 12a9 9 0 11-18 0 9 9 0 0118 0z"
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
            {formData.newPassword !== "" && (
              <div className="mt-2">
                {passwordCriteriaVisible ? (
                  <div>
                    <div className="flex items-center">
                      <span className="mr-2">
                        {formValidity.newPasswordField ? (
                          <svg
                            className="h-5 w-5 text-green-500"
                            fill="none"
                            stroke="currentColor"
                            viewBox="0 0 24 24"
                            xmlns="http://www.w3.org/2000/svg"
                          >
                            <path
                              strokeLinecap="round"
                              strokeLinejoin="round"
                              strokeWidth="2"
                              d="M5 13l4 4L19 7"
                            ></path>
                          </svg>
                        ) : (
                          <svg
                            className="h-5 w-5 text-red-500"
                            fill="none"
                            stroke="currentColor"
                            viewBox="0 0 24 24"
                            xmlns="http://www.w3.org/2000/svg"
                          >
                            <path
                              strokeLinecap="round"
                              strokeLinejoin="round"
                              strokeWidth="2"
                              d="M12 9v2m0 4h.01m-6 0h12"
                            ></path>
                          </svg>
                        )}
                      </span>
                      <span
                        className={`${
                          formValidity.newPasswordField
                            ? "text-green-500"
                            : "text-red-500"
                        }`}
                      >
                        {formValidity.newPasswordField
                          ? "New password meets the criteria ✓"
                          : formErrorMessage.newPasswordError}
                      </span>
                    </div>
                  </div>
                ) : null}
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
                onFocus={showPasswordCriteria}
                onBlur={hidePasswordCriteria}
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
                      d="M21 12a9 9 0 11-18 0 9 9 0 0118 0z"
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
              <div className="mt-2">
                {passwordCriteriaVisible ? (
                  <div>
                    <div className="flex items-center">
                      <span className="mr-2">
                        {formValidity.confirmPasswordField ? (
                          <svg
                            className="h-5 w-5 text-green-500"
                            fill="none"
                            stroke="currentColor"
                            viewBox="0 0 24 24"
                            xmlns="http://www.w3.org/2000/svg"
                          >
                            <path
                              strokeLinecap="round"
                              strokeLinejoin="round"
                              strokeWidth="2"
                              d="M5 13l4 4L19 7"
                            ></path>
                          </svg>
                        ) : (
                          <svg
                            className="h-5 w-5 text-red-500"
                            fill="none"
                            stroke="currentColor"
                            viewBox="0 0 24 24"
                            xmlns="http://www.w3.org/2000/svg"
                          >
                            <path
                              strokeLinecap="round"
                              strokeLinejoin="round"
                              strokeWidth="2"
                              d="M12 9v2m0 4h.01m-6 0h12"
                            ></path>
                          </svg>
                        )}
                      </span>
                      <span
                        className={`${
                          formValidity.confirmPasswordField
                            ? "text-green-500"
                            : "text-red-500"
                        }`}
                      >
                        {formValidity.confirmPasswordField
                          ? "Passwords match ✓"
                          : formErrorMessage.confirmPasswordError}
                      </span>
                    </div>
                  </div>
                ) : null}
              </div>
            )}
          </div>

          <div className="flex items-center justify-between">
            <button type="submit" className="btn btn-primary w-full">
              Submit
            </button>
          </div>
        </form>
      </div>
      </div>
      <Footer/>
      <Toaster />
    </React.Fragment>
  );
};

export default ChangePasswordForm;
