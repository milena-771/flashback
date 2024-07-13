<script>
import { mapWritableState } from 'pinia';
import useStore from "@/stores/store.js";

export default {
    data(){
        return{
            baseUrl: import.meta.env.VITE_IMG_BASE_URL,
            favoriteMovies:[]
        }
    },
    computed:{
        ...mapWritableState(useStore, ['favoriteId']),
    },
    methods:{
        async initFavoriteMovies(){
            const response = await this.$axios.get('/favorites/list');
            this.favoriteMovies = response.data;
        },
        async remove(movieId, title){
            try {
                const response = await this.$axios.delete(`/favorites/${movieId}`);
                this.$toast.success('toast-global', this.$i18n.t("toast.deleteFavorite.success", {film: title}));
                this.initFavoriteMovies();
                let index = this.favoriteId.indexOf(id);
                this.favoriteId.splice(index, 1);
                window.scrollTo(0,0);
            } catch(error) {
                /**
                 * code 400 if
                 * movieId = null
                 */
                 if(error.code === "ERR_BAD_REQUEST"){
                    window.scrollTo(0,0);
                    this.$toast.error('toast-global',  this.$i18n.t("toast.deleteFavorite.error"));
                 }
            }        
        }   
    },
    beforeMount(){
        this.initFavoriteMovies();
    }
}
</script>
<template>
    <main class="container-xl pb-2">
        <h1 class="pt-3 mt-2 mb-3">{{ $t(`titles.favorites`) }}</h1>

        <div v-if="favoriteMovies.length" id="movies" v-for=" movie in favoriteMovies" class="mb-2">
            <div class="card shadow-sm mb-4">
                <div class="row g-0">
                    <div class="col-4 poster">
                        <img :src="baseUrl + movie.poster" :alt="movie.title" class="img-fluid rounded-start">
                    </div>
                    <div class="col-8">
                        <div class="card-body position-absolute top-0 start-25">
                            <h5 class="card-title mb-0">{{ movie.title }}</h5>
                            <div class="card-text mt-0"><span class="text-muted">{{ movie.releaseYear }}</span></div>
                            <div class="card-text my-2"> 
                                <span v-for="(director,index) in movie.directors" :key="index"> 
                                    {{ director.firstname }} {{ director.lastname }} {{ movie.directors.length-1 > index ? ' & ' : ' ' }}
                                </span><br>                          
                            </div>
                            <div class="card-text d-lg-block d-none">
                                <span>{{ movie.summary.length > 390 ? movie.summary.slice(0,390) + "..." : movie.summary}}</span>
                            </div>
                        </div>
                        <div class="card-footer m-1 position-absolute bottom-0 end-0">
                            <RouterLink :to="{ name:'details', params:{id:movie.id}}" title="Movie details">
                                <button class="btn smallBtn rounded-pill py-0 px-2 mx-1"><i class="bi bi-three-dots"></i></button>                 
                            </RouterLink>
                            <button class="btn smallBtn rounded-pill py-0 px-2" @click="remove(movie.id, movie.title)"><i class="bi bi-trash3 my-1"></i></button>
                        </div>
                    </div>
                </div>
            </div>
        </div>           
        <div v-else class="mb-2">
            <p>{{ $t(`label.noFavorite`) }}</p>
        </div>
    </main>
</template>
<style scoped>
.card-footer{
    border-top:none;
    background-color: white;
}

.btn{
    border:none;
    background-color: silver;
    color:white;
}

.btn:hover{
    background-color: blueviolet;
}

.poster{
    max-width:10rem;
}

</style>