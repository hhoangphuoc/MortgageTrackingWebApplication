<template>
    <div class="mt-8 text-center w-3/5 mx-auto">
        <h2 class="text-2xl font-bold text-blue-600 mx-auto mb-8">
            Actions for State
        </h2>
        <div class="">
            <div v-if="state==='Application'" class="a">
                <button v-on:click="showApplication()" id="show-application" class="button-primary">
                    View Customer Application
                </button>
            </div>
            <div v-else-if="state==='Interest Offer'" class="a">
                <button id="interestOfferButton" v-on:click="showInterestOffer()" type="button" class="button-primary">
                    Show Interest Offer
                </button>
                <div v-if="interestOffered" id="interest-content" class="rounded-lg border shadow p-5">
                    <p id="duration"> Customer mortgage duration: <span class="font-bold">{{this.interestOffer?.duration ?? ""}}</span></p>
                    <p id="offer">Customer Interest offer: <span class="font-bold">{{this.interestOffer?.interest_rate ?? ""}}</span></p>
                </div>
            </div>
            <div v-else-if="state==='Binding Offer'" class="a">
                <button id="bindingOfferButton" v-on:click="showBindingOffer()" type="button" class="button-primary">
                    Show Binding Offer
                </button>
            </div>
            <div v-else-if="state==='Documents Check'" class="a border-b-2 block md:flex">
                <ActionSDocumentCheck :m_id="mortgageID" />
                <div class="w-full md:w-1/5 p-4 sm:p-6 lg:p-8 bg-gray-100 shadow-md">
                    <button v-on:click="showApplication()" id="checkButton" type="button" class="bg-blue-500 border border-transparent rounded-md shadow-sm py-1 px-5 inline-flex justify-center text-sm font-medium text-white hover:bg-blue-600 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-400 mb-2">
                    Check Application Details
                    </button>
                </div>

            </div>
        </div>
    </div>
</template>

<script lang='ts'>
import InputIcon from './InputIcon.vue';
import { UploadIcon } from '@heroicons/vue/solid'
import { CheckCircleIcon } from '@heroicons/vue/outline';
import { Options, Vue } from "vue-class-component";
import { store } from '../modules/store/store';
import { getMortgageInterestOffer, InterestOffer, MortgageProcessState} from '../modules/api/Paths/mortgageRequest';
import ActionSDocumentCheck from "./actions/ActionSDocumentCheck/ActionSDocumentCheck.vue";
import { Prop, Watch } from 'vue-property-decorator';
import { Status } from '../modules/api/Status';
import { currentUrl } from '../modules/url';

@Options({
  components: {
    UploadIcon,
    InputIcon, 
    CheckCircleIcon,
    ActionSDocumentCheck
  }
})
export default class Actions extends Vue {

    @Prop({required: true})
    processState !: MortgageProcessState;
    
    user = store.userData;
    mortgageID !: any;
    customerID !: any;
    staffID!: any;

    get state(){
        return this.processState?.processState?.state;
    }

    interestOffer: InterestOffer | null = null;

    private isDocumentChecked: Boolean = false;
    private interestOffered: boolean = false;

    @Watch("processState")
    onProcessStateChange(){
        const request = this.processState.mortgageRequest;
        this.customerID = request.c_id;
        this.staffID = request.s_id;
        this.mortgageID = request.m_id;
    }

    mounted() {
        if (this.user == undefined){
            location.hash = "";
            return;
        }
    }

    private showApplication() {
        if (this.mortgageID === undefined){
            console.error("there is not mortgage for the application");
        }
        //move to application Preview page
        location.hash=`#applicationPreviewSP?m_id=${this.mortgageID}`;
    }

    async showInterestOffer(){
        this.interestOffered = true;
        const id = parseInt(currentUrl.getParam("id")??"");
        if (isNaN(id)){
            location.href = "";
        }

        //GET all the interest offer information from DB
        const res = await getMortgageInterestOffer(id);
        switch (res.status){
        case Status.OK: break;
        default: {
            console.error(res.status, res.statusText, res);
        }return;
        }
        this.interestOffer = res.content;
        console.log(this.interestOffer);

    }
    
    private showBindingOffer(){
        if (this.mortgageID === undefined){
            console.error("there is not mortgage for the application");
        }
        location.hash=`#bindingoffer?m_id=${this.mortgageID}`;
    }

    private viewUploadedDocument(){
        if (this.mortgageID === undefined){
            console.error("there is not mortgage for the application");
        }
        window.location.reload();
        location.hash=`#documentCheck?m_id=${this.mortgageID}`;
    }


}
</script>
<style>
    .actions {
        display: flex;
        align-items: center;
        justify-content: center;
        gap: 3rem;
    }
</style>
