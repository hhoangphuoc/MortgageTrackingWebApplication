<template>
    <div class="flex flex-col mx-auto w-4/4 mt-10 lg:flex-row">
        <div class="bg-white shadow overflow-hidden sm:rounded-lg w-150 mr-10 lg-w-3/4">
            <div class="px-2 py-3 sm:px-6">
                <h3 class="text-lg leading-6 font-medium text-gray-900">
                Applicant Information
                </h3>
                <p class="mt-1 max-w-2xl text-sm text-gray-500">
                Personal details and application.
                </p>
            </div>
            <div class="border-t border-gray-300">
                <!-- Progress bar -->
                <nav aria-label="Progress">
                    <ol class="border border-gray-300 rounded-md divide-y divide-gray-300 md:flex md:divide-y-0">
                        <li v-for="(step, stepIdx) in steps" :key="step.name" class="relative md:flex-1 md:flex">
                            <a v-if="step.status === 'complete'" :href="step.href" class="group flex items-center w-full">
                            <span class="px-6 py-4 flex items-center text-sm font-medium">
                                <span class="flex-shrink-0 w-10 h-10 flex items-center justify-center bg-blue-600 rounded-full group-hover:bg-blue-800">
                                <CheckIcon class="w-6 h-6 text-white" aria-hidden="true" />
                                </span>
                                <span class="ml-4 text-sm font-medium text-gray-900">{{ step.name }}</span>
                            </span>
                            </a>
                            <a v-else-if="step.status === 'current'" :href="step.href" class="px-6 py-4 flex items-center text-sm font-medium" aria-current="step">
                            <span class="flex-shrink-0 w-10 h-10 flex items-center justify-center border-2 border-blue-600 rounded-full">
                                <span class="text-blue-600">{{ step.id }}</span>
                            </span>
                            <span class="ml-4 text-sm font-medium text-blue-600">{{ step.name }}</span>
                            </a>
                            <a v-else :href="step.href" class="group flex items-center">
                            <span class="px-6 py-4 flex items-center text-sm font-medium">
                                <span class="flex-shrink-0 w-10 h-10 flex items-center justify-center border-2 border-gray-300 rounded-full group-hover:border-gray-400">
                                <span class="text-gray-500 group-hover:text-gray-900">{{ step.id }}</span>
                                </span>
                                <span class="ml-4 text-sm font-medium text-gray-500 group-hover:text-gray-900">{{ step.name }}</span>
                            </span>
                            </a>
                            <template v-if="(stepIdx !== steps.length - 1)">
                            <div class="hidden md:block absolute top-0 right-0 h-full w-5" aria-hidden="true">
                                <svg class="h-full w-full text-gray-300" viewBox="0 0 22 80" fill="none" preserveAspectRatio="none">
                                <path d="M0 -2L20 40L0 82" vector-effect="non-scaling-stroke" stroke="currentcolor" stroke-linejoin="round" />
                                </svg>
                            </div>
                            </template>
                        </li>
                    </ol>
                </nav>
                <dl>
                    <div class="page px-10 py-15 sm:grid sm:grid-cols-3 sm:gap-6 sm:px-6">
                        <dt class="text-sm font-medium text-gray-500">Gross Income</dt>
                        <dd class="mt-1 ml-4 text-sm text-gray-900 sm:mt-0 sm:col-span-2">
                            {{ this.application?.grossIncome }}
                        </dd>
                        <dt class="text-sm font-medium text-gray-500"> Retired within 10 years:</dt>
                        <dd class="mt-1 ml-4 text-sm text-gray-900 sm:mt-0 sm:col-span-2">
                            {{ this.application?.willretired === false ? "No" : "Yes"}}
                        </dd>
                        <dt class="text-sm font-medium text-gray-500"> Have partner:</dt>
                        <dd class="mt-1 ml-4 text-sm text-gray-900 sm:mt-0 sm:col-span-2">
                            {{ this.application?.havePartner === false ? "No" : "Yes" }}
                        </dd>
                        <dt v-show="!this.application?.havePartner" class="text-sm font-medium text-gray-500"> Partner retired within 10 years:</dt>
                        <dd v-show="!this.application?.havePartner" class="mt-1 ml-4 text-sm text-gray-900 sm:mt-0 sm:col-span-2">
                            {{ this.application?.willPartnerRetire === false ? "No" : "Yes" }}
                        </dd>
                        <dt v-show="this.application?.havePartner" class="text-sm font-medium text-gray-500"> Partner gross income:</dt>
                        <dd v-show="this.application?.havePartner" class="mt-1 ml-4 text-sm text-gray-900 sm:mt-0 sm:col-span-2">
                            {{ this.application?.partnerGrossIncome }}
                        </dd>
                        <div class="px-4 py-3 text-right sm:px-6">
                            <button class="next bg-blue-600 border border-transparent rounded-md shadow-sm py-1 px-3 inline-flex justify-center text-sm font-medium text-white hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500">
                                Next
                            </button>
                        </div>
                    </div>

                    <div class="page px-4 py-5 sm:grid sm:grid-cols-3 sm:gap-6 sm:px-6">
                        <dt class="text-sm font-medium text-gray-500">Have student Loan:</dt>
                        <dd class="mt-1 ml-4 text-sm text-gray-900 sm:mt-0 sm:col-span-2">
                            {{ this.application?.haveStudentLoan === false ? "No" : "Yes"}}
                        </dd>
                        <dt v-show="this.application?.haveStudentLoan" class="text-sm font-medium text-gray-500"> Student debt amount:</dt>
                        <dd v-show="this.application?.haveStudentLoan" class="mt-1 ml-4 text-sm text-gray-900 sm:mt-0 sm:col-span-2">
                            {{ this.application?.studentDebt }}
                        </dd>
                        <dt class="text-sm font-medium text-gray-500"> Have spousal maintenance:</dt>
                        <dd class="mt-1 ml-4 text-sm text-gray-900 sm:mt-0 sm:col-span-2">
                            {{ this.application?.haveSpousalMaintenance === false ? "No" : "Yes"}}
                        </dd>
                        <dt v-show="this.application?.haveSpousalMaintenance" class="text-sm font-medium text-gray-500"> Your monthly alimony:</dt>
                        <dd v-show="this.application?.haveSpousalMaintenance" class="mt-1 ml-4 text-sm text-gray-900 sm:mt-0 sm:col-span-2">
                            {{ this.application?.monthlyAlimony }}
                        </dd>
                        <dt class="text-sm font-medium text-gray-500"> Other debt:</dt>
                        <dd class="mt-1 ml-4 text-sm text-gray-900 sm:mt-0 sm:col-span-2">
                            {{ this.application?.haveOtherDebt === false ? "No" : "Yes" }}
                        </dd>
                        <dt v-show="this.application?.haveOtherDebt" class="text-sm font-medium text-gray-500"> Your debt information:</dt>
                        <dd v-show="this.application?.haveOtherDebt" class="mt-1 ml-4 text-sm text-gray-900 sm:mt-0 sm:col-span-2">
                            {{ this.application?.otherDebtInfo }}
                        </dd>
                        <dt v-show="this.application?.haveOtherDebt" class="text-sm font-medium text-gray-500"> Your debt amount:</dt>
                        <dd v-show="this.application?.haveOtherDebt" class="mt-1 ml-4 text-sm text-gray-900 sm:mt-0 sm:col-span-2">
                            {{ this.application?.otherDebtAmount }}
                        </dd>
                        <dt class="text-sm font-medium text-gray-500"> Have Credits:</dt>
                        <dd class="mt-1 ml-4 text-sm text-gray-900 sm:mt-0 sm:col-span-2">
                            {{ this.application?.haveCredits === false ? "No" : "Yes" }}
                        </dd>
                        <dt v-show="this.application?.haveCredits" class="text-sm font-medium text-gray-500">Your credit information:</dt>
                        <dd v-show="this.application?.haveCredits" class="mt-1 ml-4 text-sm text-gray-900 sm:mt-0 sm:col-span-2">
                            {{ this.application?.creditInfo }}
                        </dd>
                        <dt v-show="this.application?.haveCredits" class="text-sm font-medium text-gray-500"> Your credit amount:</dt>
                        <dd v-show="this.application?.haveCredits" class="mt-1 ml-4 text-sm text-gray-900 sm:mt-0 sm:col-span-2">
                            {{ this.application?.creditAmount }}
                        </dd>
                        <div class="btns px-4 py-3 text-right sm:px-6">
                            <button class="prev-1 prev bg-blue-600 border border-transparent rounded-md shadow-sm py-1 px-3 inline-flex justify-center text-sm font-medium text-white hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 mr-4">
                                Back
                            </button>
                            <button class="next-1 next bg-blue-600 border border-transparent rounded-md shadow-sm py-1 px-3 inline-flex justify-center text-sm font-medium text-white hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500">
                                Next
                            </button>

                        </div>
                    </div>

                    <div class="page px-4 py-5 sm:grid sm:grid-cols-3 sm:gap-6 sm:px-6">
                        <dt class="text-sm font-medium text-gray-500">Your Bank amount:</dt>
                        <dd class="mt-1 ml-4 text-sm text-gray-900 sm:mt-0 sm:col-span-2">
                            {{ this.application?.bankAmount }}
                        </dd>
                        <dt class="text-sm font-medium text-gray-500"> First Collateral:</dt>
                        <dd class="mt-1 ml-4 text-sm text-gray-900 sm:mt-0 sm:col-span-2">
                            {{ this.application?.collateral1}}
                        </dd>
                        <dt class="text-sm font-medium text-gray-500"> First Collateral Amount:</dt>
                        <dd class="mt-1 ml-4 text-sm text-gray-900 sm:mt-0 sm:col-span-2">
                            {{ this.application?.price1 }}
                        </dd>
                        <dt class="text-sm font-medium text-gray-500"> Second Collateral:</dt>
                        <dd class="mt-1 ml-4 text-sm text-gray-900 sm:mt-0 sm:col-span-2">
                            {{ this.application?.collateral2}}
                        </dd>
                        <dt class="text-sm font-medium text-gray-500"> Second Collateral Amount:</dt>
                        <dd class="mt-1 ml-4 text-sm text-gray-900 sm:mt-0 sm:col-span-2">
                            {{ this.application?.price2 }}
                        </dd>
                        <dt class="text-sm font-medium text-gray-500"> Third Collateral:</dt>
                        <dd class="mt-1 ml-4 text-sm text-gray-900 sm:mt-0 sm:col-span-2">
                            {{ this.application?.collateral3}}
                        </dd>
                        <dt class="text-sm font-medium text-gray-500"> Third Collateral Amount:</dt>
                        <dd class="mt-1 ml-4 text-sm text-gray-900 sm:mt-0 sm:col-span-2">
                            {{ this.application?.price3 }}
                        </dd>
                        <div class="btns px-4 py-3 text-right sm:px-6">
                            <button class="prev-2 prev bg-blue-600 border border-transparent rounded-md shadow-sm py-1 px-3 inline-flex justify-center text-sm font-medium text-white hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 mr-4">
                                Back
                            </button>
                            <button class="next-2 next bg-blue-600 border border-transparent rounded-md shadow-sm py-1 px-3 inline-flex justify-center text-sm font-medium text-white hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500">
                                Next
                            </button>
                        </div>
                    </div>
                    <div class="page px-4 py-5 sm:grid sm:grid-cols-3 sm:gap-6 sm:px-6">
                        <dt class="text-sm font-medium text-gray-500">Your Requested Mortgage Amount:</dt>
                        <dd class="mt-1 ml-4 text-sm text-gray-900 sm:mt-0 sm:col-span-2">
                            {{ this.application?.requestedMortgageAmount }}
                        </dd>
                        <dt class="text-sm font-medium text-gray-500"> Mortgage Application Date:</dt>
                        <dd class="mt-1 ml-4 text-sm text-gray-900 sm:mt-0 sm:col-span-2">
                            {{ this.application?.date}}
                        </dd>
                        <dt class="text-sm font-medium text-gray-500"> Mortgage Duration:</dt>
                        <dd class="mt-1 ml-4 text-sm text-gray-900 sm:mt-0 sm:col-span-2">
                            {{ this.application?.duration }}
                        </dd>
                        <dt class="text-sm font-medium text-gray-500"> Reason:</dt>
                        <dd class="mt-1 ml-4 text-sm text-gray-900 sm:mt-0 sm:col-span-2">
                            {{ this.application?.reason}}
                        </dd>
                        <div class="btns text-right sm:px-6 inline-flex">
                            <button class="prev-3 prev bg-blue-600 border border-transparent rounded-md shadow-sm py-0 px-3 justify-center text-sm font-medium text-white hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 mr-4">
                                Back
                            </button>
                            <button v-on:click="onGoToMain()" class="bg-blue-600 border border-transparent rounded-md shadow-sm py-0 px-4 justify-center text-sm font-medium text-white hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500">
                               Go to Main Page
                            </button>
                        </div>
                    </div>
                </dl>
            </div>
        </div>

        <div v-show="this.processState?.processState?.state==='Documents Check'" class="mx-auto w-2/5"> 
            <form class="checkApplication rounded-xl shadow bg-gray-100 mt-2 p-4 mx-auto">
                <p class="text-lg leading-3 font-medium text-gray-500">Checking Customer Application</p><br/>
                <div class="field col-span-6 sm:col-span-3">
                    <label for="isIncomeAccepted" class="block text-sm font-medium text-gray-700">Income Check</label>
                    <span class="ml-3 block text-sm font-medium text-gray-700"><input type="radio" id="has_income_check" name="isIncomeAccepted" value="true"/> Yes </span>
                    <span class="ml-3 block text-sm font-medium text-gray-700"><input type="radio" id="no_income_check" name="isIncomeAccepted" value="false"/> No </span><br/>
                </div>
                <div class="field col-span-6 sm:col-span-3">
                    <label for="isFiniancialObligationsAccepted" class="block text-sm font-medium text-gray-700">Financial Obligation Check</label>
                    <span class="ml-3 block text-sm font-medium text-gray-700"><input type="radio" id="has_financial_check" name="isFiniancialObligationsAccepted" value="true"/> Yes </span>
                    <span class="ml-3 block text-sm font-medium text-gray-700"><input type="radio" id="no_financial_check" name="isFiniancialObligationsAccepted" value="false"/> No </span><br/>
                </div>
                <div class="field col-span-6 sm:col-span-3">
                    <label for="isCollateralAccepted" class="block text-sm font-medium text-gray-700">Collateral Check</label>
                    <span class="ml-3 block text-sm font-medium text-gray-700"><input type="radio" id="has_collateral_check" name="isCollateralAccepted" value="true"/> Yes </span>
                    <span class="ml-3 block text-sm font-medium text-gray-700"><input type="radio" id="no_collateral_check" name="isCollateralAccepted" value="false"/> No </span><br/>
                </div>
                <div class="field col-span-6 sm:col-span-3">
                <label for="failedExplannation" class="block text-sm font-medium text-gray-700">Explaination </label>
                <textarea id="failedExplannation" name="failedExplannation" class="form-textarea mt-1 focus:ring-blue-500 focus:border-blue-500 block w-full shadow-sm sm:text-sm border-gray-300 rounded-md" rows="3" placeholder="Enter explanation for failed step."></textarea><br/>
                </div>
                <button v-on:click="submitDocCheck()" id="submit-button" type="button" class="bg-blue-600 border border-transparent rounded-md shadow-sm py-1 px-3 inline-flex justify-center text-sm font-medium text-white hover:bg-blue-500 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 ml-6">
                Submit
                </button>
            </form>
        </div>  
    </div>         
