<template>
<div>

</div>
</template>
<script>
    import { Options, Vue } from "vue-class-component";
    import { Prop, Watch } from "vue-property-decorator";

    export default class JSON extends Vue {
        @Prop()
        url = "about:blank";
        @Watch("url")
        onUrlChange(){
            console.log(this.url);
        }
    }
</script>
<style scoped>
</style>
