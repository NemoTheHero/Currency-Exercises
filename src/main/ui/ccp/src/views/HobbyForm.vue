<script setup lang="ts">
import {ref} from "vue";
import {useRouter} from "vue-router";

const props = defineProps({
  userId: {
    type: Number,
    required: true
  }
})

const router = useRouter();

const MAX_HOBBY_VALUE = 7;
const MID_HOBBY_VALUE = 3;
const MIN_HOBBY_VALUE = 1;

const hobbyInput = ref("");
const correlatedHobbies = ref([]);

const selections = ref<string[]>([]);

const loadingSuggestions = ref(false);
const lastAddedHobby = ref("");

const userName = ref("");

fetch(`http://localhost:8080/user/findById?userId=${props.userId}`)
    .then(response => response.json())
    .then(data => {
      userName.value = data.userName;
    })
    .catch(error => {
      console.error("Error fetching user data:", error);
    });

function clickAdd() {
  if (!hobbyInput.value || hobbyInput.value.trim() === "") {
    return;
  }
  loadingSuggestions.value = true

  if (correlatedHobbies.value.length) {
    submitHobbies();
  }

  const newHobby = hobbyInput.value.trim();
  lastAddedHobby.value = newHobby;
  addHobby(newHobby)

  fetch(`http://localhost:8080/recommendations/hobby?${new URLSearchParams({hobby: newHobby}).toString()}`)
      .then(response =>
          response.json())
      .then(data => {
        correlatedHobbies.value = data[newHobby.replaceAll(" ", "_")].correlated_hobbies;
        correlatedHobbies.value.forEach((parent) => {
          parent.name = parent.name.replaceAll("_", " ");
          parent.selected = false;
          parent.correlated_hobbies.forEach((child) => {
            child.name = child.name.replaceAll("_", " ");
            child.selected = false;
          });
        });
        console.log(data)
      })
      .catch(error => {
        console.error("Error adding hobby:", error);
      })
      .finally(() => {
        loadingSuggestions.value = false;
      });

  hobbyInput.value = "";
}

function addHobby(hobby) {
  selections.value.push(hobby);
  // update values in db
}

class KeywordDTO {
  constructor(keyword, score) {
    this.keyword = keyword.replaceAll("_", " ");
    this.score = score;
  }
}

async function submitHobbies() {
  const keywordArray = [];
  if (lastAddedHobby.value) {
    keywordArray.push(new KeywordDTO(lastAddedHobby.value, MAX_HOBBY_VALUE));
  }

  correlatedHobbies.value.forEach((parent) => {
      keywordArray.push(new KeywordDTO(parent.name, parent.selected ? MAX_HOBBY_VALUE : MID_HOBBY_VALUE));

    parent.correlated_hobbies.forEach((child) => {
      let childValue = MIN_HOBBY_VALUE;
      if (parent.selected) {
        childValue = MID_HOBBY_VALUE;
      }
      if (child.selected) {
        childValue = MAX_HOBBY_VALUE;
      }

      keywordArray.push(new KeywordDTO(child.name, childValue));
    });
  });

  console.log(keywordArray)
  return fetch(`http://localhost:8080/user/addInterests?userId=${props.userId}`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(keywordArray),
  })
      .then((response) => {
        if (!response.ok) {
          throw new Error(response);
        }
      })
      .catch((error) => {
        console.error("Error submitting hobbies:", error);
      });
}

function clickChild(e, hobby, parentIndex, childIndex) {
  e.target.classList.add("selected");
  e.target.style.pointerEvents = "none";
  correlatedHobbies.value[parentIndex].correlated_hobbies[childIndex].selected = true;
  addHobby(hobby);
}

function clickParent(e, hobby, index) {
  e.target.classList.add("selected");
  e.target.style.pointerEvents = "none";
  correlatedHobbies.value[index].selected = true;
  addHobby(hobby);
}

async function clickDone() {
  await submitHobbies();
  router.push({ name: 'UserProfile', query: { userId: props.userId.toString() } });
}
</script>

