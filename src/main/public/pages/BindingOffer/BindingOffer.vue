<template>
  <div class="mx-auto w-2/3">
    <Breadcrumbs class="mb-4 ml-4" :pages="pages" />
    <div class="bg-white shadow overflow-hidden sm:rounded-lg">
      <div class="px-4 py-5 sm:px-6 flex justify-between">
        <div>
          <h3 class="text-lg leading-6 font-medium text-gray-900">
            Binding Offer
          </h3>
          <p class="mt-1 max-w-2xl text-sm text-gray-500">
            Please carefully review these details, download and submit the signed pdf.
          </p>
        </div>
        <div>
          <button
            id="downloadButton"
            v-on:click="downloadPdf()"
            class="button-primary"
          >
            Download
          </button>
        </div>
      </div>
      <div class="border-t border-gray-200 px-4 py-5 sm:p-0">
        <dl class="sm:divide-y sm:divide-gray-200">
          <div class="py-4 sm:py-5 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-6">
            <dt class="text-sm font-medium text-gray-500">Full name</dt>
            <dd class="mt-1 text-sm text-gray-900 sm:mt-0 sm:col-span-2">
              {{ this.mortgage?.customerName }}
            </dd>
          </div>
          <div class="py-4 sm:py-5 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-6">
            <dt class="text-sm font-medium text-gray-500">
              Requested Mortgage Amount
            </dt>
            <dd class="mt-1 text-sm text-gray-900 sm:mt-0 sm:col-span-2">
              {{ formatCurrency(this.application?.requestedMortgageAmount) }}
            </dd>
          </div>
          <div class="py-4 sm:py-5 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-6">
            <dt class="text-sm font-medium text-gray-500">Application Date</dt>
            <dd class="mt-1 text-sm text-gray-900 sm:mt-0 sm:col-span-2">
              {{ this.application?.date }}
            </dd>
          </div>
          <div class="py-4 sm:py-5 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-6">
            <dt class="text-sm font-medium text-gray-500">
              Application Duration
            </dt>
            <dd class="mt-1 text-sm text-gray-900 sm:mt-0 sm:col-span-2">
              {{ this.application?.duration }}
            </dd>
          </div>
          <div class="py-4 sm:py-5 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-6">
            <dt class="text-sm font-medium text-gray-500">Payment Details</dt>
            <dd class="mt-1 text-sm text-gray-900 sm:mt-0 sm:col-span-2">
              <table class="min-w-full divide-y divide-gray-200">
                <tbody>
                  <tr v-for="i in 3" v-if="hasCollat(i)" :key="i">
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
                      Collateral #{{ i }}
                    </td>
                    <td
                      class="px-6 py-4 whitespace-nowrap text-sm text-gray-500"
                    >
                      {{ formatCurrency(this.application[`price${i}`]) }}
                    </td>
                  </tr>
                  <tr>
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
                      Interest Offer
                    </td>
                    <td
                      class="px-6 py-4 whitespace-nowrap text-sm text-gray-500"
                    >
                      {{ this.interestOffer?.interest_rate }}%
                    </td>
                  </tr>
                </tbody>
              </table>
            </dd>
          </div>
          <div class="py-4 sm:py-5 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-6">
            <dt class="text-sm font-medium text-gray-500">
              Financial Overview
            </dt>
            <dd class="mt-1 text-sm text-gray-900 sm:mt-0 sm:col-span-2">
              <table class="min-w-full divide-y divide-gray-200 text-left">
                <tbody>
                  <tr>
                    <th
                      class="
                        px-6
                        py-4
                        whitespace-nowrap
                        text-sm
                        font-medium
                        text-gray-900
                      "
                    >
                      Subtotal:
                    </th>
                    <td
                      class="px-6 py-4 whitespace-nowrap text-sm text-gray-500"
                    >
                      {{
                        formatCurrency(
                          this.application?.requestedMortgageAmount
                        )
                      }}
                    </td>
                  </tr>
                  <tr>
                    <th
                      class="
                        px-6
                        py-4
                        whitespace-nowrap
                        text-sm
                        font-medium
                        text-gray-900
                      "
                    >
                      Total Interest: ({{ this.interestOffer?.interest_rate }}%)
                    </th>
                    <td
                      class="px-6 py-4 whitespace-nowrap text-sm text-gray-500"
                    >
                      {{ formatCurrency(this.totalInterest) }}
                    </td>
                  </tr>
                  <tr>
                    <th
                      class="
                        px-6
                        py-4
                        whitespace-nowrap
                        text-sm
                        font-medium
                        text-gray-900
                      "
                    >
                      Total:
                    </th>
                    <td
                      class="px-6 py-4 whitespace-nowrap text-sm text-gray-500"
                    >
                      {{ formatCurrency(this.total) }}
                    </td>
                  </tr>
                </tbody>
              </table>
            </dd>
          </div>
        </dl>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { Vue, Options } from "vue-class-component";
