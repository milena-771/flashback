
import { createI18n } from "vue-i18n";
import frMessages from "../messages/fr";

const datetimeFormats={
    fr:{
        short:{
            year: 'numeric',
            month: 'short',
            day: 'numeric'
        }
    }
}

const i18n = createI18n({
    locale:'fr',
    fallbackLocale: 'fr',
    messages:{
        fr: frMessages //TODO lazy-loading
    },
    datetimeFormats
    
})
export default i18n
