<template>
    <img
      class="inline-block h-14 w-14 rounded-full"
      :src="imgSrc"
      alt="Profile Picture"
    />
</template>
<script lang="ts">
import { Vue } from "vue-class-component";
import { Events } from "../modules/store/Events";
import { store } from "../modules/store/store";

const defaultProfile = "https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=facearea&facepad=2&w=256&h=256&q=80";

export default class ProfileImg extends Vue {
    imgSrc = defaultProfile;
    mounted(){
        this.loadPicture();
        addEventListener(Events.UserChanged, this._loadPicture);
    }
    unmounted(){
        removeEventListener(Events.UserChanged, this._loadPicture);
    }
    _loadPicture = this.loadPicture.bind(this);
    loadPicture(){
        const user = store.userData;

        const img_id = user?.profile_img;
        this.imgSrc = !img_id ? defaultProfile : `./api/file/file_id/${img_id}`;
    }
}

</script>
