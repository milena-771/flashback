<script setup>
import { RouterView } from 'vue-router';
import Toast from '../components/commons/Toast.vue';
</script>

<template>
 <RouterView name="header"/>
  <RouterView name="view"/>
  <Toast id="toast-global" />
</template>


