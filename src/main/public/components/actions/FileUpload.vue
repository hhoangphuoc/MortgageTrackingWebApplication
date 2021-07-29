<template>
<div>
    <p id="documentRequired" class="text-lg leading-1 font-medium text-gray-700">Documents Required:</p> 
    <div v-if="docs.length===0" colspan="3">
        There are no documents requested yet.
    </div>
    <table class="docs">
            
        <tr v-for="doc in docs" :key="doc" >
            <td class="px-6 py-4 whitespace-nowrap text-base text-gray-600">
                {{doc.document_type}}
            </td>
            <td class="px-6 py-4 whitespace-nowrap text-base font-medium text-gray-600">
                <span class="px-6 py-4 whitespace-nowrap text-base font-medium text-gray-600" :class="docStatusClasses(doc?.document_status)">
                {{doc.document_status}}
                </span>
            </td>
            <td>
                <button class="bg-blue-600 border border-transparent rounded-md shadow-sm py-1 px-6 inline-flex justify-center text-sm font-medium text-white hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500" v-on:click="viewDocument(doc)">
                    View
                </button>
            </td>
        </tr>
        <tr v-if="is_staff">
            <td>
                <button id="requestDoc" class="bg-green-400 border border-transparent rounded-md shadow-sm py-1 px-6 inline-flex justify-center text-sm font-medium text-white hover:bg-green-600 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-green-500 mt-3" v-on:click="requestExtra()">
                Add Files
                </button>
            </td>
            <td colspan="2">
                <input  id="documentName" type="text" name="doc_name" class="mt-3 mr-4 focus:ring-blue-500 focus:border-blue-500 block w-full shadow-base base:text-base border-gray-300 rounded-md">
            </td>
        </tr>
    </table>
    <div class="pop-up">
        <div>
            <form v-show="!is_staff" method="POST" enctype="multipart/form-data" action="api/file">
                <label for="file" class="text-lg leading-1 font-medium text-gray-700">Choose File to Upload</label>

                <input type="file" id="file" name="file" v-on:change="chooseFile"
                accept="image/*,pdf"
                />

                <input type="number" name="doc_id" :value="currentDoc?.doc_id ?? 1" class="hidden">
            </form>
            <span id="upload-error" class="error"></span>
            <div style="height: 100%">
            <iframe style="width: 100%; height: 100%;"></iframe>
            </div>
            <div style="display: flex;">
                <button style="width:100%" class="mr-4 button-primary" v-on:click="hidePopUp()">close</button>
                <button v-if="!is_staff" style="width:100%" class="button-primary" v-on:click="uploadFile()">upload</button>
            </div>
        </div>
        <div>
            <h1 style="font-size: 200%">
                {{currentDoc.document_type}}
            </h1>
            <span class="py-1 whitespace-nowrap text-base font-medium text-gray-600" :class="docStatusClasses(currentDoc.document_status)">
                {{currentDoc.document_status}}
            </span>

            <div v-for="file in currentFiles" :key="file">
                {{file.file_id}} {{file.file_name}}
                <button class="button-primary" v-on:click="showUploaded(file.file_id)">view</button>
            </div>
            <textarea id="failed-explanation" v-show="is_staff && isSubmitted"></textarea>
            <div v-if="isRejected">
                rejection explanation:
                <div class="textbox">
                {{currentDoc.failed_explanation}}
                </div>
            </div>

            <div class="f-center" style="margin: 1rem">
                <button v-if="!is_staff && showSubmit" v-on:click="submit()"
                    class="button-primary">{{isRejected ? "re" : ""}}submit</button>
                
                <button v-if="is_staff && isSubmitted" v-on:click="reject()"
                    class="bg-red-500 border border-transparent rounded-md shadow-sm py-1 px-3 inline-flex justify-center text-sm font-medium text-white hover:bg-red-600 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-red-500 mr-4">reject</button>
                
                <button v-if="is_staff && isSubmitted" v-on:click="accept()"
                    class="bg-green-500 border border-transparent rounded-md shadow-sm py-1 px-3 inline-flex justify-center text-sm font-medium text-white hover:bg-green-600 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-green-500">
                    accept
                </button>
            </div>
        </div>
    </div>
