import { CSS } from "@dnd-kit/utilities";

// Usage
import { useSortable } from "@dnd-kit/sortable";
import { toast } from "react-toastify";

interface IProps {
    rank: any,
    rankId: string
}

const SortableJudgements:React.FC<IProps> = ({rank, rankId}) => {



    const { attributes, listeners, setNodeRef, transform, transition } = useSortable({
        id: rankId
    });

    const styles = {
        transform: CSS.Transform.toString(transform),
        transition,
        cursor: 'grab',
    };
    return (
        <div className="shadow-md" style={styles} ref={setNodeRef} {...attributes} {...listeners}>
            <div  className="text-center p-3 mt-2 mr-2 mb-2 ml-2 active:transform 
              active:scale-95 flex  cursor-pointer transition-all duration-200 font-dm-mono-medium  items-center">
                {rank}
            </div></div>
    )

}

export default SortableJudgements;
