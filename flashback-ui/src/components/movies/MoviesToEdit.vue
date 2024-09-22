<script>
import { RouterLink } from 'vue-router';

export default {
    data(){
        return{
            baseUrl: import.meta.env.VITE_IMG_BASE_URL,
            movies:[],
            search:"",
            actualPage: 1,
            totalItems: 0,
            itemsPerPage: 10,
            isLoading:false
        }
    },
    methods:{
        async initMovies(page){
            this.isLoading=true;
            const response = await this.$axios.get(`/movies/for-edit?title=${encodeURIComponent(this.search)}&page=${page}&size=${this.itemsPerPage}`);
            this.movies = response.data.content;
            this.totalItems = response.data.totalElements;
            this.itemsPerPage = response.data.size;
            this.actualPage = page;
            this.isLoading=false;
        },
        async remove(movieId, title){
            try{
                const response = await this.$axios.delete(`/movies/${movieId}`);
                this.$toast.success('toast-global', this.$i18n.t("toast.movie.delete", {film: title}));
                await this.initMovies();
                window.scrollTo(0,0); 
            }catch(error) {
                /**
                 * code 400 if
                 * movieId = null
                 */
                if(error.code === "ERR_BAD_REQUEST"){
                    window.scrollTo(0,0);
                    this.$toast.error('toast-global',  this.$i18n.t("toast.movie.errorDelete"));
                }
            }         
        },
        changePage(page) {
            this.initMovies(page);
        }        
    },
    beforeMount(){
        this.initMovies(1);
    }
}
</script>
<template>
    <main class="container-xl">
        <h1 class="pt-3 mt-2 mb-3">{{ $t(`titles.editAll`) }}</h1>

        <!--Search bar-->
        <form class="my-4 col-md-6 mx-auto" @submit.prevent="initMovies(1)">
            <label for="search" class="form-label fw-bold">{{ $t(`label.searchMovie`) }}</label>
            <div class="input-group">
                <input v-model.trim="search" name="search" id="search" type="text" class="form-control"/>
                <button class="btn blackBtnOutline" type="submit" :disabled="search.length === 0">{{ $t(`button.search`) }}</button>
            </div>
            <div class="form-text">{{ $t(`form.helpText.searchByTitle`) }}</div>
        </form> 
        
        <!--Search results-->
        <div class="d-flex">
            <h2 class="d-md-inline d-none">{{ $t(`titles.allFilms`) }}</h2>
            <vue-awesome-paginate
                v-if="movies.length > 0 && !isLoading"
                class="ms-auto"
                :total-items="totalItems"
                :items-per-page="itemsPerPage"
                :max-pages-shown="1"
                v-model.trim="actualPage"
                @click="changePage(actualPage)"
            />
        </div>

        <div class="d-flex justify-content-center">
            <div v-if="isLoading" class="spinner-border text-dark">
                <span class="visually-hidden">Loading...</span>
            </div>
        </div>


        <div v-if="movies.length > 0 && !isLoading" class="table-responsive">
            <table class="table table-hover align-middle">
            <thead>
                <tr>
                    <th class="d-none d-sm-table-cell">{{ $t(`label.posterEdit`) }}</th>
                    <th class="d-none d-lg-table-cell">{{ $t(`label.isan`) }}</th>
                    <th>{{ $t(`label.releaseYear`) }}</th>
                    <th>{{ $t(`label.title`) }}</th>
                    <th class="text-center">{{ $t(`label.update`) }}<br>{{ $t(`label.delete`) }}</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="movie in movies">
                    <td class="d-none d-sm-table-cell"><img :src="baseUrl + movie.poster" :alt="movie.title" class="img-thumbnail"></td>
                    <td class="d-none d-lg-table-cell">{{ movie.isan }}</td>
                    <td>{{ movie.releaseYear }}</td>
                    <td class="w-5">{{ movie.title }}</td>
                    <td class="text-center">
                        <div class="my-2">
                            <RouterLink :to="{ name: 'movie-update', params: { id: movie.id }}" title="Update movie">
                                <button class="btn flashbackButton rounded-pill py-0 px-2"><i class="bi bi-pencil-square"></i></button>
                            </RouterLink>
                        </div>
                        <div class="my-2">
                            <button class="btn blackBtnOutline rounded-pill py-0 px-2" @click="remove(movie.id, movie.title)"><i class="bi bi-trash3 my-1"></i></button>
                        </div>
                    </td>
                </tr>
            </tbody>
        </table>
    </div>

     <!--Display an image if no result found-->
    <div v-else-if="movies.length === 0 && !isLoading" class="text-center my-4">
        <div><img src="/logos/empty-folder.png" class="noResult"></div>
        {{ $t(`label.noResult`) }}
    </div>
    </main>
</template>
<style scoped>
.img-thumbnail{
    width:3rem;
}
.zoom{
    color: white;
}
.searchIcon{
    background-color: black;
}

.noResult{
    width:3rem;
}

/*active state for custom button*/
.flashbackButton:active{
    color:blueviolet;
}
.blackBtnOutline:active{
    background-color: black;
    color:white;
}
</style>


