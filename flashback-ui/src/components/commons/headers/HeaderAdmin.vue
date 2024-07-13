<script>
import { RouterLink } from 'vue-router';
import useStore from "@/stores/store.js";
import { mapState } from 'pinia';

export default {
    computed:{
            ...mapState(useStore,['timerID'])
    },
    methods:{
        logOut(){
                localStorage.clear();
                clearTimeout(this.timerID);
            }
    }
}
</script>
<template>
    <header class="bg-black header headerShadow">
        <nav class="container-xl navbar-dark navbar navbar-expand-md">
            <div class="container-fluid">
                <RouterLink :to="{ name: 'admin' }" class="navbar-brand">
                    <span class="flash-back">
                        <span class="flash">{{ $t(`menu.flash`) }}</span><span class="back">{{ $t(`menu.back`) }}</span>
                    </span>
                </RouterLink>
                <button class="navbar-toggler me-2" type="button" data-bs-toggle="collapse" data-bs-target="#navbar">
                    <span class="navbar-toggler-icon"></span>
                </button>
                <div id="navbar" class="collapse navbar-collapse">
                    <ul class="navbar-nav">
                        <li class="nav-item dropdown">
                            <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown">
                                {{ $t(`menu.films`) }}
                            </a>
                            <ul class="dropdown-menu">
                                <li>
                                    <RouterLink :to="{ name: 'movie-create' }" class="dropdown-item">
                                        {{ $t(`menu.createMovie`) }}
                                    </RouterLink>
                                </li>
                            </ul>
                        </li>
                        <li class="nav-item">
                            <RouterLink :to="{ name: 'retrospectives-admin' }" >
                                <a class="nav-link" href="#" role="button">{{ $t(`menu.retroAdmin`) }}</a>
                            </RouterLink>                           
                        </li>
                        <li class="nav-item">
                            <RouterLink :to="{ name: 'admin-users' }" >
                                <a class="nav-link" href="#" role="button">Utilisateurs</a>
                            </RouterLink>                           
                        </li>
                        <!--Logout as text item in mobile-->
                        <li class="nav-item d-md-none">
                            <RouterLink :to="{ name: 'sign-in' }" class="dropdown-item" @click="logOut">
                                {{ $t(`menu.logOut`) }}
                            </RouterLink>
                        </li>
                    </ul>
                </div>
                <!--Logout as icon item in desktop-->
                <ul class="navbar-nav text-end d-md-block d-none py-2 ">
                    <li class="nav-item">
                        <RouterLink :to="{ name: 'sign-in' }" class="dropdown-item" title="Logout">
                            <button class="btn btn-lg p-0"  @click="logOut"><i class="bi bi-escape escapeIcon"></i></button>
                        </RouterLink>
                    </li>                    
                </ul>
            </div>
        </nav>
    </header>   
</template>
<style scoped>
.btn{
    border: none;
    color:white;
    rotate: 90deg;
}
.btn:hover{
    color: mediumseagreen;
}

a{
    text-decoration: none;
}
</style>
