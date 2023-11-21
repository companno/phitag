import { useEffect } from 'react';
import Router from 'next/router';
import NProgress from 'nprogress';
import 'nprogress/nprogress.css'; // Import the nprogress styles

const NProgressContainer: React.FC = ({ children }) => {
  useEffect(() => {
    NProgress.configure({showSpinner:false});
    const start = () => NProgress.start();
    const done = () => NProgress.done();

    Router.events.on('routeChangeStart', start);
    Router.events.on('routeChangeComplete', done);
    Router.events.on('routeChangeError', done);

    return () => {
      Router.events.off('routeChangeStart', start);
      Router.events.off('routeChangeComplete', done);
      Router.events.off('routeChangeError', done);
    };
  }, []);

  return <>
    <style jsx global>{`
        /* Customize the NProgress color to white (ffffff) */
        #nprogress .bar {
          background: #ffff !important;
        }
      `}
      </style>
  {children}</>;
};

export default NProgressContainer;
