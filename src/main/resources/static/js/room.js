const el = document.getElementById('messages')
el.scrollTop = el.scrollHeight

const socket = new SockJS('http://localhost:8080/chat/connect');
const stompClient = Stomp.over(socket);


// DOM Element
const messages = document.getElementById('messages');
const messageInput = document.getElementById('messageInput');
const sendButton = document.getElementById('sendButton');


const headers = {
    'username': username,
    'roomId': roomId,
    'userId': userId
};
stompClient.connect(headers, function (frame) {
    // 구독 설정
    stompClient.subscribe('/sub/chat/' + roomId, function (messageOutput) {
        showMessageOutput(JSON.parse(messageOutput.body));
    }, headers);
    sendMessage(`${username}님이 입장하셨습니다.`, "ENTER");

    // Send 버튼 이벤트
    sendButton.addEventListener('click', function () {
        sendMessage(messageInput.value, "SEND");
        messageInput.value = '';
    });

    messageInput.addEventListener('keydown', function (event) {
        if (event.key === "Enter") {
            event.preventDefault();
            sendButton.click();
        }
    });

    window.addEventListener("beforeunload", function (event) {
        stompClient.disconnect();
    });
});

function sendMessage(message, status) {
    stompClient.send("/pub/chat/" + roomId, {},
        JSON.stringify({
            'roomId': roomId,
            'message': message,
            'sender': username,
            'senderId': userId,
            'status': status
        }));
}

function showMessageOutput(messageData) {
    if (messageData.status === "EXIT") {
        stompClient.disconnect();
        window.location.href = 'http://localhost:8080/chat/rooms'; // 홈으로 이동
        return;
    }

    // 메시지를 담을 chat-message div를 생성합니다.
    const messageElement = document.createElement('div');
    messageElement.classList.add('chat-message');

    // 메시지의 상세 내용을 담을 div를 생성합니다.
    const detailsElement = document.createElement('div');
    const spanElement = document.createElement('span');
    const textElement = document.createElement('div');
    textElement.classList.add('flex', 'flex-col', 'space-y-2', 'text-xs', 'max-w-xs', 'mx-2', 'items-start');
    spanElement.classList.add('px-4', 'py-2', 'rounded-lg', 'inline-block');

    spanElement.textContent = messageData.message;
    // <div className="w-6 h-6 rounded-full order-2">hello</div>
    const senderElement = document.createElement('div');
    senderElement.textContent = messageData.sender;
    senderElement.classList.add('w-6', 'h-6', 'rounded-full');

    console.log("username:"+username);
    console.log("messageData:"+messageData.sender);
    if (messageData.sender === username) {
        detailsElement.classList.add('flex', 'items-end', 'justify-end');
        textElement.classList.add('order-1', 'items-end');
        spanElement.classList.add('rounded-br-none', 'bg-blue-600', 'text-white');
        senderElement.classList.add('order-2');
    } else {
        detailsElement.classList.add('flex', 'items-end');
        textElement.classList.add('order-2', 'items-start');
        spanElement.classList.add('rounded-bl-none', 'bg-gray-300', 'text-gray-600');
        senderElement.classList.add('order-1');
    }

    // 생성된 모든 요소를 조립합니다.
    textElement.appendChild(spanElement);
    detailsElement.appendChild(textElement);
    detailsElement.appendChild(senderElement);
    messageElement.appendChild(detailsElement);

    // 완성된 메시지를 메시지 목록에 추가합니다.
    messages.appendChild(messageElement);

    // 스크롤을 최신 메시지 위치로 이동합니다.
    messages.scrollTop = messages.scrollHeight;
}

document.addEventListener("DOMContentLoaded", function () {
    // 모달 토글 버튼을 모두 선택합니다.
    const toggleButtons = document.querySelectorAll('[data-modal-toggle]');

    toggleButtons.forEach(function (button) {
        button.addEventListener('click', function () {
            // data-modal-toggle 속성에서 대상 모달의 id를 가져옵니다.
            const targetModalId = button.getAttribute('data-modal-toggle');
            const targetModal = document.getElementById(targetModalId);

            // 현재 모달의 표시 상태를 확인하고 토글합니다.
            if (targetModal.classList.contains('hidden')) {
                targetModal.classList.remove('hidden');
            } else {
                targetModal.classList.add('hidden');
            }
        });
    });

    const closeButton = document.querySelector('#closeButton');
    const enterButton = document.querySelector('#enterButton');
    const exitButton = document.querySelector('#exitButton');

    closeButton.addEventListener('click', () => {
        const modal = document.querySelector('#exit');
        modal.classList.add('hidden');
    });
    exitButton.addEventListener('click', (navigateAway => {
        exitRoom();
    }));
    enterButton.addEventListener('click', () => {
        const modal = document.querySelector('#exit');
        modal.classList.add('hidden');
    });

    function exitRoom() {
        let formData = new FormData();

        formData.append("username", username);
        formData.append("roomId", roomId);
        const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
        const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

        fetch("/chat/room/delete", {
            method: "DELETE",
            headers: {
                "Content-Type": "application/json",
                [csrfHeader]: csrfToken
            },
            body: formData
        }).then(r => {
            if (r.status === 200) {
                sendMessage(`${username}님이 나갔습니다.`, "EXIT");
                window.location.href = 'http://localhost:8080/chat/rooms';
            }
        });
    }
});