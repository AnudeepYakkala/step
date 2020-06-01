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

/**
 * Adds a random quote to the page.
 */
function addRandomQuote() {
  const quotes = [
    'You can\'t stop change, any more than you can stop the suns from setting' +
        '. –Shmi Skywalker',
    'You miss 100% of the shots you don’t take.' +
        ' –Wayne Gretzky',
    'Challenges are what make life interesting and' +
        ' overcoming them is what makes life meaningful. –Joshua J. Marine',
    'A person who never made a mistake never tried anything new. ' +
        '–Albert Einstein',
    'Don\'t cry because it\'s over, smile because it ' +
        'happened. Dr. Suess',
    'You can\'t go back and change the beginning but ' +
        'you can start where you are and change the ending. –C.S. Lewis',
    'I can\'t change the direction of the wind but I can adjust my sails ' +
        'to always reach my destination. –Jimmy Dean',
    'A champion is defined ' +
        'not by their wins but by how they can recover when they fall.' +
        ' –Serena Williams',
    'Motivation comes from working on things we care abo' +
        'ut. –Sheryl Sandberg',
    'Help me, Obi-Wan Kenobi. You are my only hope. ' +
        '–Lelia Organa',
    'The force will be with you. Always. –Obi Wan Kenobi',
    'Never tell me the odds! –Han Solo',
  ];

  // Pick a random quote.
  const quote = quotes[Math.floor(Math.random() * quotes.length)];

  // Add it to the page.
  const quoteContainer = document.getElementById('quote-container');
  quoteContainer.innerText = quote;

  // Source: https://tobiasahlin.com/moving-letters
  const textWrapper = document.querySelector('.ml2');
  textWrapper.innerHTML = textWrapper.textContent.replace(
      /\S/g, '<span class=\'letter\'>$&</span>');

  anime.timeline({loop: false}).add({
    targets: '.ml2 .letter',
    scale: [4, 1],
    opacity: [0, 1],
    translateZ: 0,
    easing: 'easeOutExpo',
    duration: 950,
    delay: (el, i) => 70 * i,
  });
}
