<script>
import { useRoute } from 'vue-router';
import { mapWritableState } from 'pinia';
import useStore from "@/stores/store.js";

export default {
    setup(){
        return{
            route:useRoute()
        }
    },
    data(){
        return{
            id: this.route.params.id,
            baseUrl: import.meta.env.VITE_IMG_BASE_URL,
            movie:{},
            directors:{},
            trailerEmbed:"",
            genre:"",
            inputs:{
                movieId:0
            }
        }
    },
    computed:{
        ...mapWritableState(useStore, ['favoriteId']),
    },
    methods:{
        async initMovie(){
            const resp = await this.$axios.get(`/movies/${this.id}/for-update`);
            this.movie = resp.data.movieDetails;
            this.genre = resp.data.movieDetails.genre.genreName;
            this.directors = resp.data.directorDetails;
            if(resp.data.movieDetails.trailer){
                let tmp = resp.data.movieDetails.trailer;
                const cutLink = tmp.split('v=')
                this.trailerEmbed = cutLink[1];
            }
        },
        async addToFavorite(id){
            this.inputs.movieId = id
            try {
                const response = await this.$axios.post('/favorites', this.inputs)
                this.favoriteId.push(id);
            }catch(error){
                console.error(error);
            }
        }
    },
    beforeMount(){
        this.initMovie();
    }
}
</script>
<template>
    <main class="container-xl">
        <div class="text-center">
            <h1 class="pt-3 my-4">{{ movie.title }}</h1>
            <button v-if="!favoriteId.includes(movie.id)" type="submit" class="btn btn-sm rounded-pill d-lg-none mb-4 flashbackButton" @click="addToFavorite(movie.id)"><i class="bi bi-star"></i> {{ $t(`button.addList`) }}</button>
            <button v-else  class="btn btn-sm rounded-pill d-lg-none mb-4 flashbackButton" disabled><i class="bi bi-star"></i> {{ $t(`button.disabledFavorite`) }}</button>
        </div>
        <div class="row mb-2">
            <div class="col-md-6 mb-2 text-center">
                <img :src="baseUrl + movie.poster" class="rounded img-fluid detail-img"/><br>
            </div>
            <div class="col-md-5">
                <div class="my-3 text-end d-lg-block d-none">
                    <button v-if="!favoriteId.includes(movie.id)" type="submit" class="btn btn-sm rounded-pill m-1 flashbackButton" @click="addToFavorite(movie.id)"><i class="bi bi-star"></i> {{ $t(`button.addList`) }}</button>
                    <button v-else class="btn btn-sm rounded-pill m-1 flashbackButton" disabled><i class="bi bi-star"></i> {{ $t(`button.disabledFavorite`) }}</button>
                </div>
                <div class="mb-2">
                    <label for="directors" class="fw-bold">{{ $t(`label.directedBy`) }}</label>
                    <div id="directors" v-for="director in directors">
                        <span>{{ director.firstname }} {{ director.lastname }}</span>
                    </div>
                </div>
                <div class="mb-2">
                    <label for="releaseYear" class="fw-bold">{{ $t(`label.releaseYear`) }}</label>
                    <div id="releaseYear">{{ movie.releaseYear }}</div>
                </div>
                <div class="mb-2">
                    <label for="genre" class="fw-bold">{{ $t(`label.genre`) }}</label>
                    <div id="genre">{{ genre}}</div>
                </div>
                <div>
                    <label for="summary" class="fw-bold">{{ $t(`label.summary`) }}</label>
                    <div id="summary">{{ movie.summary }}</div>
                </div>
            </div>
        </div>
        <div class="row mb-2">
            <div class="col-md-6 mb-2 text-center">
                <label v-if="movie.trailer" for="trailer" class="fw-bold mt-5 d-lg-block d-none">{{ $t(`label.trailer`) }}</label>
            </div>
        </div>
        <div v-if="movie.trailer">
            <label  for="trailer" class="fw-bold mb-1 d-md-none">{{ $t(`label.trailer`) }}</label>
            <div id="trailer" class="text-center">
                <iframe class="mb-4" width="80%" height="400" :src="`https://www.youtube.com/embed/${trailerEmbed}?autoplay=0&mute=1`" title="YouTube video player" frameborder="0" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share" allowfullscreen></iframe>
            </div>
        </div>     
    </main>   
</template>
<style scoped>
.detail-img{
    width:50%;
}
/*active state for custom button*/
.flashbackButton:active{
    color:blueviolet;
}

</style>