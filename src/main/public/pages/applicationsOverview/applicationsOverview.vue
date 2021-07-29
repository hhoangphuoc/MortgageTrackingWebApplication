<template>
  <div class="mx-60 min-h-full mt-4">
    <ServiceProviderTable />
  </div>
</template>

<script lang="ts">
import { Vue, Options } from "vue-class-component";
import ServiceProviderTable from "../../components/ServiceProviderTable.vue";

@Options({
  components: {
    ServiceProviderTable
  }
})
export default class PageApplicationsOverview extends Vue {

}
</script>
