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

async function submitHobbies() {
  const hobbyObject = {};
  if (lastAddedHobby.value) {
    hobbyObject[lastAddedHobby.value.replaceAll(" ", "_")] = MAX_HOBBY_VALUE;
  }

  correlatedHobbies.value.forEach((parent) => {
    if (parent.selected) {
      hobbyObject[parent.name.replaceAll(" ", "_")] = parent.selected ? MAX_HOBBY_VALUE : MID_HOBBY_VALUE;
    }
    parent.correlated_hobbies.forEach((child) => {
      let childValue = MIN_HOBBY_VALUE;
      if (parent.selected) {
        childValue = MID_HOBBY_VALUE;
      }
      if (child.selected) {
        childValue = MAX_HOBBY_VALUE;
      }

        hobbyObject[child.name.replaceAll(" ", "_")] = childValue;
    });
  });

  console.log(hobbyObject)
  // fetch("http://localhost:8080/user/hobbies", {
  //   method: "POST",
  //   headers: {
  //     "Content-Type": "application/json",
  //   },
  //   body: JSON.stringify(hobbyObject),
  // })
  //     .then((response) => {
  //       if (!response.ok) {
  //         throw new Error("Network response was not ok");
  //       }
  //       return response.json();
  //     })
  //     .then((data) => {
  //       console.log("Hobbies submitted successfully:", data);
  //       // Optionally, clear the selections and correlated hobbies
  //     })
  //     .catch((error) => {
  //       console.error("Error submitting hobbies:", error);
  //     });
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
  <div class="center-screen">
  <div id="selections-box">
    <span class="selection" v-for="selection in selections">{{selection}}</span>
  </div>
  </div>
<div id="hobby-input-wrapper" class="center-screen">
  <input v-model="hobbyInput" id="hobby-input" name="hobby" type="text" @keydown.enter="clickAdd" />
  <button @click="clickAdd">Add</button>
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
    <button @click="clickDone">Done</button>
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

.loading-box {
  min-height: 195px;
  text-align: center;
}

#selections-box {
  display: flex;
  flex-wrap: wrap-reverse;
  justify-content: center;
  align-content: flex-start;
  gap: 10px;
  max-width: 600px;
  min-height: 200px;
  margin: 20px auto;
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
  padding: 6px 12px;
  text-align: center;
  white-space: nowrap;
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