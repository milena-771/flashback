<script>
import { RouterLink } from 'vue-router';
import { mapWritableState } from 'pinia';
import useStore from "@/stores/store.js";

    export default {
        data(){
            return {
                baseUrl: import.meta.env.VITE_IMG_BASE_URL,
                movies:[],
                moviesTosearch:[],
                directors:[],
                search:'',
                inputs:{
                    movieId:0
                }
            }
        },
        computed:{
            ...mapWritableState(useStore, ['favorites','favoriteId']),
            searchMovies(){
                const normalizeSearchValue = this.search.toLowerCase().normalize("NFD").replace(/[\u0300-\u036f]/g, "")
                return this.movies.filter(movie => {
                    const normalizeMovie = movie.title.toLowerCase().normalize("NFD").replace(/[\u0300-\u036f]/g, "")
                    return normalizeMovie.includes(normalizeSearchValue)
                })
            }
        },
        methods:{
            async initMoviesAndFavorites(){
                this.favorites = [];
                this.favoriteId = [];
                const response = await this.$axios.get('/movies/for-search');
                this.movies = response.data.movies;
                this.favorites = response.data.favorites;
                this.favorites.forEach(favorite => {
                    this.favoriteId.push(favorite.movieId)
                })
            },
            async addToFavorite(id){
                this.inputs.movieId = id
                const response = await this.$axios.post('/favorites', this.inputs)
                this.favoriteId.push(id);
            }
        },
        beforeMount(){
            this.initMoviesAndFavorites();
        }
    }
</script>
<template>
    <main class="container-xl">
        <h1 class="pt-3 mt-2 mb-3">{{ $t(`titles.search`) }}</h1>

        <!--Search bar-->
        <form class="my-4 col-md-6 mx-auto">
            <label for="search" class="form-label fw-bold">{{ $t(`label.searchQuestion`) }}</label>
            <div class="input-group">
                <input v-model.trim="search" name="search" id="search" type="text" class="form-control"/>
                <span class="input-group-text searchIcon"><i class="bi bi-search zoom"></i></span>
            </div>
            <div class="form-text">{{ $t(`form.helpText.search.searchTitle`) }}</div>
        </form>

        <h2 v-if="searchMovies.length === movies.length" class="pt-3 mt-2 mb-3">{{ $t(`titles.allFilms`) }}</h2>
        <h2 v-else>{{ $t(`titles.results`) }}</h2>

        <div class="d-flex justify-content-center">
            <div v-if="!movies.length" class="spinner-border text-dark">
                <span class="visually-hidden">Loading...</span>
            </div>
        </div>
        
        <!--Results cards-->
        <div v-if="searchMovies.length" class="row row-cols-1 row-cols-sm-2 row-cols-md-4 row-cols-lg-5 g-3 mx-2">
            <div id="movies" v-for=" movie in searchMovies" class="mb-4">
                <div class="card shadow-sm p-0 h-100"> 
                    <div>
                        <img :src="baseUrl + movie.poster" :alt="movie.title" class="card-img-top">
                    </div>
                    <div class="card-body text-center">
                        <h5 class="card-title">{{ movie.title }}</h5>   
                        <div class="card-text p-1">
                            <span v-for="(director,index) in movie.directors" :key="index"> 
                                {{ director.firstname }} {{ director.lastname }} {{ movie.directors.length-1 > index ? ' & ' : ' ' }}
                            </span><br> ({{ movie.releaseYear }})                            
                        </div>
                    </div>
                    <div v-if="!favoriteId.includes(movie.id)" class="card-footer text-end mx-1 p-0">                  
                        <button type="submit" class="btn btn-sm rounded-pill m-1" @click="addToFavorite(movie.id)" title="add to favorite">
                            <i class="bi bi-star"></i>
                        </button>
                        <RouterLink :to="{ name:'details', params:{id:movie.id}}" title="Movie details">
                            <button class="btn btn-sm rounded-pill"><i class="bi bi-three-dots"></i></button>
                        </RouterLink>  
                    </div>
                </div>
            </div>           
        </div>

        <!--Display an image if no result found-->
        <div v-else-if="!searchMovies.length && movies.length" class="text-center my-4">
            <div><img src="/logos/hulot.jpg" class="noResult"></div>
            {{ $t(`label.noResult`) }}
        </div>
    </main>
</template>
<style scoped>
.zoom{
    color: white;
}
.searchIcon{
    background-color: black;
}
.card-img-top{
    width:100%;
}

.noResult{
    width:3rem;
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