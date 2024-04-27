import React, { useState, useEffect, useRef, useCallback } from 'react';
import axios from 'axios';
import NavBar from "./NavBar";
import PostCreate from './PostCreate';
import { FeedCard } from './FeedCard';
import PostModal from './PostModal';

import logo from '../assets/A.svg';
import "../styles/FeedCard.css";
import Footer from './Footer';
import toast, { Toaster, ToastBar } from 'react-hot-toast';
function MyPosts() {
  const [posts, setPosts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [userId, setUserId] = useState();
  const [imgUrl,setImgurl]= useState("");

  const [postModalTitle, setPostModalTitle] = useState('');
  const [postModalBody, setPostModalBody] = useState('');


  const [currentPostId, setCurrentPostId] = useState();

  useEffect(() => {
    const fetchUserId = async () => {
      const userId = localStorage.getItem("userId");
      const imgurl1=localStorage.getItem("imageUrl")
      setUserId(userId);
      setImgurl(imgurl1)
    };

    fetchUserId();
  }, []);

  useEffect(() => {
    if (userId) {
      fetchPosts();
    }
  }, [userId]);

  const getPostData = useCallback((postData) => {
    setPostModalTitle(postData.title);
    setPostModalBody(postData.body);
    document.getElementById("PostModal").showModal();
  }, []);

  const fetchPosts = async () => {
    try {
      const baseUrl = import.meta.env.VITE_BASE_URL;
      const apiUrl = `${baseUrl}/api/user-posts/` + userId;
      const response = await axios.get(apiUrl);
     
      if (!response.data) {
        throw new Error('Failed to fetch posts');
      }
      
      setPosts(response.data.data);
      setLoading(false);
    } catch (error) {
      console.error('Error fetching posts:', error);
      setLoading(false);
    }
  };

  const showDeleteModal = useCallback(() => {
    document.getElementById("DeletePostModal").showModal();
  }, []);

  const handleDelete = async (postId) => {

    try {
      const baseUrl = import.meta.env.VITE_BASE_URL;
      const apiUrl = `${baseUrl}/api/user-posts/delete/${postId}`;
      await axios.delete(apiUrl);
      // alert('Post Deleted Successfully')
      toast.success('Post Deleted Successfully')
      fetchPosts();
    } catch (error) {
      console.error('Error deleting post:', error);
    }
    
    document.getElementById("DeletePostModal").close();
  };

  if (loading) {
    return <div>Loading...</div>;
  }

  return (
    
    <div className="flex flex-col min-h-screen justify-between">
      <PostModal title={postModalTitle} body={postModalBody} postID={currentPostId} isMyPost={true} />

      <dialog id="DeletePostModal" className="modal">
        <div className="modal-box">
          <form method="dialog">
            <button className="btn btn-sm btn-circle btn-ghost absolute right-2 top-2">✕</button>
          </form>
          <h3 className="font-bold text-lg">Are you sure you want to delete the post?</h3>
          <div className='flex justify-center'>
            <button className='btn btn-error text-white my-3' onClick={() => handleDelete(currentPostId)}>Yes</button>
          </div>
        </div>
      </dialog>


      <NavBar ImageUrl={imgUrl} />

      <div className='flex flex-col flex-grow bg-base-300'>
        <div className="flex w-full">
          <div className="grid w-4/6 flex-grow card rounded-box place-items-center grid-cols-1 gap-6 my-6 px-4 md:px-6 lg:px-8">
            {posts.map(post => (
              <FeedCard key={post.id} id={post.id} title={post.title} img={post.imageUrl} text={post.text} myPost={true} sendPostData={getPostData} deleteConfirmation={showDeleteModal} setCurrentPostId={setCurrentPostId} />
            ))}
          </div>
        </div>
      </div>
      <Footer/>
      <Toaster />
    </div>
  );
}

export default MyPosts;
