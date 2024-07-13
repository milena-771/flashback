<script>
import { RouterLink } from 'vue-router';
</script>
<template>
    <main class="mx-auto darkBg">
        <div class="text-center my-5 py-5">
            <div class="errorMsg">
                <h3>{{ $t(`titles.forbidden`) }}</h3>
            </div>         
            <div class="my-4 mx-0 px-0">
                <img src="/errors/four.png" class="img-fluid four">
                <img src="/errors/forbidden.png" class="img-fluid errorImg me-3">
                <img src="/errors/three.png" class="img-fluid three">
            </div>
            <div class="errorMsg px-2">
                <p>Désolée,<br>vous n'avez pas les droits pour accéder à cette page.</p>
                <RouterLink :to="{ name: 'welcome' }">
                    <a href="#">{{ $t(`button.backHome`) }}</a>
                </RouterLink>
            </div>
        </div>
    </main>
</template>
<style scoped>
.errorImg{
    width:18%;
}
.three{
    width: 7%;
}
</style>