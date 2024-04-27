import React, { useState, useEffect } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import toast, { Toaster, ToastBar } from 'react-hot-toast';

const ProfilePictureUpload = (props) => {
  const navigate = useNavigate();
  const [profilePicture, setProfilePicture] = useState(null);
  const [validationError, setValidationError] = useState("");
  const [token, setToken] = useState("");
  const [userId, setuserId] = useState("");
  useEffect(() => {
    const jwtToken = localStorage.getItem("token");
    const userId = localStorage.getItem("userId");
    setToken(jwtToken);
    setuserId(userId);
  }, []);

  const handleProfilePictureChange = (e) => {
    const file = e.target.files[0];
    setProfilePicture(file);
    setValidationError("");
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!profilePicture) {
      setValidationError("Please upload a profile picture");
      return;
    } else {
      try {
        const formData = new FormData();
        console.log("userid", userId);
        formData.append("file", profilePicture);
        const baseUrl = import.meta.env.VITE_BASE_URL;
        const apiUrl = `${baseUrl}/images/upload/`;
        const response = await axios.post(apiUrl + userId, formData, {
          headers: {
            "Content-Type": "multipart/form-data",
            token: token,
          },
        });

        if (response.status === 200) {
          // console.log("Profile picture uploaded successfully");
          toast.success("Profile updated successfully");
          navigate("/profile");
          props.onNext();
        } else if (response.status === 500) {
          // alert("Failed to upload profile picture");
          toast.error("Failed to upload profile picture");
        }
      } catch (error) {
        console.error("Error uploading profile picture:", error);
      }
    }

    setValidationError("");
  };

  return (
    <div className="min-h-screen flex items-center justify-center">
      <Toaster />
      <form onSubmit={handleSubmit} className="max-w-md w-full">
        <div className="mb-4">
          <label className="block text-gray-700 text-sm font-bold mb-2">
            Profile Picture
          </label>
          <input
            type="file"
            accept="image/*"
            onChange={handleProfilePictureChange}
            className="input"
          />
          {validationError && (
            <p className="text-red-500 text-xs mt-1">{validationError}</p>
          )}
        </div>
        <div className="mb-6">
          <button type="submit" className="btn btn-primary">
            Upload Profile Picture
          </button>
        </div>
      </form>
    </div>
  );
};

export default ProfilePictureUpload;
