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
var coll = document.getElementsByClassName("collapse");
var i;

    for (i = 0; i < coll.length; i++) {
        coll[i].addEventListener("click", function() {
        this.classList.toggle("active");
        var content = this.nextElementSibling;
        if (content.style.display === "table") {
            content.style.display = "none";
        } else {
            content.style.display = "table";
        }
    });
    }
// Tutorial Steps:    
/*
function getHelloName(){
    
    console.log("Fetching Hello [NAME]");

    const responsePromise = fetch("/data");

    responsePromise.then(HandleResponse);
}

function HandleResponse(response){

    console.log("Handling Response");

    const textPromise = response.json();

    textPromise.then(addQuoteToDOM);
}

function addQuoteToDOM(quote){

    console.log("Adding quote to DOM:" + quote);

    const quoteContainer = document.getElementById('quote-container');
    
    quoteContainer.innerText = quote;
}

function getJson(){
    console.log("Grabbing Json");

    fetch('/data').then(response => response.json()).then((messages) => {
        console.log(messages);
        const messagesListElement = document.getElementById('json-container');
        console.log("Here");
        messagesListElement.innerText = messages[0];
    });
}
*/

/** Grabs the comment from the JSP and displays as a list in HTML */
function grabComment(){
    console.log("Grabbing Comment");
    fetch("/data").then(response => response.json()).then((comment) => {
        console.log(comment);
        const commentElement = document.getElementById("comment-container");
        console.log("Here");
        comment.forEach((line) => {
            commentElement.appendChild(createListElement(line)); 
        });

    });

}

/** Creates an <li> element containing text. */
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








