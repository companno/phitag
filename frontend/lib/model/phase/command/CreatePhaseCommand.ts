export default class CreatePhaseCommand {

    private readonly name: string;
    private readonly owner: string;
    private readonly project: string;

    private readonly annotationType: string;
    private readonly sampling: string;

    private readonly tutorial: boolean;
    private readonly description: string;

    constructor (name: string, owner: string, project: string, annotationType: string, sampling: string, tutorial: boolean, description: string) {
        this.name = name;
        this.owner = owner;
        this.project = project;
        
        this.annotationType = annotationType;
        this.sampling = sampling;

        this.tutorial = tutorial;
        this.description = description;
    }


}