import React, { useState, useEffect } from "react";
import axios from "axios";
import Interest from "./Interest";
import AcademicDetailsForm from "./AcademicDetailsForm";
import ProfilePictureUpload from "./ProfilePictureUpload";
import ProfessionalJourney from "./ProfessionalJourney";
import ExpertiseAndSkills from "./ExpertiseAndSkills";
import { useNavigate } from "react-router-dom";
import Availability from "./Availability";

const AlumniProfileEnhancement = () => {
  const [formData, setFormData] = useState({
    interests: [],
    academicDetails: {},
    professionalJourney: "",
    expertiseAndSkills: [],
    email: "",
    availability: "",
  });

  const [step, setStep] = useState(1);
  const [token, setToken] = useState("");
  const [email, setemail] = useState("");

  useEffect(() => {
    const jwtToken = localStorage.getItem("token");
    setToken(jwtToken);
    const userEmail = localStorage.getItem("email");
    setemail(userEmail);

    if (formData.availability) {
      console.log("Final", formData);
      console.log("ee", email);
      console.log("tok", token);
      formData.email = email;
      const baseUrl = import.meta.env.VITE_BASE_URL;
      const apiUrl = `${baseUrl}/api/alumni-profiles`;

      axios
        .post(apiUrl, formData, config)
        .then((response) => {
          if (response.status === 200) {
            console.log(response.data);
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
    setStep((prevStep) => {
      switch (prevStep) {
        case 1:
          setFormData((prevData) => ({ ...prevData, interests: data }));
          break;
        case 2:
          setFormData((prevData) => ({ ...prevData, academicDetails: data }));
          break;
        case 3:
          setFormData((prevData) => ({
            ...prevData,
            professionalJourney: data,
          }));
          break;
        case 4:
          setFormData((prevData) => ({
            ...prevData,
            expertiseAndSkills: data,
          }));
          break;
        case 5:
          setFormData((prevData) => ({ ...prevData, availability: data }));
          break;
        case 6:
          break;
        default:
          break;
      }
      return prevStep + 1;
    });
  };

  const renderStep = () => {
    switch (step) {
      case 1:
        return <Interest onNext={handleNext} />;
      case 2:
        return <AcademicDetailsForm onNext={handleNext} />;
      case 3:
        return <ProfessionalJourney onNext={handleNext} />;
      case 4:
        return <ExpertiseAndSkills onNext={handleNext} />;
      case 5:
        return <Availability onNext={handleNext} />;
      case 6:
        return <ProfilePictureUpload onNext={handleNext} />;
      default:
        return null;
    }
  };

  return (
    <div className="snap-mandatory snap-y overflow-y-scroll h-screen">
      {renderStep()}
    </div>
  );
};

export default AlumniProfileEnhancement;
