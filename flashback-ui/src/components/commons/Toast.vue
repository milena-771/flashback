<script>
export default {
    props: {
        id: {
            type: String,
            default: null
        }
    }
}
</script>
<template>
    <div :id="id" class="toast-container p-3 top-0 end-0">
        <div class="toast shadow-sm">
            <div class="toast-header">
                <strong class="text-dark me-auto">Message</strong>
                <button type="button" class="btn-close" data-bs-dismiss="toast"></button>
            </div>
            <div class="toast-body"></div>
        </div>
    </div>
</template>
<style>
.toast{
    background-color: rgb(188, 121, 251);
    color:black;
}
</style>

