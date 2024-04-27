import { useState, useEffect } from "react";
import { UserCircleIcon } from "@heroicons/react/24/solid";
import { TrashIcon } from "@heroicons/react/24/outline";

import logo from "../assets/A.svg";
import NavBar from "./NavBar";
import axios from "axios";
import Footer from './Footer';
import toast, { Toaster, ToastBar } from 'react-hot-toast';
const AcademicDetailForm = (props) => {
  const [university, setUniversity] = useState("");
  const [degree, setDegree] = useState("");

  const handleInputChange = (e) => {
    e.preventDefault();
    if (e.target.name === "university") {
      setUniversity(e.target.value);
    } else if (e.target.name === "degree") {
      setDegree(e.target.value);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await props.updateAcademicDetails({ university, degree });
      console.log("dsds")
      setUniversity("");
      setDegree("");
    } catch (error) {
      console.error("Error during update:", error);
    }
  };
  return (
    <dialog id="addAcademicDetail" className="modal">
      <div className="modal-box max-w-sm">
        <form method="dialog">
          <button className="btn btn-sm btn-circle btn-ghost absolute right-2 top-2">
            ✕
          </button>
        </form>
        <form onSubmit={handleSubmit}>
          <h3 className="font-bold text-lg text-center">Academic Detail</h3>
          <div className="form-control">
            <label className="label justify-left">
              <span className="label-text font-bold">University</span>
            </label>
            <input
              type="text"
              name="university"
              placeholder="University"
              className="input input-bordered my-2"
              value={university}
              onChange={handleInputChange}
            />
            <label className="label justify-left">
              <span className="label-text font-bold">Degree</span>
            </label>
            <input
              type="text"
              name="degree"
              placeholder="Degree"
              className="input input-bordered my-2"
              value={degree}
              onChange={handleInputChange}
            />
            <button className="btn btn-primary">Submit</button>
          </div>
        </form>
      </div>
      <form method="dialog" className="modal-backdrop">
        <button>close</button>
      </form>
    </dialog>
  );
};

const AcademicDetail = (props) => {
  return (
    <div className="card card-compact w-full max-w-xl place-self-center">
      <div className="card-body bg-gray-200 p-4 rounded-3xl flex flex-row items-center">
        <div className="w-full px-2">
          <label className="block text-sm font-medium text-gray-600 flex-1 leading-4">
            University
          </label>
          <label className="block text-md font-bold text-gray-600 flex-1 leading-4">
            {props.university}
          </label>
          <label className="block text-sm font-medium text-gray-600 flex-1 leading-4">
            Degree
          </label>
          <label className="block text-md font-bold text-gray-600 flex-1 leading-4">
            {props.degree}
          </label>
        </div>
        <button
          className="btn bg-base-100"
          onClick={() => {
            props.onDelete();
          }}
        >
          <TrashIcon className="h-6 w-6" />
        </button>
      </div>
    </div>
  );
};

