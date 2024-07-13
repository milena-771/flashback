<script>
import { RouterLink } from 'vue-router';
</script>
<template>
    <main class="text-center darkBg">
        <div class="errorMsg mt-5 pt-5">
            <h3 class="ps-4">{{ $t(`titles.unauthorized`) }}</h3>
        </div>        
        <div>
            <img src="/errors/four.png" class="img-fluid four">
            <img src="/errors/unauthorized.png" class="img-fluid errorImg mx-1">
            <img src="/errors/one.png" class="img-fluid one mx-4">
        </div>
        <div class="errorMsg">
            <p>Vous n'êtes pas autorisé-e à accéder à cette page,<br>
                vos identifiants sont invalides.</p>
            <RouterLink :to="{ name: 'welcome' }">
                <a href="#">{{ $t(`button.backHome`) }}</a>
            </RouterLink>
        </div>
    </main>
</template>
<style scoped>
.errorImg{
    width:14%;
}
.one{
    width:4.5%;
}
</style>