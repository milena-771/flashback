<script>
import { RouterLink } from 'vue-router';
import { mapWritableState } from 'pinia';
import useStore from "@/stores/store.js";

    export default {
        data(){
            return {
                baseUrl: import.meta.env.VITE_IMG_BASE_URL,
                movies:[],
                directors:[],
                search:'',
                inputs:{
                    movieId:0
                },
                isLoading:false,
                numMoviesFound:0,
                selectedFilter:'/by-title?title=',
                actualPage: 1,
                totalItems: 0,
                itemsPerPage: 20,
            }
        },
        computed:{
            ...mapWritableState(useStore, ['favorites','favoriteId']),
        },
        methods:{
            async initMoviesAndFavorites(){
                this.isLoading=true;
                this.favorites = [];
                this.favoriteId = [];
                const response = await this.$axios.get(`/movies/for-search?page=${this.actualPage}&size=${this.itemsPerPage}`);
                this.movies = response.data.movies.content;
                this.totalItems = response.data.movies.totalElements;
                this.itemsPerPage = response.data.movies.size;
                this.favorites = response.data.favorites;
                this.favorites.forEach(favorite => {
                    this.favoriteId.push(favorite.movieId)
                });
                this.isLoading=false;
            },
            async addToFavorite(id){
                this.inputs.movieId = id;
                const response = await this.$axios.post('/favorites', this.inputs);
                this.favoriteId.push(id);
            },
            async searchForMovies(page){
                this.isLoading=true;
                this.movies=[];
                const response = await this.$axios.get(`/movies${this.selectedFilter}${encodeURIComponent(this.search)}&page=${page}&size=${this.itemsPerPage}`);
                this.movies = response.data.content;
                this.totalItems = response.data.totalElements;
                this.itemsPerPage = response.data.size;
                this.actualPage = page;
                this.numMoviesFound = this.movies.length;
                this.isLoading=false;
            },
            changeSearchFilter(filter){
                this.selectedFilter=filter;
            },
            changePage(){
                this.search.length? this.searchForMovies(this.actualPage) : this.initMoviesAndFavorites();
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
        <form class="my-2 row me-2" @submit.prevent="searchForMovies(1)">

            <div class="col-md-4 mb-4">
                <div class="mb-2">
                    <label class="fw-bold">{{ $t(`label.filterBy`) }}</label>
                </div>
                <div class="btn-group">
                    <input type="radio" class="btn-check" name="btn-views" id="byTitle" autocomplete="off" checked @click="changeSearchFilter('/by-title?title=')">
                    <label class="btn btn-outline-dark" for="byTitle">{{ $t(`label.title`) }}</label>
                    <input type="radio" class="btn-check" name="btn-views" id="ByDirector" autocomplete="off" @click="changeSearchFilter('/by-director?lastname=')">
                    <label class="btn btn-outline-dark" for="ByDirector">{{ $t(`label.director`) }}</label>
                    <input type="radio" class="btn-check" name="btn-views" id="ByGenre" autocomplete="off" @click="changeSearchFilter('/by-genre?genre=')">
                    <label class="btn btn-outline-dark" for="ByGenre">{{ $t(`label.genre`) }}</label>
                </div>
            </div>

            <div class="col-md-8">
                <label for="search" class="form-label fw-bold">{{ $t(`label.searchQuestion`) }}</label>
                <div class="input-group">
                    <input v-model.trim="search" name="search" id="search" type="text" class="form-control"/>
                    <button class="btn blackBtnOutline" type="submit" :disabled="search.length === 0">{{ $t(`button.search`) }}</button>
                </div>
                <div class="form-text">{{ $t(`form.helpText.search`) }}</div>
            </div>                     
        </form>

        <h2 v-if="numMoviesFound === 0" class="pt-3 mt-2 mb-3">{{ $t(`titles.allFilms`) }}</h2>
        <h2 v-else class="pt-3 mt-2 mb-3">{{ numMoviesFound }} {{ $t(`titles.results`) }}</h2>

        <div class="d-flex">
            <vue-awesome-paginate
                v-if="movies.length > 0 && !isLoading"
                class="ms-auto"
                :total-items="totalItems"
                :items-per-page="itemsPerPage"
                :max-pages-shown="1"
                v-model.trim="actualPage"
                @click="changePage(actualPage, itemsPerPage)"
            />
        </div>

        <div class="d-flex justify-content-center">
            <div v-if="isLoading" class="spinner-border text-dark">
                <span class="visually-hidden">Loading...</span>
            </div>
        </div>

        <!--Results cards-->
        <div v-if="movies.length > 0 && !isLoading" class="row row-cols-1 row-cols-sm-2 row-cols-md-4 row-cols-lg-5 g-3 mx-2">
            <div id="movies" v-for=" movie in movies" class="mb-4">
                <div class="card shadow-sm p-0 h-100"> 
                    <div>
                        <img :src="baseUrl + movie.poster" :alt="movie.title" class="card-img-top">
                    </div>
                    <div class="text-start ms-1 mt-1">
                        <span class="badge genreBadge">{{ movie.genreName }}</span> 
                    </div>
                    <div class="card-body">
                        <h5 class="card-title text-center">{{ movie.title }}</h5> 
                        <div class="card-text text-center p-1">
                            <span v-for="(director,index) in movie.directors" :key="index"> 
                                {{ director.firstname }} {{ director.lastname }} {{ movie.directors.length-1 > index ? ' & ' : ' ' }}
                            </span><br>
                            <span class="text-muted">{{ movie.releaseYear }}</span>                       
                        </div>
                    </div>
                    <div v-if="!favoriteId.includes(movie.id)" class="card-footer text-end mx-1 p-0">               
                        <button type="submit" class="btn favAndDetailsBtn btn-sm rounded-pill m-1" @click="addToFavorite(movie.id)" title="add to favorite">
                            <i class="bi bi-star"></i>
                        </button>
                        <RouterLink :to="{ name:'details', params:{id:movie.id}}" title="Movie details">
                            <button class="btn favAndDetailsBtn btn-sm rounded-pill"><i class="bi bi-three-dots"></i></button>
                        </RouterLink> 
                    </div>
                </div>
            </div>           
        </div>

        <!--Display an image if no result found-->
        <div v-else-if="movies.length === 0 && !isLoading" class="text-center my-4">
            <div><img src="/logos/hulot.jpg" class="noResult"></div>
            {{ $t(`label.noResult`) }}
        </div>
    </main>
</template>
<style scoped>
.card-img-top{
    width:100%;
}

.noResult{
    width:3rem;
}
.favAndDetailsBtn{
    border:none;
    background-color: silver;
    color:white;
}

.favAndDetailsBtn:hover{
    background-color: blueviolet;
}

.card-footer{
    border-top:none;
    background-color: white;
}

.btn-outline-dark:hover{
    background-color: black;
    color: white;
}

.genreBadge{
    background-color: mediumaquamarine;
}

</style>