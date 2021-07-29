
<template>
   <div class="flex items-center justify-center mt-4">
    
	<div class="rounded-lg border shadow p-10">
        <p>Welcome <span class="font-bold">{{ this.processState?.customerName ?? ""}}</span></p>
        <p>Customer number: <span class="font-bold">{{ this.processState?.mortgageRequest?.c_id ?? ""}}</span></p>
        <p>Mortgage number: <span class="font-bold">{{ this.processState?.mortgageRequest?.m_id ?? ""}}</span></p>
        <p>Applied Date: <span class="font-bold">{{ this.processState?.date ?? ""}}</span> </p>
        <p>
          Status: {{ this.processState?.processState?.status === "Processing" ? "Your Application is being processed." : "An Action Is Required."}}
        </p>
	</div>
	
  </div>
</template>
<script lang="ts">
import { Options, Vue } from 'vue-class-component';
import { Component, Prop, Watch } from "vue-property-decorator";
import { MortgageProcessState } from "../modules/api/Paths/mortgageRequest";
import { store } from '../modules/store/store';

// @Component
export default class HelloWorld extends Vue {
  user = store.userData;

  @Prop({required: true})
  processState!: MortgageProcessState
  
  @Watch("processState")
  onStateChange(newValue: MortgageProcessState|undefined) {
    console.log(newValue);
  }

  mounted(){
    let last: any = undefined;
    setInterval(()=>{
      if (this.processState != last){
        console.log(this.processState);
      }
      last = this.processState;
    });
  }
}
</script>
