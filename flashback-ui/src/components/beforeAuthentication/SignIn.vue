<script>
import { mapWritableState } from 'pinia';
import useStore from "@/stores/store.js";

export default {
    data(){
        return {
            inputs:{
                email:null,
                password:null
            }, 
            wrongCredentials:false    
        }           
    },
    computed:{
        ...mapWritableState(useStore,['favorites','favoriteId'])
    },
    methods:{
        async submit(){ 
            try{
                localStorage.clear();
                const response = await this.$axios.post('/sign-in', this.inputs);       
                if(response.data.role === "ADMIN"){
                    localStorage.setItem('hasRole', "ADMIN");
                    this.$router.push('/admin');
                }else {
                    localStorage.setItem('hasRole', "USER");
                    this.$router.push('/search');                  
                }
            }catch(error){
                if(error.response.status === 400){
                    this.wrongCredentials = true;
                }
                /**
                 * If inputs = null
                 */
                if(error.response.status === 415){
                    window.scrollTo(0,0);
                    this.$toast.error('toast-global',  this.$i18n.t("toast.signIn.error"));
                }
            }
            
        }
    }
}
</script>
<template>
    <main class="mx-auto darkBg">
        <form class="d-flex align-items-center flex-column mx-auto col-10 col-md-6 col-lg-4 py-5 mt-5 rounded-circle" novalidate @submit.prevent="submit">

            <!--Title with image and logo-->
            <div class="text-center mt-2">
                <img class="img-fluid" src="/logos/film.jpg" style="width:80px">
                <h4 class="my-2">
                    <span class="flash-back">
                    <span class="flashTitle">{{ $t(`menu.flash`) }}</span><span class="backTitle">{{ $t(`menu.back`) }}</span>
                    </span>
                </h4>
            </div>

            <!--Inputs sign-in-->                   
            <div class="col-7 mx-auto mb-3">
                <label for="email" class="form-label">{{ $t(`label.email`) }}</label>
                <input v-model.trim="inputs.email" :class="{'is-invalid': wrongCredentials}" id="email" type="email" class="form-control"> 
                <div v-if="wrongCredentials" class="form-text text-danger">{{ $t(`form.helpText.signIn.failed`) }}</div>             
            </div>
            <div class="col-7  mx-auto mb-3">
                <label for="password" class="form-label">{{ $t(`label.password`) }}</label>
                <input v-model.trim="inputs.password" :class="{'is-invalid': wrongCredentials}" id="password" type="password" class="form-control">
                <div v-if="wrongCredentials" class="form-text text-danger">{{ $t(`form.helpText.signIn.failed`) }}</div> 
            </div>
            <div class="d-flex justify-content-center my-4">
                <button type="submit" class="btn flashbackButton">{{ $t(`button.signIn`) }}</button>
            </div>
        </form>
    </main>
</template>
<style scoped>
form {
    box-shadow: inset 0 0 10px 10px black;
    box-shadow: 0 0 5px 5px white;
    background-color: white;
}
/*active state for custom button*/
.flashbackButton:active{
    color:blueviolet;
}
</style>