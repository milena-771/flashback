const options = {
    title:"Message par défaut",
    customClass:"custom-tooltip"
};

function displayTooltip(input, message) {
    const element = document.getElementById(input);
    const tooltip = bootstrap.Tooltip.getOrCreateInstance(element, options);
    tooltip.setContent({'.tooltip-inner' : message});
    element.classList.add("is-invalid");

    //get first invalid input to display first tooltip
    let firstInvalidElement = document.querySelector(".is-invalid"); 
    if(firstInvalidElement == element){
        firstInvalidElement.focus();
    }
}

function removeTooltip(element){
    const tooltip = bootstrap.Tooltip.getInstance(element);
    if(tooltip){
        element.classList.remove("is-invalid");
        tooltip.dispose();
    }   
}

export default {
    install: (app) => {
        app.config.globalProperties.$tooltip= {
            display: (input, message) => {
                displayTooltip(input, message);
            },
            remove: (element) => {
                removeTooltip(element);
            }
        }
    }
};