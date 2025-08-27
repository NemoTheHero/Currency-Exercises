<script setup lang="ts">
import { ref, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()
const userId = route.query.userId || 1 // fallback if not passed in URL

const user = ref(null)
const interests = ref([])
const matches = ref([])
const loading = ref(true)
const error = ref('')

const showModal = ref(false)
const selectedKeyword = ref(null)
const keywordUsers = ref([])

const interestCounts = ref<Record<number, number>>({})

const showSharedModal = ref(false)
const sharedWithUser = ref(null)
const sharedInterests = ref<UserInterest[]>([])

watch(() => route.query.userId, (newId) => {
  if (newId) {
    showModal.value = false
    selectedKeyword.value = null
    keywordUsers.value = []
    loadUserProfile(Number(newId))
  }
})

function goToHobbyForm() {
  if (user.value?.id) {
    router.push({
      name: 'HobbyForm',
      query: { userId: user.value.id.toString() }
    })
  }
}

onMounted(() => {
  loadUserProfile(Number(userId))
})

async function loadUserProfile(id: number) {
  loading.value = true
  error.value = ''
  try {
    const [userRes, interestRes, matchesRes] = await Promise.all([
      fetch(`http://localhost:8080/user/findById?userId=${id}`),
      fetch(`http://localhost:8080/user/interests?userId=${id}`),
      fetch(`http://localhost:8080/user/getMatches?userId=${id}`)
    ])

    if (!userRes.ok || !interestRes.ok || !matchesRes.ok) {
      throw new Error("Failed to fetch user or interests")
    }

    user.value = await userRes.json()
    interests.value = await interestRes.json()
    matches.value = (await matchesRes.json()).filter(m => m.userId !== id)

    for (const interest of interests.value) {
      try {
        const res = await fetch(`http://localhost:8080/user/getAllUsersScoresByKeywordId?keywordId=${interest.keywordId}`)
        if (!res.ok) throw new Error()

        const allUsers = await res.json()
        const filteredUsers = allUsers.filter(u => u.userId !== id)
        interestCounts.value[interest.keywordId] = filteredUsers.length
      } catch {
        interestCounts.value[interest.keywordId] = 0
      }
    }
  } catch (err) {
    console.error(err)
    error.value = "Could not load profile"
  } finally {
    loading.value = false
  }
}

async function openKeywordModal(keyword) {
  selectedKeyword.value = keyword
  showModal.value = true
  keywordUsers.value = []

  try {
    const res = await fetch(`http://localhost:8080/user/getAllUsersScoresByKeywordId?keywordId=${keyword.keywordId}`)
    if (!res.ok) throw new Error("Failed to fetch keyword users")
    keywordUsers.value = (await res.json()).filter(u => user.value && u.userId !== user.value.id)
  } catch (err) {
    console.error("Error loading keyword users", err)
  }
}

async function openSharedInterestsModal(matchUser) {
  sharedWithUser.value = matchUser
  showSharedModal.value = true
  sharedInterests.value = []

  try {
    const res = await fetch(`http://localhost:8080/user/getSharedInterest?userId=${user.value.id}&otherUserId=${matchUser.userId}`)
    if (!res.ok) throw new Error("Failed to fetch shared interests")

    sharedInterests.value = await res.json()
  } catch (err) {
    console.error("Error loading shared interests", err)
  }
}
</script>

<template>
  <div class="profile-container">
    <h2>User Profile</h2>

    <div v-if="user">
      <p><strong>Name:</strong> {{ user.userName }}</p>
    </div>

    <div v-if="matches.length">
      <h3>Matches</h3>
      <ul>
        <li
            v-for="match in matches"
            :key="match.userId"
            class="clickable"
            @click="openSharedInterestsModal(match)"
        >
          {{ match.name }} (Score: {{ match.score }})
        </li>
      </ul>
    </div>

    <div v-if="interests.length">
      <h3>Interests</h3>
      <ul>
        <li v-for="interest in interests.filter(i => i.score === 7)" :key="interest.keywordId" class="clickable" @click="openKeywordModal(interest)">
          {{ interest.name }} ({{ interestCounts[interest.keywordId] || 0 }})
        </li>
      </ul>
    </div>

    <div style="display: flex; justify-content: center; margin-top: 1rem;">
      <button class="add-hobby-btn" @click="goToHobbyForm">Add More Hobbies</button>
    </div>

    <div class="modal-overlay" v-if="showModal" @click.self="showModal = false">
      <div class="modal-content">
        <h3>Users who like "{{ selectedKeyword?.name }}"</h3>
        <ul v-if="!loading && keywordUsers.length">
          <li v-for="user in keywordUsers" :key="user.userId">
            <router-link
                :to="`/profile?userId=${user.userId}`"
                class="user-link"
                @click="showModal = false"
            >
              {{ user.name }} (Score: {{ user.score }})
            </router-link>
          </li>
        </ul>
        <p v-else>No users found.</p>
        <button @click="showModal = false">Close</button>
      </div>
    </div>

    <div class="modal-overlay" v-if="showSharedModal" @click.self="showSharedModal = false">
      <div class="modal-content">
        <h3>Shared Interests with {{ sharedWithUser?.name }}</h3>
        <ul v-if="sharedInterests.length">
          <li v-for="interest in sharedInterests" :key="interest.keywordId">
            {{ interest.name }} (Score: {{ interest.score }})
          </li>
        </ul>
        <p v-else>No shared interests found.</p>
        <button @click="showSharedModal = false">Close</button>
      </div>
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

.clickable {
  cursor: pointer;
  color: #3b82f6;
  text-decoration: underline;
}

.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.4);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 10;
}

.modal-content {
  background: white;
  padding: 1.5rem;
  border-radius: 8px;
  width: 90%;
  max-width: 400px;
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.2);

  max-height: 80vh;
  overflow-y: auto;
}


.add-hobby-btn {
  background-color: #3b82f6;
  color: white;
  border: none;

  padding: 10px 20px;
  font-size: 1rem;
  border-radius: 9999px;
  cursor: pointer;
  transition: background-color 0.2s ease-in-out;
}

.add-hobby-btn:hover {
  background-color: #2563eb;
}
</style>