<template>
  <div id="user-name" class="center-screen">{{userName ? userName : '&nbsp;'}}</div>
  <div class="center-screen">
  <div id="selections-box">
    <span class="selection" v-for="selection in selections">{{selection}}</span>
  </div>
  </div>
<div id="hobby-input-wrapper" class="center-screen">
  <input v-model="hobbyInput" id="hobby-input" name="hobby" type="text" @keydown.enter="clickAdd" />
  <button @click="clickAdd" :disabled="loadingSuggestions">Add</button>
</div>
  <div v-if="!loadingSuggestions" id="suggestion-box">
  <div class="parent-suggestion-box" v-for="(hobbyParent, i) in correlatedHobbies" :key="hobbyParent.name">
    <div class="parent-suggestion" @click="(e) => clickParent(e, hobbyParent.name, i)">{{ hobbyParent.name }}</div>
    <div class="child-suggestion-box">
    <div class="child-suggestion" v-for="(hobbyChild, j) in hobbyParent.correlated_hobbies" @click="(e) => clickChild(e, hobbyChild.name, i, j)"> {{ hobbyChild.name }}</div>
      </div>
  </div>
  </div>
  <div class="loading-box center-screen" v-else>
    Loading suggestions...
  </div>
  <div v-if="selections.length" class="center-screen">
    <button @click="clickDone" :disabled="loadingSuggestions">Done</button>
  </div>
</template>

<style scoped>
/* Layout & Text */
.center-screen {
display: flex;
justify-content: center;
align-items: center;
margin: 1rem 0;
}

#user-name {
font-size: 1.75rem;
font-weight: 600;
margin-top: 1rem;
text-align: center;
}

/* Input & Add */
#hobby-input-wrapper {
gap: 0.5rem;
}

#hobby-input {
width: 300px;
padding: 8px 12px;
border-radius: 20px;
border: 1px solid #ccc;
font-size: 1rem;
}

button {
background-color: #3b82f6;
color: white;
border: none;
padding: 8px 16px;
border-radius: 20px;
cursor: pointer;
transition: background-color 0.2s;
}

button:hover {
background-color: #2563eb;
}

/* Selections */
#selections-box {
  display: flex;
  flex-wrap: wrap-reverse;
  justify-content: center;
  align-content: flex-start;
  gap: 10px;
  max-width: 600px;
  min-height: 100px;
  margin: 20px auto;
}

.selection {
background-color: #e0f2fe;
color: #0369a1;
padding: 6px 12px;
border-radius: 9999px;
font-size: 0.95rem;
user-select: none;
}

/* Suggestion Bubbles */
#suggestion-box {
display: flex;
flex-wrap: wrap;
gap: 0.75rem;
padding: 1.5rem;
justify-content: center;
align-items: flex-start;
}

/* Both parent and child look like bubbles */
.parent-suggestion,
.child-suggestion {
background-color: #f3f4f6;
padding: 10px 14px;
border-radius: 9999px;
font-size: 0.95rem;
cursor: pointer;
transition: all 0.2s ease-in-out;
user-select: none;
  margin-bottom: 0.4rem;
}

.parent-suggestion:hover,
.child-suggestion:hover {
background-color: #dbeafe;
}

/* Selected bubbles */
.selected {
background-color: #3b82f6 !important;
color: white !important;
pointer-events: none;
}

/* Loading */
.loading-box {
font-style: italic;
font-size: 1rem;
color: #555;
margin-top: 1rem;
min-height: 265px;
display: flex;
align-items: center;
justify-content: center;
position: relative;
}
.loading-box::after {
content: '';
display: inline-block;
width: 1.2em;
height: 1.2em;
margin-left: 0.7em;
border-radius: 50%;
border: 3px solid #7cadff;
border-top: 3px solid #dbeafe;
animation: spin 0.8s linear infinite;
}
@keyframes spin {
0% { transform: rotate(0deg); }
100% { transform: rotate(360deg); }
}

button:disabled {
background-color: #bbcff4;
  cursor: default;
}
</style>