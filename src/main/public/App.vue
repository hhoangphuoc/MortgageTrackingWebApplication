<template>
  <div class="flex flex-col h-screen justify-between">
  <Navbar />
  <component :is="page"></component>
  <Footer />
  </div>
</template>

<script lang="ts">
import { Options, Vue } from 'vue-class-component';
import Navbar from './components/navbar/Navbar.vue';
import Footer from './components/Footer.vue';
import { store } from "./modules/store/store";
import { currentUrl } from "./modules/url";
import PleaseLogin from "./pages/pleaseLogin/pleaseLogin.vue";
import { Events } from "./modules/store/Events";
import {pages, page404, loggedout_pages, loggedin_pages, sp_pages, pagePleaseLogin} from './Mapping.vue';
import { userLogout } from "./modules/api/Paths/user";

function inIframe(){
  return top.location != location
}

if (inIframe()){
  document.body.innerHTML = `<div class="error">This page is not available inside an iframe</div>`;
  throw new Error("Not available in Iframe");
}

const allPages = [pages, loggedout_pages, loggedin_pages, sp_pages]
  .flatMap(record => Object.values(record));

const pageComponents = Object.fromEntries(
  allPages.map((page) => [page.name, page])
) as Record<string, typeof Vue>

const title = "Mortages Topicus";

addEventListener(Events.UserChanged, () => {
    console.log("user changed ", store.userData);
    if (store.userData === undefined){
        logout();
    }
});

function logout(){
    userLogout().then(res => {
        if (res.ok){
            console.log("user logged out success fully");
        } else {
            console.error(res);
        }
    })
}

@Options({
  components: {
    Navbar,
    TrackEvent,
    Footer,
    [page404.name]: page404,
    [PleaseLogin.name]: PleaseLogin,
    ...pageComponents
  },
})
export default class App extends Vue {
  page = "main";
  mounted(){
    this.navigate(location.hash);
    addEventListener("hashchange", () => {
      this.navigate(location.hash);
    })
  }
  navigate(hash: string){

    const route = currentUrl.pathname;
    let page: {name:string} = pages[route];
    if (!page){
      const user = store.userData;
      if (user){
        if (user.is_staff){
          page = sp_pages[route];
        } else {
          page = loggedin_pages[route];
        }
      } else {
        page = loggedout_pages[route];
        if (!page && (sp_pages[route] || loggedin_pages[route])){
          page = pagePleaseLogin;
        }
      }
    }
    if (!page){
      page = page404;
    }
    this.page = page.name;
    document.title = `${route || "home"} - ${title}`;
  }
}


</script>

<style>

</style>

