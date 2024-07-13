<script>
import { RouterLink } from 'vue-router';
</script>
<template>
    <main class="text-center darkBg">
        <div class="errorMsg mt-5 pt-5">
            <h3>{{ $t(`titles.notFound`) }}</h3>
        </div>         
        <div>
            <img src="/errors/four.png" class="img-fluid four">
            <img src="/errors/not-found.png" class="img-fluid errorImg">
            <img src="/errors/four.png" class="img-fluid four">
        </div>
        <div class="errorMsg">
            <p>Vous semblez perdu-e,<br>
            la page ou la ressource à laquelle vous souhaitez accéder n'existe pas.</p>
            <RouterLink :to="{ name: 'welcome' }">
                <a href="#">{{ $t(`button.backHome`) }}</a>
            </RouterLink>
        </div>
    </main>
</template>
<style scoped>
.errorImg{
    width:18%;
}
</style>