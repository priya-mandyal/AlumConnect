import React, { useState } from "react";

const HobbyForm = (props) => {
  const [hobbies, setHobbies] = useState("");

  const handleInputChange = (event) => {
    setHobbies(event.target.value);
  };

  const handleSubmit = (event) => {
    event.preventDefault();
    console.log("Hobbies submitted:", hobbies);
    props.onNext(hobbies);
    setHobbies("");
  };

  return (
    <div className="flex items-center justify-center min-h-screen">
      <div className="mx-auto w-2/3 text-center">
        <h2 className="text-2xl font-bold mb-4">Add Your Hobbies</h2>
        <form onSubmit={handleSubmit}>
          <div className="mb-4">
            <label className="block text-gray-700 text-md font-bold mb-2">
              Hobbies
            </label>
            <input
              type="text"
              value={hobbies}
              onChange={handleInputChange}
              placeholder="Enter your hobbies..."
              className="w-full p-2 border border-gray-300 rounded-md"
            />
          </div>
          <div className="mb-6">
            <button type="submit" className="btn btn-primary">
              Save Hobbies
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default HobbyForm;
