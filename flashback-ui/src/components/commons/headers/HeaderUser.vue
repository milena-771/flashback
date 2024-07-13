<script>
import { RouterLink } from 'vue-router';
import { mapWritableState } from 'pinia';
import useStore from "@/stores/store.js";

    export default {
        data(){
            return {
                firstname:null
            }
        },
        computed:{
            ...mapWritableState(useStore,['favoriteId'])
        },
        methods:{
            async initProfile(){
                this.firstname = localStorage.getItem('userName');
            },
            logOut(){
                localStorage.clear();
                this.favoriteId = [];
            }
        },
        beforeMount(){
            this.initProfile();
        }
    }
</script>
<template>
    <header class="bg-black header headerShadow">
        <nav class="container-xl navbar-dark navbar navbar-expand-md">
            <div class="container-fluid">
                <RouterLink :to="{ name: 'search' }" class="navbar-brand">
                    <span class="flash-back">
                        <span class="flash">{{ $t(`menu.flash`) }}</span><span class="back">{{ $t(`menu.back`) }}</span>
                    </span>
                </RouterLink>
                <button class="navbar-toggler me-2" type="button" data-bs-toggle="collapse" data-bs-target="#navbar">
                    <span class="navbar-toggler-icon"></span>
                </button>                             
                <div id="navbar" class="collapse navbar-collapse">
                    
                    <!--Mobile dropdown menu-->
                    <ul class="navbar-nav d-md-none">
                        <li class="nav-item dropdown">
                            <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown">
                                {{ $t(`menu.welcome`) }} {{ firstname }} <i class="bi bi-emoji-smile"></i>
                            </a>
                            <ul class="dropdown-menu">
                                <li>
                                    <RouterLink :to="{ name: 'sign-in' }" class="dropdown-item" @click="logOut">
                                        {{ $t(`menu.logOut`) }}
                                    </RouterLink>
                                </li>
                            </ul>
                        </li>
                    </ul>
                    <ul class="navbar-nav">
                        <li class="nav-item dropdown">
                            <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown">{{ $t(`menu.retrospectives`) }}</a>
                            <ul class="dropdown-menu">
                                <li>
                                    <RouterLink :to="{ name: 'organize-retrospective' }" class="dropdown-item">
                                        {{ $t(`menu.organize`) }}
                                    </RouterLink>
                                </li>
                                <li>
                                    <RouterLink :to="{ name: 'retrospectives-to-come' }" class="dropdown-item">
                                        {{ $t(`menu.retroToCome`) }}
                                    </RouterLink>
                                </li>
                                <li>
                                    <RouterLink :to="{ name: 'planning' }" class="dropdown-item">
                                        {{ $t(`menu.planning`) }}
                                    </RouterLink>
                                </li>
                            </ul>
                        </li>
                        <li class="nav-item position-relative">
                            <RouterLink class="nav-link position-relative" :to="{ name: 'favorites' }">
                                {{ $t(`menu.favorites`) }}
                                <span v-if="favoriteId.length" class="d-md-inline d-none position-absolute top-0 start-100 translate-middle-x badge rounded-pill notifFavorite ms-1">{{ favoriteId.length }}</span>
                                <span v-if="favoriteId.length" class="d-sm-none badge rounded-pill notifFavorite">{{ favoriteId.length }}</span>
                            </RouterLink>
                        </li>
                    </ul>                    
                </div>
                <!--Desktop dropdown menu-->
                <ul class="navbar-nav d-md-block text-end d-none">
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown">
                            {{ $t(`menu.welcome`) }} {{ firstname }} <i class="bi bi-emoji-smile"></i>
                        </a>
                        <ul class="dropdown-menu">
                            <li>
                                <RouterLink :to="{ name: 'sign-in' }" class="dropdown-item" @click="logOut">
                                    {{ $t(`menu.logOut`) }}
                                </RouterLink>
                            </li>
                        </ul>
                    </li>
                </ul>
            </div>           
        </nav>
    </header>   
</template>
<style scoped>
.btnFavorite{
    color:white;
    border: none;
}
.btnFavorite:hover{
    color:mediumseagreen;
}

.notifFavorite{
    background-color: blueviolet;
}
</style>
