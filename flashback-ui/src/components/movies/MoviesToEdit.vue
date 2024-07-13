<script>
import { RouterLink } from 'vue-router';

export default {
    data(){
        return{
            baseUrl: import.meta.env.VITE_IMG_BASE_URL,
            movies:[],
            search:""
        }
    },
    computed:{
        searchMovies(){
            const normalizeSearchValue = this.search.toLowerCase().normalize("NFD").replace(/[\u0300-\u036f]/g, "")
            return this.movies.filter(movie => {
                const normalizeMovie = movie.title.toLowerCase().normalize("NFD").replace(/[\u0300-\u036f]/g, "")
                return normalizeMovie.includes(normalizeSearchValue)
            })
        }
    },
    methods:{
        async initMovies(){
            const response = await this.$axios.get('/movies/for-edit');
            this.movies = response.data;
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
                  
        }        
    },
    beforeMount(){
        this.initMovies();
    }
}
</script>
<template>
    <main class="container-xl">
        <h1 class="pt-3 mt-2 mb-3">{{ $t(`titles.editAll`) }}</h1>
        
        <!--Search bar-->
        <form class="my-4 col-md-6 mx-auto">
            <label for="search" class="form-label fw-bold">{{ $t(`label.searchMovie`) }}</label>
            <div class="input-group">
                <input v-model.trim="search" name="search" id="search" type="text" class="form-control"/>
                <span class="input-group-text searchIcon"><i class="bi bi-search zoom"></i></span>
            </div>
            <div class="form-text">{{ $t(`form.helpText.search.searchTitle`) }}</div>
        </form> 
        
        <!--Search results-->
        <h2 v-if="searchMovies.length === movies.length && movies.length > 0">{{ $t(`titles.allFilms`) }}</h2>
        <h2 v-else>{{ $t(`titles.results`) }}</h2>

        <div class="d-flex justify-content-center">
            <div v-if="!movies.length" class="spinner-border text-dark">
                <span class="visually-hidden">Loading...</span>
            </div>
        </div>

        <div v-if="searchMovies.length" class="table-responsive">
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
                <tr v-for="movie in searchMovies">
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
    <div v-else-if="!searchMovies.length && movies.length" class="text-center my-4">
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


