<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()
const userId = route.query.userId || 1 // fallback if not passed in URL

const user = ref(null)
const interests = ref([])
const loading = ref(true)
const error = ref('')

onMounted(async () => {
  try {
    const [userRes, interestRes] = await Promise.all([
      fetch(`http://localhost:8080/user/findById?userId=${userId}`),
      fetch(`http://localhost:8080/user/interests?userId=${userId}`)
    ])

    if (!userRes.ok || !interestRes.ok) {
      throw new Error("Failed to fetch user or interests")
    }

    user.value = await userRes.json()
    interests.value = await interestRes.json()
  } catch (err) {
    console.error(err)
    error.value = "Could not load profile"
  } finally {
    loading.value = false
  }
})
</script>

<template>
  <div class="profile-container">
    <h2>User Profile</h2>

    <div v-if="user">
      <p><strong>Name:</strong> {{ user.userName }}</p>
    </div>

    <div v-if="interests.length">
      <h3>Interests</h3>
      <ul>
        <li v-for="keyword in interests" :key="keyword.id">
          {{ keyword.keyword }}
        </li>
      </ul>
    </div>

    <div v-if="loading">Loading...</div>
    <div v-if="error" class="error">{{ error }}</div>
  </div>
</template>

<style scoped>
.profile-container {
  max-width: 600px;
  margin: 2rem auto;
  padding: 1.5rem;
  font-family: system-ui, sans-serif;
  border: 1px solid #ddd;
  border-radius: 8px;
  background: #fafafa;
}

ul {
  list-style: none;
  padding: 0;
}

li {
  background: #f0f0f0;
  margin: 6px 0;
  padding: 8px 12px;
  border-radius: 6px;
}

.error {
  color: #b91c1c;
  margin-top: 10px;
}
</style>