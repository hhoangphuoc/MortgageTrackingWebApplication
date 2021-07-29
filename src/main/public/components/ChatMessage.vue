<template>
  <div class="chat-bubble border border-gray-300 p-4 rounded-lg w-72" :class="!us ? 'self-start chat-left' : 'self-end chat-right'">
    <div class="relative">
      <ProfileImg v-if="us" style="width: 2.5rem; height: 2.5rem;" />
      <img v-if="!us"
        class="
          h-10
          w-10
          rounded-full
          bg-gray-400
          flex
          items-center
          justify-center
          ring-8 ring-white
          mb-2
        "
        :src="img"
        alt=""
      />

      <span
        class="absolute -bottom-0.5 left-6 bg-white rounded-tl px-0.5 py-px"
      >
        <ChatAltIcon class="h-5 w-5 text-gray-400" aria-hidden="true" />
      </span>
    </div>
    <div class="min-w-0 flex-1">
      <div>
        <div class="text-sm">
          <a
            class="font-medium text-gray-900"
            >{{sender}}</a
          >
        </div>
        <p class="mt-0.5 text-sm text-gray-500">
          {{sent}}
        </p>
      </div>
      <div class="mt-2 text-sm text-gray-700">
        <p>
          <slot />
        </p>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { Options, Vue } from "vue-class-component";
import { Prop } from "vue-property-decorator";
import { ChatAltIcon, TagIcon, UserCircleIcon } from '@heroicons/vue/solid'
import ProfileImg from "./ProfileImg.vue";

@Options({components: {
  ChatAltIcon,
  TagIcon,
  UserCircleIcon,
  ProfileImg
}})
export default class ChatMessage extends Vue {
  @Prop()
  private us?: Boolean;

  @Prop()
  private sender?: string;

  @Prop()
  private sent?: string;

  @Prop()
  private img?: string;
}
</script>

<style scoped>
.chat-bubble {
	position: relative;
}

.chat-right {
  margin-right: 2em;
}

.chat-left {
  margin-left: 2em;
}

.chat-left:after {
	content: '';
	position: absolute;
	left: 0;
	top: 10%;
	width: 0;
	height: 0;
	border: 17px solid transparent;
	border-right-color: #D1D5DB;
	border-left: 0;
	border-top: 0;
	margin-top: -8.5px;
	margin-left: -17px;
}

.chat-right:after {
	content: '';
	position: absolute;
	right: 0;
	top: 10%;
	width: 0;
	height: 0;
	border: 17px solid transparent;
	border-left-color: #D1D5DB;
	border-right: 0;
	border-top: 0;
	margin-top: -8.5px;
	margin-right: -17px;
}

</style>
