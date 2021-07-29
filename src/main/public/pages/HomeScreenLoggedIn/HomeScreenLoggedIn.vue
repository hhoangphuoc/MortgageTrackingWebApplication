<template>
<div  id="homeScreenLoggedIn" class="flex text-center mx-auto">

<a class="myMortgagePage" id="myMortgagePage" href="#mymortgage">
<div class="border-4 border-gray-900 rounded mr-8 p-8 shadow">
	<img src="https://ryobe3d.co.uk/wp-content/uploads/2016/09/home.png" alt="House image" width="300">
	<p class="text-xl font-semibold">My Mortgages</p>
</div>
</a>

<a class="toApplicationForm" id="toApplicationForm" href="#application">
	<div class="border-4 border-gray-900 rounded ml-8 p-8 shadow">
		<img src="https://i.pinimg.com/564x/0f/25/28/0f25286c2739ff70299a77356d5cfbc9.jpg" alt="House image" width="300">
		<p class="text-xl font-semibold">Apply for a mortgage</p>
	</div>
</a>

</div>
</template>

<script lang="ts">
import { Vue } from "vue-class-component";

export default class HomeScreenLoggedIn extends Vue {
	static tagName ="HomeScreenLoggedIn";
}
</script>

<style>
</style>
