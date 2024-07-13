import { defineStore } from 'pinia';
import { ref } from 'vue';

export default defineStore('flashbackStore', () => {

    const favorites = ref([])
    const favoriteId = ref([])
    const asAdmin = ref(false)
    const asOrganizer = ref(false)
    const asParticipant = ref(false)
    const asNotRegistered = ref(false)
    const path = ref("")
    const timerID = ref(0)

    return { 
        favorites,
        favoriteId, 
        asAdmin, 
        path,
        asOrganizer,
        asParticipant,
        asNotRegistered,
        timerID
    }
})