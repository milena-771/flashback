<script>
import { mapState } from 'pinia';
import useStore from "@/stores/store.js";
import { useRoute } from 'vue-router';

export default {
    setup(){
        return{
            route:useRoute()
        }
    },
    data(){
        return{
            id:this.route.params.id,
            baseUrl: import.meta.env.VITE_IMG_BASE_URL,
            retrospective:{},
            movies:[],
            startDate:0,
            endDate:0
        }
    },
    computed:{
            ...mapState(useStore,['asAdmin', 'path', 'asOrganizer', 'asParticipant', 'asNotRegistered'])
    },
    methods:{
        async initRetrospectiveDetails(){
            try{
                const response = await this.$axios.get(this.path);
                this.retrospective = response.data.retroDetails;
                this.movies = response.data.movieDetails;
                this.startDate = Date.parse(response.data.retroDetails.startDate);
                this.endDate = Date.parse(response.data.retroDetails.endDate);
            } catch(error) {
                /**
                 * if retroId in path equals null
                 */
                if(error.code === "ERR_BAD_REQUEST"){
                    this.$router.push('/internal-error');
                }
            }
            
        },
        async deleteRetrospective(name){
            try{
                const response = await this.$axios.delete(`/retrospectives/${this.id}`);
                this.$toast.success('toast-global', this.$i18n.t("toast.retro.delete", {retro:name}));
                this.$router.push('/planning'); 
            } catch(error) {
                /**
                 * code 400 if
                 * user id doesn't match organiser's id, 
                 * retro id = null
                 */
                if(error.code === "ERR_BAD_REQUEST"){
                    window.scrollTo(0,0);
                    this.$toast.error('toast-global',  this.$i18n.t("toast.retro.errorDelete"));
                }
            }      
        },
        async cancelParticipation(name){
            try{
                const response = await this.$axios.delete(`/retrospectives/${this.id}/remove-participant`)
                this.$toast.success('toast-global', this.$i18n.t("toast.cancelParticipation.success", {retro:name}));
                this.$router.push('/planning');
            }catch(error){
                /**
                 * code 400 if
                 * user id is not a participant or is organizer 
                 * retro id = null
                 */
                 if(error.code === "ERR_BAD_REQUEST"){
                    window.scrollTo(0,0);
                    this.$toast.error('toast-global',  this.$i18n.t("toast.cancelParticipation.error"));
                }
            }
        },
        async participation(name){
            try{
                const response = await this.$axios.put(`/retrospectives/${this.id}/add-participant`);
                this.$toast.success('toast-global', this.$i18n.t("toast.participate.success", {retro:name}));
                this.$router.push('/planning');
            }catch(error){
                /**
                 * code 400 if
                 * user id is already participant or organizer 
                 * retro id = null
                 */
                 if(error.code === "ERR_BAD_REQUEST"){
                    window.scrollTo(0,0);
                    this.$toast.error('toast-global',  this.$i18n.t("toast.participate.error"));
                }
            }
            
        }
    },
    beforeMount(){
        this.initRetrospectiveDetails();       
    }
}
</script>
<template>
    <main class="container-xl">
        <h1 class="pt-3 mt-2 mb-3 text-center">{{ retrospective.retrospectiveName }}</h1>
        <div v-if="asNotRegistered" class="text-center">
            <button  class="btn btn-sm rounded-pill mb-4 flashbackButton" @click="participation(retrospective.retrospectiveName)"><i class="bi bi-ticket-perforated mx-1"></i> {{ $t(`button.participate`) }}</button>
        </div>
        <div v-if="asOrganizer" class="text-center">
            <RouterLink :to="{ name:'retrospective-update', params:{id:id}}" title="Retrospective update">
                <button  class="btn btn-sm rounded-pill mb-4 flashbackButton"><i class="bi bi-pencil-square"></i> {{ $t(`button.updateRetro`) }}</button>
            </RouterLink>
            <button  class="btn btn-sm rounded-pill mb-4 flashbackOutline mx-2" @click="deleteRetrospective(retrospective.retrospectiveName)"><i class="bi bi-trash3"></i> {{ $t(`button.deleteRetro`) }}</button>
        </div>
        <div v-if="asParticipant" class="text-center">
            <button  class="btn btn-sm rounded-pill mb-4 flashbackButton" @click="cancelParticipation(retrospective.retrospectiveName)"><i class="bi bi-slash-circle"></i> {{ $t(`button.cancelParticipation`) }}</button>
        </div>
        <div v-if="asAdmin || asOrganizer">
            <span class="d-lg-none my-2 text-muted"><i class="bi bi-people-fill"></i> {{ retrospective.participantsNumber > 1 ? retrospective.participantsNumber + " participants" : retrospective.participantsNumber + " participant"}}</span>
        </div>
        <div v-if="asAdmin || asOrganizer" class="my-3 text-end d-lg-block d-none">
            <span class="text-muted"><i class="bi bi-people-fill"></i> {{ retrospective.participantsNumber > 1 ? retrospective.participantsNumber + " participants" : retrospective.participantsNumber + " participant"}}</span>
        </div>
        <div v-if="asAdmin" class="my-2">
            <label for="organizer" class="fw-bold">{{ $t(`label.organizer`) }}</label>
            <div id="organizer">{{ retrospective.userEmail}}</div>
        </div>
        <div class="row mb-2">
            <div class="col-md-2 my-2">
                <label for="startDate" class="fw-bold">{{ $t(`label.startDate`) }}</label>
                <div id="startDate">{{ asAdmin ? retrospective.startDate : $d(startDate, 'short') }}</div>
            </div>
            <div class="col-md-2 my-2">
                <label for="endDate" class="fw-bold">{{ $t(`label.endDate`) }}</label>
                <div id="endDate">{{ asAdmin ? retrospective.startDate : $d(endDate, 'short')  }}</div>
            </div>
        </div>
        <div class="my-2">
            <label for="device" class="fw-bold">{{ $t(`label.devices`) }}</label>
            <div id="device">{{ retrospective.deviceName }}</div>
        </div>
        <div class="my-2">
            <label for="description" class="fw-bold">{{ $t(`label.description`) }}</label>
            <div id="description">{{ retrospective.description }}</div>
        </div>

        <!--Movies list-->
        <label for="movies" class="fw-bold my-2">{{ $t(`label.movies`) }}</label>
        <div class="row row-cols-1 row-cols-sm-2 row-cols-md-3 row-cols-lg-4 g-3">
            <div id="movies" v-for=" movie in movies" class="mb-4">
                <div class="card shadow-sm">
                    <div class="row g-0">
                        <div class="col-4 poster">
                            <img :src="baseUrl + movie.poster" :alt="movie.title" class="img-fluid rounded-start">
                        </div>
                        <div class="col-8">
                            <div class="card-body position-absolute top-0 start-25 pt-1 pb-0">
                                <h5 class="card-title mb-0">{{ movie.title }}</h5>
                                <div class="card-text mt-0"><span class="text-muted">{{ movie.releaseYear }}</span></div>
                                <div class="card-text"> 
                                    <span v-for="(director,index) in movie.directors" :key="index"> 
                                        {{ director.firstname }} {{ director.lastname }} {{ movie.directors.length-1 > index ? ' & ' : ' ' }}
                                    </span>                    
                                </div>
                            </div>
                            <div v-if="!asAdmin" class="position-absolute bottom-0 end-0 p-0">
                                <RouterLink :to="{ name:'details', params:{id:movie.id}}" title="Movie details">                
                                    <button class="btn btn-small smallBtn rounded-circle px-1 py-0 m-1"><i class="bi bi-three-dots"></i></button>
                                </RouterLink> 
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </main>
</template>
<style scoped>
.card-title{
    font-size: medium;
}
.card-text{
    font-size:small;
}
.poster{
    max-width:5rem;
}
</style>