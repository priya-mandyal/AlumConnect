import AuthPage from "../components/AuthenticationPage";
import LoginAuth from "../components/LoginForm";

const LoginPage = () => {
  return (
    <>
      <AuthPage>
        <LoginAuth userType="user" />
      </AuthPage>
    </>
  );
};

export default LoginPage;
