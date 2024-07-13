<script>
import { mapWritableState } from 'pinia';
import useStore from "@/stores/store.js";

export default {
    data(){
        return{
            baseUrl: import.meta.env.VITE_IMG_BASE_URL,
            retrospectives:[]
        }
    },
    computed:{
        ...mapWritableState(useStore,['asAdmin', 'path', 'asOrganizer', 'asParticipant', 'asNotRegistered'])
    },
    methods:{
        async initRetrospectives(){
            const response = await this.$axios.get('/admin/retrospectives');
            this.retrospectives= response.data;
        },
        async remove(retroId, name){
            try{
                const response = await this.$axios.delete(`/admin/${retroId}/remove-retro`);
                this.$toast.success('toast-global', this.$i18n.t("toast.retro.delete", {retro: name}));
                await this.initRetrospectives();
                window.scrollTo(0,0);  
            }catch(error) {
                /**
                 * code 400 if
                 * retroId = null
                 */
                if(error.code === "ERR_BAD_REQUEST"){
                    window.scrollTo(0,0);
                    this.$toast.error('toast-global',  this.$i18n.t("toast.retro.errorDelete"));
                }
            }
              
        },
        seeDetails(retroId){
            this.asAdmin = true;
            this.asNotRegistered = false;
            this.asOrganizer = false;
            this.asParticipant = false;
            this.path = `/admin/${retroId}/retro-details`
            this.$router.push(`/admin/retrospectives/${retroId}/details`)
        }
    },
    beforeMount(){
        this.initRetrospectives();
    }
}
</script>
<template>
    <main class="container-xl">
        <h1 class="pt-3 mt-2 mb-3">{{ $t(`titles.retroAdmin`) }}</h1>
        <div v-if="retrospectives.length" class="table-responsive">
            <table class="table table-hover align-middle">
                <thead>
                    <tr>
                        <th>{{ $t(`label.retroName`) }}</th>
                        <th class="d-none d-sm-table-cell">{{ $t(`label.startDate`) }}</th>
                        <th>{{ $t(`label.endDate`) }}</th>
                        <th class="d-none d-sm-table-cell">{{ $t(`label.organizer`) }}</th>
                        <th class="text-center">{{ $t(`label.details`) }}<br>{{ $t(`label.delete`) }}</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="retro in retrospectives">
                        <td>{{ retro.retrospectiveName }}</td>
                        <td class="d-none d-sm-table-cell">{{ retro.startDate }}</td>
                        <td>{{ retro.endDate }}</td>
                        <td class="d-none d-sm-table-cell">{{ retro.organizerEmail }}</td>
                        <td class="text-center">
                        <div class="my-2">
                            <button class="btn flashbackButton rounded-pill py-0 px-2" @click="seeDetails(retro.id)"><i class="bi bi-three-dots"></i></button>
                        </div>
                        <div class="my-2">
                            <button class="btn blackBtnOutline rounded-pill py-0 px-2" @click="remove(retro.id, retro.retrospectiveName)"><i class="bi bi-trash3 my-1"></i></button>
                        </div>
                    </td>
                    </tr>
                </tbody>
            </table>
        </div>
        <div v-else>{{ $t(`label.noRetroAdmin`) }}</div>
    </main>
</template>