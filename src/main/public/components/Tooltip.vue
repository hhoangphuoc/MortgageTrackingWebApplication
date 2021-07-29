<template>
  <div @mousedown="setOpen(true)" @mouseup="setOpen(false)" class="tooltip-box">
    <button id="activate">
      <slot />
    </button>
    <div
      class="tooltip"
      :class="open ? 'opaque' : ''"
    >
      <span
        class="text"
      >{{ text }}</span>
    </div>
  </div>
</template>

<script lang="ts">
import {Options, Vue} from "vue-class-component";
import {Prop, Watch} from "vue-property-decorator";


export default class MortgageProcessSP extends Vue {
  @Prop()
  private text!: String;

  private open = false;

  private setOpen(value: boolean) {
    this.open = value;
  }
}

</script>

<style scoped>
.tooltip-box { 
  position: relative;
  display: inline-block;
}

button:focus {
    outline: 0;
}

.opaque {
  visibility: visible !important;
}

.tooltip { 
  color: #ffffff;
  text-align: center;
  padding: 5px 0;
  border-radius: 2px;
  
  margin-top: 10px;
  width: 120px;
  top: 100%;
  left: 50%;
  margin-left: -60px;

  visibility: hidden;
  transition: opacity 0.5s;

  position: absolute;
  z-index: 1;

  background: #3B82F6;
}

.text::after {
  content: " ";
  position: absolute;
  bottom: 100%;
  left: 50%;
  margin-left: -5px;
  border-width: 5px;
  border-style: solid;
  border-color: #3B82F6 transparent transparent transparent;
}
</style>
