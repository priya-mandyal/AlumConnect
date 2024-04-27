import "./App.css";
import React from "react";
import {
  BrowserRouter as Router,
  Route,
  Routes,
  Navigate,
  Outlet,
} from "react-router-dom";
import AdminReg from "./components/AdminReg";
import StudentReg from "./components/StudentReg";
import AlumniReg from "./components/AlumniReg";
import LoginPage from "./screens/LoginPage";
import AdminAuthPage from "./screens/AdminAuthPage";
import Home from "./components/Home";
import Profile from "./components/Profile";
import AlumniProfileEnhancement from "./components/AlumniProfileEnhancement";
import ChangePasswordForm from "./components/ChangePasswordForm";
import StudentProfileEnhancement from "./components/StudentProfileEnhanement";
import ChatsPage from "./components/ChatsPage";
import MyPosts from "./components/MyPosts"; 
import ResetPassword from "./components/ResetPassword";
import toast, { Toaster } from 'react-hot-toast';

const App = () => {
  const ProtectedRoute = () => {
    const token = localStorage.getItem("token");
    return token && token !== null && token !== undefined ? (
      <Outlet />
    ) : (
      <Navigate to="/login" />
    );
  };

  return (
    <Router>
 
      <div>
        {/* <Toaster /> */}
        <Routes>
        <Route path="/resetpassword" element={<ResetPassword />} />
          <Route path="/login" element={<LoginPage />} />
          <Route path="/" element={<LoginPage />} />
          <Route exact path="/" element={<ProtectedRoute />}>
            <Route path="/home" element={<Home />} />
            <Route path="/admin" element={<AdminAuthPage />} />
            <Route path="/myposts" element={<MyPosts />} />
            <Route path="/profile" element={<Profile />} />
            <Route path="/adminregistration" element={<AdminReg />} />
            <Route path="/studentregistration" element={<StudentReg />} />
            <Route path="/alumniregistration" element={<AlumniReg />} />
     
            <Route
              path="/alumniprofile"
              element={<AlumniProfileEnhancement />}
            />
            <Route path="/changepassword" element={<ChangePasswordForm />} />
            <Route path="/chats" element={<ChatsPage />} />
        
            <Route
              path="/studentprofile"
              element={<StudentProfileEnhancement />}
            />
          </Route>
        </Routes>
      </div>
    </Router>
  );
};

export default App;
