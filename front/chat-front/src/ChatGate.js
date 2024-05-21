import React, { useState, useEffect } from 'react';
import { Stomp } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import _debounce from 'lodash/debounce';
import { useNavigate, useLocation } from 'react-router-dom';
import axios from 'axios';


function Gate() {
    const [roomList, setRoomList] = useState([]);
    const [value, setValue] = useState('');

    const navigate = useNavigate();

    const enter = (roomId) => {
        navigate(`/chat?roomId=${roomId}`);
    };

    const handleChange = (event) => {
        setValue(event.target.value);
    };

    const handleClick = () => {
        createRoom(value)
    };

    const enterRoom = (event) => {
        var roomId = event.target.value;
        axios.post('http://localhost:30006/enter/room', { "roomId": roomId })
            .then(response => {
                setRoomList(response.data.data)
            })
            .catch(error => {
                console.error('Error fetching data:', error);
            });

        enter(roomId)
    }

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

    const initRoomList = () => {

        axios.get('http://localhost:30006/chatroom/list')
            .then(response => {
                console.log(response.data.data);
                setRoomList(response.data.data)
            })
            .catch(error => {
                console.error('Error fetching data:', error);
            });
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

        initRoomList()
        clientSub.connect({}, () => {
            console.log('WebSocket 연결!');
            clientSub.subscribe(TOPIC_SUBSCRIBE, (message) => {
                const parsedMessage = JSON.parse(message.body);
                setRoomList(parsedMessage)
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
                <section>
                    <ul>
                        {roomList.map((room, index) => (
                            <div key={index}>
                                <span>{room.userCount}</span>
                                <span> - - - - </span>
                                <span>{room.roomName}</span>
                                <span> :::::::::- - - - :::::::: </span>
                                <span>{room.recentMessage?.message}</span>
                                <button onClick={enterRoom} value={room.id}>입장</button>
                            </div>
                        ))}
                    </ul>
                </section>
            </div>
        </div>
    );

}

export default Gate;