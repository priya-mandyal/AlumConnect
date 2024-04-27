import { ChatEngine } from "react-chat-engine";
import { useNavigate } from "react-router-dom";

const ChatsPage = () => {
  const user = localStorage.getItem("email");
  const projectID = localStorage.getItem("projectId");
  const atlanticTimezoneOffset = 0;
  const navigate = useNavigate();
  const homeButtonClick = () => {
    navigate("/home");
  };

  return (
    <div
      style={{
        position: "relative",
        backgroundColor: "#f0f0f0",
        height: "100vh",
        color: "#2c1f30",
        minHeight: "100vh",
      }}
    >
      <ChatEngine
        height="100vh"
        width="100vh"
        projectID={projectID}
        userName={user}
        userSecret={user}
        offset={atlanticTimezoneOffset}
      />

      <button className="homeButton" onClick={homeButtonClick}>
        <svg
          xmlns="http://www.w3.org/2000/svg"
          fill="none"
          viewBox="0 0 24 24"
          strokeWidth={1.5}
          stroke="currentColor"
          className="w-6 h-6 mr-2"
        >
          <path
            strokeLinecap="round"
            strokeLinejoin="round"
            d="m2.25 12 8.954-8.955c.44-.439 1.152-.439 1.591 0L21.75 12M4.5 9.75v10.125c0 .621.504 1.125 1.125 1.125H9.75v-4.875c0-.621.504-1.125 1.125-1.125h2.25c.621 0 1.125.504 1.125 1.125V21h4.125c.621 0 1.125-.504 1.125-1.125V9.75M8.25 21h8.25"
          />
        </svg>
      </button>

      <style>
        {`
          #new-chat-plus-button {
            visibility: hidden;
          }
        `}
      </style>
      <style>
        {`
          .ce-chat-card {
            width: 25vw !important; 
            border: round;
            background-color: #f1f1f1 !important;
            border-radius: 12px;
            margin-bottom: 3px;
          }
          
          .ce-active-chat-card {
            width: 25vw !important; 
            border: round;
            background-color: white !important;
            margin-bottom: 3px;
          }
          
          .ce-send-message-button {
            background-color: #502b63 !important;
          }
          .ce-my-message-bubble{
            background-color: #502b63 !important;
          }
          .ce-their-message-bubble{
            color: #502b63 !important;
          }
          .ce-chat-title-text {
            color: #502b63 !important;
          }
          .ce-chat-subtitle-text {
            color: #502b63 !important;
          }
          #ce-send-message-button {
            background-color: #502b63 !important;
          }
          .ce-message-timestamp {
            color: #502b63 !important;
          }
          .ce-chats-container {
            background-color: #250c33 !important;
            border: none !important;
            border-radius: 0px 0px 0px 0px !important;
          }
          .ce-chat-form-container {
            background-color: #250c33!important;
            border: none !important;
            color: white !important;
            text-align: right !important;
            padding-right: 20px;
          }
          .ce-chat-settings-container {
            color: #502b63 !important;
          }
          .ce-chat-card false {
            color: white !important;
          }
          .homeButton {
            position: absolute;
            top: 20px;
            left: 10px;
            background-color: transparent;
            color: white;
            padding: 5px;
            border-radius: 3px;
            cursor: pointer;
            display: flex;
            align-items: center;
            border: 1px solid rgba(255, 255, 255, 0.5);
          }
          
          .homeButton:hover {
            background-color: rgba(224, 186, 232, 0.5);
            border: 1px solid rgba(224, 186, 232, 0.3);
          }
        `}
      </style>
    </div>
  );
};

export default ChatsPage;