</div>
</template>
<script lang="ts">
import { Vue } from "vue-class-component";
import { Prop, Watch } from "vue-property-decorator";
import { Method, myFetch } from "../../modules/api/myFetch";
import { DocRequestCreate, DocumentRequest, EDocumentStatus, getDocumentRequestsByM_id, patchDocumentRequestAccept, patchDocumentRequestReject, patchDocumentRequestSubmit, postDocumentRequest } from "../../modules/api/Paths/documentRequest";
import { FileMeta, getFileMetaByDoc_id } from "../../modules/api/Paths/file";
import { Status } from "../../modules/api/Status";
import { ProcessState, changeStatus, ProcessStateEnum, ProcessStatusEnum } from "../../modules/api/Paths/processState";
export default class FileUpload extends Vue {
    docs: DocumentRequest[] = [];
    files: Record<number, FileMeta[]> = {};
    currentFiles: FileMeta[] = [];
    currentDoc: DocumentRequest = {"doc_id": 1, "mortgage_id": 0, "document_type": "??", "document_status": EDocumentStatus.PENDING};

    mounted(){
        this.onM_id();
    }
    
    @Prop()
    is_staff = false;

    @Prop()
    m_id = NaN;

    get showSubmit(){
        return this.currentFiles.length > 0 && (this.isPending || this.isRejected);
    }
    get isPending(){
        return this.currentDoc.document_status == EDocumentStatus.PENDING
    }
    get isSubmitted(){
        return this.currentDoc.document_status == EDocumentStatus.SUBMITTED;
    }
    get isRejected(){
        return this.currentDoc.document_status == EDocumentStatus.REJECTED;
    }

    get popUp(){
        return document.getElementsByClassName("pop-up")[0] as HTMLElement;
    }
    get iframe(){
        return document.getElementsByTagName("iframe")[0] as HTMLIFrameElement;
    }
    get uploadError(){
        return document.getElementById("upload-error") as HTMLElement;
    }
    get fileInput(){
        return document.getElementById("file") as HTMLInputElement;
    }
    get explainInput(){
        return document.getElementById("failed-explanation") as HTMLInputElement;
    }
    
    @Watch("m_id")
    onM_id(){
        if (!Number.isInteger(this.m_id)){
            this.$el.className = "error";
            this.$el.textContent = `${this.m_id} is not a valid mortgage id`;
            return;
        }
        this.viewDocuments();
    }

    async requestExtra(){
        const description = this.$el.querySelector(`input[name="doc_name"]`) as HTMLInputElement;
        const doc: DocRequestCreate = {mortgage_id: this.m_id, document_type: description.value};

        var res = await postDocumentRequest(doc);
        if (!res.ok){
            console.error(res);
            return;
        }
        await this.viewDocuments();
        description.value = "";
    }

