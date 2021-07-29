<template>
  <ClientInfo :processState="processState" />
  <MortgageProcess :processState="processState?.processState" v-model:fetch="fetch" />
  <ActionsSP :processState="processState"/>
</template>
<script lang="ts">
import { Options, Vue } from "vue-class-component";
import ClientInfo from '../../components/ClientInfo.vue';
import MortgageProcess from '../../components/MortgageProcess.vue';
import ActionsSP from '../../components/ActionsSP.vue';
import { store } from "../../modules/store/store";
import { getMortgageProcessState, MortgageProcessState } from "../../modules/api/Paths/mortgageRequest";
import { currentUrl } from "../../modules/url";
import { Watch } from "vue-property-decorator";

@Options({
  components: {
    ClientInfo,
    MortgageProcess,
    ActionsSP,
  },
})

// after this class you will get: user id, 
export default class PageTrackTraceSP extends Vue {
  static tagName = "PageTrackTraceSP";


  m_id: number | undefined;
  processState: null | MortgageProcessState = null;

  customerID =-1;
  mortgageID: any;

  customerName = "" as string;
  mortgageStatus: any;
  is_staff: any;
  fetch: Boolean = false;

  @Watch('fetch')
  getCurrentMortgageState() {
    this.queryMortgage();
    this.fetch = false;
  }

  mounted(){
    this.navigate();
    addEventListener("hashchange", this.onHashChange);
    
    //GET customerID from the fake database
    const user = store.userData;
    if (user == undefined){
      location.hash = "";
      return;
    }
    this.customerName = user.first_name + " " + user.last_name;
    this.queryMortgage();
  }

  async queryMortgage(){
    const id = parseInt(currentUrl.getParam("id")??"");

    if (isNaN(id)){
      location.href = "#applicationsOverview";
    }
    const res = await getMortgageProcessState(id);

    if (res.ok) {
      this.processState = res.content;
    } else {
      console.error(res.status, res.statusText);
    }
  }

  unmounted(){
    removeEventListener("hashchange", this.onHashChange);
  }
  onHashChange = this.navigate.bind(this);
  navigate(){
    const id = Number.parseInt(currentUrl.getParam("id") ?? "");
    if (Number.isInteger(id)){
      this.m_id = id;
    }
  }

}
</script>

<style>

</style>
