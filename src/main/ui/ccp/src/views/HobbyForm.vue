<script setup lang="ts">
import {ref} from "vue";

const hobbyInput = ref("");
const correlatedHobbies = ref([]);

const selections = ref<string[]>([]);

const loadingSuggestions = ref(false);

function clickAdd() {
  if (!hobbyInput.value || hobbyInput.value.trim() === "") {
    return;
  }
  loadingSuggestions.value = true

  const newHobby = hobbyInput.value.trim();
  addHobby(newHobby)

  fetch(`http://localhost:8080/recommendations/hobby?${new URLSearchParams({hobby: newHobby}).toString()}`)
      .then(response =>
          response.json())
      .then(data => {
        correlatedHobbies.value = data[newHobby.replaceAll(" ", "_")].correlated_hobbies;
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

function clickChild(e, hobby) {
  e.target.classList.add("selected");
  e.target.style.pointerEvents = "none";
  addHobby(hobby);
}

function clickParent(e, hobby) {
  e.target.classList.add("selected");
  e.target.style.pointerEvents = "none";
  addHobby(hobby);
}
</script>

<template>
  <div id="selections-box">
    <span class="selection" v-for="selection in selections">{{selection}}</span>
  </div>
<div id="hobby-input-wrapper" class="center-screen">
  <input v-model="hobbyInput" id="hobby-input" name="hobby" type="text" />
  <button @click="clickAdd">Add</button>
</div>
  <div v-if="!loadingSuggestions" id="suggestion-box">
  <div class="parent-suggestion-box" v-for="hobbyParent in correlatedHobbies" :key="hobbyParent.name">
    <div class="parent-suggestion" @click="(e) => clickParent(e, hobbyParent.name)">{{ hobbyParent.name }}</div>
    <div class="child-suggestion-box">
    <div class="child-suggestion" v-for="hobbyChild in hobbyParent.correlated_hobbies" @click="(e) => clickChild(e, hobbyChild.name)"> {{ hobbyChild.name }}</div>
      </div>
  </div>
  </div>
  <div v-else>
    Loading suggestions...
  </div>
  <div class="center-screen">
    <button>Done</button>
  </div>
</template>

<style scoped>
#suggestion-box {
  display: grid;
  grid-template-columns: 1fr 1fr 1fr 1fr;
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
  min-height: 200px;
}

#hobby-input {
  width: 300px;
  margin-right: 6px;
}

.center-screen {
  display: flex;
  justify-content: center;
  align-items: center;
}

.parent-suggestion-box {
  text-align: center;
}

.parent-suggestion {
  border-radius: 6px;
  background-color: #313131;
  color: white;
  padding: 4px;
  text-align: center;
  cursor: pointer;
}

.child-suggestion {
  border-radius: 6px;
  background-color: #656565;
  color: white;
  padding: 4px;
  text-align: center;
  margin: 5px;
  cursor: pointer;
}

.selection {
  border-radius: 20px;
  background-color: #313131;
  color: white;
  padding: 2px;
  text-align: center;
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
  cursor: pointer;
}

.selected {
  background-color: lightgray;
}
</style>