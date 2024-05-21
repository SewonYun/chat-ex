import { BrowserRouter as Router, Route, Routes } from "react-router-dom"
import './App.css';
import { useState } from 'react';
import Chat from './Chat'
import Gate from './ChatGate'
import { FIRST_SCREEN, ROOM_MAKE_SCREEN, CHAT_SCREEN } from './Const'
import First from './First'

function App() {
  const [screen, setScreen] = useState(null);

  return (
    <Router>
      <Routes>
        <Route path="/" element={<First />} exact={true} />
        <Route path="/gate" element={<Gate />} />
        <Route path="/chat" element={<Chat />} />
      </Routes>
    </Router>
  );
}

export default App;
