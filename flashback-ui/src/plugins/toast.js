function showToast(id, message) {
    const container = document.querySelector(`#${id}>.toast`);
    const body = container.querySelector('.toast-body');
    body.textContent = message;
    container.addEventListener('hidden.bs.toast', () => {
        body.textContent = '';
    }, { once: true });
    const toast = bootstrap.Toast.getOrCreateInstance(container);
    toast.show();
}

function showErrorToast(id, cls, message) {
    const container = document.querySelector(`#${id}>.toast`);
    const classes = container.classList;
    classes.add(cls);
    const body = container.querySelector('.toast-body');
    body.textContent = message;
    container.addEventListener('hidden.bs.toast', () => {
        classes.remove(cls);
        body.textContent = '';
    }, { once: true });
    const toast = bootstrap.Toast.getOrCreateInstance(container);
    toast.show();
}

export default {
    install: (app) => {
        app.config.globalProperties.$toast = {
            success: (id, msg) => {
                showToast(id, msg);
            },
            error: (id, msg) => {
                showErrorToast(id, 'text-bg-danger', msg);
            }
        }
    }
};