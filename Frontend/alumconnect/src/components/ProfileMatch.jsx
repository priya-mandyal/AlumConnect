import React from "react";
import axios from "axios";
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import toast, { Toaster, ToastBar } from 'react-hot-toast';

const ProfileMatch = ({ Interests, matchModalRef }) => {
  const [selectedInterests, setSelectedInterests] = useState([]);
  const [showMatchedProfiles, setShowMatchedProfiles] = useState(false);
  const [matchedProfiles, setMatchedProfiles] = useState([]);


  const handleConfirmSelection = async () => {


    const id = localStorage.getItem("userId");
    const role = localStorage.getItem("role");
    const email = localStorage.getItem("email");
    console.log("id", id);
    console.log("role", role);
    console.log("email", email);
    const baseUrl = import.meta.env.VITE_BASE_URL;
    const apiUrl = `${baseUrl}/api/match/initiate/` + id;
    const apiUrl1 = `${baseUrl}/v1/student/get/1`;
    const requestBody = {
      email: email,
      role: role,
      interests: selectedInterests,
    };
    console.log("reqestBody", requestBody);

    try {

      const response = await axios.post(apiUrl, requestBody);
      console.log("e",response)
      console.log("res", response.data.matchedProfiles);
 
      setMatchedProfiles(response.data.matchedProfiles);
      if (matchedProfiles) {
        document.getElementById("MatchedProfiles").showModal();
      }
     
      setShowMatchedProfiles(true);
      // document.getElementById("MatchedProfiles").showModal();
    } catch (error) {
      // document.getElementById("MatchedProfiles").close();

      if (error.message === "Request failed with status code 404") {
        // alert("You have been put into the waiting list. You'll be notified when we find a match for you!");
        toast("You have been put into the waiting list. You'll be notified when we find a match for you!");
      }

    }
  };

  const handleInterestSelect = (interest) => {
    const isAlreadySelected = selectedInterests.includes(interest);
    if (isAlreadySelected) {
      setSelectedInterests(
        selectedInterests.filter((item) => item !== interest),
      );
    } else {
      setSelectedInterests([...selectedInterests, interest]);
    }
  };
  const navigate = useNavigate();
  const handleStartChat = async (userId) => {
    const loggedInUserId = localStorage.getItem("userId");
    const isStudent1 = localStorage.getItem("role") === "STUDENT";
    if (!loggedInUserId) {
      console.error("No logged in user ID found");
      return;
    }

    try {
      const baseUrl = import.meta.env.VITE_BASE_URL;
      const apiUrl = `${baseUrl}/api/chat/createChatRoom`;
      const response = await axios.post(
        apiUrl,
        {
          loggedInUser: loggedInUserId,
          matchedUser: userId,
          isStudent: isStudent1, 
        },
      );
      console.log("user", userId);
      console.log("stude", isStudent1);

      console.log("Chat room created:", response.data);

      navigate("/chats");
    } catch (error) {
      console.error(
        "Failed to create chat room:",
        error.response ? error.response.data : error,
      );
    }
  };
  return (
    <>
      <Toaster />
      <dialog ref={matchModalRef} id="MatchModal" className="modal">
        <div className="modal-box w-screen max-w-screen-md max-h-screen-lg min-w-fit">
          <form method="dialog">
            <button className="btn btn-sm btn-circle btn-ghost absolute right-2 top-2">
              ✕
            </button>
            <div>
              <div className="p-4">
                <h3 className="font-bold text-lg text-center">
                  Select Interests
                </h3>
                <div className="flex flex-col gap-2">
                  {Interests && Interests.length > 0 && (
                    <div className="flex flex-col gap-2">
                      {Interests.map((interestItem, index) => (
                        <label key={index} className="flex items-center gap-2">
                          <input
                            type="checkbox"
                            value={interestItem.value}
                            checked={selectedInterests.includes(
                              interestItem.value,
                            )}
                            onChange={() =>
                              handleInterestSelect(interestItem.value)
                            }
                          />
                          <span>{interestItem.value}</span>
                        </label>
                      ))}
                    </div>
                  )}
                </div>

                <div className="flex justify-center mt-4">
                  <button
                    className="btn btn-primary"
                    onClick={handleConfirmSelection}
                  >
                    Confirm
                  </button>
                </div>

              </div>
            </div>
          </form>
        </div>
      </dialog>

{/* {showMatchedProfiles && ( */}
<dialog id="MatchedProfiles" className="modal">
          {console.log("demo")}
<div className="modal-box w-screen max-w-screen-md max-h-screen-lg ">
<form method="dialog">
<button className="btn btn-sm btn-circle btn-ghost absolute right-2 top-2">✕</button>
</form>

            <div className="carousel w-full">
              {matchedProfiles.map((profile, index) => (
                <div
                  id={`slide${index}`}
                  className={`carousel-item relative w-full flex flex-col justify-center items-center`}
                >
                  <div className="profile-info text-center mb-4">
                    <img
                      src={profile.imageUrl}
                      className="rounded-full object-cover"
                      style={{ width: "250px", height: "250px" }}
                      alt="Profile Image"
                    />
                    <div className="font-bold">
                      {profile.firstName} {profile.lastName}
                    </div>
                    <div className="italic">{profile.email}</div>
                    <div className="italic">{profile.interests.join(", ")}</div>

                    <button
                      onClick={() => handleStartChat(profile.userId)}
                      className="btn btn-primary"
                    >
                      Start Chat
                    </button>
                  </div>

                  <div className="absolute flex justify-between transform -translate-y-1/2 left-5 right-5 top-1/2">
                    <a
                      href={`#slide${index === 0 ? matchedProfiles.length - 1 : index - 1}`}
                      className="btn btn-circle"
                    >
                      ❮
                    </a>
                    <a
                      href={`#slide${index === matchedProfiles.length - 1 ? 0 : index + 1}`}
                      className="btn btn-circle"
                    >
                      ❯
                    </a>
                  </div>
                </div>
              ))}
            </div>
          </div>
          <Toaster />
        </dialog>
      {/* )} */}
    </>
  );
};

export default ProfileMatch;
