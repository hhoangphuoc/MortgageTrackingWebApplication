<template>
<div class="tasks">
    <a :href="`#applicationPreview?m_id=${m_id}`">
        <button class="button-primary mb-2">View Application Form</button>
    </a>
    <div class="border-b-2 block md:flex">
    <file-upload :m_id="m_id" class="mt-2 w-full md:w-3/5 p-4 sm:p-6 lg:p-8 bg-gray-50 shadow-md" />
    <div id="documentCheckContainer" class="mt-2 w-full md:w-3/5 p-4 sm:p-6 lg:p-8 bg-gray-50 shadow-md">
        <p class="text-lg leading-1 font-medium text-gray-700">Last checked Document</p>     
        <div class="">
        <dl>
            <div class="px-5 py-10 sm:grid sm:grid-cols-3 sm:gap-6 sm:px-6">
            <dt class="mr-6 text-sm font-medium text-gray-500">Gross Income: </dt>
            <dd class="text-sm font-medium text-green-500 sm:mt-0 sm:col-span-2">
                <span v-if="this.documentCheck?.isIncomeAccepted" class="px-4 inline-flex text-xs leading-7 font-semibold rounded-full bg-green-100 text-green-600">
                    Approved
                </span>
                <span v-else-if="!this.documentCheck?.isIncomeAccepted" class="px-4 inline-flex text-xs leading-7 font-semibold rounded-full bg-red-100 text-red-600">
                    Rejected
                </span>
            </dd> 
            <dt class="mr-6 text-sm font-medium text-gray-500">Financial Obligations:</dt>
            <dd class="text-sm font-medium text-green-500 sm:mt-0 sm:col-span-2">
                <span v-if="this.documentCheck?.isFiniancialObligationsAccepted" class="px-4 inline-flex text-xs leading-7 font-semibold rounded-full bg-green-100 text-green-600">
                    Approved
                </span>
                <span v-else-if="!this.documentCheck?.isFiniancialObligationsAccepted" class="px-4 inline-flex text-xs leading-7 font-semibold rounded-full bg-red-100 text-red-600">
                    Rejected
                </span>
            </dd>
            <dt class="mr-6 text-sm font-medium text-gray-500">Collateral:</dt>
            <dd class="text-sm font-medium text-green-500 sm:mt-0 sm:col-span-2">
                <span v-if="this.documentCheck?.isCollateralAccepted" class="px-4 inline-flex text-xs leading-7 font-semibold rounded-full bg-green-100 text-green-600">
                    Approved
                </span>
                <span v-else-if="!this.documentCheck?.isCollateralAccepted" class="px-4 inline-flex text-xs leading-7 font-semibold rounded-full bg-red-100 text-red-600">
                    Rejected
                </span>
            </dd> 
            <dt class="mr-6 text-sm font-medium text-gray-500">Failed Explanation:</dt>
            <dd class="text-sm font-medium text-gray-700 sm:mt-0 sm:col-span-2">{{this.documentCheck?.failedExplannation}}</dd>
            </div>  
        </dl>
        </div>
    </div>
    </div>
</div>
</template>
<script lang="ts">
import { Options, Vue } from "vue-class-component";
import { Prop, Watch } from "vue-property-decorator";
import { DocumentCheck, getMortgageDocumentCheck } from "../../modules/api/Paths/mortgageRequest";
import { Status } from "../../modules/api/Status";
import FileUpload from "./FileUpload.vue";

@Options({
    components: {
        FileUpload
    }
})
export default class ActionCDocumentCheck extends Vue {
    viewDocumentChecked = false;
    documentCheck: DocumentCheck | null = null;

    mounted(){
        this.onM_id();
    }
    
    @Prop()
    m_id = NaN;
    
    @Watch("m_id")
    onM_id(){
        if (!Number.isInteger(this.m_id)){
            this.$el.className = "error";
            this.$el.textContent = `${this.m_id} is not a valid mortgage id`;
            return;
        }
        this.viewDocumentCheck();
    }
    

    // function to view the Accepted or Rejected status of the uploaded document
    async viewDocumentCheck() {
        this.viewDocumentChecked=true;
        const id = this.m_id;
        
        const res = await getMortgageDocumentCheck(id);
        switch (res.status){
        case Status.OK: break;
        default: {
            console.error(res.status, res.statusText, res);
        }return;
        }
        this.documentCheck = res.content;
    }
}
</script>
