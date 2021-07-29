<template>
  <div class="bg-white">
    <div class="max-w-7xl mx-auto py-16 px-4 sm:py-24 sm:px-6 lg:px-8">
      <div class="text-center">
        <h2
          class="text-base font-semibold text-red-600 tracking-wide uppercase"
        >
          401
        </h2>
        <p
          class="
            mt-1
            text-4xl
            font-extrabold
            text-gray-900
            sm:text-5xl
            sm:tracking-tight
            lg:text-6xl
          "
        >
          Unauthorized.
        </p>
        <p class="max-w-xl mt-5 mx-auto text-xl text-gray-500">
          You need to be logged in to get access to this page.
        </p>
        <div class="mt-4" id="box">
      <a href="#register">
        <button
          type="button"
          class="
            inline-flex
            items-center
            px-4
            py-2
            border border-gray-300
            shadow-sm
            text-sm
            font-medium
            rounded-md
            text-gray-700
            bg-white
            hover:bg-gray-50
            focus:outline-none
            focus:ring-2 focus:ring-offset-2 focus:ring-blue-500
            mr-4
          "
        >
          Sign Up
        </button>
      </a>
      <a href="#login">
        <button
          type="button"
          class="
            inline-flex
            items-center
            px-4
            py-2
            border border-gray-300
            shadow-sm
            text-sm
            font-medium
            rounded-md
            text-gray-700
            bg-white
            hover:bg-gray-50
            focus:outline-none
            focus:ring-2 focus:ring-offset-2 focus:ring-blue-500
          "
        >
          Sign In
        </button>
      </a>
    </div>
      </div>
    </div>
  </div>
</template>
<script lang="ts">
import { Vue } from "vue-class-component";

export default class PleaseLogin extends Vue {
  mounted() {}
}
</script>
<style scoped>
.page {
  width: fit-content;
  margin: auto;
  text-align: center;
}
h1 {
  color: red;
  font-size: 1.8rem;
}
</style>
