import AuthPage from "../components/AuthenticationPage";
import {
  AuthCard,
  LoginForm,
  RegistrationModal,
} from "../components/LoginForm";

const AdminAuthPage = (props) => {
  return (
    <AuthPage>
      <div className="text-center lg:text-left grow">
        <h1 className="text-5xl font-bold">AlumConnect</h1>
        <p className="py-6 text-3xl">Admin Authentication</p>
      </div>
      <AuthCard>
        <LoginForm userType="admin" />
      </AuthCard>
      <RegistrationModal />
    </AuthPage>
  );
};

export default AdminAuthPage;
