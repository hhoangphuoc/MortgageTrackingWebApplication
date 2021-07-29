<template>
    
    <div class="mt-8 text-center w-3/5 mx-auto">
        <h2 class="text-2xl font-bold text-blue-600 mx-auto mb-8">
        Action steps: <span class="font-bold">{{state ?? " Received"}}</span> 
        </h2>
        <div class="page-container" id="customer-action-container">
            <div v-if="state === 'Application'" class="tasks">
              <button id="applicationButton" v-on:click="viewApplication()" type="button" class="button-primary">
                View Your Application
              </button>
            </div>
            <ActionCInterestOffer :m_id="m_id" :status="status" v-if="state === 'Interest Offer'" class="tasks"/>
          
            <div v-if="state === 'Binding Offer'" class="tasks">
              <p> View Your Binding Offer </p>
              <button id="bindingOfferButton" v-on:click="viewBindingOffer()" class="button-primary">
                View Binding offer
              </button>
            </div>
            <ActionCDocumentCheck :m_id="m_id"  v-if="state === 'Documents Check'" />
        </div>
    </div>
</template>

<script lang='ts'>
import { CheckCircleIcon } from '@heroicons/vue/outline';
import { UploadIcon } from '@heroicons/vue/solid';
import { Options, Vue } from "vue-class-component";
import { Prop, Watch } from 'vue-property-decorator';
import { Application } from '../modules/api/Paths/applications';
import { MortgageProcessState } from '../modules/api/Paths/mortgageRequest';
import { changeStatus, ProcessState, ProcessStateEnum, ProcessStatusEnum } from "../modules/api/Paths/processState";
import { store } from '../modules/store/store';
import ActionCDocumentCheck from "./actions/ActionCDocumentCheck.vue";
import ActionCInterestOffer from "./actions/ActionCInterestOffer.vue";
import InputIcon from './InputIcon.vue';
@Options({
  components: {
    UploadIcon,
    InputIcon, 
    CheckCircleIcon,
    ActionCInterestOffer,
    ActionCDocumentCheck
  }
})
export default class Actions extends Vue {

  @Prop({required: true})
  processState !: MortgageProcessState;

  get m_id(){
    return this.processState.mortgageRequest.m_id;
  }

  stateInfo: ProcessState | null = null;
  user = store.userData;

  get state(){
    return this.processState?.processState?.state;
  }
  get status(){
    return this.processState?.processState?.status;
  }
  get mortgageID(){
    return this.processState.mortgageRequest.m_id;
  }

  async viewApplication(){
    if (this.mortgageID === undefined){
      console.error("there is not mortgage for the application");
    }
    this.processState.processState.status = ProcessStatusEnum.Processing;

    
    changeStatus(this.processState.processState);

    //move to another page to view application
    location.hash = `#applicationPreview?m_id=${this.mortgageID}`;

  }

  private viewBindingOffer(){
    //move to the binding offer page:
    location.hash=`#bindingofferCustomer?m_id=${this.mortgageID}`;
  }
}
</script>
<style>
  .file_upload {
    width: 300px;
    min-height:100px;
    border: 2px solid #dddddd;
    margin-top: 15px;

    display: flex;
    align-items: center;
    justify-content: center;
    font-weight: bold;
    color: #cccccc;
  }
  .file-preview {
    display: none;
    width: 100%;
  }


</style>
