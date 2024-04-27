import React from "react";

const HomePage = (props) => {
  const receivedData = props.data;

  return (
    <div>
      <p>{receivedData}</p>
    </div>
  );
};

export default HomePage;
