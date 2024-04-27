import { useCallback, useEffect, useState, useRef } from "react";
import ExpandedPost from "./ExpandedPost";
import PostModal from "./PostModal";

import "../styles/FeedCard.css";

const FeedCard = (props) => {
  const [like, setLike] = useState(false);
  const [comment, setComment] = useState(false);

  const [isPostExpanded, setIsPostExpanded] = useState(false);

  const titleRef = useRef();
  const bodyRef = useRef();
  const imageRef = useRef();

  const handleEdit = useCallback(() => {
    document.getElementById("PostModal").showModal();
  
    const title = titleRef.current.innerText;
    const body = bodyRef.current.innerText;
    props.setCurrentPostId(props.id);

    props.sendPostData({title, body});
  }, []);

  const handleDelete = useCallback(() => {
    props.setCurrentPostId(props.id);
    props.deleteConfirmation();
  }, [props.id]);

  useEffect(() => {
    if (!isPostExpanded) {
      setComment(false);
    }
  }, [isPostExpanded]);

  return (
    <>
     
      <div className="card w-full max-w-3xl bg-white shadow-lg my-8">
        <div className="card-body">
          {/* title */}
          <div className="flex flex-row items-center justify-between">
            <h2 ref={titleRef} className="card-title font-weight-lg">{props.title ? props.title : ""}</h2>

            {/* only in our post svg button */}
            { props.myPost && (
              <div className="dropdown dropdown-end">
                <div tabIndex={0} role="button" className="btn btn-ghost btn-circle avatar">
                  {/* <div className="w-10 rounded-full"> */}
                  <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth={1.5} stroke="currentColor" className="w-6 h-6">
                    <path strokeLinecap="round" strokeLinejoin="round" d="M12 6.75a.75.75 0 1 1 0-1.5.75.75 0 0 1 0 1.5ZM12 12.75a.75.75 0 1 1 0-1.5.75.75 0 0 1 0 1.5ZM12 18.75a.75.75 0 1 1 0-1.5.75.75 0 0 1 0 1.5Z" />
                  </svg>
                  {/* </div> */}
                </div>
                <ul tabIndex={0} className="menu menu-sm dropdown-content mt-3 z-[1] p-2 shadow bg-gray-200 rounded-box w-52">
                  <li><button onClick={handleEdit}>Edit</button></li>
                  <li><button onClick={handleDelete} className="bg-red-500 text-white">Delete</button></li>
                </ul>
              </div>
            )}


          </div>

          {/* image */}
          {props.img && (
            <div className="mt-2 object-cover rounded-lg min-w-28">
              <img
                className="rounded-xl"
                src={props.img}
                alt="Failed to load the image"
              />
            </div>
          )}

          {/* feedCard body text */}
          {props.text && (
            <div className="py-2">
              <p ref={bodyRef}>
                {props.text}
              </p>
            </div>
          )}

        </div>
        
      </div>
    </>
  );
};

export { FeedCard };
