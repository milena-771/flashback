<script>
export default {
    data(){
        return{
            users:[]
        }
    },
    methods:{
        async initUsers(){
            const response = await this.$axios.get('/admin/users');
            this.users = response.data
        },
        async remove(userId, name){
            try{
                const response = await this.$axios.delete(`/admin/${userId}/remove-user`);
                this.$toast.success('toast-global', this.$i18n.t("toast.user.delete", {email: name}));
                await this.initUsers();
                window.scrollTo(0,0);   
            }catch(error) {
                /**
                 * code 400 if
                 * userId = null or if userId doesn't exist.
                 */
                if(error.code === "ERR_BAD_REQUEST"){
                    window.scrollTo(0,0);
                    this.$toast.error('toast-global',  this.$i18n.t("toast.user.errorDelete"));
                }
            }
 
        } 
    },
    beforeMount(){
        this.initUsers();
    }
}
</script>
<template>
    <main class="container-xl">
        <h1 class="pt-3 mt-2 mb-3">{{ $t(`titles.userAdmin`) }}</h1>
        <div v-if="users.length" class="table-responsive">
            <table class="table table-hover align-middle">
                <thead>
                    <tr>
                        <th>{{ $t(`label.registration`) }}</th>
                        <th class="d-none d-sm-table-cell">{{ $t(`label.firstname`) }}</th>
                        <th class="d-none d-sm-table-cell">{{ $t(`label.lastname`) }}</th>
                        <th>{{ $t(`label.email`) }}</th>
                        <th class="text-center">{{ $t(`label.delete`) }}</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="user in users">
                        <td>{{ user.createdAt }}</td>
                        <td class="d-none d-sm-table-cell">{{ user.firstname }}</td>
                        <td class="d-none d-sm-table-cell">{{ user.lastname }}</td>
                        <td>{{ user.email }}</td>
                        <td class="text-center">
                            <button class="btn blackBtnOutline rounded-pill py-0 px-2" @click="remove(user.id, user.email)"><i class="bi bi-trash3 my-1"></i></button>
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>
        <div v-else>{{ $t(`label.noRetroAdmin`) }}</div>
    </main>
</template>