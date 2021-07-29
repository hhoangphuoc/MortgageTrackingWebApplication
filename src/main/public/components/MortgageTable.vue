<template>
  <div v-if="mortgages.length === 0" class="mortgageResult">
    <p id="mortgageTable">Empty Storage! Apply for Mortgage First</p>
  </div>
  <div v-else class="flex flex-col">
    <div class="-my-2 overflow-x-auto sm:-mx-6 lg:-mx-8">
      <div class="py-2 align-middle inline-block min-w-full sm:px-6 lg:px-8">
        <div
          class="shadow overflow-hidden border-b border-gray-200 sm:rounded-lg"
        >
          <table id="mortgage-table" class="min-w-full divide-y divide-gray-200">
            <thead class="bg-gray-50">
              <tr>
                <th
                  scope="col"
                  class="
                    px-6
                    py-3
                    text-left text-xs
                    font-medium
                    text-gray-500
                    uppercase
                    tracking-wider
                  "
                >
                  Mortgage ID
                </th>
                <th
                  scope="col"
                  class="
                    px-6
                    py-3
                    text-left text-xs
                    font-medium
                    text-gray-500
                    uppercase
                    tracking-wider
                  "
                >
                  Date
                </th>
                <th
                  scope="col"
                  class="
                    px-6
                    py-3
                    text-left text-xs
                    font-medium
                    text-gray-500
                    uppercase
                    tracking-wider
                  "
                >
                  Staff ID
                </th>
                <th
                  scope="col"
                  class="
                    px-6
                    py-3
                    text-left text-xs
                    font-medium
                    text-gray-500
                    uppercase
                    tracking-wider
                  "
                >
                  Process State
                </th>
                <th
                  scope="col"
                  class="
                    px-6
                    py-3
                    text-left text-xs
                    font-medium
                    text-gray-500
                    uppercase
                    tracking-wider
                  "
                >
                  Status
                </th>
                <th scope="col" class="relative px-6 py-3">
                  <span class="sr-only">Action</span>
                </th>
              </tr>
            </thead>
            <tbody class="bg-white divide-y divide-gray-200">
              <tr
                v-for="mortgageState in mortgages"
                :key="mortgageState.mortgageRequest.m_id"
              >
                <td
                  class="
                    px-6
                    py-4
                    whitespace-nowrap
                    text-sm
                    font-medium
                    text-gray-900
                  "
                >
                  {{ mortgageState.mortgageRequest.m_id }}
                </td>
                <td
                  class="
                    px-6
                    py-4
                    whitespace-nowrap
                    text-sm
                    font-medium
                    text-gray-900
                  "
                >
                  {{ mortgageState.date }}
                </td>
                <td
                  class="px-6 py-4 whitespace-nowrap text-sm text-gray-500"
                >
                  {{ mortgageState.mortgageRequest.s_id }}
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                  <span
                    class="
                      px-2
                      inline-flex
                      text-xs
                      leading-5
                      font-semibold
                      rounded-full
                    "
                    :class="stateClasses(mortgageState?.processState?.state)"
                  >
                    {{ mortgageState.processState.state }}
                  </span>
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                  <span class="px-6 py-4 whitespace-nowrap font-medium text-sm" :class="statusClasses(mortgageState?.processState?.status)">
                    {{ mortgageState.processState.status }}
                  </span>
                </td>
                <td
                  class="
                    px-6
                    py-4
                    whitespace-nowrap
                    text-right text-sm
                    font-medium
                  "
                >
                  <button
                    @click="cancelMortgage(mortgageState.mortgageRequest.m_id)"
                    class="text-red-600 mr-4" id="deleteButton"
                  >
                    Delete
                  </button>
                  <a
                    :href="'#tracktrace?id=' + mortgageState.mortgageRequest.m_id"
                    class="text-blue-600 hover:text-blue-900" id="viewButton"
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
</template>

<script lang="ts">
import { Vue } from "vue-class-component";
import { Method, myFetch } from "../modules/api/myFetch";
import { Status } from "../modules/api/Status";
import { store } from "../modules/store/store";
import { APPLICATION_JSON } from "../modules/util";
import { ProcessState } from "../modules/api/Paths/processState";
import {
  MortgageProcessState,
} from "../modules/api/Paths/mortgageRequest";

export default class PageMyMortgage extends Vue {
  private mortgages: Array<MortgageProcessState> = [];
  private user = store.userData;
  private mortgageListUrl = "/api/mortgagerequests";

  mounted() {
    if (this.user == undefined) {
      location.hash = "";
      return;
    }

    this.fetchProcessStates();
  }

  fetchProcessStates() {
    const xhr = new XMLHttpRequest();

    xhr.onreadystatechange = () => {
      if (xhr.readyState == 4 && xhr.status == 200) {
        const object = JSON.parse(xhr.responseText);
        for (const element of object) {
          this.mortgages.push(
            new MortgageProcessState(
              element.mortgageRequest,
              element.processState,
              element.date,
              null
            )
          );
        }
      }
    };

    xhr.open("GET", `${this.mortgageListUrl}/${this.user?.id}/status`, true);
    xhr.send();
  }
  async cancelMortgage(mortgageID: number) {
    const confirmCancel = confirm("Do you really want to cancel the mortgage?");
    if (!confirmCancel) {
      return;
    }

    //Send DELETE request to server:
    const mortgageURLById = `${this.mortgageListUrl}/${this.user?.id}/${mortgageID}`;

    const deleteRequest = new ProcessState(
      mortgageID,
      "Delete Request",
      "Processing"
    );

    const res = await myFetch(mortgageURLById, {
      method: Method.PUT,
      body: JSON.stringify(deleteRequest),
      headers: {
        "Content-Type": APPLICATION_JSON,
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

  // switch Process State color for each state
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

  // switch Process Status color for each status
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

}
</script>