import jsPDF from "jspdf";
import {
  Application,
  applicationsGet,
} from "../../modules/api/Paths/applications";
import {
  getMortgageInterestOffer,
  getMortgageProcessState,
  InterestOffer,
  MortgageProcessState,
} from "../../modules/api/Paths/mortgageRequest";
import { currentUrl } from "../../modules/url";
import { Status } from "../../modules/api/Status";
//import { currentUrl } from "../../modules/url";
import Breadcrumbs from "../../components/Breadcrumbs.vue";
import { Watch } from "vue-property-decorator";

@Options({
  components: {
    Breadcrumbs,
  },
})
export default class PageBindingOffer extends Vue {
  static tagName = "PageBindingOffer";

  private application: Application = new Application();
  private interestOffer: InterestOffer | null = null;
  private mortgage: MortgageProcessState | null = null;

  mortgageId!: number;

  private totalInterest!: number;
  private requestedMortgageAmount!: number;

  private total!: number;
  private pages?;

  get getpages() {
    return [
      {
        name: "Track&Trace",
        href: `#tracktraceSP?id=${this.mortgageId}`,
        current: false,
      },
      { name: "Binding Offer", href: `${location.href}`, current: true },
    ];
  }

  private hasCollat(i: number): Boolean {
    return this.application[`price${i}`];
  }

  private formatCurrency(amount: number) {
    if (amount) {
      return `€ ${amount.toLocaleString("nl-NL")}`;
    }
  }

  async mounted() {
    this.mortgageId = Number.parseInt(currentUrl.getParam("m_id") ?? "");
    if (isNaN(this.mortgageId)) {
      console.log(this.mortgageId);
      location.hash = "#";
      location.reload();
    }

    this.pages = this.getpages;

    const applicationRes = await applicationsGet(this.mortgageId);
    switch (applicationRes.status) {
      case Status.OK:
        console.log(applicationRes.content);
        this.application = applicationRes.content;
        console.log(this.application);
        break;
      default:
        console.log("error");
    }

    const InterestRes = await getMortgageInterestOffer(this.mortgageId);
    switch (InterestRes.status) {
      case Status.OK:
        break;
      default:
        {
          console.error(
            InterestRes.status,
            InterestRes.statusText,
            InterestRes
          );
        }
        return;
    }
    this.interestOffer = InterestRes.content;
    console.log(this.interestOffer);

    const mortgageRes = await getMortgageProcessState(this.mortgageId);
    switch (mortgageRes.status) {
      case Status.OK:
        break;
      default:
        {
          console.error(
            mortgageRes.status,
            mortgageRes.statusText,
            mortgageRes
          );
        }
        return;
    }
    this.mortgage = mortgageRes.content;
    console.log(this.interestOffer);

    this.totalInterest =
      (this.interestOffer.interest_rate *
        this.application.requestedMortgageAmount) /
      100;
    console.log(this.totalInterest);

    this.total = this.totalInterest + this.application.requestedMortgageAmount;
  }

