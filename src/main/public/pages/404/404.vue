<template>
  <div class="bg-white">
    <div class="max-w-7xl mx-auto py-16 px-4 sm:py-24 sm:px-6 lg:px-8">
      <div class="text-center">
        <h2 class="text-base font-semibold text-red-600 tracking-wide uppercase">404</h2>
        <p class="mt-1 text-4xl font-extrabold text-gray-900 sm:text-5xl sm:tracking-tight lg:text-6xl">Page Not Found.</p>
        <p class="max-w-xl mt-5 mx-auto text-xl text-gray-500">This page is missing from the archives...</p>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { Vue } from "vue-class-component";

export default class Page404 extends Vue {
    static tagName = "Page404";
}
</script>

<style>

</style>
