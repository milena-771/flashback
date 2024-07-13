<script>
import { mapWritableState } from 'pinia';
import { RouterLink } from 'vue-router';
import useStore from "@/stores/store.js";

export default{
    data(){
        return{
            retrosAsOrga:[],
            retrosAsParticipant:[]
        }
    },
    computed:{
        ...mapWritableState(useStore,['asAdmin', 'path', 'asOrganizer', 'asParticipant', 'asNotRegistered'])
    },
    methods:{
        async initRetros(){
            const response = await this.$axios.get('/retrospectives/planning');
            this.retrosAsOrga = response.data.retroByOrga;
            this.retrosAsParticipant = response.data.retroByParticipant;                 
        },
        async cancelParticipation(id, name){
            try{
                const response = await this.$axios.delete(`/retrospectives/${id}/remove-participant`)
                this.$toast.success('toast-global', this.$i18n.t("toast.cancelParticipation.success", {retro:name}));
                this.initRetros();
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
        async deleteRetrospective(id,name){
            try{
                const response = await this.$axios.delete(`/retrospectives/${id}`);
                this.$toast.success('toast-global', this.$i18n.t("toast.retro.delete", {retro:name}));
                this.initRetros();
            } catch(error) {
                /**
                 * code 400 if
                 * user id doesn't match organiser's id, 
                 * id = null
                 */
                if(error.code === "ERR_BAD_REQUEST"){
                    window.scrollTo(0,0);
                    this.$toast.error('toast-global',  this.$i18n.t("toast.retro.errorDelete"));
                }
            }
              
        },
        seeDetailsAsOrganizer(retroId){
            this.asOrganizer = true;
            this.asAdmin = false;
            this.asNotRegistered = false;
            this.asParticipant = false;
            this.path = `/retrospectives/${retroId}/orga-details`
            this.$router.push(`/retrospectives/${retroId}/details`)
        },
        seeDetailsAsParticipant(retroId){
            this.asParticipant = true;
            this.asOrganizer = false;
            this.asAdmin = false;
            this.asNotRegistered = false;
            this.path = `/retrospectives/${retroId}/participant-details`
            this.$router.push(`/retrospectives/${retroId}/details`)
        }
    },
    beforeMount(){
        this.initRetros();
    }
}
</script>
<template>

        <h2>{{ $t(`titles.organization`) }}</h2>
        
        <!--Organizer cards-->
        <div v-if="retrosAsOrga.length" class="row row-cols-1 row-cols-sm-2 row-cols-md-4 row-cols-lg-5 g-3 mx-2">
            <div id="retros" v-for=" retro in retrosAsOrga" class="mb-4">
                <div class="card shadow-sm h-100">
                    <div class="text-center pt-2">
                        <img src="/logos/chair.png" alt="retrospective as organizer" class="card-img-top">
                    </div>
                    <div class="card-body text-center">
                        <h5 class="card-title">{{ retro.retrospectiveName }}</h5>  
                        <div class="card-text">
                            {{ $d(retro.startDate, 'short') }}<br>
                            {{ $d(retro.endDate, 'short') }}<br>
                            <span class="text-muted">({{ retro.moviesNumber > 1 ?  retro.moviesNumber + " films" : retro.moviesNumber + " film"}})</span>
                        </div>
                    </div>
                    <div class="card-footer text-end mx-1 p-0">               
                        <button class="btn btn-sm rounded-pill m-1" title="Retro details" @click="seeDetailsAsOrganizer(retro.id)"><i class="bi bi-three-dots"></i></button>
                        <RouterLink :to="{ name:'retrospective-update', params:{id:retro.id}}" title="Retrospective update">
                            <button class="btn btn-sm rounded-pill m-1" title="Update retro"><i class="bi bi-pencil-square"></i></button>
                        </RouterLink>
                        <button class="btn btn-sm rounded-pill m-1" title="Delete retro" @click="deleteRetrospective(retro.id,retro.retrospectiveName)"><i class="bi bi-trash3"></i></button>  
                    </div>
                </div>
            </div>
        </div>
        <div v-else class="my-2">{{ $t(`label.noOrganization`) }}</div>
        
        <!--Participant cards-->
        <h2 class="mt-4">{{ $t(`titles.participation`) }}</h2>
        <div v-if="retrosAsParticipant.length" class="row row-cols-1 row-cols-sm-2 row-cols-md-4 row-cols-lg-5 g-3 mx-2">
            <div id="retros" v-for=" retro in retrosAsParticipant" class="mb-4">
                <div class="card shadow-sm h-100">
                    <div class="text-center pt-2">
                        <img src="/logos/audience.png" alt="retrospective as participant" class="card-img-top">
                    </div>
                    <div class="card-body text-center">
                        <h5 class="card-title">{{ retro.retrospectiveName }}</h5>
                        <div class="card-text">
                            {{ $d(retro.startDate, 'short') }}<br>
                            {{ $d(retro.endDate, 'short') }}<br>
                            <span class="text-muted">({{ retro.moviesNumber > 1 ?  retro.moviesNumber + " films" : retro.moviesNumber + " film"}})</span>
                        </div>
                    </div>
                    <div class="card-footer text-end mx-1 p-0">                   
                        <button class="btn btn-sm rounded-pill m-1" title="Retro details" @click="seeDetailsAsParticipant(retro.id)"><i class="bi bi-three-dots"></i></button>
                        <button class="btn btn-sm rounded-pill m-1" title="Cancel participation" @click="cancelParticipation(retro.id, retro.retrospectiveName)"><i class="bi bi-slash-circle"></i></button>  
                    </div>
                </div>
            </div>
        </div>
        <div v-else>{{ $t(`label.noParticipation`) }}</div>

</template>
<style scoped>
.card-img-top{
    width:25%;
}
.btn{
    border:none;
    background-color: silver;
    color:white;
}

.btn:hover{
    background-color: blueviolet;
}

.card-footer{
    border-top:none;
    background-color: white;
}
</style>