<template>
<div class="w-full md:w-4/5 p-4 sm:p-6 lg:p-8 bg-gray-100 shadow-md">
    <file-upload :m_id="m_id" :is_staff="true" />
</div>
</template>
<script lang="ts">
import { Options, Vue } from "vue-class-component";
import { Prop } from "vue-property-decorator";
import { DocRequestCreate, EDocumentType, postDocumentRequest } from "../../../modules/api/Paths/documentRequest";
import FileUpload from "../FileUpload.vue";

@Options({
    components: {
        FileUpload
    }
})
export default class ActionSDocumentCheck extends Vue {
    @Prop()
    m_id = NaN;

    async requestDocuments(){
        await Promise.all([
            this.requestDocument({mortgage_id: this.m_id, document_type: EDocumentType.GROSS_INCOME}),
            this.requestDocument({mortgage_id: this.m_id, document_type: EDocumentType.FINANCIAL_OBLIGATIONS}),
            this.requestDocument({mortgage_id: this.m_id, document_type: EDocumentType.COLLATERAL}),
        ]);
    }
    async requestDocument(doc: DocRequestCreate){
        var res = await postDocumentRequest(doc);
        if (!res.ok){
            console.error(res);
            return;
        }
    }
}
</script>
<style scoped>
</style>
