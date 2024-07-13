<script>
import { mapWritableState } from 'pinia';
import useStore from "@/stores/store.js";

export default{
    data(){
        return{
            retrosToCome:[]
        }
    },
    computed:{
        ...mapWritableState(useStore,['asAdmin', 'path', 'asOrganizer', 'asParticipant', 'asNotRegistered'])
    },
    methods:{
        async initRetros(){
            const response = await this.$axios.get('/retrospectives/to-come');
            this.retrosToCome = response.data;
        },
        seeDetails(id){
            this.asNotRegistered = true;
            this.asOrganizer = false;
            this.asAdmin = false;
            this.asParticipant = false;
            this.path = `/retrospectives/${id}/retro-details`;
            this.$router.push(`/retrospectives/${id}/details`)
        }
    },
    beforeMount(){
        this.initRetros();
    }
}
</script>
<template>
    <main class="container-xl">
        <h1 class="pt-3 mt-2 mb-3">{{ $t(`titles.retroToCome`) }}</h1>

        <div v-if="retrosToCome.length" class="row row-cols-1 row-cols-sm-2 row-cols-md-4 row-cols-lg-5 g-3 mx-2">
            <div id="retros" v-for=" retro in retrosToCome" class="mb-4">
                <div class="card shadow-sm h-100">
                    <div class="text-center pt-2">
                        <img src="/logos/ticket.png" alt="retrospective to come" class="card-img-top">
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
                        <button class="btn btn-sm rounded-pill m-1" title="Retro details" @click="seeDetails(retro.id)"><i class="bi bi-three-dots"></i></button>
                    </div>
                </div>
            </div>
        </div>
        <div v-else>{{ $t(`label.noRetroToCome`) }}</div>
    </main>
</template>
<style scoped>
.card-img-top{
    width:30%;
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