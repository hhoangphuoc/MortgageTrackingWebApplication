<template>
  <div class="relative">
    <h2 class="text-2xl font-bold text-blue-600 w-3/5 mx-auto text-center mt-8">
      Mortgage Process
    </h2>
    <div class="mx-auto w-3/5">
      <div
        id="pb"
        class="bg-gray-400 shadow h-10 mt-4"
        :class="last ? 'green-border' : 'tranparent-border'"
      >
        <div
          id="progress"
          :style="{ width: progressLength, transition: transition }"
          class="bg-green-400 h-10"
        ></div>
      </div>
      <div class="flex justify-between mt-2 mb-4">
        <div v-for="step in steps" :key="step.index">
          <Tooltip v-if="step.timestamp" :text="step.timestamp">
            <span
              class="inline-flex text-gray-500 cursor-pointer hover-blue"
              :class="current(step.index) ? 'font-bold' : ''"
            >
              {{ step.name }}
              <QuestionMarkCircleIcon class="h-4 w-4 mt-1 ml-1" />
            </span>
          </Tooltip>
          <span
            class="inline-flex text-gray-500 cursor-pointer"
            :class="current(step.index) ? 'font-bold' : ''"
            v-else
            >{{ step.name }}</span
          >
        </div>
      </div>
    </div>
  </div>

  <!-- left for debugging purposes. -->
  <div v-show="user?.is_staff" class="mx-auto mt-8">
    <button
      id="moveStep"
      v-on:click="moveStep()"
      type="button"
      class="
        inline-flex
        items-center
        px-2.5
        py-1.5
        border border-gray-300
        shadow-sm
        text-xs
        font-medium
        rounded
        text-gray-700
        bg-white
        hover:bg-gray-50
        focus:outline-none
        focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500
      "
    >
      Move Step
    </button>
  </div>
</template>

<script lang='ts'>
import { Options, Vue } from "vue-class-component";
import party from "party-js";
import { Prop, Watch } from "vue-property-decorator";
import { DynamicSourceType } from "party-js/lib/systems/sources";
import { Method, myFetch } from "@/main/public/modules/api/myFetch";
import { APPLICATION_JSON } from "@/main/public/modules/util";
import { Status } from "@/main/public/modules/api/Status";
import { currentUrl } from "@/main/public/modules/url";
import { store } from "../modules/store/store";
import { UserGet } from "../modules/api/Paths/user";
import Tooltip from "./Tooltip.vue";
import { ProcessState } from "../modules/api/Paths/processState";
import { QuestionMarkCircleIcon } from "@heroicons/vue/outline";
import ClientInfo from "./ClientInfo.vue";
import {getStateTimeStamp, getTimePredictions} from "../modules/api/Paths/stateTimestamp";

interface StepDefinition {
  index: number, name: string, tag: string, timestamp: string
}

@Options({
  components: {
    Tooltip,
    QuestionMarkCircleIcon,
    ClientInfo,
  },
})
export default class MortgageProcessSP extends Vue {
  mortgageID!: any;

  @Prop()
  private processState?: ProcessState;

  // at the start we need to assign the current index corresponding to the state name that we store in the db;
  @Prop()
  private fetch!: Boolean;

  private currentIndex: number = 0;

  private user: UserGet | null = null;


  @Watch("processState")
  updateProcessState() {
    this.currentIndex = this.convertFromStateToIndex();
    if (this.last) {
      this.runConfetti();
    }
    if (this.currentIndex >= 0){
      this.getTimestamps();
      this.getPredictions();
    }
  }

  mounted() {
    this.mortgageID = currentUrl.getParam("id");
    this.updateProcessState();
    
    this.user = store.userData ?? null;
  }

  private async getTimestamps() {
    const all = this.steps
      .filter(step => step.index < this.currentIndex)
      .map(step => this.getSingleTimeStamp(step));
    await Promise.allSettled(all);
  }
  private async getSingleTimeStamp(step: StepDefinition){
    console.log(step.index, this.currentIndex);
    const res = await getStateTimeStamp(this.mortgageID, step.name);
    switch (res.status){
      case Status.OK: break;
      default: {
        step.timestamp = "Server Error";
        console.error(res);
      } return;
    }

    const timeInfo = res.content;
    if (timeInfo.end_time) {
      step.timestamp = "end: " + timeInfo.end_time;
    }
    return timeInfo;
  }

