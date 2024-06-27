// Icons
import { FiPlus } from "react-icons/fi";

// services
import { useFetchProject } from "../../../lib/service/project/ProjectResource";

// model
import PersonalJoblisting from "../../../lib/model/joblisting/model/PersonalJoblisting";



interface IJoblistingCardProps {
    joblisting: PersonalJoblisting;
    onClick: Function;
}

const PersonalJoblistingCard: React.FC<IJoblistingCardProps> = ({ joblisting, onClick }) => {

    const project = useFetchProject(joblisting?.getId().getOwner(), joblisting?.getId().getProject(), !!joblisting);

    if (!joblisting || project.isLoading || !project.project) {
        return <div />;
    }

    return (
        <div className="w-full shadow-md cursor-pointer hover:scale-[1.025] hover:transition-all duration-200">
            {/* TODO: Smaller screens, shrink! */}
            <div className="h-full flex flex-col grow p-8 xl:px-10 break-words font-dm-mono-regular text-base16-gray-900">
                {/* Project Name */}
                <h1 className="font-dm-mono-medium font-bold text-2xl">
                    {joblisting.getDisplayname()}
                </h1>

                <div className="my-2 text-sm">
                    <div>
                        Project: {project.project.getDisplayname()}
                    </div>
                    <div className="">
                        Language: {project.project.getLanguage().getVisiblename()}
                    </div>
                </div>

                <p className="my-2 text-sm">
                    Is Open to join for all: {joblisting.isOpen() ? "Yes" : "No"}
                </p>

                {/* User Bio */}
                <p className="my-2 text-sm">
                    Description: {joblisting.getDescription() ? joblisting.getDescription() : "No description"}
                </p>

                {joblisting.isOpen() ? <div /> :
                    <button type="button" className="block w-full mt-auto py-2 font-dm-mono-medium bg-base16-gray-900 text-base16-gray-100 " onClick={() => onClick()}>
                        Waitinglist
                    </button>
                }

            </div>
        </div>
    )
}

export default PersonalJoblistingCard;