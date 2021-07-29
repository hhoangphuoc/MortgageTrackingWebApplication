<template>
  <dl>
    <div
      v-for="(value, name, index) in app"
      :key="index"
      class="px-4 py-5 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-6"
      :class="index % 2 == 0 ? 'bg-gray-50' : 'bg-white'"
    >
      <dt class="text-sm font-medium text-gray-500">{{ name }}</dt>
      <dd class="mt-1 ml-4 text-sm text-gray-900 sm:mt-0 sm:col-span-2">
        {{ value }}
      </dd>
    </div>
    <div class="bg-white px-4 py-5 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-6">
      <dt class="text-sm font-medium text-gray-500">Attachments</dt>
      <dd class="mt-1 text-sm text-gray-900 sm:mt-0 sm:col-span-2">
        <ul class="border border-gray-200 rounded-md divide-y divide-gray-200">
          <li class="pl-3 pr-4 py-3 flex items-center justify-between text-sm">
            <div class="w-0 flex-1 flex items-center">
              <PaperClipIcon
                class="flex-shrink-0 h-5 w-5 text-gray-400"
                aria-hidden="true"
              />
              <span class="ml-2 flex-1 w-0 truncate">
                resume_back_end_developer.pdf
              </span>
            </div>
            <div class="ml-4 flex-shrink-0">
              <a href="#" class="font-medium text-blue-600 hover:text-blue-500">
                Download
              </a>
            </div>
          </li>
          <li class="pl-3 pr-4 py-3 flex items-center justify-between text-sm">
            <div class="w-0 flex-1 flex items-center">
              <PaperClipIcon
                class="flex-shrink-0 h-5 w-5 text-gray-400"
                aria-hidden="true"
              />
              <span class="ml-2 flex-1 w-0 truncate">
                coverletter_back_end_developer.pdf
              </span>
            </div>
            <div class="ml-4 flex-shrink-0">
              <a href="#" class="font-medium text-blue-600 hover:text-blue-500">
                Download
              </a>
            </div>
          </li>
        </ul>
      </dd>
    </div>
  </dl>
</template>

<script lang="ts">
import { Options, Vue } from "vue-class-component";
import { PaperClipIcon } from "@heroicons/vue/solid";
import { currentUrl } from "../modules/url";
import {
  Application,
  applicationsGet,
} from "../modules/api/Paths/applications";
import { Status } from "../modules/api/Status";

@Options({
  components: {
    PaperClipIcon,
  },
})
export default class PageApplicationCheck extends Vue {
  private mortgageId?: number;
  private app: Application = new Application();

  async mounted() {
    this.mortgageId = Number.parseInt(currentUrl.getParam("m_id") ?? "");
    if (isNaN(this.mortgageId)) {
      location.hash = "#";
      location.reload();
    }

    const res = await applicationsGet(this.mortgageId);
    switch (res.status) {
      case Status.OK:
        this.app = res.content;
        break;
      default:
        console.log("error");
    }
  }
}
</script>