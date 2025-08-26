<script setup lang="ts">
import {ref} from "vue";

const hobbyInput = ref("");
const suggestions = ref([
  "Reading",
  "Traveling",
  "Cooking",
  "Gardening",
  "Photography",
  "Painting",
  "Hiking",
  "Cycling",
  "Fishing",
  "Knitting",
  "Dancing",
  "Writing",
  "Yoga",
  "Bird Watching",
  "Collecting (stamps, coins, etc.)"
]);

const selections = ref<string[]>([]);

function clickAdd() {
  if (!hobbyInput.value || hobbyInput.value.trim() === "") {
    return;
  }
  const newHobby = hobbyInput.value.trim();
  selections.value.push(newHobby);
  hobbyInput.value = "";

  fetch(`http://localhost:8080/recommendations/hobby?${new URLSearchParams({hobby: newHobby}).toString()}`).then(response => {
    console.log("Response received:", response);
  }).catch(error => {
    console.error("Error adding hobby:", error);
  });
}

// function addHobby(value, weight) {
//
// }
</script>

<template>
  <div id="selections-box">
    <span v-for="selection in selections">{{selection}}</span>
  </div>
<div id="hobby-input-wrapper" class="center-screen">
  <input v-model="hobbyInput" id="hobby-input" name="hobby" type="text" />
  <button @click="clickAdd">Add</button>
</div>
  <div id="suggestion-box">
  <span v-for="suggestion in suggestions" :key="suggestion" class="suggestion">{{ suggestion }}

  </span>
  </div>
  <div class="center-screen">
    <button>Done</button>
  </div>
</template>

<style scoped>
#suggestion-box {
  display: grid;
  grid-template-columns: 1fr 1fr 1fr;
  gap: 10px;
  margin-top: 20px;
  margin-bottom: 20px;
}
#selections-box {
  display: grid;
  grid-template-columns: 1fr 1fr 1fr;
  gap: 10px;
  margin-top: 20px;
  margin-bottom: 20px;
}

#hobby-input {
  width: 300px;
}

.center-screen {
  display: flex;
  justify-content: center;
  align-items: center;
}

.suggestion {
  border-radius: 6px;
  background-color: #313131;
  color: white;
  padding: 2px;
}

input {
  border-radius: 20px;
  padding: 6px;
  border: 1px solid gray
}

button {
  border: 0;
  padding: 6px 10px;
  border-radius: 20px;
  background: #3232b8;
  color: white;
}
</style>