<script>
import { useVuelidate } from '@vuelidate/core';
import { required, maxLength, email, helpers } from '@vuelidate/validators';

const namePattern = helpers.regex(/^[a-zA-Z-éàèêôûîç'’ ]*$/);
const pwPattern = helpers.regex(/(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[%||!||*])(?!.* ).{8,42}/);
const emailPattern = helpers.regex(/^[A-Za-z0-9-_.]+@([A-Za-z]+\.{1}[A-Za-z]{2,4})/);

export default {
    setup(){
        return {
            validator: useVuelidate({})
        }
    },
    data(){
        return{
            inputs:{
                firstname:null,
                lastname:null,
                email:null,
                password:null
            }
        }
    },
    validations(){
        return {
            inputs:{
                firstname:{required, maxLength: maxLength(50), namePattern},
                lastname:{required, maxLength: maxLength(100), namePattern},
                email:{required, maxLength: maxLength(100), emailPattern},
                password:{required, pwPattern}
            }
        }
    },
    methods:{
        async signUp(){
            const valid = await this.validator.$validate();
            if(valid){
                try{
                    const response = await this.$axios.post('/sign-up', this.inputs);
                    this.$toast.success('toast-global', this.$i18n.t("toast.signUp.success"));
                    this.$router.push('/sign-in');
                    window.scrollTo(0,0);
                }catch (error){
                    if(error.response.data.fieldErrors.length){
                        const fieldErrors = error.response.data.fieldErrors;
                        for(let customError of fieldErrors){
                            if(customError.code === "UniqueEmail"){
                                let message = this.$i18n.t("tooltip.messages.uniqueEmail");
                                Object.assign(this.inputs, this.$options.data().inputs);
                                this.validator.$reset();
                                this.$toast.error('toast-global', message);
                                window.scrollTo(0,0);
                            }
                        }
                    }
                }
            }           
        }
    }
}
</script>
<template >
    <main class="mx-auto">
        <form class="mx-auto col-10 col-md-7 col-xl-3 p-3 mb-5" novalidate @submit.prevent="signUp">
            <div class="text-center mt-2">
                <img class="img-fluid" src="/logos/typescript.png" style="width:85px">
                <h4 class="my-2">
                    <span class="flash-back">
                    <span class="flashTitle">{{ $t(`menu.flash`) }}</span><span class="backTitle">{{ $t(`menu.back`) }}</span>
                    </span>
                </h4>
            </div>           
            <div class="mb-3">
                <label for="firstname" class="form-label required">{{ $t(`label.firstname`) }}</label>
                <input v-model.trim="inputs.firstname" :class="{'is-invalid': validator.inputs.firstname.$error}" id="firstname" name="firstname" type="text" maxlength="50" class="form-control">
                <div v-if="validator.inputs.firstname.namePattern.$invalid" class="form-text text-danger">{{ $t(`form.helpText.pattern`) }}</div>
                <div v-else class="form-text">{{ $t(`form.helpText.firstname.default`) }}</div>
            </div>
            <div class="mb-3">
                <label for="lastname" class="form-label required">{{ $t(`label.lastname`) }}</label>
                <input v-model.trim="inputs.lastname" :class="{'is-invalid': validator.inputs.lastname.$error}" id="lastname" name="lastname" type="text" maxlength="100" class="form-control">
                <div v-if="validator.inputs.lastname.namePattern.$invalid" class="form-text text-danger">{{ $t(`form.helpText.pattern`) }}</div>
                <div v-else class="form-text">{{ $t(`form.helpText.title.default`) }}</div>
            </div>
            <div class="mb-3">
                <label for="email" class="form-label required">{{ $t(`label.email`) }}</label>
                <input v-model.trim="inputs.email" :class="{'is-invalid': validator.inputs.email.$error}" id="email" name="email" type="email" maxlength="100" class="form-control">
                <div v-if="validator.inputs.email.emailPattern.$invalid" class="form-text text-danger">{{ $t(`form.helpText.email.format`) }}</div>
                <div v-else class="form-text">{{ $t(`form.helpText.title.default`) }}</div>
            </div>
            <div class="mb-3">
                <label for="password" class="form-label required">{{ $t(`label.password`) }}</label>
                <input v-model.trim="inputs.password" :class="{'is-invalid': validator.inputs.password.$error}"  id="password" name="password" type="password" maxlength="42" class="form-control">
                <div v-if="validator.inputs.password.pwPattern.$invalid" class="form-text text-danger">{{ $t(`form.helpText.password.pattern`) }}</div>
                <div v-else class="form-text">{{ $t(`form.helpText.password.default`) }}</div>
            </div>
            <div class="d-grid d-md-flex flex-row-reverse my-2">
                <button type="submit" class="btn flashbackButton">{{ $t(`button.signUp`) }}</button>
            </div>
        </form>
    </main>
</template>
<style scoped>
form {
    border-radius: 30px 30px;
    box-shadow: inset 0 0 10px 10px black;
    box-shadow: 0 0 5px 5px white;
    background-color: white;
}
main {
    border:2px solid black;
    height: 100vh;
    width: auto;
    background-color: black;
}
/*active state for custom button*/
.flashbackButton:active{
    color:blueviolet;
}
</style>