import React from "react";
import axios from "axios";
import ProfileMatch from "./ProfileMatch";
import { useState, useRef } from "react";
import { useNavigate, Link } from "react-router-dom";
import logo from '../assets/A.svg';

const NewNavBar = (props) => {
  const navigate = useNavigate();
  const [interest, setInterest] = useState();
  const matchModalRef = useRef();

  const handleProfileClick = () => {
    navigate("/profile");
  };
  const handleLogout = () => {
    localStorage.clear();
    navigate("/");
  };
  const handleChangePassword = () => {
    navigate("/changepassword");
  };
  const handleMyPosts = () => {
    navigate("/myposts");
  };
  console.log("in", interest);

  const match = async () => {
    matchModalRef.current.showModal();
    const userId = localStorage.getItem("userId");
    const role = localStorage.getItem("role");
    if (role == "ALUMNI") {
      const baseUrl = import.meta.env.VITE_BASE_URL;
      const apiUrl = `${baseUrl}/api/alumni-profiles/`;
      const response = await axios.get(apiUrl + userId);
      const interest1 = response.data.data.interests;
      setInterest(interest1);
      console.log("in", interest);
    } else {
      const baseUrl = import.meta.env.VITE_BASE_URL;
      const apiUrl = `${baseUrl}/v1/student/get/`;
      const response = await axios.get(apiUrl + userId);
      const interest1 = response.data.data.interests;

      setInterest(interest1);
    }
    console.log("match");
    console.log("in", interest);
  };
  return (
    <>
      <div className="navbar bg-neutral">
        <ProfileMatch Interests={interest} matchModalRef={matchModalRef} />
        <div className="flex flex-1 mx-6">
          <Link to="/home" className="btn text-xl text-neutral">
            <img src={logo} alt="" className="w-10 h-10" />
            {/* AlumConnect */}
          </Link>
        </div>
        <div className="flex-none mx-4 gap-2">
          <a href="/chats" className="btn text-xl text-neutral ml-2">
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth={1.5} stroke="currentColor" className="w-6 h-6">
              <path strokeLinecap="round" strokeLinejoin="round" d="M20.25 8.511c.884.284 1.5 1.128 1.5 2.097v4.286c0 1.136-.847 2.1-1.98 2.193-.34.027-.68.052-1.02.072v3.091l-3-3c-1.354 0-2.694-.055-4.02-.163a2.115 2.115 0 0 1-.825-.242m9.345-8.334a2.126 2.126 0 0 0-.476-.095 48.64 48.64 0 0 0-8.048 0c-1.131.094-1.976 1.057-1.976 2.192v4.286c0 .837.46 1.58 1.155 1.951m9.345-8.334V6.637c0-1.621-1.152-3.026-2.76-3.235A48.455 48.455 0 0 0 11.25 3c-2.115 0-4.198.137-6.24.402-1.608.209-2.76 1.614-2.76 3.235v6.226c0 1.621 1.152 3.026 2.76 3.235.577.075 1.157.14 1.74.194V21l4.155-4.155" />
            </svg>
          </a>
          <button className="btn text-xl text-neutral ml-2" onClick={match}>
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth={1.5} stroke="currentColor" className="w-6 h-6">
              <path strokeLinecap="round" strokeLinejoin="round" d="M16.5 8.25V6a2.25 2.25 0 0 0-2.25-2.25H6A2.25 2.25 0 0 0 3.75 6v8.25A2.25 2.25 0 0 0 6 16.5h2.25m8.25-8.25H18a2.25 2.25 0 0 1 2.25 2.25V18A2.25 2.25 0 0 1 18 20.25h-7.5A2.25 2.25 0 0 1 8.25 18v-1.5m8.25-8.25h-6a2.25 2.25 0 0 0-2.25 2.25v6" />
            </svg>

            Match
          </button>
          <div className="dropdown dropdown-end">
            <div
              tabIndex={0}
              role="button"
              className="btn btn-ghost btn-circle avatar"
            >
              <div className="w-10 rounded-full">
                <img
                  alt="Tailwind CSS Navbar component"
                  src={props.ImageUrl}
                />
              </div>
            </div>
            <ul
              tabIndex={0}
              className="menu menu-sm dropdown-content mt-3 z-[1] p-2 shadow bg-base-100 rounded-box w-52"
            >
              <li>
                <a href="#" onClick={handleProfileClick}>
                  Profile
                </a>
              </li>
              <li>
                <a href="#" onClick={handleMyPosts}>
                  My Posts
                </a>
              </li>
              <li>
                <a onClick={handleLogout}>Logout</a>
              </li>
              <li>
                <a onClick={handleChangePassword}>Change Password</a>
              </li>
            </ul>
          </div>
        </div>
      </div>
    </>
  );
};

export default NewNavBar;
