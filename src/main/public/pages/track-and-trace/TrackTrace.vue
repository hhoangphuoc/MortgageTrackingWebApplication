<template>
  <ClientInfo :processState="processState" />
  <MortgageProcess :processState="processState?.processState" />
  <Actions :processState="processState"/>
</template>

<script lang="ts">
import { Options, Vue } from "vue-class-component";
import ClientInfo from '../../components/ClientInfo.vue';
import MortgageProcess from '../../components/MortgageProcess.vue';
import Actions from '../../components/Actions.vue';
import { store } from "../../modules/store/store";
import { currentUrl } from "../../modules/url";
import { getMortgageProcessState, MortgageProcessState } from "../../modules/api/Paths/mortgageRequest";
import { Status } from "../../modules/api/Status";

@Options({
  components: {
    ClientInfo,
    MortgageProcess,
    Actions,
  },
})
export default class PageTrackTrace extends Vue {
    m_id: number | undefined;
    processState: MortgageProcessState | null = null;
    customerID =-1;
    mortgageID: any;
    customerName: string = "";
    mortgageStatus: any;
    is_staff: any;

    mounted(){
      this.navigate();
      addEventListener("hashchange", this.onHashChange);

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
        location.href = "#mymortgage";
      }
      const res = await getMortgageProcessState(id);
      switch (res.status) {
        case Status.OK: break;
        default: {
          console.error(res.status, res.statusText);
        } return;
      }
      this.processState = res.content;
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
