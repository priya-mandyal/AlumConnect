const AuthPage = (props) => {
  return (
    <div className="hero min-h-screen bg-base-200">
      <div className="hero-content flex-col lg:flex-row w-3/4">
        {props.children}
      </div>
    </div>
  );
};

export default AuthPage;
