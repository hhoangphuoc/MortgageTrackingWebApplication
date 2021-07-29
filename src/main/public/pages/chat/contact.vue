<template>
  <div
    class="
      relative
      px-6
      py-5
      flex
      items-center
      space-x-3
      hover:bg-gray-50
      focus-within:ring-2 focus-within:ring-inset focus-within:ring-pink-500
    "
  >
    <div class="flex-shrink-0">
      <img class="h-10 w-10 rounded-full" :src="`./api/file/file_id/${inbox.profile_img}`" alt="" />
    </div>
    <div class="flex-1 min-w-0">
      <a class="focus:outline-none cursor-pointer">
        <!-- Extend touch target to entire panel -->
        <span class="absolute inset-0" aria-hidden="true" />
        <p class="text-sm font-medium text-gray-900">
          {{ inbox.name }}
        </p>
        <p class="text-sm text-gray-500 truncate">
          {{ inbox.email }}
        </p>
      </a>
    </div>
  </div>
</template>

<script lang="ts">
import { Vue } from "vue-class-component";
import { Prop } from "vue-property-decorator";

export default class Contact extends Vue {
  @Prop()
  inbox
}
</script>

<style scoped>

</style>