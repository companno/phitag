export default class CreateJoblistingCommand {
    private readonly name: string;
    private readonly owner: string;
    private readonly project: string;

    private readonly open: boolean;
    private readonly description: string;

    constructor(name: string, owner: string, project: string, open: boolean, description: string) {
        this.name = name;
        this.owner = owner;
        this.project = project;
        
        this.open = open;
        this.description = description;
    }
}