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
 * Adds a random Star Wars Quote to the page.
 */
function addRandomStarWarsQuote() {
  const quotes =
      ['Help me, Obi-Wan Kenobi. You are my only hope. -Lelia Organa', 'The force will be with you. Always. -Obi Wan Kenobi', 
      'Never tell me the odds! -Han Solo', 'You can\'t stop change, any more than you can stop the suns from setting. -Shmi Skywalker'];

  // Pick a random greeting.
  const quote = quotes[Math.floor(Math.random() * quotes.length)];

  // Add it to the page.
  const quoteContainer = document.getElementById('quote-container');
  quoteContainer.innerText = quote;
}
