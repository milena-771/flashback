<script>
import Multiselect from '@vueform/multiselect';

export default {
    components:{
        Multiselect
    },
    props:{
        attributes:{
            type:String,
            default:""
        },
        items:{
            type:Array,
            default:[]
        },
        valueDir:{
            type:Array,
            default:[]
        },
        oneValue:{
            type:Object,
            default:{}
        },
        invalid:{
            type:Boolean,
            default:false
        },
        create:{
            type:Boolean,
            default:false
        },
        multi:{
            type:Boolean,
            default:false
        }
    },
    computed:{
        valueMulti:{
            get(){
                return this.valueDir
            },
            set(newValue){
                return this.$emit('update:valueDir', newValue)
            }
        },
        valueSingle:{
            get(){
                return this.oneValue
            },
            set(newValue){
                return this.$emit('update:oneValue', newValue)
            }
        }
    },
    methods:{
        async fetchData(query){
            const normalizeSearchValue = query.toLowerCase().normalize("NFD").replace(/[\u0300-\u036f]/g, "")
            return this.items.filter(item => {
                    const normalizeItem = item.lastname.toLowerCase().normalize("NFD").replace(/[\u0300-\u036f]/g, "")
                    if(normalizeItem.includes(normalizeSearchValue)){
                        return item.id
                    }else{
                        return null
                    }
                })
        },
    }
}
</script>
<template>
    <Multiselect
        v-if="multi"
        v-model="valueMulti" 
        mode="tags"
        :placeholder="$t(`form.placeholder.searchDirector`)"
        no-options-text="Aucun résultat."
        value-prop="id"
        :label="attributes"
        :track-by="attributes"
        :searchable="true"
        :clear-on-select="true"
        :filter-results="false"
        :min-chars="1"
        :resolve-on-load="false"
        :delay="0"
        :options="fetchData"
        :multiple="true"
        :max="3"
        :limit="3"
        :class="invalid ? 'multiselect-invalid' : 'multiselect-form'"
        :object="create ? false : true"
    >
        <template v-slot:tag="{ option, handleTagRemove}">
                <div class="multiselect-tag">
                    {{ option.lastname }} {{ option.firstname }}
                    <span class="multiselect-tag-remove" @click="handleTagRemove(option, $event)">
                        <span class="multiselect-tag-remove-icon"></span>
                    </span>
                </div>
        </template>

        <template v-slot:option="{ option }" v-slot:dropdown="{ dropdown }" >
                <div>
                    <span>{{ option.lastname }} {{ option.firstname }}</span>
                </div>          
        </template>
    </Multiselect>
    <Multiselect
        v-else
        v-model="valueSingle" 
        value-prop="id" 
        :label="attributes" 
        :options="items"
        :placeholder="$t(`form.helpText.select`)" 
        :object="true"
        :class="invalid ? 'multiselect-invalid' : 'multiselect-form'"
    />
</template>
<style scoped>
.multiselect-tag {
    padding: 5px 8px;
    border-radius: 22px;
    background: mediumseagreen;
    margin: 3px 3px 8px;
  }

  .multiselect-tag i:before {
    color: #ffffff;
    border-radius: 50%;
  }

  .multiselect-form {
    --ms-ring-color: none;
    --ms-border-color-active:blueviolet;
    --ms-border-width-active:3px;
  }

  .multiselect-invalid{
    --ms-ring-width:4px;
    --ms-ring-color:rgba(220,53,69,0.3);
    --ms-border-color:#dc3545;
    --ms-border-color-active:#dc3545;
    --ms-border-width-active:3px;
  }
</style>