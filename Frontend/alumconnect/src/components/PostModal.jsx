import React, { useState, useEffect, useRef, useCallback } from 'react';
import imageCompression from 'browser-image-compression'
import axios from 'axios';
import toast, { Toaster, ToastBar } from 'react-hot-toast';

const PostModal = ({ postData, title, body, isMyPost, postID }) => {
  const [selectedImg, setSelectedImg] = useState();
  const [preview, setPreview] = useState();
  const [userId1, setUserId] = useState();
  const titleInputRef = useRef();
  const bodyInputRef = useRef();
console.log("id",postID);
  const resetInputFields = useCallback(() => {
    titleInputRef.current.value = "";
    bodyInputRef.current.value = "";
  }, []);

  const onModalClose = useCallback(() => {
    if (!isMyPost) {
      resetInputFields();
    }
    setSelectedImg(undefined);
    setPreview(undefined);
  }, []);

  const onImgSelect = useCallback(
    async (e) => {
      if (!e.target.files || e.target.files.length === 0) {
        setSelectedImg(undefined);
        return;
      }

      if (!e.target.files || e.target.files.length === 0) {
        setSelectedImg(undefined);
        return;
      }

      let file = e.target.files[0];

      if (file.size > 1024 * 1024) {
        try {
          const compressedFile = await imageCompression(file, {
            maxSizeMB: 1,
            maxWidthOrHeight: 1920,
            useWebWorker: true,
          });

          console.log('compressedFile', compressedFile.size / 1024 / 1024);

          file = compressedFile;
        } catch (error) {
          file = undefined;
          console.error(error);
          // alert('Image size too large');
          toast.error('Image size too large');
        }
      }
      setSelectedImg(file);
    },
    [selectedImg],
  );

  const onSubmit = useCallback(
   
 
    (e) => {
      if (!isMyPost) {
        e.preventDefault();
        const formData = new FormData();
        formData.append("title", titleInputRef.current.value);
        formData.append("text", bodyInputRef.current.value);
        formData.append("file", selectedImg);
        const baseUrl = import.meta.env.VITE_BASE_URL;
        const apiUrl = `${baseUrl}/api/user-posts/create/` + userId1;
        axios
          .post(apiUrl, formData)
          .then((response) => response.data)
          .then((data) => {
            console.log(data);
            //  alert('Post Created Successfully');
             toast.success('Post Created Successfully');
            resetInputFields();
            setSelectedImg(undefined);
            setPreview(undefined);
            document.getElementById("PostModal").close();
          })
          .catch((error) => {
            console.error(error);
            // alert('Oops! Something went wrong');
            toast.error('Oops! Something went wrong');
          });
      } else {
        e.preventDefault();
        const baseUrl = import.meta.env.VITE_BASE_URL;
        const apiUrl = `${baseUrl}/api/user-posts/update/` + postID;
        const data = {
          userId: userId1,
          title:  titleInputRef.current.value,
          text:bodyInputRef.current.value
        };

        axios.put(apiUrl,data).then((response)=>
        {
          // alert('Post Updated Successfully');
          toast.success('Post Updated Successfully');
            console.log("updated")
            resetInputFields();
            setSelectedImg(undefined);
            setPreview(undefined);
            document.getElementById("PostModal").close();
        }).catch(err =>
        {
          console.log("error")
          // alert('Oops! Something went wrong');
          toast.error('Oops! Something went wrong');
        })
      }
    
    },
    [userId1, postID, selectedImg, titleInputRef, bodyInputRef],
  );

  useEffect(() => {
    const userId = localStorage.getItem("userId");
    setUserId(userId);
  }, []);

  useEffect(() => {
    if (!selectedImg) {
      setPreview(undefined);
      return;
    }

    const objectURL = URL.createObjectURL(selectedImg);
    setPreview(objectURL);

    return () => {
      URL.revokeObjectURL(objectURL);
      setPreview(null);
    };
  }, [selectedImg]);

  useEffect(() => {

    if (postData) {
      titleInputRef.current.value = postData.title;
      bodyInputRef.current.value = postData.text;
    }
  }, [postData]);

  useEffect(() => {
    titleInputRef.current.value = title ? title : "";
    bodyInputRef.current.value = body ? body : "";
  }, [title, body]);

  return (
    <>
      {/* Modal content */}
      <dialog id="PostModal" className="modal">
        <div className="modal-box w-screen max-w-screen-md max-h-screen-lg min-w-fit">
          <form method="dialog">
            {/* if there is a button in form, it will close the modal */}
            <button
              className="btn btn-sm btn-circle btn-ghost absolute right-2 top-2"
              onClick={onModalClose}
            >
              ✕
            </button>
          </form>
          <div className="flex flex-col justify-center">
            <h3 className="font-bold text-xl self-center">Post</h3>
            <form action="" className="flex flex-col p-2 m-2">
              <label className="p-2">Title</label>
              <input
                ref={titleInputRef}
                type="text"
                placeholder="Title"
                className="input input-bordered w-full"
                required
              />
              <label className="mt-4 py-2">Body</label>
              <textarea
                ref={bodyInputRef}
                className="textarea textarea-bordered w-full h-28"
                placeholder="Text"
              ></textarea>
              {!isMyPost && (
                <input
                  type="file"
                  className="file-input file-input-md w-full max-w-xs mt-6 py-2 self-center"
                  name="postImage"
                  onChange={onImgSelect}
                />
              )}
              {selectedImg && (
                <img
                  src={preview}
                  alt="Preview Image"
                  className="max-w-48 max-h-48 self-center"
                />
              )}
              <button
                type="button"
                className="btn btn-primary m-4"
                onClick={onSubmit}
              >
                Submit
              </button>
            </form>
          </div>
        </div>
        <form method="dialog" className="modal-backdrop">
          <button onClick={onModalClose}>close</button>
        </form>
        <Toaster />
      </dialog>
    </>
  );
};

export default PostModal;
