<script>
import { useVuelidate } from '@vuelidate/core';
import { required, maxLength, minValue, between, url, integer, helpers} from '@vuelidate/validators';
import Datalist from '../commons/Datalist.vue'

const isanPattern = helpers.regex(/^0000-000(\d){1}-[A-Z0-9]{4}-0000-[A-Z0-9]{1}-0000-0000-[A-Z0-9]{1}/);
const titlePattern = helpers.regex(/^[a-zA-Z0-9-éàèêôûîç,.'’ ]*$/);
const summaryPattern = helpers.regex(/^[a-zA-Z0-9-éàèâêôûùîïç'’"",.:()?!$€% ]*$/);
const trailerPattern = helpers.regex(/^[a-zA-Z0-9-&=?/:_.]*$/);

export default {
    setup(){
        return {
            validator: useVuelidate({})
        }
    },
   components:{
    Datalist
   },
    data(){
        return {
            genres : [],
            directors : [],
            genre:{},
            inputs:{
                isan:null,
                title:null,
                releaseYear:null,
                genreId:0,
                directorId:[],
                poster:null,
                trailer:"",
                summary:null
            },
        }
    },
    validations(){
        return {
            genre:{required},
            inputs:{
                isan:{required, maxLength: maxLength(33), isanPattern},
                title:{required, maxLength: maxLength(100), titlePattern},
                releaseYear:{required, between: between(1895, 2025), integer},
                genreId:{required, minValue:minValue(1)},
                directorId:{required, maxLength: maxLength(3)}, 
                poster:{required, 
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
        "inputs.isan":{
            handler(){
                const element = document.getElementById('isan');
                this.$tooltip.remove(element);                  
            },
            deep:true
        },
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
        handleFileUpload(event) {
            this.inputs.poster = event.target.files[0]
        },
        async submit(){
            if(this.genre.id){
                this.inputs.genreId = this.genre.id;
            }
            const valid = await this.validator.$validate();
            if(valid){
                const formData = new FormData();
                formData.append('isan', this.inputs.isan);
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
                    const response = await this.$axios.post('/movies', formData)
                    Object.assign(this.inputs, this.$options.data().inputs);
                    this.validator.$reset();
                    this.$toast.success('toast-global', this.$i18n.t("toast.movie.create"));
                    this.$router.push('/admin');
                }catch (error){
                    /**
                     * code 400, formData = null
                     */
                    window.scrollTo(0,0);
                    this.$toast.error('toast-global',  this.$i18n.t("toast.movie.errorCreate"));
                    if(error.response.data.fieldErrors.length){
                        const fieldErrors = error.response.data.fieldErrors;
                        for(let customError of fieldErrors){
                            if(customError.code === 'UniqueIsan'){
                                let message = this.$i18n.t("tooltip.messages.uniqueIsan");
                                this.$tooltip.display(customError.field, message);
                            }
                            if(customError.code === 'UniqueTrailer'){
                                let message = this.$i18n.t("tooltip.messages.uniqueTrailer");
                                this.$tooltip.display(customError.field, message);
                            }
                        }
                    }                 
                }
            } else{
                window.scrollTo(0,0);
                this.$toast.error('toast-global',  this.$i18n.t("toast.movie.invalid"))
            }           
        }
    },
    beforeMount(){
        this.initSelectLists();
    }
}
</script>
<template>
    <main class="container-xl">
        <h1 class="pt-3 mt-2 mb-4">{{ $t(`titles.createMovie`) }}</h1>
        <form novalidate @submit.prevent="submit">
            <div class="row mb-2">
                <div class="col-md-4 mb-3">
                    <label for="isan" class="form-label required">{{ $t(`label.isan`) }}</label>
                    <input v-model.trim="inputs.isan" :class="{'is-invalid': validator.inputs.isan.$error}" id="isan" name="isan" type="text" maxlength="33" class="form-control">
                    <div v-if="validator.inputs.isan.isanPattern.$invalid" class="form-text text-danger">{{ $t(`form.helpText.isan.pattern`) }}</div>
                    <div v-else class="form-text">{{ $t(`form.helpText.isan.default`) }}</div>
                </div>
                <div class="col-md-4 mb-3">
                    <label for="title" class="form-label required">{{ $t(`label.title`) }}</label>
                    <input v-model.trim="inputs.title" :class="{'is-invalid': validator.inputs.title.$error}" id="title" name="title" type="text" maxlength="100" class="form-control">
                    <div v-if="validator.inputs.title.titlePattern.$invalid" class="form-text text-danger">{{ $t(`form.helpText.pattern`) }}</div>
                    <div v-else class="form-text">{{ $t(`form.helpText.title.default`) }}</div>
                </div>
                <div class="col-md-4 mb-3">
                    <label for="releaseYear" class="form-label required">{{ $t(`label.releaseYear`) }}</label>
                    <input v-model.trim="inputs.releaseYear" :class="{'is-invalid': validator.inputs.releaseYear.$error}" id="releaseYear" name="releaseYear" type="number" class="form-control">
                    <div v-if="validator.inputs.releaseYear.between.$invalid" class="form-text text-danger">{{ $t(`form.helpText.releaseYear.between`) }}</div>
                    <div v-else-if="validator.inputs.releaseYear.integer.$invalid" class="form-text text-danger">{{ $t(`form.helpText.releaseYear.integer`) }}</div>
                    <div v-else class="form-text">{{ $t(`form.helpText.releaseYear.between`) }}</div>
                </div>
            </div>
            
            <div class="row mb-2">
                <div class="col-md-6 mb-3">
                    <label for="genreId" class="form-label required">{{ $t(`label.genre`) }}</label>
                    <Datalist id="genreId" :items="genres" v-model:one-value="genre" attributes="genreName" :invalid="validator.inputs.genreId.$error"/>
                </div>
                <div class="col-md-6 mb-3">
                    <label for="directorId" class="form-label required">{{ $t(`label.directors`) }}</label>
                    <Datalist id="directorId" attributes="lastname" :items="directors" v-model:valueDir="inputs.directorId" :invalid="validator.inputs.directorId.$error" :create="true" :multi="true"/>
                    <div class="form-text" :class="{'text-danger': validator.inputs.directorId.$error}">{{ $t(`form.helpText.directors`) }}</div>
                </div>
            </div>
            <div class="row mb-2">
                <div class="col-md-6 mb-3">                
                    <label for="poster" class="form-label required">{{ $t(`label.poster`) }}</label>
                    <input :class="{'is-invalid': validator.inputs.poster.$error}" id="poster" name="poster" type="file" maxlength="300" class="form-control" accept="image/png,image/jpeg" @change="handleFileUpload">
                    <div v-if="validator.inputs.poster.maxValue.$invalid" class="form-text text-danger">{{ $t(`form.helpText.poster`) }}</div>
                    <div v-else class="form-text">{{ $t(`form.helpText.poster`) }}</div>
                </div>
                <div class="col-md-6 mb-3">
                    <label for="trailer" class="form-label">{{ $t(`label.trailer`) }}</label>
                    <input v-model.trim="inputs.trailer" :class="{'is-invalid': validator.inputs.trailer.$error}" id="trailer" name="trailer" class="form-control" type="url" placeholder="https://...">
                    <div v-if="validator.inputs.trailer.url.$invalid" class="form-text text-danger">{{ $t(`form.helpText.trailer.url`) }}</div>
                    <div v-else-if="validator.inputs.trailer.trailerPattern.$invalid" class="form-text text-danger">{{ $t(`form.helpText.pattern`) }}</div>
                    <div v-else class="form-text">{{ $t(`form.helpText.trailer.default`) }}</div>
                </div>
            </div>
            <div class="col-12">
                <label for="summary" class="form-label required">{{ $t(`label.summary`) }}</label>
                <textarea v-model.trim="inputs.summary" :class="{'is-invalid': validator.inputs.summary.$error}" id="summary" name="summary" maxlength="1000" rows="4" class="form-control"></textarea>
                <div v-if="validator.inputs.summary.summaryPattern.$invalid" class="form-text text-danger">{{ $t(`form.helpText.pattern`) }}</div>
                <div v-else class="form-text">{{ $t(`form.helpText.summary.default`) }}</div>
            </div>
            <div class="d-grid d-md-flex justify-content-md-end my-4">
                <button type="submit" class="btn blackButton mb-1">{{ $t(`button.create`) }}</button>
            </div>
        </form>
    </main>
</template>
<style scoped>
/*active state for custom button*/
.blackButton:active{
    border:1px solid black;
    background-color: white;
    color:black;
}

</style>