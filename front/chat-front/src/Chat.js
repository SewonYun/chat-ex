import React, { useState, useEffect } from 'react';
import { Stomp } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import _debounce from 'lodash/debounce';
import { useLocation } from 'react-router-dom';
import './App.css';
import axios from 'axios';

function Chat() {
    const [messages, setMessages] = useState([]);
    const [stompClientPub, setStompClientPub] = useState(null);
    const [value, setValue] = useState('');
    const [userId, setUserId] = useState(null);

    const location = useLocation();
    const queryParams = new URLSearchParams(location.search);

    const chatRoomId  = queryParams.get('roomId');
    const TOPIC_PUBLISH = `/app/chat/publish/${chatRoomId}`;
    const TOPIC_SUBSCRIBE = `/topic/chat/${chatRoomId}`;

    const initHistory = () => {
        axios.get('http://localhost:30006/chatting/list', {
            params: {
                chatRoomId: chatRoomId
            }
        })
        .then(response => {
            setMessages(response.data.data)
        })
        .catch(error => {
            console.error('Error fetching data:', error);
        })
    }

    const getUserId = () => {
        axios.get('http://localhost:30006/user/id')
        .then(response => {
            setUserId(response.data.data)
        })
        .catch(error => {
            console.error('Error fetching data:', error);
        })
    }

    useEffect(() => {

        getUserId()
        const socketPub = new SockJS('http://localhost:30006/ws-chat-pub');
        const clientPub = Stomp.over(socketPub);
        initHistory()
        clientPub.connect({}, () => {
            console.log('Publisher WebSocket 연결됨!');
            setStompClientPub(clientPub);
        }, (error) => {
            console.error('Publisher WebSocket 오류가 발생했습니다:', error);
        });

        const socketSub = new SockJS('http://localhost:30006/ws-chat-sub');
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
            if (clientPub) {
                clientPub.disconnect();
            }
            if (clientSub) {
                clientSub.disconnect();
            }
        };
    }, []);

    const sendMessage = (message) => {
        if (stompClientPub && stompClientPub.connected) {
            stompClientPub.send(TOPIC_PUBLISH, {}, JSON.stringify({
                id: null,
                userId: userId,
                chatRoomId: chatRoomId,
                message: message,
                createdAt: new Date().toISOString()
            }));
        } else {
            console.error('Publisher STOMP 연결이 없습니다.');
        }
    };

    const debouncedSendMessage = _debounce(sendMessage, 500); // 500ms 딜레이

    const handleChange = (event) => {
        setValue(event.target.value);
    };

    const handleClick = () => {
        debouncedSendMessage(value)
    };

    const handleKeyDown = (event) => {
        const key = event.code;
        switch (key) {
            case 'Enter':
                debouncedSendMessage(value)
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
                    <ul>
                        {messages.map((message, index) => (
                            <div key={index}>
                                {message.message}
                            </div>
                        ))}
                    </ul>
                </section>
                <section>
                    <input type="text" value={value} onChange={handleChange} onKeyDown={handleKeyDown} />
                    <button onClick={handleClick}>Send Message</button>
                </section>
            </div>
        </div>
    );
}

export default Chat;