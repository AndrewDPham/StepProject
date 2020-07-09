// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

//EventListener for buttons with dropdowns
let coll = document.getElementsByClassName("collapse");
let i;

for (i = 0; i < coll.length; i++) {
    coll[i].addEventListener("click", function() {
        this.classList.toggle("active");
        let content = this.nextElementSibling;
        if (content.style.display === "table") {
            content.style.display = "none";
        } else {
            content.style.display = "table";
        }
    });
}

/**
 * Grabs the comments from the JSP and displays as a list in HTML 
 */
function grabComment(){
    console.log("Grabbing Comment");
    fetch("/data").then(response => response.json()).then((list) => {
        console.log(list);
        const commentElement = document.getElementById("comment-container");
        list.forEach((comment) => {
            commentElement.appendChild(createListElement(comment.name + " said " + comment.content)); 
        });
    });
}

function fetchBlobstoreUrlAndDisplayMemePosts() {
    fetch('/blobstore-upload-url')
        .then((response) => {
            return response.text();
        })
        .then((imageUploadUrl) => {
            const messageForm = document.getElementById('my-form');
            messageForm.action = imageUploadUrl;
        });

    fetch("/display-memepost")
        .then((response) => {
            return response.json();
        })
        .then((list) => {
            console.log(list);
            const memepostElement = document.getElementById("memepost-container");
            list.forEach((memepost) => {
                memepostElement.appendChild(createContainer(memepost.imageUrl)); 
            });
        });

}

function createContainer(url) {
    const imgElement = document.createElement("img");
    imgElement.src = url;
    return imgElement;
}

/**
 * Creates an <li> element containing text.
 */
function createListElement(text) {
    const liElement = document.createElement('li');
    liElement.innerText = text;
    return liElement;
}   

/**
 * Adds a random greeting to the page.
 */
function addRandomGreeting() {
  const greetings =
      ['Hello world!', '¡Hola Mundo!', '你好，世界！', 'Bonjour le monde!'];

  // Pick a random greeting.
  const greeting = greetings[Math.floor(Math.random() * greetings.length)];

  // Add it to the page.
  const greetingContainer = document.getElementById('greeting-container');
  greetingContainer.innerText = greeting;
}








