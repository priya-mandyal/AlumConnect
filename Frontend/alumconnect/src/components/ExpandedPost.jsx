import { useCallback, useEffect, useRef } from "react";

const ExpandedPost = (props) => {
  const modalRef = useRef();

  useEffect(() => {
    if (props.isOpen) {
      setTimeout(() => modalRef.current.showModal(), 400);
    }
  }, [props.isOpen]);

  const onClose = useCallback(() => {
    props.setIsOpen(false);
  }, []);

  return (
    <>
      <dialog ref={modalRef} id="expanded-post" className="modal">
        <div className="modal-box p-8 w-screen h-screen max-w-screen-lg max-h-screen-md">
          <form method="dialog">
            {/* if there is a button in form, it will close the modal */}
            <button
              onClick={onClose}
              className="btn btn-sm btn-circle btn-ghost absolute right-2 top-2"
            >
              ✕
            </button>
          </form>

          <div className="flex w-full flex-col lg:flex-row gap-2">
            <div className="grid h-fit w-6/12  flex-grow card rounded-box place-items-center align-middle">
              <div className="card-body max-w-96 min-w-80">
                {/* title */}
                <div className="flex flex-row items-center">
                  <div className="avatar">
                    <div className="w-8 mr-4 rounded-full">
                      <img src="https://daisyui.com/images/stock/photo-1534528741775-53994a69daeb.jpg" />
                    </div>
                  </div>
                  <h2 className="card-title font-weight-lg">
                    Image / text Card
                  </h2>
                </div>

                {/* image */}
                <div className="mt-2 object-cover rounded-lg min-w-28 max-w-96">
                  <img
                    className="rounded-xl"
                    src="https://daisyui.com/images/stock/photo-1606107557195-0e29a4b5b4aa.jpg"
                    alt="Shoes"
                  />
                </div>

                {/* like, comment */}
                <div className="flex flex-row my-2 items-center gap-x-4">
                  {/* like */}
                  <div className="flex flex-row gap-x-2">
                    {/* <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-6 h-6 hover:fill-red-400 hover:text-red-500"> */}
                    <svg
                      xmlns="http://www.w3.org/2000/svg"
                      fill="none"
                      viewBox="0 0 24 24"
                      strokeWidth={1.5}
                      stroke="currentColor"
                      className={`w-6 h-6 hover:fill-red-400 hover:text-red-500 `}
                      // onClick={onClickLike}
                    >
                      <path
                        stroke-linecap="round"
                        stroke-linejoin="round"
                        d="M21 8.25c0-2.485-2.099-4.5-4.688-4.5-1.935 0-3.597 1.126-4.312 2.733-.715-1.607-2.377-2.733-4.313-2.733C5.1 3.75 3 5.765 3 8.25c0 7.22 9 12 9 12s9-4.78 9-12Z"
                      />
                    </svg>
                    <p>21313</p>
                  </div>
                </div>

                {/* feedCard body text */}
                <div className="py-2">
                  <p>
                    Lorem ipsum dolor sit amet consectetur adipisicing elit. Et,
                    ratione dicta deleniti, quas distinctio, veniam quo rem
                    eveniet aliquid repudiandae fuga asperiores reiciendis
                    tenetur? Eius quidem impedit et soluta accusamus.
                  </p>
                </div>
              </div>
            </div>
            <div className="divider divider-horizontal"></div>
            <div className="grid h-20 w-6/12 min-w-80 flex-grow card bg-base-300 rounded-box place-items-center">
              Comments
            </div>
          </div>
        </div>
        <form method="dialog" className="modal-backdrop">
          <button onClick={onClose}>close</button>
        </form>
      </dialog>
    </>
  );
};

export default ExpandedPost;
