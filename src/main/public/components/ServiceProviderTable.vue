<template>
  <div>
    <div>
      <div class="sm:hidden">
        <label for="tabs" class="sr-only">Select a tab</label>
        <select
          id="tabs"
          name="tabs"
          class="
            block
            w-full
            pl-3
            pr-10
            py-2
            text-base
            border-gray-300
            focus:outline-none
            focus:ring-blue-500
            focus:border-blue-500
            sm:text-sm
            rounded-md
          "
        >
          <option v-for="tab in tabs" :key="tab.name" :selected="tab.current">
            {{ tab.name }}
          </option>
        </select>
      </div>
      <div class="hidden sm:block">
        <div class="border-b border-gray-200">
          <nav class="-mb-px flex space-x-8" aria-label="Tabs">
            <a
              v-for="tab in tabs"
              :key="tab.name"
              :href="tab.href"
              :class="[
                tab.current
                  ? 'border-blue-500 text-blue-600'
                  : 'border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-200',
                'whitespace-nowrap flex py-4 px-1 border-b-2 font-medium text-sm',
              ]"
              :aria-current="tab.current ? 'page' : undefined"
            >
              {{ tab.name }}
              <span
                :class="[
                  tab.current
                    ? 'bg-blue-100 text-blue-600'
                    : 'bg-gray-100 text-gray-900',
                  'hidden ml-3 py-0.5 px-2.5 rounded-full text-xs font-medium md:inline-block',
                ]"
                >{{ tab.count }}</span
              >
            </a>
          </nav>
        </div>
      </div>
      <div class="m-4 border border-gray-200 rounded w-4/4">
        <input
          type="text"
          class="min-w-full block border-0 rounded"
          placeholder="Search or filter results..."
          v-model="filter"
        />
      </div>
    </div>
    <div class="-my-2 overflow-x-auto sm:-mx-6 lg:-mx-8">
      <div class="py-2 align-middle inline-block min-w-full sm:px-4 lg:px-8">
        <div
          class="shadow overflow-hidden border-b border-gray-200 sm:rounded-lg"
        >
          <div class="flex flex-col">
            <table id="SPTable" class="min-w-full divide-y divide-gray-200">
              <thead class="bg-gray-50">
                <tr>
                  <th
                    v-for="col in columns"
                    scope="col"
                    class="
                      px-4
                      py-3
                      text-left text-xs
                      font-medium
                      text-gray-500
                      uppercase
                      tracking-wider
                    "
                    :key="col.name"
                    @click="toggleOrdering(col.name)"
                  >
                  <div class="flex">
                    <div v-if="col.sort" class="w-4 h-4 mr-2">
                      <ChevronUpIcon v-if="col.sort_direction == myEnum" />
                      <ChevronDownIcon v-else />
                    </div>
                    <span>
                    {{ col.name }}
                    </span>
                  </div>
                  </th>
                  <th scope="col" class="relative px-4 py-3">
                    <span class="sr-only">Action</span>
                  </th>
                </tr>
              </thead>
              <tbody class="bg-white divide-y divide-gray-200">
                <tr
                  v-for="mortgage in filteredRows"
                  :key="mortgage.m_id"
                >
                  <td
                    class="
                      px-4
                      py-2
                      whitespace-nowrap
                      text-sm
                      font-medium
                      text-gray-900
                    "
                  >
                    {{ mortgage.m_id }}
                  </td>
                  <td
                    class="
                      px-4
                      py-2
                      whitespace-nowrap
                      text-sm
                      font-medium
                      text-gray-900
                    "
                  >
                    {{ mortgage.date }}
                  </td>
                  <td 
                    class="
                      px-4
                      py-2
                      whitespace-nowrap
                      text-sm
                      font-medium
                      text-gray-900
                    ">
                    {{ mortgage.c_id }}
                  </td>
                  <td class="px-4 py-2 whitespace-nowrap text-sm text-gray-500">
                    {{ `${mortgage.name} ${mortgage.surname}` }}
                  </td>
                  <td class="px-4 py-2 whitespace-nowrap text-sm text-gray-500">
                    <span
                      class="
                        px-2
                        inline-flex
                        text-xs
                        leading-5
                        font-semibold
                        rounded-full
                      "
                      :class="stateClasses(mortgage?.state)"
                    >
                      {{ mortgage.state }}
                    </span>
                  </td>
                  <td class="px-4 py-2 whitespace-nowrap text-sm text-gray-500">
                    <span class="px-4 py-2 whitespace-nowrap font-medium text-sm" :class="statusClasses(mortgage?.status)">
                      {{ mortgage.status }}
                    </span>
                  </td>
                  <td
                    class="
                      px-4
                      py-2
                      whitespace-nowrap
                      text-right text-sm
                      font-medium
                    "
                  >
                    <button
                      @click="cancelMortgage(mortgage)"
                      class="text-red-600 mr-4"
                      id="deleteButton"
                    >
                      Delete
                    </button>
                    <a
                      :href="`#tracktraceSP?id=${mortgage.m_id}`"
                      class="text-blue-600 hover:text-blue-900"
                      id="viewButton"
                      >View</a
                    >
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { Vue, Options } from "vue-class-component";
import { Method, myFetch } from "../modules/api/myFetch";
import { Status } from "../modules/api/Status";
import { store } from "../modules/store/store";
import { getServiceProviderMortgages, Mortgage, MortgageData} from "../modules/api/Paths/mortgageRequest";
import { Watch } from "vue-property-decorator";
import { currentUrl } from "../modules/url";
import { ChevronUpIcon, ChevronDownIcon } from "@heroicons/vue/outline";
import { lowercaseObject, findInObject } from "../modules/util";
import { MediaType } from "../modules/api/MediaType";

