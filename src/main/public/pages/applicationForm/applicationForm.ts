import { Options, Vue } from "vue-class-component";
import { Status } from "../../modules/api/Status";
import { store } from "../../modules/store/store";
import { currentUrl } from "../../modules/url";
import { formToJson } from "../../modules/util";
import { CheckIcon } from "@heroicons/vue/solid";

@Options({
  components: { CheckIcon },
})
export default class PageApplicationForm extends Vue {
  private steps = [
    { id: 1, name: "Income", href: "#application#1", status: "current" },
    {
      id: 2,
      name: "Financial Obligations",
      href: "#application#2",
      status: "upcoming",
    },
    { id: 3, name: "Collaterals", href: "#application#3", status: "upcoming" },
    {
      id: 4,
      name: "Mortgage Information",
      href: "#application#4",
      status: "upcoming",
    },
  ];

  private hasPartner = null;
  private hasStudentDebt = null;
  private hasAlimony = null;
  private hasOtherDebt = null;
  private hasCredits = null;

  _page = 1;
  form!: HTMLFormElement;
  pages!: HTMLCollectionOf<HTMLElement>;
  dots!: HTMLCollectionOf<HTMLElement>;
  hashChangeHandler!: () => void;
  error = "";

  mounted() {
    if (!(this.$el instanceof HTMLElement)) {
      throw new Error("element not initialized");
    }

    this.pages = this.$el.getElementsByClassName("page") as any;
    this.dots = this.$el.getElementsByClassName("dot") as any;
    this.page = Number.parseInt(currentUrl.hash) || 1;
    this.hashChangeHandler = this.onHashChance.bind(this);
    addEventListener("hashchange", this.hashChangeHandler);

    //handle the next, prev buttons:
    for (const next of this.$el.getElementsByClassName("next")) {
      next.addEventListener("click", this.onNext.bind(this));
    }
    for (const prev of this.$el.getElementsByClassName("prev")) {
      prev.addEventListener("click", this.onPref.bind(this));
    }
    this.form = this.$el.querySelector("form") as any;
    this.form.addEventListener("submit", this.onSubmit.bind(this));
  }

  unmounted() {
    removeEventListener("hashchange", this.hashChangeHandler);
  }

  //moving application pages forward and backward:
  onHashChance() {
    this.page = Number.parseInt(currentUrl.hash) || 1;
    for (let step of this.steps) {
      if (step.id === this.page) {
        step.status = "current";
      } else if (step.id > this.page) {
        step.status = "upcoming";
      } else {
        step.status = "complete";
      }
    }
  }
  onNext(event: Event) {
    if (this.form.checkValidity()) {
      event.preventDefault();
      this.page++;
      currentUrl.hash = "" + this.page;
    }
  }
  onPref(event: Event) {
    event.preventDefault();
    this.page--;
    currentUrl.hash = "" + this.page;
  }

  // submit and POST the form to server
  onSubmit(event: Event) {
    event.preventDefault();

    const user = store.userData;
    if (user == undefined) {
      location.hash = "";
      return;
    }


    const data = formToJson(this.form);
    const customerID = user.id;
    data.c_id = customerID.toString();

    //Post the application data to the server via xmlhttpPost.

    const applicationUrl = "./api/applications";

    const xmlhttpPost = new XMLHttpRequest();
    xmlhttpPost.open("POST", applicationUrl, true);

    if (data != null) {
      xmlhttpPost.setRequestHeader("Content-Type", "application/json");
      xmlhttpPost.send(JSON.stringify(data));
    } else {
      xmlhttpPost.send();
    }
    this.error = "";
    xmlhttpPost.onreadystatechange = () => {
      if (xmlhttpPost.readyState == 4) {
        switch (xmlhttpPost.status) {
          case Status.ACCEPTED:
            location.hash = "";
            break;
          case Status.BAD_REQUEST:
            this.error = xmlhttpPost.statusText;
            break;
          default:
            this.error = "unexpected response: " + xmlhttpPost.statusText;
            break;
        }
      }
    };
  }

  get page() {
    return this._page;
  }

  set page(n: number) {
    n = Math.max(1, Math.min(this.pages.length, n));
    for (let i = 0; i < this.pages.length; i++) {
      const page = this.pages[i] as HTMLElement;
      page.style.display = i == n - 1 ? "" : "none";
    }
    for (let i = 0; i < this.dots.length; i++) {
      const dot = this.dots[i];
      if (i == n - 1) {
        dot.classList.add("active");
      } else {
        dot.classList.remove("active");
      }
    }
    this._page = n;
  } 
}
