import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import SignInPage from './pages/SignInPage.jsx';
import SignUpPage from './pages/SignUpPage.jsx';
import DashBoardPage from './pages/Dashboard.jsx';
import './App.css';

function App() {

  return(
    <Router>

      <Routes>
        {/* 2. /login 경로로 접속하면 LoginPage를 보여주도록 설정. */}
        <Route path="/" element={<SignInPage />} />
        <Route path="/SignInPage" element={<SignInPage />} />
        <Route path="/SignUpPage" element={<SignUpPage/>} />
        <Route path="/DashBoardPage" element={<DashBoardPage />} />
      </Routes>
    </Router>
  );

}

export default App
