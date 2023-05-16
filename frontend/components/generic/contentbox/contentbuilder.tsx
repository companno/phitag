import StaticData from '../../../lib/model/staticdata/staticdata'
import ContentImageBox from "./contentimagebox";
import MultiContentBox from "./multicontentbox";

interface IProps {
    data: StaticData | StaticData[];
}

const ContentBuilder: React.FC<IProps> = ({ data }) => {

    if (data instanceof Array) {
        return (
            <div className="mt-4 flex flex-col lg:flex-row lg:space-x-4 space-y-4 lg:space-y-0 lg:my-4">
                {
                    data.map((item, index) => {
                        return (
                                <MultiContentBox key={index} data={item} />
                        );
                    })
                }
            </div>
        );
    }

    if (data.imagepath !== "") {
        return (
            <div className="mt-4">
                <ContentImageBox data={data} />
            </div>
        )
    }

    return (
        <div className="mt-4">
            <MultiContentBox data={data} />
        </div>
    );

}

export default ContentBuilder;