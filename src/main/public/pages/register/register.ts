import { Vue } from "vue-class-component";
import { userRegisterPost } from "../../modules/api/Paths/user";
import { Status } from "../../modules/api/Status";

interface RegistrationData {
    firstName: String,
    lastName: String,
    email: String,
    password: String,
    confirmPassword: String
}

export default class Register extends Vue {
    static tagName = "Register";
    message = "";
    private registerData: RegistrationData = {
        firstName: '',
        lastName: '',
        email: '',
        password: '',
        confirmPassword: '',
    };

    async register() {
        const response = await userRegisterPost(this.registerData);
        switch (response.status) {
            case Status.CREATED:
                console.log("register success");
                return this.success();
            case Status.BAD_REQUEST:
                console.error(response.statusText, response.content);
                break;
            default:
                console.error("got an unexpected response from the server");
                console.error(response.neverStatus, response.statusText, response.content);
        }

        this.message = "Failed to register, Please try again.";
        this.resetForm();
    }

    resetForm() {
        for (let s in this.registerData) {
            if (this.registerData.hasOwnProperty(s)) {
                this.registerData[s] = '';
            }
        }
    }

    success() {
        location.hash = "login";
    }
}
