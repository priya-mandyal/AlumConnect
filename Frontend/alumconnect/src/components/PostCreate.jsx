import { useCallback, useRef} from "react";

const PostCreate = (props) => {
  const createPostInputRef = useRef();


  const openCreatePost = useCallback(() => {
    createPostInputRef.current.blur();
    document.getElementById("PostModal").showModal();
  }, []);


  
  return (
    <>
      <div className="card w-full max-w-3xl bg-white shadow-md my-8">
        <div className="card-body">
          <form>
            <div className="flex flex-row gap-6">
              <div className="avatar">
                <div className="w-12 h-12 mr-4 rounded-full">
                  <img src={props.ImageUrl} />
                </div>
              </div>
              <input
                ref={createPostInputRef}
                type="text"
                placeholder="Start a post"
                className="input input-bordered w-full"
                onFocus={openCreatePost}
              />
            </div>
          </form>
        </div>
      </div>
    </>
  );
};

export default PostCreate;
