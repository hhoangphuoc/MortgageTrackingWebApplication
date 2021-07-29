<template>
  <div class="border-2 rounded mx-auto p-6">
    <ProfileImg />
    <form
      method="POST"
      enctype="multipart/form-data"
      @submit.prevent="uploadFile()"
    >
      <label class="block text-sm font-medium text-gray-700" for="profile"
        >choose a new profile picture</label
      >
      <input type="number" name="doc_id" value="0" hidden>
      <input
        type="file"
        name="file"
        v-on:change="chooseFile"
      />
      <button type="submit">Upload</button>
    </form>
  </div>
</template>

<script lang="ts">
import { Options, Vue } from "vue-class-component";
import ProfileImg from "../../components/ProfileImg.vue";
import { Method, myFetch } from "../../modules/api/myFetch";
import { UserGet, userGet } from "../../modules/api/Paths/user";
import { Status } from "../../modules/api/Status";
import { store } from "../../modules/store/store";

@Options({
  components: {
    ProfileImg
  }
})
export default class Profile extends Vue {
  private user = store.userData;

  async uploadFile() {
    if (this.user) {
      const url = `api/file/profile/${this.user?.id}`;
      console.log(url);
      const form = document.querySelector("form") as HTMLFormElement;
      const res = await myFetch(url, {
        method: Method.POST,
        body: new FormData(form),
      });
      if (!res.ok) {
        console.error(res);
        return;
      }
      await this.updateUser(this.user.id);
    }
  }
  async updateUser(u_id: number){
    const res = await userGet(u_id);
    switch (res.status){
      case Status.OK: break;
      default: {
        console.error(res);
      } return;
    }

    store.userData = res.content;
  }
}
</script>

<style>
</style>