    async viewDocuments(){
        const res = await getDocumentRequestsByM_id(this.m_id);

        switch (res.status){
            case Status.OK: break;
            default: {
                console.error(res);
            } return;
        }
        this.docs = res.content;
    }
    viewDocument(doc: DocumentRequest) {
        this.currentDoc = doc;
        this.showPopUp();
        const oldCurrentDoc = this.docs.find((d) => d.doc_id === doc.doc_id);
        if (oldCurrentDoc){
            this.explainInput.value = oldCurrentDoc.failed_explanation ?? "";
        }
        this.fetchFileMetas(doc.doc_id);
    }
    async fetchFileMetas(doc_id: number){
        const res = await getFileMetaByDoc_id(doc_id);
        switch (res.status){
            case Status.OK: break;
            default: {
                console.error(res);
            } return;
        }
        this.files[doc_id] = res.content;
        this.currentFiles = res.content;
    }
    async chooseFile(event: Event){
        const element = event.target as HTMLInputElement;
        const iframe = document.getElementsByTagName("iframe")[0] as HTMLIFrameElement;
        if (element.files === null || element.files.length < 1){
            throw new Error("no files specified");
        }

        const reader = new FileReader();
        const file = element.files[0];
        reader.onload = () => {
            iframe.src = reader.result as string;
        };
        reader.readAsDataURL(file);
    }
    showUploaded(file_id: string){
        const iframe = document.getElementsByTagName("iframe")[0] as HTMLIFrameElement;
        iframe.src = `./api/file/file_id/${file_id}`;
    }
    async showPopUp(){
        const popUp = document.getElementsByClassName("pop-up")[0] as HTMLElement;
        popUp.style.visibility = "visible";
    }
    async uploadFile(){
        this.uploadError.textContent = "";
        const form = this.popUp.querySelector("form") as HTMLFormElement;
        const res = await myFetch("api/file", {
            method: Method.POST,
            body: new FormData(form)
        })
        if (!res.ok){
            console.error(res);
            this.uploadError.textContent = `failed to upload file:\n${await res.text()}`;
            return;
        }
        this.fileInput.value = "";
        this.iframe.src = "about:blank";
        this.viewDocument(this.currentDoc);
    }
    hidePopUp(){
        this.currentDoc.failed_explanation = this.explainInput.value;
        this.fileInput.value = "";
        this.uploadError.textContent = "";
        this.iframe.src = "about:blank"
        this.popUp.style.visibility = "hidden";
    }

    docStatusClasses(status: EDocumentStatus): String {
        switch (status) {
        case EDocumentStatus.ACCEPTED:
            return "text-green-600";
        case EDocumentStatus.PENDING:
            return "text-yellow-500";
        case EDocumentStatus.SUBMITTED: 
            return "text-blue-600"; 
        case EDocumentStatus.REJECTED:
            return "text-red-600"
        default:
            return "bg-gray-100 text-gray-800";
        }
    }

    async submit(){
        const doc = this.currentDoc;
        const res = await patchDocumentRequestSubmit(doc.doc_id);
        if (res.status !== Status.NO_CONTENT){
            console.error(res);
            return;
        }
        changeStatus(new ProcessState(
                this.m_id,
                ProcessStateEnum.DocumentsCheck,
                ProcessStatusEnum.Processing
        ));
        doc.document_status = EDocumentStatus.SUBMITTED;
    }
    async accept(){
        const doc = this.currentDoc;
        const res = await patchDocumentRequestAccept(doc.doc_id);
        if (res.status !== Status.NO_CONTENT){
            console.error(res);
            return;
        }
        doc.document_status = EDocumentStatus.ACCEPTED;
    }
    async reject(){
        const explain = this.explainInput;
        const doc = this.currentDoc;
        const res = await patchDocumentRequestReject({
            doc_id: doc.doc_id, failed_explanation: explain.value
        });
        if (res.status !== Status.NO_CONTENT){
            console.error(res);
            return;
        }
        changeStatus(new ProcessState(
                this.m_id,
                ProcessStateEnum.DocumentsCheck,
                ProcessStatusEnum.Failed
        ));
        doc.document_status = EDocumentStatus.REJECTED;
        doc.failed_explanation = explain.value;
    }
}
</script>
<style scoped>
.pop-up {
    position: fixed;
    z-index: 1000;
    background-color: white;
    border: 0.12rem solid grey;
    padding: 1rem;
    width: 90%;
    max-width: 150ch;
    height: 90%;   
    left: 50%;
    top: 50%;
    transform: translate(-50%, -50%);
    
    display: grid;
    grid-template-columns: 1fr 35ch;
    visibility: hidden;
}
.pop-up > div {
    display: flex;
    flex-direction: column;
}
.f-center {
    display: flex;
    justify-content: center;
}
td {
    padding-inline: 1rem;
}
.textbox {
    text-align: left;
    border: 0.12rem solid grey;
    min-height: 4rem;
}
</style>
