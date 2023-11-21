// Next Componenets
import Image from "next/image";
import Link from "next/link";

// Icons
import { FiLinkedin, FiLogIn, FiMail, FiMenu, FiShare2, FiUsers } from 'react-icons/fi'

// Images
import logo from "../../../public/image/logo.png";

// Custom Components
import BasicDropdownMenu from "../../generic/dropdown/basicdropdownmenu";

const BasicNavbar: React.FC<{}> = () => {

    return (
        <div className="sticky top-0 z-50 w-full  bg-uni-corporate-mittelblau text-white flex flex-row justify-between items-center">

            <div className="hidden sm:flex flex-1 h-full py-2 ">
                <Link href="https://www.uni-stuttgart.de/">
                    <div className="mx-4 h-12 w-64 cursor-pointer relative">
                        <Image src={logo} alt="Logo" layout="fill" objectFit="contain" />
                    </div>
                </Link>
            </div>
            <div className="">
                <Link href='/'>
                    <a className="flex flex-row items-center my-2 mx-4">
                        <FiShare2 className="basic-svg" />
                        <div className="ml-2 font-uni-corporate-bold font-bold text-2xl">
                            PhiTag
                        </div>
                    </a>
                </Link>
            </div>

            <div className="flex-1 flex flex-row justify-end my-2 mx-4 space-x-4">
                <Link href='https://www.linkedin.com/company/phitag/'>
                    <FiLinkedin className="basic-svg cursor-pointer" />
                </Link>

                <Link href='/about-us'>
                    <FiMail className="basic-svg cursor-pointer" />
                </Link>

                <Link href='/login'>
                    <FiLogIn className="basic-svg cursor-pointer" />
                </Link>
            </div>


        </div>
    );
}

export default BasicNavbar;