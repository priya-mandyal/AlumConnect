import React, { useState } from "react";

const StudentSummary = (props) => {
  const [summary, setSummary] = useState("");

  const handleSummaryChange = (e) => {
    const value = e.target.value;
    setSummary(value);
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    console.log("Student Summary:", summary);
    props.onNext(summary);

  };

  return (
    <div className="flex items-center justify-center min-h-screen">
      <div className="mx-auto w-2/3 text-center">
        <h2 className="text-2xl font-bold mb-4">
          Add a Summary About Yourself
        </h2>
        <form onSubmit={handleSubmit}>
          <div className="mb-4">
            <label className="block text-gray-700 text-sm font-bold mb-2">
              Summary
            </label>
            <textarea
              value={summary}
              onChange={handleSummaryChange}
              rows="8"
              placeholder="Write a brief summary about yourself..."
              className="w-full p-2 border border-gray-300 rounded-md"
            />
          </div>
          <div className="mb-6">
            <button type="submit" className="btn btn-primary">
              Save Summary
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default StudentSummary;
