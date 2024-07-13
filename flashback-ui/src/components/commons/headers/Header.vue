<script>
import { RouterLink } from 'vue-router';
</script>
<template>
    <header class="bg-black header headerShadow">
        <nav class="container-xl navbar-dark navbar navbar-expand-md">
            <div class="container-fluid">
                <RouterLink :to="{ name: 'welcome' }" class="navbar-brand">
                    <button class="btn rounded-pill px-0 py-0"><img src="/logos/logo.png" class="logo"></button>
                </RouterLink>
                <button class="navbar-toggler me-2" type="button" data-bs-toggle="collapse" data-bs-target="#navbar">
                    <span class="navbar-toggler-icon"></span>
                </button>
                <div id="navbar" class="collapse navbar-collapse d-md-flex flex-row-reverse">
                    <ul class="navbar-nav mx-1">
                        <li class="nav-item mx-1 rounded-pill">
                            <RouterLink :to="{ name: 'sign-in' }" class="dropdown-item">
                                <button class="btn rounded-pill">{{ $t(`menu.signIn`) }}</button>
                            </RouterLink>
                        </li>
                        <li class="nav-item mx-1">
                            <RouterLink :to="{ name: 'sign-up' }" class="dropdown-item">
                                <button class="btn rounded-pill">{{ $t(`menu.signUp`) }}</button>
                            </RouterLink>
                        </li>
                    </ul>
                </div>
            </div>
        </nav>
    </header>   
</template>
<style scoped>
.logo{
    width:3rem;
}
.btn{
    border:black solid 2px;
    color:white;
}

.btn:active{
    border:white solid 2px;
    color:white;
}

.btn:hover, .btn:focus{
    border:white solid 2px;
}
</style>