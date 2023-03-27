// Next
import { NextPage } from "next";
import Router from "next/router";

// React
import { useEffect } from "react";

// Toast
import { toast } from "react-toastify";

// Hooks
import useAuthenticated from "../../lib/hook/useAuthenticated";

// Custom Controllers

// Custom Components
import FullLoadingPage from "../../components/pages/fullloadingpage";


const GuidesRedirectDashboard: NextPage = () => {

    // auth
    const authenticated = useAuthenticated();

    useEffect(() => {

        if (authenticated.isReady && !authenticated.isAuthenticated) {
            toast.info("Session expired, please login again.");
            Router.push("/");
        } else {
            toast.info("Guides is under construction.");
            Router.push("/dashboard");
        }

    }, [authenticated]);

    return <FullLoadingPage headtitle="Guides" />;

}

export default GuidesRedirectDashboard;