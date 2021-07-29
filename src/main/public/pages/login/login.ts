import { Options, Vue } from "vue-class-component";
import { userLoginPost } from "../../modules/api/Paths/user";
import {formToJson} from "../../modules/util";
import ProgressBar from "../../components/ProgressBar.vue";
import { Status } from "../../modules/api/Status";
import { store } from "../../modules/store/store";

@Options({
    components: {
        ProgressBar
    }
})
export default class PageLogin extends Vue {
    static tagName = "PageLogin";
    form!: HTMLFormElement;
    error = "";
    expectedLoginDelay = 3000;
    millis = Infinity;
    mounted(){
        if (!(this.$el instanceof HTMLElement)){
            throw new Error("element not initialized");
        }
        this.form = this.$el.getElementsByTagName("form")[0] as HTMLFormElement;
        this.form.addEventListener("submit", this.onLogin.bind(this));
    }
    
    async onLogin(event: Event){
        event.preventDefault();
        const data = new FormData(this.form);
        const email = data.get("email") as string;
        const password = data.get("password") as string;
        console.log(formToJson(this.form));
        this.error = "";
        this.millis = this.expectedLoginDelay;
        const response = await userLoginPost({email, password});
        this.millis = Infinity;
        switch (response.status){
            case Status.OK: break;
            case Status.UNAUTHORIZED:
                this.error = "password incorrect";
            return;
            case Status.NOT_FOUND:
                this.error = "email does not exist";
            return;
            default: {
                console.error(response);
                this.error = response.statusText;
            } return;
        }

        const user = response.content;

        store.userData = user;

        console.log(response.ok, response.status, response.statusText, "success");
        location.href = "#";
        
    }
}