const Profile = () => {
  const initialAcademicDetails = [];
  const [academicDetails, setAcademicDetails] = useState(
    initialAcademicDetails,
  );
  const initialFormState = {
    email: "",
    academicDetails: [],
    interests: [],
    professionalJourney: "",
    studentSummary: "",
    availability: "",
  };
  const [form, setForm] = useState(initialFormState);
  const [role, setRole] = useState();
  const [imageData, setImageData] = useState("");
  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");
  const [newInterest, setNewInterest] = useState("");
  const [selectedInterests, setSelectedInterests] = useState([]);
  const [imgUrl,setImgurl]= useState("");
  useEffect(() => {
    const fname = localStorage.getItem("firstName");
    setFirstName(fname);
    const lname = localStorage.getItem("lastName");
    setLastName(lname);

    const fetchData = async () => {
      const role1 = localStorage.getItem("role");
      const id = localStorage.getItem("userId");

      const baseUrl = import.meta.env.VITE_BASE_URL;
      const apiUrl = `${baseUrl}/images/download/`;
      try {
        const response = await axios.get(apiUrl + id, {
          responseType: "arraybuffer",
        });

        const blob = new Blob([response.data], { type: "image/png" });
        const base64Image = URL.createObjectURL(blob);
        setImageData(base64Image);
      } catch (error) {
        console.error("Error downloading image:", error);
      }

      if (role1 === "STUDENT") {
        try {
          const baseUrl = import.meta.env.VITE_BASE_URL;
          const apiUrl = `${baseUrl}/v1/student/get/`;
          const response = await axios.get(apiUrl + id);
          setForm(response.data.data);
          setRole(response.data.role);
          setImgurl(response.data.data.imageUrl);
        } catch (error) {
          console.error("Error fetching data:", error);
        }
      } else if (role1 == "ALUMNI") {
        try {
          const baseUrl = import.meta.env.VITE_BASE_URL;
          const apiUrl = `${baseUrl}/api/alumni-profiles/`;
          const response = await axios.get(apiUrl + id);
          setForm(response.data.data);
          setRole(response.data.role);
          setImgurl(response.data.data.imageUrl);
        } catch (error) {
          console.error("Error fetching data:", error);
        }
      }
    };
    fetchData();
  }, []);
  const handleImageChange = async (e) => {
    const userId = localStorage.getItem("userId");

    const file = e.target.files[0];
    const formData = new FormData();
    console.log("userid", userId);
    formData.append("file", file);
    const baseUrl = import.meta.env.VITE_BASE_URL;
    const apiUrl = `${baseUrl}/images/upload/`;
    const response = await axios.post(apiUrl + userId, formData, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    });
    fetchImage(userId);
  };
  const fetchImage = async (id) => {
    try {
      const baseUrl = import.meta.env.VITE_BASE_URL;
      const apiUrl = `${baseUrl}/images/download/`;
      const response = await axios.get(apiUrl + id, {
        responseType: "arraybuffer",
      });

      const blob = new Blob([response.data], { type: "image/png" });
      const base64Image = URL.createObjectURL(blob);

      setImageData(base64Image);
    } catch (error) {
      console.error("Error downloading image:", error);
    }
  };

  const updateAcademicDetails = async (details) => {
    try {
      const updatedDetails = [...form.academicDetails, details];
      console.log(updatedDetails);
      setForm((prevForm) => ({
        ...prevForm,
        academicDetails: updatedDetails,
      }));
      setAcademicDetails(updatedDetails);
    } catch (error) {
      console.error("Error updating academic details:", error);
    }
  };

  const onAcademicDetailDelete = (id) => {
    setForm({
      ...form,
      academicDetails: form.academicDetails.filter((_, i) => i !== id),
    });
  };

  const handleInputChange = (event) => {
    const { name, value } = event.target;
    console.log(name + " " + value);
    setForm((prevForm) => ({
      ...prevForm,
      [name]: value,
    }));
  };

  const handleSubmit = async () => {
    try {
      const role1 = localStorage.getItem("role");
      const id = localStorage.getItem("userId");
      if (role1 === "STUDENT") {
        try {
          const baseUrl = import.meta.env.VITE_BASE_URL;
          const apiUrl = `${baseUrl}/v1/student/update/` + id;
          const response = await axios.put(apiUrl, {
            ...form,
            academicDetails: academicDetails,
          });
          console.log("res",response.status)
          if(response.status===200){
			// alert('Profile Updated Successfully')
			toast.success('Profile Updated Successfully')
          }

        } catch (error) {
          console.error("Error fetching data:", error);
        }
      } else if (role1 == "ALUMNI") {
        try {
          const baseUrl = import.meta.env.VITE_BASE_URL;
          const apiUrl = `${baseUrl}/api/alumni-profiles/` + id;
          const response = await axios.put(apiUrl, {
            ...form,
            academicDetails: academicDetails,
          });

			toast.success('Profile Updated Successfully')

        } catch (error) {
          console.error("Error fetching data:", error);
        }
      }
    } catch (error) {
      console.error("Error submitting form:", error);
    }
  };
  const handleAddInterest = () => {
    if (newInterest.trim() === "") {
      return;
    }
    const newInterestObject = { label: newInterest, value: newInterest };
    setForm((prevForm) => ({
      ...prevForm,
      interests: [...prevForm.interests, newInterestObject],
    }));
    setNewInterest("");
  };

  const handleRemoveInterest = (index) => {
    setForm((prevForm) => ({
      ...prevForm,
      interests: prevForm.interests.filter((_, i) => i !== index),
    }));
  };

  return (
    <div className="flex flex-col min-h-screen bg-base-300">
      <NavBar ImageUrl={imgUrl} />
      <div className="flex flex-grow w-full">
        <div className="grid flex-grow card  rounded-box place-items-center grid-cols-1 gap-6 my-6 px-4 md:px-6 lg:px-8">
          <div className="card shrink-0 w-full max-w-3xl bg-base-100 my-12 p-12">
            <div className="card-body items-center">
              <span className="text-3xl font-bold text-gray-600">Profile</span>
            </div>

            {/* Name - first + last */}

            <div className="card-body">
              <div className="flex items-center gap-x-2">
                <label className="block text-sm font-bold text-gray-500 flex-1">
                  First Name
                </label>
                <input
                  type="text"
                  placeholder="First Name"
                  value={firstName}
                  className="input input-md input-bordered w-full max-w-xs"
                  disabled
                />
              </div>
              <div className="flex items-center gap-x-2">
                <label className="block text-sm font-bold text-gray-500 flex-1">
                  Last Name
                </label>
                <input
                  type="text"
                  placeholder="Last Name"
                  value={lastName}
                  className="input input-md input-bordered w-full max-w-xs"
                  disabled
                />
              </div>
            </div>

            {/* Email */}
            <div className="card-body">
              <div className="flex items-center gap-x-2">
                <label className="block text-sm font-bold text-gray-600 flex-1">
                  Email
                </label>
                <input
                  type="text"
                  placeholder="Email"
                  className="input input-md input-bordered w-full max-w-xs"
                  value={form.email}
                  disabled
                />
              </div>
            </div>

            {/* Photo */}
            <div className="card-body ">
              <label className="block text-sm font-bold text-gray-600 flex-1">
                Photo
              </label>
              <div className="mt-2 flex items-center gap-x-3">
                {imageData ? (
                  <img src={imageData} className="w-28 h-28 rounded-full" />
                ) : (
                  <UserCircleIcon
                    className="w-28 h-28 rounded"
                    aria-hidden="true"
                  />
                )}
                <input
                  type="file"
                  accept="image/*"
                  className="file-input file-input-md file-input-ghost w-full max-w-xs text-gray-500"
                  onChange={handleImageChange}
                />
              </div>
            </div>

            {/* Bio */}
            {role === "ALUMNI" ? (
              <div className="card-body">
                <label className="block text-sm font-bold text-gray-600 flex-1">
                  Professional Journey
                </label>
                <textarea
                  type="text"
                  placeholder="professionalJourney"
                  className="textarea textarea-bordered w-full"
                  name="professionalJourney"
                  value={form.professionalJourney}
                  onChange={handleInputChange}
                />
              </div>
            ) : (
              <div className="card-body">
                <label className="block text-sm font-bold text-gray-600 flex-1">
                  Student Summary
                </label>
                <textarea
                  type="text"
                  placeholder="studentSummary"
                  className="textarea textarea-bordered w-full"
                  name="studentSummary"
                  value={form.studentSummary}
                  onChange={handleInputChange}
                />
              </div>
            )}

            {/* Academic Details */}
            <AcademicDetailForm updateAcademicDetails={updateAcademicDetails} />
            <div className="card-body">
              <label className="block text-sm font-bold text-gray-600 flex-1">
                Academic Details
              </label>
              <div className="flex flex-1 flex-col items-center gap-y-2">
                {form.academicDetails.map((detail, id) => (
                  <AcademicDetail
                    key={id}
                    university={detail.university}
                    degree={detail.degree}
                    onDelete={() => {
                      onAcademicDetailDelete(id);
                    }}
                  />
                ))}
              </div>
            </div>

            <div className="card-body items-center">
              <button
                className="btn bg-gray-300 btn-wide"
                onClick={() => {
                  document.getElementById("addAcademicDetail").showModal();
                }}
              >
                Add
              </button>
            </div>

            {/* Availability Hours */}
            {role === "ALUMNI" ? (
              <div className="card-body">
                <label className="block text-sm font-bold text-gray-600 flex-1">
                  Availability Hours
                </label>
                <textarea
                  type="text"
                  readOnly={false}
                  placeholder="professionalJourney"
                  className="textarea textarea-bordered w-full"
                  name="availability"
                  onChange={handleInputChange}
                  value={form.availability}
                />
              </div>
            ) : null}

            {/* Interests */}
            <div className="card-body">
              <label className="block text-sm font-bold text-gray-600 flex-1">
                Interests
              </label>
              <div className="mt-2 flex flex-wrap">
                {form.interests.map((interest, index) => (
                  <div
                    key={index}
                    className="flex items-center bg-gray-200 rounded p-2 m-1"
                  >
                    <span className="mr-2">{interest.value}</span>
                    <button
                      className="btn btn-ghost btn-sm"
                      onClick={() => handleRemoveInterest(index)}
                    >
                      Remove
                    </button>
                  </div>
                ))}
              </div>

              <div className="mt-2 flex flex-5">
                <input
                  type="text"
                  placeholder="Add an interest"
                  className="input input-bordered"
                  value={newInterest}
                  onChange={(e) => setNewInterest(e.target.value)}
                />
                <button
                  className="btn btn-primary ml-2"
                  onClick={handleAddInterest}
                >
                  Add
                </button>
              </div>
            </div>

            {/* Submit */}
            <div className="card-body items-center">
              <button
                className="btn btn-primary btn-wide"
                onClick={handleSubmit}
              >
                Submit
              </button>
            </div>
          </div>
        </div>
      </div>
     <Footer/>
     <Toaster />
    </div>
  );
};

export default Profile;