function dynamicSort(property) {
    let sortOrder = 1;
    if (property[0] === "-") {
        sortOrder = -1;
        property = property.substr(1);
    }

    return function (a,b) {
        var result = (a[property] < b[property]) ? -1 : (a[property] > b[property]) ? 1 : 0;
        return result * sortOrder;
    }
}

function dynamicSortMultiple(...args) {
    const props = arguments;

    return function (obj1: any, obj2: any) {
        let i = 0, result = 0;
        const numberOfProperties = props.length;

        while (result === 0 && i < numberOfProperties) {
            result = dynamicSort(props[i])(obj1, obj2);
            i++;
        }

        return result;
    }
}

class MyArray<T> extends Array<T> {
    sortBy(...args: string[]) {
        return this.sort(dynamicSortMultiple(...args));
    }
}

/**
 * Enum that defines desc and asc sorting.
 */
enum SortDirection {
  DESC,
  ASC
}

/**
 * represents a column of the table.
 */
class Column {
  private name: string;
  private tag: string;
  private sort: Boolean;
  private sort_direction: SortDirection;

  constructor(name, tag, sort, sort_direction = SortDirection.ASC) {
    this.name = name;
    this.tag = tag;
    this.sort = sort;
    this.sort_direction = sort_direction;
  }

  get getName() {
    return this.name;
  }

  get isSorted() {
    return this.sort;
  }

  get getTag() {
    return this.tag;
  }

  get getSortDirection() {
    return this.sort_direction;
  }

  toggleOrdering() {
    if (!this.sort) {
      this.sort = true;
      this.sort_direction = SortDirection.ASC;
      return;
    }

    if (this.sort_direction === SortDirection.ASC) {
      this.sort_direction = SortDirection.DESC;
      return;
    }

    if (this.sort_direction === SortDirection.DESC) {
      this.sort = false;
    }
  }
}

/**
 * Component Class.
 */
@Options({
  components: {
    ChevronUpIcon,
    ChevronDownIcon,
  },
})
export default class PageApplicationsOverview extends Vue {
  static tagName = "PageApplicationsOverview";
  private filter: string = "";
  private mortgages: MortgageData[] = [];
  private completed: MortgageData[] = [];
  private open: MortgageData[] = [];
  private columns: Column[] = [
    new Column("Mortgage ID", "m_id", true),
    new Column("Start Date", "start_date", false),
    new Column("Customer ID", "c_id", false),
    new Column("Customer Name", "customer", false),
    new Column("State", "state", false),
    new Column("Status", "status", false),
  ];
  private myEnum: SortDirection = SortDirection.DESC;
  private mortgageBucket: MyArray<MortgageData> = new MyArray();

  private tabs = [
    { name: "Applied", href: "#?tab=applied", count: 0, current: true },
    { name: "Closed", href: "#?tab=closed", count: 0, current: false },
    { name: "All", href: "#?tab=all", count: 0, current: false },
  ];

