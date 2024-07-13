import { createApp } from 'vue';
import App from './App.vue';
import router from './router';
import { createPinia } from 'pinia';
import axios from './plugins/axios';
import toast from './plugins/toast';
import tooltip from './plugins/tooltip';
import i18n from './plugins/i18n';
import './assets/styles.css';
import "@vueform/multiselect/themes/default.css";

const app = createApp(App)
const pinia = createPinia()
app.use(pinia)
app.use(router)

//plugins
app.use(axios);
app.use(toast);
app.use(tooltip);

app.provide('$axios', axios)

app.use(i18n)

app.mount('#app')
