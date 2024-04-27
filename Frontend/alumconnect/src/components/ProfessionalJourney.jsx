import React, { useState } from "react";

const ProfessionalJourney = (props) => {
  const [professionalExperience, setProfessionalExperience] = useState("");

  const handleExperienceChange = (e) => {
    const value = e.target.value;
    setProfessionalExperience(value);
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    console.log("Professional Experience:", professionalExperience);
    props.onNext(professionalExperience);
  };

  return (
    <div className="flex items-center justify-center min-h-screen">
      <div className="mx-auto w-2/3 text-center">
        <h2 className="text-2xl font-bold mb-4">
          Add Your Professional Journey
        </h2>
        <form onSubmit={handleSubmit}>
          <div className="mb-4">
            <label className="block text-gray-700 text-sm font-bold mb-2">
              Professional Experience
            </label>
            <textarea
              value={professionalExperience}
              onChange={handleExperienceChange}
              rows="8"
              placeholder="Share your professional journey and experience..."
              className="w-full p-2 border border-gray-300 rounded-md"
            />
          </div>
          <div className="mb-6">
            <button type="submit" className="btn btn-primary">
              Save Professional Journey
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default ProfessionalJourney;
