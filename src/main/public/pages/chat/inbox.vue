<template>
  <Chat v-if="!user.is_staff"/>
  <div v-else>
    <div id="container-box" class="flex mx-auto w-3/4">
      <div class="flex border-2">
        <div class="border-r-2 border-dashed p-4">
        <p class="text-lg font-bold text-gray-600 text-center">Unassigned Chats</p>
        <ul>
          <li class="relative cursor-pointer" v-for="(inbox, index) of unassignedChats" @click="moveToAssigned(index)" :key="index">
            <Contact :inbox="inbox" />
            <span class="arr-right"><ArrowCircleRightIcon class="w-6 h-6 text-green-400"/></span>
          </li>
        </ul>
        </div>
        <div class="p-4">
        <p class="text-lg font-bold text-gray-600 text-center">My Chats</p>
        <ul>
          <li v-for="(inbox, index) of assignedChats" @click="openChat(inbox.user_id)" class="cursor-pointer" :key="index">
            <Contact :inbox="inbox" />
          </li>
        </ul>
        </div>
      </div>
      <Chat class="ml-8" v-if="chatWith !== 0" :client_id="chatWith" />
      <div id="filler" v-else class="ml-8 border-2 border-dashed p-8">
        <div id="instructions" class="mx-auto text-2xl text-gray-600">
          Click A Chat To Start
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { Options, Vue } from "vue-class-component"
import { store } from "../../modules/store/store";
import Chat from "./chat.vue";
import Contact from "./contact.vue";
import { ArrowCircleRightIcon } from "@heroicons/vue/outline";
import { Method, myFetch } from "../../modules/api/myFetch";

@Options({
  components: {
    Chat,
    Contact,
    ArrowCircleRightIcon
  }
})
export default class Inbox extends Vue {
  private user = store.userData;
  private assignedChats: any[] = [];
  private unassignedChats: any[] = [];
  private selectedChat = {id: 0};


  get chatWith() {
    return this.selectedChat.id;
  }

  private moveToAssigned(index) {
    console.log("moving to assigned");
    if (this.unassignedChats[index]) {
      myFetch(`./api/chat/conversation/${this.unassignedChats[index].conversation_id}/${this.user?.id}`, {method: Method.PUT})
    }
    this.assignedChats.push(this.unassignedChats.splice(index, 1));
  }

  private openChat(userId) {
    this.selectedChat.id = userId;
    console.log("opening chat", this.selectedChat.id);
  }

  mounted() {
    if (this.user?.is_staff) {
      this.fetchInboxes();
    }
  }

  async fetchInboxes() {
    const res = await myFetch(`./api/chat/inbox/${this.user?.id}`);
    if (!res.ok){
      return;
    }
    res.json().then(value => {
      console.log(value);
      if (res.ok) {
        if (value) {
          this.assignedChats = value.assigned[0];
          this.unassignedChats = value.unassigned[0];
        }
      } else {
        console.log("error");
      }
    });
  }
}
</script>

<style scoped>
  #container-box {
    margin-top: 5vh;
    max-height: 75vh;
    height:  75vh;
  }
  .arr-right {
    display: none;
    position: absolute;
    top: 1.5em;
    right: 12px;
    transition: 0.2s ease-in;
  }

  li:hover .arr-right {
    display: block;
  }

  #filler {
    width: inherit;
  }

  #instructions {
    width: fit-content;
    margin-top: 50%;
  }
</style>