const el = document.getElementById('messages')
el.scrollTop = el.scrollHeight

const socket = new SockJS(`${url}/chat/connect`);
const stompClient = Stomp.over(socket);


// DOM Element
const messages = document.getElementById('messages');
const messageInput = document.getElementById('messageInput');
const sendButton = document.getElementById('sendButton');

const csrfToken = $("meta[name='_csrf']").attr("content");
const csrfHeader = $("meta[name='_csrf_header']").attr("content");

let chatCount = 0;
const maxChatCount = 9;

const headers = {
    'senderName': username,
    'roomId': roomId,
    'roomStatus': roomStatus,
    "X-CSRF-TOKEN": csrfToken
};
const lowerStatus = roomStatus.toLowerCase();
stompClient.connect(headers, function (frame) {
    // 구독 설정 roomStatus


    if (lowerStatus === "group") {
        stompClient.subscribe(`/chat/${lowerStatus}/${roomId}`, function (messageOutput) {
            showMessageOutput(JSON.parse(messageOutput.body));
        });
    } else {
        stompClient.subscribe(`/chat/${lowerStatus}/${roomId}`, function (messageOutput) {
            showMessageOutput(JSON.parse(messageOutput.body));
        });
    }

    sendMessage(`${username}님이 입장하셨습니다.`, "ENTER",lowerStatus);

    // Send 버튼 이벤트
    sendButton.addEventListener('click', function () {
        sendMessage(messageInput.value, "SEND",lowerStatus);
        messageInput.value = '';
    });

    messageInput.addEventListener('keydown', function (event) {
        if (event.key === "Enter") {
            event.preventDefault();
            sendButton.click();
        }
    });
    window.onpopstate = function () {
        sendExit();
        exitRoom();
    }
    window.addEventListener("beforeunload", function (event) {
        sendExit();
        exitRoom();
    });
});

function sendMessage(message, status, toStatus) {
    stompClient.send(`/pub/${toStatus}/${roomId}`, {},
        JSON.stringify({
            'roomId': roomId,
            'message': message,
            'senderName': username,
            'status': status,
        }));
}

function showMessageOutput(messageData) {
    if (messageData.status === "DELETE" || (messageData.status === "EXIT" && messageData.senderName === username)) {
        exitRoom();
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

    const senderElement2 = document.createElement('div');
    senderElement2.classList.add("flex", "items-center", "h-full");
    const senderElement3 = document.createElement('div');
    senderElement3.classList.add("flex-shrink-0", "truncate");
    const senderElement = document.createElement('span');
    senderElement.textContent = messageData.senderName;
    senderElement.classList.add('w-6', 'h-6', 'rounded-full', 'whitespace-nowrap');


    senderElement2.appendChild(senderElement3);
    senderElement3.appendChild(senderElement);

    if (messageData.status === "EXIT") {
        detailsElement.classList.add('flex', 'items-center', 'justify-center');
        textElement.classList.add('bg-pink-500', 'text-white');
        spanElement.classList.add('rounded-lg');
        senderElement.classList.add('hidden');
    } else if (messageData.status === "ENTER") {
        detailsElement.classList.add('flex', 'items-center', 'justify-center');
        textElement.classList.add('bg-blue-500', 'text-white');
        spanElement.classList.add('rounded-lg');
        senderElement.classList.add('hidden');
    } else if (messageData.senderName === username) {
        detailsElement.classList.add('flex', 'items-end', 'justify-end');
        textElement.classList.add('order-1', 'items-end');
        spanElement.classList.add('rounded-br-none', 'bg-blue-600', 'text-white');
        senderElement2.classList.add('order-2');
    } else {
        detailsElement.classList.add('flex', 'items-end');
        textElement.classList.add('order-2', 'items-start');
        spanElement.classList.add('rounded-bl-none', 'bg-gray-300', 'text-gray-600');
        senderElement.classList.add('order-1');
    }

    // 생성된 모든 요소를 조립합니다.
    textElement.appendChild(spanElement);
    detailsElement.appendChild(textElement);
    detailsElement.appendChild(senderElement2);
    messageElement.appendChild(detailsElement);

    //메시지가 일정 개수를 넘어가면 맨 위에 것을 삭제

    if (chatCount >= maxChatCount) {
        let firstElementChild = messages.firstElementChild;
        // 초과된 메시지 삭제
        messages.removeChild(firstElementChild);
        replaceOrAddClass(messages.children[0],"opacity-50","opacity-25");
        replaceOrAddClass(messages.children[1],"opacity-75","opacity-50");
        messages.children[2].classList.add("opacity-75");

        chatCount--;
    }

    // 완성된 메시지를 메시지 목록에 추가합니다.
    messages.appendChild(messageElement);
    chatCount++;
    // 스크롤을 최신 메시지 위치로 이동합니다.
    messages.scrollTop = messages.scrollHeight;
}

function replaceOrAddClass(element, oldClass, newClass) {
    if (element.classList.contains(oldClass)) {
        element.classList.replace(oldClass, newClass);
    } else {
        element.classList.add(newClass);
    }
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
        sendExit()
    }));
    enterButton.addEventListener('click', () => {
        const modal = document.querySelector('#exit');
        modal.classList.add('hidden');
    });
});

function sendExit() {
    sendMessage(`${username}님이 나갔습니다.`, "EXIT",lowerStatus);

}
function exitRoom() {
    stompClient.disconnect();
    window.location.href = `${url}/chat/rooms`;
}
