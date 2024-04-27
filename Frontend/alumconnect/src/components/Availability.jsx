import React, { useState } from "react";

const Availability = (props) => {
  const [availabilityHours, setAvailabilityHours] = useState("");

  const handleAvailabilityChange = (e) => {
    const value = e.target.value;
    setAvailabilityHours(value);
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    props.onNext(availabilityHours);
  };

  return (
    <div className="flex items-center justify-center min-h-screen">
      <div className="mx-auto w-2/3 text-center">
        <h2 className="text-2xl font-bold mb-4">
          Add Your Availability in Hours
        </h2>
        <form onSubmit={handleSubmit}>
          <div className="mb-4">
            <label className="block text-gray-700 text-sm font-bold mb-2">
              Availability Hours
            </label>
            <textarea
              value={availabilityHours}
              onChange={handleAvailabilityChange}
              rows="4"
              placeholder="Enter your availability hours..."
              className="w-full p-2 border border-gray-300 rounded-md"
            />
          </div>
          <div className="mb-6">
            <button type="submit" className="btn btn-primary">
              Save Availability Hours
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default Availability;