</template>
<script lang="ts">
    import { Options, Vue } from "vue-class-component";
    import { PaperClipIcon,CheckIcon } from "@heroicons/vue/solid";
    import { currentUrl } from "../../modules/url";
    import {Application, applicationsGet} from "../../modules/api/Paths/applications";
    import { Status } from "../../modules/api/Status";
    import { APPLICATION_JSON, formToJson } from "../../modules/util";
    import { Method, myFetch } from "../../modules/api/myFetch";
    import { getMortgageProcessState, MortgageProcessState } from "../../modules/api/Paths/mortgageRequest";
    import { changeStatus, ProcessState, ProcessStateEnum, ProcessStatusEnum } from "../../modules/api/Paths/processState";
    

    @Options({
    components: {
        PaperClipIcon,CheckIcon
    },
    })
    export default class PageApplicationPreviewSP extends Vue {
    

    private application: Application = new Application();

    _page = 1;
    pages!: HTMLCollectionOf<HTMLElement>;
    dots!: HTMLCollectionOf<HTMLElement>;
    hashChangeHandler!: ()=>void;

    form!:HTMLFormElement;
    processState: null | MortgageProcessState = null;

    error = "";
    mortgageId !: any;

    private steps = [
  { id: 1, name: 'Income', status: 'current' },
  { id: 2, name: 'Financial Obligations', status: 'upcoming' },
  { id: 3, name: 'Collaterals', status: 'upcoming' },
  { id: 4, name: 'Mortgage Information', status: 'upcoming' },
];

    async mounted() {
        this.mortgageId = Number.parseInt(currentUrl.getParam("m_id") ?? "");
        if (isNaN(this.mortgageId)) {
        location.hash = "#";
        location.reload();
        }

        //get the process state
        const processRes = await getMortgageProcessState(this.mortgageId);
            switch (processRes.status) {
            case Status.OK: break;
            default: {
                console.error(processRes.status, processRes.statusText);
            } return;
        }
        this.processState = processRes.content;

        //get application details
        const res = await applicationsGet(this.mortgageId);
        switch (res.status) {
        case Status.OK:
            this.application = res.content;
            break;
        default:
            console.log("error");
        }

        this.pages = this.$el.getElementsByClassName("page") as any;
        this.dots = this.$el.getElementsByClassName("dot") as any;
        this.page = Number.parseInt(currentUrl.hash) || 1;
        this.onHashChance();
        this.hashChangeHandler = this.onHashChance.bind(this);
        addEventListener("hashchange", this.hashChangeHandler);
        for (const next of this.$el.getElementsByClassName("next")){
            next.addEventListener("click", this.onNext.bind(this));
        }
        for (const prev of this.$el.getElementsByClassName("prev")){
            prev.addEventListener("click", this.onPref.bind(this));
        }
        this.form = this.$el.querySelector("form") as HTMLFormElement;

    }
    onHashChance(){
        this.page = Number.parseInt(currentUrl.hash) || 1;
        for (const step of this.steps) {
            if (step.id === this.page) {
                step.status = "current";
            } else if (step.id > this.page) {
                step.status = "upcoming";
            } else {
                step.status = "complete";
            }
        }
    }
    onNext(event: Event){
            event.preventDefault();
            this.page++;
            currentUrl.hash = ""+this.page;
    }
    onPref(event: Event){
        event.preventDefault();
        this.page--;
        currentUrl.hash = ""+this.page;
    }
    onGoToMain(){
        location.hash =`#tracktraceSP?id=${this.mortgageId}`;
    }

    //send the checking conditions of each document to the DB
    async submitDocCheck(){

        const data = formToJson(this.form);

        // set the status to Failed if one documents is not accepted.
        if (data.isIncomeAccepted === "false" || data.isCollateralAccepted === "false" 
            || data.isFiniancialObligationsAccepted ===  "false") {   
            changeStatus(new ProcessState(
                this.mortgageId,
                ProcessStateEnum.DocumentsCheck,
                ProcessStatusEnum.Failed
            ));
        }else {
            changeStatus(new ProcessState(
                this.mortgageId,
                ProcessStateEnum.DocumentsCheck,
                ProcessStatusEnum.Processing
            ));
        }

        data.m_id = this.mortgageId.toString();

        const interestOfferURL = "./api/mortgagecheck" + "/" + this.mortgageId ;

        const res = await myFetch(interestOfferURL, {
        method: Method.PUT,
        body: JSON.stringify(data),
        headers: {
            "Content-Type": APPLICATION_JSON
        }
        });
        
        switch (res.status){
            case Status.NO_CONTENT: {
                location.hash = `#tracktraceSP?id=${this.mortgageId}`
            } break;
            default: {
                console.error("unexpected response", res.status, res.statusText,await res.text(), res);
            }
        }
    }
    get page(){
        return this._page;
    }
    set page(n: number){
        n = Math.max(1, Math.min(this.pages.length, n));
        for (let i = 0; i < this.pages.length; i++){
            const page = this.pages[i] as HTMLElement;
            page.style.display = i == n-1 ? "" : "none";
        }
        for (let i = 0; i < this.dots.length; i++){
            const dot = this.dots[i];
            if (i == n-1){
                dot.classList.add("active")
            } else {
                dot.classList.remove("active");
            }
        }
        this._page = n;
    }
}
</script>

