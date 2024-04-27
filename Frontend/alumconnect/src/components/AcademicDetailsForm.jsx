import React, { useState } from "react";

const AcademicDetailsForm = (props) => {
  const [academicDetails, setAcademicDetails] = useState([]);
  const [newDetail, setNewDetail] = useState({ university: "", degree: "" });

  const handleUniversityChange = (e) => {
    setNewDetail({ ...newDetail, university: e.target.value });
  };

  const handleDegreeChange = (e) => {
    setNewDetail({ ...newDetail, degree: e.target.value });
  };

  const addAcademicDetail = () => {
    if (newDetail.university.trim() !== "" && newDetail.degree.trim() !== "") {
      setAcademicDetails((prevDetails) => [...prevDetails, { ...newDetail }]);
      setNewDetail({ university: "", degree: "" });
    }
  };

  const removeAcademicDetail = (index) => {
    setAcademicDetails((prevDetails) =>
      prevDetails.filter((_, i) => i !== index),
    );
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if (props.onNext) {
      props.onNext(academicDetails);
    }
  };

  return (
    <div className="flex items-center justify-center min-h-screen">
      <form onSubmit={handleSubmit} className="w-2/3">
        <ul>
          {academicDetails.map((detail, index) => (
            <li key={index}>
              <p>{detail.university}</p>
              <p>{detail.degree}</p>
              <button
                type="button"
                onClick={() => removeAcademicDetail(index)}
                className="btn btn-error btn-sm mt-2"
              >
                Remove
              </button>
            </li>
          ))}
        </ul>

        <div className="form-control mt-4">
          <label className="label">
            <span className="label-text">University</span>
          </label>
          <input
            type="text"
            value={newDetail.university}
            onChange={handleUniversityChange}
            className="input input-bordered"
          />
          <label className="label mt-2">
            <span className="label-text">Degree</span>
          </label>
          <input
            type="text"
            value={newDetail.degree}
            onChange={handleDegreeChange}
            className="input input-bordered"
          />
          <button
            type="button"
            onClick={addAcademicDetail}
            className="btn btn-primary mt-2"
          >
            Add Academic Detail
          </button>
        </div>
        <div className="form-control mt-6">
          <button type="submit" className="btn btn-primary">
            Save Academic Details
          </button>
        </div>
      </form>
    </div>
  );
};

export default AcademicDetailsForm;
