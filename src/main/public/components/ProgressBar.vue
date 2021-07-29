<template>
    <div class="container">
        <div class="bar" :style="`width:${progress*100}%;`"></div>
        <div class="text" :textContent="message"></div>
    </div>
</template>
<script lang="ts">
    import { Options, Vue } from "vue-class-component";

    @Options({
        props: {
            message: String,
            millis: Number,
        },
    })
    export default class ProgressBar extends Vue {
        message = "loading";
        millis = Infinity;
        startTime = Infinity;
        progress = 0;
        frameHandler!: number;
        sprint = 0.01;
        fake100 = 0.9;
        fakeSpeed = 0.1;
        fakeLeft = 0;
        t = 0;
        
        mounted(){
            this.t = Date.now();
            this.hide();
            this.frameHandler = setInterval(this.onFrame.bind(this), 100);
        }
        unmounted(){
            clearInterval(this.frameHandler);
        }
        start(){
            this.startTime = Date.now();
        }
        hide(){
            (this.$el as HTMLElement).classList.add("transparent");
        }
        unhide(){
            (this.$el as HTMLElement).classList.remove("transparent");
        }
        onFrame(){
            const lastT = this.t;
            this.t = Date.now();
            const dt = this.t - lastT;
            if (this.millis == Infinity){
                if (this.progress >= 1 || this.progress == 0){
                    this.progress = 0;
                    this.fakeLeft = 1 - this.fake100;
                    this.hide();
                }
                if (this.progress != 0){
                    this.progress = Math.min(1,
                        this.progress + dt * this.sprint
                    );
                }
                return;
            } else {
                this.unhide();
            }
            if (this.progress == 0){
                this.startTime = Date.now();
            }

            this.progress = Math.max(0.001, Math.min(1,
                (this.t-this.startTime)/this.millis
            )) * this.fake100;
            if (this.progress == this.fake100){
                this.fakeLeft *= (1-this.fakeSpeed);
                this.progress = 1 - this.fakeLeft;
            }
            if (this.millis == 0){
                this.progress = 1;
            }
        }
    }
</script>
<style scoped>
    .transparent {
        visibility: hidden;
    }
    .container {
        background-color: lightblue;
        position: relative;
        text-align: center;
    }
    .bar {
        position: absolute;
        height: 100%;
        width: 0%;
        background-color: rgba(110, 231, 183, 1);
        top: 0%;
        left: 0%;
    }
    .text {
        position: relative;
    }
</style>
