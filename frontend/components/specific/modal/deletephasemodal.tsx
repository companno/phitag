
// toast
import { toast } from "react-toastify";


import Phase from "../../../lib/model/phase/model/Phase";

// components
interface IProps {
    isOpen: boolean;
    closeModalCallBack: ()=>void;
    phase: Phase;
    deletePhase: ()=>void;
}
  

const DeletePhaseModal: React.FC<IProps> = ({isOpen, closeModalCallBack, phase, deletePhase }) => {

    const clear = () => {
        closeModalCallBack();
    }
    const onCancel = () => {
        clear();
        toast.info(`Canceled deleteing pahse: ${phase.getName()}`);
    }
   
    if (!isOpen || !phase) {
        return null;
    }

    return (
        <div className="relative z-10 font-dm-mono-medium" onClick={() => onCancel()}>

           
            <div className="fixed inset-0 bg-base16-gray-500 bg-opacity-75 " />
            <div className="fixed z-10 inset-0 overflow-y-auto">
              <div className="flex items-center justify-center min-h-full">
                    <div className="relative bg-white overflow-hidden shadow-md py-4 px-8  max-w-xl w-full" onClick={(e: any) => e.stopPropagation()}>
                        <div className="mx-4">
                            <div className="flex flex-col items-left mt-6">
                                <div className="font-black text-xl">
                                </div>
                                <div className="font-dm-mono-regular my-2">
                                    You are about to delete phase:{phase.getDisplayname()}.
                                </div>

                                <div className="flex flex-col items-left my-6">
                                    <div className="font-bold text-lg">
                                    Warning: Deleting a {phase.getDisplayname()} will result in the removal of all associated instances and judgements data.
                                    </div>
                                  
                                </div>

                            </div>
                        </div>
                        <div className="flex flex-row divide-x-8 bg">
                       {/*  <button className=" m-4 flex items-center  hover:bg-blue-300 focus:outline-none
                                             focus:ring focus:border-blue-300 active:transform active:scale-95 text-black  font-bold py-2 px-4 rounded">
                                                Test
                                             </button> */}
                            <button type="button" 
                            className="active:transform active:scale-95
                            block w-full  mt-8 bg-base16-gray-900 text-base16-gray-100 " onClick={onCancel}>Cancel</button>
                           
                            <button type="button" 
                            className="active:transform active:scale-95 
                            block w-full mt-8 py-2 bg-base16-gray-900 text-base16-gray-100 " onClick={deletePhase}>Delete</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>

    )
}

export default DeletePhaseModal;