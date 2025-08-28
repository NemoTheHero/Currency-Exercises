<script setup lang="ts">
import {ref} from "vue";

const users = ref([])
const userName = ref("")

function getUsers() {
  fetch('http://localhost:8080/user/getAllUsers')
    .then(response => response.json())
    .then(data => {
      users.value = data;
    })
    .catch(error => {
      console.error('Error fetching users:', error);
    });
}

function clickAdd() {
  if (!userName.value || userName.value.trim() === "") {
    return;
  }
  fetch(`http://localhost:8080/user/createUser?name=${userName.value}`, {
    method: 'POST',
  })
    .then(response => {
      if (!response.ok) {
        throw new Error('Network response was not ok');
      }
      userName.value = ""; // Clear the input field
      getUsers();
    })
    .catch(error => {
      console.error('Error adding user:', error);
    });
}

getUsers();
</script>
<template>
<div>
  <div id="users">Users</div>
  <div id="new-user-form" style="margin-bottom: 2rem">
    <div>Add new user</div>
    <div id="new-user-input">
      <label>Name:</label>
      <input type="text" name="name" v-model="userName" @keydown.enter="clickAdd" />
      <button @click="clickAdd">Add User</button>
    </div>
  </div>
    <div v-if="users.length" id="existing-users"style="margin-bottom: 4rem" >
      <div v-for="user in users" :key="user.id" class="existing-user">
        <div>{{ user.userName }}</div>
        <RouterLink :to="`/profile?userId=${user.id}`">View Profile</RouterLink>
        <RouterLink :to="`/hobby-form?userId=${user.id}`">Add Interests</RouterLink>
      </div>
    </div>

</div>
</template>

<style scoped>
#users {
  font-size: 1.75rem;
  font-weight: 600;
  margin-top: 1rem;
  text-align: center;
}

button, a {
  background-color: #3b82f6;
  color: white;
  border: none;
  padding: 8px 16px;
  border-radius: 20px;
  cursor: pointer;
  transition: background-color 0.2s;
}

button:hover, a:hover {
  background-color: #2563eb;
}

a {
  text-decoration: none;
  display: inline-block;
  margin-top: 0.5rem;
  margin-bottom: 0.5rem;
  margin-right: 0.5rem
}

input {
    width: 280px;
    padding: 8px 12px;
    border-radius: 20px;
    border: 1px solid #ccc;
    font-size: 1rem;
}

#existing-users {
  padding: 1rem;
  border: 1px solid #ddd;
  border-radius: 8px;
  background: #fafafa;
  width: 330px;
  margin: 1rem auto;
  text-align: center;
}

#new-user-input {
  display: flex;
  gap: 0.5rem;
  align-items: center;
  margin-top: 0.5rem;
  justify-content: center;
}

#new-user-form {
  margin-top: 2rem;
  text-align: center;
}

.existing-user:not(:last-child) {
  margin-bottom: 1rem;
}
</style>