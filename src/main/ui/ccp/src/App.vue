<script setup>
import { RouterLink, RouterView } from 'vue-router'
</script>

<template>
  <header>
      <nav>
<!--        <RouterLink to="/">New user</RouterLink>-->
      </nav>
  </header>

  <RouterView />
</template>

<style>
html {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  background: #f5fdff;
}

.center-screen {
  display: flex;
  justify-content: center;
  align-items: center;
  margin: 1rem 0;
}
</style>
