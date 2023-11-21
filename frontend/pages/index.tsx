// Next Modules
import Head from 'next/head'
import Image from "next/image";
import Link from "next/link";
import path from 'path'

// Data
import { getSortedDirectoryData } from '../lib/hook/useData'

// Images
import prolific from "../public/image/prolific.png";
import tags24x7 from "../public/image/tags24x7.png";

// Custom Components
import BasicNavbar from '../components/specific/navbar/basicnavbar'
import BasicFooter from '../components/specific/navbar/basicfooter'
import { useRef } from 'react'
import { FiArrowUp } from 'react-icons/fi'
import logo from "../public/image/logo.png";

export default function Home({ init, advantages, experience }: { init: Array<any>, advantages: Array<any>, experience: Array<any> }) {

    // add ref to the divs
    const refHome = useRef<HTMLDivElement>(null);
    const refPlatform = {
        big: useRef<HTMLDivElement>(null),
        small: useRef<HTMLDivElement>(null)
    }
    const refService = {
        big: useRef<HTMLDivElement>(null),
        small: useRef<HTMLDivElement>(null)
    }
    const refLexicography = {
        big: useRef<HTMLDivElement>(null),
        small: useRef<HTMLDivElement>(null)
    }
    const refCollaborations = {
        big: useRef<HTMLDivElement>(null),
        small: useRef<HTMLDivElement>(null)
    }

    const correctScroll = (ref_big: React.RefObject<HTMLDivElement>, ref_small: React.RefObject<HTMLDivElement>) => {
        // based on width of screen, scroll to correct position
        if (ref_big.current && ref_small.current) {
            if (window.innerWidth > 1500) {
                ref_big.current.scrollIntoView({ behavior: 'smooth', block: 'center' });
            } else {
                ref_small.current.scrollIntoView({ behavior: 'smooth', block: 'center' });
            }
        }
    }


    const warning: string = " Please note, this is an early version of the finished app, " +
        "hence there will be frequent changes to API-structure and how data is handled. " +
        "In some cases, this might lead to data loss. " +
        "We do daily backups of the database, so if you experience any data loss, please contact us. ";

    return (
        <div className='flex flex-col'>

            <BasicNavbar />

            <Head>
                <title>PhiTag</title>
            </Head>


            {/* div taking full height of screen */}
            <div className='w-full my-32 xl:my-0 xl:h-screen flex' >

                {/* three boxes */}
                <div className='flex flex-col m-auto space-y-8' ref={refHome}>

                    <div className='sm:px-16 md:px-64 xl:px-96 text-center' >
                        <div className='font-uni-corporate-bold font-bold text-4xl'>
                            Welcome to PhiTag
                        </div>

                        <div className='font-uni-corporate-light text-base16-gray-900 py-2'>
                            The platform for your text annotation needs
                        </div>

                    </div>

                    <div className='flex shadow-md p-8 justify-center cursor-pointer hover:scale-105 transform transition-all duration-500'
                         onClick={() => correctScroll(refPlatform.big, refPlatform.small)}>
                        <div className='font-uni-corporate-bold font-bold text-2xl '>
                            Annotation Platform
                        </div>
                    </div>

                    <div className='flex shadow-md p-8 justify-center cursor-pointer hover:scale-105 transform transition-all duration-500'
                         onClick={() => correctScroll(refService.big, refService.small)}>
                        <div className='font-uni-corporate-bold font-bold text-2xl'>
                            Annotation Service
                        </div>
                    </div>

                    <div className='flex shadow-md p-8 justify-center cursor-pointer hover:scale-105 transform transition-all duration-500'
                         onClick={() => correctScroll(refLexicography.big, refLexicography.small)}>
                        <div className='font-uni-corporate-bold font-bold text-2xl'>
                            Lexicography
                        </div>
                    </div>

                    <div className='flex shadow-md p-8 justify-center cursor-pointer hover:scale-105 transform transition-all duration-500'
                         onClick={() => correctScroll(refCollaborations.big, refCollaborations.small)}>
                        <div className='font-uni-corporate-bold font-bold text-2xl'>
                            Collaborations
                        </div>
                    </div>

                </div>
            </div>


            <div className='w-full my-32 xl:my-0 xl:h-screen flex'>
                <div className='flex flex-col m-2 xl:w-2/3 xl:m-auto' ref={refPlatform.big}>

                    {/* First Card */}
                    <div className='flex flex-col shadow-md py-6 px-8 sm:py-12 sm:px-16' >

                        <h1 className='mb-2 font-uni-corporate-bold font-bold text-4xl' ref={refPlatform.small}>
                            PhiTag: Annotation Platform
                        </h1>

                        <div className='font-uni-corporate-light text-base16-gray-900 py-2'>
                            PhiTag is an <Link href='https://github.com/Garrafao/phitag'><span className='underline cursor-pointer '>open source platform</span></Link> for your text annotation needs.
                            As a leading research university in machine learning in Europe, we know the importance of custom data labeling, and understand the importance of annotation quality!
                            We want to make it as easy as possible for you to get the data that you need, which is why we offer a wide range of annotator selection options and have built-in quality control mechanisms.
                            Our <Link href='/guide/introduction'><span className='underline cursor-pointer '>introductory guide</span></Link> will help you get started with PhiTag, and for our more advanced users,
                            we have a detailed guides on our different use cases, such as <Link href='/guide/how-to-annotator'><span className='underline cursor-pointer '>how to use PhiTag as an annotator</span></Link> or
                            <Link href='/guide/how-to-annotation-manager'><span className='underline cursor-pointer '> how to use PhiTag as an annotation manager</span></Link>, as well as <Link href='/guide/how-to-corpus'><span className='underline cursor-pointer '>how to use the PhiTag in-built corpus</span></Link> section.
                            We know that one size does not fit all, which is why we have made PhiTag as flexible as possible and open source, so that the platform can be adapted to your needs.
                            This is also why we develop curated view for different use cases, such as <Link href='/guide/how-to-lexicography'><span className='underline cursor-pointer '>Lexicography</span></Link>, to fit the tool to your needs and manage the complexity of the tool.
                            Please take a look at our <Link href='/guide/supported-tasks'><span className='underline cursor-pointer '>Supported Tasks</span></Link> to see if PhiTag already supports your use case, and if not, please <Link href='https://www.ims.uni-stuttgart.de/en/institute/team/Schlechtweg-00003/'><span className='underline cursor-pointer '>contact us</span></Link> to discuss how we can help you.
                            Do also check our <Link href='/use-cases'><span className='underline cursor-pointer '>use cases</span></Link>.
                        </div>

                        <h2 className='mt-8 mb-2 font-uni-corporate-bold font-bold text-2xl'>
                            What makes us different?
                        </h2>

                        <div className='font-uni-corporate-light text-base16-gray-900 py-2'>
                            <ul className='list-disc list-inside'>
                                <li> <span className='font-uni-corporate-bold font-bold'>Open Source </span> Other annotation platforms are closed source, which means that you cannot adapt them to your needs. PhiTag allows you to take control over your data by local and offline deployment and is not tied to a specific cloud provider nor to the platform provider. </li>
                                <li> <span className='font-uni-corporate-bold font-bold'>Customizability </span> PhiTag is built to be modular and extensible, providing you with the flexibility to adapt the platform to your needs in a simple way. </li>
                                <li> <span className='font-uni-corporate-bold font-bold'>Automation </span> PhiTag is built to support automation, by providing simple to use APIs for automation of annotation tasks. </li>
                                <li> <span className='font-uni-corporate-bold font-bold'>Support </span> We offer a wide range of support options, from deployment, bug fixing, to custom development of extensions. </li>
                            </ul>
                        </div>

                    </div>

                    {/* Second Card and Third Card */}
                    <div className='flex flex-row space-x-8 mt-8'>


                        <div className='w-1/2 flex flex-col shadow-md p-4 sm:p-8'>
                            <div className='font-uni-corporate-light text-base16-gray-900 p-2'>
                                Want to try out PhiTag? <Link href='/register'><span className='underline cursor-pointer '> Register </span></Link> for a free account and get started right away!
                            </div>
                        </div>

                        <div className='w-1/2 flex flex-col shadow-md p-4 sm:p-8'>
                            <div className='font-uni-corporate-light text-base16-gray-900 p-2'>
                                Have a project in mind? Get in touch with us <Link href='/about-us'><span className='underline cursor-pointer '> here</span></Link>.
                            </div>
                        </div>

                    </div>




                </div>
            </div>

            <div className='w-full my-32 xl:my-0 xl:h-screen flex'>
                <div className='flex flex-col m-2 xl:w-2/3 xl:m-auto' ref={refService.big}>


                    <div className='flex flex-col shadow-md py-6 px-8 sm:py-12 sm:px-16' >
                        <h1 className='mb-2 font-uni-corporate-bold font-bold text-4xl' ref={refService.small}>
                            PhiTag: Annotation Service
                        </h1>

                        <div className='font-uni-corporate-light text-base16-gray-900 py-2'>
                            We are a Europe-based company organizing text annotation processes with humans and computers.
                            We offer to take care of your annotation needs, from organize the full annotation process including guideline development, finding and training annotators, data quality checking and analysis.
                            With our experience in the field of natural language processing, we can help you to get the data you need for your next project.
                            Do not hesitate to <Link href='/about-us'><span className='underline cursor-pointer '>contact us</span></Link> to discuss your project and how we can help you.
                            Also, please check our <Link href='/use-cases'><span className='underline cursor-pointer '>use cases</span></Link> for more information.
                        </div>

                        <h2 className='mt-8 mb-2 font-uni-corporate-bold font-bold text-2xl'>
                            What makes us different?
                        </h2>

                        <div className='font-uni-corporate-light text-base16-gray-900 py-2'>
                            <ul className='list-disc list-inside'>
                                <li> <span className='font-uni-corporate-bold font-bold'> Fast and flexible </span> We implement annotation studies in a fast and flexible way, allowing you to quickly get the data you need. </li>
                                <li> <span className='font-uni-corporate-bold font-bold'> Reliable </span> We stand with our name and experience in the field of natural language processing to provide you with reliable data. </li>
                                <li> <span className='font-uni-corporate-bold font-bold'> Convenient </span> We take care of the whole process, from the preparation of the annotation task to the delivery of the data, while you can focus on your core business. </li>

                                <li> <span className='font-uni-corporate-bold font-bold'> Fair wages and data security </span> We are based in the European Union, as our annotators. Annotators receive fair payments, data is stored locally on German servers. </li>
                                <li> <span className='font-uni-corporate-bold font-bold'> High quality </span> We provide support in the preparation of annotation tasks to assure high data quality. We believe that training few annotators specifically on a task to judge more instances is better than throwing many generally trained annotators on a less specified task. </li>
                                <li> <span className='font-uni-corporate-bold font-bold'> Data analysis </span> We provide a statistical analysis at the end of each annotation study. </li>
                                <li> <span className='font-uni-corporate-bold font-bold'> Open-source </span> The code implementing our platform is <Link href='https://github.com/Garrafao/phitag'><span className='underline cursor-pointer '>open source</span></Link>, allowing local deployment and easy extension to unsupported tasks. </li>
                            </ul>
                        </div>

                    </div>

                </div>
            </div>

            <div className='w-full my-32 xl:my-0 xl:h-screen flex ' >
                <div className='flex flex-col m-2 xl:w-2/3 xl:m-auto' ref={refLexicography.big}>

                    {/* First Card */}
                    <div className='flex flex-col shadow-md py-6 px-8 sm:py-12 sm:px-16'>

                        <h1 className='mb-2 font-uni-corporate-bold font-bold text-4xl' ref={refLexicography.small}>
                            PhiTag: Lexicography
                        </h1>

                        <div className='font-uni-corporate-light text-base16-gray-900 py-2'>
                            The lexicographical process of creating dictionaries is poorly automated.
                            We believe that computational semantic text annotation models from NLP can substantially improve this process!
                            That is why we added lexicographical functionalities to our text annotation platform PhiTag. Please also check our <Link href='/use-cases'><span className='underline cursor-pointer '>use cases</span></Link> for more information.
                        </div>

                        <h2 className='mt-8 mb-2 font-uni-corporate-bold font-bold text-2xl'>
                            What makes us different?
                        </h2>

                        <div className='font-uni-corporate-light text-base16-gray-900 py-2'>
                            <ul className='list-disc list-inside'>
                                <li> <span className='font-uni-corporate-bold font-bold'>Open Source </span> Other platforms like Sketch Engine are closed source and do not allow you to adapt them to your needs. PhiTag allows you to take control by local and offline deployment and you are not tied into a proprietary platform. </li>
                                <li> <span className='font-uni-corporate-bold font-bold'> Crowdsourcing </span> PhiTag allows you to crowdsource the annotation process, allowing you to get the data you need in a fast and cost-efficient way. </li>
                                <li> <span className='font-uni-corporate-bold font-bold'>Automation </span> PhiTag is built to support automation, by providing computational semantic models for the annotation process. </li>
                            </ul>
                        </div>

                    </div>


                </div>
            </div>

            <div className='w-full my-32 xl:my-0 xl:h-screen flex ' >
                <div className='flex flex-col m-2 xl:w-2/3 xl:m-auto' ref={refCollaborations.big}>

                    {/* First Card */}
                    <div className='flex flex-col shadow-md py-6 px-8 sm:py-12 sm:px-16'>

                        <h1 className='mb-2 font-uni-corporate-bold font-bold text-4xl' ref={refCollaborations.small}>
                            Collaborations
                        </h1>

                        <div className="hidden sm:flex flex-1 h-full py-2 justify-center">
                            <Link href="https://www.prolific.co/">
                                <div className="mx-4 h-64 w-64 cursor-pointer relative">
                                    <Image src={prolific} alt="Prolific" layout="fill" objectFit="contain" />
                                </div>
                            </Link>
                            <Link href="https://tags24x7.ai/">
                                <div className="mx-4 h-64 w-64 cursor-pointer relative">
                                    <Image src={tags24x7} alt="Tags24x7" layout="fill" objectFit="contain" />
                                </div>
                            </Link>
                        </div>

                    </div>


                </div>
            </div>


            <div className='w-full h-fit flex justify-center'>
                <div className='flex flex-col sm:w-2/3 my-8'>
                    <div className="mt-16 portrait:text-center font-uni-corporate-bold font-bold text-base16-gray-900 text-4xl">
                        WARNING - EARLY VERSION - DATA LOSS POSSIBLE
                    </div>

                    <div className="w-full shadow-md flex flex-col my-4 bg-base16-yellow">
                        <div className='my-8 mx-8'>
                            <div className="ml-4 font-uni-corporate-bold font-black text-lg">
                                WARNING
                            </div>
                            <div className="my-2 ml-4 font-uni-corporate-regular text-base16-gray-900">
                                <div className="" dangerouslySetInnerHTML={{ __html: warning }} />
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <BasicFooter />

            {/* Back to top */}
            <div className="flex w-16 h-16 justify-center items-center rounded-full fixed bottom-10 right-10 bg-white shadow-md cursor-pointer hover:bg-base16-gray-900 hover:text-base16-gray-100 transition-all duration-200"
                 onClick={() => refHome.current?.scrollIntoView({ behavior: 'smooth', block: 'center' })}>
                <FiArrowUp className="h-8 w-8" />
            </div>

        </div>

    )
}