  private user?;

  @Watch("filter")
  makeTags() {
    this.columns.forEach((col) => {
      if (this.filter.includes(col.getName)) {
        console.log("found taht bit");
      }
    });
  }

  mounted() {
    addEventListener("hashchange", this.tab.bind(this)); // naviagation for the table.
    
    this.user = store.userData;
    if (!this.user) {
      location.hash = "";
      return;
    }

    this.fetchMortgages();
  }

  async fetchMortgages() {
    getServiceProviderMortgages(this.user.id).then(res => {
      if (res.ok) {
        this.mortgages = res.content;
        for (const m of this.mortgages) {
          const state = m.state.toLowerCase();
          if (state === "done" || state === "archived") {
            this.completed.push(m);
          } else {
            this.open.push(m);
          }
        }
        this.tabs[0].count = this.open.length;
        this.tabs[1].count = this.completed.length;
        this.tabs[2].count = this.mortgages.length;
        this.setMortgageBucket(currentUrl.getParam("tab"));
      } else {
        console.log(res.content);
      }
    });
  }

  setMortgageBucket(tab) {
    switch(tab) {
      case "applied":
        this.mortgageBucket.splice(0, this.mortgageBucket.length);
        for (const m of this.open) {
          this.mortgageBucket.push(m);
        }
        break;
      case "closed":
        this.mortgageBucket.splice(0, this.mortgageBucket.length);
        for (const m of this.completed) {
          this.mortgageBucket.push(m);
        }
        break;
      default:
        this.mortgageBucket.splice(0, this.mortgageBucket.length);
        for (const m of this.mortgages) {
          this.mortgageBucket.push(m);
        }
    }
  }

  toggleOrdering(name: string) {
    for (const column of this.columns) {
      if (column.getName === name) {
        column.toggleOrdering();
        this.mortgageBucket.sortBy(...this.sortArray);
      }
    }
  }

  tab() {
    const tab = currentUrl.getParam("tab");
    this.tabs.forEach((item) => {
      if (item.name.toLowerCase() === tab) {
        item.current = true;
        this.setMortgageBucket(tab);
      } else {
        item.current = false;
      }
    });
  }

  get filteredRows() {
    if (!this.filter) {
      return this.mortgageBucket;
    }

    if (this.mortgageBucket) {
      return this.mortgageBucket.filter((mortgage) => {
        const obj = lowercaseObject(mortgage);

        return findInObject(obj, this.filter.toLowerCase());
      });
    }

    return null;
  }

  get sortArray(): string[] {
    const arr: string[] = []
    for (const col of this.columns) {
      if (col.isSorted) {
        arr.push(`${col.getSortDirection === SortDirection.ASC ? "" : "-"}${col.getTag}`);
      }
    }

    return arr;
  }

  stateClasses(state: String): String {
    switch (state) {
      case "Application":
      case "Done":
        return "bg-green-100 text-green-800";
      case "Delete Request":
        return "bg-red-100 text-red-800";
      case "Interest Offer":
      case "Binding Offer":
        return "bg-blue-100 text-blue-800";  
      default:
        return "bg-gray-100 text-gray-800";
    }
  }

  statusClasses(status: String): String {
    switch (status) {
      case "Finished":
        return "text-green-600";
      case "Action Required":
        return "text-yellow-600";
      case "Processing": 
        return "text-blue-600"; 
      case "Failed":
        return "text-red-600"
      default:
        return "bg-gray-100 text-gray-800";
    }
  }

  async cancelMortgage(mortgage: Mortgage) {
    const cf = confirm("Are you sure you want to delete mortgage ?");
    if (!cf) {
      return;
    }
    const mortgageListUrl = "./api/mortgagerequests/";

    const mortgageURLById = mortgageListUrl + mortgage.c_id + "/" + mortgage.m_id;

    const res = await myFetch(mortgageURLById, {
      method: Method.DELETE,
      headers: {
        "Content-Type": MediaType.APPLICATION_JSON,
      },
    });

    switch (res.status) {
      case Status.NO_CONTENT:
      case Status.OK:
        {
          window.location.reload();
        }
        break;
      default: {
        console.error("unexpected response: " + res.statusText);
      }
    }
  }
}
</script>

<style>
</style>
