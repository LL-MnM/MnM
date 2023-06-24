const main = document.querySelector("#main");
const qna = document.querySelector("#qna");
const result = document.querySelector("#result");
const endPoint = 12;
const select = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];
let prevAnswers = [];
let qIdx = 0;

function calResult() {
    var result = select.indexOf(Math.max(...select));
    return result;
}

function setResult() {
    let point = calResult();
    const resultName = document.querySelector('.resultName');
    const resultDesc = document.querySelector('.resultDesc');
    const imgDiv = document.querySelector('#resultImg');

    const fitButton = document.querySelector('#fitButton');
    const notFitButton = document.querySelector('#notFitButton');

    function updateResult(newPoint) {
        resultName.innerText = infoList[newPoint].name;
        resultDesc.innerHTML = infoList[newPoint].desc;

        var resultImg = document.createElement('img');
        var imgURL = '/mbtiTest/img/image-' + newPoint + '.png';
        resultImg.src = imgURL;
        resultImg.alt = newPoint;
        resultImg.classList.add('img-fluid');

        imgDiv.innerHTML = ''; // clear the existing image
        imgDiv.appendChild(resultImg);
    }

    updateResult(point); // initial result update

    var currentPoint = point;

    fitButton.addEventListener('click', () => {
        let nextPoint = infoList[currentPoint].fit[0];
        updateResult(nextPoint);
        currentPoint = nextPoint;  // update the currentPoint after the result is updated
    });

    notFitButton.addEventListener('click', () => {
        let nextPoint = infoList[currentPoint].notFit[0];
        updateResult(nextPoint);
        currentPoint = nextPoint;  // update the currentPoint after the result is updated
    });
}

function goResult() {
    qna.style.WebkitAnimation = "fadeOut 1s";
    qna.style.animation = "fadeOut 1s";
    setTimeout(() => {
        result.style.WebkitAnimation = "fadeIn 1s";
        result.style.animation = "fadeIn 1s";
        setTimeout(() => {
            qna.style.display = "none";
            result.style.display = "block";
        }, 450)
    }, 450)
    setResult();
}

function addAnswer(answerText, qIdx, idx) {
    var a = document.querySelector('.answerBox');
    var answer = document.createElement('button');
    answer.classList.add('answerList');
    answer.classList.add('my-3');
    answer.classList.add('p-3');
    answer.classList.add('mx-auto');
    answer.classList.add('fadeIn');

    a.appendChild(answer);
    answer.innerHTML = answerText;

    answer.addEventListener("click", function () {
        var children = document.querySelectorAll('.answerList');
        for (let i = 0; i < children.length; i++) {
            children[i].disabled = true;
            children[i].style.WebkitAnimation = "fadeOut 0.5s";
            children[i].style.animation = "fadeOut 0.5s";
        }
        setTimeout(() => {
            var target = qnaList[qIdx].a[idx].type;
            for (let i = 0; i < target.length; i++) {
                select[target[i]] += 1;
            }

            prevAnswers.push({qIdx, answer: idx});

            for (let i = 0; i < children.length; i++) {
                children[i].style.display = 'none';
            }
            goNext(++qIdx);
        }, 450)
    }, false);
}

function goNext(qIdx) {
    if (qIdx === endPoint) {
        goResult();
        return;
    }

    var q = document.querySelector('.qBox');
    q.innerHTML = qnaList[qIdx].q;
    var answerBox = document.querySelector('.answerBox');
    answerBox.innerHTML = '';  // Reset the answer box
    for (let i in qnaList[qIdx].a) {
        addAnswer(qnaList[qIdx].a[i].answer, qIdx, i);
    }
    var status = document.querySelector('.statusBar');
    status.style.width = (100 / endPoint) * (qIdx + 1) + '%';
    var countStatusNum = document.querySelector('.countStatus');
    countStatusNum.innerHTML = (qIdx + 1) + "/" + endPoint;

    var backButton = document.querySelector('#backButton');
    if (qIdx === 0) {
        backButton.style.display = 'none';
    } else {
        backButton.style.display = 'block';
    }
}

backButton.addEventListener('click', function () {
    if (prevAnswers.length === 0) {
        return;
    }
    const lastAnswer = prevAnswers.pop();
    const target = qnaList[lastAnswer.qIdx].a[lastAnswer.answer].type;
    for (let i = 0; i < target.length; i++) {
        select[target[i]] -= 1;
    }
    goNext(lastAnswer.qIdx);
});

function begin() {
    main.style.WebkitAnimation = "fadeOut 1s";
    main.style.animation = "fadeOut 1s";
    setTimeout(() => {
        qna.style.WebkitAnimation = "fadeIn 1s";
        qna.style.animation = "fadeIn 1s";
        setTimeout(() => {
            main.style.display = "none";
            qna.style.display = "block";
        }, 450)
        qIdx = 0;
        prevAnswers = [];
        goNext(qIdx);
    }, 450);

}