  private async getPredictions(){
    const res = await getTimePredictions(this.mortgageID);
    switch (res.status){
      case Status.OK: break;
      default: {
        console.error(res);
      } return;
    }
    const predictions = res.content;
    let i = this.currentIndex;
    predictions.forEach(pred => {
      this.steps[i].timestamp = "expected-end: " + pred;
      i++;
    });
    
  }

  private steps: StepDefinition[] = [
    { index: 0, name: "Application", tag: "application", timestamp: "" },
    { index: 1, name: "Documents Check", tag: "", timestamp: "" },
    { index: 2, name: "Interest Offer", tag: "", timestamp: "" },
    { index: 3, name: "Binding Offer", tag: "", timestamp: "" },
    { index: 4, name: "Done", tag: "", timestamp: "" },
  ];

  private convertFromStateToIndex(): number {
    console.log(this.processState?.state)
    switch (this.processState?.state) {
      case "Application":
        return 0;
      case "Documents Check":
        return 1;
      case "Interest Offer":
        return 2;
      case "Binding Offer":
        return 3;
      case "Done":
        return 4;
      default:
        return -1;
    }
  }
  private stateToDateTime(state:string) {
    switch (state) {
      case "Application":
        return "Datetime of Application";
      case "Documents Check":
        return "Datetime of Documents check";
      case "Interest Offer":
        return "Datetime of Interest Offer";
      case "Binding Offer":
        return "Datetime of Binding Offer";
      case "Done":
        return "Datetime of Done";
      default:
        return "No Datetime";
    }

  }

  private nextIndex() {
    this.currentIndex = (this.currentIndex + 1) % this.steps.length;

    if (this.last) {
      this.runConfetti();
    }
  }

  get last(): Boolean {
    return this.currentIndex == this.steps.length - 1;
  }

  get progressLength() {
    return `${(this.currentIndex + 1) * 20}%`;
  }

  get transition() {
    return `width 0.25s ease-in`;
  }

  private current(index: number): Boolean {
    return index == this.currentIndex;
  }

  private runConfetti() {
    const progressBar = document.querySelector("#pb") as DynamicSourceType;
    party.confetti(progressBar, {
      count: party.variation.range(20, 40),
    });
  }
  private format(value: number): String {
    var ans = "";
    if (value < 10) {
      ans = ans + "0" + value;
    } else {
      ans = ans + value;
    }
    return ans;
  }

  private getFormatDateandTime(): String {
    var date = new Date();
    // month need to +1 because they count from 0 to 11
    var dateString =
      "" +
      date.getFullYear() +
      "-" +
      this.format(date.getMonth() + 1) +
      "-" +
      this.format(date.getDate());
    dateString =
      dateString +
      " " +
      this.format(date.getHours()) +
      ":" +
      this.format(date.getMinutes()) +
      ":" +
      this.format(date.getSeconds());
    return dateString;
  }

  private getStatus(state: String): String {
    switch (state) {
      case "Application":
        return "Processing";
      case "Documents Check":
        return "Processing";
      case "Done":
        return "Finished";
      default:
        return "Action Required";
    }
  }

  //move button to change the process state
  async moveStep() {
    var date = this.getFormatDateandTime();

    this.nextIndex();
    const processStateUrl =
      "./api/processstate/" + this.processState?.mortgageID;
    var state = this.steps[this.currentIndex].name;
    let moveStep = new ProcessState(
      this.processState?.mortgageID,
      state,
      this.getStatus(state)
    );
    const res = await myFetch(processStateUrl, {
      method: Method.PUT,
      body: JSON.stringify(moveStep),
      headers: {
        "Content-Type": APPLICATION_JSON,
      },
    });

    switch (res.status) {
      case Status.NO_CONTENT:
      case Status.OK:
          this.$emit('update:fetch', true)
          break;
      default:
        console.error("unexpected response: " + res.statusText);
    }
  }
}
</script>


<style scoped>
#pb {
  position: relative;
}

#pb:after {
  content: "";
  position: absolute;
  top: 0;
  left: 100%;
  width: 0;
  height: 0;
  border-style: solid;
  border-width: 20px 0 20px 34.6px;
  border-color: transparent transparent transparent #9ca3af;
}

#test {
  position: absolute;
  top: 140px;
  left: 600px;
}

.green-border:after {
  border-color: transparent transparent transparent rgba(52, 211, 153, 1) !important;
  transition: border-color 0.15s ease-in;
  transition-delay: 0.2s;
}

.transparent-border:after {
  border-color: transparent transparent transparent #9ca3af;
}

.hover-blue:hover {
  color: #3b82f6;
  transition: color 0.2s ease-in;
}
</style>
