<template>
  <h1 id="title" class="text-xl text-center">My Mortgages</h1>
  <div class="myMortgagePage mx-auto">
    <MortgageTable />
  </div>
</template>
<script lang="ts">
import MortgageTable from "../../components/MortgageTable.vue";
import { Vue, Options } from "vue-class-component";

@Options({
  components: {
    MortgageTable,
  },
})
export default class PageMyMortgage extends Vue {}
</script>

