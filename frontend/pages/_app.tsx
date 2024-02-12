import '../styles/globals.css'
import 'react-toastify/dist/ReactToastify.css';
import { toast, ToastContainer } from 'react-toastify';

import type { AppProps } from 'next/app'
import Head from 'next/head';


function App({ Component, pageProps }: AppProps) {

    return (
        <>
            <Head>
                <link rel="icon" href="/favicon.svg" />
                <meta charSet='UTF-8' />
                <meta name="description" content="
                PhiTag is a modular and extensible web application solution aimed at
                providing a simple and easy to use interface for different tagging and annotation tasks, 
                finding new annotators, managing the annotation process and providing a platform for the evaluation of the results. The application has a built-in lexicography layer enabling the creation of dictionaries.
                " />
                <meta name="keywords" content="phitag, annotation, tagging, lexicography, dictionary, evaluation, crowdsourcing, crowdsourced, annotation platform, tagging platform, lexicography platform, dictionary platform, evaluation platform, annotation tool, tagging tool, lexicography tool, dictionary tool, evaluation tool, annotation software, tagging software, lexicography software, dictionary software, evaluation software, annotation solution, tagging solution, lexicography solution, dictionary solution, evaluation solution, annotation service, tagging service, lexicography service, dictionary service, evaluation service, annotation application, tagging application, lexicography application, dictionary application, evaluation application, annotation system, tagging system, lexicography system, dictionary system, evaluation system" />
                <meta name="author" content="PhiTag" />

                <meta property="og:title" content="PhiTag" />
                <meta property="og:description" content="
                PhiTag is a modular and extensible web application solution aimed at
                providing a simple and easy to use interface for different tagging and annotation tasks,
                finding new annotators, managing the annotation process and providing a platform for the evaluation of the results. The application has a built-in lexicography layer enabling the creation of dictionaries.
                " />
                {/* <meta property="og:image" content="https://phitag.org/og-image.png" /> */}
                <meta property="og:url" content="https://phitag.org" />
                <meta property="og:type" content="website" />
            </Head>

            <Component {...pageProps} />

            {/* Register Toast */}
            <ToastContainer
                position="bottom-right"
                autoClose={5000}
                hideProgressBar={true}
                newestOnTop
                closeOnClick
                rtl={false}
                pauseOnFocusLoss
                draggable
                pauseOnHover
                closeButton={false}

                theme={"dark"}
                toastClassName={"prose font-dm-mono-medium font-black"}


            />
        </>
    )
}

export default App;