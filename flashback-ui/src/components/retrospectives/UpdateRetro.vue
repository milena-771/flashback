<script>
import { useRoute } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';
import { required, maxLength, sameAs, helpers, minValue } from '@vuelidate/validators';
import Datalist from '../commons/Datalist.vue';

const namePattern = helpers.regex(/^[a-zA-Z0-9-éàèêôûîç'’?!.,:() ]*$/);
const descriptionPattern = helpers.regex(/^[a-zA-Z0-9-éàèâêôûùîïç'’"",.:()?!$€% ]*$/);

export default {
    setup(){
        return{
            route: useRoute(),
            validator: useVuelidate({})
        }
    },
    components:{
        Datalist
    },
    data(){
        return{
            retroId:this.route.params.id,
            baseUrl: import.meta.env.VITE_IMG_BASE_URL,
            favoriteMovies:[],
            deviceLabels:[],
            updateDevice:{},
            updateMovies:[],
            inputs:{
                id:null,
                retrospectiveName:null,
                deviceId:0,
                startDate:null,
                endDate:null,
                description:null,
                movieId:[]
            }
        }
    },
    validations(){
        return {
            updateDevice:{required},
            inputs:{
                retrospectiveName:{required, maxLength:maxLength(150), namePattern},
                description:{required, maxLength: maxLength(1000), descriptionPattern},
                deviceId:{required, minValue:minValue(1)},
                movieId:{required},
                startDate:{required},
                endDate:{required, minValue:(value) => {
                    return value === null || new Date(value) > new Date(this.inputs.startDate)
                }}
            }
        }
    },
    computed:{
        setMinDate(){
            const newDate = new Date();
            let today = newDate.toISOString().substring(0, 10);
            return today;
        }
    },
    watch:{
        "inputs.retrospectiveName":{
            handler(){
                const element = document.getElementById('retrospectiveName');
                this.$tooltip.remove(element);
            },
            deep:true
        }
    },
    methods:{
        async initRetroUpdate(){
            const response = await this.$axios.get(`/retrospectives/${this.retroId}/for-update`);
            this.favoriteMovies = response.data.favorites;
            this.deviceLabels = response.data.devices;
            const retrospective = response.data.retroDetails.retroDetails;
            this.inputs.id = this.retroId;
            this.inputs.retrospectiveName = retrospective.retrospectiveName;
            this.inputs.startDate = retrospective.startDate;
            this.inputs.endDate = retrospective.endDate;
            this.inputs.description = retrospective.description;
            this.updateDevice = retrospective.device;
            this.updateMovies = response.data.retroDetails.movieDetails;
        },
        addFavorites(){
            this.updateMovies.forEach(movie =>{
                this.favoriteMovies.forEach(favMovie => {
                    if(movie.id === favMovie.id){
                        let index = this.favoriteMovies.indexOf(favMovie);
                        this.favoriteMovies.splice(index,1)
                    }
                })
            })
            this.favoriteMovies.forEach(movie => {
                this.updateMovies.push(movie)
            })
            this.favoriteMovies = [];
        },
        removeFromSelection(index){
            this.updateMovies.splice(index, 1);
        },
        async update(){
            this.inputs.movieId = [];
            this.updateMovies.forEach(movie => {
                this.inputs.movieId.push(movie.id)
            });
            if(this.updateDevice){
                this.inputs.deviceId = this.updateDevice.id;
            }
            const valid = await this.validator.$validate();
            if(valid){
                try{
                    const response = await this.$axios.put(`/retrospectives/${this.retroId}`, this.inputs);
                    this.$toast.success('toast-global', this.$i18n.t("toast.retro.update"));
                    this.initRetroUpdate();
                    window.scrollTo(0,0);
                }catch(error){
                    if(error.response.data.globalErrors){
                        if(error.response.data.globalErrors.length){
                            const globalErrors = error.response.data.globalErrors;
                            for(let customError of globalErrors){
                                if(customError.code === "UniqueRetroNameUpdate"){
                                    let message = this.$i18n.t("tooltip.messages.uniqueRetroName");
                                    this.$tooltip.display("retrospectiveName", message);
                                }         
                            }
                        }   
                    }else{
                        /**
                         * in case of code 415 this.inputs = null
                         * code 400, no inputs has been sent
                         */
                        window.scrollTo(0,0);
                        this.$toast.error('toast-global',  this.$i18n.t("toast.retro.errorUpdate"));
                    }                    
                }                
            }else{
                this.$toast.error('toast-global', this.$i18n.t("toast.retro.invalid"));
                window.scrollTo(0,0);
            }        
        }
    },
    beforeMount(){
        this.initRetroUpdate();
    }
}
</script>
<template>
    <main class="container-xl">
        <h1 class="pt-3 mt-2 mb-3">{{ $t(`titles.retroUpdate`) }}</h1>
        <form novalidate @submit.prevent="">
            <div class="row mb-2">
                <div class="col-md-6 mb-3">
                    <label for="retrospectiveName" class="form-label required">{{ $t(`label.retroName`) }}</label>
                    <input v-model.trim="inputs.retrospectiveName" :class="{'is-invalid': validator.inputs.retrospectiveName.$error}" id="retrospectiveName" name="retrospectiveName" type="text" maxlength="150" class="form-control">
                    <div v-if="validator.inputs.retrospectiveName.namePattern.$invalid" class="form-text text-danger">{{ $t(`form.helpText.pattern`) }}</div>
                    <div v-else class="form-text">{{ $t(`form.helpText.retro.name`) }}</div>
                </div>
                <div class="col-md-6 mb-3">
                    <label for="deviceId" class="form-label required">{{ $t(`label.devices`) }}</label>
                    <Datalist id="deviceId" :items="deviceLabels" v-model:one-value="updateDevice"  attributes="deviceName" :invalid="validator.updateDevice.required.$invalid"/>
                    <div class="form-text">{{ $t(`form.helpText.retro.device`) }}</div>
                </div>
            </div>
            <div class="row mb-2">
                <div class="col-md-6 mb-3">
                    <label for="startDate" class="form-label required">{{ $t(`label.startDate`) }}</label>
                    <input v-model="inputs.startDate" :class="{'is-invalid': validator.inputs.startDate.$error}" id="startDate" name="startDate" type="date" class="form-control" :min="setMinDate">
                </div>
                <div class="col-md-6 mb-3">
                    <label for="endDate" class="form-label required">{{ $t(`label.endDate`) }}</label>
                    <input v-model="inputs.endDate" :class="{'is-invalid': validator.inputs.endDate.$error}" id="endDate" name="endDate" type="date" class="form-control" :min="setMinDate">
                    <div v-if="validator.inputs.endDate.minValue.$invalid" class="form-text text-danger">{{ $t(`form.helpText.retro.endDate`) }}</div>                  
                </div>
            </div>
            <div class="col-12 mb-2">
                <label for="description" class="form-label required">{{ $t(`label.description`) }}</label>
                <textarea v-model.trim="inputs.description" :class="{'is-invalid': validator.inputs.description.$error}" id="description" name="description" maxlength="1000" rows="4" class="form-control"></textarea>
                <div v-if="validator.inputs.description.descriptionPattern.$invalid" class="form-text text-danger">{{ $t(`form.helpText.pattern`) }}</div>
                <div v-else class="form-text">{{ $t(`form.helpText.summary.default`) }}</div>
            </div>
            <label for="movies" class="form-label required mt-2">{{ $t(`label.movies`) }}</label>
            <div class="row row-cols-1 row-cols-sm-2 row-cols-md-3 row-cols-lg-4 g-3">
                <div v-if="updateMovies.length" id="movies" v-for=" (movie,index) in updateMovies" :key="index" class="mb-4">
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
                                <div class="position-absolute top-0 end-0 p-0">                  
                                    <button class="btn btn-lg smallBtn rounded-circle px-1 py-0" @click="removeFromSelection(index)"><i class="bi bi-x"></i></button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div v-else>
                    <span>{{ $t(`label.noMovieSelected`) }}</span>
                </div>
                <div class="d-flex align-items-center">
                    <button v-if="favoriteMovies.length" class="btn btn-sm rounded-pill mb-4 flashbackButton" @click="addFavorites"><i class="bi bi-plus-circle"></i> Ajouter mes favoris</button>
                    <RouterLink v-else :to="{ name: 'search' }" class="mx-auto">
                        <button class="btn btn-sm rounded-pill mb-4 flashbackButton" ><i class="bi bi-plus-circle"></i> Ajouter d'autres films</button>
                    </RouterLink>
                </div>                   
            </div>
            <div class="d-grid d-md-flex justify-content-md-end">
                <button type="submit" class="btn blackButton mb-4" @click="update" :disabled="!updateMovies.length">{{ $t(`button.organize`) }}</button>
            </div>    
        </form>  
    
    </main>
</template>
<style scoped>
.card-title{
    font-size: medium;
}
.card-text{
    font-size:small;
}
.smallBtn{
    color:black;
}

.smallBtn:hover{
    background-color: silver;
}
.poster{
    max-width:5rem;
}
/*active state for custom button*/
.flashbackButton:active{
    color:blueviolet;
}
.blackButton:active{
    border:1px solid black;
    background-color: white;
    color:black;
}
</style>