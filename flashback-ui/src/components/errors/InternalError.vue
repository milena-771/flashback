<script>
import { RouterLink } from 'vue-router';
</script>
<template>
    <main class="mx-auto darkBg">
        <div class="text-center my-5 py-5">
            <div class="errorMsg">
                <h3>{{ $t(`titles.internalError`) }}</h3>
            </div>         
            <div class="my-4 mx-0 px-0">
                <img src="/errors/five.jpg" class="img-fluid five mx-3">
                <img src="/errors/neo.jpg" class="img-fluid neoImg me-2">
                <img src="/errors/matrix.png" class="img-fluid matrixImg">
            </div>
            <div class="errorMsg px-2">
                <p>Il semble qu'un disfonctionnement se soit produit,<br>
                    nous travaillons à la résolution du problème.
                </p>
                <RouterLink :to="{ name: 'welcome' }">
                    <a href="#">{{ $t(`button.backHome`) }}</a>
                </RouterLink>
            </div>
        </div>
    </main>
</template>
<style scoped>
.neoImg{
    width:15%;
}
.matrixImg{
    width:15%
}
.five{
    width:7%;
}
.three{
    width: 7%;
}
</style>