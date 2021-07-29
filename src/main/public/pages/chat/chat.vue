<template>
  <div
    class="outline-2 w-2/3 mx-auto min-h-100 mt-8 flex flex-col justify-between"
  >
    <div class="flex">
      <h1
        class="
          text-2xl
          font-bold
          leading-7
          text-gray-900
          sm:text-3xl
          sm:truncate
          mr-4
        "
      >
        Chat
      </h1>
    </div>
    <p class="text-blue-600" v-if="!noOther">Please wait for a member of our service desk to pick this thread</p>
    <p class="text-green-600">You are connected to a customer service agent</p>

    <div id="mainChat" class="flex flex-col mt-8">
      <ChatMessage
        v-for="message of messages"
        :key="message.chat_id"
        :us="isUs(message.sender_id)"
        :sender="message.sender_name"
        :sent="message.time"
        class="mb-4"
        :img="profilePic(message.profile_img)"
        >{{message.content}}</ChatMessage
      >
      <div class="error">{{socketError}}</div>
    </div>
    <div class="flex align-center min-w-max mx-8 mt-8">
      <textarea
        class="
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
          mr-2
          max-h-10
        "
        type="text"
        v-model="message"
        v-on:keyup.enter="sendMessage()"
      />
      <button
        @click="sendMessage()"
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
        <PaperAirplaneIcon class="h-6 w-6" />
      </button>
    </div>
  </div>
</template>

<script lang='ts'>
import ChatMessage from "../../components/ChatMessage.vue";
import { Options, Vue } from "vue-class-component";
import { PaperAirplaneIcon } from "@heroicons/vue/outline";
import { Menu, MenuButton, MenuItem, MenuItems } from "@headlessui/vue";
import { ChevronDownIcon } from "@heroicons/vue/solid";
import { myFetch } from "../../modules/api/myFetch";
import { store } from "../../modules/store/store";
import { ChatSocket, ChatMessage as IChatMessage } from "../../modules/api/Paths/chat";
import { userGet, UserGet } from "../../modules/api/Paths/user";
import { Prop } from "vue-property-decorator";

class Conversation {
  public id?: number;
  public user_id?: number;
  public assignee_id?: number;
  public other?: UserGet;
}

@Options({
  components: {
    ChatMessage,
    PaperAirplaneIcon,
    Menu,
    MenuButton,
    MenuItem,
    MenuItems,
    ChevronDownIcon,
  },
})
export default class Chat extends Vue {
  messages: Object[] = [];
  message: string = "";
  socketError = "";
  private user = store.userData;
  socket!: ChatSocket;
  conversation: Conversation = new Conversation();

  @Prop()
  client_id?: number

  async getConversation() {
    // conversation is fetched through the client id.
    const idOfClient = this.user?.is_staff ? this.client_id : this.user?.id;
    const res = await myFetch(`./api/chat/conversation/${idOfClient}`);

    if (!res.ok) {
      console.error(res);
      this.socketError = await res.text();
      return;
    }

    return res.json().then(value => {
      console.log(value);
      if (value) {
        this.conversation.id = value.conversation_id;
        this.conversation.user_id = value.user_id;
        this.conversation.assignee_id = value.assignee_id;

        console.log(this.conversation);
      }
    });
  }

  get noOther() {
    return this.conversation.other;
  }

  async getOtherUser() {
    const other_id = this.conversation.user_id === this.user?.id ? this.conversation.assignee_id : this.conversation.user_id;
    console.log(other_id, "other id");

    if (other_id) {
      const other = await userGet(other_id);
      console.log(other);
          
          if (other.ok) {
            this.conversation.other = other.content;
          }
    }
  }

  get otherU_id (){
    return this.conversation.other?.id;
  }

  mounted() {
    this.getConversation().then(() => this.getOtherUser());
    this.getAllMessage();
    
    if (this.user?.id) {
      this.socket = new ChatSocket(this.user?.id, this.otherU_id ?? null, 
        this.onMessage.bind(this), this.onError.bind(this));
    }
  }

  hydrateWsMessage(message, user, other_user) {
    console.log(message);
    return {sender_id: message["from"], content: message["content"]};
  }

  onMessage(message) {
    console.log(message);
    console.log(JSON.parse(message));
    const msg = this.hydrateWsMessage(JSON.parse(message), null, null);
    console.log(msg);
    this.messages.push(msg);
  }

  onError(error: any){
    this.socketError = "Error Conneting to the server: " + error;
  }

  isUs(sender: number): Boolean {
    return sender === this.user?.id;
  }

  sendMessage() {
    console.log("sending message...");
    console.log(this.user, {from: this.user?.id, to: this.otherU_id, content: this.message});
    if (this.user) {
      this.socket.send({from: this.user.id, to: this.otherU_id ?? null, content: this.message});
    }
    this.message = "";
  }

  profilePic(fileId) {
    return `./api/file/file_id/${fileId}`;
  }

  async getAllMessage() {
    let url;
    if (this.user?.is_staff) {
      url = `./api/chat/messages/${this.client_id}`;
    } else {
      url = `./api/chat/messages/${this.user?.id}`;
    }

    let res = await myFetch(url);
    if (!res.ok){
      console.error(res);
      this.socketError = await res.text();
      return;
    }
    res.json().then(value => {
      console.log(value);
      if (res.ok) {
        if (value) {
          this.messages = value;
        }
      } else {
        console.log("error");
      }
    });
  }
}
</script>

<style scoped>

#mainChat {
  overflow-y: auto;
  overflow-x: hidden;
}

textarea {
  width: 100%;
}
</style>