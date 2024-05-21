import { useState } from "react"
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

// axios 기본 설정 변경
axios.defaults.withCredentials = true;

const First = () => {
    const [value, setValue] = useState('');
    const navigate = useNavigate();
    const enter = (nickName) => {

        axios.post('http://localhost:30006/user/join', { "nickName": nickName })
            .then(response => {
                console.log(response.data);
                navigate('/gate');
            })
            .catch(error => {
                console.error('Error fetching data:', error);
            });
    };

    const handleChange = (event) => {
        setValue(event.target.value);
    };

    const handleClick = () => {
        enter(value)
    };

    const handleKeyDown = (event) => {
        const key = event.code;
        switch (key) {
            case 'Enter':
                enter(value)
                break;
            default:
        }
    }

    return (
        <div className="App">
            <header>
            </header>
            <div>
                <section>
                    <input type="text" value={value} onChange={handleChange} onKeyDown={handleKeyDown} />
                    <button onClick={handleClick}>입장</button>
                </section>
            </div>
        </div>
    );

}

export default First;