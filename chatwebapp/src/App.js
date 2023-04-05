//pages
import LoginPage from './pages/LoginPage';
import RegistrationPage from './pages/RegistrationPage';
import RegistrationConfirmationPage from './pages/RegistrationConfirmationPage';
import PostRegistrationPage from './pages/PostRegistrationPage';

//router
import {Route, Routes} from 'react-router-dom';

export default function App() {
  return (
    <div className="App">
      <Routes>
        <Route path="/" element={<LoginPage/>} />
        <Route path="/registration" element={<RegistrationPage/>} />
        <Route path="/registration/confirm/:token" element={<RegistrationConfirmationPage/>} />
        <Route path="/registration/completed" element={<PostRegistrationPage/>} />
      </Routes>
    </div>
  );
}

