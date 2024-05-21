import React, { useState, useEffect } from 'react';
import { Stomp } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import _debounce from 'lodash/debounce';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

function Gate() {
    const [messages, setMessages] = useState([]);
    const [value, setValue] = useState('');

    const navigate = useNavigate();

    const enter = () => {
        navigate('/chat');
    };

    const handleChange = (event) => {
        setValue(event.target.value);
    };

    const handleClick = () => {
        createRoom(value)
    };

    const handleKeyDown = (event) => {
        const key = event.code;
        switch (key) {
            case 'Enter':
                createRoom(value)
                break;
            default:
                break;
        }
    }

    const createRoom = (roomName) => {

        axios.post('http://localhost:30006/chatroom/make', { "roomName": roomName })
            .then(response => {
                console.log(response.data);
            })
            .catch(error => {
                console.error('Error fetching data:', error);
            });
    };

    const TOPIC_SUBSCRIBE = "/topic/list"

    useEffect(() => {
        const socketSub = new SockJS('http://localhost:30006/ws-list-sub');
        const clientSub = Stomp.over(socketSub);

        clientSub.connect({}, () => {
            console.log('WebSocket 연결!');
            clientSub.subscribe(TOPIC_SUBSCRIBE, (message) => {
                const parsedMessage = JSON.parse(message.body);
                console.log('Received chat message:', parsedMessage);
                setMessages(prevMessages => [...prevMessages, JSON.parse(message.body)])
            });
        }, (error) => {
            console.error('WebSocket 오류가 발생했습니다:', error);
        });

        return () => {
            if (clientSub) {
                clientSub.disconnect();
            }
        };
    }, []);


    return (
        <div className="App">
            <header>
            </header>
            <div>
                <section>
                    <input type="text" value={value} onChange={handleChange} onKeyDown={handleKeyDown} />
                    <button onClick={handleClick}>방제목 입력</button>
                </section>
            </div>
        </div>
    );

}

export default Gate;