  downloadPdf() {
    const popup = confirm(
      "Are you sure you want to download this Binding Offer ?"
    );
    if (!popup) {
      return;
    }

    const doc = new jsPDF();

    const mortgageOfferID = this.mortgageId as number;
    const applicationDate = this.application?.date as string;
    const customerName = this.mortgage?.customerName as string;
    //const birthdate ; we currently don't have customer DOB
    const valueloan = this.application?.requestedMortgageAmount;
    const collateralValue = (this.application?.price1 +
      this.application?.price2 +
      this.application?.price3) as number;
    const interest_rate = this.interestOffer?.interest_rate as number;
    const duration = this.interestOffer?.duration as string;

    const totalcost = valueloan + interest_rate * valueloan; //mortgagevalue + interest

    //Noah template GOES HERE----

    doc.setFont("arial").setFontSize(12);

    doc.text("Mortgage ID: " + mortgageOfferID, 30, 20);
    doc
      .text("Application Date: " + applicationDate, 128, 20)
      .setLineHeightFactor(1.5);

    doc.text("Dear " + customerName + ",", 30, 50);
    doc.text(
      "You are looking for a mortgage that suits your situation and wishes. That is why you have" +
        "\nlisted your preferences together with your mortgage advisor. We will be happy to send" +
        "\nyou this offer that hopefully matches your situation and wishes." +
        "\n\nWe offer you a mortgage of €" +
        valueloan +
        ". Detailed information about your mortgage can" +
        "\nbe found in the binding offer by this letter." +
        "\n\nIf you accept this offer, then we will process this further. We will let you know as soon as" +
        "\nthe mortgage is approved. You then have until the latest passing date to sign the papers for" +
        "\nthe mortgage at the notary." +
        "\n\n\nSincerely," +
        "\n\nTopicus",
      30,
      65
    );

    doc
      .addPage()
      .setFontSize(14)
      .setTextColor(49, 62, 140)
      .setLineHeightFactor(2);

    doc.text("DETAILS OF MORTGAGE OFFER", 30, 35);
    doc.line(30, 40, 175, 40).setFontSize(12).setTextColor(0, 0, 0);
    doc.text(
      "Name applicant:				" +
        customerName +
        "\nAmount of the loan:			  " +
        valueloan +
        "\nValue of the house:			   " +
        collateralValue +
        "\nRepayment method:			  " +
        "fixed-rate mortgage" +
        "\ninterest rate:				      " +
        interest_rate +
        "%" +
        "\nfixed rate period:			      " +
        duration,
      30,
      60
    );

    doc
      .addPage()
      .setFontSize(14)
      .setTextColor(49, 62, 140)
      .setLineHeightFactor(1.5);

    doc.text("ADDITIONAL ONE-OFF COSTS", 30, 35);
    doc.line(30, 40, 175, 40).setFontSize(12).setTextColor(0, 0, 0);

    doc
      .text(
        "APPRASIALS COSTS: the collateral must be appraised by an appraiser acceptable to us." +
          "\nThe costs charged by the appraiser for this you need to pay yourself." +
          "\n\n\nNOTARY FEES: the costs charged by the notary are for your own account. The notary" +
          "\nwill give you a specification of his costs and the other deductions." +
          "\n\n\nPLEASE NOTE: co-financing the additional one-off costs costs money. Since 1 January" +
          "\n2018 you cannot get more than 100% of the home value as a mortgage. You can therefore no" +
          "\nlonger co-finance the additional costs in your mortgage and you will have to pay them" +
          "\ncompletely with you own resources.",
        30,
        50
      )
      .setFontSize(14)
      .setTextColor(49, 62, 140);

    doc.text("EARLY REPAIMENT", 30, 150).setFontSize(12).setTextColor(0, 0, 0);
    doc.line(30, 155, 175, 155).setFontSize(12).setTextColor(0, 0, 0);

    doc
      .text(
        "Early repayment of the financing is always possible. You can make additional" +
          "\nrepayments up to 10% per year at no extra cost. Do you you want to repay even more or" +
          "\nor transfer your mortgage interest to the current interest rate then you need to pay a" +
          "\nfee. For transfering your mortgage to the cureent interest rate you pay a fee of €350.",
        30,
        165
      )
      .setFontSize(14)
      .setTextColor(49, 62, 140);

    doc
      .text("CODE OF CONDUCT MORTGAGE FINANCES", 30, 215)
      .setFontSize(12)
      .setTextColor(0, 0, 0);
    doc.line(30, 220, 175, 220).setFontSize(12).setTextColor(0, 0, 0);

    doc.text(
      "We signed the Code of Conduct for Mortgage Financing. You can request this code" +
        "\nof Conduct by us. A mortgage advisor of us can provied you with more information about" +
        "\nthe Code of Conduct and the possible consequences in your situation.",
      30,
      230
    );

    doc.addPage().setFontSize(14).setTextColor(49, 62, 140);

    doc.text("FOR COMPLAINTS ABOUT CODE COMPLIANCE", 30, 35);
    doc.line(30, 40, 175, 40).setFontSize(12).setTextColor(0, 0, 0);

    doc
      .text(
        "Complaints about compliance with the code of conduct can be submitted in writtin" +
          "\nform to us. You complain should state the following information:" +
          "\n	Name" +
          "\n	Address" +
          "\n	Place of residence" +
          "\n	Contract number" +
          "\n\n\nIf the complaint is not resolved to your satisfaction, you can submit it within three" +
          "\nmonths after you have received a definitive response from the Mortgage lender to:" +
          "\n\nFinancial Services Complaints Institute" +
          "\nPostbus 93257" +
          "\n2509 AG Den Haag" +
          "\nFor more information: www.kifid.nl.",
        30,
        50
      )
      .setFontSize(14)
      .setTextColor(49, 62, 140);

    doc.text("GENERAL INFORMATION", 30, 160);
    doc.line(30, 165, 175, 165).setFontSize(12).setTextColor(0, 0, 0);

    doc.text(
      "This offer is made on the condition that all information requested by us regarding" +
        "\nthe collateral and the applicant(s) is entirely to our satisfaction. This also includes" +
        "\nthe information requested from the Credit Registration Office in Tiel (BKR). This also" +
        "\nincludes the requested information from the Fraud Testing System of the Establishment" +
        "\nMortgagees Unit for Registration of Incidents and Fraud Facts. If it appears that the" +
        "\napplicant commits or attempts to commit fraud in whatever form, whether or not in" +
        "\ncollaboration with several persons, then the details of the applicant will be registered" +
        "\nin Fraud Testing System. This is mostly for the benefit of third parties.",
      30,
      175
    );

    doc.addPage().setFontSize(14).setTextColor(49, 62, 140);

    doc.text("STATEMENT OF ACCEPTANCE AND PERIOD OF VALIDITY", 30, 35);
    doc.line(30, 40, 175, 40).setFontSize(12).setTextColor(0, 0, 0);

    doc.text(
      "SIGNING THE OFFER. By accepting this offer you declare that you agree with the entire" +
        "\nbinding offer and that you have received the conditions. Make sure that we have received" +
        "\nthe signed offer before the end date of the acceptance period. If we receive the signed" +
        "\noffer later, it is no longer valid." +
        "\n\nTO THE NOTARY. You have until the closing date to sign the mortgage certificate at the" +
        "\nnotary." +
        "\n\nI go to the notary's office: ____________________ in ____________________" +
        "\n\nI agree to request and use the citizen service number for information exchange with the" +
        "\ntax authorities and possibly the Establishment Guarentee Fund of Own Homes to obtain a" +
        "\nNational Mortgage Guarantee if there is a National Mortgage Guarantee" +
        "\n\nCitizen service number applicant." +
        "\n\n\nSIGNATURE FOR APPROVAL" +
        "\n\nPlace: ____________________ Date: ____________________" +
        "\n\n\nSignature applicant:" +
        "\n\n\n\n__________________________",
      30,
      50
    );

    doc.save("BindingOffer");
  }
}
</script>

<style>
</style>
