<script setup>
import DefaultLayout from './layout/DefaultLayout.vue';
import { onMounted } from 'vue';
import i18n from './plugins/i18n';

onMounted(() => {
    document.title =  i18n.global.t('appTitle');
    document.getElementById('html-tag').setAttribute('lang', i18n.global.locale);
});
</script>

<template>
  <DefaultLayout />
</template>

