import { ref } from 'vue'
import { Disclosure, DisclosureButton, DisclosurePanel, Menu, MenuButton, MenuItem, MenuItems } from '@headlessui/vue'
import { BellIcon, MenuIcon, XIcon } from '@heroicons/vue/outline'
import { Options, Vue } from "vue-class-component";
import { store } from "../../modules/store/store";
import { currentUrl } from "../../modules/url";
import { Events } from '../../modules/store/Events';
import ProfileImg from '../ProfileImg.vue';

interface NavLink {
  name: String,
  href: String,
  active: Boolean,
  enabled: Boolean,
}

@Options({
  components: {
    Disclosure,
    DisclosureButton,
    DisclosurePanel,
    Menu,
    MenuButton,
    MenuItem,
    MenuItems,
    BellIcon,
    MenuIcon,
    XIcon,
    ProfileImg
  }
})
export default class Navbar extends Vue {
  private navigation : Array<NavLink> = [
      { name: 'Home', href: '#', active: true, enabled: true },
      { name: 'Chat', href: '#chat', active: false, enabled: false },
      { name: 'Application', href: '#application', active: false, enabled: false },
    ];
    private user = store.userData;

    private loggedIn = false;

    get enabledNavbar() {
      return this.navigation.filter(item => item.enabled)
    }

    mounted() {
      const open = ref(false);
      addEventListener("hashchange", this.onNavigate.bind(this));
      // set the navbar to the correct setting.
      this.setupNavbar();

      // change the navbar when the user object changes.
      addEventListener(Events.UserChanged, () => {
        this.setupNavbar();
      });
      return {
        open,
      };
    }

    setupNavbar() {
      if (store.userData) {
        if (!store.userData.is_staff) {
          this.navigation[2].enabled = true;
        }
        this.loggedIn = true;
        this.navigation[1].enabled = true;
      } else {
        this.loggedIn = false;
        this.navigation[1].enabled = false;
        this.navigation[2].enabled = false;
      }
    }


    logout() {
        store.userData = undefined;
        location.hash = "";
        location.reload();
    }

    onNavigate() {
      let hash = "#" + currentUrl.pathname;


      for (const link of this.navigation) {
        if (link.href === hash) {
          link.active = true;
        } else {
          link.active = false;
        }
      }
    }
  }
