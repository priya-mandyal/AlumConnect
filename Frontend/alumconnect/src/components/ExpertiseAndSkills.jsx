import React, { useState } from "react";

const ExpertiseAndSkills = (props) => {
  const [expertiseSkills, setExpertiseSkills] = useState("");

  const handleSkillsChange = (e) => {
    const value = e.target.value;
    setExpertiseSkills(value);
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    props.onNext(expertiseSkills);
  };

  return (
    <div className="flex items-center justify-center min-h-screen">
      <div className="mx-auto w-2/3 text-center">
        <h2 className="text-2xl font-bold mb-4">
          Add Your Expertise and Skills
        </h2>
        <form onSubmit={handleSubmit}>
          <div className="mb-4">
            <label className="block text-gray-700 text-md font-bold mb-2">
              Expertise and Skills
            </label>
            <input
              type="text"
              value={expertiseSkills}
              onChange={handleSkillsChange}
              placeholder="Enter your expertise and skills..."
              className="w-full p-2 border border-gray-300 rounded-md"
            />
          </div>
          <div className="mb-6">
            <button type="submit" className="btn btn-primary">
              Save Expertise and Skills
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default ExpertiseAndSkills;
