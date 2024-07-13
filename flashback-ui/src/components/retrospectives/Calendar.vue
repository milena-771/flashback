<script>
import FullCalendar from '@fullcalendar/vue3';
import dayGridPlugin from '@fullcalendar/daygrid';
import { mapWritableState } from 'pinia';
import useStore from "@/stores/store.js";

export default {
    components: {
        FullCalendar
    },
    data(){
        return{
            retrosAsOrga:[],
            retrosAsParticipant:[],
            calendarOptions: {
                plugins: [ dayGridPlugin ],
                initialView: 'dayGridMonth',
                locale:'fr',
                firstDay:1,
                navLinks:false, //default,
                headerToolbar:{
                    start: '',
                    center: 'title',
                    end: 'today prev,next'
                },
                events: [],
                eventClick:this.displayRetro,
                displayEventTime:false
            }
        }
    },
    computed:{
        ...mapWritableState(useStore,['path', 'asOrganizer', 'asParticipant', 'asAdmin', 'asNotRegistered'])
    },
    methods:{
        async initRetros(){
            const response = await this.$axios.get('/retrospectives/planning');
            this.retrosAsOrga = response.data.retroByOrga;
            this.retrosAsParticipant = response.data.retroByParticipant; 
            this.retrosAsOrga.forEach(retro => {
                this.calendarOptions.events.push(
                    Object.create({
                        id: retro.id,
                        title: retro.retrospectiveName,
                        backgroundColor:'blueviolet',
                        borderColor:'blueviolet',
                        start: retro.startDate,
                        end: Date.parse(retro.endDate)
                    })
                )
            })
            this.retrosAsParticipant.forEach(retro => {
                this.calendarOptions.events.push(
                    Object.create({
                        id: retro.id,
                        title: retro.retrospectiveName,
                        backgroundColor:'mediumseagreen',
                        borderColor:'mediumseagreen',
                        start: retro.startDate,
                        end: Date.parse(retro.endDate)
                    })
                )
            })              
        },
        displayRetro(arg){
            if(arg.event.backgroundColor === 'blueviolet'){
                this.asOrganizer = true;
                this.asAdmin = false;
                this.asNotRegistered = false;
                this.asParticipant = false;
                this.path = `/retrospectives/${arg.event.id}/orga-details`
                this.$router.push(`/retrospectives/${arg.event.id}/details`)
            }
            if(arg.event.backgroundColor === 'mediumseagreen'){
                this.asParticipant = true;
                this.asOrganizer = false;
                this.asAdmin = false;
                this.asNotRegistered = false;
                this.path = `/retrospectives/${arg.event.id}/participant-details`
                this.$router.push(`/retrospectives/${arg.event.id}/details`)
            }
        }
    },
    beforeMount(){
            this.initRetros();
        }
}
</script>
<template>
    <FullCalendar ref="fullCalendar" :options="calendarOptions">
        <template v-slot:navLinks='arg'>
            <span class="fc-nav-links">{{ arg.navLinks }}</span>
        </template>
    </FullCalendar>
</template>
<style scoped>
.fc-direction-ltr {
    direction: ltr;
    text-align: left;
    padding-bottom: 10px;
}

.fc {
    text-transform: capitalize;
}

.fc :deep(.fc-col-header-cell-cushion){
    color:black;
    text-decoration:none;
}

.fc :deep(.fc-daygrid-day-number){
    color:black;
    text-decoration:none;
}

</style>