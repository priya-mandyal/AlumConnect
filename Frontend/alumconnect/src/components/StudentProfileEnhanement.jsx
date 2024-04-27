import React, { useState, useEffect } from "react";
import Interest from "./Interest";
import AcademicDetailsForm from "./AcademicDetailsForm";
import ProfilePictureUpload from "./ProfilePictureUpload";
import StudentSummary from "./StudentSummary";
import HobbyForm from "./HobbyForm";
import axios from "axios";
import { useNavigate } from "react-router-dom";

const StudentProfileEnhancement = () => {
  const [formData, setFormData] = useState({
    interests: [],
    academicDetails: {},
    studentSummary: "",
    email: "",
    hobbyForm: "",
  });

  const [step, setStep] = useState(1);
  const [token, setToken] = useState("");
  const [email, setemail] = useState("");

  useEffect(() => {
    const jwtToken = localStorage.getItem("token");
    setToken(jwtToken);
    const userEmail = localStorage.getItem("email");
    console.log("error", userEmail);
    setemail(userEmail);

    if (formData.hobbyForm) {
      console.log("Final", formData);
      console.log("ee", email);
      console.log("tok", token);
      formData.email = email;
      console.log(formData);
      const baseUrl = import.meta.env.VITE_BASE_URL;
      const apiUrl = `${baseUrl}/v1/student/save`;
      axios
        .post(apiUrl, formData, config)
        .then((response) => {
          if (response.status === 200) {
            console.log(response);
            localStorage.setItem("role", response.data.role);
          } else if (response.status === 500) {
            alert("");
          }
        })
        .catch((error) => {
          alert("Error Occurred");
        });
    }
  }, [formData]);

  useEffect(() => {
    setFormData((prevData) => ({ ...prevData, email: email }));
  }, [setemail]);

  const config = {
    headers: {
      token: token,
    },
  };

  const handleNext = async (data) => {
    setStep((prevStep) => prevStep + 1);

    switch (step) {
      case 1:
        setFormData((prevData) => ({ ...prevData, interests: data }));
        break;
      case 2:
        setFormData((prevData) => ({ ...prevData, academicDetails: data }));
        break;
      case 3:
        setFormData((prevData) => ({ ...prevData, studentSummary: data }));
        break;
      case 4: {
        setFormData((prevData) => ({ ...prevData, hobbyForm: data })); // Use camelCase here

        break;
      }
      case 5:
        break;
      default:
        break;
    }
  };

  const renderCurrentStep = () => {
    switch (step) {
      case 1:
        return <Interest onNext={handleNext} />;
      case 2:
        return <AcademicDetailsForm onNext={handleNext} />;
      case 3:
        return <StudentSummary onNext={handleNext} />;
      case 4:
        return <HobbyForm onNext={handleNext} />;
      case 5:
        return <ProfilePictureUpload onNext={handleNext} />;
      default:
        return null;
    }
  };

  return <div className="snap-mandatory snap-y">{renderCurrentStep()}</div>;
};

export default StudentProfileEnhancement;
