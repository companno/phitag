import React from 'react';  // Import React if not already imported
import { FiImage, FiLock } from "react-icons/fi";
import sucess from "../../../public/image/success.png";
import Image from 'next/image';

interface IProps {
    isOpen: boolean;
    onEdit: () => void;
    onSubmit: () => void;
    
}

const StudyCompletedModal: React.FC<IProps> = ({isOpen, onEdit, onSubmit}) => {
     if (!isOpen) {
        return null;
    }
    return (
        <div className="relative z-10 font-dm-mono-medium" >
            <div className="fixed inset-0 bg-base16-gray-500 bg-opacity-75" />

            <div className="fixed z-10 inset-0 overflow-y-auto">
                <div className="flex items-center justify-center min-h-full">
                    <div className="relative bg-white overflow-hidden shadow-md py-4 px-8  max-w-xl w-full">
                        <div className="mx-4">
                            <div className="flex flex-col items-left mt-6">
                                <div className="font-black text-xl font-dm-mono-medium">
                                    Thank You For Participating In Study
                                </div>
                                <div className="flex items-center justify-center my-5">
                                    <div className="w-20 h-20 cursor-pointer relative">
                                        <Image src={sucess} layout="fill" objectFit="contain" />
                                    </div>
                                </div>
                                <div className="flex flex-row divide-x-8">
                                    <button type="button" className="block w-full mt-8 py-2 bg-base16-gray-900 text-base16-gray-100" onClick={onEdit}>Edit Response</button>
                                    <button type="button" className="block w-full mt-8 py-2 bg-base16-gray-900 text-base16-gray-100" onClick={onSubmit}>Submit</button>
                                </div>

                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default StudyCompletedModal;
