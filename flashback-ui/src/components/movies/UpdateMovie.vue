<script>
import { useVuelidate } from '@vuelidate/core';
import { required, maxLength, minValue, between, url, integer, helpers} from '@vuelidate/validators';
import { useRoute } from 'vue-router';
import Datalist from '../commons/Datalist.vue';

const titlePattern = helpers.regex(/^[a-zA-Z0-9-éàèêôûîç,.'’ ]*$/);
const summaryPattern = helpers.regex(/^[a-zA-Z0-9-éàèâêôûùîïç'’"",.:()?!$€% ]*$/);
const trailerPattern = helpers.regex(/^[a-zA-Z0-9-&=?/:_.]*$/);

export default{
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
            id:this.route.params.id,
            baseUrl: import.meta.env.VITE_IMG_BASE_URL,
            genres : [],
            updateGenre: {},
            directors : [],
            updateDir: [],
            actualPoster:null,
            inputs: {
                isan:null,
                title:null,
                releaseYear:null,
                genreId:0,
                directorId:[],
                poster:null,
                trailer:"",
                summary:null
            }
        }
    },
    validations(){
        return {
            updateDir:{required},
            updateGenre:{required},
            inputs:{
                title:{required, maxLength: maxLength(100), titlePattern},
                releaseYear:{required, between: between(1895, 2025), integer},
                poster:{ 
                    maxValue: (file) => {
                        return file === null || file.size < 200000
                    },
                    maxLength: maxLength(300)
                },
                trailer:{url, maxLength: maxLength(300), trailerPattern},
                summary:{required, maxLength: maxLength(1000), summaryPattern}
            }
        }
    },
    watch:{
        "inputs.trailer":{
            handler(){
                const element = document.getElementById('trailer');
                this.$tooltip.remove(element);                  
            },
            deep:true
        }
    },
    methods : {
        async initSelectLists(){
            const resp = await this.$axios.get('/movies/labels');
            this.genres = resp.data.allGenres;
            this.directors = resp.data.allDirectors;
        },
        async initInputs() {
            const resp = await this.$axios.get(`/movies/${this.id}/for-update`);
            const movie = resp.data.movieDetails;
            this.updateDir = resp.data.directorDetails;
            this.inputs.isan = movie.isan;
            this.inputs.title = movie.title;
            this.inputs.releaseYear = movie.releaseYear;
            this.updateGenre = movie.genre;
            this.actualPoster = movie.poster;
            if(movie.trailer != null){
                this.inputs.trailer = movie.trailer;
            }
            this.inputs.summary = movie.summary;
        },
        async update() {
            this.inputs.directorId = [];
            this.updateDir.forEach(director => {
                this.inputs.directorId.push(director.id)
            })
            if(this.updateGenre){
                this.inputs.genreId = this.updateGenre.id;
            }
            const valid = await this.validator.$validate();
            if(valid){
                const formData = new FormData();
                formData.append('title', this.inputs.title);
                formData.append('releaseYear', this.inputs.releaseYear);
                formData.append('genreId', this.inputs.genreId);
                formData.append('directorId', this.inputs.directorId);
                if(this.inputs.poster != null){
                    formData.append('poster', this.inputs.poster);
                }
                formData.append('trailer', this.inputs.trailer);
                formData.append('summary', this.inputs.summary);
                try {
                    //if id = null, no exception thrown from the back-end
                    const response = await this.$axios.put(`/movies/${this.id}`, formData)
                    if(response.data.poster != null){
                        this.actualPoster = response.data.poster;
                    }                   
                    this.$toast.success('toast-global', this.$i18n.t("toast.movie.update"));
                    this.initInputs();
                    window.scrollTo(0,0);
                }catch (error){
                    if(error.code === "ERR_BAD_REQUEST"){
                        /**
                         * code 400, 
                         * formData = null
                         */
                        window.scrollTo(0,0);
                        this.$toast.error('toast-global',  this.$i18n.t("toast.movie.errorCreate"));
                        if(error.response.data.globalErrors.length){
                            const globalErrors = error.response.data.globalErrors;
                            for(let customError of globalErrors){
                                if(customError.code === "UniqueTrailerUpdate"){
                                    let message = this.$i18n.t("tooltip.messages.uniqueTrailer");
                                    this.$tooltip.display("trailer", message);
                                }
                            }
                        }

                    }
                }
            }else{
                window.scrollTo(0,0);
                this.$toast.error('toast-global',  this.$i18n.t("toast.movie.invalid"))
            }
        },
        handleFileUpload(event) {
            this.inputs.poster = event.target.files[0]
        }
    },
    beforeMount(){
        this.initSelectLists();
        this.initInputs();
    }
}
</script>
<template>
    <main class="container-xl">

        <h1 class="pt-3 mt-2 mb-3">{{ $t(`titles.updateMovie`) }}</h1>

        <form novalidate @submit.prevent="update">
            <div class="row mb-2">
                <!--First column movie poster-->
                <div class="col-md-4 mb-2 text-center">
                    <img :src="baseUrl + actualPoster" class="rounded img-fluid update-img"/>
                </div>
                <!--Second column ISAN, title and directors-->
                <div class="col-md-4 my-4">
                    <div class="mb-2">
                        <label for="isan" class="form-label">{{ $t(`label.isan`) }}</label>
                        <input v-model.trim="inputs.isan" id="isan" name="isan" type="text" maxlength="33" class="form-control" disabled>
                        <div class="form-text">{{ $t(`form.helpText.isan.recorded`) }}</div>
                    </div>
                    <div class="mb-3">
                        <label for="title" class="form-label required">{{ $t(`label.title`) }}</label>
                        <input v-model.trim="inputs.title" :class="{'is-invalid': validator.inputs.title.$error}" id="title" name="title" 
                        type="text" maxlength="100" class="form-control">
                        <div v-if="validator.inputs.title.titlePattern.$invalid" class="form-text text-danger">{{ $t(`form.helpText.pattern`) }}</div>
                        <div v-else class="form-text">{{ $t(`form.helpText.title.default`) }}</div>
                    </div> 
                    <div class="mb-2">
                        <label for="directorId" class="form-label required">{{ $t(`label.directors`) }}</label>
                        <Datalist id="directorId" v-model:valueDir="updateDir" :invalid="validator.updateDir.required.$invalid" :multi="true" :items="directors"/>
                        <div v-if="validator.updateDir.required.$invalid" class="form-text text-danger">{{ $t(`form.helpText.directors`) }}</div>
                    </div>   
                </div>
                <!--Third column releaseYear, genre and trailer-->
                <div class="col-md-4 align-self-center pb-2 pt-2">
                    <div class="mb-3">
                        <label for="releaseYear" class="form-label required">{{ $t(`label.releaseYear`) }}</label>
                        <input v-model.trim="inputs.releaseYear" :class="{'is-invalid': validator.inputs.releaseYear.$error}"  id="releaseYear" name="releaseYear" type="number" class="form-control">
                        <div v-if="validator.inputs.releaseYear.between.$invalid" class="form-text text-danger">{{ $t(`form.helpText.releaseYear.between`) }}</div>
                        <div v-else-if="validator.inputs.releaseYear.integer.$invalid" class="form-text text-danger">{{ $t(`form.helpText.releaseYear.integer`) }}</div>
                        <div v-else class="form-text">{{ $t(`form.helpText.releaseYear.between`) }}</div>
                    </div>
                    <div class="mb-4">
                        <label for="genreId" class="form-label required">{{ $t(`label.genre`) }}</label>
                        <Datalist id="genreId" v-model:one-value="updateGenre" :invalid="validator.updateGenre.required.$invalid" :items="genres" attributes="genreName"/>
                    </div>  
                    <div class="mb-3">
                        <label for="trailer" class="form-label">{{ $t(`label.trailer`) }}</label>
                        <input v-model.trim="inputs.trailer" :class="{'is-invalid': validator.inputs.trailer.$error}"  id="trailer" name="trailer" class="form-control" type="url">
                        <div v-if="validator.inputs.trailer.url.$invalid" class="form-text text-danger">{{ $t(`form.helpText.trailer.url`) }}</div>
                        <div v-else-if="validator.inputs.trailer.trailerPattern.$invalid" class="form-text text-danger">{{ $t(`form.helpText.pattern`) }}</div>
                        <div v-else class="form-text">{{ $t(`form.helpText.trailer.default`) }}</div>
                    </div>          
                </div>      
            </div>
            <!--Second and third row poster and summary-->
            <div class="cols-12 my-2">
                <label for="poster" class="form-label">{{ $t(`label.poster`) }}</label>
                <input :class="{'is-invalid': validator.inputs.poster.$error}"  id="poster" name="poster" type="file" maxlength="300" class="form-control" accept="image/png,image/jpeg" @change="handleFileUpload">
                <div v-if="validator.inputs.poster.maxValue.$invalid" class="form-text text-danger">{{ $t(`form.helpText.poster`) }}</div>
                <div v-else class="form-text">{{ $t(`form.helpText.poster`) }}</div>
            </div>   
            <div class="col-12 mb-3">
                <label for="summary" class="form-label required">{{ $t(`label.summary`) }}</label>
                <textarea v-model.trim="inputs.summary" :class="{'is-invalid': validator.inputs.summary.$error}"  id="summary" name="summary" maxlength="1000" rows="4" class="form-control"></textarea>
                <div v-if="validator.inputs.summary.summaryPattern.$invalid" class="form-text text-danger">{{ $t(`form.helpText.pattern`) }}</div>
                <div v-else class="form-text">{{ $t(`form.helpText.summary.default`) }}</div>
            </div>
            <div class="d-grid d-md-flex justify-content-md-end">
                <button type="submit" class="btn blackButton mb-4">{{ $t(`button.update`) }}</button>
            </div>
        </form>
    </main>      
</template>
<style scoped>
.update-img{
    width:15rem;
}
/*active state for custom button*/
.blackButton:active{
    border:1px solid black;
    background-color: white;
    color:black;
}
</style>