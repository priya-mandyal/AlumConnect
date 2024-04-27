import { useEffect,useState } from "react";
import { FeedCard } from "./FeedCard";
import NavBar from "./NavBar";
import { useNavigate } from "react-router-dom";
import PostModal from "./PostModal";
import PostCreate from "./PostCreate";
import axios from "axios";
import logo from '../assets/A.svg';
import Footer from "./Footer";

const Home = () => {
  const navigate = useNavigate();
  const [imgUrl,setImgurl]= useState("");
  const [posts,setPosts]=useState("");
  useEffect(() => {
    const fetchData = async () => {
      const userId = localStorage.getItem("userId");
      const role = localStorage.getItem("role");
      if (role === "ALUMNI") {
        const baseUrl = import.meta.env.VITE_BASE_URL;
        const apiUrl = `${baseUrl}/api/alumni-profiles/`;
        const response = await axios.get(apiUrl + userId);
        localStorage.setItem("imageUrl", response.data.data.imageUrl);
        setImgurl(response.data.data.imageUrl);
      } else if(role==="STUDENT"){
        const baseUrl = import.meta.env.VITE_BASE_URL;
        const apiUrl = `${baseUrl}/v1/student/get/`;
        const response = await axios.get(apiUrl + userId);
        localStorage.setItem("imageUrl", response.data.data.imageUrl);
        setImgurl(response.data.data.imageUrl);
      }
      const baseUrl = import.meta.env.VITE_BASE_URL;
        const apiUrl = `${baseUrl}/api/homepage/posts/`+userId;
      const response_posts=await axios.get(apiUrl)
      setPosts(response_posts.data.data||[])
    };
    fetchData();
  }, []); 
  
  return (
    <div className="flex flex-col min-h-screen justify-between">
      <PostModal />
      <NavBar ImageUrl={imgUrl} />

      <div className="flex flex-col flex-grow bg-base-300">
        <div className="flex w-full">
          <div className="grid w-4/6 flex-grow card rounded-box place-items-center grid-cols-1 gap-6 my-6 px-4 md:px-6 lg:px-8">
            <PostCreate ImageUrl={imgUrl} />
            {/* <div className="divider divider-vertical divider-neutral w-full max-w-2xl"></div> */}
            {posts.length > 0 && posts.map(post => (
              <FeedCard key={post.id} id={post.id} title={post.title} img={post.imageUrl} text={post.text} />
            ))}

          </div>
        </div>
      </div>
      <Footer />
    </div>
  );
};

export default Home;
