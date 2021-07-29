<script lang="ts">
import { Vue } from 'vue-class-component';
import Page404 from "./pages/404/404.vue";
import PageLogin from "./pages/login/login.vue";
import PageRegister from "./pages/register/register.vue";
import PageTrackTrace from "./pages/track-and-trace/TrackTrace.vue";
import PageStartScreen from './pages/StartScreen/StartScreen.vue';
import PageApplicationForm from './pages/applicationForm/applicationForm.vue';
import PageContactUs from "./pages/contactUs/contactUs.vue";
import PageAboutUs from "./pages/aboutUs/aboutUs.vue";
import HomeScreenLoggedIn from './pages/HomeScreenLoggedIn/HomeScreenLoggedIn.vue';
import PageMyMortgage from "./pages/myMortgage/myMortgage.vue";
import PageApplicationsOverview from './pages/applicationsOverview/applicationsOverview.vue';
import pleaseLogin from "./pages/pleaseLogin/pleaseLogin.vue";
import PageTrackTraceSP from './pages/track-and-trace/TrackTraceSP.vue';
import PageBindingOffer from "./pages/BindingOffer/BindingOffer.vue";
import PageApplicationPreview from './pages/ProvidedDocumentView/applicationPreview.vue';
import PageApplicationPreviewSP from './pages/ProvidedDocumentView/applicationPreviewSP.vue';
import Chat from './pages/chat/chat.vue';
import PageBindingOfferCustomer from './pages/BindingOffer/BindingOfferCustomer.vue';
import Inbox from './pages/chat/inbox.vue';
import Profile from './pages/profile/profile.vue';

export default {}

export const pages = {
    "startScreen": PageStartScreen,
    "contactus": PageContactUs,
    "aboutus": PageAboutUs,
    "applicationsOverview" : PageApplicationsOverview,
    "tracktraceSP":PageTrackTraceSP,
    "applicationPreview": PageApplicationPreview,
    "applicationPreviewSP": PageApplicationPreviewSP,
    "bindingoffer": PageBindingOffer,
    "bindingofferCustomer": PageBindingOfferCustomer,
    "chat": Inbox,
} as any as Record<string, typeof Vue>

export const loggedout_pages = {
    "": PageStartScreen,
    "login": PageLogin, 
    "register": PageRegister,
}

export const loggedin_pages = {
    "": HomeScreenLoggedIn,
    "tracktrace": PageTrackTrace,
    "application": PageApplicationForm,
    "mymortgage": PageMyMortgage,
    "chat": Inbox,
    "profile": Profile,
}

export const sp_pages = {
    "": PageApplicationsOverview,
    "tracktraceSP": PageTrackTraceSP,
    "bindingoffer": PageBindingOffer,
    "applicationPreviewSP": PageApplicationPreviewSP,
    "profile": Profile,
}


export const pagePleaseLogin = pleaseLogin;
export const page404 = Page404;

</script>
