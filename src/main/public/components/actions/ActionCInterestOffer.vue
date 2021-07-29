<template>
    <div class="tasks">
        <p class="mt-1 max-w-2xl text-base text-gray-700"> 
            Based on current duration of your mortgage. The interest offers has been selected.
        </p><br>
        <button id="interestOfferButton" v-on:click="viewInterestOffer()" class="default" >
            View Your Interest Offer
        </button>
        <div v-if="interestOffered" id="interest-content" class="rounded-lg border shadow p-5 bg-gray-50">
            <p id="duration"> Your mortgage duration: <span class="font-bold">{{interestOffer?.duration ?? ""}}</span></p>
            <p id="offer">
                Interest offer: <span class="font-bold">{{interestOffer?.interest_rate ?? ""}}</span> %
            </p>
            <template v-if="isActionRequired">
                <p v-show="show" class="mt-1 max-w-2xl text-base text-gray-700">
                    To get another interest offer, please select another duration option.
                </p>
                <button v-show="show" id="chooseDurationButton" v-on:click="showDuration" class="mr-4 button-blue">
                    Choose another option
                </button>
                <div id="durationChanged" v-show="durationChanged" class="duration-change">
                    <p v-show="show" class="mt-1 max-w-2xl text-sm text-gray-700">
                        Please select another duration:
                    </p>
                    <select v-show="show" v-on:click="changeDuration" id="duration-select" name="duration" class="form-control default">
                    <option value="1 months">1 months</option>
                    <option value="6 months">6 months</option>
                    <option value="12 months">12 months</option>
                    <option value="2 years">2 years</option>
                    <option value="5 years">5 years</option>
                    <option value="10 years">10 years</option>
                    </select><br/>
                </div>
                <button v-show="show" id="confirmInterest" v-on:click="confirmInterestOffer()" class="button-green">
                    Confirm this interest
                </button>
            </template>
        </div>
    </div>
</template>

<script lang="ts">
    import { Vue } from "vue-class-component";
    import { Prop } from "vue-property-decorator";
    import { MediaType } from "../../modules/api/MediaType";
    import { Method, myFetch } from "../../modules/api/myFetch";
    import { getMortgageInterestOffer, InterestOffer, MortgageProcessState } from "../../modules/api/Paths/mortgageRequest";
    import { changeStatus, ProcessState, ProcessStateEnum, ProcessStatusEnum } from "../../modules/api/Paths/processState";
    import { Status } from "../../modules/api/Status";
    import { currentUrl } from "../../modules/url";
    export default class ActionCInterestOffer extends Vue {
        interestOffered = false;
        interestOffer: InterestOffer | null = null;
        durationChanged: boolean = false;
        duration !: string;
        interest !: number;
        private show = true;
    
        @Prop()
        status: ProcessStatusEnum = ProcessStatusEnum.ActionRequired;

        get isActionRequired(){
            return this.status == ProcessStatusEnum.ActionRequired;
        }

        @Prop()
        mortgageID =- 1;

        async viewInterestOffer(){
            this.interestOffered = true;
            
            const id = parseInt(currentUrl.getParam("id")??"");
            if (isNaN(id)){
            location.href = "#mymortgage";
            }
            this.mortgageID = id;
            const res = await getMortgageInterestOffer(id);
            switch (res.status){
            case Status.OK: break;
            default: {
                console.error(res.status, res.statusText, res);
            }return;
            }
            this.interestOffer = res.content;
        }
        showDuration(){
            this.durationChanged = true;
        }
        async confirmInterestOffer(){
            if(this.durationChanged){
            this.submitChangeDuration();
            }

            const cf = confirm("Do you confirm with our interest offer ?");
            if (!cf){return;}

            this.show = false;

            
            changeStatus(new ProcessState(
                this.mortgageID,
                ProcessStateEnum.InterestOffer,
                ProcessStatusEnum.Processing
            ));
            
        }
        changeDuration(e: Event){
    const select = e.target as HTMLSelectElement;
    this.duration = select.value;

    this.interest = this.durationToInterst(this.duration);
    this.interestOffer = new InterestOffer(this.mortgageID,this.duration,this.interest);
  }

  // Switch to each corresponding interest by each duration
  durationToInterst(duration: string) {
    switch(duration){
      case "1 months": return 0.4;
      case "6 months": return 0.6;
      case "12 months": return 0.8;
      case "2 years": return 1;     
      case "5 years": return 1.2;
      case "10 years": return 1.4;
      default: {
          throw new Error(duration + "is not a valid duration");
      }
    }
  }

  // confirm the interest offer and the duration 
  async submitChangeDuration(){
    this.interest = this.durationToInterst(this.duration);
    this.interestOffer = new InterestOffer(this.mortgageID,this.duration,this.interest);
    const interestOfferURL = "./api/interestoffer" + "/" + this.mortgageID ;

    // Update the interestOffer details to the DB after changes:
    const res = await myFetch(interestOfferURL, {
        method: Method.PUT,
        body: JSON.stringify(this.interestOffer),
        headers: {
            "Content-Type": MediaType.APPLICATION_JSON
        }
    });
    switch (res.status){
        case Status.NO_CONTENT:
            break;
        default: {
            console.error("unexpected response", res.status, res.statusText,await res.text(), res);
        }
    }
  }
    }
</script>
<style scoped>
    .button-green {
        @apply inline-flex items-center justify-center px-4 py-1 border border-transparent font-medium rounded-md text-green-700 bg-green-300 hover:bg-green-500 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-green-500 sm:text-sm;
    }
    .button-blue {
        @apply inline-flex items-center justify-center px-4 py-1 border border-transparent font-medium rounded-md text-blue-600 bg-blue-300 hover:bg-blue-500 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 sm:text-sm;
    }
</style>
