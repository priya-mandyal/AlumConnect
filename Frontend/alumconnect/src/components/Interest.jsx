import React, { useState } from "react";
import Select from "react-select";

const Interest = (props) => {
  const defaultInterests = [
    { label: "Reading", value: "Reading" },
    { label: "Traveling", value: "Traveling" },
    { label: "Coding", value: "Coding" },
  ];

  const [selectedInterests, setSelectedInterests] = useState(defaultInterests);
  const [newInterest, setNewInterest] = useState("");
  const [validationError, setValidationError] = useState("");

  const handleInterestChange = (selectedOptions) => {
    setSelectedInterests(selectedOptions);
    if (selectedOptions.length > 0) {
      setValidationError("");
    }
  };

  const handleNewInterestChange = (e) => {
    setNewInterest(e.target.value);
  };

  const addNewInterest = () => {
    if (
      newInterest.trim() !== "" &&
      !selectedInterests.some((interest) => interest.label === newInterest)
    ) {
      const newInterestOption = { label: newInterest, value: newInterest };
      setSelectedInterests((prevInterests) => [
        ...prevInterests,
        newInterestOption,
      ]);
      setNewInterest("");
    }
  };

  const removeInterest = (removedInterest) => {
    setSelectedInterests((prevInterests) =>
      prevInterests.filter(
        (interest) => interest.value !== removedInterest.value,
      ),
    );
  };

  const customStyles = {
    multiValueRemove: (base) => ({
      ...base,
      cursor: "pointer",
    }),
  };

  const customComponents = {
    MultiValueRemove: ({ innerProps, innerRef, ...props }) => (
      <div {...innerProps} ref={innerRef} style={{ cursor: "pointer" }}>
        <span onClick={() => removeInterest(props.data)}>&times;</span>
      </div>
    ),
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if (selectedInterests.length === 0) {
      setValidationError("This field is required for your profile match");
      return;
    }
    setValidationError("");
    props.onNext(selectedInterests);
  };

  return (
    <div className="flex items-center justify-center min-h-screen">
      <form onSubmit={handleSubmit}>
        <div className="form-control">
          <label className="label">
            <span className="label-text">Interests</span>
          </label>
          <Select
            isMulti
            value={selectedInterests}
            onChange={handleInterestChange}
            options={selectedInterests}
            styles={customStyles}
            components={customComponents}
          />
          {validationError && <p className="text-error">{validationError}</p>}
        </div>
        <div className="form-control mt-4">
          <label className="label">
            <span className="label-text">Add New Interest</span>
          </label>
          <div className="flex items-center">
            <input
              type="text"
              value={newInterest}
              onChange={handleNewInterestChange}
              placeholder={defaultInterests[0].label}
              className="input input-bordered"
            />
            <button
              type="button"
              onClick={addNewInterest}
              className="btn btn-primary ml-2"
            >
              Add
            </button>
          </div>
        </div>
        <div className="form-control mt-6">
          <button type="submit" className="btn btn-primary">
            Save Interests
          </button>
        </div>
      </form>
    </div>
  );
};

export default Interest;
