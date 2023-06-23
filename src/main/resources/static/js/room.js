const el = document.getElementById('messages')
el.scrollTop = el.scrollHeight

const socket = new SockJS('http://localhost:8080/chat/connect');
const stompClient = Stomp.over(socket);


// DOM Element
const messages = document.getElementById('messages');
const messageInput = document.getElementById('messageInput');
const sendButton = document.getElementById('sendButton');

stompClient.connect({}, function(frame) {
    // 구독 설정
    stompClient.subscribe('/sub/chat/' + roomId, function(messageOutput) {
        showMessageOutput(JSON.parse(messageOutput.body));
    });

    // Send 버튼 이벤트
    sendButton.addEventListener('click', function() {
        sendMessage(messageInput.value);
        messageInput.value = '';
    });

    messageInput.addEventListener('keydown', function(event) {
        if (event.key === "Enter") {
            event.preventDefault();
            sendButton.click();
        }
    });

    window.addEventListener("beforeunload", function(event) {
        stompClient.disconnect();
    });
});

function sendMessage(message) {
    stompClient.send("/pub/chat/" + roomId, {},
        JSON.stringify({
            'roomId': roomId,
            'message': message,
            'sender': username
        }));
}

function showMessageOutput(messageData) {
    // 메시지를 담을 chat-message div를 생성합니다.
    const messageElement = document.createElement('div');
    messageElement.classList.add('chat-message');

    // 메시지의 상세 내용을 담을 div를 생성합니다.
    const detailsElement = document.createElement('div');
    const spanElement = document.createElement('span');
    const textElement = document.createElement('div');
    textElement.classList.add('flex', 'flex-col', 'space-y-2', 'text-xs', 'max-w-xs', 'mx-2', 'items-start');
    spanElement.classList.add('px-4', 'py-2', 'rounded-lg', 'inline-block');
    console.log(messageData.message);
    spanElement.textContent = messageData.message;
    // <div className="w-6 h-6 rounded-full order-2">hello</div>
    const senderElement = document.createElement('div');
    senderElement.textContent = messageData.sender;
    senderElement.classList.add('w-6', 'h-6', 'rounded-full');

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