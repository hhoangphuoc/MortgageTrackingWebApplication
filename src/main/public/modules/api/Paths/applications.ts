import { myFetch } from "../myFetch";
import { neverText, okJson } from "../Response";
import { Status } from "../Status";

export class Application {
    c_id!: number
    m_id!: number
    
    grossIncome!: number
    willRetired!: boolean
    havePartner!: boolean
    partnerGrossIncome!: number
    willPartnerRetire!: boolean

    haveStudentLoan!: boolean
    studentDebt!: number
    haveSpousalMaintenance!: boolean
    monthlyAlimony!: number
    haveOtherDebt!: boolean
    otherDebtInfo!: string
    otherDebtAmount!: number
    haveCredits!: boolean
    creditInfo!: string
    creditAmount!: number;


    bankAmount!: number;
    collateral1!: string;
    price1!: number;
    collateral2!: string;
    price2!: number;
    collateral3!: string;
    price3!: number;

    requestedMortgageAmount!: number;
    date!: string;
    duration!: string;
    reason!: string;

    static pageList: (keyof Application)[][] = [
        [
            "grossIncome",
            "willRetired",
            "havePartner",
            "partnerGrossIncome",
            "willPartnerRetire",
        ], [
            "haveStudentLoan",
            "studentDebt",
            "haveSpousalMaintenance",
            "monthlyAlimony",
            "haveOtherDebt",
            "otherDebtInfo",
            "otherDebtAmount",
            "haveCredits",
            "creditInfo",
            "creditAmount",
        ], [
            "bankAmount",
            "collateral1",
            "price1",
            "collateral2",
            "price2",
            "collateral3",
            "price3",
        ], [
            "requestedMortgageAmount",
            "date",
            "duration",
            "reason",
        ]
    ]
    /**
     * @deprecated
     */
    static pages = [{
        grossIncome: Number,
        willRetired: Boolean,
        havePartner: Boolean,
        partnerGrossIncome: Number,
        willPartnerRetire: Boolean
    }, {
        haveStudentLoan: Boolean,
        studentDebt: Number,
        haveSpousalMaintenance: Boolean,
        monthlyAlimony: Number,
        haveOtherDebt: Boolean,
        otherDebtInfo: String,
        otherDebtAmount: Number,
        haveCredits: Boolean,
        creditInfo: String,
        creditAmount: Number,
    }, {
        bankAmount: Number,
        collateral1: String,
        price1: Number,
        collateral2: String,
        price2: Number,
        collateral3: String,
        price3: Number,
    }, {
        requestedMortgageAmount: Number,
        date: String,
        duration: String,
        reason: String,
    }];
}

export async function applicationsGet(id: number){
    const res = await myFetch(`api/applications/${id}`);
    switch (res.status){
        case Status.OK: return okJson(res, Application, res.status);
        default: return neverText(res);
    }
}
