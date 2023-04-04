export default class Guide {

    public id: string;
    public title: string;

    public description: string;
    public content: string;

    constructor(id: string, title: string, description: string, content: string) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.content = content;
    }

}
