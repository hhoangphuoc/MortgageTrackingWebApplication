<template>
  <div class="flex mx-auto items-center mt-8 flex-col">
  <h1 class="text-4xl tracking-tight font-extrabold text-gray-900 sm:text-5xl md:text-6xl text-center">
    <span class="block xl:inline">Mortgages</span>
    {{ ' ' }}
    <span class="block text-blue-600 xl:inline">Made Easy</span>
  </h1>
  <p class="block text-gray-600 mt-4 font-bold">Powered by</p>
  <img
    id="topicusLogo"
    class="mb-4"
    src="../../assets/logo-square.svg"
    alt="Topicus logo"
    width="125"
  />
  </div>

    <div class="py-4 bg-white">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
      <div class="lg:text-center">
        <h2 class="text-base text-blue-600 font-semibold tracking-wide uppercase">Mortgage Features</h2>
        <p class="mt-2 text-3xl leading-8 font-extrabold tracking-tight text-gray-900 sm:text-4xl">
          A better way to take out a mortgage
        </p>
        <p class="mt-4 max-w-2xl text-xl text-gray-500 lg:mx-auto">
          Hello! this web app helps you apply
    for a mortgage responsibly and then guides you through the process.
    Purchase the house of your dreams in an easy streamlined way.
        </p>
      </div>

      <div class="mt-10">
        <dl class="space-y-10 md:space-y-0 md:grid md:grid-cols-2 md:gap-x-8 md:gap-y-10">
          <div v-for="feature in features" :key="feature.name" class="relative">
            <dt>
              <div class="absolute flex items-center justify-center h-12 w-12 rounded-md bg-blue-500 text-white">
                <component :is="feature.icon" class="h-6 w-6" aria-hidden="true" />
              </div>
              <p class="ml-16 text-lg leading-6 font-medium text-gray-900">{{ feature.name }}</p>
            </dt>
            <dd class="mt-2 ml-16 text-base text-gray-500">
              {{ feature.description }}
            </dd>
          </div>
        </dl>
      </div>
    </div>
  </div>

    <div class="max-w-7xl mx-auto py-16 px-4 sm:px-6 lg:px-8">
      <div class="bg-blue-700 rounded-lg shadow-xl overflow-hidden lg:grid lg:grid-cols-2 lg:gap-4">
        <div class="pt-10 pb-12 px-6 sm:pt-16 sm:px-16 lg:py-16 lg:pr-0 xl:py-20 xl:px-20">
          <div class="lg:self-center">
            <h2 class="text-3xl font-extrabold text-white sm:text-4xl">
              <span class="block">Ready to buy your house?</span>
              <span class="block">Start your account now.</span>
            </h2>
            <p class="mt-4 text-lg leading-6 text-blue-200">Spend more time on the important stuff: setting up for your new life and less time on paperwork. Get the champagne and confetii ready 🎉</p>
            <a href="#register" class="mt-8 bg-white border border-transparent rounded-md shadow px-5 py-3 inline-flex items-center text-base font-medium text-blue-600 hover:bg-blue-50">Sign up for free</a>
          </div>
        </div>
        <div class="-mt-6 aspect-w-5 aspect-h-3 md:aspect-w-2 md:aspect-h-1">
          <img class="transform translate-x-6 translate-y-6 rounded-md object-cover object-left-top sm:translate-x-16 lg:translate-y-20" src="../../public/mortgage.jpeg" alt="App screenshot" />
        </div>
      </div>
    </div>
</template>

<script lang="ts">
import { Vue } from "vue-class-component";
import { AnnotationIcon, CubeTransparentIcon, ClockIcon, ClipboardCheckIcon } from '@heroicons/vue/outline';  

export default class PageStartScreen extends Vue {
  static tagName = "PageStartScreen";
  private features: Array<Object> = [
    {
      name: 'Progress Bar Interface',
      description:
        'Easily get an overview of the progress of your application at any point in time. When you create an application there is a progress bar you can follow.',
      icon: ClipboardCheckIcon,
    },
    {
      name: 'Time Estimation',
      description:
        'Get the estimated time of completion of steps along the process for an easier planning and overview.',
      icon: ClockIcon,
    },
    {
      name: 'Data Transparency',
      description:
        'Your data can be removed from the application, if your mortgage application has concluded.',
      icon: CubeTransparentIcon,
    },
    {
      name: 'Chat',
      description:
        'Chat directly with the bank for personal feedback. No headaches when you don\'t understand a step of the process.',
      icon: AnnotationIcon,
    },
  ];
}
</script>

<style>
#topicusLogo {
  display: block;
  margin-left: auto;
  margin-right: auto;
  user-select: none;
  user-drag: none;
  -moz-user-select: none;
  -webkit-user-drag: none;
  -webkit-user-select: none;
  -ms-user-select: none;
}

#text {
  text-align: center;
  font-size: 18px;
}

</style>
