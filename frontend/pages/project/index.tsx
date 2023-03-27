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


const ProjectRedirectPool: NextPage = () => {

    // auth
    const authenticated = useAuthenticated();

    useEffect(() => {

        if (authenticated.isReady && !authenticated.isAuthenticated) {
            toast.info("Session expired, please login again.");
            Router.push("/");
        } else {
            Router.push("/pool/project");
        }

    }, [authenticated]);

    return <FullLoadingPage headtitle="Project Pool" />;

}

export default ProjectRedirectPool;