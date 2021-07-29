import { Vue } from "vue-class-component";
import Emitter from "./Emitter";

export default class Confetti extends Vue {
    active = false;
    base!: HTMLElement;
    emitter!: Emitter;
    frameHandle!: (t: number) => void;
    t = 0;
    mounted(){
        this.active = true;
        this.base = this.$el;
        this.emitter = new Emitter(this.base);
        this.frameHandle = this.onFrame.bind(this);
        requestAnimationFrame(this.frameHandle);
    }
    unmounted(){
        this.active = false;
    }
    onFrame(t: number){
        const dt = this.t == 0 ? 0 : t - this.t;
        this.t = t;
        this.emitter.update(dt/1500);
        if (this.active){
            requestAnimationFrame(this.frameHandle);
        }
    }
}
