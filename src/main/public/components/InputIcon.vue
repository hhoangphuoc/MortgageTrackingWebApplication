<template>
  <div>
    <label for="account_number" class="block text-sm font-medium text-gray-700">{{label}}</label>
    <div class="mt-1 relative rounded-md shadow-sm">
      <input type="text" name="account_number" id="account_number" class="focus:ring-indigo-500 focus:border-indigo-500 block w-full pr-10 sm:text-sm border-gray-300 rounded-md" :placeholder="placeholder" />
      <div class="absolute inset-y-0 right-0 pr-3 flex items-center pointer-events-none">
        <QuestionMarkCircleIcon class="h-5 w-5 text-gray-400" aria-hidden="true" />
      </div>
    </div>
  </div>
</template>

<script>
import { QuestionMarkCircleIcon } from '@heroicons/vue/solid'

export default {
  components: {
    QuestionMarkCircleIcon,
  },
  props: {label: String, placeholder: String}
}
</script>