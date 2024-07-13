<script>
import CalendarCards from './CalendarCards.vue';
import Calendar from './Calendar.vue';
export default {
    components:{
        CalendarCards,
        Calendar
    },
    data(){
        return{
            cardsView:false,
            calendarView:true
        }
    },
    methods:{
        showCardsViews(){
            this.cardsView = true;
            this.calendarView = false;
        },
        showCalendarViews(){
            this.cardsView = false;
            this.calendarView = true;
        }
    }
}
</script>
<template>
    <main class="container-xl">
        <div class=" pt-3 mt-2 mb-3 btn-group">
            <input type="radio" class="btn-check" name="btn-views" id="calendarView" autocomplete="off" checked @click="showCalendarViews()">
            <label class="btn btn-outline-dark" for="calendarView"><i class="bi bi-calendar-week"></i></label>
            <input type="radio" class="btn-check" name="btn-views" id="cardsView" autocomplete="off" @click="showCardsViews()">
            <label class="btn btn-outline-dark" for="cardsView"><i class="bi bi-grid"></i></label>
        </div>
        <CalendarCards v-if="cardsView"/>
        <Calendar v-if="calendarView"/>
    </main>
</template>
<style scoped>
</style>